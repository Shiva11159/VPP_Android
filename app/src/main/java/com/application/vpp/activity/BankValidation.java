package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.BankValidateData;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Interfaces.APiValidateAccount;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.NetworkCall.APIClient;
import com.application.vpp.NetworkCall.TLSSocketFactory;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BankValidation extends AppCompatActivity implements View.OnClickListener, RequestSent, ConnectionProcess {

    //168201000017428
    //IOBA0001682

    AlertDialog alertDialog;

    //    TextInputLayout text_input_AccNoOld;
    EditText edtAccNo, edtIfscCode, edtAccNoOld;
    TextView txtAccNoOld;
    RelativeLayout layName;
    TextView txtAccountValidateNote;
    Button btnProceed, btn_verify_AccNo;
    Button btn_Submit_AccNo;
    private int resCode;
    private String resMsg;
    public static Handler handlerBank, handlerbankverify;
    public String fname, lname, mname, name;
    //    ProgressDialog ringProgressDialog;
    ScrollView mainlayout;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    int MaxTry = 0;
    String err = "";

    TextView txtACC, txtIFSC;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_validation1);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        try {
            new TLSSocketFactory();
        } catch (KeyManagementException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }


        layName = (RelativeLayout) findViewById(R.id.lay_name);
        edtAccNoOld = findViewById(R.id.edtAccNoOld);
        txtAccNoOld = findViewById(R.id.txtAccNoOld);


        txtIFSC = findViewById(R.id.txtIFSC);
        txtACC = findViewById(R.id.txtACC);

//        text_input_AccNoOld = findViewById(R.id.text_input_AccNoOld);
        edtAccNo = findViewById(R.id.edtAccNo);
        edtIfscCode = findViewById(R.id.edtIfscCode);
        btn_verify_AccNo = findViewById(R.id.btn_verify_AccNo);
        btnProceed = findViewById(R.id.btn_bankProceed);
        //  btnEdtName=findViewById(R.id.btn_edt_name);
        txtAccountValidateNote = findViewById(R.id.txtNote3);
        mainlayout = (ScrollView) findViewById(R.id.mainlayout);

        btn_verify_AccNo.setOnClickListener(this);
        //btnEdtName.setOnClickListener(this);
        btnProceed.setOnClickListener(this);
        handlerBank = new ViewHandler();
        handlerbankverify = new ViewHandler();


//        if (getIntent().getStringExtra("from").equalsIgnoreCase("profile")){
//            edtAccNoOld.setVisibility(View.VISIBLE);
//            txtAccNoOld.setVisibility(View.VISIBLE);
//            edtAccNoOld.setText(getIntent().getStringExtra("acc"));
//
//            txtACC.setText("New Account Number");
//            txtIFSC.setText("New IFSC code");
//        }else {
//            txtACC.setText("Account Number");
//            txtIFSC.setText("IFSC code");
//
//            edtAccNoOld.setVisibility(View.GONE);
//            txtAccNoOld.setVisibility(View.GONE);
//        }

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList, "SocketLogs", BankValidation.this);

    }

