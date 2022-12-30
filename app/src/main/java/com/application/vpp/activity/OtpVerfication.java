package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.application.vpp.Views.Views;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;


public class OtpVerfication extends AppCompatActivity implements RequestSent, View.OnClickListener, ConnectionProcess {
    EditText edt_mob_otp, edt_email_otp;
    FancyButton btn1, btn2, btn_proceed;
    //boolean  = false;
    String strMobOtp, mobileOtp, mobileNum, emailOtp, strEmailOtp, existingContact, existingEmail, newEmail, email;
    public int verifyMobile = 0, verifyEmail = 1, updateContact = 0, updateEmail = 0;
    public int isRegistered = 0, isSignup = 0;
    ScrollView mainlayout;
    TextView mob, emailid, txt_resend_mob, txt_email_resend, textView1, textView3, textViewChangeMobile, textViewChangeEmail;
    LinearLayout linearemail;
    public static Handler otpVerfhandler;
//    ProgressDialog ringProgressDialog;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    boolean checkmobile = false;
    boolean checkemail = false;
    boolean checkresend = false;
    public static final String PREFS_NAME = "LoginPrefs";
    int e = 0;
    int m = 0;

    int MaxTry=0;

    ArrayList<InserSockettLogs> inserSockettLogsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        Profile.handlerProfile = null;
        OtpVerfication.otpVerfhandler = null;
        otpVerfhandler = new ViewHandler();
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        mainlayout = (ScrollView) findViewById(R.id.mainlayout);
        mob = findViewById(R.id.textView2);
        emailid = findViewById(R.id.textView4);
        linearemail = findViewById(R.id.linear2);
        btn_proceed = (FancyButton) findViewById(R.id.btn_proceed);
        textViewChangeMobile = findViewById(R.id.textViewChangeMobile);
        textViewChangeEmail = findViewById(R.id.textViewChangeEmail);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView3 = (TextView) findViewById(R.id.textView3);
        txt_resend_mob = (TextView) findViewById(R.id.txt_resend_mob);
        txt_email_resend = (TextView) findViewById(R.id.txt_email_resend);
        edt_mob_otp = (EditText) findViewById(R.id.edt_mob_otp);
        edt_email_otp = (EditText) findViewById(R.id.edt_email_otp);
        btn1 = (FancyButton) findViewById(R.id.btn_mob_submit);
        btn2 = (FancyButton) findViewById(R.id.btn_email_submit);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn_proceed.setOnClickListener(this);
        txt_resend_mob.setOnClickListener(this);
        txt_email_resend.setOnClickListener(this);

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList,"SocketLogs", OtpVerfication.this);

//        if (SharedPref.getPreferences(getApplicationContext(),))

        Log.e("ssssss", SharedPref.getPreferences(getApplicationContext(), Const.FromUpdate));


        if (SharedPref.getPreferences(getApplicationContext(), Const.FromUpdate).equalsIgnoreCase("FromPanLoginMob")) {
            isRegistered = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "isRegistered"));
            mobileNum = Logics.getContact(getApplicationContext());//UPDATED Contact HERE
            email = Logics.getEmail(getApplicationContext());

            verifyMobile = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "verifyMobile"));

            mobileOtp = SharedPref.getPreferences(getApplicationContext(), "mobileOtp");
            emailOtp = SharedPref.getPreferences(getApplicationContext(), "emailOtp");
            updateContact = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "updateContact"));
            existingContact = SharedPref.getPreferences(getApplicationContext(), "existingContact");
            updateEmail = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "updateEmail"));

            mob.setText(mobileNum);
            emailid.setText(email);

            textViewChangeMobile.setVisibility(View.GONE);
            textViewChangeEmail.setVisibility(View.VISIBLE);
