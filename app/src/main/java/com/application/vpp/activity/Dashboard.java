package com.application.vpp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Database.DatabaseHelper;
import com.application.vpp.Datasets.ProductMasterDataset;
import com.application.vpp.Datasets.UploadFileResponse;
import com.application.vpp.HttpsTrustManager;
import com.application.vpp.Interfaces.APiValidateAccount;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.DescrpncyIntrfce;
import com.application.vpp.Interfaces.GstProceed;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.Interfaces.SliderImagesPojo;
import com.application.vpp.NetworkCall.APIClient;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.ExitActivity;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.application.vpp.other.FcmMessagingService;
import com.application.vpp.other.GstDlg;
import com.application.vpp.other.ShareLinkDlg;
import com.daasuu.cat.CountAnimationTextView;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.listener.OnViewInflateListener;
import retrofit2.Call;
import retrofit2.Response;

public class Dashboard extends com.application.vpp.activity.NavigationDrawer implements GstProceed, RequestSent, View.OnClickListener, ConnectionProcess {


    APiValidateAccount apiService;

    private Button btnzIn;
    private Button btnzOut;
    PhotoView popupimage;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    ProgressBar progress_bar;

    //  ZoomControls zoomControls;
    android.app.AlertDialog alertDialog;

    public static View customView;
    public static PopupWindow popupWindow;
    boolean ranBefore = false;

    GstProceed gstProceed;
    RelativeLayout cardDisspripancy, cardBrokerage, cardClient, cardMyLeads, cardInProcess, cardRejected, cardAddLead, cardNotInterested;

    CardView cardReferral;

    boolean reff = false;
    RelativeLayout cardReferral1;
    public static Handler handlerDashboard;
    //    public static Handler handlervppcallonDashboard1;
    static Gson gson;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;
    DatabaseHelper dbh;
    int cardVisibility = 0;
    CardView cardViewInProgress, cardViewInRejected;
    ImageView imgDropDown;
    TextView txtdescripancy, txtReferral, txtAddReference, txtInProcess, txtMyLead, txtRejected, txtNotInterested, txtClients, txtPayout;
    CountAnimationTextView txtMyLeadsCount, txtClientsCount;
    byte[] data = null;
    byte[] data1 = null;
    private FancyShowCaseQueue fancyShowCaseQueue;
    FancyShowCaseView fancyShowCaseView0;
    FancyShowCaseView fancyShowCaseView2;
    FancyShowCaseView fancyShowCaseView3, fancyShowCaseView4, fancyShowCaseView5, fancyShowCaseView6, fancyShowCaseView7;
    int screenValue = 0;
    public int isfirstTime = 0;
    ConnectionProcess connectionProcess;
    ProgressDialog pDialog;
    //    public static Handler handlerpaymenetupdatestatus;
    String is_bank_verified = "0";
    String is_adhar_verified = "0";
    String is_pan_verified = "0";
    Timer timer;
    ArrayList<ProductMasterDataset> productMasterDatasetArrayList = new ArrayList<>();
    private int REQUEST_CODE = 11;
    //UPDATE PURPOSE ..
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private static final String TAG = "Sample";
    //// private InAppUpdateManager inAppUpdateManager;
    boolean NOCheckINTERNET = false;
    AppUpdateManager mAppupdatemanager;
    String currentVersion = "";
    LinearLayout layoutCard3;
    AlertDialog dialogLockPIN;
    AlertDialog dialogLockPIN1;
    String result = "";
    RelativeLayout mainLayout;

    DescrpncyIntrfce descrpncyIntrfce;

//    String is_selfie_verified = "0";
//    String is_video_verified = "0";
//    String is_esign_verified = "0";

    String is_selfie_verified = "0";
    String is_esign_verified = "0";
    String created_datetime = "0";

    boolean conditionCheck1Doc = true;
    boolean conditionCheck1Discrpncy = true;
    boolean conditionCheck2Payment = true;
    boolean conditionCheck3Esign = true;
    SharedPreferences preferences;
    JSONObject paramObject = null;
    String branchcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_dashboard_1, mDrawerLayout);


        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        try {

            apiService = new APIClient().getClientOPS(Dashboard.this).create(APiValidateAccount.class);
        } catch (Exception e) {
            Log.e("test6", e.getMessage());
        }


        //  send();


        descrpncyIntrfce = (DescrpncyIntrfce) this;
        dbh = new DatabaseHelper(this);
        gstProceed = (GstProceed) this;

        preferences = getPreferences(MODE_PRIVATE);


//        AppUpdater appUpdater = new AppUpdater(Dashboard.this);
//        appUpdater.start();


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        isfirstTime = Logics.getInstScreenVal(this);
        mainLayout = findViewById(R.id.mainLayout);
        layoutCard3 = (LinearLayout) findViewById(R.id.layoutCard3);
        cardAddLead = (RelativeLayout) findViewById(R.id.cardAddLead);
        cardMyLeads = (RelativeLayout) findViewById(R.id.cardMyLeads);
        cardInProcess = (RelativeLayout) findViewById(R.id.cardInProcess);
        cardRejected = (RelativeLayout) findViewById(R.id.cardRejcted);
        cardBrokerage = (RelativeLayout) findViewById(R.id.cardBrokerage);
        cardReferral = (CardView) findViewById(R.id.cardReferral);
        cardReferral1 = (RelativeLayout) findViewById(R.id.cardReferral1);
        cardDisspripancy = (RelativeLayout) findViewById(R.id.cardDisspripancy);
        cardClient = (RelativeLayout) findViewById(R.id.cardClient);
        cardNotInterested = (RelativeLayout) findViewById(R.id.cardNotInterested);
        cardViewInProgress = (CardView) findViewById(R.id.cardViewInProgress);
        cardViewInRejected = (CardView) findViewById(R.id.cardViewRejected);
        txtAddReference = (TextView) findViewById(R.id.txtAddReference);
        txtInProcess = (TextView) findViewById(R.id.txtInProcess);
        txtMyLead = (TextView) findViewById(R.id.txtMyLeads);
        txtRejected = (TextView) findViewById(R.id.txtRejected);
        txtNotInterested = (TextView) findViewById(R.id.txtNotInterested);
        txtClients = (TextView) findViewById(R.id.txtClients);
        txtPayout = (TextView) findViewById(R.id.txtPayout);

        txtMyLeadsCount = (CountAnimationTextView) findViewById(R.id.txtMyLeadsCount);
        txtClientsCount = (CountAnimationTextView) findViewById(R.id.txtClientsCount);
        txtdescripancy = (TextView) findViewById(R.id.txtdescripancy);
        txtReferral = (TextView) findViewById(R.id.txtReferral);
        //   imgDropDown = (ImageView)findViewById(R.id.imgDropDown);

        cardAddLead.setOnClickListener(this);
        cardInProcess.setOnClickListener(this);
        cardMyLeads.setOnClickListener(this);
        cardRejected.setOnClickListener(this);
        cardBrokerage.setOnClickListener(this);
        cardReferral.setOnClickListener(this);
        cardDisspripancy.setOnClickListener(this);
        cardClient.setOnClickListener(this);
        cardNotInterested.setOnClickListener(this);

        View drawerIcon = getToolbarNavigationIcon(toolbar);

//        Map<String, String> stringStringMap = new HashMap<>();
//        stringStringMap.put("OCRAPIKey", "OPSID1:e5EPWugygD_Wsdfsfsdfsdsc2wWHoC1D");
//        stringStringMap.put("Content-Type", "application/json");
//        stringStringMap.put("Accept", "application/json");

        try {

            String vppid = Logics.getVppId(Dashboard.this);
            paramObject = new JSONObject();
//            paramObject.put("userid", vppid); //655841
            paramObject.put("userid", vppid); //655841

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.e("ZZZZ", e.getMessage());
        }

/*
        cardReferral1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialogClass.PopupWindowShow(Dashboard.this, mainLayout);

                // temp comment.

                Call<JsonObject> validateSignature = apiService.SendOps(paramObject.toString());
                validateSignature.enqueue(new retrofit2.Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        String err = "";

                        if (response.isSuccessful()) {
                            try {
                                Log.e("test2", response.body().toString());
                                JSONObject object = new JSONObject(response.body().toString());
                                String status = object.getString("success");
                                if (status.equalsIgnoreCase("true")) {
                                    JSONObject jsonObject = object.getJSONObject("response");
                                    JSONArray jsonArray = jsonObject.getJSONArray("Table1");

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                    String EmpCode = jsonObject1.getString("EmpCode");
                                    branchcode = jsonObject1.getString("branchCode");
                                    // call Sp
                                    personlized_link_for_accnt_opnApicall(branchcode);
                                    Log.e("onResponseSuccess: ", branchcode);

                                } else {

                                    AlertDialogClass.PopupWindowDismiss();
//                                    Log.e("onResponseSuccess: ", "No active");
                                    Toast.makeText(Dashboard.this, "Data not pushed to OPS yet..", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Log.e("test3: ", "No active");
                            }

                        } else {
                            switch (response.code()) {
                                case 404:
//                            Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
                                    err = "Server Not Found";
                                    break;
                                case 500:
//                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
                                    err = "Server Unavailable";
                                    break;
                                case 503:
//                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
                                    err = "Server Overloaded try after sometime";
                                    break;
                                default:
                                    err = String.valueOf(response.code());
                                    err = "Something went wrong try again." + response.code();
//                            Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                            Toast.makeText(Dashboard.this, err, Toast.LENGTH_SHORT).show();
                            Log.e("test4", err);
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("test5", "   throwable===" + t.getMessage());
                    }
                });
            }
//                personlized_link_for_accnt_opnApicall();
        });
*/

        cardReferral1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e( "onClick: ", "new");
                FragmentManager fm = getSupportFragmentManager();
                if (Logics.getPLFOA(Dashboard.this).equalsIgnoreCase("") || Logics.getPLFOA(Dashboard.this).equalsIgnoreCase("null")) {
                    Toast.makeText(Dashboard.this, "Your referral link would be shared soon", Toast.LENGTH_SHORT).show();
                } else {
                    ShareLinkDlg gstDlg = ShareLinkDlg.newInstance(Logics.getPLFOA(Dashboard.this), gstProceed, Dashboard.this);
                    gstDlg.show(fm, "Dialog Fragment");
                }
            }
        });

