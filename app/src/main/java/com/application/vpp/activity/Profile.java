package com.application.vpp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Database.DatabaseHelper;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.ImagePickerActivity;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class Profile extends AppCompatActivity implements RequestSent, ConnectionProcess {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    TextView txtName, txtMobile, txtVppId, txtCity, txtProfilePan, txtProfileEmail, txt_update_doc, txt_update_payment,txt_update_esign;
    ImageView edt_mob, edt_email;
    FancyButton btnUpdateProfile;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    public static Handler handlerProfile;
    //    ProgressDialog ringProgressDialog;
    int e = 0;
    int m = 0;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    byte[] data;
    CircularImageView uplaodimage;
    DatabaseHelper db;
    TextView tvbankRemark, tvbankStatus, tvadharRemark, tvadharStatus, tvpanStatus, tvpanRemark, tvdocumentStatus, tvpaymentStatus;
    ImageView imgbankstatus, imgadharstatus, imgpanstatus, imgdocstatus, imgpaymentstatus;
    ScrollView nestedScrollView;
    String is_bank_verified = "0";
    String is_adhar_verified = "0";
    String is_pan_verified = "0";

    String is_selfie_verified = "0";
  //  String is_video_verified = "0";
    String is_esign_verified = "0";
    String bank_remark, adhar_remark, pan_remark;

    TextView paynowbutton, uploaddocbutton ,uploadesignbutton;
    LinearLayout linearPaynDoc,mainlayout;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    int MaxTry=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);
        try {
            db = new DatabaseHelper(this);
            connectionProcess = (ConnectionProcess) this;
            requestSent = (RequestSent) this;
            ImagePickerActivity.clearCache(this);
            mainlayout = findViewById(R.id.coordinatorlayout);
            nestedScrollView = findViewById(R.id.NestedScrollView);
            linearPaynDoc = findViewById(R.id.linearPaynDoc);
            paynowbutton = findViewById(R.id.paynowbutton);
            uplaodimage = findViewById(R.id.uplaodimage);
            tvbankRemark = findViewById(R.id.tvbankRemark);
            tvbankStatus = findViewById(R.id.tvbankStatus);
            tvadharRemark = findViewById(R.id.tvadharRemark);
            tvadharStatus = findViewById(R.id.tvadharStatus);
            tvpanRemark = findViewById(R.id.tvpanRemark);
            tvpanStatus = findViewById(R.id.tvpanStatus);
            tvdocumentStatus = findViewById(R.id.tvdocumentStatus);
            tvpaymentStatus = findViewById(R.id.tvpaymentStatus);
            imgbankstatus = findViewById(R.id.imgbankstatus);
            imgadharstatus = findViewById(R.id.imgadharstatus);
            imgpanstatus = findViewById(R.id.imgpanstatus);
            imgpaymentstatus = findViewById(R.id.imgpaymentstatus);
            imgdocstatus = findViewById(R.id.imgdocstatus);
            uploaddocbutton = findViewById(R.id.uploaddocbutton);
            uploadesignbutton = findViewById(R.id.uploadesignbutton);

            inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList,"SocketLogs", Profile.this);


//        lineardocumentClick = findViewById(R.id.lineardocumentClick);

//        lineardocumentClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (documentClick == false) {
//                    documentClick = true;
//                    Views.createRotateAnimator(ImageButtonDocument, 180f, 0f).start();
//                    linearmainView.setVisibility(View.VISIBLE);
//                    nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
//                } else {
//                    Views.createRotateAnimator(ImageButtonDocument, 0f, 180f).start();
//                    documentClick = false;
//                    linearmainView.setVisibility(View.GONE);
//                    nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
//
//                }
//            }
//        });

            if (db.getimage() == null) {
                // Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            } else {
                uplaodimage.setImageBitmap(db.getimage());
            }
            txtName = findViewById(R.id.txtProfileName);
            txtVppId = findViewById(R.id.txtProfileVPPId);
            txtMobile = findViewById(R.id.txtProfileContact);
            txtCity = findViewById(R.id.txtProfileCity);
            txtProfilePan = findViewById(R.id.txtProfilePan);
            txtProfileEmail = findViewById(R.id.txtProfileEmail);
