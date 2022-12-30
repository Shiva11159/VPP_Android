package com.application.vpp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class UpdateProfile extends NavigationDrawer implements TextWatcher, RequestSent, ConnectionProcess {

    EditText edtProfileContact, edtProfileEmail;
    TextView txtProfileVPPId, imgerror;
    Button btnSaveProfile;
    String newContact, newEmail, mobileOTP, contact, emailOTP, email;
    public int updateEmail = 0, updateContact = 0;
    CoordinatorLayout mainlayout;
    int isSignup;
//    ProgressDialog ringProgressDialog;
    public static Handler updateProfileHandler;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;

    String existingContact, existingEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_update_profile, mDrawerLayout);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;

        txtProfileVPPId = (TextView) findViewById(R.id.txtProfileVPPId);
        imgerror = (TextView) findViewById(R.id.imgerror);

        edtProfileContact = (EditText) findViewById(R.id.txtProfileContact);
        edtProfileEmail = (EditText) findViewById(R.id.txtProfileEmail);
        btnSaveProfile = (Button) findViewById(R.id.btnSaveProfile);

        String vppId = Logics.getVppId(this);
        existingContact = Logics.getContact(this);
        existingEmail = Logics.getEmail(this);


        txtProfileVPPId.setText(vppId);
        edtProfileContact.setText(existingContact);
        edtProfileEmail.setText(existingEmail);

        updateProfileHandler = new ViewHandler();

        mainlayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.getNetworkState(getApplicationContext())){
                    validation();
                }else {
                    Views.SweetAlert_NoDataAvailble(UpdateProfile.this,"Connect internet !");
                }
            }
        });


        edtProfileContact.addTextChangedListener(this);
        edtProfileEmail.addTextChangedListener(this);


        //  edtProfileContact.setOnClickListener(this);
        //  edtProfileEmail.setOnClickListener(this);

    }

    private void validation() {

        imgerror.setVisibility(View.GONE);

        newContact = edtProfileContact.getText().toString().toUpperCase().trim();
        newEmail = edtProfileEmail.getText().toString().toUpperCase().trim();

        if (!isValidEmail(newEmail)) {

            edtProfileEmail.setError("Enter Valid Email");
            imgerror.setVisibility(View.VISIBLE);

        } else if (newContact.length() != 10) {

            edtProfileContact.setError("Enter Valid  Number");
            imgerror.setVisibility(View.VISIBLE);


        } else {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

            sendData();

        }
    }

    private void sendData() {

//        ringProgressDialog = ProgressDialog.show(UpdateProfile.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        AlertDialogClass.PopupWindowShow(UpdateProfile.this,mainlayout);
        String imei = Logics.getDeviceID(this);
        String simId = Logics.getSimId(this);

        JSONObject jsonObject = new JSONObject();
        isSignup = 3;


        //18-10-19 check for change in profile (Contact, email)
        if (existingContact.equals(newContact) && existingEmail.equals(newEmail)) {

            AlertDialogClass.PopupWindowDismiss();
            //ringProgressDialog.dismiss();
            Views.toast(this, "There is a no change in a profile. ");
        } else {
            if (!existingContact.equals(newContact) && !existingEmail.equals(newEmail)) {

                updateContact = 1;
                updateEmail = 1;
            } else {
                //if not equal then 1
                if (!existingContact.equals(newContact)) {
                    updateContact = 1;
                } else if (!existingEmail.equals(newEmail)) {
                    updateEmail = 1;
                }

            }

            try {
                jsonObject.put("imei", imei);
                jsonObject.put("sim", simId);
                jsonObject.put("pan", Logics.getPanNo(UpdateProfile.this));
                jsonObject.put("vppid", Logics.getVppId(UpdateProfile.this));
                jsonObject.put("otp", 1);
                jsonObject.put("mobile", newContact);
                jsonObject.put("updateContact", updateContact);
                jsonObject.put("newEmail", newEmail);
                jsonObject.put("updateEmail", updateEmail);

                jsonObject.put("isSignup", isSignup);

                byte[] data = jsonObject.toString().getBytes();

                Log.d("data", "sendData: " + jsonObject.toString());

                new SendTOServer(this, this, Const.MSGUPDATEPROFILE, data,connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                //verify otp on new mobile number
                Views.toast(UpdateProfile.this, "OTP Sent.... kindly wait for some moment ");

            } catch (JSONException e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        Log.d("change", "afterTextChanged: ");

        imgerror.setVisibility(View.GONE);
        newContact = edtProfileContact.getText().toString().toUpperCase().trim();

        if (editable.hashCode() == edtProfileContact.getText().hashCode()) {

            if (newContact.length() != 10) {

                edtProfileContact.setError("Enter Valid Number");
                imgerror.setVisibility(View.VISIBLE);
            }
        }

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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

            String data = (String) msg.obj;
            Log.e( "UpdateProfileRESPONSE " , data);
            JSONObject jsonObject = null;

            Log.e("newEmail", newEmail);
            Log.e("newMobile", newContact);
            try {
                jsonObject = new JSONObject(data);
                int status = jsonObject.getInt("status"); //3

                if (status != 0) {

                     //String message = jsonObject.getString("message");
                    int updateContact = jsonObject.getInt("updateContact");
                    int updateEmail = jsonObject.getInt("updateEmail");


                    if (updateContact == 1 && updateEmail == 1) {

// data:{"emailOTP":1728,"verifyMobile":3,"newContact":"9665538502","isRegistered":1,"newEmail":"chavanapurva56@gmail.com","message":"OTP is send on your registered email id.","status":3,"mobileOTP":6491} msgCode: 31
                        mobileOTP = jsonObject.getString("mobileotp");
                        emailOTP = jsonObject.getString("emailOTP");

                        Logics.setContact(UpdateProfile.this, newContact);
                        Logics.setEmail(UpdateProfile.this, newEmail);

                    } else {
                        if (updateContact == 1) {

// data:{"verifyMobile":3,"newContact":"7900149988","message":"OTP is send on your new mobile number.","status":3,"mobileOTP":1892} msgCode: 31
// verify contact
                            mobileOTP = jsonObject.getString("mobileotp");
                            newContact = jsonObject.getString("mobile");

                            Intent intent = new Intent(UpdateProfile.this, UpdateOtpVerify.class);
                            // intent.putExtra("isSignup",1);

                            intent.putExtra("strOtp", mobileOTP);
                            intent.putExtra("mobileNum", newContact);
                            intent.putExtra("updateEmail", 0);
                            intent.putExtra("updateContact", 1);

                            startActivity(intent);
                            //Logics.setContact(UpdateProfile.this,newContact);
                        }
                        if (updateEmail == 1) {

                            //data:{"emailOTP":8037,"verifyMobile":3,"isRegistered":0,"newEmail":"sandeshlande1995@gmail.com","message":"OTP is send on your registered email id.","status":3} msgCode: 31
                            //verify email
                            //otp = jsonObject.getString("emailOTP");
                            Logics.setEmail(UpdateProfile.this, newEmail);
                        }
                    }

//                    Intent  intent = new Intent(UpdateProfile.this, LoginScreen.class);
//                    intent.putExtra("otp", otp);
//                    intent.putExtra("emailOTP", emailOTP);
//                    intent.putExtra("verifyMobile", verifyMobile);
//                    intent.putExtra("updateContact", updateContact);
//                    intent.putExtra("updateEmail", updateEmail);
//
//                    intent.putExtra("mobileNum", newContact);
//                    intent.putExtra("existingContact", existingContact);
//                    intent.putExtra("newEmail", newEmail);
//                    intent.putExtra("existingEmail", existingEmail);

                    //startActivity(intent);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }
    }

    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {




                AlertDialogClass.PopupWindowShow(UpdateProfile.this,mainlayout);
//                ringProgressDialog = ProgressDialog.show(UpdateProfile.this, "Please wait ...", "Loading Your Data ...", true);
//                ringProgressDialog.setCancelable(true);
//                ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
                validation();
                //TastyToast.makeText(UpdateProfile.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
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
                    Views.ProgressDlgConnectSocket(UpdateProfile.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(UpdateProfile.this, "null", Toast.LENGTH_SHORT).show();
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

        Views.SweetAlert_NoDataAvailble(UpdateProfile.this, "Internet Not Available");
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
                    new ConnectTOServer(UpdateProfile.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Views.ProgressDlgConnectSocket(UpdateProfile.this, connectionProcess, "Server Not Available");
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
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(UpdateProfile.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(UpdateProfile.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}