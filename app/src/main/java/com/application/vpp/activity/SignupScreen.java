package com.application.vpp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;

public class

SignupScreen extends AppCompatActivity implements RequestSent, TextWatcher, ConnectionProcess {
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    EditText edtFName, edtMName, edtLName, edtMobile, edtPan, edtRefCode;
    EditText edtHouseNo, edtArea, edtCity, edtPin, edtState, edtEmail;
    FancyButton btnSubmit;
    String pan_no, name, number, selectedInitials, selectedState, selectedRef, refCode;
    String fName, mName, lName;
    String imei = Const.simNumber;
    public static Handler handlerSignup;
//    ProgressDialog ringProgressDialog;
    Spinner spnrInitials, spinnerState, spnrRef;
    TextView txtFullName, imgerror, spnrError;
    String houseNum, area, city, pin, state, email;
    ScrollView mainlayout;
    ArrayList<String> states = new ArrayList<>();
    int maxLength = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        edtFName = (EditText) findViewById(R.id.edtFName);
        edtMName = (EditText) findViewById(R.id.edtMName);
        edtLName = (EditText) findViewById(R.id.edtLName);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtRefCode = (EditText) findViewById(R.id.edtreference);


        btnSubmit = (FancyButton) findViewById(R.id.btn_signup_submit);
        spnrInitials = (Spinner) findViewById(R.id.spnrInitials);
        spnrRef = (Spinner) findViewById(R.id.spnrSelectRef);

        // txtFullName = (TextView)findViewById(R.id.txtFullName);
        imgerror = (TextView) findViewById(R.id.imgerror);


        edtEmail = (EditText) findViewById(R.id.edtEmail);
        imgerror = (TextView) findViewById(R.id.imgerror);
        btnSubmit = (FancyButton) findViewById(R.id.btn_signup_submit);


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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.getNetworkState(getApplicationContext()))
                validateRegPage1();
                else
                    Views.SweetAlert_NoDataAvailble(SignupScreen.this,"No Internet");
            }
        });

        // }

        edtEmail.addTextChangedListener(this);

        edtMobile.addTextChangedListener(this);

        edtEmail.addTextChangedListener(this);


        edtEmail.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


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


    private void validateRegPage1() {

        btnSubmit.setEnabled(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        imgerror.setVisibility(View.GONE);

        fName = edtFName.getText().toString().toUpperCase().trim();
        mName = edtMName.getText().toString().toUpperCase().trim();
        lName = edtLName.getText().toString().toUpperCase().trim();
        name = fName + " " + mName + " " + lName;
        number = edtMobile.getText().toString().toUpperCase().trim();

        Logics.setfmlName(this, fName, mName, lName);

        boolean validfn = validateLetters(fName);
        boolean validln = validateLetters(lName);
        if (fName.length() < 1 || !validfn || fName.isEmpty()) {
            edtFName.setError("Enter Valid First Name");
            imgerror.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(true);


        } else if (lName.length() < 1 || !validln || lName.isEmpty()) {
            edtLName.setError("Enter Valid  Last Name");
            imgerror.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(true);


        }

//        else if(!pan_no.matches( "[A-Z]{5}[0-9]{4}[A-Z]{1}")){
//
//            edtPan.setError("Enter Valid  PAN");
//            imgerror.setVisibility(View.VISIBLE);
//
//
//        }
        else if (number.length() != 10 || number.isEmpty()) {

            edtMobile.setError("Enter Valid  Number");
            imgerror.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(true);


        }
//        else if (!isValidEmail(email)){
//
//            edtEmail.setError("Enter Valid Email");
//            imgerror.setVisibility(View.VISIBLE);
//
//        }
        else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter Valid Email");
            imgerror.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(true);

        } else {


            sendDataRegPage1();


        }


    }

    @Override
    public void requestSent(int value) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        Log.d("change", "afterTextChanged: ");

        //  imgerror.setVisibility(View.GONE);

        //  houseNum = edtHouseNo.getText().toString().toUpperCase().trim();
