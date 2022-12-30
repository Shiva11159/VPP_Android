package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.NotInterestedAdapter;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.ClientlistData;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Interfaces.CallBack;
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

public class NotInterested extends NavigationDrawer implements RequestSent, CallBack, ConnectionProcess {

    public static Handler handlerNotInterested;
    static Gson gson;
//    ProgressDialog ringProgressDialog;
    RecyclerView listNotInterested;
    ArrayList<ClientlistData> clientlistDataArrayList;
    EditText searchView;
    CallBack callBack;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    NotInterestedAdapter notInterestedAdapter;
    AlertDialog alertDialog;
    TextView tv_nodataavail;
    LinearLayout linearnotinterest;
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
        getLayoutInflater().inflate(R.layout.activity_not_interested, mDrawerLayout);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        clientlistDataArrayList = new ArrayList<>();
        ssb = new StringBuffer();

        handlerNotInterested = new ViewHandler();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        callBack = this;
        tv_nodataavail = (TextView) findViewById(R.id.tv_nodataavail);
        linearnotinterest = (LinearLayout) findViewById(R.id.linearnotinterest);
        searchView = (EditText) findViewById(R.id.searchView);
        mainlayout = findViewById(R.id.mainlayout);

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList,"SocketLogs", NotInterested.this);

//        ringProgressDialog = ProgressDialog.show(NotInterested.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));


/*
        if (Connectivity.getNetworkState(this)) {
            if (Const.isSocketConnected) {
                sendData();
                ringProgressDialog = Views.showDialog(this);
            } else {

                Views.toast(this, Const.checkConnection);
            }
        }
*/
//        sendData();
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                filter(s.toString());
                filter(s.toString());
            }
        });


    }


    @Override
    public void requestSent(int value) {

    }

    @Override
    public void getDetails(String branchname, String contactperson, String emailid, String mobileno) {

    }

    @Override
    public void getReason(String reason, String name, String leadNo) {
        showPopup(reason, name, leadNo);
//        String s = name + "/n" + reason + "/n" + leadNo;
//        Views.ShowMsg(NotInterested.this, "", s);
    }

//    private void showPopup(String reason, String name, String leadNo) {
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.contactdetails_layout, viewGroup, false);
//        TextView txtreason = view.findViewById(R.id.textview_mobile_num);
//        TextView txt_name = view.findViewById(R.id.textview_email_id);
//        TextView txtleadNo = view.findViewById(R.id.textview_contact_person);
//        ImageView imageViewdlg = view.findViewById(R.id.imageViewdlg);
//        txt_name.setText("Name: " + name);
//        txtleadNo.setText("Lead No: " + leadNo);
//        txtreason.setText("Reason: " + reason);
//        imageViewdlg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
////                finish();
//            }
//        });
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getApplicationContext());
//        builder.setView(view);
//        alertDialog = builder.create();
////        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
//        alertDialog.show();
//    }

    private void showPopup(String reason, String name, String leadNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.reason_popup, null);
        builder.setView(dialogView);
        TextView txtreason = dialogView.findViewById(R.id.txt_reason);
        TextView txt_name = dialogView.findViewById(R.id.textview_lead_name);
        TextView txtleadNo = dialogView.findViewById(R.id.textview_lead_no);

        txt_name.setText("Name: " + name);
        txtleadNo.setText("Lead No: " + leadNo);
        txtreason.setText("Reason: " + reason);
        ImageView imageViewdlg = dialogView.findViewById(R.id.imageViewdlg);

        //  FancyButton btn_positive = dialogView.findViewById(R.id.btnIsRegYes);
        imageViewdlg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
        builder.setCancelable(true);
        alertDialog = builder.show();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public class ViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("NotInterested", "handleMessage: " + msg.obj);

            listNotInterested = (RecyclerView) findViewById(R.id.listNotInterested);
