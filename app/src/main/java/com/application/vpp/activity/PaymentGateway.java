package com.application.vpp.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.application.vpp.Datasets.Order;
import com.application.vpp.Datasets.RazorpayCheckoutReqRes;
import com.application.vpp.Interfaces.APiValidateAccount;
import com.application.vpp.NetworkCall.APIClient;
import com.application.vpp.NetworkCall.TLSSocketFactory;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Validate_Signature_Class;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentGateway extends AppCompatActivity implements PaymentResultWithDataListener {


    APiValidateAccount apiService;
    Button button;
    String checkout_req_result = "0", checkout_res_result = "0", order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentgateway);
        try {
            new TLSSocketFactory();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        apiService = new APIClient().getClient2().create(APiValidateAccount.class);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();
            }
        });
        try {
            new TLSSocketFactory();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    void get() {
        String VppId= Logics.getVppId(PaymentGateway.this);
        //Toast.makeText(getApplicationContext(),"Check Point 1",Toast.LENGTH_SHORT).show();
        apiService = new APIClient().getClient2().create(APiValidateAccount.class);
        Log.i("before call", "check");

        Order order = new Order();
        order.setVpp_id("11019");
        order.setAmount("45000");
        order.setCurrency("INR");
        order.setReceipt("order_rcpid_101");
        order.setPayment_capture(true);
        order.setMobile_app_version("1.0.1");
        order.setCreated_by("VPP");


        Call<Order> create = apiService.createOrder(order);
        //Toast.makeText(getApplicationContext(),"Check Point 1",Toast.LENGTH_SHORT).show();
        create.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                System.out.println("response");
                Order order = response.body();
//                Toast.makeText(getApplicationContext(), "Check Point 1" + order.toString(), Toast.LENGTH_LONG).show();
//                Log.i("++++++++++++++++ ", order.toString());
                startPayment(order);

            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Check Point 1 Error " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("+++++++++++++++++++ ", t.getMessage());
            }
        });


    }


    public void startPayment(Order order) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();


        checkout.setKeyID("rzp_test_EczI8f0j4DcYeL");    //rzp_test_6FNVBJTY2HFCBu

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.ic_launcher_background);

        /**
         * Reference to current activity
         */
        final PaymentGateway activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Ventura Securities Ltd.");
            options.put("description", "VPP Registration Fees");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", order.getCurrency());
            options.put("amount", order.getAmount());// amount in paisa
            options.put("order_id", order.getId());
            options.put("vpp_id",  order.getVpp_id());

            /*phone number and email edittext will filled with below data*/
            JSONObject preFill = new JSONObject();
            preFill.put("email", "bipin.pandey@ventura1.com");
            preFill.put("contact", "8082369498");

            //options.put("prefill", preFill);

            Iterator x = options.keys();
            JSONArray jsonArray = new JSONArray();

            while (x.hasNext()){
                String key = (String) x.next();
                jsonArray.put(options.get(key));
            }

            RazorpayCheckoutReqRes checkoutReqRes= new RazorpayCheckoutReqRes();
            checkoutReqRes.setOrder_id(order.getId());
            checkoutReqRes.setVpp_id("00008");
            checkoutReqRes.setRequest(jsonArray.toString());

            SaveCheckoutRequest(checkoutReqRes);
            order_id = order.getId();
            /*if(result.equalsIgnoreCase("")){
                checkout.open(activity, options);
            }*/

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("error", "Error in starting Razorpay Checkout", e);
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        Checkout.clearUserData(getApplicationContext());
        Log.e("success", "onPaymentSuccess: " + s + "  paymentdata===" + paymentData.toString());
        String paymentId = paymentData.getPaymentId();
        String signature = paymentData.getSignature();
        String orderId = paymentData.getOrderId();
        Log.e("success", "paymentId: " + paymentId + "  signature===" + signature + "  orderId==" + orderId);

        try {
            RazorpayCheckoutReqRes checkoutReqRes= new RazorpayCheckoutReqRes();
            checkoutReqRes.setRazorpay_payment_id(paymentId);
            checkoutReqRes.setPayment_status(signature);
            checkoutReqRes.setRazorpay_order_id(orderId);
            checkoutReqRes.setOrder_id(order_id);

            SaveCheckoutResponse(checkoutReqRes);

            order_id = null;

            Validate_Signature_Class validate_signature_class = new Validate_Signature_Class(paymentId, orderId, signature);

            validate_signatrue_by_server(validate_signature_class);

        } catch (Exception e) {

            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

        Checkout.clearUserData(getApplicationContext());
        Log.e("error", "int value ==" + i + "  onPaymentSuccess: " + s + "  paymentdata===" + paymentData);
        String paymentId = paymentData.getPaymentId();
        String signature = paymentData.getSignature();
        String orderId = paymentData.getOrderId();
        Log.e("success", "paymentId: " + paymentId + "  signature===" + signature + "  orderId==" + orderId);


    }

    void validate_signatrue_by_server(Validate_Signature_Class validate_signature_class) {

        Call<String> validateSignature = apiService.Validate_Signature(validate_signature_class);
        validateSignature.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e("success", "" + response.toString());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("failure", "" + call.toString() + "   throwable===" + t.toString());
            }
        });


    }

    String SaveCheckoutRequest(RazorpayCheckoutReqRes checkoutReqRes) {
        Log.i("++++++++Request ", checkoutReqRes.toString());
        Call<String> saveCheckoutRequest = apiService.SaveCheckout(checkoutReqRes);
        saveCheckoutRequest.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                checkout_req_result = response.toString();
                Log.i("++++Response ", checkout_req_result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        return checkout_req_result;
    }

    String SaveCheckoutResponse(RazorpayCheckoutReqRes checkoutReqRes) {
        //Log.i("++++Response ", checkoutReqRes.toString());
        Call<String> saveCheckoutResponse = apiService.SaveCheckout(checkoutReqRes);
        saveCheckoutResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                checkout_res_result = response.toString();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        return checkout_res_result;
    }
}
