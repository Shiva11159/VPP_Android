package com.application.vpp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

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
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Utility.SnackBar;
import com.application.vpp.Views.Views;
import com.goodiebag.pinview.Pinview;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;

public class SignupScreen extends AppCompatActivity implements RequestSent, ConnectionProcess {

    String mobileNo = "", mobileOTP = "";
    int isMobile;

    int passwordattemptscount = 0;
    AlertDialog alertDialog;
    int resendOtpcount = 0;
    CountDownTimer countDownTimer;
    boolean timer_ = false;

    byte[] data;

    ConnectionProcess connectionProcess;
    RequestSent requestSent;
//    EditText edtFName, edtMName, edtLName, edt_mobile_no, edtPan, edtRefCode;
//    EditText edtHouseNo, edtArea, edtCity, edtPin, edtState, edtEmail;

    EditText edt_mobile_no;
    FancyButton btnSubmit;
    Button btn_login;
    //    String pan_no, name, number, selectedInitials, selectedState, selectedRef, refCode;
//    String fName, mName, lName;
//    String imei = Const.simNumber;
    public static Handler handlerSignup;
    //    ProgressDialog ringProgressDialog;
    Spinner spnrInitials, spinnerState, spnrRef;
    TextView txtFullName, imgerror, spnrError;
    String houseNum, area, city, pin, state, email;
    ScrollView mainlayout;
    ArrayList<String> states = new ArrayList<>();
    int maxLength = 15;
    TextView txt_privacypolicy;
    TextView txt_version;
    TextView txt_login;

    CheckBox checkbox_whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen_mobile);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
//        edtFName = (EditText) findViewById(R.id.edtFName);
//        edtMName = (EditText) findViewById(R.id.edtMName);
//        edtLName = (EditText) findViewById(R.id.edtLName);
//        edtMobile = (EditText) findViewById(R.id.edtMobile);
//        edtRefCode = (EditText) findViewById(R.id.edtreference);

        checkbox_whatsapp = findViewById(R.id.checkbox_whatsapp);
        txt_login = findViewById(R.id.txt_login);
        txt_version = findViewById(R.id.txt_version);
        txt_privacypolicy = findViewById(R.id.txt_privacypolicy);
        btnSubmit = (FancyButton) findViewById(R.id.btn_signup_submit);
        btn_login = (Button) findViewById(R.id.btn_login);
        edt_mobile_no = (EditText) findViewById(R.id.edt_mobile_no);

        txt_version.setText("v" + Methods.getVersionInfo(SignupScreen.this));

        checkbox_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_whatsapp.isChecked()){
                    SharedPref.savePreferences1(SignupScreen.this,"whatsappflag","Yes");
                }else {
                    SharedPref.savePreferences1(SignupScreen.this,"whatsappflag","No");
                }
            }
        });

        txt_privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupScreen.this, com.application.vpp.activity.PrivacyPolicy.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("link", "https://vpp.ventura1.com/VPP/privacy.html"));

            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupScreen.this, LoginScreen.class));
            }
        });

        SignupScreen.handlerSignup = null;
        SignupScreenEmail.handlerSignup = null;

        handlerSignup = new ViewHandler();
        mainlayout = (ScrollView) findViewById(R.id.mainlayout);


//        int isBankVerified=Logics.getisBankVerified(SignupScreen.this);
//        int isPanVerified=Logics.getisPanVerified(SignupScreen.this);
//        int isMobile=Logics.getisMobile_V(SignupScreen.this);
//        int isEmail=Logics.getisEmail_V(SignupScreen.this);
//        if (isBankVerified==1){
//            Toast.makeText(SignupScreen.this, "You have Completed Steps upto Bank Verfication", Toast.LENGTH_LONG).show();
//            Intent intent =  new Intent(SignupScreen.this,SignupScreen2.class);
//            startActivity(intent);
//        }
//       else if (isPanVerified==1){
//            Toast.makeText(SignupScreen.this, "You have Completed Steps upto Pan Verification", Toast.LENGTH_LONG).show();
//            Intent intent =  new Intent(SignupScreen.this,BankValidation.class);
//            startActivity(intent);
//        }
//        else if(isMobile==1 && isEmail==1){
//            Toast.makeText(SignupScreen.this, "Mobile and Email Id Already Verified", Toast.LENGTH_LONG).show();
//            Intent intent =  new Intent(SignupScreen.this,PanValidation.class);
//            startActivity(intent);
//        }
//        else {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.getNetworkState(getApplicationContext()))

                    if (edt_mobile_no.getText().toString().equalsIgnoreCase("")) {
                        edt_mobile_no.setError("Please enter mobile no.");
                        edt_mobile_no.startAnimation(shakeError());

                    } else if (edt_mobile_no.getText().toString().length()<10) {
                        edt_mobile_no.setError("Please enter valid mobile no.");
                        edt_mobile_no.startAnimation(shakeError());

                    } else if (!checkbox_whatsapp.isChecked()) {
                        checkbox_whatsapp.setError("Please select");
                        TastyToast.makeText(getApplicationContext(), "Please select checkbox", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                        checkbox_whatsapp.startAnimation(shakeError());

                    } else {
                        sendSIGNUP_OTP(edt_mobile_no.getText().toString().trim(),0,resendOtpcount);
                    }
//                validateRegPage1();
                else
                    Views.SweetAlert_NoDataAvailble(SignupScreen.this, "No Internet");
            }
        });

        // }