//        private void shareTextUrl() {
//            Intent share = new Intent(android.content.Intent.ACTION_SEND);
//            share.setType("text/plain");
//            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//            // Add data to the intent, the receiving app will decide
//            // what to do with it.
//            share.putExtra(Intent.EXTRA_SUBJECT, "VPP share Link");
//            share.putExtra(Intent.EXTRA_TEXT, "This is the testing link "+"https://www.facebook.com/");
//            startActivity(Intent.createChooser(share, "Share link!"));
//        }


//        cardInProgress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                startActivity( new Intent(getApplicationContext(),MyLeads.class));
//
//            }
//        });

        // Inappupdate();

        Welcome.handlerWelcome = null;

        Dashboard.handlerDashboard = null;
        Profile.handlerProfile = null;
        SplashScreen.handlerSplash = null;
        DiscripancyActivity.handlerDiscripancy = null;
//        handlervppcallonDashboard1 = null;

        //  DashoboardDesign.handlerDashoboardDesign = null;
        handlerDashboard = new ViewHandler();
//        handlerpaymenetupdatestatus = new ViewHandler();
//        handlervppcallonDashboard1 = new ViewHandler();


//        if (!Const.isSocketConnected){
//            imgConnection.setBackground(getResources().getDrawable(R.drawable.ic_up_and_down_arrows_symbol_red));
//        }

        //update apk ...code in app updatee ..
//        inAppUpdateManager = InAppUpdateManager.Builder(this, REQ_CODE_VERSION_UPDATE)
//                .resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
//                .mode(Constants.UpdateMode.IMMEDIATE);
//
//        inAppUpdateManager.checkForAppUpdate();


//        if(Logics.getInstScreenVal(this)!=0) {

        // GetVppDetails();

    /*    int paymentdone = 0;
        if (getIntent().getExtras() != null) {
            paymentdone = getIntent().getExtras().getInt("isPaymentDone", 0);

            if (paymentdone == 1) {

             //   UpdatePaymentStatus();

            }
        }*/

        //Intent intent=new Intent(getApplicationContext(), PhotoVideoSignatureActivity.class);
        //startActivity(intent);

        mAppupdatemanager = AppUpdateManagerFactory.create(this);
        mAppupdatemanager.registerListener(installStateUpdatedListener);

//        PopupBanner();
//        PopUpClass popUpClass = new PopUpClass();
//        popUpClass.showPopupWindow(layoutCard3);

        if (SharedPref.getPreferences(Dashboard.this, "PopUpShow").equalsIgnoreCase("1")) {
            mpinPopupWindow(Dashboard.this);
        }

        if (SharedPref.getPreferences(Dashboard.this, "New").equalsIgnoreCase("0")) {
            if (SharedPref.getPreferences(Dashboard.this, "PopUpShowRevenueShare").equalsIgnoreCase("0")) {

                //   if (getVersionInfo())

            }
            mpinPopupWindow(Dashboard.this);
        }


        if (Logics.getVppId(Dashboard.this).length() > 0) {


            //kept as it is ..
            Logics.setIsBankVerified(com.application.vpp.activity.Dashboard.this, 0);

            // removed this ..
//            Logics.setVppPanDetails(com.application.vpp.activity.Dashboard.this, "", "", 0);
            // added this

            SharedPref.savePreferences1(Dashboard.this, "isPan", "0");
            SharedPref.savePreferences1(Dashboard.this, "panNo", "");
            SharedPref.savePreferences1(Dashboard.this, "panName", "");

            //removed this ..
//            Logics.setOtpVerificationDetails(com.application.vpp.activity.Dashboard.this, "", "", 0, 0, 0);
            //added this ..
            SharedPref.savePreferences1(Dashboard.this, "mobileNo", "");
            SharedPref.savePreferences1(Dashboard.this, "isMobile", "0");
            SharedPref.savePreferences1(Dashboard.this, "email", "");
            SharedPref.savePreferences1(Dashboard.this, "isEmail", "0");
            SharedPref.savePreferences1(Dashboard.this, "isReg", "0");


        }


        // before i was calling on resume method

        AlertDialogClass.PopupWindowDismiss();

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        new GetVersionCode().execute();

        connectionProcess = (ConnectionProcess) this;
        if (Connectivity.getNetworkState(getApplicationContext())) {
            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgConnectSocket(Dashboard.this, connectionProcess, "Server Not Available");
                    }
                });
            } else if (Const.isSocketConnected == true) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObject1 = new JSONObject();
                    String vppid = Logics.getVppId(Dashboard.this);

                    jsonObject.put("VPPID", vppid);
                    jsonObject.put("reportType", "LeadsCount");
                    jsonObject.put("version", "android");

                    jsonObject1.put("VPPID", vppid);
                    jsonObject1.put("reportType", "LeadsCount");
                    jsonObject1.put("version", "android");
                    jsonObject1.put("token_id", FcmMessagingService.getToken(getApplicationContext()));

                    data = jsonObject.toString().getBytes();
                    data1 = jsonObject1.toString().getBytes();

                    AlertDialogClass.PopupWindowShow(Dashboard.this, mainLayout);

                    GetVppDetails();

                    new SendTOServer(Dashboard.this, Dashboard.this, Const.MSGFETCHDOCSTAT, data1, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new SendTOServer(Dashboard.this, Dashboard.this, Const.MSGPRODUCTMASTER, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new SendTOServer(Dashboard.this, Dashboard.this, Const.MSGFETCHDASHBOARD, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    //  personlized_link_for_accnt_opnApicall();
                    //   new SendTOServer(Dashboard.this,Dashboard.this, Const.MSGFETCHVERSION,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    //    new SendTOServer(Dashboard.this,Dashboard.this, Const.MSGFETCHLEADDETAILREPORT,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Views.SweetAlert_NoDataAvailble(Dashboard.this, "Internet Not Available");

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
                            //Log.e("run: ", " net available");

                        }

                        if (NOCheckINTERNET == true) {
                            if (Connectivity.getNetworkState(getApplicationContext())) {
                                NOCheckINTERNET = false;
//                                if (Const.dismiss == true)
                                new ConnectTOServer(Dashboard.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            }
                        }
                    }
                });
            }
        }, delay);

    }


    private void GetVppDetails() {
//        pDialog = ProgressDialog.show(Dashboard.this, "Please wait ...", "Loading Your Data ...", true);
//        pDialog.setCancelable(true);
//        pDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        AlertDialogClass.PopupWindowShow(Dashboard.this, mainLayout);

        try {
            String versionName = "";
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                AlertDialogClass.ShowMsg(Dashboard.this, e.getMessage());
            }
            String vpp_id = Logics.getVppId(Dashboard.this);
            String panno = Logics.getPanNo(Dashboard.this);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("versionName", versionName);
            jsonObject.put("vpp_id", vpp_id);
            jsonObject.put("panno", panno);

            byte[] data = jsonObject.toString().getBytes();

            Log.e("splashScreen2", "connected: " + jsonObject.toString());
            new SendTOServer(Dashboard.this, Dashboard.this, Const.MSGVERIFYVPP, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(Dashboard.this, "", Toast.LENGTH_SHORT).show();
            AlertDialogClass.ShowMsg(Dashboard.this, e.getMessage());

        }

    }

