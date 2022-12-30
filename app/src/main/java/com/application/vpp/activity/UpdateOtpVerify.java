package com.application.vpp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class UpdateOtpVerify extends AppCompatActivity implements RequestSent, View.OnClickListener,ConnectionProcess{
    EditText edt_mob_otp, edt_email_otp;

    String strMobOtp, mobileOtp, mobileNum, emailOtp, strEmailOtp, existingContact, existingEmail, newEmail, email;
    public int verifyMobile = 0, verifyEmail = 1, updateContact = 0, updateEmail = 0;
    TextView mob, emailid, txt_resend_mob, txt_email_resend,textView1,textView3;
    LinearLayout linearemail,linear3,linearFirst;
    FancyButton btn1, btn2, btn_proceed;
    public static Handler otpVerfhandler;
//    ProgressDialog ringProgressDialog;
    ConnectionProcess connectionProcess;
    ScrollView mainlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpauth2);
        connectionProcess=(ConnectionProcess)this;
        linearemail = findViewById(R.id.linear2);
        linear3= findViewById(R.id.linear3);
        linearFirst = findViewById(R.id.linearFirst);
        mob = findViewById(R.id.textView2);
        emailid = findViewById(R.id.textView4);
        btn_proceed =findViewById(R.id.btn_proceed);
        mainlayout =findViewById(R.id.mainlayout);
        otpVerfhandler = new ViewHandler();
        if (getIntent().getExtras() != null) {
            email = getIntent().getExtras().getString("email", null); //// mistake
            mobileOtp = getIntent().getExtras().getString("strOtp", null);
            emailOtp = getIntent().getExtras().getString("emailOTP", null);
            updateContact = getIntent().getExtras().getInt("updateContact", 0);
            updateEmail = getIntent().getExtras().getInt("updateEmail", 0);
            mobileNum = getIntent().getExtras().getString("mobileNum", null);

            SharedPref.savePreferences(getApplicationContext(),"newemail",newEmail);
            SharedPref.savePreferences(getApplicationContext(),"newcontact",mobileNum);

            btn_proceed.setOnClickListener(this);
            emailid.setText(email);
            mob.setText(mobileNum);
            if(updateContact==1 && updateEmail==0){
                linearemail.setVisibility(View.GONE);
                linearFirst.setVisibility(View.VISIBLE);
            }
            if(updateContact==0 && updateEmail==1){
                linearFirst.setVisibility(View.GONE);
                linearemail.setVisibility(View.VISIBLE);
            }
        }
        textView1=(TextView) findViewById(R.id.textView1);
        textView3=(TextView) findViewById(R.id.textView3);
        txt_resend_mob = (TextView) findViewById(R.id.txt_resend_mob);
        txt_resend_mob.setVisibility(View.VISIBLE);
        txt_email_resend = (TextView) findViewById(R.id.txt_email_resend);
        edt_mob_otp = (EditText) findViewById(R.id.edt_mob_otp);
        edt_email_otp = (EditText) findViewById(R.id.edt_email_otp);
        btn1 =  findViewById(R.id.btn_mob_submit);
        btn2 =  findViewById(R.id.btn_email_submit);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        txt_email_resend.setOnClickListener(this);
        txt_resend_mob.setOnClickListener(this);
    }
    public void VerifyMoblieOtp() {
        strMobOtp = edt_mob_otp.getText().toString().toUpperCase();
        if (strMobOtp.matches(mobileOtp)) {
            Toast.makeText(UpdateOtpVerify.this, " Mobile Otp Verified", Toast.LENGTH_LONG).show();
            txt_resend_mob.setVisibility(View.VISIBLE);
            txt_resend_mob.setText("Your mobile number is verified");
            txt_resend_mob.setTextColor(getResources().getColor(R.color.btn_active));
            txt_resend_mob.setTextSize(16);

            btn1.setVisibility(View.GONE);
            edt_mob_otp.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            //   linearemail.setVisibility(View.VISIBLE);

            if(updateEmail==0){

                btn_proceed.setVisibility(View.VISIBLE);
            }
            verifyMobile = 1;
            // edt_email_otp.setFocusable(true);
        } else {
            Toast.makeText(UpdateOtpVerify.this, "Enter Valid Mobile Otp", Toast.LENGTH_SHORT).show();
        }

    }

    public void VerifyEmailOtp() {
        strEmailOtp = edt_email_otp.getText().toString().toUpperCase();
        if (strEmailOtp.matches(emailOtp)) {
            Toast.makeText(UpdateOtpVerify.this, " Email Otp Verified", Toast.LENGTH_LONG).show();
            txt_email_resend.setVisibility(View.VISIBLE);
            txt_email_resend.setText("Your Email id is verified");
            txt_email_resend.setTextColor(getResources().getColor(R.color.btn_active));
            txt_email_resend.setTextSize(16);

            btn2.setVisibility(View.GONE);
            edt_email_otp.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
            //   linearemail.setVisibility(View.VISIBLE);
            if(updateContact==0){

                btn_proceed.setVisibility(View.VISIBLE);
            }
            verifyEmail = 1;
            // edt_email_otp.setFocusable(true);
        } else {
            Toast.makeText(UpdateOtpVerify.this, "Enter Valid Email Otp", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.btn_mob_submit: {
                VerifyMoblieOtp();

            }
            break;
            case R.id.btn_email_submit: {
                VerifyEmailOtp();

            }
            break;
            case R.id.btn_proceed:{
                proceed();
            }break;
            case R.id.txt_resend_mob: {
                updateResendOtp(mobileNum,email,updateEmail,updateContact);
            }
            break;
            case R.id.txt_email_resend: {
                updateResendOtp(mobileNum,email,updateEmail,updateContact);

            }
            break;
        }
    }
    private void updateResendOtp(String mobileNum,String email,int updateEmail,int updateContact){
        try {
//            ringProgressDialog = ProgressDialog.show(UpdateOtpVerify.this, "Please wait ...", "Loading Your Data ...", true);
//            ringProgressDialog.setCancelable(true);
//            ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));


            AlertDialogClass.PopupWindowShow(UpdateOtpVerify.this, mainlayout);


            if(updateEmail==0) {
                email = Logics.getEmail(UpdateOtpVerify.this);
            }
            if(updateContact==0) {
                mobileNum = Logics.getContact(UpdateOtpVerify.this);
            }
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("mobile", mobileNum);
            json.put("isemail", updateEmail);
            json.put("isMobile", updateContact);
            byte[] data = json.toString().getBytes();
            new SendTOServer(this, this, Const.MSGUPDATERESEND, data,connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }
    private void proceed() {
        if(verifyMobile==1 &&updateContact==1 &&updateEmail==0){
            saveNewMobile();
        }

        if(verifyEmail==1 &&updateEmail==1 &&updateContact==0){
            saveNewMobile();
        }
    }
    private  void saveNewMobile(){
        String oldMobile=Logics.getContact(UpdateOtpVerify.this);
        String oldEmail = Logics.getEmail(UpdateOtpVerify.this);
//        ringProgressDialog = ProgressDialog.show(UpdateOtpVerify.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        AlertDialogClass.PopupWindowShow(UpdateOtpVerify.this, mainlayout);


        try {

            String imei = Logics.getDeviceID(this);
            String simID = Logics.getSimId(this);
            if (android.os.Build.VERSION.SDK_INT>=29){
                imei=Logics.getTokenID(UpdateOtpVerify.this);

                simID="12345";
            }
            String pan= Logics.getPanNo(this);
            String vppid=Logics.getVppId(this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("imei",imei);
            jsonObject.put("page",1);
            //jsonObject.put("size",10);

            jsonObject.put("vppid",vppid);
            jsonObject.put("pan",pan);

            if(updateContact==1) {

                jsonObject.put("newMobile", mobileNum);
                jsonObject.put("oldMobile", oldMobile);
                jsonObject.put("updateContact", 1);
                jsonObject.put("updateEmail", 0);

            }
            if(updateEmail==1){

                jsonObject.put("newEmail", email);
                jsonObject.put("oldEmail", oldEmail);
                jsonObject.put("updateEmail", 1);
                jsonObject.put("updateContact", 0);
            }



            byte data[] = jsonObject.toString().getBytes();

            //   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SendTOServer(UpdateOtpVerify.this, UpdateOtpVerify.this, Const.MSGUPDATCONTACT,data,connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }


    }
    @Override
    public void requestSent(int value) {

    }

    @Override
    public void connected() {

    }

    @Override
    public void serverNotAvailable() {

    }

    @Override
    public void IntenrnetNotAvailable() {

    }

    @Override
    public void ConnectToserver(ConnectionProcess connectionProcess) {

    }

    @Override
    public void SocketDisconnected() {

    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // {"updateEmail":0,"newContact":"9867658339","updateContact":1,"message":" Updated  Successfully","status":1}
            //{"contact":"7900149918","verifyMobile":3,"message":"OTP is send on your registered email id.","status":1,"mobileOTP":8782}
            //ringProgressDialog.dismiss();
            AlertDialogClass.PopupWindowDismiss();

            String data = (String) msg.obj;
            Log.e("UpdateOtpVerified" , data);
            JSONObject jsonObject = null;

            int id = msg.arg1;
            switch (id) {
                case Const.MSGUPDATCONTACT:{
                    try {
                        jsonObject = new JSONObject(data);
                        int status = jsonObject.getInt("status"); //3
                        if (status == 0) {
                            String Message = jsonObject.getString("message");
                            Toast.makeText(UpdateOtpVerify.this, Message, Toast.LENGTH_LONG).show();
                            //Views.toast(AuthenticateUpdateProfile.this,Message);
                        }

                        if (status == 1) {

                            int updateContact = jsonObject.getInt("updateContact");
                            int updateEmail = jsonObject.getInt("updateEmail");

                            String Message = jsonObject.getString("message");
                            Toast.makeText(UpdateOtpVerify.this, Message, Toast.LENGTH_LONG).show();
                            if (updateContact == 1) {
                                Logics.setContact(UpdateOtpVerify.this, jsonObject.getString("newContact"));
                            }

                            if (updateEmail == 1) {
                                Logics.setEmail(UpdateOtpVerify.this, jsonObject.getString("newEmail"));
                            }


                            Log.e("ssssss", SharedPref.getPreferences(getApplicationContext(),Const.FromUpdate));
                            if (SharedPref.getPreferences(getApplicationContext(),Const.FromUpdate).equalsIgnoreCase(Const.FromProfile)){
                                SharedPref.savePreferences(getApplicationContext(),Const.FromUpdate,"");
                                startActivity(new Intent(UpdateOtpVerify.this, Dashboard.class));
                            } else {
//                                SharedPref.savePreferences(getApplicationContext(),Const.FromUpdate,"");
                                startActivity(new Intent(UpdateOtpVerify.this, OtpVerfication.class));
                            }
                           // startActivity(new Intent(UpdateOtpVerify.this, DashoboardDesign.class));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                    break;
                }

                case Const.MSGUPDATERESEND: {

                    try {
//{"updateEmail":0,"mobile":"9975153610","updateContact":1,"status":1,"mobileotp":4520}
                        jsonObject = new JSONObject(data);
                        //String message=jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status != 0) {
                            int updateContact = jsonObject.getInt("updateContact");
                            int updateEmail = jsonObject.getInt("updateEmail");
                            if (updateContact == 1) {
                                mobileOtp = jsonObject.getString("mobileotp");
                            } else if (updateEmail == 1) {
                                emailOtp = jsonObject.getString("emailotp");
                            }
                            //  _isSignup=jsonObject.getInt("isReg");

                        }else{


                            String mesg = jsonObject.getString("message");
                            AlertDialogClass.ShowMsg(UpdateOtpVerify.this,mesg);

                            ///

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialogClass.ShowMsg(UpdateOtpVerify.this,e.getMessage());

                    }
                    break;

                }
            }

        }
    }
}
