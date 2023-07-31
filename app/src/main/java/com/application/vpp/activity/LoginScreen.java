package com.application.vpp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener, RequestSent, TextWatcher, ConnectionProcess {
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    TextView txt_version;
    static EditText edtPanNo;

    FancyButton btnLogin;
    String strPan;
    LinearLayout linearOtp, linearOtp2;
    boolean isPanVerified = false;
    public static Handler handlerLogin;
    String strOtp, mobileNum, emailOtp, MobileEmailAddedOtp, existingContact, existingEmail, newEmail;
    String verifyOtp = null, verifyOtp1 = null, verifyOtp2 = null, verifyOtp3 = null, verifyOtp4 = null, verifyOtp5 = null, verifyOtp6 = null, verifyOtp7 = null, verifyOtp8 = null;
    boolean isSignup = false;
    String deviceID = Const.simNumber;
    String simNumber = Const.simNumber;
    //    ProgressDialog ringProgressDialog;
    AlertDialog.Builder builder;
    RadioGroup indexationRd;
    RadioButton rdemail;
    RadioButton rdmobile;
    LinearLayout spnrlayout;
    TextView txt_mobile;
    TextView txt_email;
    AlertDialog alertDialog;
    TextView txtResendOTP, txtResendEmailOTP;
    static int countAttempt = 1;
    public int isRegistered = 0;
    public int verifyMobile = 0, updateContact = 0, updateEmail = 0;
    ImageView imgLogoIcon;
    String osversion;
    CoordinatorLayout mainlayout;
    TextView txt_SignUp;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private static final String TAG = "Sample";

    int e = 0;
    int m = 0;
    int MaxTry = 0;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    //    private InAppUpdateManager inAppUpdateManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen1);

        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;

        Profile.handlerProfile = null;
        LoginScreen.handlerLogin = null;

        handlerLogin = new ViewHandler();

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList, "SocketLogs", LoginScreen.this);


        btnLogin = (FancyButton) findViewById(R.id.btn_login);
        edtPanNo = (EditText) findViewById(R.id.edt_pan_no);
        txt_version = findViewById(R.id.txt_version);

        txt_version.setText("v" + Methods.getVersionInfo(LoginScreen.this));

        txt_SignUp = (TextView) findViewById(R.id.txt_SignUp);


        btnLogin.setOnClickListener(this);

        mainlayout = (CoordinatorLayout) findViewById(R.id.mainlayout);

        txt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, SignupScreen.class);
                startActivity(intent);
            }
        });