//    private void UpdatePaymentStatus() {
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("vpp_id", Logics.getVppId(this));
//            jsonObject.put("pan_no", Logics.getPanNo(this));
//            jsonObject.put("paymentStaus", 1);
//            Log.e("Updatepaymentstatus", jsonObject.toString());
//            byte data[] = jsonObject.toString().getBytes();
//
//            pDialog = ProgressDialog.show(Dashboard.this, "Please wait ...", "Loading Your Data ...", true);
//            pDialog.setCancelable(true);
//            pDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//
//            new SendTOServer(Dashboard.this, Dashboard.this, Const.MSGUPDATEPAYMENTSTATUS, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private boolean isFirstTime1() {
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_add_prospects,menu);
//        return super.onCreateOptionsMenu(menu);
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//        switch (id){
//
//            case R.id.action_add:{
//
//                startActivity(new Intent(Dashboard.this,AddLead.class));
//
//            }break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void requestSent(int value) {

    }

    @Override
    public void onClick(View view) {
//        timer.cancel();
        int id = view.getId();
        switch (id) {

            case R.id.cardAddLead: {
                if (Connectivity.getNetworkState(getApplicationContext())) {
                    startActivity(new Intent(getApplicationContext(), AddLead.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("from", ""));
                    // startActivity(new Intent(getApplicationContext(), BankValidation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Views.SweetAlert_NoDataAvailble(Dashboard.this, "Connect internet !");
                }
            }
            break;

//            case R.id.cardMyLeads:{
//                if(cardVisibility==0){
//                    cardVisibility = 1;
//                    cardViewInProgress.setVisibility(View.VISIBLE);
//                    cardViewInRejected.setVisibility(View.VISIBLE);
//                    imgDropDown.setBackground(getResources().getDrawable(R.drawable.ic_arrow_up_18));
//
//                }else {
//
//                    cardVisibility = 0;
//                    cardViewInProgress.setVisibility(View.GONE);
//                    cardViewInRejected.setVisibility(View.GONE);
//                    imgDropDown.setBackground(getResources().getDrawable(R.drawable.ic_arrow_down_18));
//                }
//               // startActivity( new Intent(getApplicationContext(),MyLeads.class));
//            }break;

            case R.id.cardInProcess: {
//                if (Connectivity.getNetworkState(getApplicationContext())) {
//                    startActivity(new Intent(getApplicationContext(), InProcessLeads.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                } else {
//                    Views.SweetAlert_NoDataAvailble(Dashboard.this, "Connect internet !");
//                }
            }
            break;

            case R.id.cardMyLeads: {
                if (Connectivity.getNetworkState(getApplicationContext())) {
                    startActivity(new Intent(Dashboard.this, MyLeads.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Views.SweetAlert_NoDataAvailble(Dashboard.this, "Connect internet !");
                }
            }
            break;

            case R.id.cardRejcted: {
                if (Connectivity.getNetworkState(Dashboard.this)) {
                    startActivity(new Intent(Dashboard.this, RejectedList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Views.SweetAlert_NoDataAvailble(Dashboard.this, "Connect internet !");
                }
            }
            break;

            case R.id.cardNotInterested: {
                if (Connectivity.getNetworkState(getApplicationContext())) {
                    startActivity(new Intent(Dashboard.this, NotInterested.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Views.SweetAlert_NoDataAvailble(Dashboard.this, "Connect internet !");
                }
            }
            break;

            case R.id.cardBrokerage: {
                if (Connectivity.getNetworkState(getApplicationContext())) {

                    Log.e("onClick: ", "zzz");
                    FragmentManager fm = getSupportFragmentManager();

//                    ShareLinkDlg gstDlg = ShareLinkDlg.newInstance(gstProceed, Dashboard.this);
//                    gstDlg.show(fm, "Dialog Fragment");
                    startActivity(new Intent(Dashboard.this, Payout.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Views.SweetAlert_NoDataAvailble(Dashboard.this, "Connect internet !");
                }
            }
            break;

            case R.id.cardReferral1: {

                Log.e( "onClick: ", "zzz");
                FragmentManager fm = getSupportFragmentManager();
//                if (Logics.getPLFOA(Dashboard.this).equalsIgnoreCase("") || Logics.getPLFOA(Dashboard.this).equalsIgnoreCase(null)){
//                    Toast.makeText(Dashboard.this, "Link not available", Toast.LENGTH_SHORT).show();
//                }else {
//                    ShareLinkDlg gstDlg = ShareLinkDlg.newInstance(Logics.getPLFOA(Dashboard.this),gstProceed, Dashboard.this);
//                    gstDlg.show(fm, "Dialog Fragment");
//                }


//                Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
//                if (Connectivity.getNetworkState(getApplicationContext())) {
//                    String link = Logics.getPLFOA(Dashboard.this);
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_SEND);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//
//                    intent.putExtra(Intent.EXTRA_TEXT, "" + Logics.getPLFOA(Dashboard.this));
//                    intent.setType("text/plain");
//                    Intent chooserIntent = Intent.createChooser(intent, "share your referral link : \n" + Logics.getPLFOA(Dashboard.this));
//                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
//                    startActivity(chooserIntent);
////
//                }
            }
//            break;
////            case R.id.cardDisspripancy: {
////                if (Connectivity.getNetworkState(getApplicationContext())) {
////                    //startActivity(new Intent(Dashboard.this, DiscripancyActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//////                    Intent sendIntent = new Intent();
//////                    sendIntent.setAction(Intent.ACTION_SEND);
//////                    sendIntent.putExtra(Intent.EXTRA_TEXT, "" + Logics.getPLFOA(Dashboard.this));
//////                    sendIntent.setType("text/plain");
//////                    startActivity(sendIntent);
////
////                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
////                    clipboard.setText(Logics.getPLFOA(Dashboard.this));
////
////                    Intent intent = new Intent();
////                    intent.setAction(Intent.ACTION_SEND);
////                    intent.putExtra(Intent.EXTRA_TEXT, ""+clipboard.getText());
////                    intent.setType("text/plain");
////
////
////
////                    Intent chooserIntent = Intent.createChooser(intent, "Chooser Title");
////                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent     });
////                    startActivity(chooserIntent);
////
////                } else {
////                    Views.SweetAlert_NoDataAvailble(Dashboard.this, "Connect internet !");
////                }
////            }
////            break;

            case R.id.cardClient: {
                if (Connectivity.getNetworkState(getApplicationContext())) {
                    startActivity(new Intent(getApplicationContext(), ClientList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Views.SweetAlert_NoDataAvailble(Dashboard.this, "Connect internet !");
                }
            }
            break;
        }

    }


    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        //        AlertDailog.ProgressDlgDiss();

        Log.e("connected: ", "connected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject jsonObject1 = new JSONObject();
                        String vppid = Logics.getVppId(Dashboard.this);

                        jsonObject.put("VPPID", vppid);  //650180
                        jsonObject.put("reportType", "LeadsCount");
                        jsonObject.put("version", "android");
                        data = jsonObject.toString().getBytes();

                        jsonObject1.put("VPPID", vppid);  //650180
                        jsonObject1.put("reportType", "LeadsCount");
                        jsonObject1.put("version", "android");
                        jsonObject1.put("token_id", FcmMessagingService.getToken(getApplicationContext()));

                        data1 = jsonObject1.toString().getBytes();

                        GetVppDetails();

                        AlertDialogClass.PopupWindowShow(Dashboard.this, mainLayout);

                        new SendTOServer(Dashboard.this, Dashboard.this, Const.MSGFETCHDOCSTAT, data1, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        new SendTOServer(Dashboard.this, Dashboard.this, Const.MSGPRODUCTMASTER, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        new SendTOServer(Dashboard.this, Dashboard.this, Const.MSGFETCHDASHBOARD, data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        //   personlized_link_for_accnt_opnApicall();
                        //   new SendTOServer(Dashboard.this,Dashboard.this, Const.MSGFETCHVERSION,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        //    new SendTOServer(Dashboard.this,Dashboard.this, Const.MSGFETCHLEADDETAILREPORT,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        AlertDialogClass.ShowMsg(Dashboard.this, e.getMessage());

                    }
                else
//                    Views.SweetAlert_NoDataAvailble(Welcome.this,"No Internet");
                    TastyToast.makeText(Dashboard.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
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
                    ProgressDlgConnectSocket(Dashboard.this, connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(Dashboard.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        Views.SweetAlert_NoDataAvailble(Dashboard.this, "Internet Not Available");
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
                    new ConnectTOServer(Dashboard.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(Dashboard.this, connectionProcess, "Server Not Available");
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
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(Dashboard.this, connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(Dashboard.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void GstProceedclick(GstDlg gstDlg, String gstno, String gstname, String gstaddress) {

    }

    public class ViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.d("Message", "handleMessageDashboard: " + msg.toString());

            switch (msg.arg1) {
                case Const.MSGPRODUCTMASTER: {

                    AlertDialogClass.PopupWindowDismiss();

                    String data = (String) msg.obj;
                    Log.e("MSGPRODUCTMASTER", data);

                    productMasterDatasetArrayList = gson.fromJson(data, new TypeToken<ArrayList<ProductMasterDataset>>() {
                    }.getType());

                    if (productMasterDatasetArrayList != null) {
                        dbh.insertProductMaster(productMasterDatasetArrayList);
                    }
                }
                break;

//                case Const.MSGFETCHVPPDETAILS: {
//
//                    String data = (String) msg.obj;
//
//                    Log.e("MSGFETCHVPPDETAILS", data);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(data);
//                        String name = jsonObject.getString("name");
//                        String city = jsonObject.getString("city");
//                        String mobile = jsonObject.getString("mobile");
//                        String email = jsonObject.getString("email");
//                        String vppid = jsonObject.getString("vpp_id");
//                        String pan_no = jsonObject.getString("pan_no");
//                        Logics.setVppDetails(Dashboard.this, name, mobile, email, city, vppid, pan_no);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        AlertDialogClass.ShowMsg(Dashboard.this, e.getMessage());
//
//                    }
//                }
//                break;
//                case Const.MSGPERSONALIZED    LINK_FOR_ACCOUNT_OPENING_NEW: {
//                    try {
//                        AlertDialogClass.PopupWindowDismiss();
//                        String data = (String) msg.obj;
//                        JSONObject js = new JSONObject(data);
//
//                        Log.e("LINK", data);
//
//                        if (js.getInt("Status") == 1) { dw233
//                            Toast.makeText(Dashboard.this, "" + js.getString("ErrorMessage"), Toast.LENGTH_SHORT).show();
//                        } else {
//                            String link = (js.getString("URL") != null) ? js.getString("URL") : "";
//                            // Logics.setPLFOA(Dashboard.this, link);
//                            if (link.length() > 0) {
//                                FragmentManager fm = getSupportFragmentManager();
//                                ShareLinkDlg gstDlg = ShareLinkDlg.newInstance(link,gstProceed, Dashboard.this);
//                                gstDlg.show(fm, "Dialog Fragment");
//
////                                Intent intent = new Intent();
////                                intent.setAction(Intent.ACTION_SEND);
////                                intent.setType("text/plain");
////                                Intent chooserIntent = Intent.createChooser(intent, "Chooser Title");
////                                chooserIntent.putExtra(Intentf.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
////                                chooserIntent.putExtra(Intentf.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
////                                startActivity(chooserIntent);
//                            } else {
//                                personlized_link_for_accnt_opnApicall(branchcode);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                break;
//
////                case Const.MSGFETCHVPPDETAILS: {
////
////                    String data = (String) msg.obj;
////
////                    Log.e("MSGFETCHVPPDETAILS", data);
////
////                    try {
////                        JSONObject jsonObject = new JSONObject(data);
////                        String name = jsonObject.getString("name");
////                        String city = jsonObject.getString("city");
////                        String mobile = jsonObject.getString("mobile");
////                        String email = jsonObject.getString("email");
////                        String vppid = jsonObject.getString("vpp_id");
////                        String pan_no = jsonObject.getString("pan_no");
////                        Logics.setVppDetails(Dashboard.this, name, mobile, email, city, vppid, pan_no);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                        AlertDialogClass.ShowMsg(Dashboard.this, e.getMessage());
////
////                    }
////                }
////                break;
                case Const.MSG_POSTREVENUESHARING: {
                    AlertDialogClass.PopupWindowDismiss();
                    String data = (String) msg.obj;
                    Log.e("MSG_POSTREVENUESHARING", data);
                    try {
                        String data1 = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(data1);
                        int status = jsonObject.getInt("status");

                        if (status == 1) {
                            String message = jsonObject.getString("message");
                            // AlertDialogClass.ShowMsg(Dashboard.this, message);
                        } else {
                            String message = jsonObject.getString("message");
                            AlertDialogClass.ShowMsg(Dashboard.this, message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        AlertDialogClass.ShowMsg(Dashboard.this, e.getMessage());

                        Log.e("handleMessage: ", e.getMessage());
                    }

                }
                break;

//                case Const.MSGFETCHVPPDETAILS: {
//
//                    String data = (String) msg.obj;
//
//                    Log.e("MSGFETCHVPPDETAILS", data);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(data);
//                        String name = jsonObject.getString("name");
//                        String city = jsonObject.getString("city");
//                        String mobile = jsonObject.getString("mobile");
//                        String email = jsonObject.getString("email");
//                        String vppid = jsonObject.getString("vpp_id");
//                        String pan_no = jsonObject.getString("pan_no");
//                        Logics.setVppDetails(Dashboard.this, name, mobile, email, city, vppid, pan_no);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        AlertDialogClass.ShowMsg(Dashboard.this, e.getMessage());
//
//                    }
//                }
//                break;

                case Const.MSGFETCHDASHBOARD: {
                    String data = (String) msg.obj;
                    Log.e("vppCount", data);

                    try {
                        JSONObject jsonObject = new JSONObject(data);

                        if (jsonObject.length() != 0) {

                            String AccountOpened = jsonObject.getString("AccountOpened");
                            String LeadsCount = jsonObject.getString("LeadsCount");
                            String ConvertedLeads = jsonObject.getString("ConvertedLeads");
                            String ProgressLeads = jsonObject.getString("ProgressLeads");

                            txtClientsCount.setText(AccountOpened);
                            txtMyLeadsCount.setText(ProgressLeads);

                            txtClientsCount.setAnimationDuration(2000)
                                    .countAnimation(0, Integer.parseInt(AccountOpened));
                            txtMyLeadsCount.setAnimationDuration(2000)
                                    .countAnimation(0, Integer.parseInt(ProgressLeads));
                        } else {
                            txtClientsCount.setVisibility(View.GONE);
                            txtMyLeadsCount.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertDialogClass.ShowMsg(Dashboard.this, e.getMessage());

                    }

                }
                break;

                case Const.MSGFETCHDOCSTAT:
                    String dataq = (String) msg.obj;
                    Log.e("doc_response_dash", dataq);

                    AlertDialogClass.PopupWindowDismiss();

                    try {
                        JSONArray jsonArray = new JSONArray(dataq);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONObject jsonObject1 = jsonArray.getJSONObject(1);

                        String revenuecount = jsonObject.getString("revenuecount");
                        String is_payment_p = jsonObject.getString("is_payment_p");
                        created_datetime = jsonObject.getString("created_datetime");

                        Log.e("created_datetime", created_datetime);

                        is_selfie_verified = jsonObject1.getString("is_selfie_verified");
                        //is_video_verified = jsonObject1.getString("is_video_verified");
                        is_esign_verified = jsonObject1.getString("is_esign_verified");


                        is_bank_verified = jsonObject1.getString("is_bank_verified");
                        is_adhar_verified = jsonObject1.getString("is_adhar_verified");
                        is_pan_verified = jsonObject1.getString("is_pan_verified");

                        // doc...

                        //0 means not uploaded..
//                        1 means verified..
                        // 2 rejected..
//                        3 means verification pending..

                        if (is_adhar_verified.equalsIgnoreCase("0") || (is_bank_verified.equalsIgnoreCase("0")) || (is_pan_verified.equalsIgnoreCase("0"))) {
                            Logics.setDocumentStatus(Dashboard.this, "0");
                            conditionCheck1Doc = true;
                            conditionCheck1Discrpncy = false;
                        } else if (is_adhar_verified.equalsIgnoreCase("2") || (is_bank_verified.equalsIgnoreCase("2")) || (is_pan_verified.equalsIgnoreCase("2"))) {
                            Logics.setDocumentStatus(Dashboard.this, "0");
                            conditionCheck1Discrpncy = true;
                            conditionCheck1Doc = true;
                        } else {
                            Logics.setDocumentStatus(Dashboard.this, "1");
                            conditionCheck1Doc = false;
                            conditionCheck1Discrpncy = false;
                        }


                        if (is_adhar_verified.equalsIgnoreCase("2") ||
                                (is_bank_verified.equalsIgnoreCase("2")) ||
                                (is_pan_verified.equalsIgnoreCase("2")) || is_selfie_verified.equalsIgnoreCase("2") ||
                                is_esign_verified.equalsIgnoreCase("2")) {
                            Logics.setDescrStatus(Dashboard.this, "up");
                            descrpncyIntrfce.CheckUp();
//                            NavigationDrawer navigationDrawer = new NavigationDrawer();
//                            navigationDrawer.layoutdiscrepancyup.setVisibility(View.VISIBLE);
//                            navigationDrawer.layoutdiscrepancy.setVisibility(View.GONE);
                            //
                        } else {
                            descrpncyIntrfce.CheckDown();
                            Logics.setDescrStatus(Dashboard.this, "down");
//                            NavigationDrawer navigationDrawer = new NavigationDrawer();
//                            navigationDrawer.layoutdiscrepancyup.setVisibility(View.GONE);
//                            navigationDrawer.layoutdiscrepancy.setVisibility(View.VISIBLE);
                        }

                        //payment...

                        if (is_payment_p.equalsIgnoreCase("1")) {
                            Logics.setPaymentStatus(Dashboard.this, "1");
                            conditionCheck2Payment = false;
                        } else {
                            Logics.setPaymentStatus(Dashboard.this, "0");
                            conditionCheck2Payment = true;
                        }


                        if (SharedPref.getPreferences(getApplicationContext(), SharedPref.UPIPayment1).equalsIgnoreCase(SharedPref.UPIPaymentDONE)) {
                            conditionCheck2Payment = false;
                            Logics.setPaymentStatus(Dashboard.this, "1");
                        }

                        //esign ...

                        if (is_selfie_verified.equalsIgnoreCase("0") || (is_esign_verified.equalsIgnoreCase("0"))) {
                            Logics.setEsignStatus(Dashboard.this, "0");
                            conditionCheck3Esign = true;
                        } else if (is_selfie_verified.equalsIgnoreCase("2") || (is_esign_verified.equalsIgnoreCase("2"))) {
                            Logics.setEsignStatus(Dashboard.this, "0");
                            conditionCheck3Esign = true;
                        } else {
                            Logics.setEsignStatus(Dashboard.this, "1");
                            conditionCheck3Esign = false;
                        }

//                        if (conditionCheck1Doc == true || conditionCheck2Payment == true || conditionCheck3Esign == true) {
//                            startAlertPopup(Dashboard.this, mainLayout);
//                        }

                        Log.e("handleMessage1: ", String.valueOf(conditionCheck1Doc));
                        Log.e("handleMessage2: ", String.valueOf(conditionCheck2Payment));
                        Log.e("handleMessage3: ", String.valueOf(conditionCheck3Esign));

                        int ii = 0;

                        if (conditionCheck1Doc == false) { // doc done
                            ii = 1;
                            Log.e("handleMessage: ", "1");
                        }
                        if (conditionCheck1Doc == false && conditionCheck2Payment == false) { // doc and payment done
                            Log.e("handleMessage: ", "2");
                            ii = 2;
                        }

                        if (conditionCheck1Doc == false && conditionCheck2Payment == false && conditionCheck3Esign == false) { //doc ,payment, esign done.
                            Log.e("handleMessage: ", "3");
                            ii = 3;
                        }

                        if (conditionCheck1Discrpncy == true) {
                            startAlertPopup1(Dashboard.this, mainLayout, ii);
                        } else {
                            if (ii != 3) {
                                startAlertPopup(Dashboard.this, mainLayout, ii);
                            }
                        }

                        if (ii == 3) {
                            try {
                                // all done

                                if (Integer.parseInt(revenuecount) > 0) {

                                    //done

                                } else {


                                    // not done.

                                    SimpleDateFormat dtobj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    // Parsing dates in Date datatype

                                    Date a = null;
                                    Date b = null;

                                    a = dtobj.parse(created_datetime);
//                                    b = dtobj.parse("2023-04-06 00:00:01");
                                    b = dtobj.parse("2023-04-14 00:00:01");

                                    // Printing the dates

                                    Log.e("", "Date a is " + dtobj.format(a));
                                    Log.e("", "Date b is " + dtobj.format(b));

                                    // Checking for equal case

                                    if (a.equals(b)) {

                                        Log.e("", "Both dates are of same day");
                                    }
                                    // Checking for after case

                                    else if (a.after(b)) {

                                        Log.e("", "Date a comes after Date b");
                                    }
                                    // Checking for before case

                                    else if (a.before(b)) {
                                        mRevenueSharingPopupWindow(Dashboard.this);

                                        Log.e("", "Date a comes before Date b");
                                    }
                                    // here we need to add one condition ... to check agreed or not,

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                                Log.e("exception: ", e.getMessage());
                            }


                        }


//                        // 1 means complete , o means incomplete.. ...
//                        String is_document_v = jsonObject.getString("is_document_v");
//                        String is_payment_p = jsonObject.getString("is_payment_p");
//                        String is_email_v = jsonObject.getString("is_email_v");

//                        //is_payment_p  o pending 1 done

//                        if (is_payment_p.equalsIgnoreCase("1")) {
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.is_payment_p, "1");
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.UPIPayment, SharedPref.UPIPaymentDONE);
//                        } else {
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.is_payment_p, "0");
//                        }

                        ///second object ....validation   0 means pending,1 means verified ,2 means rejected
//                        is_bank_verified = jsonObject1.getString("is_bank_verified");
//                        is_adhar_verified = jsonObject1.getString("is_adhar_verified");
//                        is_pan_verified = jsonObject1.getString("is_pan_verified");

                        //is_bank_verified
//                        if (is_bank_verified.equalsIgnoreCase("0")) {
////                            tvpanStatus.setText("Verification Pending");
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isBankStatus, "0");
//                        } else if (is_bank_verified.equalsIgnoreCase("1")) {
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isBankStatus, "1");
//
//                        } else if (is_bank_verified.equalsIgnoreCase("2")) {
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isBankStatus, "2");
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isbank_remark, jsonObject1.getString("bank_remark"));
////                            tvpanRemark.setText("Remark : "+adhar_remark);
//                        }

                        //is_adhar_verified
//                        if (is_adhar_verified.equalsIgnoreCase("0")) {
////                            tvpanStatus.setText("Verification Pending");
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isAdharStatus, "0");
//                        } else if (is_adhar_verified.equalsIgnoreCase("1")) {
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isAdharStatus, "1");
//
//                        } else if (is_adhar_verified.equalsIgnoreCase("2")) {
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isAdharStatus, "2");
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isAdharremark, jsonObject1.getString("adhar_remark"));
////                            tvpanRemark.setText("Remark : "+adhar_remark);
//                        }

                        //is_pan_verified
//                        0 means Verification Pending
//                        1 means Verified
//                        2 means Rejected
//                        if (is_pan_verified.equalsIgnoreCase("0")) {
////                            tvpanStatus.setText("Verification Pending");
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isPanStatus, "0");
//                        } else if (is_pan_verified.equalsIgnoreCase("1")) {
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isPanStatus, "1");
//
//                        } else if (is_pan_verified.equalsIgnoreCase("2")) {
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.isPanStatus, "2");
//                            SharedPref.savePreferences(getApplicationContext(), SharedPref.ispan_remark, jsonObject1.getString("pan_remark"));
////                            tvpanRemark.setText("Remark : "+adhar_remark);
//                        }
                        //

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //   AlertDialogClass.ShowMsg(Dashboard.this,e.getMessage());

                    }

                    ///apurva code merged by shiva  ....on 07/10/2020...
//                    try {
//                        String docStatus = "Pending";
//                        String payStatus = "Pending";
//                        String strReminder = "A gentle reminder to complete your documentation and payment.We would be happy to provide any further assistance, if required. Contact us on vpp@ventura1.com . Keep referring, keep earning !";
//                        //String strTextCongrats = "Congratulations ! Your VPP code is : "+Logics.getVppId(Dashboard.this)+". We are glad to inform you that your provisional partner registration is completed. Please click the link below to know the further process to complete the documentation and final registration.";
//                        JSONArray array = new JSONArray(dataq);
//                        JSONObject jsonObject = array.getJSONObject(0);
//                        int is_document_v = jsonObject.getInt("is_document_v");
//                        int is_payment_p = jsonObject.getInt("is_payment_p");
//                        int days = jsonObject.getInt("days");
//
//
//                        JSONObject jsonObject1 = array.getJSONObject(1);
//
//                        if (is_document_v == 1) {
//                            docStatus = "Completed";
//                        }
//                        if (is_payment_p == 1) {
//                            payStatus = "Completed";
//                        }
//
//                        Log.d("MSGFETCHDOCSTAT", "handleMessage: " + days);
//
//                        final Dialog dialog = new Dialog(Dashboard.this,android.R.style.Theme_Wallpaper_NoTitleBar_Fullscreen);
//                        dialog.setContentView(R.layout.layout_days_popup);
//                        dialog.setCancelable(false);
//                        TextView txtDoc = (TextView) dialog.findViewById(R.id.txtDocumentStat);
//                        TextView txtPay = (TextView) dialog.findViewById(R.id.txtPaymentStat);
//                        TextView txtDaysLeft = (TextView) dialog.findViewById(R.id.txtDaysLeft);
////                        TextView txtCongrats = (TextView) dialog.findViewById(R.id.txtCongrats);
////                        TextView txtClose = (TextView) dialog.findViewById(R.id.txtClose);
//                        Button btnKnowMore = (Button) dialog.findViewById(R.id.btnKnowMore);
//                        LinearLayout lay_doc = (LinearLayout) dialog.findViewById(R.id.doc_layout);
//                        LinearLayout lay_pay = (LinearLayout) dialog.findViewById(R.id.pay_layout);
//                        LinearLayout esign_layout = (LinearLayout) dialog.findViewById(R.id.esign_layout);
//
//                        if(is_document_v==2 || is_document_v==0){
//                            lay_doc.setVisibility(View.VISIBLE);
//                        }else {
//                            lay_doc.setVisibility(View.GONE);
//                        }
//
//                         if(is_payment_p==0){
//                             lay_pay.setVisibility(View.VISIBLE);
//                        }else {
//                             lay_pay.setVisibility(View.GONE);
//                        }
//
//                        btnKnowMore.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                                dialog.cancel();
//                                Date today = new Date();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                                String curdate = dateFormat.format(today);
//                                Logics.setDate(Dashboard.this, curdate);
//                            }
//                        });
//                        if (is_document_v == 0) {
//                            lay_doc.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    //Call document complete avtivity
//                                    dialog.dismiss();
//                                    Intent intent = new Intent(Dashboard.this, UploadDocScreen.class);
//                                    intent.putExtra(Const.from, Const.doc);
//                                    intent.putExtra(Const.bankstatus, is_bank_verified);
//                                    intent.putExtra(Const.adharstatus, is_adhar_verified);
//                                    intent.putExtra(Const.panstatus, is_pan_verified);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                }
//                            });
//                        }
//                        if (is_payment_p == 0) {
//                            lay_pay.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    //Call payment complete
//                                    dialog.dismiss();
//
//                                    Intent intent = new Intent(Dashboard.this, UpiPayment.class);
//                                    intent.putExtra(Const.from, "UPI");
//                                    startActivity(intent);
//                                }
//                            });
//                        }
//                        //txtCongrats.setText(strReminder);
//                        txtDoc.setText(docStatus);
//                        txtPay.setText(payStatus);
//
//                        if (days < 0) {
//                            txtDaysLeft.setText("Days left for Registration  " + days);
////                            if (is_document_v == 0 || is_payment_p == 0 || is_selfie_verified.equalsIgnoreCase("0") || is_video_verified.equalsIgnoreCase("0") || is_esign_verified.equalsIgnoreCase("0")) {
//
//                            if (is_document_v == 0 || is_payment_p == 0 ) {
//                                //dialog.show();
//                                Date date = new Date();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                                String today = dateFormat.format(date);
//
//                                Log.e("today", today);
//                                String shareddate = Logics.getDate(Dashboard.this);
////                                Log.e("shareddate", shareddate);
//
//                                if (shareddate == null || !shareddate.equals(today)) {
//                                    dialog.show();
//                                    Logics.setDate(Dashboard.this, today);
//                                } else {
//                                    //dialog.show();//Remove later at time of live
//                                }
//                            }
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        AlertDialogClass.ShowMsg(Dashboard.this,e.getMessage());
//
//                    }

                    break;
                case Const.MSGVERIFYVPP: {
                    int status = 0;
                    int downloadApk = 0;
                    int isDeactivated;

                    AlertDialogClass.PopupWindowDismiss();

                    try {
                        String data = (String) msg.obj;
                        Log.e("MSGVERIFYVPP2", data);
                        JSONObject jsonObject = new JSONObject(data);
                        status = jsonObject.getInt("status");
                        downloadApk = jsonObject.getInt("downloadApk");
                        isDeactivated = jsonObject.getInt("isDeactivated");

                        // String vpp_id = Logics.getVppId(Dashboard.this);

                        if (status == 1) {
                            if (isDeactivated == 1) {
                                //Toast.makeText(SplashScreen.this, "Your Account is Deactivated", Toast.LENGTH_LONG).show();

                                TastyToast.makeText(
                                        getApplicationContext(),
                                        "Your Account is Deactivated",
                                        TastyToast.LENGTH_LONG,
                                        TastyToast.INFO
                                );
                            } else {
                                if (downloadApk == 0) {
                                    JSONObject vppdata = jsonObject.getJSONObject("vppdata");
                                    int x = vppdata.length();
                                    if (x == 0) {
                                        Intent intent = new Intent(Dashboard.this, SliderImages.class).putExtra("from", "");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else {
                                        String name = vppdata.getString("name");
                                        String city = vppdata.getString("city");
                                        String mobile = vppdata.getString("mobile");
                                        String email = vppdata.getString("email");
                                        String vppid = vppdata.getString("vpp_id");
                                        String pan_no = vppdata.getString("pan_no");
                                        String bankAccNo = vppdata.getString("bankAccNo");
                                        String state = vppdata.getString("state");
                                        String accnt_opn_link = vppdata.getString("accnt_opn_link");

                                        Log.e("accnt_opn_link", accnt_opn_link.trim());

                                        Logics.setPLFOA(Dashboard.this, accnt_opn_link.trim());

                                /*{ {"isDeactivated":0,"vppdata":{"area":"sainagar road Panvel","is_email":1,"is_doc_v":0,
                                "city":"Head Office","isBankVerified":1,"vpp_bank_name":"Mr  PRAVIN LAXMAN DI","mobile":"9975153610",
                                "pan_no":"ARAPD2934M","refcode":"11145","bankAccNo":"20149541044","vpp_pan_name":"Shri PRAVIN LAXMAN DITI",
                                "ref":"Ventura Partner","pin":"410206","dob":"19-08-1994","proffession":"student","name":"Pravin Laxman Diti",
                                "is_pay_p":0,"house_no":"203, uptown avenue","is_otp_v":1,"state":"Maharashtra","ifsc":"SBIN0003667",
                                "email":"PRAVINDI.BOOKONSPOT@GMAIL.COM","vpp_id":"11145"},"downloadApk":0,"message":"Existing User","status":1}  }*/

                                        SharedPref.savePreferences(Dashboard.this, "state", state);
                                        // Logics.setPaymentStatus(Dashboard.this, vppdata.getString("is_pay_p"));
                                        SharedPref.savePreferences(Dashboard.this, "bankAccNo", bankAccNo);
                                        Logics.setVppDetails(Dashboard.this, name, mobile, email, city, vppid, pan_no);

                                        Logics.setmobileNo(Dashboard.this, mobile);  //save mble no ..

                                        try {
                                           /* if (accnt_opn_link.equalsIgnoreCase("NOT IDENTIFIED")) {
                                                cardReferral.setVisibility(View.GONE);
                                                reff = false;

                                            } else if (accnt_opn_link.equalsIgnoreCase("Not VPP")) {
                                                cardReferral.setVisibility(View.GONE);
                                                reff = false;

                                            } else if (accnt_opn_link.trim() == null || accnt_opn_link.trim().equals("")) {
                                                cardReferral.setVisibility(View.GONE);
                                                reff = false;

                                            } else {
                                                cardReferral.setVisibility(View.VISIBLE);
                                                reff = true;
                                            }*/


                                            fancyShowCaseView0 = new FancyShowCaseView.Builder(Dashboard.this)
                                                    .title("First Queue Item")
                                                    .focusOn(txtAddReference)
                                                    .closeOnTouch(false)
                                                    .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
                                                        @Override
                                                        public void onViewInflated(@NonNull View view) {
                                                            Button btn = (Button) view.findViewById(R.id.btn_action_1);
                                                            // btn.setOnClickListener(mClickListener);

                                                            btn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    fancyShowCaseView0.hide();
                                                                }
                                                            });


                                                            btn.setText("Next");
                                                            TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
                                                            TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
                                                            txtView.setText("Add Reference");
                                                            //  subtitle.setText("Check me out for Application snapshots & upcoming holidays.");
                                                            //  subtitle.setText(" Check me out for Application snapshots , viewing  & applying your EWM .. also upcoming holidays.");
                                                            subtitle.setText("Referring your contacts is just a click away. Enter their basic details here and that’s it!");
                                                            Log.d("0", "onViewInflated: ");

                                                        }
                                                    })
                                                    .build();

//        fancyShowCaseView1 = new FancyShowCaseView.Builder(this)
//                .title("Second Queue Item")
//                .focusOn(txtInProcess)
//                .closeOnTouch(false)
//                .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
//                    @Override
//                    public void onViewInflated(@NonNull View view) {
//                        Button btn = (Button) view.findViewById(R.id.btn_action_1);
//                        btn.setOnClickListener(mClickListener);
//                        btn.setText("Next");
//                        TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
//                        TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
//                        txtView.setText("In process");
//                        //   subtitle.setText("Check me out to know your in/out timings.");
//                        //  subtitle.setText("Check me out to know if you are on time at work or On leave.");
//                        subtitle.setText("We’re sending your references to our system. Work In Progress…");
//                        Log.d("0", "onViewInflated: ");
//                        screenValue = 1;
//                    }
//                })
//                .build();

                                            fancyShowCaseView2 = new FancyShowCaseView.Builder(Dashboard.this)
                                                    .title("Third Queue Item")
                                                    .focusOn(txtMyLead)
                                                    .closeOnTouch(false)
                                                    .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
                                                        @Override
                                                        public void onViewInflated(@NonNull View view) {
                                                            Button btn = (Button) view.findViewById(R.id.btn_action_1);
                                                            //btn.setOnClickListener(mClickListener);
                                                            btn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    fancyShowCaseView2.hide();

                                                                }
                                                            });


                                                            btn.setText("Next");
                                                            TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
                                                            TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
                                                            txtView.setText("My References");
                                                            subtitle.setText("Yes, your reference has been accepted by our system! You can track the communication between us and your reference.");
                                                            Log.d("1", "onViewInflated: ");
                                                            screenValue = 1;
                                                        }
                                                    })

                                                    .build();

                                            fancyShowCaseView3 = new FancyShowCaseView.Builder(Dashboard.this)
                                                    .title("Fourth Queue Item")
                                                    .focusOn(txtRejected)
                                                    .closeOnTouch(false)
                                                    .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
                                                        @Override
                                                        public void onViewInflated(@NonNull View view) {
                                                            Button btn = (Button) view.findViewById(R.id.btn_action_1);
                                                            //btn.setOnClickListener(mClickListener);
                                                            btn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    fancyShowCaseView3.hide();

                                                                    SharedPref.savePreferences1(Dashboard.this, "F", "1");

//                                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//                                                                    SharedPreferences.Editor editor = pref.edit();
//                                                                    editor.putBoolean(IS_FIRST_RUN, false);
//                                                                    editor.commit();
                                                                }
                                                            });

                                                            btn.setText("Next");
                                                            TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
                                                            TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
                                                            txtView.setText("Rejected");
                                                            subtitle.setText("We’re sorry, this referral was rejected by the system because (Reason is here).");
                                                            Log.d("2", "onViewInflated: ");
                                                            screenValue = 2;
                                                        }
                                                    })

                                                    .build();


                                            fancyShowCaseView4 = new FancyShowCaseView.Builder(Dashboard.this)
                                                    .title("Fourth Queue Item")
                                                    .focusOn(txtNotInterested)
                                                    .closeOnTouch(false)
                                                    .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
                                                        @Override
                                                        public void onViewInflated(@NonNull View view) {
                                                            Button btn = (Button) view.findViewById(R.id.btn_action_1);
                                                            //btn.setOnClickListener(mClickListener);
                                                            btn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    fancyShowCaseView4.hide();

                                                                    Log.e("onClick: ", "444");

                                                                    SharedPref.savePreferences1(Dashboard.this, "F", "1");


//                                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//                                                                    SharedPreferences.Editor editor = pref.edit();
//                                                                    editor.putBoolean(IS_FIRST_RUN, false);
//                                                                    editor.commit();
                                                                }
                                                            });

                                                            btn.setText("Next");
                                                            TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
                                                            TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
                                                            txtView.setText("Not Interested");
                                                            subtitle.setText("It seems your reference is not interested for now. No problem, you can approach them again sometime!");


                                                            Log.d("2", "onViewInflated: ");
                                                            screenValue = 3;
                                                        }
                                                    })

                                                    .build();


                                            fancyShowCaseView5 = new FancyShowCaseView.Builder(Dashboard.this)
                                                    .title("Fourth Queue Item")
                                                    .focusOn(txtClients)
                                                    .closeOnTouch(false)
                                                    .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
                                                        @Override
                                                        public void onViewInflated(@NonNull View view) {
                                                            Button btn = (Button) view.findViewById(R.id.btn_action_1);
                                                            //btn.setOnClickListener(mClickListener);
                                                            btn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    fancyShowCaseView5.hide();

                                                                    Log.e("onClick: ", "555");

                                                                    SharedPref.savePreferences1(Dashboard.this, "F", "1");

//                                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//                                                                    SharedPreferences.Editor editor = pref.edit();
//                                                                    editor.putBoolean(IS_FIRST_RUN, false);
//                                                                    editor.commit();

                                                                }
                                                            });

                                                            btn.setText("Next");
                                                            TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
                                                            TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
                                                            txtView.setText("My Clients");
                                                            subtitle.setText("Congrats, your referred accounts are open!");

                                                            Log.d("2", "onViewInflated: ");
                                                            screenValue = 4;
                                                        }
                                                    })

                                                    .build();


                                            fancyShowCaseView6 = new FancyShowCaseView.Builder(Dashboard.this)
                                                    .title("Fourth Queue Item")
                                                    .focusOn(txtPayout)
                                                    .closeOnTouch(false)
                                                    .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
                                                        @Override
                                                        public void onViewInflated(@NonNull View view) {
                                                            Button btn = (Button) view.findViewById(R.id.btn_action_1);
                                                            //btn.setOnClickListener(mClickListener);

                                                            btn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    fancyShowCaseView6.hide();
                                                                    Log.e("onClick: ", "666");

                                                                    SharedPref.savePreferences1(Dashboard.this, "F", "1");

//                                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//                                                                    SharedPreferences.Editor editor = pref.edit();
//                                                                    editor.putBoolean(IS_FIRST_RUN, false);
//                                                                    editor.commit();


                                                                }
                                                            });

                                                            if (reff == false) {
                                                                btn.setText("Finish");

                                                            } else {
                                                                btn.setText("Next");

                                                            }

                                                            TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
                                                            TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
                                                            txtView.setText("My Earnings");
                                                            subtitle.setText("Wow, your referral income has started!");

                                                            Log.d("2", "onViewInflated: ");
                                                            screenValue = 5;

                                                            isfirstTime = 1;
                                                            Logics.setInstScreenVal(Dashboard.this, isfirstTime);

                                                        }
                                                    })

                                                    .build();

                                            fancyShowCaseView7 = new FancyShowCaseView.Builder(Dashboard.this)
                                                    .title("Fourth Queue Item")
                                                    .focusOn(txtReferral)
                                                    .focusOn(txtReferral)
                                                    .closeOnTouch(false)
                                                    .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
                                                        @Override
                                                        public void onViewInflated(@NonNull View view) {
                                                            Button btn = (Button) view.findViewById(R.id.btn_action_1);
                                                            //btn.setOnClickListener(mClickListener);

                                                            btn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    fancyShowCaseView7.hide();
                                                                    Log.e("onClick: ", "777");
                                                                    SharedPref.savePreferences1(Dashboard.this, "F", "1");

//                                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//                                                                    SharedPreferences.Editor editor = pref.edit();
//                                                                    editor.putBoolean(IS_FIRST_RUN, false);
//                                                                    editor.commit();
                                                                }
                                                            });
                                                            if (reff == true) {
                                                                btn.setText("Finish");


                                                            } else {
                                                                btn.setText("Next");

                                                            }
                                                            TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
                                                            TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
                                                            txtView.setText("Share your personalized referral link");
                                                            subtitle.setText("you can share your personalized referral link!");
                                                            Log.d("2", "onViewInflated: ");
                                                            screenValue = 6;
                                                            isfirstTime = 1;
                                                            Logics.setInstScreenVal(Dashboard.this, isfirstTime);

                                                        }
                                                    })

                                                    .build();

                                            // Log.e("isFirstRun", String.valueOf(isFirstRun()));

                                            if (SharedPref.getPreferences1(Dashboard.this, "F").equalsIgnoreCase("0")) {

                                                if (reff == true) {
                                                    fancyShowCaseQueue = new FancyShowCaseQueue()
                                                            .add(fancyShowCaseView0)
                                                            .add(fancyShowCaseView2)
                                                            .add(fancyShowCaseView3)
                                                            .add(fancyShowCaseView4)
                                                            .add(fancyShowCaseView5)
                                                            .add(fancyShowCaseView6)
                                                            .add(fancyShowCaseView7);
                                                    fancyShowCaseQueue.show();
                                                } else {
                                                    fancyShowCaseQueue = new FancyShowCaseQueue()
                                                            .add(fancyShowCaseView0)
                                                            .add(fancyShowCaseView2)
                                                            .add(fancyShowCaseView3)
                                                            .add(fancyShowCaseView4)
                                                            .add(fancyShowCaseView5)
                                                            .add(fancyShowCaseView6);
                                                    fancyShowCaseQueue.show();
                                                }
                                            }
                                        } catch (Exception e) {
                                            Log.e("handleMessage: ", e.getMessage());
                                        }
                                    }
                                } else {
                                    startAlert();
                                }
                            }
                        } else {
                            String Message = jsonObject.getString("Message");
                            AlertDialogClass.ShowMsg(Dashboard.this, Message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // AlertDialogClass.ShowMsg(Dashboard.this,e.getMessage());

                    }
                }
                break;
            }


        }
    }

    @Override
    public void onBackPressed() {
        onExit();

    }

    private void onExit() {

//        int version = BuildConfig.VERSION_CODE;

        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Dashboard.super.onBackPressed();
                        //finish();
                        ExitActivity.exitApplication(Dashboard.this);


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        dialogInterface.cancel();
                    }
                });
        alert.show();

    }


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        if (!Const.isSocketConnected){
//            imgConnection.setBackground(getResources().getDrawable(R.drawable.ic_up_and_down_arrows_symbol_red));
//        }else {
//
//            imgConnection.setBackground(getResources().getDrawable(R.drawable.ic_up_and_down_arrows_symbol));
//        }
//        new SendTOServer(Dashboard.this,Dashboard.this, Const.MSGFETCHDASHBOARD,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//
//        Log.d("Restart", "onRestart: ");
//    }


//    private void versionPopup(){
//
//
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(this)
//                .setTitle("New Version Available")
//                .setMessage("Latest version of VPP app is available to download.")
//                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//        alert.show();
//    }


    public static View getToolbarNavigationIcon(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = TextUtils.isEmpty(toolbar.getNavigationContentDescription());
        String contentDescription = !hadContentDescription ? (String) toolbar.getNavigationContentDescription() : "navigationIcon";
        toolbar.setNavigationContentDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setNavigationContentDescription ensures its existence
        View navIcon = null;
        if (potentialViews.size() > 0) {
            navIcon = potentialViews.get(0);

            //navigation icon is ImageButton

        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setNavigationContentDescription(null);
        return navIcon;
    }

//    View.OnClickListener mClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Log.d("", "onClick: ");
//
//            if (screenValue == 0) {
//                fancyShowCaseView0.hide();
//                // fancyShowCaseQueue.cancel(true);
//            } else if (screenValue == 1) {
//                fancyShowCaseView2.hide();
//                // fancyShowCaseQueue.cancel(true);
//            } else if (screenValue == 2) {
//                fancyShowCaseView3.hide();
//                // fancyShowCaseQueue.cancel(true);
//            } else if (screenValue == 3) {
//                fancyShowCaseView4.hide();
//                // fancyShowCaseQueue.cancel(true);
//            } else if (screenValue == 4) {
//                fancyShowCaseView5.hide();
//                // fancyShowCaseQueue.cancel(true);
//            } else if (screenValue == 5) {
//                fancyShowCaseView6.hide();
//                // fancyShowCaseQueue.cancel(true);
//
//                if (!ranBefore) {
//
//                    // first time
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putBoolean("RanBefore", true);
//                    editor.commit();
//                }
//
//                Log.e("onClick: ", "aa");
//            }
//
//
//            /*else if (screenValue == 5) {
//                fancyShowCaseView6.hide();
//                Log.e("onClick: ", "bb");
//
//                if (!ranBefore) {
//
//                    // first time
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putBoolean("RanBefore", true);
//                    editor.commit();
//                }
//                // fancyShowCaseQueue.cancel(true);
//            } *//*else if (screenValue == 7) {
//                fancyShowCaseView7.hide();
//
//                // fancyShowCaseQueue.cancel(true);
//            }*/
//            // fancyShowCaseQueue.cancel(true);
//        }
//    };

    @Override
    protected void onResume() {
        Log.e("onResume: ", "caalleed");


        super.onResume();
    }

    private void startAlert() {
        android.app.AlertDialog.Builder alertBuild = new android.app.AlertDialog.Builder(this)
                .setTitle("VPP App Update")
                .setMessage("VPP App recommends that you update to the latest version.")
                .setCancelable(false)
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

    private void startAlertesygn() {
        Log.e("startAlertesygn: ", "hiii");
        android.app.AlertDialog.Builder alertBuild = new android.app.AlertDialog.Builder(this)
                .setTitle("E-sign Pending")
                .setMessage("E-sign is a mandatory part to complete your registration process, it will just take a few minutes !\n" +
                        "Keep your Aadhar number ready, you will receive OTP on your registered mobile number from UIDAI..")
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dialogInterface.cancel();
                        Intent intent = new Intent(Dashboard.this, PhotoVideoSignatureActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert = alertBuild.create();
        alert.show();
    }


    public void startAlertPopup(Context context, View linearLayout1, int ii) {
        String[] descriptionData = {"Documents", "Payment", "Esign"};

        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_doc,
                null);
        builder.setView(customLayout);

        builder.setCancelable(false);


        Button button = (Button) customLayout.findViewById(R.id.btn_proceed);
        StateProgressBar stateProgressBar = (StateProgressBar) customLayout.findViewById(R.id.progress_bar);
        stateProgressBar.setStateDescriptionData(descriptionData);

        stateProgressBar.setStateDescriptionTypeface("fonts/RobotoSlab-Light.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/Questrial-Regular.ttf");


        if (ii == 0) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        } else if (ii == 1) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        } else if (ii == 2) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        } else if (ii == 3) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (conditionCheck1Doc == true) {
                    Intent intent = new Intent(Dashboard.this, UploadDocScreen.class);
                    intent.putExtra(Const.from, Const.doc);
                    intent.putExtra(Const.bankstatus, is_bank_verified);
                    intent.putExtra(Const.adharstatus, is_adhar_verified);
                    intent.putExtra(Const.panstatus, is_pan_verified);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //payemnt..
                } else if (conditionCheck2Payment == true) {
                    Intent intent = new Intent(Dashboard.this, UpiPayment.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //esign..
                } else if (conditionCheck3Esign == true) {
                    Intent intent = new Intent(Dashboard.this, PhotoVideoSignatureActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });


        // add a button
        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    public void startAlertPopup1(Context context, View linearLayout1, int ii) {
        String[] descriptionData = {"Documents", "Payment", "Esign"};

        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_doc1,
                null);
        builder.setView(customLayout);

        builder.setCancelable(false);
        Button button = (Button) customLayout.findViewById(R.id.btn_proceed);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (conditionCheck1Doc == true) {
                    Intent intent = new Intent(Dashboard.this, UploadDocScreen.class);
                    intent.putExtra(Const.from, Const.doc);
                    intent.putExtra(Const.bankstatus, is_bank_verified);
                    intent.putExtra(Const.adharstatus, is_adhar_verified);
                    intent.putExtra(Const.panstatus, is_pan_verified);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //payemnt..
                } else if (conditionCheck2Payment == true) {
                    Intent intent = new Intent(Dashboard.this, UpiPayment.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //esign..
                } else if (conditionCheck3Esign == true) {
                    Intent intent = new Intent(Dashboard.this, PhotoVideoSignatureActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });


        // add a button
        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }


    public static void startAlertPopup2(Context context, View linearLayout1, int ii) {

        String[] descriptionData = {"Documents", "Payment", "Esign"};

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = layoutInflater.inflate(R.layout.layout_doc, null);
        //instantiate popup window
        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setOutsideTouchable(true);

        StateProgressBar stateProgressBar = (StateProgressBar) customView.findViewById(R.id.progress_bar);
        stateProgressBar.setStateDescriptionData(descriptionData);

        stateProgressBar.setStateDescriptionTypeface("fonts/RobotoSlab-Light.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/Questrial-Regular.ttf");


        if (ii == 0) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        } else if (ii == 1) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        } else if (ii == 2) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        } else if (ii == 3) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
        }
        //display the popup window
        new Handler().postDelayed(new Runnable() {

            public void run() {
                popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);
            }

        }, 200L);

        if (!popupWindow.isShowing()) {
            customView.setVisibility(View.VISIBLE);
        } else {
            customView.setVisibility(View.GONE);
        }

