package com.application.vpp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.application.vpp.Views.Views;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class DiscripancyActivity extends NavigationDrawer implements RequestSent, View.OnClickListener, ConnectionProcess {
    RequestSent requestSent;
    ConnectionProcess connectionProcess;
    byte[] data = null;
    public static Handler handlerDiscripancy;
//    ProgressDialog ringProgressDialog;

    FancyButton btn_upload_document;
    //    SpinKitView spinKitView;
    TextView tvbankRemark, tvbankStatus, tvadharRemark, tvadharStatus, tvpanStatus, tvpanRemark,tvselfieRemark,tvselfieStatus, tvVideoStatus, tvvideoRemark, tvdocumentStatus, tvpaymentStatus;
    ImageView imgbankstatus, imgadharstatus, imgpanstatus, imgdocstatus, imgpaymentstatus,  imgselfiestatus,  videostatus;
    LinearLayout linear1, linear2, linear3, linear4, linear5,linear6,linear7;
    TextView arrow1, arrow2, arrow3, arrow4, arrow5,arrow6,arrow7;
    CardView cardview1, cardview2, cardView3, cardView4, cardView5 ,cardView6 ,cardView7 ;
    String is_bank_verified = "0";
    String is_adhar_verified = "0";
    String is_pan_verified = "0";

    String is_selfie_verified = "0";
    //String is_video_verified = "0";

    //    NiftyDialogBuilder materialDesignAnimatedDialog;
    String bank_remark, adhar_remark, pan_remark, selfie_remark, video_remark;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;
    boolean NOCheckINTERNET = false;
    Context context;

    int MaxTry = 0;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    LinearLayout LinearMain;

    TextView txt_paymentstts,txt_bankstatts,txt_adharstatus,txt_panstatts,txt_selfiestatts;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_discripancy, mDrawerLayout);
        requestSent = (RequestSent) this;
        connectionProcess = (ConnectionProcess) this;
//        materialDesignAnimatedDialog = NiftyDialogBuilder.getInstance(this);

        SplashScreen.handlerSplash = null;
        Dashboard.handlerDashboard = null;
        Profile.handlerProfile = null;
        DiscripancyActivity.handlerDiscripancy = null;

        handlerDiscripancy = new ViewHandler();
//        spinKitView = findViewById(R.id.spin_kit);
        LinearMain = findViewById(R.id.LinearMain);
        btn_upload_document = findViewById(R.id.btn_upload_document);


        tvbankRemark = findViewById(R.id.tvbankRemark);
        tvbankStatus = findViewById(R.id.tvbankStatus);

        tvadharRemark = findViewById(R.id.tvadharRemark);
        tvadharStatus = findViewById(R.id.tvadharStatus);

        tvpanRemark = findViewById(R.id.tvpanRemark);
        tvpanStatus = findViewById(R.id.tvpanStatus);

        tvselfieRemark = findViewById(R.id.tvselfieRemark);
        tvselfieStatus = findViewById(R.id.tvselfieStatus);


        tvdocumentStatus = findViewById(R.id.tvdocumentStatus);
        tvpaymentStatus = findViewById(R.id.tvpaymentStatus);

