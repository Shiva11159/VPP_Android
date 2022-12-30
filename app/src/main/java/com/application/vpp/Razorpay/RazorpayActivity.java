package com.application.vpp.Razorpay;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.application.vpp.activity.NavigationDrawer;
import com.application.vpp.activity.UpiPayment;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.ConfirmPayment;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Views.Views;
import com.google.android.material.textfield.TextInputEditText;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.sdsmdg.tastytoast.TastyToast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RazorpayActivity extends NavigationDrawer implements
        ConfirmPayment,RequestSent,ConnectionProcess, PaymentResultWithDataListener {
    TextInputEditText editText_Vppid, editText_amount;
    AutoCompleteTextView autoCompleteTextView;
    String vppid_str, state_str,creatorderid;
    Button button;
    public static Handler handlerrazorpay;
    RequestSent requestSent;
    boolean checkcreateorder=false;
    boolean checkamount=false;
    boolean checkvalidatesignature=false;
    boolean checksaverequest=false;
    boolean checksaveresponse=false;
    boolean checkbalance=false;
    ConfirmPayment confirmPayment;
    SweetAlertDialog pDialog;
    String order_id;
    JSONObject jsonObjectresponse,jsonObjectrequest;
    String paymentId,orderId,signature;
    ConnectionProcess connectionProcess;
//    public static SweetAlertDialog pDialog;
    String amount_str="";
    BigDecimal v = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_razorpay,mDrawerLayout);
        UpiPayment.upiHandler=null;
        RazorpayActivity.handlerrazorpay=null;
        confirmPayment=(ConfirmPayment) this;
        handlerrazorpay = new ViewHandler();
        requestSent = (RequestSent) this;
        connectionProcess = (ConnectionProcess) this;
        /*editText_amount = findViewById(R.id.editText_amount);
        editText_Vppid = findViewById(R.id.editText_Vppid);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
       */ button = findViewById(R.id.button);
       /* String[] statesFromResource=getResources().getStringArray(R.array.States);
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,statesFromResource);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(1);
        String selectedState=autoCompleteTextView.getText().toString().toUpperCase();
        System.out.println("select"+selectedState);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                Toast.makeText(RazorpayActivity.this, String.valueOf(arg0.getItemAtPosition(arg2)), Toast.LENGTH_SHORT).show();
                state_str= String.valueOf(arg0.getItemAtPosition(arg2));
                Methods.hideKeyboard(getApplicationContext(),arg1);
            }
        });

*/

        state_str = SharedPref.getPreferences(this, "state");
        Toast.makeText(RazorpayActivity.this, state_str , Toast.LENGTH_SHORT).show();
        if (Connectivity.getNetworkState(getApplicationContext()))
            CALLAMOUNT();
        else
            Views.SweetAlert_NoDataAvailble(RazorpayActivity.this,"No Internet");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callPinocde();
                //vppid_str = editText_Vppid.getText().toString().toUpperCase();
