package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.ReadTaskPost;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.NetProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class PanValidation extends AppCompatActivity implements NetProcess, RequestSent, ConnectionProcess {
    TextView txtName;
//    TextView txtWarning;
    RelativeLayout layName;
//    ConstraintLayout layout_pan_validation;
    FancyButton  btnProceedPAN, btnsubmitName;
//    FancyButton btnSubmitPAN;
//    FancyButton btnEdtName;
    EditText edtPan;
    public static Handler handlerPan;
    public String fname, lname, mname, name, pan;
    //    ProgressDialog ringProgressDialog;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;

    CoordinatorLayout mainlayout;
    int MaxTry=0;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    TextView txt_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_validation1);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        txtName = findViewById(R.id.txtName);
        mainlayout = findViewById(R.id.mainlayout);
        txt_login = findViewById(R.id.txt_login);

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PanValidation.this, LoginScreen.class));
            }
        });


//        txtWarning = findViewById(R.id.txtWarning);
       // btnSubmitPAN = findViewById(R.id.btnSubmitPAN);
        edtPan = findViewById(R.id.edt_pan_no);
        btnProceedPAN = findViewById(R.id.btnProceedPAN);

//        btnEdtName = findViewById(R.id.btnEditName);
//        layout_pan_validation = (ConstraintLayout) findViewById(R.id.layout_pan_validation);
        handlerPan = new ViewHandler();

        edtPan.setFocusable(true);
        btnProceedPAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DlgCnfrtmn();
                validate();
            }
        });




//        btnEdtName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showPopupScreen();
//
//            }
//        });

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PanValidation.this, LoginScreen.class);
//                startActivity(intent);
//                SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");
//            }
//        });

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList,"SocketLogs", PanValidation.this);

    }

    private void showPopupScreen() {


        final EditText edtFName;
        final EditText edtMName;
        final EditText edtLName;
        ImageView imgClose;
        FancyButton btnEditName;

        final PopupWindow popupWindow;
        View customView;

        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);


        customView = layoutInflater.inflate(R.layout.name_edit_popup, null);

        edtFName = (EditText) customView.findViewById(R.id.edtFNamePopup);
        edtMName = (EditText) customView.findViewById(R.id.edtMNamePopup);

        edtLName = (EditText) customView.findViewById(R.id.edtLNamePopup);
        btnsubmitName = (FancyButton) customView.findViewById(R.id.btnSubmitName);
        imgClose = (ImageView) customView.findViewById(R.id.imgClose);


        Log.d("display", "showPopupScreen: ");

        popupWindow = new PopupWindow(customView, ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
        popupWindow.setWidth(width - 100);
        popupWindow.setHeight(height - 100);
        popupWindow.setFocusable(true);

        View view = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.activity_pan_validation, null);
        ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.layout_pan_validation);
        popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);
        btnsubmitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname = edtFName.getText().toString().toUpperCase().trim();
                mname = edtMName.getText().toString().toUpperCase().trim();
                lname = edtLName.getText().toString().toUpperCase().trim();
                if (fname.isEmpty() || lname.isEmpty() || mname.isEmpty()) {
                    Toast.makeText(PanValidation.this, "Please Enter Valid Name", Toast.LENGTH_SHORT).show();
                } else {
                    Logics.setfmlName(PanValidation.this, fname, mname, lname);
                    Toast.makeText(PanValidation.this, "Name is Changed to " + fname + " " + mname + " " + lname, Toast.LENGTH_SHORT).show();

//                    txtWarning.setVisibility(View.GONE);
//                    btnEdtName.setVisibility(View.GONE);

                    //sendData(fname,mname,lname);
                }
            }
        });


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });


    }

    private void validate() {

        String panNo = edtPan.getText().toString().toUpperCase();
        if (panNo.matches("") || panNo.length() < 10) {
            Toast.makeText(this, "Please enter valid PAN", Toast.LENGTH_LONG).show();
            btnProceedPAN.setEnabled(true);
        } else if (!panNo.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
            btnProceedPAN.setEnabled(true);
            edtPan.setError("Enter Valid  PAN no");
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

            sendData(panNo);

//            ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
//            list.add(new BasicNameValuePair("pan", panNo));
//            list.add(new BasicNameValuePair("roId", "r1659"));
//            list.add(new BasicNameValuePair("leadNo", "1000"));
//
//            new ReadTaskPost(PanValidation.this, PanValidation.this, list).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Const.URL_PANVerifyClient);


        }
    }

    @Override
    public void process(String result, int http_response_code) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            Log.e("result", "process: " + jsonObject.toString() + " , " + http_response_code);

            if (http_response_code == 200) {    // success
                String name = jsonObject.getString("name");
                Log.d("name", "fetched: " + name);
                pan = jsonObject.getString("pan").toUpperCase();

                if (name.length() > 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    String strName = "PAN entered belongs to <font fgcolor='#191442'> <b>" + name + " </b></font> as per Income Tax website.";
                    txtName.setVisibility(View.VISIBLE);
                    txtName.setText(Html.fromHtml(strName));
                    String[] fmlName = Logics.getfml(this);
                    //.   stringMatched(name, fmlName[0], fmlName[1], fmlName[2]);
//                    btnLogin.setVisibility(View.GONE);
                    btnProceedPAN.setVisibility(View.VISIBLE);

                    btnProceedPAN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPref.savePreferences1(PanValidation.this,"isPan","1");
                            SharedPref.savePreferences1(PanValidation.this,"panNo",pan);
                            SharedPref.savePreferences1(PanValidation.this,"panName",name.toUpperCase());

                            //Logics.setVppPanDetails(PanValidation.this, pan, name.toUpperCase(), 1);
                            //Logics.setLoginPan(PanValidation.this, pan);

                            Intent intent = new Intent(PanValidation.this, BankValidation.class);
                            intent.putExtra("from","pan");
                            intent.putExtra("acc", "");

                            startActivity(intent);
                            // savePan(pan);
                        }
                    });
                } else {
                    txtName.setText("could not verify your PAN with income tax site");
                    txtName.setVisibility(View.VISIBLE);
                }

            }


            //temp comment ...
