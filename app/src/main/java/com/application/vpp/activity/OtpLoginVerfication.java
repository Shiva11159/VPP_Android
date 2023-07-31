package com.application.vpp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Utility.SnackBar;
import com.application.vpp.Views.Views;
import com.goodiebag.pinview.Pinview;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class OtpLoginVerfication extends AppCompatActivity implements ConnectionProcess, RequestSent {

    byte[] data;

    String PREFS_NAME = "LoginPrefs";
    public static Handler otploginverifyhandler;

    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    CoordinatorLayout coordinatorLayout_mobile, coordinatorLayout_email;
    TextView txtOtpMobile, txtOtpEmail, resendotptxt_mobile, resendotptxt_email, txttimer_mobile, txttimer_email, txtmobileOTPverified, txtemailOTPverified;
    Pinview pinview_mobile, pinview_email;

    ScrollView mainlayout;
    int passwordattemptscount_mobile = 0;
    int passwordattemptscount_email = 0;

    boolean MobileOtpDone = false;
    boolean EmailOtpDone = false;

    CountDownTimer countDownTimer_mobile;
    CountDownTimer countDownTimer_email;
    boolean timer_mobile = false;
    boolean timer_email = false;

    String mobileOTP, emailOTP, mobileNo, emailId;

    String strmobileOTP, stremailOTP;
    TranslateAnimation animation;

    FancyButton btn_proceed;

    int MaxTry = 0;


    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    int resendMobileCount = 0;
    int resendEmailCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_login_verfication);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;

        Profile.handlerProfile = null;
        OtpVerfication.otpVerfhandler = null;
        OtpLoginVerfication.otploginverifyhandler = null;

        otploginverifyhandler = new ViewHandler();
        mainlayout = findViewById(R.id.mainlayout);
        btn_proceed = findViewById(R.id.btn_proceed);
        coordinatorLayout_mobile = findViewById(R.id.coordinatorLayout_mobile);
        coordinatorLayout_email = findViewById(R.id.coordinatorLayout_email);
        txtOtpMobile = findViewById(R.id.txtOtpMobile);
        txtOtpEmail = findViewById(R.id.txtOtpEmail);
        pinview_mobile = findViewById(R.id.pinview_mobile);
        pinview_email = findViewById(R.id.pinview_email);

        resendotptxt_mobile = findViewById(R.id.resendotptxt_mobile);
        resendotptxt_email = findViewById(R.id.resendotptxt_email);
        txttimer_mobile = findViewById(R.id.txttimer_mobile);
        txttimer_email = findViewById(R.id.txttimer_email);

        txtmobileOTPverified = findViewById(R.id.txtmobileOTPverified);
        txtemailOTPverified = findViewById(R.id.txtemailOTPverified);


        animation = new TranslateAnimation(100.0f, 0.0f, 100.0f, 0.0f);
        animation.setDuration(1000);  // animation duration
        animation.setRepeatCount(0);  // animation repeat count

        pinview_mobile.startAnimation(animation);

        mobileNo = getIntent().getStringExtra("mobileNum");
        emailId = getIntent().getStringExtra("emailId");
        mobileOTP = getIntent().getStringExtra("strOtp");
        emailOTP = getIntent().getStringExtra("emailOTP");

        txtOtpMobile.setText(mobileNo);
        txtOtpEmail.setText(emailId);

        MobileVerify(mobileOTP);

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList, "SocketLogs", OtpLoginVerfication.this);


        //


        resendotptxt_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendMobileCount > 2) {

                    closeapp("No more attempt for Resend OTP, try after sometime.");

                } else {
                    resendMobileCount++;
                    sendDataM(resendMobileCount, Integer.parseInt(mobileOTP));
                }
            }
        });


        //


        resendotptxt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resendEmailCount > 2) {
                    closeapp("No more attempt for Resend OTP, try after sometime.");
                } else {

                    resendEmailCount++;
                    sendDataE(resendEmailCount, Integer.parseInt(emailOTP));
                }
            }
        });


        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MobileOtpDone == false) {
                    SnackBar.SnackBar(coordinatorLayout_mobile, "Please enter mobile OTP");
                } else if (EmailOtpDone == false) {
                    SnackBar.SnackBar(coordinatorLayout_email, "Please enter Email OTP");
                } else if (MobileOtpDone == true && EmailOtpDone == true) {
//                    proceed();
                    fetchDetails();

                }
            }
        });

    }

    private void fetchDetails() {

        AlertDialogClass.PopupWindowShow(OtpLoginVerfication.this, mainlayout);
        try {
            String pan = Logics.getLoginPan(OtpLoginVerfication.this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pannum", pan);
            Log.e("fetchDetailsREQ: ", jsonObject.toString());
            byte data[] = jsonObject.toString().getBytes();
            new SendTOServer(OtpLoginVerfication.this, OtpLoginVerfication.this, Const.MSGFETCHVPPDETAILSONLOGIN, data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }


    void closeapp(String ss) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme1));
        builder.setMessage(ss)
                .setCancelable(false)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                       Dashboard.this.finish();
                        finishAffinity();
                        finish();
                        dialog.dismiss();

                    }
                })
                .show();
    }


    private void timerMobile(final TextView timer, final TextView txtResend) {
        timer.setVisibility(View.VISIBLE);
        txtResend.setVisibility(View.GONE);
        // countDownTimer = new  CountDownTimer(119000,1000){
        countDownTimer_mobile = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtResend.setVisibility(View.VISIBLE);
                txtResend.setTextColor(ContextCompat.getColor(OtpLoginVerfication.this, R.color.gray400));
                txtResend.setClickable(false);
                timer_mobile = true;
                int totatlTime = (int) (millisUntilFinished / 1000);
                if (totatlTime > 59) {
                    int seconds = totatlTime - 60;
                    if (seconds < 10) {
                        timer.setText("Resend OTP in 01:0" + String.valueOf(seconds));
                    } else {
                        timer.setText("Resend OTP in " + String.valueOf(seconds));
                    }
                } else {
                    if (totatlTime < 10) {
                        timer.setText("Resend OTP in 00:0" + String.valueOf(totatlTime));
                    } else {
                        timer.setText("Resend OTP in 00:" + String.valueOf(totatlTime));
                    }
                }
            }

            @Override
            public void onFinish() {
                timer_mobile = false;
                timer.setVisibility(View.INVISIBLE);
                txtResend.setVisibility(View.VISIBLE);
                txtResend.setClickable(true);
                txtResend.setTextColor(ContextCompat.getColor(OtpLoginVerfication.this, R.color.ventura_color));

            }
        };
        countDownTimer_mobile.start();
    }

    private void timerEmail(final TextView timer, final TextView txtResend) {
        timer.setVisibility(View.VISIBLE);
        txtResend.setVisibility(View.GONE);
        // countDownTimer = new  CountDownTimer(119000,1000){
        countDownTimer_email = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtResend.setVisibility(View.VISIBLE);
                txtResend.setTextColor(ContextCompat.getColor(OtpLoginVerfication.this, R.color.gray400));
                txtResend.setClickable(false);
                timer_email = true;
                int totatlTime = (int) (millisUntilFinished / 1000);
                if (totatlTime > 59) {
                    int seconds = totatlTime - 60;
                    if (seconds < 10) {
                        timer.setText("Resend OTP in 01:0" + String.valueOf(seconds));
                    } else {
                        timer.setText("Resend OTP in " + String.valueOf(seconds));
                    }
                } else {
                    if (totatlTime < 10) {
                        timer.setText("Resend OTP in 00:0" + String.valueOf(totatlTime));
                    } else {
                        timer.setText("Resend OTP in 00:" + String.valueOf(totatlTime));
                    }
                }
            }

            @Override
            public void onFinish() {
                timer_email = false;
//                isOTPGenerated = false;
                timer.setVisibility(View.INVISIBLE);
                txtResend.setVisibility(View.VISIBLE);
                txtResend.setClickable(true);
                txtResend.setTextColor(ContextCompat.getColor(OtpLoginVerfication.this, R.color.ventura_color));

            }
        };
        countDownTimer_email.start();
    }


    public void MobileVerify(String mobileOTP_str) {

        strmobileOTP = mobileOTP_str;

        try {
            timerMobile(txttimer_mobile, resendotptxt_mobile);
        } catch (Exception e) {
            SnackBar.SnackBar(coordinatorLayout_mobile, e.getMessage());
        }

        pinview_mobile.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                String pin = pinview.getValue();
                if (Integer.parseInt(pin) > 3) {
//                    progressbar_resend.setVisibility(View.VISIBLE);
                    if (passwordattemptscount_mobile > 3) {
                        passwordattemptscount_mobile = 0;

                        pinview.clearValue();
                        int left = 3 - passwordattemptscount_mobile;
                        if (left == 0) {
                            closeapp("you have entered multiple times wrong password, please try after sometime.");
                        } else
                            SnackBar.SnackBar(coordinatorLayout_mobile, Methods.attemps(left));

                    } else {
                        if (strmobileOTP.equalsIgnoreCase(pin)) {
                            TastyToast.makeText(getApplicationContext(), "Otp verified", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                            MobileOtpDone = true;

                            coordinatorLayout_email.setVisibility(View.VISIBLE);
                            pinview_email.startAnimation(animation);
                            countDownTimer_mobile.onFinish();
                            countDownTimer_mobile.cancel();
                            pinview_mobile.setVisibility(View.GONE);
                            resendotptxt_mobile.setVisibility(View.GONE);
                            txtmobileOTPverified.setVisibility(View.VISIBLE);
                            txtmobileOTPverified.startAnimation(animation);

                            EmailVerify(emailOTP);


                        } else {
                            pinview.clearValue();
                            passwordattemptscount_mobile++;
                            int left = 3 - passwordattemptscount_mobile;
                            if (left == 0) {

                                closeapp("you have entered multiple times wrong password, please try after sometime.");
                            } else
                                SnackBar.SnackBar(coordinatorLayout_mobile, Methods.attemps(left));
                        }
                        Methods.hideKeyboard(OtpLoginVerfication.this, pinview_mobile);
                    }
                }
            }
        });


    }

    public void EmailVerify(String emailOTP_str) {

        stremailOTP = emailOTP_str;

        try {
            timerEmail(txttimer_email, resendotptxt_email);
        } catch (Exception e) {
            SnackBar.SnackBar(coordinatorLayout_email, e.getMessage());
        }

        pinview_email.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                String pin = pinview.getValue();
                if (Integer.parseInt(pin) > 3) {
//                    progressbar_resend.setVisibility(View.VISIBLE);
                    if (passwordattemptscount_email > 3) {
                        passwordattemptscount_email = 0;

                        pinview.clearValue();
                        int left = 3 - passwordattemptscount_email;
                        if (left == 0) {
                            closeapp("you have entered multiple times wrong password, please try after sometime.");
                        } else
                            SnackBar.SnackBar(coordinatorLayout_email, Methods.attemps(left));

                    } else {
                        if (stremailOTP.equalsIgnoreCase(pin)) {
                            TastyToast.makeText(getApplicationContext(), "Otp verified", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                            EmailOtpDone = true;

                            countDownTimer_email.onFinish();
                            countDownTimer_email.cancel();
                            pinview_email.setVisibility(View.GONE);
                            resendotptxt_email.setVisibility(View.GONE);
                            txtemailOTPverified.setVisibility(View.VISIBLE);

                        } else {
                            pinview.clearValue();
                            passwordattemptscount_email++;
                            int left = 3 - passwordattemptscount_email;
                            if (left == 0) {

                                closeapp("you have entered multiple times wrong password, please try after sometime.");

                            } else
                                SnackBar.SnackBar(coordinatorLayout_email, Methods.attemps(left));
                        }
                        Methods.hideKeyboard(OtpLoginVerfication.this, pinview_email);


                    }
                }
            }
        });


    }


    void proceed() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("logged", "logged");
        editor.commit();

        Intent intent = new Intent(OtpLoginVerfication.this, Dashboard.class);
        startActivity(intent);
    }

    @Override
    public void connected() {

    }

    @Override
    public void serverNotAvailable() {

        AlertDialogClass.PopupWindowDismiss();
        ProgressDlgConnectSocket(OtpLoginVerfication.this, connectionProcess, "Server Not Available");
    }

    @Override
    public void IntenrnetNotAvailable() {

        AlertDialogClass.PopupWindowDismiss();
        Views.SweetAlert_NoDataAvailble(OtpLoginVerfication.this, "Internet Not Available");
        Log.e("IntenrnetNotAvailable: ", "internet");

    }

    @Override
    public void ConnectToserver(ConnectionProcess connectionProcess) {

        AlertDialogClass.PopupWindowDismiss();
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(OtpLoginVerfication.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(OtpLoginVerfication.this, connectionProcess, "Server Not Available");
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

        AlertDialogClass.PopupWindowDismiss();
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(OtpLoginVerfication.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(OtpLoginVerfication.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                case Const.MSGFETCHVPPDETAILSONLOGIN: {
                    String data = (String) msg.obj;
                    Log.e("fetchDetails_response: ", data);
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String name = jsonObject.getString("name");
                        String city = jsonObject.getString("city");
                        String mobile = jsonObject.getString("mobile");
                        String email = jsonObject.getString("email");
                        String vppid = jsonObject.getString("vpp_id");
                        String pan_no = jsonObject.getString("pan_no");

                        //make SharedPreferences object
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("logged", "logged");
                        editor.commit();

                        SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");

                        Logics.setVppDetails(OtpLoginVerfication.this, name, mobile, email, city, vppid, pan_no);

                        Intent intent = new Intent(OtpLoginVerfication.this, Dashboard.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("handleexception:", e.getMessage());
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }

                break;
                case Const.MSGAUTHENTICATERESENDOTP:
//                    ringProgressDialog.dismiss();
//                    ringProgressDialog.cancel();
                    AlertDialogClass.PopupWindowDismiss();

                    String data1 = (String) msg.obj;
                    Log.e("MSGAUTHENTICATE", data1);

                    try {

                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(data1);
                        int status = jsonObject.getInt("status");

                        if (status == 1) {
                            int isEmail = jsonObject.getInt("isEmail");

                            if (isEmail == 1) {
                                String emailotp = jsonObject.getString("emailotp");
                                stremailOTP = emailotp;
                                EmailVerify(stremailOTP);
                            } else {
                                mobileOTP = jsonObject.getString("mobileotp");
                                strmobileOTP = mobileOTP;
                                MobileVerify(mobileOTP);
                            }
                            //
                        } else {
                            String message = jsonObject.getString("message");
                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_SHORT, TastyToast.INFO);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        TastyToast.makeText(getApplicationContext(), e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }


//                    try {
//
//                        JSONObject jsonObject = null;
//                        jsonObject = new JSONObject(data1);
//                        int status = jsonObject.getInt("status"); //3
//                        if (status == 1) {
//                            int updateContact = jsonObject.getInt("updateContact");
//                            int updateEmail = jsonObject.getInt("updateEmail");
//
//                            if (updateContact == 1) {
//                                mobileOTP = jsonObject.getString("mobileotp");
//                                String mobile = jsonObject.getString("mobile");
//                                strmobileOTP=mobileOTP;
//
//                                MobileVerify(mobileOTP);
//
//                            } else if (updateEmail == 1) {
//                                emailOTP = jsonObject.getString("emailotp");
//                                String email = jsonObject.getString("email");
//                                stremailOTP=emailOTP;
//
//                                EmailVerify(stremailOTP);
//                            }
//
//                        } else {
//
//                            String message = jsonObject.getString("message");
//                            AlertDialogClass.ShowMsg(OtpLoginVerfication.this, message);
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        FirebaseCrashlytics.getInstance().recordException(e);
//                    }

                    break;
            }
        }
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(OtpLoginVerfication.this),
                                    "0",
                                    Methods.getVersionInfo(OtpLoginVerfication.this),
                                    Methods.getlogsDateTime(), "LoginScreen",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    OtpLoginVerfication.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!OtpLoginVerfication.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                //Toast.makeText(LoginScreen.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            if (connectionProcess == null) {
                Log.e("DlgConnectSocket11111_null", "called");

            } else {
                new ConnectTOServer(OtpLoginVerfication.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                connectionProcess.ConnectToserver(connectionProcess);
            }
            Log.e("DlgConnectSocket11111", "called");

        }

        Log.e("DlgConnectSocketMaxTry", String.valueOf(MaxTry));

    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void sendDataM(int resendOtpCount, int Otp) {
        try {
            AlertDialogClass.PopupWindowShow(OtpLoginVerfication.this, mainlayout);
            String mobileNo = SharedPref.getPreferences1(OtpLoginVerfication.this,"mobileNo");
            String emailId = SharedPref.getPreferences1(OtpLoginVerfication.this,"emailId");

            JSONObject json = new JSONObject();
            /*json.put("email", "0");
            json.put("mobile", SharedPref.getPreferences1(OtpLoginVerfication.this,"mobileNo"));
            json.put("isemail", "0");
            json.put("isMobile", "1");*/

            json.put("email", 0); //email id empty
            json.put("mobile", mobileNo);  //mobile no passing here
            json.put("isemail", 0);
            json.put("isMobile", 1); //calling mobile flag 1 otp
            json.put("whatsappflag", "yes"); // whatsapp flag inserting after email otp gt done.
            json.put("resendOtpCount", resendOtpCount); // whatsapp flag inserting after email otp gt done.
            json.put("Otp", Otp); // whatsapp flag inserting after email otp gt done.

            data = json.toString().getBytes();
            new SendTOServer(this, this, Const.MSGAUTHENTICATERESENDOTP, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(OtpLoginVerfication.this, e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void sendDataE(int resendOtpCount, int Otp) {
        try {
            AlertDialogClass.PopupWindowShow(OtpLoginVerfication.this, mainlayout);
            String mobileNo = SharedPref.getPreferences1(OtpLoginVerfication.this,"mobileNo");
            String emailId = SharedPref.getPreferences1(OtpLoginVerfication.this,"emailId");
            String mobile = Logics.getContact(OtpLoginVerfication.this);
            JSONObject json = new JSONObject();
           /* json.put("email", "1");
            json.put("mobile", SharedPref.getPreferences1(OtpLoginVerfication.this,"emailId"));
            json.put("isemail", "1");
            json.put("isMobile", "0");*/

            json.put("email", emailId); //email id passing
            json.put("mobile", 0);  //mobile no empty here .
            json.put("isemail", 1);// calling email flag 1 for otp individual ..
            json.put("isMobile", 0);
            json.put("resendOtpCount", resendOtpCount);
            json.put("Otp", Otp);

            Log.e("sendDataE:", json.toString());

            data = json.toString().getBytes();
            new SendTOServer(this, this, Const.MSGAUTHENTICATERESENDOTP, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(OtpLoginVerfication.this, e.getMessage());
        }
    }


}