//            SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");
            TastyToast.makeText(getApplicationContext(), "Mobile Otp Verified", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            txt_resend_mob.setText("Your mobile number is verified");
            txt_resend_mob.setTextColor(getResources().getColor(R.color.btn_active));
            txt_resend_mob.setTextSize(16);

            btn1.setVisibility(View.GONE);
            edt_mob_otp.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            linearemail.setVisibility(View.VISIBLE);
            verifyMobile = 1;
            edt_email_otp.setFocusable(true);
            SharedPref.savePreferences(getApplicationContext(), Const.MobileVerified, "Done");
        } else if (SharedPref.getPreferences(getApplicationContext(), Const.FromUpdate).equalsIgnoreCase("FromPanLoginEMail")) {

            if (SharedPref.getPreferences(getApplicationContext(), Const.MobileVerified).equalsIgnoreCase("Done")) {

                isRegistered = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "isRegistered"));
                mobileNum = Logics.getContact(getApplicationContext());
                email = Logics.getEmail(getApplicationContext());  //UPDATED EMAIL HERE
                verifyMobile = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "verifyMobile"));

                mobileOtp = SharedPref.getPreferences(getApplicationContext(), "mobileOtp");
                emailOtp = SharedPref.getPreferences(getApplicationContext(), "emailOtp");
                updateContact = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "updateContact"));
                existingContact = SharedPref.getPreferences(getApplicationContext(), "existingContact");
                updateEmail = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "updateEmail"));

                mob.setText(mobileNum);
                emailid.setText(email);

                textViewChangeMobile.setVisibility(View.GONE);
                textViewChangeEmail.setVisibility(View.VISIBLE);
//                SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");
                TastyToast.makeText(getApplicationContext(), "Mobile Otp Verified", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                txt_resend_mob.setText("Your mobile number is verified");
                txt_resend_mob.setTextColor(getResources().getColor(R.color.btn_active));
                txt_resend_mob.setTextSize(16);

                btn1.setVisibility(View.GONE);
                edt_mob_otp.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                linearemail.setVisibility(View.VISIBLE);
                verifyMobile = 1;
                edt_email_otp.setFocusable(true);

            }

            isRegistered = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "isRegistered"));
            mobileNum = SharedPref.getPreferences(getApplicationContext(), "mobileNum");
            email = SharedPref.getPreferences(getApplicationContext(), "email");
            verifyMobile = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "verifyMobile"));

            mobileOtp = SharedPref.getPreferences(getApplicationContext(), "mobileOtp");
            emailOtp = SharedPref.getPreferences(getApplicationContext(), "emailOtp");
            updateContact = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "updateContact"));
            existingContact = SharedPref.getPreferences(getApplicationContext(), "existingContact");
            updateEmail = Integer.parseInt(SharedPref.getPreferences(getApplicationContext(), "updateEmail"));

            mob.setText(mobileNum);
            emailid.setText(email);

            SharedPref.savePreferences(getApplicationContext(), Const.EmailVerified, "Done");

            textViewChangeEmail.setVisibility(View.GONE);
            textViewChangeMobile.setVisibility(View.GONE);
//            SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");
            TastyToast.makeText(getApplicationContext(), "Email Otp Verified", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            txt_email_resend.setText("Your email id is verified");
            txt_email_resend.setTextSize(16);

            txt_email_resend.setTextColor(getResources().getColor(R.color.btn_active));
            edt_email_otp.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            verifyEmail = 1;
            Logics.setOtpVerificationDetails(OtpVerfication.this, mobileNum, email, verifyMobile, verifyEmail, isRegistered);
            btn_proceed.setVisibility(View.VISIBLE);
//            SharedPref.savePreferences(getApplicationContext(),Const.EmailVerified,"Done");


        } else {

            if (getIntent().getExtras() != null) {

                // here we come from login as well as singup screen., we go from same screen and change number or email .. den come here ,,

                isRegistered = getIntent().getExtras().getInt("isRegistered", 0);
                mobileNum = getIntent().getExtras().getString("mobileNum", null);
                email = getIntent().getExtras().getString("emailId", null);
                verifyMobile = getIntent().getExtras().getInt("verifyMobile", 0); //3
//            isSignup = getIntent().getExtras().getInt("isSignup", 0);
                //additional para for update profile contact
                mobileOtp = getIntent().getExtras().getString("strOtp", null);
                emailOtp = getIntent().getExtras().getString("emailOTP", null);
                updateContact = getIntent().getExtras().getInt("updateContact", 0);
                existingContact = getIntent().getExtras().getString("existingContact", null);
                updateEmail = getIntent().getExtras().getInt("updateEmail", 0);
//            existingEmail = getIntent().getExtras().getString("existingEmail", null);
//            newEmail = getIntent().getExtras().getString("newEmail", null);


                SharedPref.savePreferences(getApplicationContext(), "isRegistered", String.valueOf(isRegistered));
                SharedPref.savePreferences(getApplicationContext(), "mobileNum", String.valueOf(mobileNum));
                SharedPref.savePreferences(getApplicationContext(), "email", String.valueOf(email));
                SharedPref.savePreferences(getApplicationContext(), "verifyMobile", String.valueOf(verifyMobile));

                SharedPref.savePreferences(getApplicationContext(), "mobileOtp", String.valueOf(mobileOtp));
                SharedPref.savePreferences(getApplicationContext(), "emailOtp", String.valueOf(emailOtp));
                SharedPref.savePreferences(getApplicationContext(), "updateContact", String.valueOf(updateContact));
                SharedPref.savePreferences(getApplicationContext(), "existingContact", String.valueOf(existingContact));
                SharedPref.savePreferences(getApplicationContext(), "updateEmail", String.valueOf(updateEmail));

                mob.setText(mobileNum);
                emailid.setText(email);
            }

        }