//    private void fetchBankResponse() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
////        ringProgressDialog = ProgressDialog.show(BankValidation.this, "Please wait ...", "Loading Your Data ...", true);
////        ringProgressDialog.setCancelable(true);
////        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//
//        AlertDialogClass.PopupWindowShow(BankValidation.this,mainlayout);
//
//        String strAccNo = edtAccNo.getText().toString().trim().toUpperCase();
//
//        String strIfsc = edtIfscCode.getText().toString().trim().toUpperCase();
//
//        final JsonObject jsonObject = new JsonObject();
//
//        try {
//
//            jsonObject.addProperty("ifsc", strIfsc);
//            jsonObject.addProperty("acc_no", strAccNo);
//            jsonObject.addProperty("login_id", "VPPAPP");
//            Context context = BankValidation.this;
//            Retrofit retrofit = APIClient.getClient(context);
//
//            APiValidateAccount aPiValidateAccount = retrofit.create(APiValidateAccount.class);
//
//            Call<BankValidateData> call = aPiValidateAccount.getbackdetails(jsonObject);
//
//            call.enqueue(new Callback<BankValidateData>() {
//
//                @Override
//                public void onResponse(Call<BankValidateData> call, Response<BankValidateData> response) {
//                    btnSubmit.setEnabled(true);
//
//
//
//                    if ((response.body()!=null) || (!response.body().toString().equalsIgnoreCase("null"))){
//                        if (response.isSuccessful()) {
//                            Log.e("onResponse11: ", String.valueOf(response.body()));
//
//                            Log.e("resmsge", String.valueOf(response.body().getNwrespmessg()));
//                            Log.e("resp", String.valueOf(response.body().getResponse()));
//                            Log.e("code", String.valueOf(response.body().getNwrespcode()));
//                            Log.e("txt", String.valueOf(response.body().getTxnid()));
//                            Log.e("getRespcode", String.valueOf(response.body().getRespcode()));
//                            Log.e("getC_name", String.valueOf(response.body().getC_name()));
//
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
//                            //Log.d("response", "onResponse: "+response.body().getC_name());
//                            //  Log.e("response", "onResponse: "+response.body().toString());
////                    ringProgressDialog.dismiss();
//                            AlertDialogClass.PopupWindowDismiss();
//                            // String name = response.body().getC_name().toString();
//                            BankValidateData bk = response.body();
//
//                            if (bk != null) {
//                                String networkrepcode = bk.getNwrespcode();
//                                String responsecode = bk.getRespcode();
//                                if (responsecode.equals("00")) {
//
//                                    Log.e("onResponse: ", "one");
//                                    String AccNo = bk.getAcc_no();
//                                    String Ifsc = bk.getIfsc();
//                                    String vppBkname = bk.getC_name();
//                                    String txnId = bk.getTxnid();
//                                    String respCode = bk.getRespcode();
//                                    String resp = bk.getResponse();
//                                    String nwResmsg = bk.getNwrespmessg();
//                                    String nwtxnField = bk.getNwtxnrefid();
//                                    Logics.setVppBankDetails(BankValidation.this, AccNo, Ifsc, vppBkname, txnId, respCode, resp, networkrepcode, nwResmsg, nwtxnField, 1);
//                                    btnProceed.setVisibility(View.VISIBLE);
//                                    layName.setVisibility(View.VISIBLE);
//                                    txtAccountValidateNote.setText(bk.getC_name());
//                                    btnSubmit.setVisibility(View.INVISIBLE);
//                                    //Toast.makeText(BankValidation.this, ""+response.body(), Toast.LENGTH_SHORT).show();
//                                } else if (responsecode.equals("99")) {
//                                    Log.e("onResponse: ", "two");
//
//                                    if (!networkrepcode.equalsIgnoreCase("M5") || !!networkrepcode.equalsIgnoreCase("52")) {
//                                        String AccNo = bk.getAcc_no();
//                                        String Ifsc = bk.getIfsc();
//                                        String vppBkname = bk.getC_name();
//                                        String txnId = bk.getTxnid();
//                                        String respCode = bk.getRespcode();
//                                        String resp = bk.getResponse();
//                                        String nwResmsg = bk.getNwrespmessg();
//                                        String nwtxnField = bk.getNwtxnrefid();
//
//                                        String namearr[] = Logics.getfml(BankValidation.this);
//                                        String name = namearr[0] + " " + namearr[1] + " " + namearr[2];
//
//                                        Logics.setVppBankDetails(BankValidation.this, edtAccNo.getText().toString(), edtIfscCode.getText().toString(), name, txnId, respCode, resp, networkrepcode, nwResmsg, nwtxnField, 0);
//                                        btnProceed.setVisibility(View.VISIBLE);
//                                        layName.setVisibility(View.VISIBLE);
//                                        txtAccountValidateNote.setText(bk.getC_name());
//                                        btnSubmit.setVisibility(View.INVISIBLE);
//                                    } else {
//                                        // Toast.makeText(BankValidation.this, "" + "null", Toast.LENGTH_SHORT).show();
////                                TastyToast.makeText(getApplicationContext(), bk.getNwrespmessg(), TastyToast.LENGTH_LONG, TastyToast.WARNING);
//
//                                        AlertDialogClass.ShowMsg(BankValidation.this, bk.getNwrespmessg());
//
//                                    }
//                                    //Toast.makeText(BankValidation.this, ""+response.body(), Toast.LENGTH_SHORT).show();
//                                } else if (responsecode.equals("01")) {
//                                    Log.e("onResponse: ", "three");
//
//                                    String AccNo = bk.getAcc_no();
//                                    String Ifsc = bk.getIfsc();
//                                    String vppBkname = bk.getC_name();
//                                    String txnId = bk.getTxnid();
//                                    String respCode = bk.getRespcode();
//                                    String resp = bk.getResponse();
//                                    String nwResmsg = bk.getNwrespmessg();
//                                    String nwtxnField = bk.getNwtxnrefid();
//
//                                    String namearr[] = Logics.getfml(BankValidation.this);
//                                    String name = namearr[0] + " " + namearr[1] + " " + namearr[2];
//
//                                    Logics.setVppBankDetails(BankValidation.this, edtAccNo.getText().toString().trim(), edtIfscCode.getText().toString().trim(), name, txnId, respCode, resp, networkrepcode, nwResmsg, nwtxnField, 0);
//                                    btnProceed.setVisibility(View.VISIBLE);
//                                    layName.setVisibility(View.VISIBLE);
//                                    txtAccountValidateNote.setText(bk.getC_name());
//                                    btnSubmit.setVisibility(View.INVISIBLE);
//                                    //Toast.makeText(BankValidation.this, ""+response.body(), Toast.LENGTH_SHORT).show();
//                                } else if (responsecode.equals("02")) {
//                                    Log.e("onResponse: ", "four");
//
//                                    String AccNo = bk.getAcc_no();
//                                    String Ifsc = bk.getIfsc();
//                                    String vppBkname = bk.getC_name();
//                                    String txnId = bk.getTxnid();
//                                    String respCode = bk.getRespcode();
//                                    String resp = bk.getResponse();
//                                    String nwResmsg = bk.getNwrespmessg();
//                                    String nwtxnField = bk.getNwtxnrefid();
//
//                                    String namearr[] = Logics.getfml(BankValidation.this);
//                                    String name = namearr[0] + " " + namearr[1] + " " + namearr[2];
//
//                                    Logics.setVppBankDetails(BankValidation.this, edtAccNo.getText().toString(), edtIfscCode.getText().toString(), name, txnId, respCode, resp, networkrepcode, nwResmsg, nwtxnField, 0);
//                                    btnProceed.setVisibility(View.VISIBLE);
//                                    layName.setVisibility(View.VISIBLE);
//                                    txtAccountValidateNote.setText(bk.getC_name());
//                                    btnSubmit.setVisibility(View.INVISIBLE);
//                                    //Toast.makeText(BankValidation.this, ""+response.body(), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    // Toast.makeText(BankValidation.this, "" + "null", Toast.LENGTH_SHORT).show();
////                            TastyToast.makeText(getApplicationContext(), bk.getNwrespmessg(), TastyToast.LENGTH_LONG, TastyToast.WARNING);
//
//                                    AlertDialogClass.ShowMsg(BankValidation.this, bk.getNwrespmessg());
//                                    AlertDialogClass.PopupWindowDismiss();
//
//
//                                }
//
//
//                            } else {
//                                // Toast.makeText(BankValidation.this, "Failed To Connect Server Try again", Toast.LENGTH_LONG).show();
////                        TastyToast.makeText(getApplicationContext(), bk.getNwrespmessg(), TastyToast.LENGTH_LONG, TastyToast.WARNING);
//
//                                AlertDialogClass.ShowMsg(BankValidation.this, bk.getNwrespmessg());
//                                AlertDialogClass.PopupWindowDismiss();
//
//                            }
//                        }else {
//
//                            switch (response.code()) {
//
//                                case 404:
//
////                            Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
//                                    err = "Server Not Found";
//                                    break;
//                                case 500:
//
////                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
//                                    err = "Server Unavailable";
//                                    break;
//                                case 503:
//
////                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
//                                    err = "Server Overloaded try after sometime";
//                                    break;
//                                default:
//
//                                    err = String.valueOf(response.code());
//                                    err = "Something went wrong try again.";
////                            Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//                            AlertDialogClass.ShowMsg(BankValidation.this, err+  "  Something went wrong please try after sometime.");
//                        }
//                    }else
//                        AlertDialogClass.ShowMsg(BankValidation.this,"we are not getting response from server.");
//                }
//                @Override
//                public void onFailure(Call<BankValidateData> call, Throwable t) {
//                    btnSubmit.setEnabled(true);
//
////                    Log.e("failure",   "onFailure: " + t.toString());
//
//                    AlertDialogClass.ShowMsg(BankValidation.this,t.getMessage());
//                    AlertDialogClass.PopupWindowDismiss();
//
//                }
//            });
//
//
//         /*   new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    getHTTPS_ResponseFromUrl(Const.URL_VerifyAccount,jsonObject.toString());
//
//                }
//            }).start();
//*/
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            FirebaseCrashlytics.getInstance().recordException(e);
////            AlertDialogClass.ShowMsg(BankValidation.this, e.getMessage());
//            btnSubmit.setEnabled(true);
//            AlertDialogClass.ShowMsg(BankValidation.this, "Something went wrong please try after sometime.");
//        }
//    }

    private void call_bankvarification() {
        try {
            AlertDialogClass.PopupWindowShow(BankValidation.this, mainlayout);
            JSONObject jsonObject = new JSONObject();
            String strAccNo = edtAccNo.getText().toString().trim().toUpperCase();

            String strIfsc = edtIfscCode.getText().toString().trim().toUpperCase();

            jsonObject.put("ifsc", strIfsc);
            jsonObject.put("acc_no", strAccNo);
            jsonObject.put("login_id", "VPPAPP");

            byte[] data = jsonObject.toString().getBytes();
            Log.e("splashScreen", "connected: " + jsonObject.toString());
            new SendTOServer(BankValidation.this, BankValidation.this, Const.MSGBANKVERIFICATION, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void call_bankUpdateAccountIfsc() {
        try {
            AlertDialogClass.PopupWindowShow(BankValidation.this, mainlayout);
            JSONObject jsonObject = new JSONObject();

            String AccNo = Logics.getBankAccNo(BankValidation.this);
            String Ifsc = Logics.getBankIfsc(BankValidation.this);
            String vppBkname = Logics.getVppBankName(BankValidation.this);
            jsonObject.put("vpp_id", Logics.getVppId(BankValidation.this));
            jsonObject.put("bank_acc_no", AccNo);
            jsonObject.put("ifsc", Ifsc);
            jsonObject.put("micr_no", "");
            jsonObject.put("bank_name", vppBkname);
            jsonObject.put("branch_name", "");
            jsonObject.put("branch_address", "");
            jsonObject.put("bank_acc_no_old", getIntent().getStringExtra("acc")); // new params added
            jsonObject.put("ifsc_old", ""); // new params added

            byte[] data = jsonObject.toString().getBytes();
            Log.e("splashScreen", "connected: " + jsonObject.toString());
            new SendTOServer(BankValidation.this, BankValidation.this, Const.MSG_UPDATEACCOUNT_IFSCCODE, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getHTTPS_ResponseFromUrl(String url, String jsonObject) {
        String resultString = "";
        HttpResponse httpResponse = null;
        try {
            BufferedReader reader = null;
            StringBuilder sb;

            URL url1 = new URL(url);

            //TODO Comment for ssl
            //HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

            //TODO UnComment for ssl
            HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();
            //connection.setHostnameVerifier(new DummyHostnameVerifier());

            connection.setRequestMethod("POST");

            // enable writing output to this url
            connection.setDoOutput(true);

            connection.setConnectTimeout(15 * 60 * 1000);
            connection.setReadTimeout(15 * 60 * 1000);

            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonObject);
            writer.flush();
            writer.close();


            resCode = connection.getResponseCode();
            resMsg = connection.getResponseMessage();


            InputStream in;
            if (resCode != HttpURLConnection.HTTP_OK)
                in = connection.getErrorStream();
            else
                in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            sb.append(resCode).append(" : ").append(resMsg);
            resultString = sb.toString();

            Log.d("URL_RESULT", resultString);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);

        } catch (IOException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        return resultString;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_verify_AccNo: {
                if (Connectivity.getNetworkState(getApplicationContext())) {
//                    btn_verify_AccNo.setEnabled(false);
                    if (edtAccNo.getText().toString().equalsIgnoreCase("")) {

                        edtAccNo.setError("Enter Account number.");

                        edtAccNo.startAnimation(shakeError());
//                        TastyToast.makeText(BankValidation.this,"Enter Account number",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
//                        btn_verify_AccNo.setEnabled(true);
                    } else if (edtIfscCode.getText().toString().equalsIgnoreCase("")) {
//                        TastyToast.makeText(BankValidation.this,"Enter IFSC code",TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                        edtIfscCode.setError("Enter IFSC code.");

                        edtIfscCode.startAnimation(shakeError());

                        //                        btn_verify_AccNo.setEnabled(true);
                    } else if (!Methods.isValidIFSCCode(edtIfscCode.getText().toString().trim())) {

                        edtIfscCode.setError("Enter Valid IFSC code.");
                        edtIfscCode.startAnimation(shakeError());
//                        TastyToast.makeText(BankValidation.this,"Enter Valid IFSC code",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
//                        btn_verify_AccNo.setEnabled(true);

                    } else {
//                    fetchBankResponse();
                        call_bankvarification();
                    }

                } else {
                    TastyToast.makeText(BankValidation.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }

            break;
//            case R.id.btn_edt_name: {
//                showPopupScreen();
//            }
//            break;
            case R.id.btn_bankProceed: {
                if (Connectivity.getNetworkState(getApplicationContext())) {
                    btnProceed.setEnabled(false);
                    saveBankDetails();
                    //call_bankUpdateAccountIfsc();


                } else {
                    TastyToast.makeText(BankValidation.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
                //Intent intent=new Intent(BankValidation.this,SignupScreen2.class);
                //startActivity(intent);
                //saveBankVer();


            }
            break;
        }
    }


    private void saveBankDetails() {

        AlertDialogClass.PopupWindowShow(BankValidation.this, mainlayout);
        String AccNo = Logics.getBankAccNo(BankValidation.this);
        String Ifsc = Logics.getBankIfsc(BankValidation.this);
        String vppBkname = Logics.getVppBankName(BankValidation.this);
        String txnId = Logics.getVppTxnId(BankValidation.this);
        String respCode = Logics.getrespCode(BankValidation.this);
        String resp = Logics.getresponse(BankValidation.this);
        String nwRespCode = Logics.getnwrespcode(BankValidation.this);

        String nwResmsg = Logics.getnwrespmessg(BankValidation.this);
        String nwtxnField = Logics.getnwtxnrefid(BankValidation.this);

        JSONObject jsonObject = new JSONObject();
        try {
            // jsonObject.put("pan_no",pan_no);
            jsonObject.put("accno", AccNo);
            jsonObject.put("ifsc", Ifsc);
            jsonObject.put("cname", vppBkname);
            jsonObject.put("txnid", txnId);
            jsonObject.put("response", resp);
            jsonObject.put("respcode", respCode);
            jsonObject.put("networkrepcode", nwRespCode);
            jsonObject.put("nwResmsg", nwResmsg);
            jsonObject.put("nwtxnField", nwtxnField);

            byte[] data = jsonObject.toString().getBytes();
            new SendTOServer(this, this, Const.MSGSAVEBANKDETAILS, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(BankValidation.this, e.getMessage());
            btnProceed.setEnabled(true);

        }

    }

    @Override
    public void requestSent(int value) {

    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AlertDialogClass.PopupWindowDismiss();


            switch (msg.arg1) {


//                case Const.MSGUPDATENAMEBANK: {
//                    try {
////                        ringProgressDialog.dismiss();
//                        AlertDialogClass.PopupWindowDismiss();
//
//                        String data = (String) msg.obj;
//                        Log.d("Message", "handleMessagePan: " + data);
//                        JSONObject jsonObject = null;
//                        jsonObject = new JSONObject(data);
//                        int status = jsonObject.getInt("status");
//                        String message = jsonObject.getString("message");
//                        if (status != 0) {
//                            Toast.makeText(BankValidation.this, "Name Update :" + message, Toast.LENGTH_SHORT).show();
//
//
//                            //  btnEdtName.setVisibility(View.GONE);
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        FirebaseCrashlytics.getInstance().recordException(e);
//                        AlertDialogClass.ShowMsg(BankValidation.this,e.getMessage());
//
//                    }
//                }
//                break;
                case Const.MSGSAVEBANKDETAILS: {
                    try {

//                        ringProgressDialog.dismiss();
                        AlertDialogClass.PopupWindowDismiss();
                        btnProceed.setEnabled(true);

                        String data = (String) msg.obj;
                        Log.d("Message", "handleMessagePan: " + data);
//                        Toast.makeText(BankValidation.this, "Name Update :" + data, Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(data);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            if (getIntent().getStringExtra("from").equalsIgnoreCase("profile")) {
                                call_bankUpdateAccountIfsc();
                            } else {
                                Intent intent = new Intent(BankValidation.this, SignupScreen2.class);
                                startActivity(intent);
                            }

                        } else {
                            String Message = jsonObject.getString("Message");
                            AlertDialogClass.ShowMsg(BankValidation.this, Message);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialogClass.ShowMsg(BankValidation.this, e.getMessage());
                        btnProceed.setEnabled(true);

                    }
                }
                break;
                case Const.MSGBANKVERIFICATION: {
                    String data = (String) msg.obj;
                    Log.d("Message", "handleMessagePan: " + data);
//                    Toast.makeText(BankValidation.this, "verification :" + data, Toast.LENGTH_SHORT).show();
                    Log.e("data", data);
                    JSONObject jsonObject = null;

                    Methods.hideKeyboard(BankValidation.this, mainlayout);
                    try {
                        jsonObject = new JSONObject(data);

                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                            AlertDialogClass.PopupWindowDismiss();
                            String data1 = jsonObject.getString("bank_response");

                            Log.e("data1", data1);
                            Gson g = new Gson();
                            BankValidateData bk = g.fromJson(data1, BankValidateData.class);
                            if (bk != null) {
                                String networkrepcode = bk.getNwrespcode();
                                String responsecode = bk.getRespcode();
                                String AccNo = bk.getAcc_no();
                                String Ifsc = bk.getIfsc();
                                String vppBkname = bk.getC_name();
                                String txnId = bk.getTxnid();
                                String respCode = bk.getRespcode();
                                String resp = bk.getResponse();
                                String nwResmsg = bk.getNwrespmessg();
                                String nwtxnField = bk.getNwtxnrefid();
                                String namearr[] = Logics.getfml(BankValidation.this);
                                String name = namearr[0] + " " + namearr[1] + " " + namearr[2];
                                layName.setVisibility(View.VISIBLE);
                                txtAccountValidateNote.setText(bk.getC_name());

                                if (responsecode.equals("00")) {
                                    btnProceed.setVisibility(View.VISIBLE);
                                    btn_verify_AccNo.setVisibility(View.INVISIBLE);

                                    Logics.setVppBankDetails(BankValidation.this, AccNo, Ifsc, vppBkname, txnId, respCode, resp, networkrepcode, nwResmsg, nwtxnField, 1);

                                    //Toast.makeText(BankValidation.this, ""+response.body(), Toast.LENGTH_SHORT).show();
                                } else if (responsecode.equals("99")) {

                                    if (!networkrepcode.equalsIgnoreCase("M5") || !!networkrepcode.equalsIgnoreCase("52")) {

                                        Log.e("onResponse: ", "99999");

                                        Logics.setVppBankDetails(BankValidation.this, edtAccNo.getText().toString().trim(), edtIfscCode.getText().toString().trim(), name, txnId, respCode, resp, networkrepcode, nwResmsg, nwtxnField, 0);
                                        txtAccountValidateNote.setText(bk.getNwrespmessg() + "\n Opps!! something went wrong please contact to customer support team");

                                    } else {
                                        AlertDialogClass.ShowMsg(BankValidation.this, bk.getNwrespmessg());

                                    }
                                    //Toast.makeText(BankValidation.this, ""+response.body(), Toast.LENGTH_SHORT).show();
                                } else if (responsecode.equals("01")) {
                                    Log.e("onResponse: ", "three");

                                    Logics.setVppBankDetails(BankValidation.this, edtAccNo.getText().toString().trim(), edtIfscCode.getText().toString().trim(), name, txnId, respCode, resp, networkrepcode, nwResmsg, nwtxnField, 0);
                                    txtAccountValidateNote.setText(bk.getNwrespmessg() + "\n Opps!! something went wrong please contact to customer support team");

                                } else if (responsecode.equals("02")) {
                                    Log.e("onResponse: ", "four");

                                    Logics.setVppBankDetails(BankValidation.this, edtAccNo.getText().toString().trim(), edtIfscCode.getText().toString().trim(), name, txnId, respCode, resp, networkrepcode, nwResmsg, nwtxnField, 0);
                                    txtAccountValidateNote.setText(bk.getNwrespmessg() + "\n Opps!! something went wrong please contact to customer support team");


                                } else {

                                    AlertDialogClass.ShowMsg(BankValidation.this, bk.getNwrespmessg() + "\n Opps!! something went wrong please contact to customer support team");
                                    AlertDialogClass.PopupWindowDismiss();
                                    //show error popup in excetion too.
                                    Log.e("onResponse: ", "00000");


                                }
                            }

                        } else {
                            TastyToast.makeText(BankValidation.this, "" + jsonObject.getString("message"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            //  btn_verify_AccNo.setEnabled(true);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        AlertDialogClass.PopupWindowDismiss();
                        AlertDialogClass.ShowMsg(BankValidation.this, "Opps!! something went wrong please contact to customer support team");
                        Log.e("onResponse: ", "four");

                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertDialogClass.PopupWindowDismiss();
                        AlertDialogClass.ShowMsg(BankValidation.this, "Opps!! something went wrong please contact to customer support team");
                        Log.e("onResponse: ", "5555");

                    }
                }
                break;
                case Const.MSG_UPDATEACCOUNT_IFSCCODE: {
                    String data = (String) msg.obj;
                    Log.e("ACCOUNT_IFSCCODE", data);
                    JSONObject jsonObject = null;

                    Methods.hideKeyboard(BankValidation.this, mainlayout);
                    try {
                        jsonObject = new JSONObject(data);

                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            AlertDialogClass.PopupWindowDismiss();
                            alert(jsonObject.getString("mesage"), 1);
                            TastyToast.makeText(getApplicationContext(), jsonObject.getString("mesage"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        } else {
                            TastyToast.makeText(BankValidation.this, "" + jsonObject.getString("message"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
//                            btn_verify_AccNo.setEnabled(true);
                            alert(jsonObject.getString("mesage"), 0);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AlertDialogClass.PopupWindowDismiss();
                        AlertDialogClass.ShowMsg(BankValidation.this, "Opps!! something went wrong please contact to customer support team");
                        Log.e("onResponse: ", "four");

                    }
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (getIntent().getStringExtra("from").equalsIgnoreCase("profile")) {
            super.onBackPressed();

        }
    }


    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(BankValidation.this),
                            "1",
                            Methods.getVersionInfo(BankValidation.this),
                            Methods.getlogsDateTime(), "BankValidation",
                            Connectivity.getNetworkState(getApplicationContext()),
                            BankValidation.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(BankValidation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        Log.e("connected: ", "connected");
        AlertDialogClass.PopupWindowDismiss();

        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    saveBankDetails();
                else
                    Views.SweetAlert_NoDataAvailble(BankValidation.this, "No Internet");
                //   TastyToast.makeText(BankValidation.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        btnProceed.setEnabled(true);
//        btn_verify_AccNo.setEnabled(true);


        AlertDialogClass.PopupWindowDismiss();

        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(BankValidation.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(BankValidation.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        btnProceed.setEnabled(true);
//        btnSubmit.setEnabled(true);
        AlertDialogClass.PopupWindowDismiss();


        Views.SweetAlert_NoDataAvailble(BankValidation.this, "Internet Not Available");
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
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        AlertDialogClass.PopupWindowDismiss();

//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(BankValidation.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(BankValidation.this, connectionProcess, "Server Not Available");
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
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        btnProceed.setEnabled(true);
//        btnSubmit.setEnabled(true);
        AlertDialogClass.PopupWindowDismiss();

        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(BankValidation.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(BankValidation.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(BankValidation.this),
                                    "0",
                                    Methods.getVersionInfo(BankValidation.this),
                                    Methods.getlogsDateTime(), "BankValidation",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    BankValidation.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!BankValidation.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                //  Toast.makeText(BankValidation.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess == null) {
                Log.e("DlgConnectSocket11111_null", "called");

            } else {
                new ConnectTOServer(BankValidation.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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


    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }


    private void alert(String msg, int i) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.
                verify_update1, null);
        builder.setView(dialogView);

        TextView txtMsg = dialogView.findViewById(R.id.txtMsg);
        TextView txtMsg1 = dialogView.findViewById(R.id.txtMsg1);

        if (i == 1) {
            txtMsg.setVisibility(View.VISIBLE);
            txtMsg1.setVisibility(View.GONE);
        } else {
            txtMsg.setVisibility(View.GONE);
            txtMsg1.setVisibility(View.VISIBLE);

        }
        txtMsg.setText(msg);
        ImageView txtclose = dialogView.findViewById(R.id.forgot_closeimage);


        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                alertDialog.cancel();

                startActivity(new Intent(BankValidation.this, Profile.class).putExtra("from", "").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

        builder.setCancelable(true);
        alertDialog = builder.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
}
