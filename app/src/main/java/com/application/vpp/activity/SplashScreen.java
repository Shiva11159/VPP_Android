package com.application.vpp.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Database.DatabaseHelper;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Interfaces.APiValidateAccount;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.Interfaces.SliderImagesPojo;
import com.application.vpp.NetworkCall.APIClient;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Timer;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity implements RequestSent, ConnectionProcess {
    ConnectionProcess connectionProcess;
    public static Handler handlerSplash;
    String title, message;
    DatabaseHelper dbh;
    TextView textView_version;
    Timer timer;
    boolean connected = false;
    boolean popupdetected = false;
    boolean ProgressDlgSomethingIssue = false;
    boolean popupinternet = false;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 500;
    Context context;
    public static final String PREFS_NAME = "LoginPrefs";

    DatabaseHelper databaseHelper;

    static ArrayList<InserSockettLogs> inserSockettLogsArrayList;
    APiValidateAccount apiService1;
    ArrayList<SliderImagesPojo> arrayListSlider;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        databaseHelper = new DatabaseHelper(SplashScreen.this);
        try {
            textView_version = findViewById(R.id.textView_version);
            textView_version.setText("v" + getVersionInfo());
            connectionProcess = (ConnectionProcess) this;

            arrayListSlider = new ArrayList<>();

            apiService1 = new APIClient().getClientsocketConn(SplashScreen.this).create(APiValidateAccount.class);

            //inserSockettLogsArrayList=new ArrayList<>();
            inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList, "SocketLogs", SplashScreen.this);

//        if (inserSockettLogsArrayList != null) {
//            if (inserSockettLogsArrayList.size() > 0) {
////                Log.e("arrayListsize", String.valueOf(inserSockettLogsArrayList.size()));
//                InsertLogsMethod(inserSockettLogsArrayList);
//            }
//        }

            context = this;
            FirebaseMessaging.getInstance().subscribeToTopic("news");
            SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");

            SplashScreen.handlerSplash = null;
            Dashboard.handlerDashboard = null;
            DiscripancyActivity.handlerDiscripancy = null;
            Profile.handlerProfile = null;
            handlerSplash = new ViewHandler();

//        String[] _permission = {Manifest.permission.READ_PHONE_STATE};
//        if (!checkPermission(_permission)) {
//            checkPermission(_permission, tag -> {
//                afterPermission();
//            });
//        } else {
//            afterPermission();
//        }

            afterPermission();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void afterPermission() {
        //deviceId();
//        handlerSplash = new ViewHandler();

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                if (key.equals("title")) {
                    title = getIntent().getExtras().getString(key);
                } else if (key.equals("message")) {
                    message = getIntent().getExtras().getString(key);
                }
            }

        }
        if (title != null && message != null) {
            dbh = new DatabaseHelper(this);
            //   dbh.insertNotificaton(title,message);
        }
        //


        try {
//            KeyPair keyPair = RSA.generateKeyPair();
//            Log.d("keypair.public", "onCreate: "+keyPair.getPublic());
//            Log.d("keypair.private", "onCreate: "+keyPair.getPrivate());
//            String base641pub =  Base64.encodeToString(keyPair.getPublic().getEncoded(),Base64.DEFAULT);
//            Log.d("base641public", "onCreate: "+base641pub);
//            String base641priv =  Base64.encodeToString(keyPair.getPrivate().getEncoded(),Base64.DEFAULT);
//            Log.d("base641priv", "onCreate: "+base641priv);
//            PublicKey pubKey = keyPair.getPublic();
//            Log.d("public", "onCreate: "+pubKey);
//            String base642 =  Base64.encodeToString(pubKey.getEncoded(),Base64.DEFAULT);
//            Log.d("base642", "onCreate: "+base642);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestSent(int value) {

    }

    @Override
    public void connected() {

       /* String newVersion="";
        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + this.getPackageName()+ "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

//        Log.e("newVersion", newVersion);
      //  Intent intent = new Intent(getApplicationContext(), DashoboardDesign.class);
      //  startActivity(intent);

//        SharedPref.insertSocketLogs(Logics.getVppId(SplashScreen.this),
//                "2",
//                Methods.getVersionInfo(SplashScreen.this),
//                Methods.getlogsDateTime(),
//                "splashscreen",4
//                Connectivity.getNetworkState(getApplicationContext()),
//                SplashScreen.this);
//
//

//        Intent intent = new Intent(SplashScreen.this, SignupScreen2.class);
//        //               Intent intent = new Intent(SplashScreen.this, PhotoVideoSignatureActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        //   Intent intent = new Intent(getApplicationContext(), DashoboardDesign.class);
//        startActivity(intent);


        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(SplashScreen.this),
                            "1",
                            Methods.getVersionInfo(SplashScreen.this),
                            Methods.getlogsDateTime(), "SplashScreen",
                            Connectivity.getNetworkState(getApplicationContext()),
                            SplashScreen.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(SplashScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("arrayListsize", String.valueOf(inserSockettLogsArrayList.size()));
                JSONArray jsonArray = new JSONArray();

                try {
                    for (int i = 0; i < inserSockettLogsArrayList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("brcode", inserSockettLogsArrayList.get(i).getVpp_id());
                        jsonObject.put("flag", inserSockettLogsArrayList.get(i).getFlag());
                        jsonObject.put("app_version", inserSockettLogsArrayList.get(i).getApp_version());
                        jsonObject.put("log_date", inserSockettLogsArrayList.get(i).getLog_date());
                        jsonObject.put("screen_name", inserSockettLogsArrayList.get(i).getScreenname());
                        jsonObject.put("internet_connection", inserSockettLogsArrayList.get(i).getIsinternetAvailable());
                        Log.e("jsonObject00", jsonObject.toString());
                        jsonArray.put(jsonObject);
                    }

//                    Log.e("jsonObject11", jsonObject.toString());
                    Log.e("jsonArray11", jsonArray.toString());

                    InsertLogsMethod(jsonArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exexex", e.getMessage());
                }
            }
        }
        try {
            String vpp_id = Logics.getVppId(SplashScreen.this);
            if (vpp_id == null) {
                JSONObject jsonObject = new JSONObject();
                String versionName = getVersionInfo();
                jsonObject.put("versionName", versionName);
                jsonObject.put("vpp_id", "");
                jsonObject.put("panno", "");
                jsonObject.put("updated_date", "");
                jsonObject.put("popup_updated_date", "");
                byte[] data = jsonObject.toString().getBytes();
                Log.e("splashScreen111", "connected: " + jsonObject.toString());

                new SendTOServer(SplashScreen.this, SplashScreen.this, Const.MSGVERIFYVPP, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                ///call here  starting

            } else {
                String panno = Logics.getPanNo(SplashScreen.this);
                if (panno == null) {
                    panno = "";
                }
                if (vpp_id == null) {
                    vpp_id = "";
                }

                //Add method to check version and if is deactivated
                //in iresponse check if is deacticated stop. and if version not match show popup.If all ok send to dashboard
                JSONObject jsonObject = new JSONObject();
                String versionName = getVersionInfo();
                jsonObject.put("versionName", versionName);
                jsonObject.put("vpp_id", vpp_id);
                jsonObject.put("panno", panno);
                jsonObject.put("updated_date", SharedPref.getPreferences(SplashScreen.this, "slider_udpated_date"));
                jsonObject.put("popup_updated_date", SharedPref.getPreferences(SplashScreen.this, "popup_udpated_date"));
                byte[] data = jsonObject.toString().getBytes();
                Log.e("splashScreen", "connected: " + jsonObject.toString());
                new SendTOServer(SplashScreen.this, SplashScreen.this, Const.MSGVERIFYVPP, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        connected = false;

        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    if (Connectivity.getNetworkState1(getApplicationContext())) {
                        Views.ProgressDlgConnectSocket(SplashScreen.this, connectionProcess, "Server Not Available");
                        popupdetected = true;
                    } else
//                        TastyToast.makeText(getApplicationContext(), "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        ProgressDlgNoInternet(SplashScreen.this, "Please make sure that you are connected to the internet");
                    popupinternet = true;
                } else {

                    Toast.makeText(SplashScreen.this, "nullsplash", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
//        pDialog.dismiss();
//        pDialog.cancel();
//        connected=true;
        connected = false;
        popupdetected = false;
        popupinternet = false;
        Views.SweetAlert_NoDataAvailble(SplashScreen.this, "Internet Not Available");
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

        popupdetected = false;
        popupinternet = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState1(getApplicationContext()))
                    new ConnectTOServer(SplashScreen.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (Const.dismiss == true) {
//                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (Connectivity.getNetworkState(getApplicationContext())){
//                                            Views.ProgressDlgConnectSocket(SplashScreen.this, connectionProcess, "Server Not Available");
//                                            popupinternet=true;
//                                        }
//                                        else{
//
//                                            popupinternet=true;
//                                            TastyToast.makeText(getApplicationContext(),"No Internet",TastyToast.LENGTH_LONG,TastyToast.ERROR);
//                                        }
////                                            Views.SweetAlert_NoDataAvailble(SplashScreen.this, "No Internet");
//                                    }
//                                });
//                            }
//                        }
//                    }
//                }, 1000);
            }
        });
    }

    @Override
    public void SocketDisconnected() {
//        pDialog.dismiss();
//        pDialog.cancel();
        connected = false;

        Log.e("SocketDisconnectedspla", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    if (Connectivity.getNetworkState1(getApplicationContext()))
                        Views.ProgressDlgConnectSocket(SplashScreen.this, connectionProcess, "Server Not Available");
                    else
//                        TastyToast.makeText(getApplicationContext(), "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        ProgressDlgNoInternet(SplashScreen.this, "Please make sure that you are connected to the internet");

                } else {
                    TastyToast.makeText(getApplicationContext(), "nullSocketsplash", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    //  Toast.makeText(SplashScreen.this, "nullSocketsplash", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = Base64.decode(key.getBytes(), Base64.NO_WRAP);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePublic(X509publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static PrivateKey getPrivateKey(String key) {
        try {
            byte[] byteKey = Base64.decode(key.getBytes(), Base64.NO_WRAP);
            PKCS8EncodedKeySpec X509publicKey = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePrivate(X509publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.arg1 == Const.MSGVERIFYVPP) {

                int status = 0;
                int downloadApk = 0;
                int isDeactivated;
                String slider_udpated_date = "";

                try {
                    String data = (String) msg.obj;
                    Log.e("DATA_", data);
                    JSONObject jsonObject = new JSONObject(data);
                    status = jsonObject.getInt("status");
                    downloadApk = jsonObject.getInt("downloadApk");
                    isDeactivated = jsonObject.getInt("isDeactivated");

//                    String simnum = "";
//                    String imei = "";

                    if (status == 1) {
                        String vpp_id = Logics.getVppId(SplashScreen.this);

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

                                // temp comment...
                                JSONArray popupdata = jsonObject.getJSONArray("popupdata");
                                if (popupdata.length()==0){
                                    SharedPref.savePreferences(SplashScreen.this,"PopUpShow","0");
                                }else {
                                    SharedPref.savePreferences(SplashScreen.this,"PopUpShow","1");
                                    String linkPopup= popupdata.getJSONObject(0).getString("link");
                                    String msg1= popupdata.getJSONObject(0).getString("msg");

                                    if (!msg1.equalsIgnoreCase("")){
                                        SharedPref.savePreferences(SplashScreen.this, "msgPopup", msg1);
                                    }else {
                                        SharedPref.savePreferences(SplashScreen.this, "msgPopup", "");
                                    }

                                    String popup_udpated_date = jsonObject.getString("popup_udpated_date");
                                    SharedPref.savePreferences(SplashScreen.this, "popup_udpated_date1", popup_udpated_date);
                                    SharedPref.savePreferences(SplashScreen.this, "linkPopup", linkPopup);

                                }

                                /// slider block ...

                                JSONArray jsonArray = jsonObject.getJSONArray("sliderdata");
                                if (jsonArray.length() == 0) {
                                    //use local sqlite dataBase ..
                                } else {

                                    slider_udpated_date = jsonObject.getString("slider_udpated_date");
                                    SharedPref.savePreferences(SplashScreen.this, "slider_udpated_date", slider_udpated_date);
                                    //insert into sqlite dateBase

//                                    insert into database;
                                    arrayListSlider.clear();
                                    databaseHelper.delete();
                                    String link = "";
                                    for (int z = 0; z < jsonArray.length(); z++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(z);
                                        link = jsonObject1.getString("link");
                                        SliderImagesPojo sliderImages = new SliderImagesPojo(link);
                                        arrayListSlider.add(sliderImages);
                                    }
                                    databaseHelper.InsertSliderImages(arrayListSlider);
                                    //insert into db ..
                                }

                                //vpp data block is empty when vpp is new ....

                                JSONObject vppdata = jsonObject.getJSONObject("vppdata");
                                int x = vppdata.length();


                                if (vppdata.length() == 0) {

                                    int isBankVerified = Logics.getisBankVerified(SplashScreen.this);
                                    int isPanVerified = Logics.getisPanVerified(SplashScreen.this);
                                    int isMobile = Logics.getisMobile_V(SplashScreen.this);
                                    int isEmail = Logics.getisEmail_V(SplashScreen.this);
                                    int isReg = Logics.getisRegisterd(SplashScreen.this);

                                    if (isBankVerified == 1) {
//                                         Toast.makeText(SplashScreen.this, "You have Completed Steps upto Bank Verfication", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), SignupScreen2.class);
                                        startActivity(intent);
                                    } else if (isPanVerified == 1) {
//                                        Toast.makeText(getApplicationContext(), "You have Completed Steps upto Pan Verification", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), BankValidation.class);
                                        startActivity(intent);
                                    } else if (isMobile == 1 && isEmail == 1 && isReg == 1) {
                                        // Toast.makeText(getApplicationContext(), "Mobile and Email Id Already Verified", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), PanValidation.class);
                                        startActivity(intent);
                                    }  else {
                                        boolean pg = Logics.getSharedPrefFromTag("permission_granted", false, context);
                                        if (pg != true) {
                                            Intent intent = new Intent(getApplicationContext(), PermissionActivity.class).putExtra("from", "");
//                    intent.putExtra("from", "splashscreen");
                                            startActivity(intent);
                                        } else {

                                            if(x==0){
                                                Intent intent = new Intent(SplashScreen.this, SliderImages.class).putExtra("from","splashscreen");
                                                startActivity(intent);
                                            }else {
                                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                                //     Intent intent = new Intent(SplashScreen.this, PhotoVideoSignatureActivity.class);
                                                intent.putExtra("from", "splashscreen");
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                }else {

                                    //if vpp is there... will call here .

                                    String name = vppdata.getString("name");
                                    String city = vppdata.getString("city");
                                    String mobile = vppdata.getString("mobile");
                                    String email = vppdata.getString("email");
                                    String vppid = vppdata.getString("vpp_id");
                                    String pan_no = vppdata.getString("pan_no");
                                    String bankAccNo = vppdata.getString("bankAccNo");
                                    String state = vppdata.getString("state");

                                    String accnt_opn_link = vppdata.getString("accnt_opn_link");

                                    Log.e("spl_accnt_opn_link",accnt_opn_link);

                                    Logics.setPLFOA(SplashScreen.this,accnt_opn_link.trim());
                                   // Logics.setmobileNO_lINK(SplashScreen.this,mobile);

                                /*{ {"isDeactivated":0,"vppdata":{"area":"sainagar road Panvel","is_email":1,"is_doc_v":0,
                                "city":"Head Office","isBankVerified":1,"vpp_bank_name":"Mr  PRAVIN LAXMAN DI","mobile":"9975153610",
                                "pan_no":"ARAPD2934M","refcode":"11145","bankAccNo":"20149541044","vpp_pan_name":"Shri PRAVIN LAXMAN DITI",
                                "ref":"Ventura Partner","pin":"410206","dob":"19-08-1994","proffession":"student","name":"Pravin Laxman Diti",
                                "is_pay_p":0,"house_no":"203, uptown avenue","is_otp_v":1,"state":"Maharashtra","ifsc":"SBIN0003667",
                                "email":"PRAVINDI.BOOKONSPOT@GMAIL.COM","vpp_id":"11145"},"downloadApk":0,"message":"Existing User","status":1}  }*/

                                   /* {"popup_udpated_date":"","isDeactivated":0,"vppdata":{"area":"HHJJUJU","is_email":1,"is_doc_v":1,"city":"Head Office",
                                            "isBankVerified":1,"vpp_bank_name":"SHIVAKUMAR LAXMIRAJA","mobile":"9867328538","pan_no":"AZTPT4416B","refcode":"",
                                            "bankAccNo":"149301510436","vpp_pan_name":"SHRI SHIVAKUMAR LAXMIRAJAM TUMMA","ref":"Ventura Partner","pin":"421305",
                                            "dob":"1998-11-12","proffession":"","name":"Shivakumar Laxmirajam Tumma","is_pay_p":0,"house_no":"5YHJ","is_otp_v":1,
                                            "state":"ANDAMAN & NICOBAR","ifsc":"ICIC0001493",
                                            "email":"SHIVA.TUMMA@VENTURA1.COM","vpp_id":"11159"},"iswrite":"0","slider_udpated_date":"","popupdata":[],
                                        "downloadApk":0,"message":"Existing User","sliderdata":[],"status":1}*/


                                    SharedPref.savePreferences(SplashScreen.this, "state", state);
                                   // Logics.setPaymentStatus(SplashScreen.this, vppdata.getString("is_pay_p"));
                                    SharedPref.savePreferences(SplashScreen.this, "bankAccNo", bankAccNo);
                                    Logics.setVppDetails(SplashScreen.this, name, mobile, email, city, vppid, pan_no);


                                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                    if (settings.getString("logged", "").toString().equals("logged")) {
                                        Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        //   Intent intent = new Intent(getApplicationContext(), DashoboardDesign.class);
                                        startActivity(intent);
                                    }
                                }


                            } else {

                                startAlert();

                            }
                        }
                    } else {
                        String message = jsonObject.getString("message");
                        AlertDialogClass.ShowMsg(SplashScreen.this, message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    AlertDialogClass.ShowMsg(SplashScreen.this, e.getMessage());

                    Log.e("handleMessage: ", e.getMessage());
                }

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
            AlertDialogClass.ShowMsg(SplashScreen.this, e.getMessage());

        }

        Log.d("versionName", "getVersionInfo: " + versionName);
        return versionName;

    }


    private void startAlert() {
        AlertDialog.Builder alertBuild = new AlertDialog.Builder(this)
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
        AlertDialog alert = alertBuild.create();

        if (!SplashScreen.this.isFinishing()) {
            alert.show();
        }
    }


    public void deviceId() {
        String deviceID = "";
        //String simNumber = "";
        String simNumber1 = "";
        String simNumber2 = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                String imei;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = telephonyManager.getImei();
                } else {
                    imei = telephonyManager.getDeviceId();
                }
                if (imei != null && !imei.isEmpty()) {
                    deviceID = imei;
                } else {
                    deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                }
                /* code by pravin 15.05.2020 , to read sim number from dual sim above lolipop */

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

                    /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }*/