//        edtEmail.addTextChangedListener(this);
//
//        edt_mobile_no.addTextChangedListener(this);
//
//        edtEmail.addTextChangedListener(this);
//
//
//        edtEmail.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


    }

//    private void validation(){
//
//        imgerror.setVisibility(View.GONE);
//
//        Log.d("selectedInitials", "validation: "+selectedInitials);
//
//        houseNum = edtHouseNo.getText().toString().toUpperCase().trim();
//        area = edtArea.getText().toString().toUpperCase().trim();
//        //city = edtCity.getText().toString().toUpperCase().trim();
//        pin = edtPin.getText().toString().toUpperCase().trim();
//        //   state = edtState.getText().toString().toUpperCase().trim();
//        email = edtEmail.getText().toString().toUpperCase().trim();
//
//
//
//
//        pan_no = edtPan.getText().toString().toUpperCase().trim();
//
//        selectedState = spinnerState.getSelectedItem().toString();
//
//        if(houseNum.length()<1){
//            edtHouseNo.setError("Enter Valid House No./Wing/ Name of the Bldg.");
//            imgerror.setVisibility(View.VISIBLE);
//
//        }else if(area.length()<1){
//            edtArea.setError("Enter Valid Area");
//            imgerror.setVisibility(View.VISIBLE);
//
//        }else if(city.length()<3){
//            edtCity.setError("Enter Valid City");
//            imgerror.setVisibility(View.VISIBLE);
//
//        }else if(pin.length()!= 6){
//            edtPin.setError("Enter Valid Pin");
//            imgerror.setVisibility(View.VISIBLE);
//
//        } else if(spinnerState.getSelectedItemPosition() == 0){
//
//            spnrError.setVisibility(View.VISIBLE);
//            spnrError.setText("Please Enter Valid State");
//
//
//        }else {
//
//            spnrError.setVisibility(View.GONE);
//
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
//
//            //sendData();
//
//        }
//
//
//    }


//    private void validateRegPage1() {
//
//        btnSubmit.setEnabled(false);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
//        imgerror.setVisibility(View.GONE);
//
//        fName = edtFName.getText().toString().toUpperCase().trim();
//        mName = edtMName.getText().toString().toUpperCase().trim();
//        lName = edtLName.getText().toString().toUpperCase().trim();
//        name = fName + " " + mName + " " + lName;
//        number = edtMobile.getText().toString().toUpperCase().trim();
//
//        Logics.setfmlName(this, fName, mName, lName);
//
//        boolean validfn = validateLetters(fName);
//        boolean validln = validateLetters(lName);
//        if (fName.length() < 1 || !validfn || fName.isEmpty()) {
//            edtFName.setError("Enter Valid First Name");
//            imgerror.setVisibility(View.VISIBLE);
//            btnSubmit.setEnabled(true);
//
//
//        } else if (lName.length() < 1 || !validln || lName.isEmpty()) {
//            edtLName.setError("Enter Valid  Last Name");
//            imgerror.setVisibility(View.VISIBLE);
//            btnSubmit.setEnabled(true);
//
//
//        }
//
////        else if(!pan_no.matches( "[A-Z]{5}[0-9]{4}[A-Z]{1}")){
////
////            edtPan.setError("Enter Valid  PAN");
////            imgerror.setVisibility(View.VISIBLE);
////
////
////        }
//        else if (number.length() != 10 || number.isEmpty()) {
//
//            edtMobile.setError("Enter Valid  Number");
//            imgerror.setVisibility(View.VISIBLE);
//            btnSubmit.setEnabled(true);
//
//
//        }
////        else if (!isValidEmail(email)){
////
////            edtEmail.setError("Enter Valid Email");
////            imgerror.setVisibility(View.VISIBLE);
////
////        }
//        else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            edtEmail.setError("Enter Valid Email");
//            imgerror.setVisibility(View.VISIBLE);
//            btnSubmit.setEnabled(true);
//
//        } else {
//
//
//            sendDataRegPage1();
//
//
//        }
//
//
//    }

    @Override
    public void requestSent(int value) {

    }