//        tvvideoRemark = findViewById(R.id.tvVideoRemark);
//        tvVideoStatus = findViewById(R.id.tvVideoStatus);





        imgbankstatus = findViewById(R.id.imgbankstatus);
        imgadharstatus = findViewById(R.id.imgadharstatus);
        imgpanstatus = findViewById(R.id.imgpanstatus);
        imgpaymentstatus = findViewById(R.id.imgpaymentstatus);
        imgdocstatus = findViewById(R.id.imgdocstatus);
        imgselfiestatus = findViewById(R.id.imgselfiestatus);
        videostatus = findViewById(R.id.videostatus);

        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        linear3 = findViewById(R.id.linear3);
        linear4 = findViewById(R.id.linear4);
        linear5 = findViewById(R.id.linear5);
        linear6 = findViewById(R.id.linear6);
        linear7 = findViewById(R.id.linear7);

        cardview1 = findViewById(R.id.cardview1);
        cardview2 = findViewById(R.id.cardview2);
        cardView3 = findViewById(R.id.cardview3);
        cardView4 = findViewById(R.id.cardview4);
        cardView5 = findViewById(R.id.cardview5);
        cardView6 = findViewById(R.id.cardview6);
        cardView7 = findViewById(R.id.cardview7);

        arrow1 = findViewById(R.id.arrow1);
        arrow2 = findViewById(R.id.arrow2);
        arrow3 = findViewById(R.id.arrow3);
        arrow4 = findViewById(R.id.arrow4);
        arrow5 = findViewById(R.id.arrow5);
        arrow6 = findViewById(R.id.arrow6);
        arrow7 = findViewById(R.id.arrow7);



        txt_paymentstts = findViewById(R.id.txt_paymentstts);
        txt_bankstatts = findViewById(R.id.txt_bankstatts);
        txt_adharstatus = findViewById(R.id.txt_adharstatus);
        txt_panstatts = findViewById(R.id.txt_panstatts);
        txt_selfiestatts = findViewById(R.id.txt_selfiestatts);

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/poppinsBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/poppinsBlack.otf");
        Typeface typeface3 = Typeface.createFromAsset(getAssets(), "fonts/poppinsMedium.otf");
        Typeface typeface4 = Typeface.createFromAsset(getAssets(), "fonts/ppinsExtraBold.otf");
        Typeface typeface5 = Typeface.createFromAsset(getAssets(), "fonts/poppinsRegular.otf");


        txt_paymentstts.setTypeface(typeface1);
        txt_bankstatts.setTypeface(typeface1);
        txt_adharstatus.setTypeface(typeface1);
        txt_panstatts.setTypeface(typeface1);
        txt_selfiestatts.setTypeface(typeface1);



        toolbar.setTitle("Registration Tracker");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList, "SocketLogs", DiscripancyActivity.this);

//        btn_upload_document.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialogCreate(tvdocumentStatus.getText().toString().toUpperCase(), Const.doc, "Documents status");
//
////                Intent intent = new Intent(DiscripancyActivity.this, UploadDocScreen.class);
////                intent.putExtra(Const.from, "D");
////                intent.putExtra(Const.bankstatus, is_bank_verified);
////                intent.putExtra(Const.adharstatus, is_adhar_verified);
////                intent.putExtra(Const.panstatus, is_pan_verified);
////                //  Intent intent = new Intent(Welcome.this,DashoboardDesign.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent);
//            }
//        });
        cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogCreate(tvdocumentStatus.getText().toString().toUpperCase(), Const.doc, "Documents status");

//                Intent intent = new Intent(DiscripancyActivity.this, UploadDocScreen.class);
//                intent.putExtra(Const.from, "D");
//                intent.putExtra(Const.bankstatus, is_bank_verified);
//                intent.putExtra(Const.adharstatus, is_adhar_verified);
//                intent.putExtra(Const.panstatus, is_pan_verified);
//                //  Intent intent = new Intent(Welcome.this,DashoboardDesign.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        });

        //payment click.
        cardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogCreate(tvpaymentStatus.getText().toString().toUpperCase(), Const.payment, "Payment status");
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialogCreate(bank_remark, Const.bankstatus, "Bank status");
                AlertDialogCreate(tvdocumentStatus.getText().toString().toUpperCase(), Const.doc, "Documents status");

            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialogCreate(adhar_remark, Const.adharstatus, "Adhar status");
                AlertDialogCreate(tvdocumentStatus.getText().toString().toUpperCase(), Const.doc, "Documents status");

            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialogCreate(pan_remark, Const.panstatus, "Pancard Status");
                AlertDialogCreate(tvdocumentStatus.getText().toString().toUpperCase(), Const.doc, "Documents status");

            }
        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialogCreate(bank_remark, Const.bankstatus, "Bank status");
                AlertDialogCreate(tvselfieStatus.getText().toString().toUpperCase(), Const.selfiestatus, "Selfie status");

            }
        });