//        Bundle bundle = new Bundle();
//        bundle.putString("id","");
//        GstDlg resourse_share=new GstDlg();
//        resourse_share.show(fm, "Dialog Fragment");
//        resourse_share.setArguments(bundle);

    }


    private void startAlertPopup1() {
        android.app.AlertDialog.Builder alertBuild = new android.app.AlertDialog.Builder(this)
                .setTitle("Documents Pending ! ")
                .setMessage("Documents are mandatory part to complete your registration process, it will just take a few minutes !\n please complete.")
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dialogInterface.cancel();
                        //
                        //doc..
                        if (conditionCheck1Doc == true) {
                            Intent intent = new Intent(Dashboard.this, UploadDocScreen.class);
                            intent.putExtra(Const.from, Const.doc);
                            intent.putExtra(Const.bankstatus, is_bank_verified);
                            intent.putExtra(Const.adharstatus, is_adhar_verified);
                            intent.putExtra(Const.panstatus, is_pan_verified);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            //payemnt..
                        } else if (conditionCheck2Payment == true) {
                            Intent intent = new Intent(Dashboard.this, UpiPayment.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            //esign..
                        } else if (conditionCheck3Esign == true) {
                            Intent intent = new Intent(Dashboard.this, PhotoVideoSignatureActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                    }
                });
        android.app.AlertDialog alert = alertBuild.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                // If the update is cancelled by the user,
                // you can request to start the update again.
                // inAppUpdateManager.checkForAppUpdate();

                Log.d(TAG, "Update flow failed! Result code: " + resultCode);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public static void ProgressDlgConnectSocket(Context context, final ConnectionProcess connectionProcess, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

        sweetAlertDialog.setTitleText(msg)
//                .setContentText("Socket Not Available")
                .setConfirmText("Reconnect!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                        connectionProcess.ConnectToserver(connectionProcess);
                    }
                })
                .show();
        sweetAlertDialog.setCancelable(false);