//    @Override
//    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable editable) {
//
//        Log.d("change", "afterTextChanged: ");
//
//        //  imgerror.setVisibility(View.GONE);
//
//        //  houseNum = edtHouseNo.getText().toString().toUpperCase().trim();
////        area = edtArea.getText().toString().toUpperCase().trim();
////        city = edtCity.getText().toString().toUpperCase().trim();
////        pin = edtPin.getText().toString().toUpperCase().trim();
//        //    state = edtState.getText().toString().toUpperCase().trim();
//        email = edtEmail.getText().toString().toUpperCase().trim();
//
//        //  pan_no = edtPan.getText().toString().toUpperCase().trim();
//        number = edtMobile.getText().toString().toUpperCase().trim();
//        String fName = edtFName.getText().toString().toUpperCase().trim();
//        String mName = edtMName.getText().toString().toUpperCase().trim();
//        String lName = edtLName.getText().toString().toUpperCase().trim();
//
//
////       if(editable.hashCode() == edtPan.getText().hashCode()){
////
////           if(!pan_no.matches( "[A-Z]{5}[0-9]{4}[A-Z]{1}")){
////
////               edtPan.setError("Enter Valid PAN");
////              // imgerror.setVisibility(View.VISIBLE);
////           }
////       }else
//
//        if (editable.hashCode() == edtMobile.getText().hashCode()) {
//
//            if (number.length() != 10) {
//
//                edtMobile.setError("Enter Valid Number");
//                // imgerror.setVisibility(View.VISIBLE);
//            }
//
//
//        } else if (editable.hashCode() == edtEmail.getText().hashCode()) {
//
//            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                edtEmail.setError("Enter Valid Email");
//                imgerror.setVisibility(View.VISIBLE);
//
//            }
//        } else if (editable.hashCode() == edtFName.getText().hashCode() || editable.hashCode() == edtMName.getText().hashCode() || editable.hashCode() == edtLName.getText().hashCode()) {
//
//
////           txtFullName.setVisibility(View.VISIBLE);
////           txtFullName.setText("Name : "+fName + " "+mName+ " "+lName);
//
//        }
////       else if(editable.hashCode() == edtHouseNo.getText().hashCode()) {
////
////            if (houseNum.length() < 1) {
////                edtHouseNo.setError("Enter Valid House No./Wing/ Name of the Bldg.");
////               // imgerror.setVisibility(View.VISIBLE);
////            }
////        }else if(editable.hashCode() == edtArea.getText().hashCode()){
////
////            if(area.length()<1) {
////                edtArea.setError("Enter Valid Area");
////              //  imgerror.setVisibility(View.VISIBLE);
////            }
////        }
////        else if(editable.hashCode() == edtCity.getText().hashCode()){
////
////            if(city.length()<1) {
////                edtCity.setError("Enter Valid City");
////               // imgerror.setVisibility(View.VISIBLE);
////            }
////        }
////        else if(editable.hashCode() == edtPin.getText().hashCode()){
////
////            if(pin.length()!= 6) {
////                edtPin.setError("Enter Valid Pin");
////                imgerror.setVisibility(View.VISIBLE);
////            }
////        }
////        else if(editable.hashCode() == edtEmail.getText().hashCode()){
////
////            if(email.length()<1) {
////                edtEmail.setError("Enter Valid Email");
////                // imgerror.setVisibility(View.VISIBLE);
////            }
////        }
//
//
//    }

    public static boolean validateLetters(String txt) {

        String regx = "[a-zA-Z]+\\.?";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();

    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.arg1) {
                case Const.MSGFETCHDOCSTAT:

                    break;

                case Const.MSG_SIGNUP_OTP_NEW:
                    AlertDialogClass.PopupWindowDismiss();
                    String data1 = (String) msg.obj;
                    Log.e("signupResponse", data1);


                    Methods.hideKeyboard(getApplicationContext(), btn_login);

                    try {

                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(data1);
                        int status = jsonObject.getInt("status");

                        if (status == 1) {
                            mobileNo = jsonObject.getString("mobile");
                             isMobile = jsonObject.getInt("isMobile");
                            mobileOTP = jsonObject.getString("mobileotp");
                            mobileNo = jsonObject.getString("mobile");
                            PopUpOtp(mobileNo, mobileOTP);

                        }else{

                            String message = jsonObject.getString("message");
                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_SHORT, TastyToast.INFO);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        TastyToast.makeText(getApplicationContext(), e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    }

                    break;
            }


//                btnSubmit.setEnabled(true);
//                try {
//
//            //    ringProgressDialog.dismiss();
//
//                AlertDialogClass.PopupWindowDismiss();
//
//                String data = (String) msg.obj;
//                Log.d("Message", "handleMessageSignup: " + data);
//                JSONObject jsonObject = null;
//                jsonObject = new JSONObject(data);
//
//                int status = jsonObject.getInt("status");
//                if (status == 0) {
//                    String messg = jsonObject.getString("message");
//                    TastyToast.makeText(
//                            getApplicationContext(), messg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                } else if (status != 0) {
//
//                    int isRegistered = jsonObject.getInt("isRegistered");
//                    String mobileNum = jsonObject.getString("newContact");
//                    String mobileOTP = jsonObject.getString("mobileOTP");
//                    String emailOTP = jsonObject.getString("emailOTP");
//                    String message = jsonObject.getString("message");
//                    String email = jsonObject.getString("email");
//                    int reg_page_num = jsonObject.getInt("reg_page_num");
//
//                    Intent intent = new Intent(SignupScreen.this, OtpVerfication.class);
//
//                    intent.putExtra("isSignup", 0);
//                    intent.putExtra("strOtp", mobileOTP);
//                    intent.putExtra("mobileNum", mobileNum);
//                    intent.putExtra("strPan", pan_no);
//                    intent.putExtra("isRegistered", isRegistered);
//                    intent.putExtra("emailOTP", emailOTP);
//                    intent.putExtra("emailId", email);
////
////                    String mobilenum = jsonObject.getString("mobile");
////                    String email = jsonObject.getString("email");
//
//                    //  Toast.makeText(SignupScreen.this, "Reg page :"+mobileNum+email, Toast.LENGTH_SHORT).show();
//
//                    switch (reg_page_num) {
//
//                        case 1: {
//
//                            Intent intent1 = new Intent(SignupScreen.this, OtpVerfication.class);
//                            intent1.putExtra("mobile", mobileNum);
//                            //  intent1.putExtra("email",email);
//                            startActivity(intent);
//                        }
//                        break;
//
//                    }
//
//
//                } else {
//                    TastyToast.makeText(getApplicationContext(), "NetworkCall Error", TastyToast.LENGTH_LONG, TastyToast.INFO);
//                }
//

//            } catch (JSONException e) {
//                e.printStackTrace();
//                FirebaseCrashlytics.getInstance().recordException(e);
//                AlertDialogClass.ShowMsg(SignupScreen.this,e.getMessage());
//                btnSubmit.setEnabled(true);
//
//            }

        }
    }

    private void PopUpOtp(final String contactNo, String Otp) {
        resendOtpcount++;

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.otp_layout, viewGroup, false);
        Pinview pinview1 = (Pinview) dialogView.findViewById(R.id.pinview1);
        TextView txttimer = (TextView) dialogView.findViewById(R.id.txttimer);
        TextView txt_resendotp = (TextView) dialogView.findViewById(R.id.resendotptxt);
        TextView txtOtpMoborEmail = (TextView) dialogView.findViewById(R.id.txtOtpMoborEmail);
        TextView txtOtpMoborEmailheader = (TextView) dialogView.findViewById(R.id.txtOtpMoborEmailheader);


        try {
            timer(txttimer, txt_resendotp);
        } catch (Exception e) {
            //SnackBar.SnackBar(coordinatorLayout, e.getMessage());

        }

        txtOtpMoborEmail.setText(contactNo);
        txtOtpMoborEmailheader.setText("We have sent an OTP to your Mobile number");

        ProgressBar progressbar_resend = (ProgressBar) dialogView.findViewById(R.id.progressbar_resend);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) dialogView.findViewById(R.id.coordinatorLayout);
        ImageView imageclose = (ImageView) dialogView.findViewById(R.id.forgot_closeimage);


        TranslateAnimation animation = new TranslateAnimation(100.0f, 0.0f, 100.0f, 0.0f);
        animation.setDuration(1000);  // animation duration
        animation.setRepeatCount(0);  // animation repeat count

        pinview1.startAnimation(animation);
        imageclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                alertDialog.cancel();
            }
        });


        Log.e("resendOtpcount", String.valueOf(resendOtpcount));

        pinview1.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                String pin = pinview.getValue();
                if (Integer.parseInt(pin) > 3) {
//                    progressbar_resend.setVisibility(View.VISIBLE);
                    if (passwordattemptscount > 3) {
                        passwordattemptscount = 0;

                        pinview.clearValue();
                        int left = 3 - passwordattemptscount;
                        if (left == 0) {
                            alertDialog.cancel();
                            alertDialog.dismiss();
                            closeapp(getResources().getString(R.string.tryagain));
                        } else
                            SnackBar.SnackBar(coordinatorLayout, Methods.attemps(left));

                    } else {
                        if (Otp.equalsIgnoreCase(pin)) {

                            //mobile and mobileV saved ..by shiva.
                            SharedPref.savePreferences1(SignupScreen.this, "mobileNo", mobileNo);
                            SharedPref.savePreferences1(SignupScreen.this, "isMobile", String.valueOf(isMobile));

                            TastyToast.makeText(getApplicationContext(), getResources().getString(R.string.mobOtpverified), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                            startActivity(new Intent(SignupScreen.this, SignupScreenEmail.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                            alertDialog.dismiss();
                            alertDialog.cancel();

                            //call email verification tab ..
                            // here we will call bank verification ..
                            passwordattemptscount = 0;


                        } else {
                            pinview.clearValue();
                            passwordattemptscount++;
                            int left = 3 - passwordattemptscount;
                            if (left == 0) {
                                alertDialog.cancel();
                                alertDialog.dismiss();
                                closeapp(getResources().getString(R.string.tryagain));
                                passwordattemptscount = 0;
                            } else

                                SnackBar.SnackBar(coordinatorLayout, Methods.attemps(left));

//                            TastyToast.makeText(getApplicationContext(), "Otp not verified", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                        Methods.hideKeyboard(SignupScreen.this, pinview1);


                    }
                    Log.e("verifyPin", pin);
                }
            }
        });


        txt_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resendOtpcount > 2) {
                    closeapp("No more attempt for Resend OTP, try after sometime.");
                    alertDialog.dismiss();
                    alertDialog.cancel();
//                    TastyToast.makeText(getApplicationContext(), "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                } else {
                    // sendData(email,contact, radioGroup);
                    sendSIGNUP_OTP(edt_mobile_no.getText().toString().trim(),Integer.parseInt(Otp),1);
                    alertDialog.dismiss();
                    alertDialog.cancel();
                }
            }
        });

        imageclose.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void sendSIGNUP_OTP(String mobielNo,int Otp,int resendOtpCount) {
        try {
            AlertDialogClass.PopupWindowShow(SignupScreen.this, mainlayout);
            JSONObject json = new JSONObject();
            json.put("email", 0); //email id empty
            json.put("mobile", mobielNo);  //mobile no passing here
            json.put("isemail", 0);
            json.put("isMobile", 1); //calling mobile flag 1 otp
            json.put("whatsappflag", ""); // whatsapp flag inserting after email otp gt done.
            json.put("resendOtpCount", resendOtpCount); // whatsapp flag inserting after email otp gt done.
            json.put("Otp", Otp); // whatsapp flag inserting after email otp gt done.

            data = json.toString().getBytes();

            Log.e("signupRequest", json.toString());
            new SendTOServer(this, this, Const.MSG_SIGNUP_OTP_NEW, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(SignupScreen.this, e.getMessage());
        }
    }


//    private void sendDataRegPage1() {
//        btnSubmit.setEnabled(false);
//
//        selectedInitials = spnrInitials.getSelectedItem().toString();
//        selectedRef = spnrRef.getSelectedItem().toString();
//        refCode = edtRefCode.getText().toString().toUpperCase().trim();
//        Logics.setRef(SignupScreen.this, selectedRef);
//        Logics.setRefCode(SignupScreen.this, refCode);
//
//        //selectedState = spinnerState.getSelectedItem().toString();
//
////        ringProgressDialog = ProgressDialog.show(SignupScreen.this, "Please wait ...", "Loading Your Data ...", true);
////        ringProgressDialog.setCancelable(true);
////        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//
//
//        AlertDialogClass.PopupWindowShow(SignupScreen.this, mainlayout);
//
//        String imei = Logics.getDeviceID(this);
//        String ip = Logics.getSimId(this);
//        if (android.os.Build.VERSION.SDK_INT >= 29) {
//            imei = Logics.getTokenID(SignupScreen.this);
//
//            ip = "12345";
//        }
//        JSONObject jsonObject = new JSONObject();
//        try {
//
//            jsonObject.put("imei", imei);
//            jsonObject.put("ip", ip);
//            //  jsonObject.put("ip","1234566666");
//            // jsonObject.put("pan_no",pan_no);
//            jsonObject.put("name", selectedInitials + " " + name);
//            jsonObject.put("fname", fName);
//            jsonObject.put("mName", mName);
//            jsonObject.put("lName", lName);
//            jsonObject.put("mobile", number);
//
////            jsonObject.put("houseNum", houseNum);
////            jsonObject.put("area", area);
////            jsonObject.put("city", city);
////            jsonObject.put("pin", pin);
////            jsonObject.put("state", selectedState);
//            jsonObject.put("email", email);
//
//            jsonObject.put("isSignup", 1);
//            jsonObject.put("regpegno", 1);
//
//            byte[] data = jsonObject.toString().getBytes();
//
//            Log.d("data", "sendData: " + jsonObject.toString());
//
//            new SendTOServer(SignupScreen.this, SignupScreen.this, Const.MSGSIGNUP, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            FirebaseCrashlytics.getInstance().recordException(e);
//            AlertDialogClass.ShowMsg(SignupScreen.this,e.getMessage());
//
//        }
//    }


//    private boolean checkAlreadyVerified(String email, String moile) {
//        boolean isVerified = false;
//        String verifiedMobile = Logics.getMobile_1(SignupScreen.this);
//        String verifiedEmail = Logics.getEmail_1(SignupScreen.this);
//        if (verifiedEmail == null || verifiedMobile == null) {
//            return isVerified;
//        } else if (verifiedEmail.equals(email) && verifiedMobile.equals(moile)) {
//            int isEmail_v = Logics.getisEmail_V(SignupScreen.this);
//            int is_Mobile_v = Logics.getisMobile_V(SignupScreen.this);
//            if (isEmail_v == 1 && is_Mobile_v == 1) {
//                isVerified = true;
//                return isVerified;
//            } else {
//                return isVerified;
//            }
//        } else {
//            return isVerified;
//        }
//
//        // return  isVerified;
//    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }


        Log.e("connected11", "connected11");
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    sendSIGNUP_OTP(edt_mobile_no.getText().toString().trim(),0,0);
                else
                    Views.SweetAlert_NoDataAvailble(SignupScreen.this, "No Internet");
                //  TastyToast.makeText(SignupScreen.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        btnSubmit.setEnabled(true);

        AlertDialogClass.PopupWindowDismiss();
        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(SignupScreen.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(SignupScreen.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        btnSubmit.setEnabled(true);

        AlertDialogClass.PopupWindowDismiss();
        Views.SweetAlert_NoDataAvailble(SignupScreen.this, "Internet Not Available");
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
        btnSubmit.setEnabled(true);

        AlertDialogClass.PopupWindowDismiss();
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(SignupScreen.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Views.ProgressDlgConnectSocket(SignupScreen.this, connectionProcess, "Server Not Available");
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
        btnSubmit.setEnabled(true);

        AlertDialogClass.PopupWindowDismiss();
        Log.e("SocketDisconnected_x: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(SignupScreen.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(SignupScreen.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finishAffinity();
    }


    private void timer(final TextView timer, final TextView txtResend) {
        timer.setVisibility(View.VISIBLE);
        txtResend.setVisibility(View.GONE);
        // countDownTimer = new  CountDownTimer(119000,1000){
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtResend.setVisibility(View.VISIBLE);
                txtResend.setTextColor(ContextCompat.getColor(SignupScreen.this, R.color.gray400));
                txtResend.setClickable(false);
                timer_ = true;
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
                timer_ = false;
                timer.setVisibility(View.INVISIBLE);
                txtResend.setVisibility(View.VISIBLE);
                txtResend.setClickable(true);

                txtResend.setTextColor(ContextCompat.getColor(SignupScreen.this, R.color.ventura_color));

            }
        };
        countDownTimer.start();
    }

    void closeapp(String ss) {

        Methods.hideKeyboard(SignupScreen.this,btn_login);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme1));
        builder.setMessage(ss)
                .setCancelable(false)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                       Dashboard.this.finish();
//                        finishAffinity();
                        finish();
                        dialog.dismiss();

                    }
                })
                .show();
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

}