//        cardView7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                AlertDialogCreate(bank_remark, Const.bankstatus, "Bank status");
//                AlertDialogCreate(tvVideoStatus.getText().toString().toUpperCase(), Const.videostatus, "Video status");
//
//            }
//        });



        //// calling hhere

        connectionProcess = (ConnectionProcess) this;
        if (Connectivity.getNetworkState(getApplicationContext())) {

            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgConnectSocket(DiscripancyActivity.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                    }
                });
            } else if (Const.isSocketConnected == true) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    String vppid = Logics.getVppId(DiscripancyActivity.this);
                    jsonObject.put("VPPID", Logics.getVppId(getApplicationContext()));
                    //jsonObject.put("VPPID", "11019");
                    jsonObject.put("reportType", "LeadsCount");
                    jsonObject.put("version", "android");
                    data = jsonObject.toString().getBytes();
//                Toast.makeText(getApplicationContext(), "called", Toast.LENGTH_SHORT).show();
//                    ringProgressDialog = new ProgressDialog(this);
//                    ringProgressDialog.setMessage("Please wait ..\n Loading Your Data ...");
//                    ringProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    ringProgressDialog.setIndeterminate(true);
//                    if (!DiscripancyActivity.this.isFinishing()){
//                        ringProgressDialog.show();
//                    }

                    AlertDialogClass.PopupWindowShow(DiscripancyActivity.this,mainLayout);

                    new SendTOServer(DiscripancyActivity.this, DiscripancyActivity.this, Const.MSGFETCHDOCSTAT, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } catch (JSONException e) {
                    e.printStackTrace();
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            }
        }else {
            Views.SweetAlert_NoDataAvailble(DiscripancyActivity.this, "Internet Not Available");

        }

        ///added this lines extra by shiva ....
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!Connectivity.getNetworkState(getApplicationContext())) {
                            NOCheckINTERNET = true;   /// no net ....
                            Log.e("run: ", "no net");
                            imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol_red));
//                            lineinternet.setBackgroundColor(getResources().getColor(R.color.red));
//                            txtinternet.setText("Online");
                            showSnackbar("Internet Not Available");

                        } else {
//                            NOCheckINTERNET = false;   /// no net ....
                            imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol));
//                            lineinternet.setBackgroundColor(getResources().getColor(R.color.btn_active));
//                            showSnackbar("Online");

//                            imgConnection.setBackground(getResources().getDrawable(R.drawable.ic_up_and_down_arrows_symbol));
                            Log.e("run: ", " net available");

                        }

                        if (NOCheckINTERNET == true) {
                            if (Connectivity.getNetworkState(getApplicationContext())) {
                                NOCheckINTERNET = false;
//                                if (Const.dismiss == true)
                                new ConnectTOServer(DiscripancyActivity.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            }
                        }
                    }
                });
            }
        }, delay);

    }