//        btnUpdateProfile = (FancyButton) findViewById(R.id.btn_updateprofile);
            txt_update_doc = (TextView) findViewById(R.id.txt_update_doc);
            txt_update_payment = (TextView) findViewById(R.id.txt_update_payment);
            txt_update_esign = (TextView) findViewById(R.id.txt_update_esign);
            edt_mob = (ImageView) findViewById(R.id.img_edt_mob);
            edt_email = (ImageView) findViewById(R.id.img_edt_email);
            ArrayList<String> profileList = Logics.getProfile(this);

            SplashScreen.handlerSplash = null;
            Dashboard.handlerDashboard = null;
            Profile.handlerProfile = null;
            DiscripancyActivity.handlerDiscripancy = null;

            OtpVerfication.otpVerfhandler = null;
            LoginScreen.handlerLogin=null;

            handlerProfile = new ViewHandler();
            txtVppId.setText(profileList.get(0));
            txtName.setText(profileList.get(1));
            txtCity.setText(profileList.get(2));
            txtMobile.setText(profileList.get(3));
            txtProfilePan.setText(profileList.get(4));
            txtProfileEmail.setText(profileList.get(5));

            paynowbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialogCreate("", Const.payment, "Payment status");

                }
            });
            uploaddocbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                AlertDialogCreate("", Const.doc, "Documents status");
                    Upload("", Const.doc, "Documents status");
                }
            });

            uploadesignbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                AlertDialogCreate("", Const.doc, "Documents status");

                    Upload("", "Esign", "Esign status");
                }
            });

            uplaodimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    {
                        Dexter.withActivity(Profile.this)
                                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        PopUpChooseCamORgallery(uplaodimage);
                                        if (report.isAnyPermissionPermanentlyDenied()) {
                                            //  showSettingsDialog();
                                        }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).check();
                    }
                }
            });

            edt_mob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Connectivity.getNetworkState(getApplicationContext())) {
                        m = 1;
                        alert();
                    } else {
                        Views.SweetAlert_NoDataAvailble(Profile.this, "Connect internet !");
                    }
                }
            });
            edt_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Connectivity.getNetworkState(getApplicationContext())) {
                        e = 1;
                        alert();
                    } else {
                        Views.SweetAlert_NoDataAvailble(Profile.this, "Connect internet !");
                    }
                }
            });

        }catch (Exception e){
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(Profile.this,e.getMessage());
        }

       /* String isPayment = Logics.getPaymentStatus(Profile.this);
        String isDocument = Logics.getDocStatus(Profile.this);

        if (isDocument != null) {
            if (isDocument.equals("0")) {
                txt_update_doc.setTextColor(getResources().getColor(R.color.blue));
                txt_update_doc.setText("Click here to Complete Your Document");
                txt_update_doc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Profile.this, UploadDocScreen.class);
                        intent.putExtra("isDocument", 1);
                        startActivity(intent);
                    }
                });
            }  if (isDocument.equals("Pending")) {
                txt_update_doc.setTextColor(getResources().getColor(R.color.blue));
                txt_update_doc.setText("Click here to Complete Your Document");
                txt_update_doc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Profile.this, UploadDocScreen.class);
                        intent.putExtra("isDocument", 1);
                        startActivity(intent);
                    }
                });
            }else {

                if (isDocument.equals("1")) {
                    txt_update_doc.setTextColor(getResources().getColor(R.color.green));
                    txt_update_doc.setText("Completed");
                }
                if (isDocument.equals("Completed")) {
                    txt_update_doc.setTextColor(getResources().getColor(R.color.green));
                    txt_update_doc.setText("Completed");
                }
            }
        }else{
            txt_update_doc.setTextColor(getResources().getColor(R.color.blue));
            txt_update_doc.setText("Click here to Complete Your Document");
            txt_update_doc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Profile.this, UploadDocScreen.class);
                    intent.putExtra("isDocument", 1);
                    startActivity(intent);
                }
            });
        }

        if (isPayment != null) {
            if (isPayment.equals("0")) {
                txt_update_payment.setTextColor(getResources().getColor(R.color.blue));
                txt_update_payment.setText("Click here to Complete Your Payment");
                txt_update_payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Profile.this, UpiPayment.class);
                        intent.putExtra("isDocument", 1);
                        startActivity(intent);
                    }
                });
            }if (isPayment.equals("Pending")) {
                txt_update_payment.setTextColor(getResources().getColor(R.color.blue));
                txt_update_payment.setText("Click here to Complete Your Payment");
                txt_update_payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Profile.this, UpiPayment.class);
                        intent.putExtra("isDocument", 1);
                        startActivity(intent);
                    }
                });
            } else {

                if (isPayment.equals("1")) {
                    txt_update_payment.setTextColor(getResources().getColor(R.color.green));
                    txt_update_payment.setText("Completed");
                }
                if (isPayment.equals("Completed")) {
                    txt_update_payment.setTextColor(getResources().getColor(R.color.green));
                    txt_update_payment.setText("Completed");
                }
            }
        }else{
            txt_update_payment.setTextColor(getResources().getColor(R.color.blue));
            txt_update_payment.setText("Click here to Complete Your Payment");
            txt_update_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Profile.this, UpiPayment.class);
                    intent.putExtra("isDocument", 1);
                    startActivity(intent);
                }
            });
        }*/

        //is_bank_verified
