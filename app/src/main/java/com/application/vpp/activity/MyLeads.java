package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.MyLeadsdapter;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Datasets.LeadDetailReportDataset;
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

public class MyLeads extends NavigationDrawer implements RequestSent, ConnectionProcess {
    public static Handler handlerLeadList;
    static Gson gson;
    ArrayList<LeadDetailReportDataset> listDatasetArrayList;
    RecyclerView listLead;
    //    ProgressDialog ringProgressDialog;
    EditText searchView;
    MyLeadsdapter leadListadapter;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    LinearLayout linearleadhide;
    TextView tv_nodataavail;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;
    boolean NOCheckINTERNET = false;
    RelativeLayout mainlayout;
    StringBuffer ssb = null;
    String data="";

    int MaxTry=0;
    ArrayList<InserSockettLogs>inserSockettLogsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_lead_list, mDrawerLayout);

        connectionProcess = (ConnectionProcess) this;

        handlerLeadList = new ViewHandler();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        searchView = (EditText) findViewById(R.id.searchView);
        ssb = new StringBuffer();
        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList,"SocketLogs", MyLeads.this);


        linearleadhide = (LinearLayout) findViewById(R.id.linearleadhide);
        tv_nodataavail = (TextView) findViewById(R.id.tv_nodataavail);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;

        listLead = (RecyclerView) findViewById(R.id.listLead);

            mainlayout =  findViewById(R.id.mainlayout);

//        ringProgressDialog = ProgressDialog.show(MyLeads.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        //sendData();