//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(@NonNull InstallState state) {

            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                showCompletedUpdate();
            }
        }
    };

    private void showCompletedUpdate() {

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "New App Ready !", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppupdatemanager.completeUpdate();
            }
        });
        snackbar.show();
    }

    @Override
    protected void onStop() {
        if (mAppupdatemanager != null)
            mAppupdatemanager.unregisterListener(installStateUpdatedListener);
        super.onStop();
    }

    class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + Dashboard.this.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    //show anything

                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this, R.style.MyAlertDialogStyle);

                    alertDialogBuilder.setMessage("There is new version available for download! we noticed you are still using older version. Please update the app in order to proceed")
//                            .setMessage("Want to update app?")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                               /* final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    Toast.makeText(getApplicationContext(), "App is in BETA version cannot update", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }*/


//                                mAppupdatemanager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
//                                    @Override
//                                    public void onSuccess(AppUpdateInfo result) {
//                                        if (result.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
//                                            try {
//                                                mAppupdatemanager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE,Dashboard.this,REQ_CODE_VERSION_UPDATE);
//                                            } catch (IntentSender.SendIntentException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//
//                                    }
//                                });


                                    mAppupdatemanager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                                        @Override
                                        public void onSuccess(AppUpdateInfo result) {
                                            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                                                //Toast.makeText(Dashboard.this, "UPDATE_AVAILABLE", Toast.LENGTH_SHORT).show();

                                                try {
                                                    mAppupdatemanager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, Dashboard.this, REQ_CODE_VERSION_UPDATE);
                                                } catch (IntentSender.SendIntentException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }

//                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                                                try {
//                                                    Toast.makeText(getApplicationContext(), "App is in BETA version cannot update", Toast.LENGTH_SHORT).show();
//                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                                                } catch (ActivityNotFoundException anfe) {
//                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                                                }

                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(Dashboard.this, "failure called : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
//                                new MyAsyncTask().execute();
                                }
                            })
                            .setIcon(R.drawable.vpp_logo)
                            .setCancelable(false)
                            .setTitle("Update Your App Now")
                            .show();

                }

            }

            Log.e("update", "Current version " + currentVersion + "playstore version " + onlineVersion);

        }

    }


    private void mpinPopupWindow(Context c) {
        try {
            View m_view = LayoutInflater.from(c).inflate(R.layout.popup_banner, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
            dialogBuilder.setView(m_view);

            ImageView popupimage = m_view.findViewById(R.id.popupimage);
            TextView textView = m_view.findViewById(R.id.textView);
            ImageView imageViewCncl = m_view.findViewById(R.id.buttonImageclose);
            imageViewCncl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPref.savePreferences(Dashboard.this, "PopUpShow", "0");
                    SharedPref.savePreferences(Dashboard.this, "popup_udpated_date",
                            SharedPref.getPreferences(Dashboard.this, "popup_udpated_date1"));
                    dialogLockPIN.dismiss();
                    dialogLockPIN.cancel();
                }
            });

            if (!SharedPref.getPreferences(c, "msgPopup").equalsIgnoreCase("")) {
                textView.setText(SharedPref.getPreferences(c, "msgPopup"));
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.GONE);
            }

            HttpsTrustManager.allowAllSSL();

            dialogLockPIN = dialogBuilder.create();
            dialogLockPIN.setCancelable(false);
            dialogLockPIN.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
