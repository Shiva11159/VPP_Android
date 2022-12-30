package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.QueryStatusAdapter;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Datasets.QueryStatusData;
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

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class QueryStatus extends NavigationDrawer implements RequestSent, ConnectionProcess {
    public static Handler handlerQueryList;
    static Gson gson;
    ArrayList<QueryStatusData> queryStatusData = new ArrayList<>();
    RecyclerView listQuery;
//    ProgressDialog ringProgressDialog;
    QueryStatusAdapter queryStatusAdapter;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    LinearLayout mainlayout;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;
    TextView tv_nodataavail;

    int MaxTry=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_query_status, mDrawerLayout);
        handlerQueryList = new ViewHandler();
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        listQuery = (RecyclerView) findViewById(R.id.listQuery);
        tv_nodataavail =  findViewById(R.id.tv_nodataavail);
        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList,"SocketLogs", QueryStatus.this);

        mainlayout=findViewById(R.id.mainlayout);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        RecyclerView.ItemAnimator animator = listQuery.getItemAnimator();
//
//        if (animator instanceof DefaultItemAnimator) {
//            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
//        }
//
//        listQuery.setLayoutManager(layoutManager);
        sendData();
    }

    @Override
    public void requestSent(int value) {

    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.d("Message", "handleMessageLeadList: " + msg.toString());

//            if (ringProgressDialog != null) {
//                ringProgressDialog.dismiss();
//            }

            AlertDialogClass.PopupWindowDismiss();
            String data = (String) msg.obj;
            int msgCode = msg.arg1;
            switch (msgCode) {
                case Const.MSGQUERYLIST: {
                    Log.e("response", data);
                    if (data.equalsIgnoreCase("[]")){
                        tv_nodataavail.setVisibility(View.VISIBLE);
                        listQuery.setVisibility(View.GONE);
                    }else {
                        tv_nodataavail.setVisibility(View.GONE);
                        listQuery.setVisibility(View.VISIBLE);
                        Type userListType = new TypeToken<ArrayList<QueryStatusData>>() {
                        }.getType();
                        queryStatusData = gson.fromJson(data, userListType);
                        queryStatusAdapter = new QueryStatusAdapter(queryStatusData, QueryStatus.this);
                        listQuery.setLayoutManager(new LinearLayoutManager(QueryStatus.this));
                        listQuery.setAdapter(queryStatusAdapter);
                        listQuery.setItemAnimator(new DefaultItemAnimator());
                    }
                }
                break;
            }
        }
    }

    private void sendData() {
//        ringProgressDialog = Views.showDialog(this);
        AlertDialogClass.PopupWindowShow(QueryStatus.this, mainlayout);

        try {
            String vppId = Logics.getVppId(QueryStatus.this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("VppId", vppId);
            Log.e("ssss", jsonObject.toString());
            byte data[] = jsonObject.toString().getBytes();
            new SendTOServer(QueryStatus.this, QueryStatus.this, Const.MSGQUERYLIST, data,connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QueryStatus.this, Dashboard.class);
        startActivity(intent);
    }

    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }


        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(QueryStatus.this),
                            "1",
                            Methods.getVersionInfo(QueryStatus.this),
                            Methods.getlogsDateTime(), "QueryStatus",
                            Connectivity.getNetworkState(getApplicationContext()),
                            QueryStatus.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(QueryStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        AlertDialogClass.PopupWindowDismiss();
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendData();
                //TastyToast.makeText(QueryStatus.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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
                    ProgressDlgConnectSocket(QueryStatus.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(QueryStatus.this, "null", Toast.LENGTH_SHORT).show();
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

        Views.SweetAlert_NoDataAvailble(QueryStatus.this, "Internet Not Available");
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
                    new ConnectTOServer(QueryStatus.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       ProgressDlgConnectSocket(QueryStatus.this, connectionProcess, "Server Not Available");
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
                    ProgressDlgConnectSocket(QueryStatus.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(QueryStatus.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(QueryStatus.this),
                                    "0",
                                    Methods.getVersionInfo(QueryStatus.this),
                                    Methods.getlogsDateTime(), "QueryStatus",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    QueryStatus.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!QueryStatus.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                Toast.makeText(QueryStatus.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess==null){
                Log.e( "DlgConnectSocket11111_null", "called");

            }else {
                new ConnectTOServer(QueryStatus.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