/*
        if (Connectivity.getNetworkState(this)) {
            if (Const.isSocketConnected) {
                ringProgressDialog = Views.showDialog(this);
            } else {
                Views.toast(this, Const.checkConnection);
            }
        }
*/

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                if (leadListadapter!=null)
//                leadListadapter.getFilter().filter(s);
//                else {
//
//                }
//                return false;
//            }
//        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s!=null){
                filter( s.toString());
                }
                //filter( s.toString());
            }
        });




        if (Connectivity.getNetworkState(getApplicationContext())) {
            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgConnectSocket(MyLeads.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                    }
                });
            } else {
                sendData();
            }
        }

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
//                ringProgressDialog.cancel();
//                ringProgressDialog.dismiss();
//            }

            data = "";
            data = (String) msg.obj;
            data = String.valueOf(ssb.append(data));

            int msgCode = msg.arg1;
            switch (msgCode) {

//                case Const.MSGSOCKETCONNECTEDMYLEADS: {
//                    sendData();
//                }
//                break;

                case Const.MSGFETCHLEADDETAILS: {
                    Log.e("MSGFETCHLEADDETAILS", data);

                    if (data.endsWith("]")){
                        if (data.equalsIgnoreCase("[]")) {
                            tv_nodataavail.setVisibility(View.VISIBLE);
                            linearleadhide.setVisibility(View.GONE);
                            ssb.setLength(0);
                            AlertDialogClass.PopupWindowDismiss();


                        } else {
                            tv_nodataavail.setVisibility(View.GONE);
                            linearleadhide.setVisibility(View.VISIBLE);
                           // listDatasetArrayList.clear();

                            if(data.endsWith("]")) {
                                Type userListType = new TypeToken<ArrayList<LeadDetailReportDataset>>() {
                                }.getType();
                                listDatasetArrayList = gson.fromJson(data, userListType);
                                leadListadapter = new MyLeadsdapter(listDatasetArrayList, MyLeads.this);
                                if (listDatasetArrayList != null) {
//                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String s) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String s) {
//
//                                    leadListadapter.getFilter().filter(s);
//                                    return false;
//                                }
//                            });


                                    listLead.setLayoutManager(new LinearLayoutManager(MyLeads.this));
                                    listLead.setAdapter(leadListadapter);
                                    listLead.setItemAnimator(new DefaultItemAnimator());


                                }
                                ssb.setLength(0);

                            }


                        }
                       // ssb.setLength(0);
                    }

                    // data = "[\"[{\"LeadDate\":\"10 Sep 2019 10:04\",\"LeadNo\":\"2506305\",\"CustomerName\":\"CHARANJIT SINGH\",\"BranchCode\":\"9999\",\"VPPPAN\":\"72729\",\"UserName\":\"11030 - Momita Bose\",\"ClientName\":\"  \",\"Status\":\"Call Back\",\"ProductName\":\"Commodities\"}]\"]";
                    //listDatasetArrayList = gson.fromJson(data, new TypeToken<ArrayList<LeadDetailReportDataset>>() {}.getType());


                }
                break;

            }


        }
    }

    private void sendData() {
//        ringProgressDialog = Views.showDialog(this);
//        ringProgressDialog = ProgressDialog.show(MyLeads.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
////        ringProgressDialog.getWindow().setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.color.white));
//        ringProgressDialog.show();
//        if(((Activity) getApplicationContext()).isFinishing())
//        {
//            ringProgressDialog = ProgressDialog.show(MyLeads.this, "Please wait ...", "Loading Your Data ...", true);
//            ringProgressDialog.setCancelable(true);
////        ringProgressDialog.getWindow().setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.color.white));
//            ringProgressDialog.show();
//        }
//        ringProgressDialog = Views.showDialog(this);
//        ProgressDialog ringProgressDialog;

        /*ringProgressDialog = new ProgressDialog(this);
        ringProgressDialog.setMessage("Please wait ..\n Loading Your Data ...");
        ringProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ringProgressDialog.setIndeterminate(true);
        if (!MyLeads.this.isFinishing()){
            ringProgressDialog.show();
        }*/

        AlertDialogClass.PopupWindowShow(MyLeads.this, mainlayout);


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
            String vppid = Logics.getVppId(MyLeads.this);

            jsonObject.put("VPPID", vppid);
//            jsonObject.put("VPPID", "72891");

            jsonObject.put("page", 1);
            jsonObject.put("size", 10);
            jsonObject.put("reportType", "ProgressLeads");

            byte data[] = jsonObject.toString().getBytes();
            new SendTOServer(MyLeads.this, MyLeads.this, Const.MSGFETCHLEADDETAILS, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyLeads.this, Dashboard.class);
        startActivity(intent);
    }

    @Override
    public void connected() {
       /* if (ringProgressDialog != null) {
            ringProgressDialog.cancel();
            ringProgressDialog.dismiss();
        }*/

        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(MyLeads.this),
                            "1",
                            Methods.getVersionInfo(MyLeads.this),
                            Methods.getlogsDateTime(), "MyLeads",
                            Connectivity.getNetworkState(getApplicationContext()),
                            MyLeads.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(MyLeads.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        AlertDialogClass.PopupWindowDismiss();
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendData();
               // TastyToast.makeText(MyLeads.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.cancel();
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();
        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(MyLeads.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(MyLeads.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.cancel();
//            ringProgressDialog.dismiss();
//        }


        AlertDialogClass.PopupWindowDismiss();

        Views.SweetAlert_NoDataAvailble(MyLeads.this, "Internet Not Available");
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
//            ringProgressDialog.cancel();
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(MyLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       ProgressDlgConnectSocket(MyLeads.this, connectionProcess, "Server Not Available");
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
//            ringProgressDialog.cancel();
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(MyLeads.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(MyLeads.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {


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
                                new ConnectTOServer(MyLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(MyLeads.this),
                                    "0",
                                    Methods.getVersionInfo(MyLeads.this),
                                    Methods.getlogsDateTime(), "MyLeads",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    MyLeads.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!MyLeads.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                Toast.makeText(MyLeads.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess==null){
                Log.e( "DlgConnectSocket11111_null", "called");

            }else {
                new ConnectTOServer(MyLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        ArrayList<LeadDetailReportDataset> filteredList = new ArrayList<>();

        for (LeadDetailReportDataset item : listDatasetArrayList) {

            if (item.getCustomerName() != null) {
                if (item.getCustomerName().toUpperCase().contains(text.toUpperCase())||item.getMobileNo().toUpperCase().contains(text.toUpperCase())) {
                    filteredList.add(item);
                }
            }
            leadListadapter.filterList(filteredList);
        }

    }


}
