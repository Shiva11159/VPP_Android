package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.InProcessAdapter;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.InProcessDataset;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class

RejectedList extends NavigationDrawer implements RequestSent, ConnectionProcess {

    RecyclerView listRejected;
    public static Handler handlerRejectedList;
    static Gson gson;
//    ProgressDialog ringProgressDialog;
    ArrayList<InProcessDataset> rejecDatasetArrayList;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    TextView tv_nodataavail;
    LinearLayout linearrejected;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;
    boolean NOCheckINTERNET = false;
    Context context;
    RelativeLayout mainlayout;
    String data="";
    StringBuffer ssb = null;

    int MaxTry=0;
    ArrayList<InserSockettLogs>inserSockettLogsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_rejected_list, mDrawerLayout);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        linearrejected = (LinearLayout) findViewById(R.id.linearrejected);
        listRejected = (RecyclerView) findViewById(R.id.listRejected);
        tv_nodataavail = (TextView) findViewById(R.id.tv_nodataavail);
        mainlayout = findViewById(R.id.mainlayout);
        ssb = new StringBuffer();

        handlerRejectedList = new ViewHandler();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList,"SocketLogs", RejectedList.this);


//        ringProgressDialog = ProgressDialog.show(RejectedList.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

//        sendData();

/*
        if (Connectivity.getNetworkState(this)) {
            if (Const.isSocketConnected) {
                ringProgressDialog = Views.showDialog(this);
            }else {

                Views.toast(this,Const.checkConnection);
            }
        }
*/

    }

    @Override
    public void requestSent(int value) {

    }


    public class ViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("RejectedList", "handleMessage: " + msg.obj);

            data = "";
            data = (String) msg.obj;
            data = String.valueOf(ssb.append(data));

//            if (ringProgressDialog != null) {
//                ringProgressDialog.dismiss();
//            }

            AlertDialogClass.PopupWindowDismiss();
            int msgCode = msg.arg1;
            switch (msgCode) {

                case Const.MSGFETCHLEADREJECTED: {
                    if (data.equalsIgnoreCase("[]")) {
                        tv_nodataavail.setVisibility(View.VISIBLE);
                        linearrejected.setVisibility(View.GONE);
                        ssb.setLength(0);

                    } else {

                       // rejecDatasetArrayList.clear();
                        tv_nodataavail.setVisibility(View.GONE);
                        linearrejected.setVisibility(View.VISIBLE);
                        if(data.endsWith("]")) {
                            rejecDatasetArrayList = gson.fromJson(data, new TypeToken<ArrayList<InProcessDataset>>() {
                            }.getType());
                            InProcessAdapter inProcessAdapter = new InProcessAdapter(rejecDatasetArrayList, RejectedList.this, "rejected");
                            listRejected.setLayoutManager(new LinearLayoutManager(RejectedList.this));
                            listRejected.setAdapter(inProcessAdapter);
                            listRejected.setItemAnimator(new DefaultItemAnimator());
                            ssb.setLength(0);

                        }

                    }
                  //  ssb.setLength(0);
                }
                break;


                case Const.MSGSOCKETCONNECTEDREJECTED: {
                    sendData();

                }
                break;

            }


        }
    }


    private void sendData() {
//        ringProgressDialog = Views.showDialog(this);
//        if(((Activity) context).isFinishing())
//        {
//            ringProgressDialog = ProgressDialog.show(RejectedList.this, "Please wait ...", "Loading Your Data ...", true);
//            ringProgressDialog.setCancelable(true);
////        ringProgressDialog.getWindow().setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.color.white));
//            ringProgressDialog.show();
//        }
//        ringProgressDialog = new ProgressDialog(this);
//        ringProgressDialog.setMessage("Please wait ..\n Loading Your Data ...");
//        ringProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        ringProgressDialog.setIndeterminate(true);
//
//        if (!RejectedList.this.isFinishing()){
//            ringProgressDialog.show();
//        }


        AlertDialogClass.PopupWindowShow(RejectedList.this, mainlayout);

        try {

//            String imei;
//            String simID;
//            String osversion="normal";
//            imei = Logics.getDeviceID(this);
//            simID = Logics.getSimId(this);
//            if (android.os.Build.VERSION.SDK_INT>=29){
//                imei=Logics.getTokenID(this);
//                osversion="oxy";
//                simID="12345";
//            }
            JSONObject jsonObject = new JSONObject();
////            jsonObject.put("imei",imei);
////            jsonObject.put("page",1);
////            jsonObject.put("size",10);
//
//            jsonObject.put("imei",imei);
//            jsonObject.put("simNo",simID);
            String vppid = Logics.getVppId(RejectedList.this);
//            jsonObject.put("VPPID", "su72259");
            jsonObject.put("VPPID", vppid);
//            jsonObject.put("VPPID","650666");
            jsonObject.put("reportType", "Rejected");

            byte data[] = jsonObject.toString().getBytes();
            //   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SendTOServer(RejectedList.this, RejectedList.this, Const.MSGFETCHLEADREJECTED, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }

    @Override
    public void connected() {

//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(RejectedList.this),
                            "1",
                            Methods.getVersionInfo(RejectedList.this),
                            Methods.getlogsDateTime(), "RejectedList",
                            Connectivity.getNetworkState(getApplicationContext()),
                            RejectedList.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(RejectedList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        AlertDialogClass.PopupWindowDismiss();
        Log.e("connected11", "connected11");
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendData();
                //TastyToast.makeText(RejectedList.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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
                    ProgressDlgConnectSocket(RejectedList.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(RejectedList.this, "null", Toast.LENGTH_SHORT).show();
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

        Views.SweetAlert_NoDataAvailble(RejectedList.this, "Internet Not Available");
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
                    new ConnectTOServer(RejectedList.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(RejectedList.this, connectionProcess, "Server Not Available");
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
                    ProgressDlgConnectSocket(RejectedList.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(RejectedList.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        connectionProcess = (ConnectionProcess) this;

        if (Connectivity.getNetworkState(getApplicationContext())) {
            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgConnectSocket(RejectedList.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                    }
                });
            } else {
                sendData();
            }
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
                                new ConnectTOServer(RejectedList.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            }
                        }
                    }
                });
            }
        }, delay);
        super.onResume();

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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(RejectedList.this),
                                    "0",
                                    Methods.getVersionInfo(RejectedList.this),
                                    Methods.getlogsDateTime(), "RejectedList",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    RejectedList.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!RejectedList.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                Toast.makeText(RejectedList.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess==null){
                Log.e( "DlgConnectSocket11111_null", "called");

            }else {
                new ConnectTOServer(RejectedList.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