//                    List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
//                    if (subscription != null && subscription.size() > 0) {
//                        switch (subscription.size()) {
//                            case 2:
//                                SubscriptionInfo info = subscription.get(1);
//                                simNumber2 = info.getIccId();
//
//                            case 1:
//                                SubscriptionInfo info1 = subscription.get(0);
//                                simNumber1 = info1.getIccId();
//                                //simNumber1 = "849464644646";
//                                break;
//                            default:
//                                break;
//                        }
//                    }
                } else {

                    String tempSimSerial = telephonyManager.getSimSerialNumber();
                    simNumber1 = tempSimSerial == null ? "" : tempSimSerial;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogClass.ShowMsg(SplashScreen.this, e.getMessage());

        }
        Logics.setDeviceID1(SplashScreen.this, deviceID, simNumber1, simNumber2);
        Log.d("simNum", "splashScreen: " + simNumber1);
        Log.d("deviceId", "splashScreen: " + deviceID);

    }


    public interface OnPermissionAccepted {
        void OnPermission(String tag);
    }


    private String _permissionTag = "";
    private OnPermissionAccepted _onpermission;
    public final int REQUEST_CODE_PERMISSION = 10000;

    public void checkPermission(String[] permissions, OnPermissionAccepted _onpermission) {
        checkPermission(permissions, "", _onpermission);
    }

    public void checkPermission(String[] permissions, String _permissionTag, OnPermissionAccepted _onpermission) {
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
                                new AlertDialog.Builder(this)
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

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
    int sockedConnectioncalled = 0;

    @Override
    protected void onResume() {
        super.onResume();

        new ConnectTOServer(SplashScreen.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                if (Const.dismiss == true) {
                    if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Connectivity.getNetworkState1(getApplicationContext())) {
                                    if (sockedConnectioncalled > 2) {
                                        if (!ProgressDlgSomethingIssue) {
                                            ProgressDlgSomethingIssue(SplashScreen.this, "Server not available try after sometime..");
                                            ProgressDlgSomethingIssue = true;
                                        }
                                    } else {
                                        if (!popupdetected)
                                            new ConnectTOServer(SplashScreen.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        sockedConnectioncalled++;
//                                    Views.ProgressDlgConnectSocket(SplashScreen.this, connectionProcess, "Server Not Available");
                                        popupdetected = true;
                                    }


                                } else if (!popupinternet)
//                                    Views.SweetAlert_NoDataAvailble(SplashScreen.this, "No Internet");
                                    ProgressDlgNoInternet(SplashScreen.this, "Please make sure that you are connected to the internet");

//                                TastyToast.makeText(getApplicationContext(), "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                popupinternet = true;
                            }
                        });
                    }
                }
            }
        }, delay);
    }

    public void ProgressDlgSomethingIssue(Context context, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        ProgressDlgSomethingIssue = true;

        sweetAlertDialog.setTitleText(msg)
//                .setContentText("Something went wrong try again later..")
                .setConfirmText("Close!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        ProgressDlgSomethingIssue = false;

                        SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(SplashScreen.this),
                                "0",
                                Methods.getVersionInfo(SplashScreen.this),
                                Methods.getlogsDateTime(), "SplashScreen",
                                Connectivity.getNetworkState(getApplicationContext()),
                                SplashScreen.this);

                        //finish();
                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            finishAndRemoveTask();
                        } else {
                            finish();
                        }

                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                    }
                });
        if (!SplashScreen.this.isFinishing()) {
            sweetAlertDialog.show();
        }
        sweetAlertDialog.setCancelable(false);