//        String isBankStatus = SharedPref.getPreferences(getApplicationContext(), SharedPref.isBankStatus);
//        String isAdharStatus = SharedPref.getPreferences(getApplicationContext(), SharedPref.isAdharStatus);
//        String isPanStatus = SharedPref.getPreferences(getApplicationContext(), SharedPref.isPanStatus);
//        String is_payment_p = SharedPref.getPreferences(getApplicationContext(), SharedPref.is_payment_p);


    }

    private void alert() {
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.
                verify_update_profile, null);
        builder.setView(dialogView);

        RadioGroup radioGroup = dialogView.findViewById(R.id.myRadioGroup);
        RadioButton email = dialogView.findViewById(R.id.email);
        RadioButton mobile = dialogView.findViewById(R.id.mobile);
        int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.email) {
                    SharedPref.savePreferences(getApplicationContext(),Const.FromUpdate,Const.FromProfile);
                    sendData(1, 0);
                } else if (i == R.id.mobile) {
                    SharedPref.savePreferences(getApplicationContext(),Const.FromUpdate,Const.FromProfile);
                    sendData(0, 1);
                }
            }
        });

        builder.setCancelable(true);
        alertDialog = builder.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void sendData(int Email, int Mobile) {
        try {
//            ringProgressDialog = ProgressDialog.show(Profile.this, "Please wait ...", "Loading Your Data ...", true);
//            ringProgressDialog.setCancelable(true);

            AlertDialogClass.PopupWindowShow(Profile.this,mainlayout);

//            ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
            AlertDialogClass.PopupWindowShow(Profile.this,mainlayout);
            String EmailId = Logics.getEmail(Profile.this);
            String mobile = Logics.getContact(Profile.this);
            JSONObject json = new JSONObject();
            json.put("email", EmailId);
            json.put("mobile", mobile);
            json.put("isemail", Email);
            json.put("isMobile", Mobile);
            data = json.toString().getBytes();
            new SendTOServer(this, this, Const.MSGAUTHENTICATE, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(Profile.this,e.getMessage());
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
            String data111 = (String) msg.obj;


            switch (msg.arg1) {
                case Const.MSGFETCHDOCSTAT:
                    String data = (String) msg.obj;
                    // String data = "[{\"is_document_v\":1,\"is_payment_p\":0,\"is_email_v\":1,\"days\":-31},{}]";

                    Log.e("doc_response", data);
//                    ringProgressDialog.dismiss();
//                    ringProgressDialog.cancel();
                    AlertDialogClass.PopupWindowDismiss();
                    linearPaynDoc.setVisibility(View.VISIBLE);
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONObject jsonObject1 = jsonArray.getJSONObject(1);

                        // 1 means complete , o means incomplete.. ...
                        String is_document_v = jsonObject.getString("is_document_v");
                        String is_payment_p = jsonObject.getString("is_payment_p");
                        String is_email_v = jsonObject.getString("is_email_v");
                        //is_payment_p  o pending 1 done


                        if (is_payment_p.equalsIgnoreCase("1")) {
//                            txt_update_payment.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.right));
                            txt_update_payment.setText("Payment completed");
                            SharedPref.savePreferences(getApplicationContext(), SharedPref.UPIPayment, SharedPref.UPIPaymentDONE);
                            paynowbutton.setVisibility(View.GONE);
//                            txt_update_payment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_, 0);

                        } else {
//                            txt_update_payment.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.wrong));
                            txt_update_payment.setText("Payment incompleted");
                            txt_update_payment.setTextColor(getResources().getColor(R.color.red));
                            paynowbutton.setVisibility(View.VISIBLE);
                            SharedPref.savePreferences(getApplicationContext(), SharedPref.UPIPayment, "");

//                            txt_update_payment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cancel_, 0);

//                            tvpaymentStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
                        }

//                        Log.e("11", String.valueOf(jsonObject1.length()));
//                        Log.e("22", String.valueOf(jsonObject1.toString()));


//                        0 Document not Uploaded
//                            1 Verified
//                            2 rejected
//                            3 Verification Pending
                        if (jsonObject1.length() == 0) {
                            txt_update_doc.setText("Upload pending");
                            txt_update_doc.setTextColor(getResources().getColor(R.color.red));
                            uploaddocbutton.setVisibility(View.VISIBLE);
                        }

                        if (jsonObject1.length() != 0) {
                            ///second object ....validation   0 means not uploaded,  1 means verified ,2 means rejected, 3 means verification pending,
                            is_bank_verified = jsonObject1.getString("is_bank_verified");
                            is_adhar_verified = jsonObject1.getString("is_adhar_verified");
                            is_pan_verified = jsonObject1.getString("is_pan_verified");

                            is_selfie_verified = jsonObject1.getString("is_selfie_verified");
                            //is_video_verified = jsonObject1.getString("is_video_verified");
                            is_esign_verified = jsonObject1.getString("is_esign_verified");


                            if (is_adhar_verified.equalsIgnoreCase("3") && (is_bank_verified.equalsIgnoreCase("3")) && (is_pan_verified.equalsIgnoreCase("3"))) {
                                txt_update_doc.setText("Verification pending");
                                txt_update_doc.setTextColor(getResources().getColor(R.color.red));
                                txt_update_doc.setTextColor(getResources().getColor(R.color.red));
                                uploaddocbutton.setVisibility(View.GONE);
//                                txt_update_doc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cancel_, 0);
                            }

                            if ((!is_adhar_verified.equalsIgnoreCase("2")) || (!is_pan_verified.equalsIgnoreCase("2")) || (!is_bank_verified.equalsIgnoreCase("2"))) {
                                if (is_adhar_verified.equalsIgnoreCase("3") || (is_bank_verified.equalsIgnoreCase("3")) || (is_pan_verified.equalsIgnoreCase("3"))) {
                                    txt_update_doc.setText("Verification pending");
                                    txt_update_doc.setTextColor(getResources().getColor(R.color.red));
                                    txt_update_doc.setTextColor(getResources().getColor(R.color.red));
                                    uploaddocbutton.setVisibility(View.GONE);
//                                txt_update_doc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cancel_, 0);
                                } else if (is_adhar_verified.equalsIgnoreCase("2") || (is_bank_verified.equalsIgnoreCase("2")) || (is_pan_verified.equalsIgnoreCase("2"))) {
                                    txt_update_doc.setText("Discrepancy raised");
                                    txt_update_doc.setTextColor(getResources().getColor(R.color.red));
                                    uploaddocbutton.setVisibility(View.VISIBLE);
//                                txt_update_doc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cancel_, 0);
                                }
                            } else {
                                txt_update_doc.setText("Discrepancy raised");
                                txt_update_doc.setTextColor(getResources().getColor(R.color.red));
                                uploaddocbutton.setVisibility(View.VISIBLE);
                            }


                            if (is_adhar_verified.equalsIgnoreCase("2") || (is_bank_verified.equalsIgnoreCase("2")) || (is_pan_verified.equalsIgnoreCase("2"))) {
                                txt_update_doc.setText("Discrepancy raised");
                                txt_update_doc.setTextColor(getResources().getColor(R.color.red));
                                uploaddocbutton.setVisibility(View.VISIBLE);
//                                txt_update_doc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cancel_, 0);
                            }

                            if (is_document_v.equalsIgnoreCase("1")) {
                                txt_update_doc.setText("Verified");
                                uploaddocbutton.setVisibility(View.GONE);
                                txt_update_doc.setTextColor(getResources().getColor(R.color.black));
//                                txt_update_doc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_, 0);
                            }

                            if (is_adhar_verified.equalsIgnoreCase("1") && (is_bank_verified.equalsIgnoreCase("1")) && (is_pan_verified.equalsIgnoreCase("1"))) {
                                txt_update_doc.setText("Verified");
                                txt_update_doc.setTextColor(getResources().getColor(R.color.black));
                                uploaddocbutton.setVisibility(View.GONE);
//                                txt_update_doc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_, 0);
                            }

                            if (is_selfie_verified.equalsIgnoreCase("1") /*&& (is_video_verified.equalsIgnoreCase("1"))*/ && (is_esign_verified.equalsIgnoreCase("1"))) {
                                txt_update_esign.setText("Verified");
                                txt_update_esign.setTextColor(getResources().getColor(R.color.black));
                                uploadesignbutton.setVisibility(View.GONE);
//                                txt_update_doc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_, 0);
                            }

                        }

                         if (is_document_v.equalsIgnoreCase("1")) {
                            txt_update_doc.setText("Verified");
                            uploaddocbutton.setVisibility(View.GONE);
                            txt_update_doc.setTextColor(getResources().getColor(R.color.black));
//                                txt_update_doc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_, 0);
                        }

                        if (is_adhar_verified.equalsIgnoreCase("0") || (is_bank_verified.equalsIgnoreCase("0")) || (is_pan_verified.equalsIgnoreCase("0"))) {
                            txt_update_doc.setText("Upload pending");
                            txt_update_doc.setTextColor(getResources().getColor(R.color.red));
                            uploaddocbutton.setVisibility(View.VISIBLE);
//                                txt_update_doc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cancel_, 0);
                        }
// commented by pravin.... 09.11.2020 . due to old user verified from admin panel


                        //esign --------------------------------check



                        if (is_selfie_verified.equalsIgnoreCase("0") || /*(is_video_verified.equalsIgnoreCase("0")) ||*/ (is_esign_verified.equalsIgnoreCase("0"))) {
                            txt_update_esign.setText("Upload pending");
                            txt_update_esign.setTextColor(getResources().getColor(R.color.red));
                            uploadesignbutton.setVisibility(View.VISIBLE);

                        }


                        if (is_selfie_verified.equalsIgnoreCase("3") &&
                                /*(is_video_verified.equalsIgnoreCase("3")) &&*/
                                (is_esign_verified.equalsIgnoreCase("1"))) {
                            txt_update_esign.setText("Verification pending");
                            txt_update_esign.setTextColor(getResources().getColor(R.color.red));
                            uploadesignbutton.setVisibility(View.GONE);

                        }

                        //is_document_v
                       /* if (is_document_v.equalsIgnoreCase("1")) {
                            imgdocstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.right));
                            tvdocumentStatus.setText("Upload completed");
//                            tvdocumentStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                            arrow1.setVisibility(View.GONE);
                            cardview1.setClickable(false);

                        } else {
                            imgdocstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.wrong));
                            tvdocumentStatus.setText("Upload Incompleted");
//                            tvdocumentStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
                            arrow1.setVisibility(View.VISIBLE);
                        }
*/

// [{"is_document_v":0,"is_payment_p":0,"is_email_v":1,"days":30},{"is_bank_verified":3,"selfie_remark":"","is_adhar_verified":2,"is_video_verified":3,"video_remark":"","is_esign_verified":1,"is_selfie_verified":3,"is_pan_verified":3}]
                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
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
                                Intent intent = new Intent(Profile.this, AuthenticateUpdateProfile.class);
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
                                Intent intent = new Intent(Profile.this, AuthenticateUpdateProfile.class);
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

            }


        }
    }

    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        AlertDialogClass.PopupWindowDismiss();

        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(Profile.this),
                            "1",
                            Methods.getVersionInfo(Profile.this),
                            Methods.getlogsDateTime(), "Profile",
                            Connectivity.getNetworkState(getApplicationContext()),
                            Profile.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }



        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ringProgressDialog = ProgressDialog.show(Profile.this, "Please wait ...", "Loading Your Data ...", true);