//    @Override
//    protected void onResume() {
//
//        connectionProcess = (ConnectionProcess) this;
//        if (Connectivity.getNetworkState(getApplicationContext())) {
//
//            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Views.ProgressDlgConnectSocket(DiscripancyActivity.this, connectionProcess, "Server Not Available");
////                    ConnectToserver(connectionProcess);
//                    }
//                });
//            } else if (Const.isSocketConnected == true) {
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    String vppid = Logics.getVppId(DiscripancyActivity.this);
//                    jsonObject.put("VPPID", Logics.getVppId(getApplicationContext()));
//                    jsonObject.put("reportType", "LeadsCount");
//                    jsonObject.put("version", "android");
//                    data = jsonObject.toString().getBytes();
////                Toast.makeText(getApplicationContext(), "called", Toast.LENGTH_SHORT).show();
//                    ringProgressDialog = new ProgressDialog(this);
//                    ringProgressDialog.setMessage("Please wait ..\n Loading Your Data ...");
//                    ringProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    ringProgressDialog.setIndeterminate(true);
//                    if (!DiscripancyActivity.this.isFinishing()){
//                        ringProgressDialog.show();
//                    }
//                    new SendTOServer(DiscripancyActivity.this, DiscripancyActivity.this, Const.MSGFETCHDOCSTAT, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    FirebaseCrashlytics.getInstance().recordException(e);
//                }
//            }
//        }else {
//            Views.SweetAlert_NoDataAvailble(DiscripancyActivity.this, "Internet Not Available");
//
//        }
//
//        ///added this lines extra by shiva ....
//        handler.postDelayed(runnable = new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(runnable, delay);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!Connectivity.getNetworkState(getApplicationContext())) {
//                            NOCheckINTERNET = true;   /// no net ....
//                            Log.e("run: ", "no net");
//                            imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol_red));
////                            lineinternet.setBackgroundColor(getResources().getColor(R.color.red));
////                            txtinternet.setText("Online");
//                            showSnackbar("Internet Not Available");
//
//                        } else {
////                            NOCheckINTERNET = false;   /// no net ....
//                            imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol));
////                            lineinternet.setBackgroundColor(getResources().getColor(R.color.btn_active));
////                            showSnackbar("Online");
//
////                            imgConnection.setBackground(getResources().getDrawable(R.drawable.ic_up_and_down_arrows_symbol));
//                            Log.e("run: ", " net available");
//
//                        }
//
//                        if (NOCheckINTERNET == true) {
//                            if (Connectivity.getNetworkState(getApplicationContext())) {
//                                NOCheckINTERNET = false;
////                                if (Const.dismiss == true)
//                                new ConnectTOServer(DiscripancyActivity.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//                            }
//                        }
//                    }
//                });
//            }
//        }, delay);
//        super.onResume();
//    }

    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    try {
                        JSONObject jsonObject = new JSONObject();
                        String vppid = Logics.getVppId(DiscripancyActivity.this);
                        jsonObject.put("VPPID", Logics.getVppId(getApplicationContext()));
                        jsonObject.put("reportType", "LeadsCount");
                        jsonObject.put("version", "android");
                        data = jsonObject.toString().getBytes();
                       // Toast.makeText(getApplicationContext(), "called", Toast.LENGTH_SHORT).show();
                        new SendTOServer(DiscripancyActivity.this, DiscripancyActivity.this, Const.MSGFETCHDOCSTAT, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                else
//                    Views.SweetAlert_NoDataAvailble(Welcome.this,"No Internet");
                    TastyToast.makeText(DiscripancyActivity.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(DiscripancyActivity.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(DiscripancyActivity.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        Views.SweetAlert_NoDataAvailble(DiscripancyActivity.this, "Internet Not Available");
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
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(DiscripancyActivity.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(DiscripancyActivity.this, connectionProcess, "Server Not Available");
//                                        ConnectToserver(connectionProcess);
                                    }
                                });
                            }
                        }
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void SocketDisconnected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(DiscripancyActivity.this, connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(DiscripancyActivity.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void requestSent(int value) {

    }

    public class ViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String dataq = (String) msg.obj;
            switch (msg.arg1) {
                case Const.MSGFETCHDOCSTAT:

                    Log.e("doc_response", dataq);
//                    ringProgressDialog.dismiss();
//                    ringProgressDialog.cancel();

                    AlertDialogClass.PopupWindowDismiss();
                    LinearMain.setVisibility(View.VISIBLE);
//                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
//                    linearmainView.startAnimation(animation);
                    try {
                        JSONArray jsonArray = new JSONArray(dataq);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONObject jsonObject1 = jsonArray.getJSONObject(1);

                        // 1 means complete , o means incomplete.. ...
                        String is_document_v = jsonObject.getString("is_document_v");
                        String is_payment_p = jsonObject.getString("is_payment_p");
                        String is_email_v = jsonObject.getString("is_email_v");
                        //is_payment_p  o pending 1 done
                        if (is_payment_p.equalsIgnoreCase("1")) {
                            imgpaymentstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.right_));
                            tvpaymentStatus.setText("Payment completed");
//                            tvpaymentStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                            arrow2.setVisibility(View.GONE);
                            cardview2.setClickable(false);
                            SharedPref.savePreferences(getApplicationContext(), SharedPref.UPIPayment, SharedPref.UPIPaymentDONE);
                        } else {
                            imgpaymentstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));
                            tvpaymentStatus.setText("Payment Incompleted");
//                            tvpaymentStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
                            arrow2.setVisibility(View.VISIBLE);
                            SharedPref.savePreferences(getApplicationContext(), SharedPref.UPIPayment, "");
                        }

                        Log.e("11", String.valueOf(jsonObject1.length()));
                        Log.e("22q", String.valueOf(jsonObject1.toString()));

                        if (jsonObject1.length() != 0) {
                            cardview1.setVisibility(View.GONE);
                            linear1.setVisibility(View.GONE);
                            Log.e("123123", "ll");
                            is_bank_verified = jsonObject1.getString("is_bank_verified");
                            is_adhar_verified = jsonObject1.getString("is_adhar_verified");
                            is_pan_verified = jsonObject1.getString("is_pan_verified");
                            is_selfie_verified = jsonObject1.getString("is_selfie_verified");
                           // is_video_verified = jsonObject1.getString("is_video_verified");


//                            0 Document not Uploaded
//                            1 Verified
//                            2 rejected
//                            3 Verification Pending
                            //is_bank_verified


                            if (is_bank_verified.equalsIgnoreCase("0")) {
                                tvbankStatus.setText("Document not Uploaded");
                                imgbankstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));
//                            tvbankStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                                cardView3.setClickable(true);
                                arrow3.setVisibility(View.VISIBLE);

                            } else if (is_bank_verified.equalsIgnoreCase("1")) {
                                tvbankStatus.setText("Verified");
                                imgbankstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.right_));