//        if (SharedPref.getPreferences(getApplicationContext(), Const.FromUpdate).equalsIgnoreCase("FromPanLoginEMail")) {
//            VerifyEmai/**/lOtp();
//            textViewChangeEmail.setVisibility(View.GONE);
//            textViewChangeMobile.setVisibility(View.GONE);
//            SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");
//            TastyToast.makeText(getApplicationContext(), "Email Otp Verified", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
//            txt_email_resend.setText("Your email id is verified");
//            txt_email_resend.setTextSize(16);
//
//            txt_email_resend.setTextColor(getResources().getColor(R.color.btn_active));
//            edt_email_otp.setVisibility(View.GONE);
//            textView3.setVisibility(View.GONE);
//            btn2.setVisibility(View.GONE);
//            verifyEmail = 1;
//            Logics.setOtpVerificationDetails(OtpVerfication.this, mobileNum, email, verifyMobile, verifyEmail, isRegistered);
//            btn_proceed.setVisibility(View.VISIBLE);
//        }

        textViewChangeMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m = 1;
                SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, Const.FromPanLoginMob);

                open("0");
            }
        });

        textViewChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e = 1;
                SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, Const.FromPanLoginEMail);

                open("1");
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void sendDataChange(int Email, int Mobile, String mobileNumstr, String emailstr) {
        try {
//            ringProgressDialog = ProgressDialog.show(OtpVerfication.this, "Please wait ...", "Loading Your Data ...", true);
//            ringProgressDialog.setCancelable(true);
//            ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

            AlertDialogClass.PopupWindowShow(OtpVerfication.this, mainlayout);


            String EmailId = Logics.getEmail(OtpVerfication.this);
            String mobile = Logics.getContact(OtpVerfication.this);
            JSONObject json = new JSONObject();
            json.put("email", emailstr);
            json.put("mobile", mobileNumstr);
            json.put("isemail", Email);
            json.put("isMobile", Mobile);
            byte[] data = json.toString().getBytes();
            new SendTOServer(this, this, Const.MSGAUTHENTICATE, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public void VerifyMoblieOtp() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        strMobOtp = edt_mob_otp.getText().toString().toUpperCase();
        if (strMobOtp.matches(mobileOtp)) {
            //   Toast.makeText(OtpVerfication.this, " Mobile Otp Verified", Toast.LENGTH_LONG).show();
            TastyToast.makeText(getApplicationContext(), "Mobile Otp Verified", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            txt_resend_mob.setText("Your mobile number is verified");
            txt_resend_mob.setTextColor(getResources().getColor(R.color.btn_active));
            txt_resend_mob.setTextSize(16);

            btn1.setVisibility(View.GONE);
            edt_mob_otp.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            linearemail.setVisibility(View.VISIBLE);
            verifyMobile = 1;
            edt_email_otp.setFocusable(true);
        } else {


            //dummy added for playstore
            if (SharedPref.getPreferences(OtpVerfication.this,"DPAN").equalsIgnoreCase("AZTPT4416B")){
                TastyToast.makeText(getApplicationContext(), "Mobile Otp Verified", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                txt_resend_mob.setText("Your mobile number is verified");
                txt_resend_mob.setTextColor(getResources().getColor(R.color.btn_active));
                txt_resend_mob.setTextSize(16);

                btn1.setVisibility(View.GONE);
                edt_mob_otp.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                linearemail.setVisibility(View.VISIBLE);
                verifyMobile = 1;
                edt_email_otp.setFocusable(true);

            }else {
                TastyToast.makeText(OtpVerfication.this, "Enter Valid Mobile Otp", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }

            // Toast.makeText(OtpVerfication.this, "Enter Valid Mobile Otp", Toast.LENGTH_SHORT).show();
        }

    }

    public void VerifyEmailOtp() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        strEmailOtp = edt_email_otp.getText().toString().toUpperCase();
        if (strEmailOtp.matches(emailOtp)) {
            // Toast.makeText(OtpVerfication.this, " Email Otp Verified", Toast.LENGTH_LONG).show();
            TastyToast.makeText(getApplicationContext(), "Email Otp Verified", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            txt_email_resend.setText("Your email id is verified");
            txt_email_resend.setTextSize(16);

            txt_email_resend.setTextColor(getResources().getColor(R.color.btn_active));
            edt_email_otp.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            verifyEmail = 1;
            Logics.setOtpVerificationDetails(OtpVerfication.this, mobileNum, email, verifyMobile, verifyEmail, isRegistered);
            btn_proceed.setVisibility(View.VISIBLE);
//            if (verifyMobile == 1 && verifyEmail == 1) {
//                if (isRegistered == 1) {
//                    Intent intent = new Intent(OtpVerfication.this, PanValidation.class);
//                    startActivity(intent);
//                } else if (isRegistered == 2) {
//                    sendData();
//
////                    Intent intent = new Intent(OtpVerfication.this, Welcome.class);
////                    startActivity(intent);
//                }
//
//            }
//        } else {
//            Toast.makeText(OtpVerfication.this, "Enter Valid Email Otp", Toast.LENGTH_SHORT).show();
//        }

        } else {


            // for play store dummy..

            if (SharedPref.getPreferences(OtpVerfication.this,"DPAN").equalsIgnoreCase("AZTPT4416B")){

                TastyToast.makeText(getApplicationContext(), "Email Otp Verified", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                txt_email_resend.setText("Your email id is verified");
                txt_email_resend.setTextSize(16);

                txt_email_resend.setTextColor(getResources().getColor(R.color.btn_active));
                edt_email_otp.setVisibility(View.GONE);
                textView3.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                verifyEmail = 1;
                Logics.setOtpVerificationDetails(OtpVerfication.this, mobileNum, email, verifyMobile, verifyEmail, isRegistered);
                btn_proceed.setVisibility(View.VISIBLE);

            }else {
                TastyToast.makeText(OtpVerfication.this, "Enter Valid Email Otp", TastyToast.LENGTH_LONG, TastyToast.ERROR);

            }


            // Toast.makeText(OtpVerfication.this, "Enter Valid Email Otp", Toast.LENGTH_LONG).show();
        }
    }

    private void proceed() {
        checkresend = false;
        String mbleVrfd = SharedPref.getPreferences(getApplicationContext(), Const.MobileVerified);
        String EmailVrfd = SharedPref.getPreferences(getApplicationContext(), Const.EmailVerified);
        String EMAILMOBILE = SharedPref.getPreferences(getApplicationContext(), Const.FromUpdate);

        Log.e("mbleVrfd ", mbleVrfd);
        Log.e("EmailVrfd", EmailVrfd);
        Log.e("EMAILMOBILE", EMAILMOBILE);

        if (EMAILMOBILE.equalsIgnoreCase(Const.FromPanLoginEMail)) {
            if (mbleVrfd.equalsIgnoreCase("Done") && EmailVrfd.equalsIgnoreCase("Done")) {
                if (isRegistered == 1) {
                    Intent intent = new Intent(OtpVerfication.this, PanValidation.class);
                    startActivity(intent);
                } else if (isRegistered == 2) {
                    sendData();
//                    Intent intent = new Intent(OtpVerfication.this, Welcome.class);
//                    startActivity(intent);
                } else if (isRegistered == 3) {
                    sendData();
//                 Intent intent = new Intent(OtpVerfication.this, Welcome.class);
//                intent.putExtra("issignup",0);
//                startActivity(intent);
                }

            } else {
                TastyToast.makeText(getApplicationContext(), "Enter Valid Email Otp", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                //Toast.makeText(OtpVerfication.this, "Enter Valid Email Otp", Toast.LENGTH_LONG).show();
            }
        } else if (verifyMobile == 1 && verifyEmail == 1) {
            if (isRegistered == 1) {
                Intent intent = new Intent(OtpVerfication.this, PanValidation.class);
                startActivity(intent);
            } else if (isRegistered == 2) {
                sendData();
//                    Intent intent = new Intent(OtpVerfication.this, Welcome.class);
//                    startActivity(intent);
            } else if (isRegistered == 3) {
                sendData();
//                 Intent intent = new Intent(OtpVerfication.this, Welcome.class);
//                intent.putExtra("issignup",0);
//                startActivity(intent);
            }

        } else {
            TastyToast.makeText(getApplicationContext(), "Enter Valid Email Otp", TastyToast.LENGTH_LONG, TastyToast.ERROR);

            //Toast.makeText(OtpVerfication.this, "Enter Valid Email Otp", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void requestSent(int value) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_mob_submit: {
                VerifyMoblieOtp();
            }
            break;
            case R.id.btn_email_submit: {
                VerifyEmailOtp();
            }
            break;
            case R.id.txt_resend_mob: {
                checkresend = true;
                if (isRegistered == 1) {
                    updateContact = 1;
                    int isSignup = 1;
                    resendOtp(updateContact, updateEmail, isSignup);
                } else if (isRegistered == 2 || isRegistered == 3) {
                    updateContact = 1;
                    isRegistered = 2;
                    int isSignup = 2;
                    resendOtp(updateContact, updateEmail, isSignup);
                }


                // VerifyEmailOtp();

            }
            break;
            case R.id.txt_email_resend: {
                if (isRegistered == 1) {
                    updateEmail = 1;
                    int isSignup = 1;
                    resendOtp(0, updateEmail, isSignup);
                } else if (isRegistered == 2 || isRegistered == 3) {
                    updateEmail = 1;
                    int isSignup = 2;
                    resendOtp(0, updateEmail, isSignup);
                }
            }

            break;
            case R.id.btn_proceed: {
                proceed();
            }

            break;
        }
    }

    private void resendOtp(int updateContact, int updateEmail, int isSignup) {
//        ringProgressDialog = ProgressDialog.show(OtpVerfication.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));


        AlertDialogClass.PopupWindowShow(OtpVerfication.this, mainlayout);

        try {

            String imei = Logics.getDeviceID(this);
            String simID = Logics.getSimId(this);
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                imei = Logics.getTokenID(OtpVerfication.this);

                simID = "12345";
            }
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("mobile", mobileNum);
            jsonObject.put("email", email);
            jsonObject.put("isMobile", updateContact);
            jsonObject.put("isemail", updateEmail);

            byte data[] = jsonObject.toString().getBytes();
//   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SendTOServer(OtpVerfication.this, OtpVerfication.this, Const.MSGRESENDOTP, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);

        }
    }
//    private void resendOtp(int updateContact,int updateEmail,int isSignup) {
//        ringProgressDialog = ProgressDialog.show(OtpVerfication.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//        try {
//
//            String imei = Logics.getDeviceID(this);
//            String simID = Logics.getSimId(this);
//            if (android.os.Build.VERSION.SDK_INT>=29){
//                imei=Logics.getTokenID(OtpVerfication.this);
//
//                simID="12345";
//            }
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("imei",imei);
//            jsonObject.put("simNo",simID);
//            jsonObject.put("isRegisted",isRegistered);
//            jsonObject.put("mobile",mobileNum);
//            jsonObject.put("email",email);
//            jsonObject.put("otp",mobileOtp);
//            jsonObject.put("isSignup",isSignup);
//            jsonObject.put("updateContact",updateContact);
//            jsonObject.put("resendOtp",1);
//            jsonObject.put("updateEmail",updateEmail);
//
//            byte data[] = jsonObject.toString().getBytes();
////   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            new SendTOServer(OtpVerfication.this, OtpVerfication.this, Const.MSGRESENDOTP,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        }

    //
//
//        catch (JSONException e) {
//            e.printStackTrace();
//
//        }
//    }
    private void sendData() {

//        ringProgressDialog = ProgressDialog.show(OtpVerfication.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        AlertDialogClass.PopupWindowShow(OtpVerfication.this, mainlayout);

        try {

//            String imei = Logics.getDeviceID(this);
//            String simID = Logics.getSimId(this);
//            if (android.os.Build.VERSION.SDK_INT>=29){
//                imei=Logics.getTokenID(OtpVerfication.this);
//
//                simID="12345";
//            }
            String pan = Logics.getLoginPan(this);
            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("imei",imei);
//            jsonObject.put("page",1);
//            jsonObject.put("size",10);

//            jsonObject.put("imei",imei);
//            jsonObject.put("simNo",simID);
            jsonObject.put("pan", pan);


            byte data[] = jsonObject.toString().getBytes();

            //   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SendTOServer(OtpVerfication.this, requestSent, Const.MSGVERIFYOTPPAN, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }


    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

//            if (ringProgressDialog != null) {
//                ringProgressDialog.dismiss();
//            }

            AlertDialogClass.PopupWindowDismiss();

            Log.e("OTPVERYFY", msg.toString());

            switch (msg.arg1) {


                case Const.MSGVERIFYOTPPAN: {


                    try {
                       // ringProgressDialog.dismiss();

                        AlertDialogClass.PopupWindowDismiss();
                        String data = (String) msg.obj;
                        Log.d("Message", "handleMessagePan: " + data);
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(data);

                        int status = jsonObject.getInt("status");
                        if (status != 0) {
                            fetchDetails();
//                            if (isRegistered == 3) {
//                                Intent intent = new Intent(OtpVerfication.this, Welcome.class);
//                                intent.putExtra("issignup", 0);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(OtpVerfication.this, Welcome.class);
//                                intent.putExtra("issignup", 0);
//                                startActivity(intent);
//                            }
                        }else {
                            String message = jsonObject.getString("message");
                            AlertDialogClass.ShowMsg(OtpVerfication.this,message);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialogClass.ShowMsg(OtpVerfication.this,e.getMessage());

                    }


                }
                break;

                case Const.MSGRESENDOTP: {

                   // ringProgressDialog.dismiss();


                    try {
                     //   ringProgressDialog.dismiss();

                       AlertDialogClass.PopupWindowDismiss();

                        String data = (String) msg.obj;
                        Log.d("Message", "handleMessagePan: " + data);
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(data);
                        //String message=jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            int updateContact = jsonObject.getInt("updateContact");
                            int updateEmail = jsonObject.getInt("updateEmail");
                            if (updateContact == 1) {
                                mobileOtp = jsonObject.getString("mobileotp");
                            } else if (updateEmail == 1) {
                                emailOtp = jsonObject.getString("emailotp");
                            }


                        }else {
                            String message = jsonObject.getString("message");
                            AlertDialogClass.ShowMsg(OtpVerfication.this,message);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialogClass.ShowMsg(OtpVerfication.this,e.getMessage());

                    }


                }
                break;
                case Const.MSGFETCHVPPDETAILSONLOGIN: {

                    String data = (String) msg.obj;

                    Log.e( "fetchDetailsRESP: ", data);

                    Log.d("vppDetails", "handleMessage: " + data);
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

                        Logics.setVppDetails(OtpVerfication.this, name, mobile, email, city, vppid, pan_no);
//                        Intent intent = new Intent(OtpVerfication.this, DashoboardDesign.class);
                        Intent intent = new Intent(OtpVerfication.this, Dashboard.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.e("errorVPPdetails", e.getMessage());
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }


                }
                break;

                case Const.MSGAUTHENTICATE:
//                    ringProgressDialog.dismiss();
//                    ringProgressDialog.cancel();

                    AlertDialogClass.PopupWindowDismiss();
                    JSONObject jsonObject = null;
                    String data1 = (String) msg.obj;
                    try {
                        jsonObject = new JSONObject(data1);
                        int status = jsonObject.getInt("status"); //3
                        if (status == 1) {
                            int updateContact = jsonObject.getInt("updateContact");
                            int updateEmail = jsonObject.getInt("updateEmail");
                            if (updateContact == 1) {
                                String mobileOTP = jsonObject.getString("mobileotp");
                                String mobile = jsonObject.getString("mobile");
                                Intent intent = new Intent(OtpVerfication.this, AuthenticateUpdateProfile.class);
                                // intent.putExtra("isSignup",1);
                                intent.putExtra("strOtp", mobileOTP);
                                intent.putExtra("mobileNum", mobile);
                                intent.putExtra("updateEmail", 0);
                                intent.putExtra("updateContact", 1);
                                intent.putExtra("edtEmail", e);
                                intent.putExtra("edtMobile", m);

                                startActivity(intent);
                            } else if (updateEmail == 1) {
                                String emailOtp = jsonObject.getString("emailotp");
                                String email = jsonObject.getString("email");
                                Intent intent = new Intent(OtpVerfication.this, AuthenticateUpdateProfile.class);
                                // intent.putExtra("isSignup",1);
                                intent.putExtra("emailOTP", emailOtp);
                                intent.putExtra("email", email);
                                intent.putExtra("updateEmail", 1);
                                intent.putExtra("updateContact", 0);
                                intent.putExtra("edtEmail", e);
                                intent.putExtra("edtMobile", m);
                                startActivity(intent);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }

                    break;

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        Intent intent =new Intent(OtpVerfication.this,SignupScreen.class);
//        startActivity(intent);
    }

    @Override
    public void connected() {

//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();

        Log.e("connected11", "connected11");
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (checkresend == true) {
                    if (isRegistered == 1) {
                        updateContact = 1;
                        int isSignup = 1;
                        resendOtp(updateContact, updateEmail, isSignup);
                    } else if (isRegistered == 2 || isRegistered == 3) {
                        updateContact = 1;
                        isRegistered = 2;
                        int isSignup = 2;
                        resendOtp(updateContact, updateEmail, isSignup);
                    }
                } else {
                    proceed();
                }
               // TastyToast.makeText(OtpVerfication.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    private void fetchDetails() {

//        ringProgressDialog = ProgressDialog.show(OtpVerfication.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        AlertDialogClass.PopupWindowShow(OtpVerfication.this, mainlayout);



        try {


            String pan = Logics.getLoginPan(OtpVerfication.this);
            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("imei",imei);
//            jsonObject.put("page",1);
//            jsonObject.put("size",10);

//            jsonObject.put("imei",imei);//change here pan no in req
            jsonObject.put("pannum", pan);
            //  jsonObject.put("simNo","1111111111111111111111");

            Log.e( "fetchDetailsREQ: ", jsonObject.toString());

            byte data[] = jsonObject.toString().getBytes();

            //   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SendTOServer(OtpVerfication.this, OtpVerfication.this, Const.MSGFETCHVPPDETAILSONLOGIN, data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }

    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();
        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(OtpVerfication.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(OtpVerfication.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();
        Views.SweetAlert_NoDataAvailble(OtpVerfication.this, "Internet Not Available");
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
                    new ConnectTOServer(OtpVerfication.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(OtpVerfication.this, connectionProcess, "Server Not Available");
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

        AlertDialogClass.PopupWindowDismiss();
        Log.e("SocketDisconnected_x: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(OtpVerfication.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(OtpVerfication.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //    s=0 for mobile s=1 fro email
    public void open(String s) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure ? , Do you want to change");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (s.equalsIgnoreCase("0")) {
                            sendDataChange(0, 1, mobileNum, email);
                        } else {
                            sendDataChange(1, 0, mobileNum, email);
                        }
//                                Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("LongLogTag")

    public void ProgressDlgConnectSocket(Context context, final ConnectionProcess connectionProcess, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(OtpVerfication.this),
                                    "0",
                                    Methods.getVersionInfo(OtpVerfication.this),
                                    Methods.getlogsDateTime(), "OtpVerfication",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    OtpVerfication.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!OtpVerfication.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
              //  Toast.makeText(OtpVerfication.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess==null){
                Log.e( "DlgConnectSocket11111_null", "called");

            }else {
                new ConnectTOServer(OtpVerfication.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

}