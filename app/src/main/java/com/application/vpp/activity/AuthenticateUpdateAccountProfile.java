package com.application.vpp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.sdsmdg.tastytoast.TastyToast;

import mehdi.sakout.fancybuttons.FancyButton;

public class AuthenticateUpdateAccountProfile extends NavigationDrawer implements TextWatcher,  View.OnClickListener , RequestSent {
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
    int _isSignup,edtEmail,edtMob;
    //ProgressDialog ringProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_authenticate_update_account_profile, mDrawerLayout);
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
            txt_resend_mob.setText(getResources().getString(R.string.tryagain));
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
        } else {
            TastyToast.makeText(getApplicationContext(), "Enter Valid Mobile Otp", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onClick(View v) {
    }
}