//        area = edtArea.getText().toString().toUpperCase().trim();
//        city = edtCity.getText().toString().toUpperCase().trim();
//        pin = edtPin.getText().toString().toUpperCase().trim();
        //    state = edtState.getText().toString().toUpperCase().trim();
        email = edtEmail.getText().toString().toUpperCase().trim();

        //  pan_no = edtPan.getText().toString().toUpperCase().trim();
        number = edtMobile.getText().toString().toUpperCase().trim();
        String fName = edtFName.getText().toString().toUpperCase().trim();
        String mName = edtMName.getText().toString().toUpperCase().trim();
        String lName = edtLName.getText().toString().toUpperCase().trim();


//       if(editable.hashCode() == edtPan.getText().hashCode()){
//
//           if(!pan_no.matches( "[A-Z]{5}[0-9]{4}[A-Z]{1}")){
//
//               edtPan.setError("Enter Valid PAN");
//              // imgerror.setVisibility(View.VISIBLE);
//           }
//       }else

        if (editable.hashCode() == edtMobile.getText().hashCode()) {

            if (number.length() != 10) {

                edtMobile.setError("Enter Valid Number");
                // imgerror.setVisibility(View.VISIBLE);
            }


        } else if (editable.hashCode() == edtEmail.getText().hashCode()) {

            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError("Enter Valid Email");
                imgerror.setVisibility(View.VISIBLE);

            }
        } else if (editable.hashCode() == edtFName.getText().hashCode() || editable.hashCode() == edtMName.getText().hashCode() || editable.hashCode() == edtLName.getText().hashCode()) {


//           txtFullName.setVisibility(View.VISIBLE);
//           txtFullName.setText("Name : "+fName + " "+mName+ " "+lName);

        }
//       else if(editable.hashCode() == edtHouseNo.getText().hashCode()) {
//
//            if (houseNum.length() < 1) {
//                edtHouseNo.setError("Enter Valid House No./Wing/ Name of the Bldg.");
//               // imgerror.setVisibility(View.VISIBLE);
//            }
//        }else if(editable.hashCode() == edtArea.getText().hashCode()){
//
//            if(area.length()<1) {
//                edtArea.setError("Enter Valid Area");
//              //  imgerror.setVisibility(View.VISIBLE);
//            }
//        }
//        else if(editable.hashCode() == edtCity.getText().hashCode()){
//
//            if(city.length()<1) {
//                edtCity.setError("Enter Valid City");
//               // imgerror.setVisibility(View.VISIBLE);
//            }
//        }
//        else if(editable.hashCode() == edtPin.getText().hashCode()){
//
//            if(pin.length()!= 6) {
//                edtPin.setError("Enter Valid Pin");
//                imgerror.setVisibility(View.VISIBLE);
//            }
//        }
//        else if(editable.hashCode() == edtEmail.getText().hashCode()){
//
//            if(email.length()<1) {
//                edtEmail.setError("Enter Valid Email");
//                // imgerror.setVisibility(View.VISIBLE);
//            }
//        }


    }

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