//            dialogLockPIN.show();
            dialogLockPIN.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogLockPIN.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    //dialogLockPIN = null;
                }
            });


            Picasso.with(Dashboard.this).load(SharedPref.getPreferences(c, "linkPopup")).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).into(popupimage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("TAG", "onSuccess");

                            if (!((Activity) Dashboard.this).isFinishing()) {
                                dialogLockPIN.show();
                            }
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            //  VenturaException.Print(e);
        }
    }

    private void mRevenueSharingPopupWindow(Context c) {
        try {
            View m_view = LayoutInflater.from(c).inflate(R.layout.popup_sharingrevenue, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
            dialogBuilder.setView(m_view);
            progress_bar = m_view.findViewById(R.id.progress_bar);
            TextView txtIagree = m_view.findViewById(R.id.txtIagree);
            TextView checkbox_sharing = m_view.findViewById(R.id.checkbox_sharing);
            ImageView imageViewCncl = m_view.findViewById(R.id.buttonImageclose);
            imageViewCncl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogLockPIN1.dismiss();
                    dialogLockPIN1.cancel();
                }
            });

            txtIagree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogLockPIN1.dismiss();
                    dialogLockPIN1.cancel();
                    sendRSharingData("yes", "", "");

                    TastyToast.makeText(getApplicationContext(), "Agreed...", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                    // Toast.makeText(c, "Agreed...", Toast.LENGTH_SHORT).show();
                }
            });

            // shiva
            checkbox_sharing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progress_bar.setVisibility(View.VISIBLE);
                    mRevenueSharingPopupWindow2(Dashboard.this, progress_bar);
                }
            });

            dialogLockPIN1 = dialogBuilder.create();
            dialogLockPIN1.setCancelable(false);
            dialogLockPIN1.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            if (!dialogLockPIN1.isShowing()) {
                dialogLockPIN1.show();
            }
            dialogLockPIN1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogLockPIN1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    //dialogLockPIN = null;
                }
            });

        } catch (Exception e) {
            //  VenturaException.Print(e);
            Log.e("zzzzz", e.getMessage());
        }
    }

    private void mRevenueSharingPopupWindow2(Context c, ProgressBar progress_bar) {
        try {

            ImageView mImageView;

            View m_view = LayoutInflater.from(c).inflate(R.layout.popup_bannerrs, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
            dialogBuilder.setView(m_view);
//            final EditText m_mpinTextEntry = m_view.findViewById(R.id.mpintext);
//            textView = m_view.findViewById(R.id.errorText) ;

            // zoomControls = m_view.findViewById(R.id.simpleZoomControl); // initiate a ZoomControls


            btnzIn = m_view.findViewById(R.id.btnZoomIn);
            btnzOut = m_view.findViewById(R.id.btnZoomOut);


            btnzIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                    popupimage.startAnimation(animZoomIn);
                }
            });
            btnzOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                    popupimage.startAnimation(animZoomOut);
                }
            });


            popupimage = m_view.findViewById(R.id.popupimage);
            TextView textView = m_view.findViewById(R.id.textView);
            ImageView imageViewCncl = m_view.findViewById(R.id.buttonImageclose);
            imageViewCncl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogLockPIN.dismiss();
                    dialogLockPIN.cancel();
                }
            });

            dialogLockPIN = dialogBuilder.create();
            dialogLockPIN.setCancelable(true);
            dialogLockPIN.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            dialogLockPIN.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogLockPIN.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