//
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.btn_login: {
                if (Connectivity.getNetworkState(LoginScreen.this)) {
                    SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");
                    btnLogin.setEnabled(false);
                    validation();
                } else
                    TastyToast.makeText(getApplicationContext(), Const.checkConnection, TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }

            break;

        }
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }


    private void validation() {

        strPan = edtPanNo.getText().toString().toUpperCase().trim();
        SharedPref.savePreferences(LoginScreen.this, "DPAN", strPan);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        if (strPan.matches("")) {
            edtPanNo.setError("Please enter PAN No");
            btnLogin.setEnabled(true);
            edtPanNo.setAnimation(shakeError());

        } else if ((!strPan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}"))) {
            edtPanNo.setError("Invalid PAN No");
            btnLogin.setEnabled(true);
            edtPanNo.setAnimation(shakeError());


        } else {

            JSONObject jsonObject = new JSONObject();
            try {
                Logics.setLoginPan(LoginScreen.this, strPan);
                AlertDialogClass.PopupWindowShow(LoginScreen.this, mainlayout);
                String version = getVersionInfo();
                jsonObject.put("pan_no", strPan);
                jsonObject.put("versionName", version);
                Log.e("version", version);
                byte[] data = jsonObject.toString().getBytes();
                new SendTOServer(this, this, Const.MSGLOGIN, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } catch (JSONException e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
                btnLogin.setEnabled(true);

            }


        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {


        Intent intent = null;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);


    }

    @Override
    public void requestSent(int value) {


    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.e("handleMessageLogin:", (String) msg.obj);

            AlertDialogClass.PopupWindowDismiss();
            int status = 2;
            int id = msg.arg1;
            switch (id) {
//                case Const.MSGAUTHENTICATE:
//                    ringProgressDialog.dismiss();
//                    ringProgressDialog.cancel();
//                    String data1 = (String) msg.obj;
//                    JSONObject jsonObject1 = null;
//
//                    try {
//                        jsonObject1 = new JSONObject(data1);
//                        int status1 = jsonObject1.getInt("status"); //3
//                        if (status1 == 1) {
//                            int updateContact = jsonObject1.getInt("updateContact");
//                            int updateEmail = jsonObject1.getInt("updateEmail");
//                            if (updateContact == 1) {
//                                String mobileOTP = jsonObject1.getString("mobileotp");
//                                String mobile = jsonObject1.getString("mobile");
//                                Intent intent = new Intent(LoginScreen.this, AuthenticateUpdateProfile.class);
//                                // intent.putExtra("isSignup",1);
//                                intent.putExtra("strOtp", mobileOTP);
//                                intent.putExtra("mobileNum", mobile);
//                                intent.putExtra("updateEmail", 0);
//                                intent.putExtra("updateContact", 1);
//                                intent.putExtra("edtEmail", e);
//                                intent.putExtra("edtMobile", m);
//
//                                startActivity(intent);
//                            } else if (updateEmail == 1) {
//                                String emailOtp = jsonObject1.getString("emailotp");
//                                String email = jsonObject1.getString("email");
//                                Intent intent = new Intent(LoginScreen.this, AuthenticateUpdateProfile.class);
//                                // intent.putExtra("isSignup",1);
//                                intent.putExtra("emailOTP", emailOtp);
//                                intent.putExtra("email", email);
//                                intent.putExtra("updateEmail", 1);
//                                intent.putExtra("updateContact", 0);
//                                intent.putExtra("edtEmail", e);
//                                intent.putExtra("edtMobile", m);
//                                startActivity(intent);
//                            }
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        FirebaseCrashlytics.getInstance().recordException(e);
//                    }
//
//                    break;
//                case Const.MSGUpdateEMailMobile: {
//                    try {
//                        ringProgressDialog.dismiss();
////                        jsonObject = null;
//                        JSONObject jsonObject = null;
//                        String data = (String) msg.obj;
//                        Log.e("MSGUpdateEMailMobile: ", data);
//                        jsonObject = new JSONObject(data);
//                        status = jsonObject.getInt("status");
//
//                        if (status == 0) {
//                            TastyToast.makeText(getApplicationContext(), jsonObject.getString("message"), TastyToast.LENGTH_LONG, TastyToast.INFO);
//                            //  Toast.makeText(PanValidation.this, message, Toast.LENGTH_SHORT).show();
//                        } else {
////                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.INFO);
//                            String mobile = jsonObject.getString("mobile");
//                            String email = jsonObject.getString("email");
//                            DlgCnfrtmn(mobile, email);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Log.e("handleMessage:", e.getMessage());
//                        FirebaseCrashlytics.getInstance().recordException(e);
//                    }
//                }
//
//                break;

                case Const.MSGLOGIN: {
                    String data = (String) msg.obj;
                    Log.e("MSGLOGIN", data);
                    btnLogin.setEnabled(true);
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        status = jsonObject.getInt("status");
                        if (status == 0) {
                            int isdeactivated = jsonObject.getInt("isDeactivated");
                            String message = jsonObject.getString("message");

                            Snackbar.make(mainlayout, message, Snackbar.LENGTH_LONG).show();
                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.INFO);

                            Intent intent = new Intent(LoginScreen.this, SignupScreen.class);
                            startActivity(intent);


                        } else if (status == 3) {
                            //int downloadApk  = jsonObject.getInt("DownloadApk");

                            startAlert();
//                               if(downloadApk==1){
//                                startAlert();
//                               }
                            //Toast.makeText(LoginScreen.this, message, Toast.LENGTH_LONG).show();
                        } else {

                            isPanVerified = true;

                            String mobileNum = jsonObject.getString("newContact");
                            String mobileOTP = jsonObject.getString("mobileOTP");
                            String emailOTP = jsonObject.getString("emailOTP");
                            String message = jsonObject.getString("message");
                            String email = jsonObject.getString("email");
                            int reg_page_num = jsonObject.getInt("reg_page_num");
                            int isRegister = jsonObject.getInt("isRegistered");

                            Logics.setmobileNo(LoginScreen.this, mobileNum);  //save mble no ..
                            Logics.setContact(LoginScreen.this, mobileNum);  //save mble no ..

                            //here save mobile and email for resend otp ...

                            SharedPref.savePreferences1(LoginScreen.this,"mobileNo",mobileNum);
                            SharedPref.savePreferences1(LoginScreen.this,"emailId",email);

                            Intent intent = new Intent(LoginScreen.this, OtpLoginVerfication.class);

                            intent.putExtra("isSignup", 1);
                            intent.putExtra("strOtp", mobileOTP);
                            intent.putExtra("mobileNum", mobileNum);
                            intent.putExtra("isRegistered", isRegister);
                            intent.putExtra("emailOTP", emailOTP);
                            intent.putExtra("emailId", email);
                            startActivity(intent);

                        }
//                       else {
//
//                            String message = jsonObject.getString("message");
//                            Views.toast(LoginScreen.this, message);
//
//                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        btnLogin.setEnabled(true);

                        Log.e("handleMessage: ", e.getMessage());

                    }


                }
                break;

            }
        }
    }


    public String deviceId() {
        try {

            String imei = Logics.getDeviceID(LoginScreen.this);
            //String sim_number = Logics.getSimId(SplashScreen.this);

            String simNumber2 = Logics.getSimId1(LoginScreen.this);
            String sim_number1 = Logics.getSimId(LoginScreen.this);

            String osversion = "normal";
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                imei = Logics.getTokenID(LoginScreen.this);
                osversion = "oxy";
                sim_number1 = "12345";
            }


            if (imei == null) {

                imei = Const.simNumber;

            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("imei", imei);
            jsonObject.put("sim_number1", sim_number1);
            jsonObject.put("sim_number2", simNumber2);


            byte[] data = jsonObject.toString().getBytes();

            // new SendTOServer(LoginScreen.this,LoginScreen.this, Const.MSGVERIFYIMEILOGIN,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        return deviceID;
    }

    public boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;

    }

    private void askPersmission() {

        String[] _permission = {Manifest.permission.READ_PHONE_STATE};
        if (!checkPermission(_permission)) {
            checkPermission(_permission, tag -> {
                deviceID = deviceId();
            });
        } else {
            deviceID = deviceId();
        }
    }


    private String _permissionTag = "";
    private SplashScreen.OnPermissionAccepted _onpermission;
    public final int REQUEST_CODE_PERMISSION = 10000;

    public void checkPermission(String[] permissions, SplashScreen.OnPermissionAccepted _onpermission) {
        checkPermission(permissions, "", _onpermission);
    }

    public void checkPermission(String[] permissions, String _permissionTag, SplashScreen.OnPermissionAccepted _onpermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkPermission(permissions)) {
            this._permissionTag = _permissionTag;
            this._onpermission = _onpermission;
            requestPermission(permissions);
        } else {
            _onpermission.OnPermission(_permissionTag);
        }
    }

    public boolean checkPermission(String[] permissions) {
        boolean permissionGranted = true;
        for (String _permissionStr : permissions) {
            int _permissionInt = ContextCompat.checkSelfPermission(this, _permissionStr);
            if (_permissionInt != PackageManager.PERMISSION_GRANTED) {
                permissionGranted = false;
                break;
            }
        }
        return permissionGranted;
    }

    private void requestPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0) {
                    boolean _isAllPermissionGranted = checkPermission(permissions);
                    if (_isAllPermissionGranted) {
                        if (_onpermission != null) _onpermission.OnPermission(_permissionTag);
                    } else {
                        for (String _permissionStr : permissions) {
                            if (shouldShowRequestPermissionRationale(_permissionStr)) {
                                new android.app.AlertDialog.Builder(this)
                                        .setTitle("Permission Denied!")
                                        .setMessage("You need to allow all the permissions to access Features")
                                        .setPositiveButton("OK", (dialog, which) -> requestPermission(permissions))
                                        .setNegativeButton("Cancel", null)
                                        .create()
                                        .show();
                                break;
                            }
                        }
                    }
                    break;
                }
        }
    }

    private String getVersionInfo() {
        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        Log.d("versionName", "getVersionInfo: " + versionName);
        return versionName;

    }

    private void startAlert() {
        android.app.AlertDialog.Builder alertBuild = new android.app.AlertDialog.Builder(this)
                .setTitle("VPP App Update")
                .setMessage("VPP App recommends that you update to the latest version.")
                .setCancelable(true)
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final String appPackageName = getPackageName();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));

                    }
                });
        android.app.AlertDialog alert = alertBuild.create();
        alert.show();
    }

    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(LoginScreen.this),
                            "1",
                            Methods.getVersionInfo(LoginScreen.this),
                            Methods.getlogsDateTime(), "LoginScreen",
                            Connectivity.getNetworkState(getApplicationContext()),
                            LoginScreen.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        AlertDialogClass.PopupWindowDismiss();
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                btnLogin.setEnabled(true);
                validation();
                //TastyToast.makeText(LoginScreen.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();
        ProgressDlgConnectSocket(LoginScreen.this, connectionProcess, "Server Not Available");