btnSubmit.setEnabled(true);
            try {

            //    ringProgressDialog.dismiss();

                AlertDialogClass.PopupWindowDismiss();

                String data = (String) msg.obj;
                Log.d("Message", "handleMessageSignup: " + data);
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(data);

                int status = jsonObject.getInt("status");
                if (status == 0) {
                    String messg = jsonObject.getString("message");
                    TastyToast.makeText(
                            getApplicationContext(), messg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                } else if (status != 0) {

                    int isRegistered = jsonObject.getInt("isRegistered");
                    String mobileNum = jsonObject.getString("newContact");
                    String mobileOTP = jsonObject.getString("mobileOTP");
                    String emailOTP = jsonObject.getString("emailOTP");
                    String message = jsonObject.getString("message");
                    String email = jsonObject.getString("email");
                    int reg_page_num = jsonObject.getInt("reg_page_num");

                    Intent intent = new Intent(SignupScreen.this, OtpVerfication.class);
                    intent.putExtra("isSignup", 0);
                    intent.putExtra("strOtp", mobileOTP);
                    intent.putExtra("mobileNum", mobileNum);
                    intent.putExtra("strPan", pan_no);
                    intent.putExtra("isRegistered", isRegistered);
                    intent.putExtra("emailOTP", emailOTP);
                    intent.putExtra("emailId", email);
//
//                    String mobilenum = jsonObject.getString("mobile");
//                    String email = jsonObject.getString("email");

                    //  Toast.makeText(SignupScreen.this, "Reg page :"+mobileNum+email, Toast.LENGTH_SHORT).show();

                    switch (reg_page_num) {

                        case 1: {

                            Intent intent1 = new Intent(SignupScreen.this, OtpVerfication.class);
                            intent1.putExtra("mobile", mobileNum);
                            //  intent1.putExtra("email",email);
                            startActivity(intent);
                        }
                        break;

                    }


                } else {
                    TastyToast.makeText(getApplicationContext(), "NetworkCall Error", TastyToast.LENGTH_LONG, TastyToast.INFO);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
                AlertDialogClass.ShowMsg(SignupScreen.this,e.getMessage());
                btnSubmit.setEnabled(true);

            }

        }
    }

    private void sendDataRegPage1() {
        btnSubmit.setEnabled(false);

        selectedInitials = spnrInitials.getSelectedItem().toString();
        selectedRef = spnrRef.getSelectedItem().toString();
        refCode = edtRefCode.getText().toString().toUpperCase().trim();
        Logics.setRef(SignupScreen.this, selectedRef);
        Logics.setRefCode(SignupScreen.this, refCode);

        //selectedState = spinnerState.getSelectedItem().toString();

//        ringProgressDialog = ProgressDialog.show(SignupScreen.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));


        AlertDialogClass.PopupWindowShow(SignupScreen.this, mainlayout);

        String imei = Logics.getDeviceID(this);
        String ip = Logics.getSimId(this);
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            imei = Logics.getTokenID(SignupScreen.this);

            ip = "12345";
        }
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("imei", imei);
            jsonObject.put("ip", ip);
            //  jsonObject.put("ip","1234566666");
            // jsonObject.put("pan_no",pan_no);
            jsonObject.put("name", selectedInitials + " " + name);
            jsonObject.put("fname", fName);
            jsonObject.put("mName", mName);
            jsonObject.put("lName", lName);
            jsonObject.put("mobile", number);

//            jsonObject.put("houseNum", houseNum);
//            jsonObject.put("area", area);
//            jsonObject.put("city", city);
//            jsonObject.put("pin", pin);
//            jsonObject.put("state", selectedState);
            jsonObject.put("email", email);

            jsonObject.put("isSignup", 1);
            jsonObject.put("regpegno", 1);

            byte[] data = jsonObject.toString().getBytes();


            Log.d("data", "sendData: " + jsonObject.toString());

            new SendTOServer(SignupScreen.this, SignupScreen.this, Const.MSGSIGNUP, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(SignupScreen.this,e.getMessage());

        }


    }


    private boolean checkAlreadyVerified(String email, String moile) {
        boolean isVerified = false;
        String verifiedMobile = Logics.getMobile_1(SignupScreen.this);
        String verifiedEmail = Logics.getEmail_1(SignupScreen.this);
        if (verifiedEmail == null || verifiedMobile == null) {
            return isVerified;
        } else if (verifiedEmail.equals(email) && verifiedMobile.equals(moile)) {
            int isEmail_v = Logics.getisEmail_V(SignupScreen.this);
            int is_Mobile_v = Logics.getisMobile_V(SignupScreen.this);
            if (isEmail_v == 1 && is_Mobile_v == 1) {
                isVerified = true;
                return isVerified;
            } else {
                return isVerified;
            }
        } else {
            return isVerified;
        }

        // return  isVerified;
    }

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
                    validateRegPage1();
                else
                    Views.SweetAlert_NoDataAvailble(SignupScreen.this,"No Internet");
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
//        super.onBackPressed();
        finishAffinity();
    }
}