//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();
    }

    public void ProgressDlgNoInternet(Context context, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

        sweetAlertDialog.setTitleText(msg)
//                .setContentText("Something went wrong try again later..")
                .setConfirmText("Close!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                        popupinternet = false;

                    }
                });
        if (!SplashScreen.this.isFinishing()) {
            sweetAlertDialog.show();
        }
        sweetAlertDialog.setCancelable(false);
//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();
    }


    void InsertLogsMethod(JSONArray jsonArray) {
        Log.e("jsonArray", String.valueOf(jsonArray.length()));
        Log.e("jsonArraySIZE", jsonArray.toString());

//        JSONArray js = null;
//        try {
//            js = dbh.createJsonArray();
//            Log.e("LOGS_", js.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JSONObject paramObject = new JSONObject();
//        try {
//            paramObject.put("ispojo", js.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(jsonObject);

//        Log.e("logsRequest", jsonArray.toString());

        Call<JsonObject> validateSignature = apiService1.SendSocketLogs(jsonArray.toString());
        validateSignature.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String err = "";

                if (response.isSuccessful()) {
                    SharedPref.clearLogsArrayList(inserSockettLogsArrayList, SplashScreen.this);
//                    Log.e("DASHBOARD_success", "" + response.body());
//                    Log.e("DASHBOARD_success", "" + response.toString());
                    Toast.makeText(SplashScreen.this, response.body().toString(), Toast.LENGTH_SHORT).show();


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
                            err = "Something went wrong try again.";
//                            Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
                Toast.makeText(SplashScreen.this, err, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log.e("DASHBOARD_failure", "   throwable===" + t.getMessage());
            }
        });


    }


}