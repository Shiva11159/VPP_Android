package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Interfaces.ConfirmPayment;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.GstProceed;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Utility.Signature;
import com.application.vpp.Views.Views;
import com.application.vpp.other.GstDlg;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.paynimo.android.payment.PaymentActivity;
import com.paynimo.android.payment.PaymentModesActivity;
import com.paynimo.android.payment.util.Constant;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

import static com.application.vpp.Const.Const.MSGTECHPROCRESP;

//import com.application.vpp.Razorpay.RazorpayActivity;

public class UpiPayment extends AppCompatActivity implements View.OnClickListener, RequestSent,
        ConfirmPayment, ConnectionProcess, PaymentResultWithDataListener, GstProceed {
    boolean gotResponse = false;
    boolean socketreponse = false;

    boolean checkvalidatesignature = false;
    boolean checkcreateorder = false;
    boolean checkamount = false;
    JSONObject jsonObjectresponse, jsonObjectrequest;
    String amount_str = "", creatorderid, order_id, paymentId, orderId, signature;

    FragmentManager fm = getSupportFragmentManager();

    @BindView(R.id.pay_later)
    TextView pay_later;

    @BindView(R.id.checkbox_gst)
    CheckBox checkbox_gst;
    AlertDialog alertDialog;

//    SweetAlertDialog pDialog;

    @BindView(R.id.buttonRazorpay1)
    Button buttonRazorpay;

    @BindView(R.id.btnTechprocess)
    Button btnTechprocess;

    @BindView(R.id.linearlayout_upipayment)
    LinearLayout linearlayout_upipayment;
    // @BindView(R.id.linearlayout_state_autocomplete)
    // LinearLayout linearlayout_state_autocomplete;
    @BindView(R.id.editText_Vppid)
    EditText editText_Vppid;
    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.relative1)
    RelativeLayout relative1;
    ConfirmPayment confirmPayment;
    //private static final int TEZ_REQUEST_CODE = 123;
    public static final int UPI_PAYMENT = 0;
    private String secret_id = "Gqt9RxHaSOih3NbrZN8ZWlNF";

    RequestSent requestSent;
    ConnectionProcess connectionProcess;

    // private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    final int GOOGLE_PAY_REQUEST_CODE = 123;
    public static Handler upiHandler;
    //ProgressDialog ringProgressDialog;
    String status = "";
    String txnId = "";
    String responseCode = "";
    String approvalRefNo = "";
    com.paynimo.android.payment.model.Checkout checkout;
    AutoCompleteTextView autoCompleteTextView;
    Button button;
    public static Handler techprocessHandler;
    int isDocument = 0;
    String state_str = "";
    String created_date = "";
    int PaymentStatus = 0;
    String PaymentMessage = "";
    com.paynimo.android.payment.model.Checkout checkout_res;
    String transactionDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String total_response_techprocess = "";
    String accno = "";
    BigDecimal v = null;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;

    boolean callamount = false;
    boolean MSGTECHPROCREQ_ = false;
    boolean MSGTECHPROCRESP_ = false;
    boolean CallSaveTechProcessRequest_HIT = false;

    public static View customView;
    public static PopupWindow popupWindow;

    GstProceed gstProceed;
    GstDlg gstDlg;

    int MaxTry = 0;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi);
        //autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        // button = findViewById(R.id.button);
        ButterKnife.bind(this);
        UpiPayment.upiHandler = null;
        // RazorpayActivity.handlerrazorpay = null;
        UpiPayment.techprocessHandler = null;
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        upiHandler = new ViewHandler();
        techprocessHandler = new ViewHandler();
        gstProceed = (GstProceed) this;
        pay_later.setOnClickListener(this);
        buttonRazorpay.setOnClickListener(this);
        btnTechprocess.setOnClickListener(this);

        // button.setOnClickListener(this);
        confirmPayment = (ConfirmPayment) this;

        Date date = new Date();

        transactionDate = dateFormat.format(date);
        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList, "SocketLogs", UpiPayment.this);

        accno = SharedPref.getPreferences(UpiPayment.this, "bankAccNo") != null ? SharedPref.getPreferences(UpiPayment.this, "bankAccNo") : "";

        if (getIntent().getExtras() != null) {
            isDocument = getIntent().getExtras().getInt("isDocument", 0);

          /* if (isDocument == 1) {
                linearlayout_state_autocomplete.setVisibility(View.VISIBLE);

                String[] statesFromResource=getResources().getStringArray(R.array.States);
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
                        SharedPref.savePreferences(getApplicationContext(), SharedPref.state, state_str);
                    }
                });

            } else {*/
            linearlayout_upipayment.setVisibility(View.VISIBLE);
            state_str = SharedPref.getPreferences(this, "state");
            //Toast.makeText(UpiPayment.this, state_str , Toast.LENGTH_SHORT).show();
            //  }
        }

        CALLAMOUNT();
        // v = BigDecimal.valueOf(Long.valueOf(SharedPref.getPreferences(UpiPayment.this,"amount")),2);
        //String totalamount = String.format("%.2f",v);

    }

    //only for android 11
    public void open(){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Dear partner, our eSign feature is under development, request you to skip this and complete it after a week ");
        alertDialogBuilder.setPositiveButton("Skip",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(UpiPayment.this, Dashboard.class);
                        startActivity(intent);
                    }
                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button: {
                linearlayout_upipayment.setVisibility(View.VISIBLE);
                //linearlayout_state_autocomplete.setVisibility(View.GONE);
            }
            break;
            case R.id.pay_later: {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    open();
//                }else{
//                    Intent intent = new Intent(UpiPayment.this, PhotoVideoSignatureActivity.class);
//                    //  Intent intent = new Intent(UpiPayment.this, Dashboard.class);
//                    startActivity(intent);
//                    SharedPref.savePreferences(getApplicationContext(), SharedPref.UPIPayment, "");
//                }

                Intent intent = new Intent(UpiPayment.this, PhotoVideoSignatureActivity.class);
                //  Intent intent = new Intent(UpiPayment.this, Dashboard.class);
                startActivity(intent);
                SharedPref.savePreferences(getApplicationContext(), SharedPref.UPIPayment, "");
            }
            break;

            case R.id.btnTechprocess: {

                if (Connectivity.getNetworkState(getApplicationContext())) {

                    if (SharedPref.getPreferences(UpiPayment.this, "amount") != null ||
                            !SharedPref.getPreferences(UpiPayment.this, "amount").equalsIgnoreCase("")) {

                        CallSaveTechProcessRequest();
                    } else {
                        TastyToast.makeText(UpiPayment.this, "Please try again", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        CALLAMOUNT();
                    }

                } else {
                    Views.SweetAlert_NoDataAvailble(UpiPayment.this, "Connect internet !");
                }


            }
            break;

            case R.id.buttonRazorpay1: {
//
                if (Connectivity.getNetworkState(getApplicationContext())) {
                    if (SharedPref.getPreferences(UpiPayment.this, "amount") != null ||
                            !SharedPref.getPreferences(UpiPayment.this, "amount").equalsIgnoreCase("")) {


                        //call here .. gst .


//                    PopupWindowShowGST(UpiPayment.this,relative1);

                        if (checkbox_gst.isChecked()) {
                            Bundle bundle = new Bundle();
//                            bundle.putString("id", "");
//                            GstDlg resourse_share = new GstDlg();
                            gstDlg = GstDlg.newInstance(gstProceed,UpiPayment.this);
                            gstDlg.show(fm, "Dialog Fragment");
//                            gstDlg.setArguments(bundle);
                        } else {
                            CallcreateOrder();
                        }

                    } else {
                        TastyToast.makeText(UpiPayment.this, "Please try again", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
//                        CALLAMOUNT();
                    }
//
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

     /*   if (requestCode == TEZ_REQUEST_CODE) {
            // Process based on the data in response.
            Log.e("result", data.getStringExtra("Status"));
        }*/


        try {
            Log.e("main ", "response " + resultCode);
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
      E/UPI: payment successfull: 922118921612
         */
            switch (requestCode) {
                // Check which request we're responding to
                case PaymentActivity.REQUEST_CODE:
                    if (requestCode == PaymentActivity.REQUEST_CODE) {
                        // Make sure the request was successful


                        if (resultCode == PaymentActivity.RESULT_OK) {
                            Log.e("UpiPayment", "Result Code :" + RESULT_OK);
                            if (data != null) {
                                try {
                                    checkout_res = (com.paynimo.android.payment.model.Checkout) data
                                            .getSerializableExtra(Constant
                                                    .ARGUMENT_DATA_CHECKOUT);
                                    Log.d("Checkout Response Obj", checkout_res
                                            .getMerchantResponsePayload().toString());
                                    // below is the response getting from techprocess.
//                        ResponsePayload [merchantCode=T585181, merchantTransactionIdentifier=TXN001, merchantTransactionRequestType=TAR,
//                        responseType=web, transactionState=F, merchantAdditionalDetails={email:pravintest88@gmail.com}{mob:9975153610},
//                        paymentMethod=PaymentMethod [token=, instrumentAliasName=, instrumentToken=, bankSelectionCode=470,
//                        aCS=ACS [bankAcsFormName=net, bankAcsHttpMethod=post, bankAcsParams=[], bankAcsUrl=],
//                        oTP=OTP [initiator=, message=, numberOfDigit=, target=, type=],
//                        paymentTransaction=PaymentTransaction [amount=1.00, balanceAmount=, bankReferenceIdentifier=, dateTime=12-08-2020 14:54:25,
//                        errorMessage=, identifier=1196235303, refundIdentifier=, statusCode=0300, statusMessage=success, instruction=Instruction
//                        [id=, statusCode=, errorcode=, errordesc=]], authentication=Authentication [type=, subType=], error=Error [code=, desc=]]]
                                    String transactionSubType = checkout_res.getMerchantRequestPayload().getTransaction().getSubType();
                                    System.out.println("Payment type => " + transactionSubType);
                                    // Transaction Completed and Got SUCCESS
                                    if (checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusCode().equalsIgnoreCase(
                                            PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS)) {
                                        Toast.makeText(getApplicationContext(), "Transaction Status - Success", Toast.LENGTH_SHORT).show();
                                        Log.e("TRANSACTION STATUS=>", "SUCCESS");
                                        System.out.println("TRANSACTION_STATUS_SALES_DEBIT_SUCCESS");
                                        /**
                                         * TRANSACTION STATUS - SUCCESS (status code
                                         * 0300 means success), NOW MERCHANT CAN PERFORM
                                         * ANY OPERATION OVER SUCCESS RESULT
                                         */

                                        if (checkout_res.getMerchantResponsePayload().
                                                getPaymentMethod().getPaymentTransaction().
                                                getInstruction().getId() != null && checkout_res.getMerchantResponsePayload().
                                                getPaymentMethod().getPaymentTransaction().
                                                getInstruction().getId().isEmpty()) {
                                            Log.v("TRANSACTION SI STATUS=>",
                                                    "SI Transaction Not Initiated");
                                            System.out.println("TRANSACTION SI  SI Transaction Not Initiated");
                                        } else if (checkout_res.getMerchantResponsePayload().
                                                getPaymentMethod().getPaymentTransaction().
                                                getInstruction().getId() != null && !checkout_res.getMerchantResponsePayload().
                                                getPaymentMethod().getPaymentTransaction().
                                                getInstruction().getId().isEmpty()) {
                            /*
                              SI TRANSACTION STATUS - SUCCESS (Mandate  Registration ID received means success)
                             */
                                            System.out.println("TRANSACTION SI SUCCESS (Mandate  Registration ID received means success)");
                                            Log.v("TRANSACTION SI STATUS=>", "SUCCESS");
                                        }
                                    } else if (checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusCode().equalsIgnoreCase(
                                                    PaymentActivity.TRANSACTION_STATUS_DIGITAL_MANDATE_SUCCESS
                                            )) {
                                        Toast.makeText(getApplicationContext(), "Transaction Status - Success", Toast.LENGTH_SHORT).show();
                                        Log.v("TRANSACTION STATUS=>", "SUCCESS");
                                        System.out.println("TRANSACTION_STATUS_DIGITAL_MANDATE_SUCCESS");
                                        /**
                                         * TRANSACTION STATUS - SUCCESS (status code
                                         * 0398 means success), NOW MERCHANT CAN PERFORM
                                         * ANY OPERATION OVER SUCCESS RESULT
                                         */

                                        PaymentMessage = "TRANSACTION_STATUS_DIGITAL_MANDATE_SUCCESS";
                                        if (checkout_res.getMerchantResponsePayload().
                                                getPaymentMethod().getPaymentTransaction().
                                                getInstruction().getId() != null
                                                && !checkout_res
                                                .getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getInstruction().getId().isEmpty()) {
                                            /**
                                             * SI TRANSACTION STATUS - SUCCESS (status
                                             * code 0300 means success)
                                             */
                                            Log.v("TRANSACTION SI STATUS=>",
                                                    "INITIATED");
                                            System.out.println("Transaction Digital Mandate Success");
                                            PaymentMessage = "Transaction Digital Mandate Success";
                                        } else {
                                            System.out.println("Transaction Digital Mandate Failure");
                                            /**
                                             * SI TRANSACTION STATUS - Failure (status
                                             * code OTHER THAN 0300 means failure)
                                             */
                                            Log.v("TRANSACTION SI STATUS=>", "FAILURE");
                                            PaymentMessage = "Transaction Digital Mandate Failure";
                                        }
                                    } else {
                                        System.out.println("Bank Error Failure");
                                        // some error from bank side
                                        Log.v("TRANSACTION STATUS=>", "FAILURE");
                                        Toast.makeText(getApplicationContext(),
                                                "Transaction Status - Failure",
                                                Toast.LENGTH_SHORT).show();
                                        PaymentMessage = "Transaction Status - Failure";
                                    }
                                    String umrnNo = "";
                                    String addDetails = checkout_res
                                            .getMerchantResponsePayload().getMerchantAdditionalDetails();
                                    if (addDetails != null && addDetails.contains("UMRNNumber")) {
                                        String[] arrMandateData = addDetails.split("mandateData");
                                        if (arrMandateData != null && arrMandateData.length > 1) {
                                            String[] arrMandateParams = arrMandateData[1].split("~");
                                            if (arrMandateParams != null
                                                    && arrMandateParams.length > 1) {
                                                String[] arrUMRN = arrMandateParams[0].split(":");
                                                if (arrUMRN != null && arrUMRN.length > 1) {
                                                    umrnNo = arrUMRN[1];
                                                }
                                            }
                                        }
                                    }

                                    String result = "StatusCode : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getStatusCode()
                                            + "\nStatusMessage : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getStatusMessage()
                                            + "\nErrorMessage : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getErrorMessage()
                                            + "\nAmount : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount()
                                            + "\nDateTime : " + checkout_res.
                                            getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getDateTime()
                                            + "\nMerchantTransactionIdentifier : "
                                            + checkout_res.getMerchantResponsePayload()
                                            .getMerchantTransactionIdentifier()
                                            + "\nIdentifier : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getIdentifier()
                                            + "\nBankSelectionCode : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getBankSelectionCode()
                                            + "\nBankReferenceIdentifier : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getBankReferenceIdentifier()
                                            + "\nRefundIdentifier : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getRefundIdentifier()
                                            + "\nBalanceAmount : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getBalanceAmount()
                                            + "\nInstrumentAliasName : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getInstrumentAliasName()
                                            + "\nSI Mandate Id : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getInstruction().getId()
                                            + "\nUMRN No : "
                                            + umrnNo

                                            + "\nSI Mandate Status : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getInstruction().getStatusCode()
                                            + "\nSI Mandate Error Code : " + checkout_res
                                            .getMerchantResponsePayload().getPaymentMethod()
                                            .getPaymentTransaction().getInstruction().getErrorcode();


                                    checkout_res.getMerchantResponsePayload().toString();

                                /*If StatusCode = 0300
                                SI Transaction is successful.

                                If StatusCode = 0399
                                SI Transaction is failed.
                                For eSign transaction, check if txn_status == 0398.

                                If Transaction Type is PreAuth & subtype is RESERVE then
                                    If StatusCode == 0200
                                    PreAuth RESERVE transaction is successful.

                                    If Status code = 0299
                                    PreAuth RESERVE transaction is failed.
                                If Transaction Type is PreAuth & subtype is CONSUME
                                    If StatusCode == 0300
                                    PreAuth CONSUME transaction is successful.

                                    If Status code = 0399
                                    PreAuth CONSUME transaction is failure.

*/

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(UpiPayment.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                                    FirebaseCrashlytics.getInstance().recordException(e);
                                }
                            }
                        } else if (resultCode == PaymentActivity.RESULT_ERROR) {

                            Log.d("UpiPayment", "got an error");
                            if (data.hasExtra(PaymentActivity.RETURN_ERROR_CODE) &&
                                    data.hasExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION)) {
                                String error_code = (String) data
                                        .getStringExtra(PaymentActivity.RETURN_ERROR_CODE);
                                String error_desc = (String) data
                                        .getStringExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION);
                                Toast.makeText(getApplicationContext(), " Got error :"
                                        + error_code + "--- " + error_desc, Toast.LENGTH_SHORT)
                                        .show();
                                PaymentMessage = error_code + "--- " + error_desc;
                                Log.d("UpiPayment" + " Code=>", error_code);
                                Log.d("UpiPayment" + " Desc=>", error_desc);

                            }

                        } else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                            Toast.makeText(getApplicationContext(), "Transaction Aborted by User",
                                    Toast.LENGTH_SHORT).show();
                            PaymentMessage = "Transaction Aborted by User";

                            Log.d("UpiPayment", "User pressed back button");
                        }

                        MSGTECHPROCRESP(checkout_res);
                    }
                    break;

//                case GOOGLE_PAY_REQUEST_CODE:
//                    //case UPI_PAYMENT:
//                    if ((RESULT_OK == resultCode) || (resultCode == 11)) {
//                        if (data != null) {
//                            String trxt = data.getStringExtra("response");
//                            Log.e("UPI", "onActivityResult: " + trxt);
//                            ArrayList<String> dataList = new ArrayList<>();
//                            dataList.add(trxt);
//                            upiPaymentDataOperation(dataList);
//                        } else {
//                            Log.e("UPI", "onActivityResult: " + "Return data is null");
//                            ArrayList<String> dataList = new ArrayList<>();
//                            dataList.add("nothing");
//                            upiPaymentDataOperation(dataList);
//                        }
//                    } else {
//                        //when user simply back without payment
//                        Log.e("UPI", "onActivityResult: " + "Return data is null");
//                        ArrayList<String> dataList = new ArrayList<>();
//                        dataList.add("nothing");
//                        upiPaymentDataOperation(dataList);
//                    }
//                    break;


            }
        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(UpiPayment.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
//            FirebaseCrashlytics.getInstance().recordException(e);

            AlertDialogClass.ShowMsg(UpiPayment.this, e.getMessage());
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

//    private void upiPaymentDataOperation(ArrayList<String> data) {
//        if (isConnectionAvailable(UpiPayment.this)) {
//            String str = data.get(0);
//            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
//            String paymentCancel = "";
//            if (str == null) str = "discard";
//
//            String response[] = str.split("&");
//            for (int i = 0; i < response.length; i++) {
//                String equalStr[] = response[i].split("=");
//                if (equalStr.length >= 2) {
//                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
//                        status = equalStr[1].toLowerCase();
//                    } else if (equalStr[0].toLowerCase().equals("txnId".toLowerCase())) {
//                        txnId = equalStr[1].toLowerCase();
//                    } else if (equalStr[0].toLowerCase().equals("responseCode".toLowerCase())) {
//                        responseCode = equalStr[1].toLowerCase();
//                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
//                        approvalRefNo = equalStr[1].toLowerCase();
//                    }
//                } else {
//                    paymentCancel = "Payment cancelled by user.";
//                }
//            }
//
//            if (status.equals("success")) {     //txnId=AXI6bb37a941d594e00baeebf1940fa3069&responseCode=00&Status=SUCCESS&txnRef=020313680898
//                //Code to handle successful transaction here.
//                Toast.makeText(UpiPayment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
//                Log.e("UPI", "payment successfull: " + approvalRefNo);
//                gotResponse = true;
//                // sendData(txnId, responseCode, status, approvalRefNo);
//            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
//                Toast.makeText(UpiPayment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
//                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
//            } else {
//                gotResponse = true;
//                Toast.makeText(UpiPayment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
//                Log.e("UPI", "failed payment: " + approvalRefNo);
//                //  sendData(txnId, responseCode, status, approvalRefNo);
//            }
//        } else {
//            Log.e("UPI", "Internet issue: ");
//            Toast.makeText(UpiPayment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
//        }
//    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Check Whether UPI App is installed on device or not
     *
     * @return true if app exists, otherwise false.
     */
    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;

        }
    }

//    void sendData(String txnId, String responseCode, String status, String approvalRefNo) {
//        //  ringProgressDialog = Views.showDialog(this);
//
//        pDialog = new SweetAlertDialog(UpiPayment.this, SweetAlertDialog.PROGRESS_TYPE);
//
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(true);
//        pDialog.show();
//        try {
//            String vppid = Logics.getVppId(this);
//            JSONObject jsonObject = new JSONObject();
////            jsonObject.put("pan_no", Logics.getPanNo(this));
//            jsonObject.put("pan_no", "aztpt4416B"); //txnId=AXI6bb37a941d594e00baeebf1940fa3069&responseCode=00&Status=SUCCESS&txnRef=020313680898
//            jsonObject.put("vpp_id", "1111");
//            jsonObject.put("client_state", state_str);
//            jsonObject.put("amount", "2124");
//            jsonObject.put("txnId", txnId);
//            jsonObject.put("Status", status);
//            jsonObject.put("txnRef", approvalRefNo);
//            jsonObject.put("responseCode", responseCode);
//            byte data[] = jsonObject.toString().getBytes();
//            new SendTOServer(this, requestSent, Const.MSGSAVEGPAYRESPONSE, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            pDialog.dismiss();
//        }
//    }

    @Override
    public void requestSent(int value) {
    }

    @Override
    public void Confirm() {
//        shiva
        //// call activity here...
        Intent intent = new Intent(UpiPayment.this, Dashboard.class);
        //intent.putExtra("isPaymentDone",1);
        startActivity(intent);
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        com.razorpay.Checkout.clearUserData(getApplicationContext());
        Log.e("success", "onPaymentSuccess: " + s + "  paymentdata===" + paymentData);
        paymentId = paymentData.getPaymentId();
        signature = paymentData.getSignature();
        orderId = paymentData.getOrderId();
        Log.e("success", "paymentId: " + paymentId + "  signature===" + signature + "  orderId==" + orderId);


        //String vppid= Logics.getVppId(MyLeads.this);
        try {
            jsonObjectresponse = new JSONObject();
            jsonObjectresponse.put("razorpay_payment_id", paymentId);
            jsonObjectresponse.put("payment_status", signature);
            jsonObjectresponse.put("razorpay_order_id", orderId);
            jsonObjectresponse.put("order_id", creatorderid);

            CallSAVECHECKOUT(jsonObjectresponse);

            CallVALIDATESIGNATURE(paymentId, orderId, signature);

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }


    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }




    public class ViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

           /* if (pDialog != null) {
                pDialog.dismiss();
                pDialog.cancel();
            }*/

            String data = (String) msg.obj;
            int msgCode = msg.arg1;
            switch (msgCode) {

                case Const.MSGMOUNT:
                    try {
                       /* pDialog.dismiss();
                        pDialog.cancel();*/

                        AlertDialogClass.PopupWindowDismiss();
                        checkamount = true;

                        Log.e("datamount", data);
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            callamount = true;
                            amount_str = jsonObject.getString("amount");
                            SharedPref.savePreferences(UpiPayment.this, SharedPref.amount, amount_str);
                            txt_title.setText("One time Registration fees \n to become Partner Rs." + Integer.parseInt(amount_str) / 100);
                            //btnTechprocess.setVisibility(View.VISIBLE);

                        } else {
                            callamount = false;
                            Toast.makeText(UpiPayment.this, "amount fetch falied..", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.e("exception", e.getMessage());
                        FirebaseCrashlytics.getInstance().recordException(e);
                        Toast.makeText(UpiPayment.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Const.MSG_GST:
                    try {
                       /* pDialog.dismiss();
                        pDialog.cancel();*/

                        AlertDialogClass.PopupWindowDismiss();
                        checkamount = true;
                        Log.e("MSG_GST", data);
                        JSONObject jsonObject = new JSONObject(data);

                        if (jsonObject.getString("status").equalsIgnoreCase("1")){
                            CallcreateOrder();
//                            Toast.makeText(UpiPayment.this, jsonObject.getString("mesage"), Toast.LENGTH_SHORT).show();
                            TastyToast.makeText(getApplicationContext(),  jsonObject.getString("mesage"), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        }else {
                            TastyToast.makeText(getApplicationContext(),  jsonObject.getString("mesage"), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                        gstDlg.dismiss();

                    } catch (Exception e) {
                        Log.e("exception", e.getMessage());
                        FirebaseCrashlytics.getInstance().recordException(e);
                        Toast.makeText(UpiPayment.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case Const.MSGTECHPROCREQ: {
                    try {
                       /* pDialog.dismiss();
                        pDialog.cancel();*/

                        AlertDialogClass.PopupWindowDismiss();

                        //{"transactionReference":"REF15973034131783010","transactionIdentifier":"TXN15973034131783010","created_date":"1597303413178","message":"Success","status":1}
                        JSONObject dataObject = new JSONObject(data);
                        //JSONObject dataObject = jsonObject.getJSONObject("data");
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        if (dataObject.getString("status").toLowerCase().equalsIgnoreCase("1")) {
                            MSGTECHPROCREQ_ = true;
                            // Views.ProgressDlgPaymentDone(UpiPayment.this, confirmPayment);
                            created_date = dataObject.getString("created_date");
                            // amount_str = "100";

                            v = BigDecimal.valueOf(Long.valueOf(SharedPref.getPreferences(UpiPayment.this, "amount")), 2);
                            // System.out.println(v.toString());
                            // System.out.println(v.toPlainString());
                            // System.out.println(String.format("%.2f", v));
                            // System.out.printf("%.2f\n",v);
                            String totalamount = String.format("%.2f", v);
                            //String totalamount = String.valueOf(Integer.valueOf(amount_str)/100);
                            // Creating Checkout Object
                            checkout = new com.paynimo.android.payment.model.Checkout();

                            // setting values to checkout object
                            // Case 1: Mandatory Fields
                            // setting Merchant fields values
                            //uat merchant id T585181
                            checkout.setMerchantIdentifier("L585181");
                            //checkout.setMerchantIdentifier("L585181"); //where T1234 is the MERCHANT CODE, update it with Merchant Code provided by TPSL
                            checkout.setTransactionIdentifier(dataObject.getString("transactionIdentifier")); //where TXN001 is the Merchant Transaction Identifier, it should be different for each transaction (alphanumeric value, no special character allowed)
                            checkout.setTransactionReference(dataObject.getString("transactionReference")); //where ORD0001 is the Merchant Transaction Reference number
                            checkout.setTransactionType
                                    (PaymentActivity.TRANSACTION_TYPE_SALE);//Transaction Type
                            checkout.setTransactionSubType
                                    (PaymentActivity.TRANSACTION_SUBTYPE_DEBIT);//Transaction Subtype
                            checkout.setTransactionCurrency("INR"); //Currency Type
                            checkout.setTransactionAmount(totalamount); //Transaction Amount
                            checkout.setTransactionDateTime(transactionDate); //Transaction Date
// setting Consumer fields values
                            checkout.setConsumerIdentifier(Logics.getVppId(UpiPayment.this)); //Consumer Identifier, default value "", set this value as application user name if you want Instrument Vaulting, SI on Cards. Consumer ID should be alpha-numeric value with no space
                            checkout.setConsumerAccountNo(accno); //Account Number, default value "". For eMandate, you can set this value here or can be set later in SDK.
                            checkout.setConsumerEmailID(Logics.getEmail(UpiPayment.this)); //Consumer email id
                            checkout.setConsumerMobileNumber(Logics.getMobile_1(UpiPayment.this)); //Consumer mobile number

// setting Consumer Cart Item
                            checkout.addCartItem("FIRST", totalamount, "0.0", "0.0", "SMSG2015-01-12345", "Mobile", "HTC Desire", "www.flipkart.com");
                            checkout.setTransactionAmount("" + totalamount);
                            //checkout.addCartItem("ProductID","ProductAmount","ProductSurchargeOrDiscountAmount", "CommisionAmount", "ProductSKU", "ProductReference", "ProductDescriptor","ProductProviderID");


                            Intent authIntent = PaymentModesActivity.Factory.getAuthorizationIntent(getApplicationContext(), true);
                            // Intent authIntent = new Intent(UpiPayment.this,PaymentModesActivity.class);

// Checkout Object
//                            Log.d("Checkout Request Object",
//                                    checkout.getMerchantRequestPayload().toString());

                            authIntent.putExtra(Constant.ARGUMENT_DATA_CHECKOUT, checkout);

// Public Key
                            authIntent.putExtra(PaymentActivity.EXTRA_PUBLIC_KEY, "1234-6666-6789-56");

// Requested Payment Mode
                            authIntent.putExtra(PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE,
                                    PaymentActivity.PAYMENT_METHOD_DEFAULT);
                            //PaymentActivity.PAYMENT_METHOD_UPI);

                            //PaymentModesActivity.Settings settings = new PaymentModesActivity.Settings();
                            //authIntent.putExtra(Constant.ARGUMENT_DATA_SETTING, settings);

                            startActivityForResult(authIntent, PaymentActivity.REQUEST_CODE);

                        } else {
//                            Views.showDialog(UpiPayment.this,confirmPayment);
                            Toast.makeText(getApplicationContext(), dataObject.getString("error"), Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
//                        e.printStackTrace();
//                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialogClass.ShowMsg(UpiPayment.this, e.getMessage());
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }

                }
                break;
                case MSGTECHPROCRESP: {
                    MSGTECHPROCRESP_ = true;
                   /* pDialog.dismiss();
                    pDialog.cancel();*/

                    AlertDialogClass.PopupWindowDismiss();

                    try {
                        JSONObject dataObject = new JSONObject(data);
                        //  JSONObject dataObject = jsonObject.getJSONObject("data");
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        if (dataObject.getString("status").toLowerCase().equalsIgnoreCase("1")) {
                            if (PaymentStatus == 1)
                                //int amt = Integer.valueOf(amount_str)/100;

                                v = BigDecimal.valueOf(Long.valueOf(SharedPref.getPreferences(UpiPayment.this, "amount")), 2);
                            // System.out.println(v.toString());
                            // System.out.println(v.toPlainString());
                            // System.out.println(String.format("%.2f", v));
                            // System.out.printf("%.2f\n",v);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (PaymentStatus == 0) {
                                        Views.ProgressDlgPaymentFail(UpiPayment.this, confirmPayment, PaymentMessage);
                                    }
                                    if (PaymentStatus == 1) {
                                        Views.ProgressDlgPaymentDone(UpiPayment.this, confirmPayment, String.format("%.2f", v));
                                    }
                                }
                            });


//                            showPopup();
                        } else {
//                            Views.showDialog(UpiPayment.this,confirmPayment);
                            Toast.makeText(getApplicationContext(), dataObject.getString("error"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        /*if (pDialog != null) {
                            pDialog.dismiss();
                        }*/

                        AlertDialogClass.PopupWindowDismiss();

                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
                break;
                case Const.MSGCREATEORDEROBJ:
//                    Toast.makeText(RazorpayActivity.this, data, Toast.LENGTH_SHORT).show();
                    Log.e("datacreateorder", data);

                    AlertDialogClass.PopupWindowDismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String datastr = jsonObject.getString("data");
                        if (!datastr.equalsIgnoreCase("FAIL")) {
                            JSONObject jsonObject1 = new JSONObject(datastr);
                            creatorderid = jsonObject1.getString("id");
                            startPayment(creatorderid);
                            checkcreateorder = true;
                        } else {
                            Toast.makeText(UpiPayment.this, "Failed", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.e("exception", e.getMessage());
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                    break;
                case Const.MSGVALIDATESIGNATURE:
//                    Toast.makeText(UpiPayment.this, data, Toast.LENGTH_SHORT).show();

                    AlertDialogClass.PopupWindowDismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getString("data").equalsIgnoreCase("SUCCESS")) {
                            checkvalidatesignature = true;
                            //Views.ProgressDlgPaymentDone(UpiPayment.this,confirmPayment,amount_str);
                            v = BigDecimal.valueOf(Long.valueOf(SharedPref.getPreferences(UpiPayment.this, "amount")), 2);
                            Views.ProgressDlgPaymentDone(UpiPayment.this, confirmPayment, String.format("%.2f", v));

                        }
                    } catch (Exception e) {
                        Log.e("exception", e.getMessage());
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                    break;
                case Const.MSGSAVECHECKOUT:
                    AlertDialogClass.PopupWindowDismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getString("data").equalsIgnoreCase("Success")) {
//                            checkcreateorder=tr
                        }
                    } catch (Exception e) {
                        Log.e("exception", e.getMessage());
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                    break;
            }

        }
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.payment_successful_popoup, null);
        builder.setView(dialogView);
        FancyButton btnProcced = dialogView.findViewById(R.id.btn_Proceed);

        //  FancyButton btn_positive = dialogView.findViewById(R.id.btnIsRegYes);
        btnProcced.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UpiPayment.this, Dashboard.class);
                        startActivity(intent);

                    }
                });
        builder.setCancelable(true);


        alertDialog = builder.show();


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void connected() {
       /* if (pDialog != null) {
            pDialog.dismiss();
        }*/

        AlertDialogClass.PopupWindowDismiss();

        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!callamount) {
                    CALLAMOUNT();
                } else if (!MSGTECHPROCREQ_) {
                    if (CallSaveTechProcessRequest_HIT == true) {
                        CallSaveTechProcessRequest();
                    }
                } else if (!MSGTECHPROCRESP_) {

                    MSGTECHPROCRESP(checkout_res);

                }
                /*else if (!checkcreateorder){

                    CallcreateOrder();

                } else if (!checkvalidatesignature){
                    CallVALIDATESIGNATURE(paymentId,orderId,signature);
                }
*/


                //TastyToast.makeText(UpiPayment.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
       /* if (pDialog != null) {
            pDialog.dismiss();
        }*/

        AlertDialogClass.PopupWindowDismiss();

        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(UpiPayment.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(UpiPayment.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
      /*  if (pDialog != null) {
            pDialog.dismiss();
        }*/

        AlertDialogClass.PopupWindowDismiss();


        Views.SweetAlert_NoDataAvailble(UpiPayment.this, "Internet Not Available");
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
       /* if (pDialog != null) {
            pDialog.dismiss();
        }*/

        AlertDialogClass.PopupWindowDismiss();

//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(UpiPayment.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(UpiPayment.this, connectionProcess, "Server Not Available");//
//                                        ConnectToserver(connectionProcess);
                                    }
                                });
                            }
                        }
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void SocketDisconnected() {
        /*if (pDialog != null) {
            pDialog.dismiss();
        }*/

        AlertDialogClass.PopupWindowDismiss();

        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(UpiPayment.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(UpiPayment.this, "null", Toast.LENGTH_SHORT).show();
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

           /* if (pDialog == null) {
                pDialog = new SweetAlertDialog(UpiPayment.this, SweetAlertDialog.PROGRESS_TYPE);
            }
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();*/

            AlertDialogClass.PopupWindowShow(UpiPayment.this,relative1);
            new SendTOServer(UpiPayment.this, requestSent, Const.MSGMOUNT, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
//            pDialog.dismiss();
            AlertDialogClass.PopupWindowDismiss();

            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    public void GstProceedclick(GstDlg gstDlg,String gstno,String gstname,String gstaddress) {
        this.gstDlg=gstDlg;
        CALL_gst(gstno,gstname,gstaddress);
    }
    private void CALL_gst(String gstno,String gstname,String gstaddress) {
        try {
            JSONObject jsonObject = new JSONObject();

            String vppid = Logics.getVppId(UpiPayment.this);
            jsonObject.put("vpp_id", vppid);
            jsonObject.put("gst_number", gstno);
            jsonObject.put("gst_name", gstname);
            jsonObject.put("gst_address", gstaddress);
//            Log.e("CALLAMOUNT", jsonObject.toString());
            byte data[] = jsonObject.toString().getBytes();

           /* if (pDialog == null) {
                pDialog = new SweetAlertDialog(UpiPayment.this, SweetAlertDialog.PROGRESS_TYPE);
            }
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();*/

            AlertDialogClass.PopupWindowShow(UpiPayment.this,relative1);
            new SendTOServer(UpiPayment.this, requestSent, Const.MSG_GST, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
//            pDialog.dismiss();
            AlertDialogClass.PopupWindowDismiss();

            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    void CallSaveTechProcessRequest() {
        CallSaveTechProcessRequest_HIT = true;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("merchantIdentifier", "L585181");
            jsonObject.put("transactionIdentifier", "TXN001");
            jsonObject.put("transactionReference", "ORD001");
            jsonObject.put("transactionType", "" + PaymentActivity.TRANSACTION_TYPE_SALE);
            jsonObject.put("transactionSubType", "" + PaymentActivity.TRANSACTION_SUBTYPE_DEBIT);
            jsonObject.put("transactionCurrency", "INR");
            jsonObject.put("transactionAmount", SharedPref.getPreferences(UpiPayment.this, "amount"));// DYANAMIC AMOUNT MUST COME
            jsonObject.put("transactionDateTime", transactionDate);
            jsonObject.put("consumerIdentifier", Logics.getVppId(this));
            jsonObject.put("consumerAccountNo", accno);
            jsonObject.put("consumerEmailId", Logics.getEmail(this));
            jsonObject.put("consumerMobileNumber", Logics.getMobile_1(this));
            jsonObject.put("cartItem", "FIRST");
            // jsonObject.put("vpp_id","11145");
            jsonObject.put("vpp_id", Logics.getVppId(UpiPayment.this));

//            Log.e("requestfortechprocess", jsonObject.toString());
            byte data[] = jsonObject.toString().getBytes();

          /*  if (pDialog == null) {
                pDialog = new SweetAlertDialog(UpiPayment.this, SweetAlertDialog.PROGRESS_TYPE);
            }
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();*/
            AlertDialogClass.PopupWindowShow(UpiPayment.this,relative1);

            new SendTOServer(UpiPayment.this, requestSent, Const.MSGTECHPROCREQ, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
//            pDialog.dismiss();

            AlertDialogClass.PopupWindowDismiss();
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);

        }
    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//        handler.removeCallbacks(runnable);
//    }


/*

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("merchantCode",checkout_res.getMerchantResponsePayload().getMerchantCode());
                                jsonObject.put("merchantTransactionIdentifier",checkout_res.getMerchantResponsePayload().getMerchantTransactionIdentifier());
                                jsonObject.put("merchantTransactionRequestType",checkout_res.getMerchantResponsePayload().getMerchantTransactionRequestType());
                                jsonObject.put("responseType" ,checkout_res.getMerchantResponsePayload().getResponseType());
                                jsonObject.put("transactionState",checkout_res.getMerchantResponsePayload().getTransactionState());
                                jsonObject.put("merchantAdditionalDetails",checkout_res.getMerchantResponsePayload().getMerchantAdditionalDetails());
                                jsonObject.put("token",checkout_res.getMerchantResponsePayload().getPaymentMethod().getToken());
                                jsonObject.put("instrumentAliasName",checkout_res.getMerchantResponsePayload().getPaymentMethod().getInstrumentAliasName());
                                jsonObject.put("instrumentToken",checkout_res.getMerchantResponsePayload().getPaymentMethod().getInstrumentToken());
                                jsonObject.put("bankSelectionCode",checkout_res.getMerchantResponsePayload().getPaymentMethod().getBankSelectionCode());
                                jsonObject.put("bankAcsFormName",checkout_res.getMerchantResponsePayload().getPaymentMethod().getACS().getBankAcsFormName());
                                jsonObject.put("bankAcsHttpMethod",checkout_res.getMerchantResponsePayload().getPaymentMethod().getACS().getBankAcsHttpMethod());
                                jsonObject.put("bankAcsParams",checkout_res.getMerchantResponsePayload().getPaymentMethod().getACS().getBankAcsParams());
                                jsonObject.put("bankAcsUrl",checkout_res.getMerchantResponsePayload().getPaymentMethod().getACS().getBankAcsUrl());
                                jsonObject.put("initiator",checkout_res.getMerchantResponsePayload().getPaymentMethod().getOTP().getInitiator());
                                jsonObject.put("message",checkout_res.getMerchantResponsePayload().getPaymentMethod().getOTP().getMessage());
                                jsonObject.put("numberOfDigit",checkout_res.getMerchantResponsePayload().getPaymentMethod().getOTP().getNumberOfDigit());
                                jsonObject.put("target",checkout_res.getMerchantResponsePayload().getPaymentMethod().getOTP().getTarget());
                                jsonObject.put("type",checkout_res.getMerchantResponsePayload().getPaymentMethod().getOTP().getType());
                                jsonObject.put("message",checkout_res.getMerchantResponsePayload().getPaymentMethod().getOTP().getMessage());
                                jsonObject.put("amount",checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount());
                                jsonObject.put("balanceAmount",checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getBalanceAmount());
                                jsonObject.put("bankReferenceIdentifier",checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getBankReferenceIdentifier());
                                jsonObject.put("dateTime",checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getDateTime());
                                jsonObject.put("errorMessage",checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getErrorMessage());
                                jsonObject.put("identifier",checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getIdentifier());
                                jsonObject.put("refundIdentifier",checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getRefundIdentifier());
                                jsonObject.put("statusCode",checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusCode());
                                jsonObject.put("statusMessage",checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusMessage();
                                jsonObject.put("instruction",checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getInstruction().toString());
                                jsonObject.put("authentication",checkout_res.getMerchantResponsePayload().getPaymentMethod().getAuthentication().toString());
                                jsonObject.put("error",checkout_res.getMerchantResponsePayload().getPaymentMethod().getError().toString());

* */

    void MSGTECHPROCRESP(com.paynimo.android.payment.model.Checkout checkout_res) {
        try {

            // total_response_techprocess = checkout_res.getMerchantResponsePayload().toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("vpp_id", Logics.getVppId(this));
            //jsonObject.put("vpp_id","11145");
            //jsonObject.put("client_state", "Goa");
            jsonObject.put("client_state", state_str);
            //jsonObject.put("amount", amount_str);
            if (checkout_res != null) {
                jsonObject.put("total_response_techprocess", checkout_res.getMerchantResponsePayload().toString());
                if (checkout_res
                        .getMerchantResponsePayload().getPaymentMethod()
                        .getPaymentTransaction().getStatusCode().equalsIgnoreCase("0300") ||
                        checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getStatusCode().equalsIgnoreCase("0200") ||
                        checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getStatusCode().equalsIgnoreCase("0398")) {
                    jsonObject.put("Status", "Success");
                    PaymentStatus = 1;
                    jsonObject.put("paymentStaus", 1);

                } else {
                    jsonObject.put("Status", "Failure");
                    PaymentStatus = 0;
                    jsonObject.put("paymentStaus", 0);

                }
                jsonObject.put("transactionIdentifier", checkout_res.getMerchantResponsePayload()
                        .getMerchantTransactionIdentifier().toString());
                jsonObject.put("created_date", created_date);
                jsonObject.put("vpp_id", Logics.getVppId(this));
                jsonObject.put("pan_no", Logics.getPanNo(this));
                // jsonObject.put("paymentStaus", 1);
                jsonObject.put("signature", Signature.calculateRFC2104HMAC(checkout_res.getMerchantResponsePayload().
                        getMerchantTransactionIdentifier().trim().toString() + "|" + created_date, secret_id.trim().toString()));

                if (checkout.getMerchantRequestPayload().toString() != null ||
                        !checkout.getMerchantRequestPayload().toString().equalsIgnoreCase("")) {
                    jsonObject.put("checkout_request", checkout.getMerchantRequestPayload().toString());
                } else {
                    jsonObject.put("checkout_request", "");
                }

                jsonObject.put("PaymentMessage", PaymentMessage);


                jsonObject.put("status_code", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusCode());
                jsonObject.put("status_message", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusMessage());
                jsonObject.put("error_message", checkout_res.getMerchantResponsePayload().getPaymentMethod().getError().getDesc());
                jsonObject.put("error_code", checkout_res.getMerchantResponsePayload().getPaymentMethod().getError().getCode());
                // jsonObject.put("amount","1");
                String amount = checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount().split("\\.")[0]; //or else (Pattern.quote("."))
                jsonObject.put("amount", amount + "00");
                //jsonObject.put("amount", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount());
                jsonObject.put("identifier", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getIdentifier());
                jsonObject.put("bank_selection_code", checkout_res.getMerchantResponsePayload().getPaymentMethod().getBankSelectionCode());
                jsonObject.put("bank_reference_identifier", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getBankReferenceIdentifier());
                jsonObject.put("refund_identifier", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getRefundIdentifier());
                jsonObject.put("balance_amount", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getBalanceAmount());
                jsonObject.put("instrument_alias_name", checkout_res.getMerchantResponsePayload().getPaymentMethod().getInstrumentAliasName());
                jsonObject.put("simandate_id", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getInstruction().getId());
                jsonObject.put("simandate_status", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getInstruction().getStatusCode());
                jsonObject.put("simandate_error_code", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getInstruction().getErrorcode());
                jsonObject.put("email", Logics.getEmail(UpiPayment.this));
                jsonObject.put("mobile_no", Logics.getMobile_1(UpiPayment.this));
                jsonObject.put("response_date_time", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getDateTime());

                //9152150808

            } else {

                jsonObject.put("total_response_techprocess", "");
                jsonObject.put("Status", "Failure");
                PaymentStatus = 0;
                jsonObject.put("paymentStaus", 0);

                jsonObject.put("transactionIdentifier", "");
                jsonObject.put("created_date", created_date);
                jsonObject.put("vpp_id", Logics.getVppId(this));
                jsonObject.put("pan_no", Logics.getPanNo(this));
                // jsonObject.put("paymentStaus", 1);
                jsonObject.put("signature", "");

                if (checkout.getMerchantRequestPayload().toString() != null ||
                        !checkout.getMerchantRequestPayload().toString().equalsIgnoreCase("")) {
                    jsonObject.put("checkout_request", checkout.getMerchantRequestPayload().toString());
                } else {
                    jsonObject.put("checkout_request", "");
                }

                jsonObject.put("PaymentMessage", PaymentMessage);
                jsonObject.put("status_code", "");
                jsonObject.put("status_message", "");
                jsonObject.put("error_message", "");
                jsonObject.put("error_code", "");
                // jsonObject.put("amount","1");

                jsonObject.put("amount", "0");
                //jsonObject.put("amount", checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount());
                jsonObject.put("identifier", "");
                jsonObject.put("bank_selection_code", "");
                jsonObject.put("bank_reference_identifier", "");
                jsonObject.put("refund_identifier", "");
                jsonObject.put("balance_amount", "");
                jsonObject.put("instrument_alias_name", "");
                jsonObject.put("simandate_id", "");
                jsonObject.put("simandate_status", "");
                jsonObject.put("simandate_error_code", "");
                jsonObject.put("email", Logics.getEmail(UpiPayment.this));
                jsonObject.put("mobile_no", Logics.getMobile_1(UpiPayment.this));
                jsonObject.put("response_date_time", "");
            }
//            Log.e("RequestcreateOrder", jsonObject.toString());
            byte data1[] = jsonObject.toString().getBytes();
            /*{"vpp_id":"11145","client_state":"Maharashtra","amount":"1.00","total_response_techprocess":"ResponsePayload [merchantCode=T585181, merchantTransactionIdentifier=TXN15979151252717110, merchantTransactionRequestType=TAR, responseType=web, transactionState=F, merchantAdditionalDetails={email:PRAVINDI.BOOKONSPOT@GMAIL.COM}{mob:9975153610}, paymentMethod=PaymentMethod [token=, instrumentAliasName=, instrumentToken=, bankSelectionCode=470, aCS=ACS [bankAcsFormName=net, bankAcsHttpMethod=post, bankAcsParams=[], bankAcsUrl=], oTP=OTP [initiator=, message=, numberOfDigit=, target=, type=], paymentTransaction=PaymentTransaction [amount=1.00, balanceAmount=, bankReferenceIdentifier=, dateTime=20-08-2020 14:49:12, errorMessage=, identifier=1204003531, refundIdentifier=, statusCode=0300, statusMessage=success, instruction=Instruction [id=, statusCode=, errorcode=, errordesc=]], authentication=Authentication [type=, subType=], error=Error [code=, desc=]]]","Status":"Success","paymentStaus":1,"transactionIdentifier":"TXN15979151252717110","created_date":"1597915125271","pan_no":"ARAPD2934M","signature":"af83f2196330fcf385050fbbb97028179b90a4f1fc3615bc646ac704ce43b5e5","checkout_request":"RequestJson [merchant=Merchant [identifier=T585181, responseType=, description=, responseEndpointURL=, webhookType=, webhookEndpointURL=], transaction=Transaction [requestType=, token=, identifier=TXN15979151252717110, type=SALE, subType=DEBIT, currency=INR, amount=1.00\n, dateTime=20-08-2020, reference=REF15979151252717110, description=, isRegistration=N, deviceIdentifier=Android, forced3DSCall=N, smsSending=, merchantInitiated=N, securityToken=], cart=Cart [identifier=, reference=, description=, item=[Item [identifier=FIRST, amount=1.00\n, surchargeOrDiscountAmount=0.0, comAmt=0.0, sKU=SMSG2015-01-12345, reference=Mobile, description=HTC Desire, providerIdentifier=www.flipkart.com]]], consumer=Consumer [identifier=11145, emailID=PRAVINDI.BOOKONSPOT@GMAIL.COM, mobileNumber=9975153610, accountNo=20149541044, accountHolderName=, aadharNo=, accountType=, vpa=, pan=, phoneNumber=], payment=Payment [method=Method [type=, token=], instrument=Instrument [token=, action=, type=, subType=, identifier=, alias=, holder=Holder [name=, address=Address [street=, city=, state=, county=, country=, zipCode=]], provider=, acquirer=, processor=, issuer=, iFSC=, mICR=, bIC=, iBAN=, issuance=Issuance [month=, year=, dateTime=], expiry=Expiry [month=, year=, dateTime=], verificationCode=, authentication=Authentication [type=, subType=, token=]], instruction=Instruction [identifier=, reference=, description=, action=D, type=, limit=, amount=, occurrence=, frequency=, validity=, startDateTime=, endDateTime=, debitDay=, debitFlag=N]]]","PaymentMessage":"","status_code":"0300","status_message":"success","error_message":"","error_code":"","identifier":"1204003531","bank_selection_code":"470","bank_reference_identifier":"","refund_identifier":"","balance_amount":"","instrument_alias_name":"","simandate_id":"","simandate_status":"","simandate_error_code":"","email":"","mobile_no":""}*/
         /*   if (pDialog == null) {
                pDialog = new SweetAlertDialog(UpiPayment.this, SweetAlertDialog.PROGRESS_TYPE);
            }
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();*/

            AlertDialogClass.PopupWindowShow(UpiPayment.this,relative1);

            new SendTOServer(UpiPayment.this, requestSent, MSGTECHPROCRESP, data1, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
//            pDialog.dismiss();

            AlertDialogClass.PopupWindowDismiss();
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    ///----------------------razorpay-------

    private void CallcreateOrder() {

        try {
            JSONObject jsonObject = new JSONObject();
            String vpp_id = Logics.getVppId(UpiPayment.this);
            if(vpp_id!= null || vpp_id.trim().length()>5) {

                jsonObject.put("vpp_id", vpp_id);

                jsonObject.put("amount", amount_str);
                jsonObject.put("currency", "INR");
                jsonObject.put("receipt", "order_rcpid_101"); // order id generated at server and created date too.
                jsonObject.put("payment_capture", "true");
                jsonObject.put("mobile_app_version", Logics.getVersionInfo(UpiPayment.this));
                jsonObject.put("created_by", "VPP");
                Log.e("RequestcreateOrder", jsonObject.toString());
                byte data[] = jsonObject.toString().getBytes();

          /*  if (pDialog == null) {
                pDialog = new SweetAlertDialog(UpiPayment.this, SweetAlertDialog.PROGRESS_TYPE);
            }
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();*/

                AlertDialogClass.PopupWindowShow(UpiPayment.this, relative1);
                new SendTOServer(UpiPayment.this, requestSent, Const.MSGCREATEORDEROBJ, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }else{

                TastyToast.makeText(UpiPayment.this, "something went wrong, please login or Signup", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                Intent intent = new Intent(UpiPayment.this, SliderImages.class).putExtra("from","");
                startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public void startPayment(String id) {
        /**
         * Instantiate Checkout
         */

        //    key_id	                    key_secret
        //      rzp_test_wzgnSUypkltsAF	        QiLifBMAECa6nSlNvO58OQRa

        com.razorpay.Checkout checkout_rz = new com.razorpay.Checkout();
        checkout_rz.setKeyID("rzp_live_3fJw8WVyANEt4w");    //

        //rzp_live_3fJw8WVyANEt4w -- live key
        //rzp_test_wzgnSUypkltsAF --- test key
        /**
         * Set your logo here
         */
        checkout_rz.setImage(R.drawable.ic_launcher_background);

        /**
         * Reference to current activity
         */
        final UpiPayment activity = this;
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Ventura Securities Ltd.");
            options.put("description", "VPP Registration Fees");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            // options.put("image", getResources().getDrawable(R.drawable.vpp_logo));
            options.put("currency", "INR");
            options.put("amount", SharedPref.getPreferences(getApplicationContext(), SharedPref.amount));// amount in paisa
            options.put("order_id", id);
            options.put("vpp_id", Logics.getVppId(UpiPayment.this));

            /*phone number and email edittext will filled with below data*/
            JSONObject preFill = new JSONObject();
            preFill.put("email", Logics.getEmail(UpiPayment.this));
            preFill.put("contact", Logics.getMobile_1(UpiPayment.this));
            options.put("prefill", preFill);
            JSONObject notes = new JSONObject();
            notes.put("vpp_id", Logics.getVppId(UpiPayment.this));
            notes.put("vpp_name", Logics.getVppName(UpiPayment.this));
            options.put("notes", notes);
            JSONObject theme = new JSONObject();
            theme.put("color", "#FB9109");
            options.put("theme", theme);

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
            jsonObjectrequest.put("vpp_id", Logics.getVppId(UpiPayment.this));
            //jsonObjectrequest.put("vpp_id", SharedPref.getPreferences(getApplicationContext(),SharedPref.vppid));
            jsonObjectrequest.put("request", jsonArray.toString());

            CallSAVECHECKOUT(jsonObjectrequest);
            order_id = id;

            checkout_rz.open(activity, options);

        } catch (Exception e) {
            Log.e("error", "Error in starting Razorpay Checkout", e);
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void CallSAVECHECKOUT(JSONObject jsonObject) {
//        ringProgressDialog = Views.showDialog(this);
        Log.e("SAVECHECKOUT", jsonObject.toString());
        byte data[] = jsonObject.toString().getBytes();

       /* if (pDialog == null) {
            pDialog = new SweetAlertDialog(UpiPayment.this, SweetAlertDialog.PROGRESS_TYPE);
        }
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();*/

        AlertDialogClass.PopupWindowShow(UpiPayment.this,relative1);
        new SendTOServer(UpiPayment.this, requestSent, Const.MSGSAVECHECKOUT, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void CallVALIDATESIGNATURE(String paymentId, String orderId, String signature) {
//        ringProgressDialog = Views.showDialog(this);
        try {
            JSONObject jsonObject = new JSONObject();
            //String vppid= Logics.getVppId(MyLeads.this);
            jsonObject.put("razorpay_payment_id", paymentId);
            jsonObject.put("razorpay_order_id", orderId);
            jsonObject.put("signature_by_checkout", signature);
            jsonObject.put("vpp_id", Logics.getVppId(UpiPayment.this));
            jsonObject.put("amount", SharedPref.getPreferences(getApplicationContext(), SharedPref.amount));
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


           /* if (pDialog == null) {
                pDialog = new SweetAlertDialog(UpiPayment.this, SweetAlertDialog.PROGRESS_TYPE);
            }
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();*/

            AlertDialogClass.PopupWindowShow(UpiPayment.this,relative1);

            new SendTOServer(UpiPayment.this, requestSent, Const.MSGVALIDATESIGNATURE, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }


    public static void PopupWindowShowGST(Context context, View linearLayout1) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = layoutInflater.inflate(R.layout.layout_cnfrmgst, null);
        //instantiate popup window
        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //display the popup window
        new Handler().postDelayed(new Runnable() {

            public void run() {
                popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);
            }

        }, 200L);

        if (!popupWindow.isShowing()) {
            customView.setVisibility(View.VISIBLE);
        } else {
            customView.setVisibility(View.GONE);
        }

//        Bundle bundle = new Bundle();
//        bundle.putString("id","");
//        GstDlg resourse_share=new GstDlg();
//        resourse_share.show(fm, "Dialog Fragment");
//        resourse_share.setArguments(bundle);

    }

    @SuppressLint("LongLogTag")
    public void ProgressDlgConnectSocket(Context context, final ConnectionProcess connectionProcess, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

        Log.e("DlgConnectSocket", "called");
        MaxTry++;
        if (MaxTry > 3) {
            sweetAlertDialog.setTitleText(msg)
//                .setContentText("Socket Not Available")
                    .setConfirmText("Try Again later!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            sDialog.dismiss();
                            sDialog.cancel();

                            //call here
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(UpiPayment.this),
                                    "0",
                                    Methods.getVersionInfo(UpiPayment.this),
                                    Methods.getlogsDateTime(), "UpiPayment",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    UpiPayment.this);

                            finishAffinity();
                            finish();
                        }
                    });
            if (!UpiPayment.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
               // Toast.makeText(UpiPayment.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            if (connectionProcess == null) {
                Log.e("DlgConnectSocket11111_null", "called");

            } else {
                new ConnectTOServer(UpiPayment.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                connectionProcess.ConnectToserver(connectionProcess);
            }
            Log.e("DlgConnectSocket11111", "called");

        }

        Log.e("DlgConnectSocketMaxTry", String.valueOf(MaxTry));


//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();
    }


}