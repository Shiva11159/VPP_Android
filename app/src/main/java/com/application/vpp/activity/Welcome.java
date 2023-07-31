package com.application.vpp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Views.Views;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class Welcome extends AppCompatActivity implements RequestSent, ConnectionProcess {
    TextView txtName, txt_version, txtMobile, txtVppId, txtCity, txtProfilePan, txtProfileEmail, txt_Dob;
    public static Handler handlerWelcome;
    int isSignup;
    byte[] data = null;
    ProgressDialog ringProgressDialog;
    CheckBox chkIagree;
    Button btnStartReferring;
    TextView txtTermsConditions;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    public static final String PREFS_NAME = "LoginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;

        SharedPref.savePreferences(getApplicationContext(), SharedPref.UPIPayment, "");

        if (getIntent().getExtras() != null) {
            isSignup = getIntent().getExtras().getInt("issignup", 0);
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("logged", "logged").apply();
            editor.commit();
        }

        txt_version = (TextView) findViewById(R.id.txt_version);
        txt_version.setText("v"+Methods.getVersionInfo(Welcome.this));
        txtName = (TextView) findViewById(R.id.txtProfileName);
        txtVppId = (TextView) findViewById(R.id.txtVppId);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtProfileEmail = (TextView) findViewById(R.id.txtProfileEmail);
        //txtCity = (TextView)findViewById(R.id.txtProfileCity);
        txtProfilePan = (TextView) findViewById(R.id.txtProfilePan);
//        chkIagree = (CheckBox) findViewById(R.id.imgCheckTerms);
        btnStartReferring = (Button) findViewById(R.id.btnStartReferring);
//        txtTermsConditions = (TextView) findViewById(R.id.txtIagree);
//        //  txt_Dob=(TextView)findViewById(R.id.txt_Dob);
//        btnStartReferring.setClickable(false);
//        btnStartReferring.setEnabled(false);

//        chkIagree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                if (b) {
//
//                    btnStartReferring.setBackgroundColor(getResources().getColor(R.color.btn_active));
//                    btnStartReferring.setClickable(true);
//                    btnStartReferring.setEnabled(true);
//                } else {
//
//                    btnStartReferring.setBackgroundColor(getResources().getColor(R.color.btn_active));
//                    btnStartReferring.setClickable(false);
//                    btnStartReferring.setEnabled(false);
//
//                }
//
//            }
//        });


//        txtTermsConditions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Welcome.this, TermsConditions.class);
//                startActivity(intent);
//
//            }
//        });

        btnStartReferring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.getNetworkState(getApplicationContext())) {

                    if (isSignup == 0) {
                        Intent intent = new Intent(Welcome.this, Dashboard.class);
                        //  Intent intent = new Intent(Welcome.this,DashoboardDesign.class);
                        startActivity(intent);
                    } else if (isSignup == 1) {
                        Intent intent = new Intent(Welcome.this, UploadDocScreen.class);
                        //  Intent intent = new Intent(Welcome.this,DashoboardDesign.class);
                        intent.putExtra("from","");
                        startActivity(intent);
                    }
                } else {
                    Views.SweetAlert_NoDataAvailble(Welcome.this, "Connect internet !");
                }
            }
        });
        handlerWelcome = new ViewHandler();
//        ringProgressDialog = ProgressDialog.show(ClientList.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));


        if (Logics.getVppId(Welcome.this)==null){
         //   sendData();
        }

//        if (Connectivity.getNetworkState(this)) {
//            if (Const.isSocketConnected) {
//            }else {
//
//                Views.toast(this,Const.checkConnection);
//
//            }
//        }



      String  mobile = SharedPref.getPreferences(Welcome.this,"mobileNo");

       String email = SharedPref.getPreferences(Welcome.this,"email");

       txtVppId.setText(Logics.getVppId(Welcome.this));
        txtName.setText(Logics.getVppName(Welcome.this));
        //  txtCity.setText(city);
        txtMobile.setText(mobile);
        txtProfilePan.setText(Logics.getPanNo(Welcome.this));
        txtProfileEmail.setText(email);

    }

    @Override
    public void requestSent(int value) {

    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (ringProgressDialog != null) {
                ringProgressDialog.dismiss();
            }

            Log.d("Message", "handleMessageWelcome: " + msg.toString());

            switch (msg.arg1) {
                case Const.MSGFETCHVPPDETAILS: {
                    String data = (String) msg.obj;
                    Log.e("vppDetails", "handleMessage: " + data);
                    try {

                        JSONObject jsonObject = new JSONObject(data);
                        String name = jsonObject.getString("name");
                        String city = jsonObject.getString("city");
                        String mobile = jsonObject.getString("mobile");
                        String email = jsonObject.getString("email");
                        String vppid = jsonObject.getString("vpp_id");
                        String pan_no = jsonObject.getString("pan_no");

                        Logics.setVppDetails(Welcome.this, name, mobile, email, city, vppid, pan_no);

                        Logics.setmobileNo(Welcome.this, mobile);  //save mble no ..


                        txtVppId.setText(vppid);
                        txtName.setText(name);
                        //  txtCity.setText(city);
                        txtMobile.setText(mobile);
                        txtProfilePan.setText(pan_no);
                        txtProfileEmail.setText(email);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }

                }
                break;
//                case Const.MSGVERIFYOTP: {
//                    String data = (String) msg.obj;
//
//                    Log.d("OTPVerified", "handleMessage: " + data);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(data);
//
//                        int status = jsonObject.getInt("status");
//
//                        if (status != 0) {
//                            sendData();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                break;


            }
        }
    }

//    private void sendData() {
//
//        ringProgressDialog = ProgressDialog.show(Welcome.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//        try {
//
//            String imei = Logics.getDeviceID(this);
//            String ip = Logics.getSimId(this);
//            if (android.os.Build.VERSION.SDK_INT >= 29) {
//                imei = Logics.getTokenID(Welcome.this);
//                ip = "12345";
//            }
//            String pan = Logics.getLoginPan(Welcome.this);
//            JSONObject jsonObject = new JSONObject();
////            jsonObject.put("imei",imei);
////            jsonObject.put("page",1);
////            jsonObject.put("size",10);
////            jsonObject.put("imei",imei);//change here pan no in req
//            jsonObject.put("pannum", pan);
//            //  jsonObject.put("simNo","1111111111111111111111");
//
//            byte data[] = jsonObject.toString().getBytes();
//
//            //   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            new SendTOServer(Welcome.this, Welcome.this, Const.MSGFETCHVPPDETAILS, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            FirebaseCrashlytics.getInstance().recordException(e);
//        }
//
//    }

    @Override
    public void connected() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        //        AlertDailog.ProgressDlgDiss();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (Connectivity.getNetworkState(getApplicationContext()))
//                    sendData();
//            }
//            //    else
////                    Views.SweetAlert_NoDataAvailble(Welcome.this,"No Internet");
//            // TastyToast.makeText(Welcome.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
//            //   }
//        });
    }

    @Override
    public void serverNotAvailable() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(Welcome.this, connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(Welcome.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }

        Views.SweetAlert_NoDataAvailble(Welcome.this, "Internet Not Available");
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
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(Welcome.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Views.ProgressDlgConnectSocket(Welcome.this, connectionProcess, "Server Not Available");
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
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(Welcome.this, connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(Welcome.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