//                            tvbankStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                                cardView3.setClickable(false);

                            } else if (is_bank_verified.equalsIgnoreCase("2")) {
                                bank_remark = jsonObject1.getString("bank_remark");
                                tvbankStatus.setText("Discrepancy raised");
                                tvbankRemark.setText("Remark : " + bank_remark);
                                tvbankRemark.setVisibility(View.VISIBLE);
                                cardView3.setClickable(true);
                                imgbankstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.wrong));
//                            tvbankStatus.setTextColor(Color.RED);
                                arrow3.setVisibility(View.VISIBLE);

                            } else if (is_bank_verified.equalsIgnoreCase("3")) {
                                tvbankStatus.setText("Verification Pending");
                                cardView3.setClickable(false);
                                imgbankstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));
                            }

                            //is_adhar_verified
                            if (is_adhar_verified.equalsIgnoreCase("0")) {
                                tvadharStatus.setText("Document not Uploaded");
                                imgadharstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));
//                            tvadharStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                                cardView4.setClickable(true);
                                arrow4.setVisibility(View.VISIBLE);

                            } else if (is_adhar_verified.equalsIgnoreCase("1")) {
                                tvadharStatus.setText("Verified");
                                imgadharstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.right_));
//                            tvadharStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                                cardView4.setClickable(false);

                            } else if (is_adhar_verified.equalsIgnoreCase("2")) {
                                tvadharStatus.setText("Discrepancy raised");
                                adhar_remark = jsonObject1.getString("adhar_remark");
                                tvadharRemark.setText("Remark : " + adhar_remark);
                                tvadharRemark.setVisibility(View.VISIBLE);
                                imgadharstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.wrong));
                                arrow4.setVisibility(View.VISIBLE);
                                cardView4.setClickable(true);

                            } else if (is_adhar_verified.equalsIgnoreCase("3")) {
                                tvadharStatus.setText("Verification Pending");
                                cardView4.setClickable(false);
                                imgadharstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));

                            }

                           // is_pan_verified
                            if (is_pan_verified.equalsIgnoreCase("0")) {
                                tvpanStatus.setText("Document not Uploaded");
                                imgpanstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));
//                            tvpanStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                                cardView5.setClickable(true);
                                arrow5.setVisibility(View.VISIBLE);

                            } else if (is_pan_verified.equalsIgnoreCase("1")) {
                                tvpanStatus.setText("Verified");
                                imgpanstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.right_));
//                            tvpanStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                                cardView5.setClickable(false);

                            } else if (is_pan_verified.equalsIgnoreCase("2")) {
                                tvpanStatus.setText("Discrepancy raised");
                                pan_remark = jsonObject1.getString("pan_remark");
                                tvpanRemark.setText("Remark : " + pan_remark);
                                tvpanRemark.setVisibility(View.VISIBLE);
                                imgpanstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.wrong));
//                            tvpanStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
                                arrow5.setVisibility(View.VISIBLE);
                                cardView5.setClickable(true);
