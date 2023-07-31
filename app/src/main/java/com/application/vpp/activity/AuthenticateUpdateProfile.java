package com.application.vpp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class AuthenticateUpdateProfile extends NavigationDrawer implements TextWatcher,  View.OnClickListener , RequestSent  {
    EditText edt_mob_otp, edt_email_otp;
//    ProgressDialog ringProgressDialog;
    public  static Handler resendAuthenticateHandler;
    ScrollView mainlayout;

    String strMobOtp, mobileOtp, mobileNum, emailOtp, strEmailOtp, existingContact, existingEmail, newEmail, email;
    public int verifyMobile = 0, verifyEmail = 1, updateContact = 0, updateEmail = 0;
    TextView mob, emailid, txt_resend_mob, txt_email_resend,textView1,textView3;
    LinearLayout linearemail,linear3,linearMobile;
    FancyButton btn1, btn2, btn_proceed;

    //----------------
    NestedScrollView nestedscrollview;
    LinearLayout linearlayout_main_otp_screen,layout_mob,layout_email;
    EditText _edtProfileContact, _edtProfileEmail;
    TextView _txtProfileVPPId, _imgerror;
    FancyButton _btnSaveProfile;
    String _newContact, _newEmail, _mobileOTP, _contact, _emailOTP, _email;
    public int _updateEmail = 0, _updateContact = 0;
    String _existingContact, _existingEmail;
    //CoordinatorLayout mainlayout;
    int _isSignup,edtEmail,edtMob;
    //ProgressDialog ringProgressDialog;
    public static Handler updateProfileHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_otpauth, mDrawerLayout);
        resendAuthenticateHandler = new ViewHandler();
        linearemail = findViewById(R.id.linear2);
        linear3= findViewById(R.id.linear3);
        linearMobile=findViewById(R.id.linearFirst);
        mob = findViewById(R.id.textView2);
        emailid = findViewById(R.id.textView4);
        txt_resend_mob = (TextView) findViewById(R.id.txt_resend_mob);
        txt_resend_mob.setVisibility(View.GONE);
        txt_email_resend = (TextView) findViewById(R.id.txt_email_resend);
        layout_email=(LinearLayout) findViewById(R.id.layout_email);
        layout_mob=(LinearLayout) findViewById(R.id.layout_mob);
        mainlayout = (ScrollView) findViewById(R.id.mainlayout);


        txt_email_resend.setVisibility(View.GONE);
        if (getIntent().getExtras() != null) {
            email = getIntent().getExtras().getString("email", null);
            mobileOtp = getIntent().getExtras().getString("strOtp", null);
            emailOtp = getIntent().getExtras().getString("emailOTP", null);
            updateContact = getIntent().getExtras().getInt("updateContact", 0);
            updateEmail = getIntent().getExtras().getInt("updateEmail", 0);
            edtEmail=getIntent().getExtras().getInt("edtEmail", 0);
            edtMob=getIntent().getExtras().getInt("edtMobile", 0);

            mobileNum = getIntent().getExtras().getString("mobileNum", null);
            email=getIntent().getExtras().getString("email", null);
            btn_proceed = (FancyButton) findViewById(R.id.btn_proceed);

            btn_proceed.setOnClickListener(this);
            emailid.setText(email);
            mob.setText(mobileNum);
            if(updateContact==1 && updateEmail==0){
                txt_resend_mob.setVisibility(View.VISIBLE);
                linearemail.setVisibility(View.GONE);
            }
            else  if(updateContact==0 && updateEmail==1){
                linearMobile.setVisibility(View.GONE);
                linearemail.setVisibility(View.VISIBLE);
                txt_email_resend.setVisibility(View.VISIBLE);
            }
        }
        textView1=(TextView) findViewById(R.id.textView1);
        textView3=(TextView) findViewById(R.id.textView3);

        edt_mob_otp = (EditText) findViewById(R.id.edt_mob_otp);
        edt_email_otp = (EditText) findViewById(R.id.edt_email_otp);
        btn1 = (FancyButton) findViewById(R.id.btn_mob_submit);
        btn2 = (FancyButton) findViewById(R.id.btn_email_submit);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        txt_email_resend.setOnClickListener(this);
        txt_resend_mob.setOnClickListener(this);


        //--------------------------------------------------------------------
        linearlayout_main_otp_screen = findViewById(R.id.linearlayout_main_otp_screen);
        nestedscrollview = findViewById(R.id.nestedscrollview);
        _txtProfileVPPId = (TextView)findViewById(R.id.txtProfileVPPId);
        _imgerror = (TextView)findViewById(R.id.imgerror);

        _edtProfileContact = (EditText)findViewById(R.id.txtProfileContact);
        _edtProfileEmail = (EditText)findViewById(R.id.txtProfileEmail);
        _btnSaveProfile = (FancyButton) findViewById(R.id.btnSaveProfile);

        _btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
        _edtProfileContact.addTextChangedListener(this);
        _edtProfileEmail.addTextChangedListener(this);
        //updateProfileHandler = new ViewHandler();


    }
    public void VerifyMoblieOtp() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        strMobOtp = edt_mob_otp.getText().toString().toUpperCase();
        if (strMobOtp.matches(mobileOtp)) {
            //  Toast.makeText(AuthenticateUpdateProfile.this, " Mobile number verified", Toast.LENGTH_LONG).show();
            TastyToast.makeText(getApplicationContext(), "Mobile number verified", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

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
            TastyToast.makeText(getApplicationContext(), "Enter Valid Mobile Otp", TastyToast.LENGTH_LONG, TastyToast.ERROR);

            //Toast.makeText(AuthenticateUpdateProfile.this, "Enter Valid Mobile Otp", Toast.LENGTH_SHORT).show();
        }



    }
    //-----------------update class method--------
    private void validation() {

        _imgerror.setVisibility(View.GONE);

        if(SharedPref.getPreferences(AuthenticateUpdateProfile.this,"click").equalsIgnoreCase("email")){
//        if(updateEmail==1){
            _newEmail = _edtProfileEmail.getText().toString().toUpperCase().trim();

            if (!isValidEmail(_newEmail)){

                _edtProfileEmail.setError("Enter Valid Email");
                _imgerror.setVisibility(View.VISIBLE);

            }else {
                _updateEmail=1;
                sendData();
            }

        }
        else {
            _newContact = _edtProfileContact.getText().toString().toUpperCase().trim();

            if (_newContact.length()!=10){

                _edtProfileContact.setError("Enter Valid  Number");
                _imgerror.setVisibility(View.VISIBLE);


            }else {
                _updateContact=1;
                sendData();

            }
        }

    }
    //---------update profile method----------
    private void sendData(){

//        ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));


        AlertDialogClass.PopupWindowShow(AuthenticateUpdateProfile.this,mainlayout);

        String imei = Logics.getDeviceID(this);
        String simId = Logics.getSimId(this);

        JSONObject jsonObject = new JSONObject();
        _isSignup = 3;

        //18-10-19 check for change in profile (Contact, email)
        if(_existingContact.equals(_newContact) && _existingEmail.equals(_newEmail)){

//            ringProgressDialog.dismiss();
            AlertDialogClass.PopupWindowDismiss();
            TastyToast.makeText(getApplicationContext(), "There is a no change in a profile.", TastyToast.LENGTH_LONG, TastyToast.INFO);

            //   Views.toast(this,"There is a no change in a profile. ");
        }else {

//                //if not equal then 1
//                if (!_existingContact.equals(_newContact)) {
//                    _updateContact = 1;
//                } else if (!_existingEmail.equals(_newEmail)) {
//                    _updateEmail = 1;
//                }

        }

        try {
            jsonObject.put("imei", imei);
            jsonObject.put("sim", simId);
            jsonObject.put("pan", Logics.getPanNo(this));
            jsonObject.put("vppid", Logics.getVppId(this));
            jsonObject.put("otp", 1);
            jsonObject.put("mobile", _newContact);

            jsonObject.put("updateContact", _updateContact);
            jsonObject.put("email", _newEmail);
            jsonObject.put("updateEmail", _updateEmail);

            jsonObject.put("isSignup", _isSignup);

            byte[] data = jsonObject.toString().getBytes();

            Log.d("data", "sendData: " + jsonObject.toString());

            new SendTOServer(this, this, Const.MSGUPDATEPROFILE, data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            //verify otp on new mobile number
            //  Views.toast(UpdateProfile.this, "OTP Sent.... kindly wait for some moment ");

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }


    //------------update profile method------
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //--------------update profile method ----watcher-----------

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        Log.d("change", "afterTextChanged: ");

        _imgerror.setVisibility(View.GONE);
        _newContact = _edtProfileContact.getText().toString().toUpperCase().trim();

        if(editable.hashCode() == _edtProfileContact.getText().hashCode()){

            if (_newContact.length()!=10){

                _edtProfileContact.setError("Enter Valid Number");
                _imgerror.setVisibility(View.VISIBLE);
            }
        }

    }
//--------------update profile ----close ------------------

    public void VerifyEmailOtp() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        strEmailOtp = edt_email_otp.getText().toString().toUpperCase();
        if (strEmailOtp.matches(emailOtp)) {
            //   Toast.makeText(AuthenticateUpdateProfile.this, " Email Id Otp Verified", Toast.LENGTH_LONG).show();
            TastyToast.makeText(getApplicationContext(), "Email id verified", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            txt_email_resend.setVisibility(View.VISIBLE);
            txt_email_resend.setText("Your New Email Id is verified");
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
            //   Toast.makeText(AuthenticateUpdateProfile.this, "Enter Valid Email Otp", Toast.LENGTH_SHORT).show();
            TastyToast.makeText(getApplicationContext(), "Enter Valid Email Otp", TastyToast.LENGTH_LONG, TastyToast.ERROR);

        }

    }
    private void proceed() {
//        if(verifyMobile==1 &&updateContact==1 &&updateEmail==0){ //
        if(SharedPref.getPreferences(AuthenticateUpdateProfile.this,"click").equalsIgnoreCase("mobile")){ //
            //Intent intent = new Intent(AuthenticateUpdateProfile.this, UpdateProfile.class);
            //  startActivity(intent);
            nestedscrollview.setVisibility(View.VISIBLE);
            layout_mob.setVisibility(View.VISIBLE);
            _edtProfileContact.setText(_existingContact);
            linearlayout_main_otp_screen.setVisibility(View.GONE);

            String vppId = Logics.getVppId(this);
            _existingContact = Logics.getContact(this);
            _existingEmail = Logics.getEmail(this);
            _txtProfileVPPId.setText(vppId);
//            if(edtMob==1){
//                layout_mob.setVisibility(View.VISIBLE);
//                _edtProfileContact.setText(_existingContact);
//
//            }
//            else if(edtEmail==1){
//                layout_email.setVisibility(View.VISIBLE);
//                _edtProfileEmail.setText(_existingEmail);
//
//            }


        }
        else if(SharedPref.getPreferences(AuthenticateUpdateProfile.this,"click").equalsIgnoreCase("email")){
            // Intent intent = new Intent(AuthenticateUpdateProfile.this, UpdateProfile.class);
            // startActivity(intent);
            nestedscrollview.setVisibility(View.VISIBLE);
            layout_email.setVisibility(View.VISIBLE);
            _edtProfileEmail.setText(_existingEmail);

            linearlayout_main_otp_screen.setVisibility(View.GONE);

            String vppId = Logics.getVppId(this);
            _existingContact = Logics.getContact(this);
            _existingEmail = Logics.getEmail(this);
            _txtProfileVPPId.setText(vppId);
//            if(edtMob==1){
//                layout_mob.setVisibility(View.VISIBLE);
//
//
//                _edtProfileContact.setText(_existingContact);
//
//            }
//            else if(edtEmail==1){
//                layout_email.setVisibility(View.VISIBLE);
//                _edtProfileEmail.setText(_existingEmail);
//
//            }
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
            case R.id.btn_proceed: {
                proceed();
            }
            break;
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
//            ringProgressDialog = ProgressDialog.show(AuthenticateUpdateProfile.this, "Please wait ...", "Loading Your Data ...", true);
//            ringProgressDialog.setCancelable(true);
//            ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

            AlertDialogClass.PopupWindowShow(AuthenticateUpdateProfile.this,mainlayout);

            String EmailId = Logics.getEmail(AuthenticateUpdateProfile.this);
            String mobile = Logics.getContact(AuthenticateUpdateProfile.this);
            JSONObject json = new JSONObject();
            json.put("email", EmailId);
            json.put("mobile", mobile);
            json.put("isemail", updateEmail);
            json.put("isMobile", updateContact);
            byte[] data = json.toString().getBytes();
            new SendTOServer(this, this, Const.MSGAUTHENTICATERESEND, data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }

    @Override
    public void requestSent(int value) {

    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //{"contact":"7900149918","verifyMobile":3,"message":"OTP is send on your registered email id.","status":1,"mobileOTP":8782}
//            ringProgressDialog.dismiss();
            AlertDialogClass.PopupWindowDismiss();
            String data = (String)msg.obj;
            Log.e("OTPVERYFYUTH",data);
            JSONObject jsonObject = null;

            int id = msg.arg1;
            switch (id) {
                case Const.MSGUPDATEPROFILE: {

                    try {

                        AlertDialogClass.PopupWindowDismiss();
//                        ringProgressDialog.dismiss();

                        jsonObject = new JSONObject(data);
                        int status = jsonObject.getInt("status"); //3
                        if(status==0){
                            String Message=jsonObject.getString("Message");
                            AlertDialogClass.ShowMsg(AuthenticateUpdateProfile.this,Message);

                            //  Toast.makeText(AuthenticateUpdateProfile.this,Message,Toast.LENGTH_LONG).show();
//                            TastyToast.makeText(getApplicationContext(), Message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        }

                        if(status != 0){

                            //String message = jsonObject.getString("message");
                            int updateContact = jsonObject.getInt("updateContact");
                            int updateEmail = jsonObject.getInt("updateEmail");



                            if (updateContact == 1 && updateEmail == 1) {

// data:{"emailOTP":1728,"verifyMobile":3,"newContact":"9665538502","isRegistered":1,"newEmail":"chavanapurva56@gmail.com","message":"OTP is send on your registered email id.","status":3,"mobileOTP":6491} msgCode: 31
                                _mobileOTP = jsonObject.getString("mobileotp");
                                _emailOTP = jsonObject.getString("emailOTP");

                                //  Logics.setContact(UpdateProfile.this,newContact);
                                //Logics.setEmail(UpdateProfile.this,newEmail);

                            } else {
                                if (updateContact == 1) {

// data:{"verifyMobile":3,"newContact":"7900149988","message":"OTP is send on your new mobile number.","status":3,"mobileOTP":1892} msgCode: 31
// verify contact
                                    _mobileOTP = jsonObject.getString("mobileotp");
                                    _newContact=jsonObject.getString("mobile");

                                    Intent intent =  new Intent(AuthenticateUpdateProfile.this, UpdateOtpVerify.class);
                                    // intent.putExtra("isSignup",1);
                                    intent.putExtra("strOtp",_mobileOTP);
                                    intent.putExtra("mobileNum",_newContact);
                                    intent.putExtra("updateEmail", 0);
                                    intent.putExtra("updateContact",1);

                                    startActivity(intent);
                                    //Logics.setContact(UpdateProfile.this,newContact);
                                }
                                if (updateEmail == 1) {

                                    _emailOTP = jsonObject.getString("emailotp");
                                    _newEmail=jsonObject.getString("email");

                                    Intent intent =  new Intent(AuthenticateUpdateProfile.this, UpdateOtpVerify.class);
                                    // intent.putExtra("isSignup",1);
                                    intent.putExtra("emailOTP",_emailOTP);
                                    intent.putExtra("email",_newEmail);
                                    intent.putExtra("updateEmail", 1);
                                    intent.putExtra("updateContact",0);

                                    startActivity(intent);
                                    //Logics.setEmail(UpdateProfile.this,newEmail);
                                }
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialogClass.ShowMsg(AuthenticateUpdateProfile.this,e.getMessage());

                    }

                    break;
                }

//                case Const.MSGAUTHENTICATE: {
//
//                    try {
//                        jsonObject = new JSONObject(data);
//                        int status = jsonObject.getInt("status"); //3
//                        if (status == 1) {
//                            int updateContact = jsonObject.getInt("updateContact");
//                            int updateEmail = jsonObject.getInt("updateEmail");
//                            if (updateContact == 1) {
//                                mobileOtp = jsonObject.getString("mobileotp");
//                                String mobile = jsonObject.getString("mobile");
//
//                            } else if (updateEmail == 1) {
//                                emailOtp = jsonObject.getString("emailotp");
//                                String email = jsonObject.getString("email");
//
//                            }
//
//                        }else{
//
//
//                            String mesg = jsonObject.getString("message");
//                            AlertDialogClass.ShowMsg(AuthenticateUpdateProfile.this,mesg);
//
//                            ///
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        FirebaseCrashlytics.getInstance().recordException(e);
//                        AlertDialogClass.ShowMsg(AuthenticateUpdateProfile.this,e.getMessage());
//                    }
//                    break;
//                }
                case Const.MSGAUTHENTICATERESEND: {

                    try {
//{"updateEmail":0,"mobile":"9975153610","updateContact":1,"status":1,"mobileotp":4520}
                        jsonObject = new JSONObject(data);
                        //String message=jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status != 0) {
                            int updateContact=jsonObject.getInt("updateContact");
                            int updateEmail=jsonObject.getInt("updateEmail");
                            if(updateContact==1){
                                mobileOtp=jsonObject.getString("mobileotp");
                            }
                            else  if(updateEmail==1){
                                emailOtp=jsonObject.getString("emailotp");
                            }
                            //  _isSignup=jsonObject.getInt("isReg");

                        }else{


                            String mesg = jsonObject.getString("message");
                            AlertDialogClass.ShowMsg(AuthenticateUpdateProfile.this,mesg);

                            ///

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialogClass.ShowMsg(AuthenticateUpdateProfile.this,e.getMessage());

                    }


                }
                break;

            }
        }
    }
}