/*
            else {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(layout_pan_validation.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
//                String strName = "PAN entered belongs to <b>" + name + " </b> as per Income Tax website.";
//                txtName.setText(Html.fromHtml(strName));
                String[] fmlName = Logics.getfml(this);
//                stringMatched(name, fmlName[0], fmlName[1], fmlName[2]);
                btnSubmitPAN.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.GONE);
                btnProceedPAN.setVisibility(View.VISIBLE);
                btnProceedPAN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logics.setVppPanDetails(PanValidation.this, edtPan.getText().toString().toUpperCase(), ""+fmlName[0]+ fmlName[1]+ fmlName[2], 1);
                        Logics.setLoginPan(PanValidation.this,  edtPan.getText().toString().toUpperCase());
                        Intent intent = new Intent(PanValidation.this, BankValidation.class);
                        startActivity(intent);
                        // savePan(pan);
                    }
                });
            }
*/


        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            ShowMsg(PanValidation.this, "Something went wrong please try after sometime.");

        }
    }


    private void stringMatched(String s1, String fName, String mName, String lName) {

        int percentage = 0;

        btnProceedPAN.setVisibility(View.VISIBLE);
        String[] panEntered = s1.split(" ");


        if (s1.contains(fName)) {

            percentage = percentage + 100 / 3;

            Log.d("1", "stringMatched: " + percentage + " , " + fName);

        }
        ;


        if (s1.contains(mName)) {

            percentage = percentage + 100 / 3;


            Log.d("2", "stringMatched: " + percentage + " , " + mName);

        }
        ;

        if (s1.contains(lName)) {

            percentage = percentage + 100 / 3;

            Log.d("3", "stringMatched: " + percentage + " , " + lName);

        }
        ;

        Log.d("percentage", "stringMatched: " + percentage);

        if (percentage < 90) {
//            txtWarning.setText(Html.fromHtml("<b> Note* : </b> Name entered & Name fetched from Income Tax website are not exact match, \n Do you wish to?  <b>"));

//            btnEdtName.setVisibility(View.VISIBLE);


        } else {
//            txtWarning.setText(Html.fromHtml("Name entered & Name fetched from IT is  <b>Exact Match</b>."));
//
//            btnEdtName.setVisibility(View.GONE);

        }


    }

    private void sendData(String panNo) {

//        ringProgressDialog = ProgressDialog.show(PanValidation.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        btnProceedPAN.setEnabled(false);

        AlertDialogClass.PopupWindowShow(PanValidation.this, mainlayout);

        JSONObject jsonObject = new JSONObject();
        try {
            //data:{"result":"{\"result\":1,\"msg\":\"PAN Number verified\",\"panstatus\":1,\"kratype\":\"\",\"hpan\":\" \",\"reqType\":1,\"name\":\"Shri SHIVAKUMAR LAXMIRAJAM TUMMA\",\"pan\":\"AZTPT4416B\"}","Errormsg":"PAN API working fine","message":"New PAN","pan":"AZTPT4416B","status":1,"http_response_code":200} msgCode: 32
            //data:{"result":"{\"result\":0,\"msg\":\"PAN Number verified\",\"panstatus\":1,\"kratype\":\"\",\"hpan\":\" \",\"reqType\":1,\"name\":\"Shri SAMEER ARUN CHINCHOLE\",\"pan\":\"AGMPC6919Q\"}","Errormsg":"PAN API working fine","message":"New PAN","pan":"AGMPC6919Q","status":1,"http_response_code":200}

            // jsonObject.put("pan_no",pan_no);
            jsonObject.put("pan", panNo); //BNWPP1278F
            jsonObject.put("regpegno", 2);

            byte[] data = jsonObject.toString().getBytes();
            new SendTOServer(this, this, Const.MSGCHECKPAN, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            btnProceedPAN.setEnabled(true);

            AlertDialogClass.ShowMsg(PanValidation.this, ""+e.getMessage());


        }

    }

    @Override
    public void requestSent(int value) {

    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            if (ringProgressDialog != null) {
//                ringProgressDialog.dismiss();
//            }

            AlertDialogClass.PopupWindowDismiss();

            switch (msg.arg1) {

//                case Const.MSGSAVEPAN: {
//
//                    try {
//                        ringProgressDialog.dismiss();
//
//                        String data = (String) msg.obj;
//                        Log.d("Message", "handleMessagePan: " + data);
//                        JSONObject jsonObject = null;
//                        jsonObject = new JSONObject(data);
//
//                        int status = jsonObject.getInt("status");
//                        if (status != 0) {
//                            int isEmp = jsonObject.getInt("isEmp");
//                            Intent intent = new Intent(PanValidation.this, BankValidation.class);
//                            startActivity(intent);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;

                case Const.MSGCHECKPAN: {
                    btnProceedPAN.setEnabled(true);
                    try {
                        //ringProgressDialog.dismiss();

                        AlertDialogClass.PopupWindowDismiss();
                        String data = (String) msg.obj;
                        Log.d("Message", "handleMessagePan: " + data);
                        JSONObject jsonObject1 = null;
                        //data = data.replaceAll("\\\\","");

                        jsonObject1 = new JSONObject(data);
                        int status = jsonObject1.getInt("status");
                        String message = jsonObject1.getString("message");


                        //data:{"result":"{\"result\":1,\"msg\":\"PAN Number verified\",\"panstatus\":1,\"kratype\":\"\",\"hpan\":\" \",\"reqType\":1,\"name\":\"Shri SHIVAKUMAR LAXMIRAJAM TUMMA\",\"pan\":\"AZTPT4416B\"}","Errormsg":"PAN API working fine","message":"New PAN","pan":"AZTPT4416B","status":1,"http_response_code":200} msgCode: 32


                        if (status == 0) {
                            //TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.INFO);

                            if (message.contains("PAN number already")){

                                txt_login.setVisibility(View.VISIBLE);
                            }

                        } else {
                            txt_login.setVisibility(View.GONE);
//                            btnLogin.setVisibility(View.GONE);
//                            txtWarning.setVisibility(View.GONE);
//                            String panNo = jsonObject.getString("pan");
//                            ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
//                            list.add(new BasicNameValuePair("pan", panNo));
//                            list.add(new BasicNameValuePair("roId", "r1659"));
//                            list.add(new BasicNameValuePair("leadNo", "1000"));
//
//                            new ReadTaskPost(PanValidation.this, PanValidation.this, list).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Const.URL_PANVerifyClient);

                            //data:{"result":"{\"result\":0,\"msg\":\"PAN Number verified\",\"panstatus\":1,\"kratype\":\"\",\"hpan\":\" \",\"reqType\":1,\"name\":\"Shri SAMEER ARUN CHINCHOLE\",\"pan\":\"AGMPC6919Q\"}","Errormsg":"PAN API working fine","message":"New PAN","pan":"AGMPC6919Q","status":1,"http_response_code":200}


                            int http_response_code = jsonObject1.getInt("http_response_code");
                            if (http_response_code == 200) {    // success
                                String sss = jsonObject1.getString("result");
                                Log.e("result",sss);
                                JSONObject result = new JSONObject(sss);
                                String name = result.getString("name");
                                int panstatus = result.getInt("panstatus");
                                String pan_msg = result.getString("msg");
                                Log.d("name", "fetched: " + name);
                                pan = result.getString("pan").toUpperCase();

                                if (panstatus ==1) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                                    String strName = "PAN entered belongs to <font fgcolor='#191442'> <b>" + name + " </b></font> as per Income Tax website.";
                                    txtName.setText(Html.fromHtml(strName));
                                    txtName.setVisibility(View.VISIBLE);
                                    String[] fmlName = Logics.getfml(PanValidation.this);
                                    //stringMatched(name, fmlName[0], fmlName[1], fmlName[2]);
                                    btnProceedPAN.setVisibility(View.INVISIBLE);
//                                    btnLogin.setVisibility(View.GONE);
                                    btnProceedPAN.setVisibility(View.VISIBLE);

//                                    btnProceedPAN.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Logics.setVppPanDetails(PanValidation.this, pan, name.toUpperCase(), 1);
//                                            Logics.setLoginPan(PanValidation.this, pan);
//                                            Intent intent = new Intent(PanValidation.this, BankValidation.class);
//                                            intent.putExtra("from","pan");
//                                            intent.putExtra("acc","");
//                                            startActivity(intent);
//
//
//
//                                            // savePan(pan);
//                                        }
//                                    });

                                    btnProceedPAN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            SharedPref.savePreferences1(PanValidation.this,"isPan","1");
                                            SharedPref.savePreferences1(PanValidation.this,"panNo",pan);
                                            SharedPref.savePreferences1(PanValidation.this,"panName",name.toUpperCase());

                                            //Logics.setVppPanDetails(PanValidation.this, pan, name.toUpperCase(), 1);
                                            //Logics.setLoginPan(PanValidation.this, pan);

                                            Intent intent = new Intent(PanValidation.this, BankValidation.class);
                                            intent.putExtra("from","pan");
                                            intent.putExtra("acc", "");
                                            startActivity(intent);
                                            // savePan(pan);
                                        }
                                    });

                                } else {
                                    txtName.setText(""+pan_msg);
                                    txtName.setVisibility(View.VISIBLE);
                                    //txtName.setText("could not verify your PAN with income tax site");
                                }

                            }else{
                                AlertDialogClass.ShowMsg(PanValidation.this, " "+jsonObject1.getString("Errormsg"));
                            }




                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialogClass.ShowMsg(PanValidation.this, "here "+e.getMessage()+"\n Opps!! something went wrong please contact to customer support team");
                        btnProceedPAN.setEnabled(true);

                    }

                }

                break;
                case Const.MSGUpdateEMailMobile: {
                    try {
                        //ringProgressDialog.dismiss();


                        AlertDialogClass.PopupWindowDismiss();

                        String data = (String) msg.obj;
                        Log.e("MSGUpdateEMailMobile: ", data);
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(data);
                        int status = jsonObject.getInt("status");
                        String message = jsonObject.getString("message");

                        if (status == 0) {
                           // TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.INFO);
                            //  Toast.makeText(PanValidation.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                           // TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.INFO);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
    }

    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        btnProceedPAN.setEnabled(true);

        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(PanValidation.this),
                            "1",
                            Methods.getVersionInfo(PanValidation.this),
                            Methods.getlogsDateTime(), "PanValidation",
                            Connectivity.getNetworkState(getApplicationContext()),
                            PanValidation.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(PanValidation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        AlertDialogClass.PopupWindowDismiss();
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    validate();
                else
                    Views.SweetAlert_NoDataAvailble(PanValidation.this, "No Internet");
                //TastyToast.makeText(PanValidation.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        btnProceedPAN.setEnabled(true);

        AlertDialogClass.PopupWindowShow(PanValidation.this, mainlayout);

        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(PanValidation.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(PanValidation.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        btnProceedPAN.setEnabled(true);

        AlertDialogClass.PopupWindowDismiss();

        Views.SweetAlert_NoDataAvailble(PanValidation.this, "Internet Not Available");
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
        btnProceedPAN.setEnabled(true);


        AlertDialogClass.PopupWindowDismiss();
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(PanValidation.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(PanValidation.this, connectionProcess, "Server Not Available");
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
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        btnProceedPAN.setEnabled(true);

        AlertDialogClass.PopupWindowDismiss();
        Log.e("SocketDisconnected_x: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(PanValidation.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(PanValidation.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //    public void DlgCnfrtmn() {
//        // 2. Confirmation message
//        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PanValidation.this, SweetAlertDialog.SUCCESS_TYPE);
//        sweetAlertDialog.setTitleText("We will send otp to given mobile and email");
//        sweetAlertDialog.setCancelable(false);
//        sweetAlertDialog.setContentText("");
//        sweetAlertDialog.setConfirmText("Continue");
//        sweetAlertDialog.setCustomImage(R.drawable.vpp_logo);
//        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sDialog) {
//                sDialog.dismissWithAnimation();
//                sDialog.dismiss();
//                sDialog.cancel();
//
//            }
//        })
////                .show();
//                .setCancelButton("Change", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();
//    }
    @SuppressLint("LongLogTag")
    public void ProgressDlgConnectSocket(Context context, final ConnectionProcess connectionProcess, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        btnProceedPAN.setEnabled(true);

        Log.e( "DlgConnectSocket", "called");
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(PanValidation.this),
                                    "0",
                                    Methods.getVersionInfo(PanValidation.this),
                                    Methods.getlogsDateTime(), "PanValidation",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    PanValidation.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!PanValidation.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
               // Toast.makeText(PanValidation.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess==null){
                Log.e( "DlgConnectSocket11111_null", "called");

            }else {
                new ConnectTOServer(PanValidation.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                connectionProcess.ConnectToserver(connectionProcess);
            }
            Log.e( "DlgConnectSocket11111", "called");

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
    public  void ShowMsg(Context context, String msg) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle("VPP");
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setIcon(R.drawable.vpp_logo);
//        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        alertDialogBuilder.setPositiveButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                arg0.dismiss();
                arg0.cancel();
               // finish();
                //finishAffinity();
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