//                    dialogLockPIN = null;
                }
            });

            Picasso.with(Dashboard.this).load("https://crm.ventura1.com/VPP%20mobile%20pop%20up.jpg").memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).into(popupimage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("TAG", "onSuccess");

                            if (!((Activity) Dashboard.this).isFinishing()) {
                                dialogLockPIN.show();
                                progress_bar.setVisibility(View.GONE);

                            }
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                            progress_bar.setVisibility(View.GONE);

                        }
                    });

        } catch (Exception e) {
            //  VenturaException.Print(e);
        }

    }

    private String getVersionInfo() {

        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            AlertDialogClass.ShowMsg(Dashboard.this, e.getMessage());
        }

        Log.d("versionName", "getVersionInfo: " + versionName);
        return versionName;

    }


    private void sendRSharingData(String revenuesharing, String created_date, String updated_date) {

        try {
            JSONObject jsonObject = new JSONObject();
            String vppid = Logics.getVppId(Dashboard.this);
            jsonObject.put("vpp_id", vppid);
            jsonObject.put("revenuesharing", revenuesharing);
            jsonObject.put("created_date", created_date);
            jsonObject.put("updated_date", "");
            AlertDialogClass.PopupWindowShow(Dashboard.this, mainLayout);

            Log.e("jsonjson", String.valueOf(jsonObject));

            byte[] data = jsonObject.toString().getBytes();
            new SendTOServer(this, this, Const.MSG_POSTREVENUESHARING, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            AlertDialogClass.PopupWindowDismiss();
            Views.toast(this, e.getMessage());
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(Dashboard.this, e.getMessage());
            //btnSubmit.setEnabled(true);

        }
    }

    // this redirects all touch events in the activity to the gesture detector
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            matrix.setScale(scaleFactor, scaleFactor);
            popupimage.setImageMatrix(matrix);
            return true;
        }
    }

//

    //temp blocked
//    public void personlized_link_for_accnt_opnApicall(String branchcode) {
//        //getMobile_1
//        try {
////            AlertDialogClass.PopupWindowShow(Dashboard.this, mainLayout);
//            JSONObject jsonObject = new JSONObject();
//            //   jsonObject.put("vpp_id", "72001");
////            jsonObject.put("vpp_id", "656010");
//            String vpp_id = Logics.getVppId(Dashboard.this);
//            jsonObject.put("vpp_id", "656263");
////             jsonObject.put("mobile_no","9723179601");
//            jsonObject.put("branchcode", branchcode);
//            byte[] data = jsonObject.toString().getBytes();
//
//            Log.e("link generate", jsonObject.toString());
//            new SendTOServer(Dashboard.this, requestSent, Const.MSGPERSONALIZED_LINK_FOR_ACCOUNT_OPENING_NEW, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
//
//
//    }
    public void personlized_link_for_accnt_opnApicall() {

        //getMobile_1
        try {
//            AlertDialogClass.PopupWindowShow(Dashboard.this, mainLayout);
            JSONObject jsonObject = new JSONObject();
            //   jsonObject.put("vpp_id", "72001");
//            jsonObject.put("vpp_id", "656010");
            String vpp_id = Logics.getVppId(Dashboard.this);
            jsonObject.put("vpp_id", vpp_id);
             jsonObject.put("mobile_no",Logics.getProfile(Dashboard.this).get(3));
//            jsonObject.put("branchcode", branchcode);
            byte[] data = jsonObject.toString().getBytes();

            Log.e("mobileNO",Logics.getProfile(Dashboard.this).get(3));

            Log.e("link generate", jsonObject.toString());
            new SendTOServer(Dashboard.this, requestSent, Const.MSGPERSONALIZED_LINK_FOR_ACCOUNT_OPENING, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

}