//                amount_str = editText_amount.getText().toString().toUpperCase();
//                state_str = editText_state.getText().toString().toUpperCase();
//                SharedPref.savePreferences(getApplicationContext(), SharedPref.vppid, vppid_str);

                    SharedPref.savePreferences(getApplicationContext(), SharedPref.state, state_str);
                    if (amount_str.equalsIgnoreCase("")){
                        CALLAMOUNT();
                    }else
                    CallcreateOrder();

            }
        });
    }

    private void CallcreateOrder() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("vpp_id", Logics.getVppId(RazorpayActivity.this));

            jsonObject.put("amount", amount_str);
            jsonObject.put("currency", "INR");
            jsonObject.put("receipt", "order_rcpid_101");
            jsonObject.put("payment_capture", "true");
            jsonObject.put("mobile_app_version", "1.0.1");
            jsonObject.put("created_by", "VPP");
            Log.e("RequestcreateOrder", jsonObject.toString());
            byte data[] = jsonObject.toString().getBytes();

            if (pDialog == null) {
                pDialog = new SweetAlertDialog(RazorpayActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            }
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();
            new SendTOServer(RazorpayActivity.this, requestSent, Const.MSGCREATEORDEROBJ, data,connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestSent(int value) {
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Checkout.clearUserData(getApplicationContext());
        Log.e("success", "onPaymentSuccess: " + s + "  paymentdata===" + paymentData);
         paymentId = paymentData.getPaymentId();
        signature = paymentData.getSignature();
         orderId = paymentData.getOrderId();
        Log.e("success", "paymentId: " + paymentId + "  signature===" + signature + "  orderId==" + orderId);
        try {
            jsonObjectresponse = new JSONObject();
            //String vppid= Logics.getVppId(MyLeads.this);
            jsonObjectresponse.put("razorpay_payment_id", paymentId);
            jsonObjectresponse.put("payment_status", signature);
            jsonObjectresponse.put("razorpay_order_id",orderId);
            jsonObjectresponse.put("order_id",creatorderid);

            CallSAVECHECKOUT(jsonObjectresponse);

            CallVALIDATESIGNATURE(paymentId,orderId,signature);

        } catch (Exception e) {

            e.printStackTrace();
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

    @Override
    public void Confirm() {
        TastyToast.makeText(getApplicationContext(),"Payment done",TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
        ///here we have to call activity....
    }
    public class ViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (pDialog != null) {
                pDialog.dismiss();
            }
            String data = (String) msg.obj;
            int msgCode = msg.arg1;
            Log.e("data ",data);
            switch (msgCode) {
                case Const.MSGCREATEORDEROBJ:
//                    Toast.makeText(RazorpayActivity.this, data, Toast.LENGTH_SHORT).show();
                    Log.e("datacreateorder", data);
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        String datastr= jsonObject.getString("data");
                        if (!datastr.equalsIgnoreCase("FAIL")){
                            JSONObject jsonObject1=new JSONObject(datastr);
                            creatorderid=jsonObject1.getString("id");
                            startPayment(creatorderid);
                            checkcreateorder=true;
                        }else {
                            Toast.makeText(RazorpayActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Log.e("exception", e.getMessage());
                    }
                    break;
                case Const.MSGVALIDATESIGNATURE:
//                    Toast.makeText(RazorpayActivity.this, data, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        if (jsonObject.getString("data").equalsIgnoreCase("SUCCESS")){
                            checkvalidatesignature=true;
                            //Views.ProgressDlgPaymentDone(RazorpayActivity.this,confirmPayment,amount_str);
                            v = BigDecimal.valueOf(Long.valueOf(SharedPref.getPreferences(RazorpayActivity.this, "amount")), 2);
                            Views.ProgressDlgPaymentDone(RazorpayActivity.this, confirmPayment, String.format("%.2f", v));

                        }
                    }catch (Exception e){
                        Log.e("exception", e.getMessage());
                    }
                    break;
                case Const.MSGSAVECHECKOUT:
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        if (jsonObject.getString("data").equalsIgnoreCase("Success")){
//                            checkcreateorder=tr
                        }
                    }catch (Exception e){
                        Log.e("exception", e.getMessage());
                    }
                    break;
                case Const.MSGMOUNT:
                    try {
                        checkamount=true;
                        JSONObject jsonObject=new JSONObject(data);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")){
                            amount_str=jsonObject.getString("amount");
                            SharedPref.savePreferences(getApplicationContext(), SharedPref.amount, amount_str);

                        }else {
                            Toast.makeText(RazorpayActivity.this, "amount fetch falied..", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Log.e("exception", e.getMessage());
                    }
                    break;
            }

        }
    }

    public void startPayment(String id) {
        /**
         * Instantiate Checkout
         */

    //    key_id	                    key_secret
  //      rzp_test_wzgnSUypkltsAF	        QiLifBMAECa6nSlNvO58OQRa

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_wzgnSUypkltsAF");    //rzp_test_6FNVBJTY2HFCBu,  rzp_test_EczI8f0j4DcYeL, rzp_test_Gl1wC32N4vKBF5

        //rzp_live_3fJw8WVyANEt4w -- live key
        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.ic_launcher_background);
        /**
         * Reference to current activity
         */
        final RazorpayActivity activity = this;
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Ventura Securities Ltd.");
            options.put("description", "VPP Registration Fees");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", SharedPref.getPreferences(getApplicationContext(),SharedPref.amount));// amount in paisa
            options.put("order_id", id);
            options.put("vpp_id", Logics.getVppId(RazorpayActivity.this));

            /*phone number and email edittext will filled with below data*/
            JSONObject preFill = new JSONObject();
            preFill.put("email", Logics.getEmail(RazorpayActivity.this));
            preFill.put("contact", Logics.getMobile_1(RazorpayActivity.this));
            //options.put("prefill", preFill);

            Iterator x = options.keys();
            JSONArray jsonArray = new JSONArray();

            while (x.hasNext()) {
                String key = (String) x.next();
                jsonArray.put(options.get(key));
            }

             jsonObjectrequest = new JSONObject();
            //String vppid= Logics.getVppId(MyLeads.this);
            jsonObjectrequest.put("order_id", id);
         //   jsonObjectrequest.put("vpp_id", "1100");
            jsonObjectrequest.put("vpp_id",Logics.getVppId(RazorpayActivity.this));
            //jsonObjectrequest.put("vpp_id", SharedPref.getPreferences(getApplicationContext(),SharedPref.vppid));
            jsonObjectrequest.put("request", jsonArray.toString());

            CallSAVECHECKOUT(jsonObjectrequest);
            order_id = id;
            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("error", "Error in starting Razorpay Checkout", e);
        }
    }

    private void CallVALIDATESIGNATURE(String paymentId,String orderId,String signature) {
//        ringProgressDialog = Views.showDialog(this);
        try {
            JSONObject jsonObject = new JSONObject();
            //String vppid= Logics.getVppId(MyLeads.this);
            jsonObject.put("razorpay_payment_id", paymentId);
            jsonObject.put("razorpay_order_id", orderId);
            jsonObject.put("signature_by_checkout", signature);
            jsonObject.put("vpp_id", Logics.getVppId(RazorpayActivity.this));
            jsonObject.put("amount", SharedPref.getPreferences(getApplicationContext(),SharedPref.amount));
            jsonObject.put("client_state", state_str);
            jsonObject.put("pan_no", Logics.getPanNo(this));

            Log.e("VALIDATESIGNATURE", jsonObject.toString());
            byte data[] = jsonObject.toString().getBytes();
//            if (Const.isSocketConnected) {
//                Toast.makeText(this, String.valueOf(Const.isSocketConnected), Toast.LENGTH_SHORT).show();
//            } else {
//                if (ringProgressDialog != null) {
//                    ringProgressDialog.dismiss();
//                }
//                Views.toast(this, "Check Internet Connection ");
//            }


            if (pDialog == null) {
                pDialog = new SweetAlertDialog(RazorpayActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            }
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();
            new SendTOServer(RazorpayActivity.this, requestSent, Const.MSGVALIDATESIGNATURE, data,connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CallSAVECHECKOUT(JSONObject jsonObject) {
//        ringProgressDialog = Views.showDialog(this);
        Log.e("SAVECHECKOUT", jsonObject.toString());
        byte data[] = jsonObject.toString().getBytes();

        if (pDialog == null) {
            pDialog = new SweetAlertDialog(RazorpayActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        }
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();
        new SendTOServer(RazorpayActivity.this, requestSent, Const.MSGSAVECHECKOUT, data,connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        if (Const.isSocketConnected) {
//            Toast.makeText(this, String.valueOf(Const.isSocketConnected), Toast.LENGTH_SHORT).show();
//        } else {
////            if (pDialog != null) {
////                pDialog.dismiss();
////            }
//            Views.toast(this, "Check Internet Connection ");
//        }
    }


    @Override
    public void connected() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
            pDialog.cancel();
        }

        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!checkamount){
                    CALLAMOUNT();
                }else if (!checkcreateorder){
                    if (amount_str.equalsIgnoreCase("")){
                        CALLAMOUNT();
                    }else
                        CallcreateOrder();
                }else if (!checkvalidatesignature){
                    CallVALIDATESIGNATURE(paymentId,orderId,signature);
                }

                /*else if (checksaverequest){

                    CallSAVECHECKOUT(jsonObjectrequest);
                }else if (checksaveresponse){
                    CallSAVECHECKOUT(jsonObjectresponse);
                }*/
//                else if (checkbalance){
//                    CALLAMOUNT();
//                }
              //  TastyToast.makeText(RazorpayActivity.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
            pDialog.cancel();
        }

        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(RazorpayActivity.this, connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(RazorpayActivity.this, "nullserver", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
        pDialog.dismiss();
        pDialog.cancel();
        Views.SweetAlert_NoDataAvailble(RazorpayActivity.this, "Internet Not Available");
        //        if (pDialog.isShowing()) {
        //            pDialog.dismiss();
        //            pDialog.cancel();
        //        }
        Log.e("IntenrnetNotAvailable: ", "internet");

        //        AlertDailog.ProgressDlgDiss();
        //        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ConnectToserver(final ConnectionProcess connectionProcess) {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog.cancel();
        }
        Log.e("ConnectToserver11: ", "ConnectToserver11");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(RazorpayActivity.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    Views.SweetAlert_NoDataAvailble(RazorpayActivity.this,"No Internet");
            }
        }, 2000);
    }

    @Override
    public void SocketDisconnected() {
        pDialog.dismiss();
        pDialog.cancel();
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(RazorpayActivity.this, connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(RazorpayActivity.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CALLAMOUNT() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("", "");
//            Log.e("CALLAMOUNT", jsonObject.toString());
            byte data[] = jsonObject.toString().getBytes();

            if (pDialog == null) {
                pDialog = new SweetAlertDialog(RazorpayActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            }
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();
            new SendTOServer(RazorpayActivity.this, requestSent, Const.MSGMOUNT, data,connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