//                            tvpanRemark.setText("Remark : "+adhar_remark);

                            } else if (is_pan_verified.equalsIgnoreCase("3")) {
                                tvpanStatus.setText("Verification Pending");
                                cardView5.setClickable(false);
                                imgpanstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));
                            }

                            //

                        } else {
                            cardview1.setVisibility(View.VISIBLE);
                            linear1.setVisibility(View.VISIBLE);
                            cardView3.setVisibility(View.GONE);
                            cardView4.setVisibility(View.GONE);
                            cardView5.setVisibility(View.GONE);
                            imgdocstatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.wrong));
                            tvdocumentStatus.setText("Upload Incompleted");
//                            tvdocumentStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
                            arrow1.setVisibility(View.VISIBLE);
//                            btn_upload_document.setVisibility(View.VISIBLE);

//                            Toast.makeText(DiscripancyActivity.this, "dcsfdgfd", Toast.LENGTH_SHORT).show();
                        }

                        if (is_adhar_verified.equalsIgnoreCase("3") && (is_bank_verified.equalsIgnoreCase("3")) && (is_bank_verified.equalsIgnoreCase("3"))) {
                            btn_upload_document.setVisibility(View.GONE);
                        } else {
                            btn_upload_document.setVisibility(View.GONE);
                        }

                        //is_selfie_verified
                        if (is_selfie_verified.equalsIgnoreCase("0")) {
                            tvselfieStatus.setText("Document not Uploaded");
//                            imgselfiestatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));
//                            tvpanStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                            cardView6.setClickable(true);
                            arrow6.setVisibility(View.VISIBLE);


                        } else if (is_selfie_verified.equalsIgnoreCase("1")) {
                            tvselfieStatus.setText("Verified");
                            imgselfiestatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.right_));
//                            tvpanStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                            cardView6.setClickable(false);

                        } else if (is_selfie_verified.equalsIgnoreCase("2")) {
                            tvselfieStatus.setText("Discrepancy raised");
                            selfie_remark = jsonObject1.getString("selfie_remark");
                            tvselfieRemark.setText("Remark : " + selfie_remark);
                            tvselfieRemark.setVisibility(View.VISIBLE);
                            imgselfiestatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.wrong));
//                            tvpanStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
                            arrow6.setVisibility(View.VISIBLE);
                            cardView6.setClickable(true);
