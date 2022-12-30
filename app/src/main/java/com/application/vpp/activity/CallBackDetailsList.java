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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.CallBackAdapter;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.CallbackModel;
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
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CallBackDetailsList extends NavigationDrawer implements RequestSent, ConnectionProcess {

    public static Handler handlerCallback;
    static Gson gson;
//    ProgressDialog ringProgressDialog;
    ArrayList<CallbackModel> clientlistDataArrayList;
    RecyclerView listClient;
    EditText searchView;
    CallBackAdapter clientListAdapter;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    TextView tv_nodataavail;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;
    boolean NOCheckINTERNET = false;
    RelativeLayout mainLayout;

    int MaxTry=0;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_callbackdetails, mDrawerLayout);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        handlerCallback = new ViewHandler();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        searchView = (EditText) findViewById(R.id.searchView);
        tv_nodataavail = (TextView) findViewById(R.id.tv_nodataavail);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_right);
        listClient = (RecyclerView) findViewById(R.id.listClient);
        mainLayout =  findViewById(R.id.mainLayout);

//        ringProgressDialog = ProgressDialog.show(CallBackDetailsList.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

      /*  if (Connectivity.getNetworkState(this)) {
            if (Const.isSocketConnected) {
                sendData();
                ringProgressDialog = Views.showDialog(this);
            }else {

                Views.toast(this,Const.checkConnection);
            }
        }*/
        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList,"SocketLogs", CallBackDetailsList.this);

        sendData();

//        searchView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                filter(s.toString());
////                clientListAdapter.getFilter().filter(s);
//
//            }
//        });

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                clientListAdapter.getFilter().filter(s);
//                return false;
//            }
//        });
    }

    @Override
    public void requestSent(int value) {

    }


    public class ViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            clientlistDataArrayList = new ArrayList<>();
//            ringProgressDialog.dismiss();
//            ringProgressDialog.cancel();


            AlertDialogClass.PopupWindowDismiss();
            String data = (String) msg.obj;
            Log.e("Clientlist", data);

//            switch (msgCode) {
//
//                case Const.MSGCallbackdetails: {
//
            if (data.equalsIgnoreCase("[]")) {
                tv_nodataavail.setVisibility(View.VISIBLE);
            } else {
                tv_nodataavail.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = new JSONArray(data);

                    for (int i = 0; i <= jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String name = jsonObject.getString("name");
                        String created_date = jsonObject.getString("created_date");
                        String contact_number = jsonObject.getString("contact_number");
                        String status = jsonObject.getString("status");
                        String remark = jsonObject.getString("remark");
                        CallbackModel callbackModel = new CallbackModel(name, created_date, contact_number,status,remark);
                        clientlistDataArrayList.add(callbackModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                        clientlistDataArrayList = gson.fromJson(data, new TypeToken<ArrayList<ClientlistData>>() {
//                        }.getType());
                clientListAdapter = new CallBackAdapter(clientlistDataArrayList, CallBackDetailsList.this);
                listClient.setLayoutManager(new LinearLayoutManager(CallBackDetailsList.this));
                listClient.setItemAnimator(new DefaultItemAnimator());
                listClient.setAdapter(clientListAdapter);
//
//


    }
}
    }

    private void sendData() {
//        ringProgressDialog = new ProgressDialog(this);
//        ringProgressDialog.setMessage("Loading Your Data ...");
//        ringProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        ringProgressDialog.setIndeterminate(true);

        AlertDialogClass.PopupWindowShow(CallBackDetailsList.this,mainLayout);

        try {
            JSONObject jsonObject = new JSONObject();
            String vppid = Logics.getVppId(CallBackDetailsList.this);
            jsonObject.put("vpp_id", vppid);
//            jsonObject.put("vpp_id", vppid);

            byte data[] = jsonObject.toString().getBytes();
            //   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SendTOServer(CallBackDetailsList.this, CallBackDetailsList.this, Const.MSGCallbackdetails, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }

    @Override
    public void connected() {
//        ringProgressDialog.dismiss();
//        ringProgressDialog.cancel();
        //        AlertDailog.ProgressDlgDiss();

        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(CallBackDetailsList.this),
                            "1",
                            Methods.getVersionInfo(CallBackDetailsList.this),
                            Methods.getlogsDateTime(), "CallBackDetailsList",
                            Connectivity.getNetworkState(getApplicationContext()),
                            CallBackDetailsList.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(CallBackDetailsList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        AlertDialogClass.PopupWindowDismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendData();
              //  TastyToast.makeText(CallBackDetailsList.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
        AlertDialogClass.PopupWindowDismiss();

//        ringProgressDialog.dismiss();
//        ringProgressDialog.cancel();
        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(CallBackDetailsList.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(CallBackDetailsList.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CallBackDetailsList.this, Dashboard.class);
        startActivity(intent);
    }

    @Override
    public void IntenrnetNotAvailable() {
//        ringProgressDialog.dismiss();
//        ringProgressDialog.cancel();
AlertDialogClass.PopupWindowDismiss();

        Views.SweetAlert_NoDataAvailble(CallBackDetailsList.this, "Internet Not Available");
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
//        ringProgressDialog.dismiss();
//        ringProgressDialog.cancel();
//        connected = false;

        AlertDialogClass.PopupWindowDismiss();
        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(CallBackDetailsList.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(CallBackDetailsList.this, connectionProcess, "Server Not Available");
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
//        ringProgressDialog.dismiss();
//        ringProgressDialog.cancel();
        AlertDialogClass.PopupWindowDismiss();
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
//                    ConnectToserver(connectionProcess);
                    ProgressDlgConnectSocket(CallBackDetailsList.this, connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(CallBackDetailsList.this, "null", Toast.LENGTH_SHORT).show();
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
                        ProgressDlgConnectSocket(CallBackDetailsList.this, connectionProcess, "Server Not Available");
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
                                new ConnectTOServer(CallBackDetailsList.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            }
                        }
                    }
                });
            }
        }, delay);
        super.onResume();

    }

//    public void ProgressDlgConnectSocket(Context context, final ConnectionProcess connectionProcess, String msg) {
//        // 2. Confirmation message
//        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
//
//        sweetAlertDialog.setTitleText(msg)
////                .setContentText("Socket Not Available")
//                .setConfirmText("Reconnect!")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                        sDialog.dismiss();
//                        sDialog.cancel();
//                        connectionProcess.ConnectToserver(connectionProcess);
//                    }
//                });
//
//
//        if (!CallBackDetailsList.this.isFinishing()) {
//            sweetAlertDialog.show();
//        }
//
//        sweetAlertDialog.setCancelable(false);
////                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
////                    @Override
////                    public void onClick(SweetAlertDialog sDialog) {
////                        sDialog.dismissWithAnimation();
////                    }
////                })
////                .show();
//    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(CallBackDetailsList.this),
                                    "0",
                                    Methods.getVersionInfo(CallBackDetailsList.this),
                                    Methods.getlogsDateTime(), "CallBackDetailsList",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    CallBackDetailsList.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!CallBackDetailsList.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                Toast.makeText(CallBackDetailsList.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess==null){
                Log.e( "DlgConnectSocket11111_null", "called");

            }else {
                new ConnectTOServer(CallBackDetailsList.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