//        ConnectToserver(connectionProcess);
    }

    @Override
    public void IntenrnetNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();

        Views.SweetAlert_NoDataAvailble(LoginScreen.this, "Internet Not Available");
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
                    new ConnectTOServer(LoginScreen.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(LoginScreen.this, connectionProcess, "Server Not Available");
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
                    ProgressDlgConnectSocket(LoginScreen.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(LoginScreen.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == REQ_CODE_VERSION_UPDATE) {
//            if (resultCode == Activity.RESULT_CANCELED) {
//                // If the update is cancelled by the user,
//                // you can request to start the update again.
//                inAppUpdateManager.checkForAppUpdate();
//
//                Log.d(TAG, "Update flow failed! Result code: " + resultCode);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onBackPressed() {
        if (SharedPref.getPreferences(getApplicationContext(), SharedPref.LoggedIN).equalsIgnoreCase("")) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }


//    public void DlgCnfrtmn(String mobilestr, String emailstr) {
//        builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.verify_update_loginpan, null);
//        builder.setView(dialogView);
//        indexationRd = dialogView.findViewById(R.id.indexationRd);
//        indexationRd.setOnCheckedChangeListener(checkChange);
//        RadioGroup radioGroup = dialogView.findViewById(R.id.myRadioGroup);
//        rdemail = dialogView.findViewById(R.id.email);
//        rdmobile = dialogView.findViewById(R.id.mobile);
//        TextView textViewChange = dialogView.findViewById(R.id.textViewChange);
//        TextView textviewContinue = dialogView.findViewById(R.id.textviewContinue);
//        spnrlayout = dialogView.findViewById(R.id.spnrlayout);
//        txt_mobile = dialogView.findViewById(R.id.txt_mobile);
//        txt_email = dialogView.findViewById(R.id.txt_email);
//        int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();
//        rdemail.setText(emailstr);
//        rdmobile.setText(mobilestr);
//
//        Logics.setEmail(getApplicationContext(), emailstr);
//        Logics.setContact(getApplicationContext(), mobilestr);
//
//
//        txt_email.setText("Email : " + emailstr);
//        txt_mobile.setText("Mobile : " + mobilestr);
//
//        textViewChange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                spnrlayout.setVisibility(View.VISIBLE);
//            }
//        });
//
////        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(RadioGroup radioGroup, int i) {
////                if (i == R.id.email) {
////                    SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, Const.FromPanLogin);
////                    e = 1;
////                    m = 0;
////                    sendData(1, 0);
////                } else if (i == R.id.mobile) {
////                    m = 1;
////                    e = 0;
////                    SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, Const.FromPanLogin);
////                    sendData(0, 1);
////                }
////            }
////        });
//
//        builder.setCancelable(true);
//        alertDialog = builder.show();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//    }

    private RadioGroup.OnCheckedChangeListener checkChange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
            switch (checkId) {
                case R.id.RbtnContinue:
                    ///continuee validate

                    validation();
                    spnrlayout.setVisibility(View.GONE);
                    txt_email.setVisibility(View.VISIBLE);
                    txt_mobile.setVisibility(View.VISIBLE);
                    break;
                case R.id.RbtnChange:
                    spnrlayout.setVisibility(View.VISIBLE);  // radio button visible ///

                    txt_email.setVisibility(View.GONE);
                    txt_mobile.setVisibility(View.GONE);

                    break;
                default:
                    break;
            }
        }
    };


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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(LoginScreen.this),
                                    "0",
                                    Methods.getVersionInfo(LoginScreen.this),
                                    Methods.getlogsDateTime(), "LoginScreen",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    LoginScreen.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!LoginScreen.this.isFinishing()) {
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
                new ConnectTOServer(LoginScreen.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                connectionProcess.ConnectToserver(connectionProcess);
            }
            Log.e("DlgConnectSocket11111", "called");

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