//                            tvpanRemark.setText("Remark : "+adhar_remark);

                        } else if (is_selfie_verified.equalsIgnoreCase("3")) {
                            tvselfieStatus.setText("Verification Pending");
                            cardView6.setClickable(false);
                            imgselfiestatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));
                        }

                        //is_video_verified
                      /*  if (is_video_verified.equalsIgnoreCase("0")) {
                            tvVideoStatus.setText("Video not Uploaded");
                            videostatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));
//                            tvpanStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                            cardView7.setClickable(true);
                            arrow7.setVisibility(View.VISIBLE);


                        } else if (is_video_verified.equalsIgnoreCase("1")) {
                            tvVideoStatus.setText("Verified");
                            videostatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.right));
//                            tvpanStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                            cardView7.setClickable(false);

                        } else if (is_video_verified.equalsIgnoreCase("2")) {
                            tvVideoStatus.setText("Discrepancy raised");
                            video_remark = jsonObject1.getString("video_remark");
                            tvvideoRemark.setText("Remark : " + video_remark);
                            tvvideoRemark.setVisibility(View.VISIBLE);
                            videostatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.wrong));
//                            tvpanStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
                            arrow7.setVisibility(View.VISIBLE);
                            cardView7.setClickable(true);
//                            tvpanRemark.setText("Remark : "+adhar_remark);

                        } else if (is_video_verified.equalsIgnoreCase("3")) {
                            tvVideoStatus.setText("Verification Pending");
                            cardView7.setClickable(false);
                            videostatus.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.pending));
                        }*/


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

                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);

                        Log.e("handleMessage: ", e.getMessage());
                    }
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static RippleDrawable getBackgroundDrawable(int pressedColor, Drawable backgroundDrawable) {
        return new RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null);
    }

    public static ColorStateList getPressedState(int pressedColor) {
        return new ColorStateList(new int[][]{new int[]{}}, new int[]{pressedColor});
    }

    public void AlertDialogCreate1(String remarkmsg, String type) {

        new AlertDialog.Builder(DiscripancyActivity.this)
//                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Remark")
                .setMessage(remarkmsg)
                .setPositiveButton("Upload Again", null)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Upload Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DiscripancyActivity.this, "You Clicked on OK", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DiscripancyActivity.this, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }


    public void AlertDialogCreate(String remarkmsg_, String type, String string) {
        String btntext = "";
        String remark = "";
        String remarkmsg = "";
        if (type.equalsIgnoreCase(Const.payment)) {
            btntext = "Pay Now";
            remark =
                    remark = string;
        } else if (type.equalsIgnoreCase(Const.doc)) {
            btntext = "Upload Again";
            remarkmsg = "Upload Incomplete";
            remark = string;
        } else {
            btntext = "Upload Again";
//            remarkmsg = "Rejected Reason : " + remarkmsg;
            remark = string;
        }
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DiscripancyActivity.this, SweetAlertDialog.WARNING_TYPE);

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
                        Toast.makeText(getApplicationContext(), "type=="+type, Toast.LENGTH_SHORT).show();
                        if (type.equalsIgnoreCase(Const.doc)) {
                            Intent intent = new Intent(DiscripancyActivity.this, UploadDocScreen.class);
                            intent.putExtra(Const.from, Const.doc);
                            intent.putExtra(Const.bankstatus, is_bank_verified);
                            intent.putExtra(Const.adharstatus, is_adhar_verified);
                            intent.putExtra(Const.panstatus, is_pan_verified);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (type.equalsIgnoreCase(Const.payment)) {
                            Intent intent = new Intent(DiscripancyActivity.this, UpiPayment.class);
                            intent.putExtra(Const.from, "UPI");
                            startActivity(intent);
                        } else if (type.equalsIgnoreCase(Const.bankstatus)) {
                            Intent intent = new Intent(DiscripancyActivity.this, UploadDocScreen.class);
                            intent.putExtra(Const.from, Const.bankstatus);
                            intent.putExtra(Const.bankstatus, is_bank_verified);
                            startActivity(intent);
                        } else if (type.equalsIgnoreCase(Const.adharstatus)) {
                            Intent intent = new Intent(DiscripancyActivity.this, UploadDocScreen.class);
                            intent.putExtra(Const.from, Const.adharstatus);
                            intent.putExtra(Const.adharstatus, is_adhar_verified);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (type.equalsIgnoreCase(Const.panstatus)) {
                            Intent intent = new Intent(DiscripancyActivity.this, UploadDocScreen.class);
                            intent.putExtra(Const.from, Const.panstatus);
                            intent.putExtra(Const.panstatus, is_pan_verified);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else if (type.equalsIgnoreCase(Const.selfiestatus)) {
                            Intent intent = new Intent(DiscripancyActivity.this, PhotoVideoSignatureActivity.class);
                            intent.putExtra(Const.from, Const.panstatus);
                            intent.putExtra(Const.selfiestatus, is_selfie_verified);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
/*
                        else if (type.equalsIgnoreCase(Const.videostatus)) {
                            Intent intent = new Intent(DiscripancyActivity.this, PhotoVideoSignatureActivity.class);
                            intent.putExtra(Const.from, Const.videostatus);
                            intent.putExtra(Const.videostatus, is_video_verified);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
*/

                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(DiscripancyActivity.this),
                                    "0",
                                    Methods.getVersionInfo(DiscripancyActivity.this),
                                    Methods.getlogsDateTime(), "DiscripancyActivity",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    DiscripancyActivity.this);

                            finishAffinity();
                            finish();
                        }
                    });
            if (!DiscripancyActivity.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                // Toast.makeText(UpiPayment.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            if (connectionProcess == null) {
                Log.e("DlgConnectSocket11111_null", "called");

            } else {
                new ConnectTOServer(DiscripancyActivity.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                connectionProcess.ConnectToserver(connectionProcess);
            }
            Log.e("DlgConnectSocket11111", "called");

        }

        Log.e("DlgConnectSocketMaxTry", String.valueOf(MaxTry));

    }

}