//            if (ringProgressDialog != null) {
//                ringProgressDialog.dismiss();
//            }

            AlertDialogClass.PopupWindowDismiss();

            data = "";
            data = (String) msg.obj;
            data = String.valueOf(ssb.append(data));

            Log.d("NotInterested", "handleMessage: " + data);

            int msgCode = msg.arg1;
            switch (msgCode) {

                case Const.MSGFETCHLEADDETAILDEAD: {
                    if (data.equalsIgnoreCase("[]")) {
                        tv_nodataavail.setVisibility(View.VISIBLE);
                        linearnotinterest.setVisibility(View.GONE);
                        ssb.setLength(0);

                    } else {
                        tv_nodataavail.setVisibility(View.GONE);
                        linearnotinterest.setVisibility(View.VISIBLE);

                        clientlistDataArrayList.clear();

                        if(data.endsWith("]")) {
                            clientlistDataArrayList = gson.fromJson(data, new TypeToken<ArrayList<ClientlistData>>() {
                            }.getType());
                            notInterestedAdapter = new NotInterestedAdapter(clientlistDataArrayList, NotInterested.this, callBack);
                            if (clientlistDataArrayList != null) {
//                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String s) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String s) {
//                                    if (notInterestedAdapter!=null)
//                                        notInterestedAdapter.getFilter().filter(s);
//                                    return false;
//                                }
//                            });

                                listNotInterested.setLayoutManager(new LinearLayoutManager(NotInterested.this));
                                listNotInterested.setAdapter(notInterestedAdapter);
                                listNotInterested.setItemAnimator(new DefaultItemAnimator());
                                ssb.setLength(0);

                            }
                        }

                    }
                 //   ssb.setLength(0);
                }
                break;


                case Const.MSGSOCKETCONNECTEDNOTINT: {
                    sendData();

                }
                break;

            }


        }
    }

    private void sendData() {
//        ringProgressDialog = Views.showDialog(this);
//        ringProgressDialog = new ProgressDialog(this);
//        ringProgressDialog.setMessage("Please wait ..\n Loading Your Data ...");
//        ringProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        ringProgressDialog.setIndeterminate(true);
//        if (!NotInterested.this.isFinishing()) {
//            ringProgressDialog.show();
//        }

        AlertDialogClass.PopupWindowShow(NotInterested.this, mainlayout);


        try {

            JSONObject jsonObject = new JSONObject();

            String vppid = Logics.getVppId(NotInterested.this);


            jsonObject.put("VPPID", vppid);

            jsonObject.put("reportType", "DeadLeads");

            byte data[] = jsonObject.toString().getBytes();

            //   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SendTOServer(NotInterested.this, NotInterested.this, Const.MSGFETCHLEADDETAILDEAD, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(NotInterested.this),
                            "1",
                            Methods.getVersionInfo(NotInterested.this),
                            Methods.getlogsDateTime(), "NotInterested",
                            Connectivity.getNetworkState(getApplicationContext()),
                            NotInterested.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(NotInterested.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
               // TastyToast.makeText(NotInterested.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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
                    ProgressDlgConnectSocket(NotInterested.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(NotInterested.this, "null", Toast.LENGTH_SHORT).show();
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

        Views.SweetAlert_NoDataAvailble(NotInterested.this, "Internet Not Available");
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
                    new ConnectTOServer(NotInterested.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(NotInterested.this, connectionProcess, "Server Not Available");
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
                    ProgressDlgConnectSocket(NotInterested.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(NotInterested.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotInterested.this, Dashboard.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        connectionProcess = (ConnectionProcess) this;

        if (Connectivity.getNetworkState(getApplicationContext())) {
            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgConnectSocket(NotInterested.this, connectionProcess, "Server Not Available");
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
                                new ConnectTOServer(NotInterested.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(NotInterested.this),
                                    "0",
                                    Methods.getVersionInfo(NotInterested.this),
                                    Methods.getlogsDateTime(), "RejectedList",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    NotInterested.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!NotInterested.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                Toast.makeText(NotInterested.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess==null){
                Log.e( "DlgConnectSocket11111_null", "called");

            }else {
                new ConnectTOServer(NotInterested.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    private void filter(String text) {
        ArrayList<ClientlistData> filteredList = new ArrayList<>();
        for (ClientlistData item : clientlistDataArrayList) {
            if (item.getCustomerName() != null) {
                if (item.getCustomerName().toUpperCase().contains(text.toUpperCase())) {
                    filteredList.add(item);
                }
            }
            notInterestedAdapter.filterList(filteredList);
        }
    }

}