//                ringProgressDialog.setCancelable(true);
//                ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

                AlertDialogClass.PopupWindowShow(Profile.this,mainlayout);
//                new SendTOServer(Profile.this, requestSent, Const.MSGFETCHDOCSTAT, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                MSGFETCHDOCSTATmethod();
//                TastyToast.makeText(Profile.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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
                    ProgressDlgConnectSocket(Profile.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(Profile.this, "null", Toast.LENGTH_SHORT).show();
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

        Views.SweetAlert_NoDataAvailble(Profile.this, "Internet Not Available");
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
                    new ConnectTOServer(Profile.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(Profile.this, connectionProcess, "Server Not Available");
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
                   ProgressDlgConnectSocket(Profile.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(Profile.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void PopUpChooseCamORgallery(ImageView imageView) {
        PopupMenu popup = new PopupMenu(Profile.this, imageView);
        try {
            // Reflection apis to enforce show icon
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(POPUP_CONSTANT)) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(POPUP_FORCE_SHOW_ICON, boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(Profile.this,e.getMessage());
        }
        popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
//        popup.setOnMenuItemClickListener(this);
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cameraclick:
                        cameraIntent();
                        break;

                    case R.id.galleryclick:
                        galleryIntent();
                        break;
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }

    private void galleryIntent() {
        Intent intent = new Intent(Profile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);

        startActivityForResult(intent, SELECT_FILE);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = new Intent(Profile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
//        Bitmap bm = null;
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        Uri uri = data.getParcelableExtra("path");
        try {
            // You can update this bitmap to your server
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            uplaodimage.setImageBitmap(bitmap);
//            uplaodimage.setScaleType(ImageView.ScaleType.MATRIX);


            ///converting
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte imageInByte[] = stream.toByteArray();
            db.deleteContact(String.valueOf(db.getid()));
            db.addImage(imageInByte);

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(Profile.this,e.getMessage());
        }
    }

    private void onCaptureImageResult(Intent data) {
        Uri uri = data.getParcelableExtra("path");
        try {
            // You can update this bitmap to your server
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            // loading profile image from local cache
            uplaodimage.setImageBitmap(bitmap);
//            uplaodimage.setScaleType(ImageView.ScaleType.MATRIX);

            ///converting
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte imageInByte[] = stream.toByteArray();
            db.deleteContact(String.valueOf(db.getid()));

            db.addImage(imageInByte);

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(Profile.this,e.getMessage());
        }
    }

//    @Override
//    protected void onResume() {
//
//        super.onResume();
//    }

    public void AlertDialogCreate(String remarkmsg, String type, String string) {
        String btntext = "";
        String remark = "";
        if (type.equalsIgnoreCase(Const.payment)) {
            btntext = "Pay Now";
            remark = string;
        } else if (type.equalsIgnoreCase(Const.doc)) {
            btntext = "Upload Again";
            remarkmsg = "Upload Incomplete";
            remark = string;
        } else {
            btntext = "Upload Again";
            remarkmsg = "Rejected Reason : " + remarkmsg;
            remark = string;
        }
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Profile.this, SweetAlertDialog.WARNING_TYPE);

        sweetAlertDialog.setTitleText(remark)
                .setContentText(remarkmsg)
                .setConfirmText(btntext)
//                .setCancelText("cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                        connectionProcess.ConnectToserver(connectionProcess);

                        if (type.equalsIgnoreCase(Const.doc)) {
                            Log.e("is_bank_verified: ", is_bank_verified);
                            Log.e("is_adhar_verified: ", is_adhar_verified);
                            Log.e("is_pan_verified: ", is_pan_verified);
                            Intent intent = new Intent(Profile.this, UploadDocScreen.class);
                            intent.putExtra(Const.from, Const.doc);
                            intent.putExtra(Const.bankstatus, is_bank_verified);
                            intent.putExtra(Const.adharstatus, is_adhar_verified);
                            intent.putExtra(Const.panstatus, is_pan_verified);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (type.equalsIgnoreCase(Const.payment)) {
                            Intent intent = new Intent(Profile.this, UpiPayment.class);
                            intent.putExtra(Const.from, "UPI");
                            startActivity(intent);
                        } else if (type.equalsIgnoreCase(Const.bankstatus)) {
                            Intent intent = new Intent(Profile.this, UploadDocScreen.class);
                            intent.putExtra(Const.from, Const.bankstatus);
                            intent.putExtra(Const.bankstatus, is_bank_verified);
                            startActivity(intent);
                        } else if (type.equalsIgnoreCase(Const.adharstatus)) {
                            Intent intent = new Intent(Profile.this, UploadDocScreen.class);
                            intent.putExtra(Const.from, Const.adharstatus);
                            intent.putExtra(Const.adharstatus, is_adhar_verified);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (type.equalsIgnoreCase(Const.panstatus)) {
                            Intent intent = new Intent(Profile.this, UploadDocScreen.class);
                            intent.putExtra(Const.from, Const.panstatus);
                            intent.putExtra(Const.panstatus, is_pan_verified);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                    }
                })
                .show();
//        sweetAlertDialog.setCancelable(false);
//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();

    }


    @Override
    protected void onResume() {
        super.onResume();

//        Methods.InsertLogs(Profile.this);
       // SharedPref.insert(SharedPref.LogsALL,this.getClass().getSimpleName(),Logics.getVppId(Profile.this),String.valueOf(Calendar.getInstance().getTime()),Profile.this);

        if (getIntent().getStringExtra("from").equalsIgnoreCase("0")){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Connectivity.getNetworkState(getApplicationContext()))
                        new ConnectTOServer(Profile.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

/*
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Const.dismiss == true) {
                                if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Views.ProgressDlgConnectSocket(Profile.this, connectionProcess, "Server Not Available");
//                                        ConnectToserver(connectionProcess);
                                        }
                                    });
                                }
                            }
                        }
                    }, 1000);
*/
                }
            });


        }
        else {
            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       ProgressDlgConnectSocket(Profile.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                    }
                });
            } else if (Const.isSocketConnected == true) {
                MSGFETCHDOCSTATmethod();
            }
        }

    }


    void Upload(String remarkmsg, String type, String string){
//        connectionProcess.ConnectToserver(connectionProcess);

        if (type.equalsIgnoreCase(Const.doc)) {
//            Log.e("is_bank_verified: ", is_bank_verified);
//            Log.e("is_adhar_verified: ", is_adhar_verified);
//            Log.e("is_pan_verified: ", is_pan_verified);
            Intent intent = new Intent(Profile.this, UploadDocScreen.class);
            intent.putExtra(Const.from, Const.doc);
            intent.putExtra(Const.bankstatus, is_bank_verified);
            intent.putExtra(Const.adharstatus, is_adhar_verified);
            intent.putExtra(Const.panstatus, is_pan_verified);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (type.equalsIgnoreCase(Const.payment)) {
            Intent intent = new Intent(Profile.this, UpiPayment.class);
            intent.putExtra(Const.from, "UPI");
            startActivity(intent);
        } else if (type.equalsIgnoreCase(Const.bankstatus)) {
            Intent intent = new Intent(Profile.this, UploadDocScreen.class);
            intent.putExtra(Const.from, Const.bankstatus);
            intent.putExtra(Const.bankstatus, is_bank_verified);
            startActivity(intent);
        } else if (type.equalsIgnoreCase(Const.adharstatus)) {
            Intent intent = new Intent(Profile.this, UploadDocScreen.class);
            intent.putExtra(Const.from, Const.adharstatus);
            intent.putExtra(Const.adharstatus, is_adhar_verified);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (type.equalsIgnoreCase(Const.panstatus)) {
            Intent intent = new Intent(Profile.this, UploadDocScreen.class);
            intent.putExtra(Const.from, Const.panstatus);
            intent.putExtra(Const.panstatus, is_pan_verified);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        }else if (type.equalsIgnoreCase("Esign")) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                open();
            }else {
                Intent intent = new Intent(Profile.this, PhotoVideoSignatureActivity.class);
                //    intent.putExtra(Const.from, Const.panstatus);
                //       intent.putExtra(Const.panstatus, is_pan_verified);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }*/
            Intent intent = new Intent(Profile.this, PhotoVideoSignatureActivity.class);
            //    intent.putExtra(Const.from, Const.panstatus);
            //       intent.putExtra(Const.panstatus, is_pan_verified);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    //only for android 11
    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Dear partner, our eSign feature is under development, request you to skip this and complete it after a week ");
        alertDialogBuilder.setPositiveButton("Skip",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Profile.this, Dashboard.class);
                        startActivity(intent);
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    void MSGFETCHDOCSTATmethod (){
        try {
            JSONObject jsonObject = new JSONObject();
            String vppid = Logics.getVppId(Profile.this);
            jsonObject.put("VPPID", vppid);
            jsonObject.put("reportType", "LeadsCount");
            jsonObject.put("version", "android");
            data = jsonObject.toString().getBytes();
//                ringProgressDialog = ProgressDialog.show(Profile.this, "Please wait ...", "Loading Your Data ...", true);
//                ringProgressDialog.setCancelable(true);
//
//                ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
            AlertDialogClass.PopupWindowShow(Profile.this,mainlayout);
            new SendTOServer(Profile.this, Profile.this, Const.MSGFETCHDOCSTAT, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //    new SendTOServer(Dashboard.this, Dashboard.this, Const.MSGFETCHDASHBOARD, data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //   new SendTOServer(Dashboard.this,Dashboard.this, Const.MSGFETCHVERSION,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //    new SendTOServer(Dashboard.this,Dashboard.this, Const.MSGFETCHLEADDETAILREPORT,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),Dashboard.class);
        startActivity(intent);
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(Profile.this),
                                    "0",
                                    Methods.getVersionInfo(Profile.this),
                                    Methods.getlogsDateTime(), "Profile",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    Profile.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!Profile.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                Toast.makeText(Profile.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess==null){
                Log.e( "DlgConnectSocket11111_null", "called");

            }else {
                new ConnectTOServer(Profile.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
