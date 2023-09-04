package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.ClientListAdapter;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.ClientlistData;
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
import java.util.LinkedHashSet;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ClientList extends NavigationDrawer implements RequestSent, ConnectionProcess {

    public static Handler handlerClientList;
    static Gson gson;
    //    ProgressDialog ringProgressDialog;
    ArrayList<ClientlistData> clientlistDataArrayList;
    RecyclerView listClient;
    EditText searchView;
    ClientListAdapter clientListAdapter;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    TextView tv_nodataavail;
//    LinearLayout linearclientlist;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;
    boolean NOCheckINTERNET = false;
    StringBuffer ssb = null;
    String data = "";
    RelativeLayout mainLayout;
    int MaxTry = 0;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_client_list, mDrawerLayout);
        try {
            connectionProcess = (ConnectionProcess) this;
            clientlistDataArrayList = new ArrayList<>();
            requestSent = (RequestSent) this;
            handlerClientList = new ViewHandler();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
            gson = gsonBuilder.create();
            searchView = (EditText) findViewById(R.id.searchView);
            tv_nodataavail = (TextView) findViewById(R.id.tv_nodataavail);
//            linearclientlist = (LinearLayout) findViewById(R.id.linearclientlist);
            mainLayout = findViewById(R.id.mainLayout);


            listClient = (RecyclerView) findViewById(R.id.listClient);


            listClient.setHasFixedSize(true);
            tv_nodataavail.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
//            linearclientlist.setVisibility(View.GONE);

            ssb = new StringBuffer();
            inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList, "SocketLogs", ClientList.this);
            searchView.setVisibility(View.GONE);


            try {
                if (Connectivity.getNetworkState(getApplicationContext())) {
                    if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProgressDlgConnectSocket(ClientList.this, connectionProcess, "Server Not Available");
                            }
                        });
                    } else {
                        sendData();
                    }
                }

            } catch (Exception e) {

                AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
            }


        } catch (Exception e) {
            AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
        }

//        ringProgressDialog = ProgressDialog.show(ClientList.this, "Please wait ...", "Loading Your Data ...", true);
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
                if (s != null) {

                    try {

                        filter(s.toString());
                    } catch (Exception e) {

                        AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
                    }
                }
//                clientListAdapter.getFilter().filter(s);

            }
        });

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
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            int msgCode = 0;
            try {
                data = "";
                data = (String) msg.obj;
                data = String.valueOf(ssb.append(data));
                msgCode = msg.arg1;
            } catch (Exception e) {
                AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
            }


            switch (msgCode) {
                case Const.MSGFETCHLEADDETAILCLIENT: {

                    Log.e("handleMessage: ", data);

                    try {
                        if (data.equalsIgnoreCase("[]")) {
                            tv_nodataavail.setVisibility(View.VISIBLE);
                            listClient.setVisibility(View.GONE);
                            ssb.setLength(0);
                            AlertDialogClass.PopupWindowDismiss();


                        } else {
                            if (data.endsWith("]")) {
                                try {
//                                    clientlistDataArrayList.clear();
                                    tv_nodataavail.setVisibility(View.GONE);
                                    searchView.setVisibility(View.VISIBLE);
                                    listClient.setVisibility(View.VISIBLE);
                                    clientlistDataArrayList.clear();
                                    JSONArray jsonArray = new JSONArray(data);

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        String ClientCode = jsonObject.getString("ClientCode");
                                        String ClientName = jsonObject.getString("ClientName");
                                        String ProductName = jsonObject.getString("ProductName");
                                        String AccountOpenedDate = jsonObject.getString("AccountOpenedDate");

                                        Log.e("AccountOpenedDate", AccountOpenedDate);
                                        String LeadNo = jsonObject.getString("LeadNo");
                                        String CustomerName = jsonObject.getString("CustomerName");
                                        String LeadDate = jsonObject.getString("LeadDate");
                                        String MobileNo = jsonObject.getString("MobileNo");

                                        ClientlistData clientlistData = new ClientlistData(MobileNo,ClientCode, ClientName, ProductName, AccountOpenedDate,LeadNo,CustomerName,LeadDate,"");

                                        clientlistDataArrayList.add(clientlistData);
                                    }


//                                    clientlistDataArrayList = gson.fromJson(data, new TypeToken<ArrayList<ClientlistData>>() {
//                                    }.getType());

                                    clientListAdapter = new ClientListAdapter(getUniqueList(clientlistDataArrayList), ClientList.this);
                                    listClient.setVisibility(View.VISIBLE);
                                    listClient.setLayoutManager(new LinearLayoutManager(ClientList.this));
                                    listClient.setAdapter(clientListAdapter);
                                    //clientListAdapter.setHasStableIds(true);

//                                    clientListAdapter.notifyDataSetChanged();
                                    ssb.setLength(0);

                                } catch (Exception e) {
                                    AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
                                }

                            }
                        }


                        if (listClient.getVisibility() == View.VISIBLE || tv_nodataavail.getVisibility()==View.VISIBLE) {
                            //AlertDialogClass.PopupWindowDismiss();

                        }


                    } catch (Exception e) {

                        AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
                    }
                }
                break;


                case Const.MSGSOCKETCONNECTEDCLIENT: {
                    sendData();
                }
                break;

            }


        }
    }

    private void sendData() {
//        ringProgressDialog = new ProgressDialog(this);
//        ringProgressDialog.setMessage("Please wait ..\n Loading Your Data ...");
//        ringProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        ringProgressDialog.setIndeterminate(true);
//        if (!ClientList.this.isFinishing()) {
//            ringProgressDialog.show();
//        }


        try {

            AlertDialogClass.PopupWindowShow(ClientList.this, mainLayout);

            JSONObject jsonObject = new JSONObject();

            String vppid = Logics.getVppId(ClientList.this);

//            jsonObject.put("VPPID", "72891");
            jsonObject.put("VPPID", "72089");
            jsonObject.put("reportType", "AccountOpened");

            byte data[] = jsonObject.toString().getBytes();
            new SendTOServer(ClientList.this, ClientList.this, Const.MSGFETCHLEADDETAILCLIENT, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);

            AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
        }

    }

    @Override
    public void connected() {

        try {
            if (inserSockettLogsArrayList != null) {
                if (inserSockettLogsArrayList.size() > 0) {
                    try {
                        SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(ClientList.this),
                                "1",
                                Methods.getVersionInfo(ClientList.this),
                                Methods.getlogsDateTime(), "ClientList",
                                Connectivity.getNetworkState(getApplicationContext()),
                                ClientList.this);
                        ;
                    } catch (Exception e) {
                        Toast.makeText(ClientList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
            AlertDialogClass.PopupWindowDismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sendData();
                    //TastyToast.makeText(ClientList.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                }
            });
        } catch (Exception e) {

            AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
        }
    }

    @Override
    public void serverNotAvailable() {
        try {
            AlertDialogClass.PopupWindowDismiss();
            Log.e("serverNotAvailable00: ", "serverNotAvailable00");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (connectionProcess != null) {
                        ProgressDlgConnectSocket(ClientList.this, connectionProcess, "Server Not Available");
                    } else {
                        Toast.makeText(ClientList.this, "null", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {

            AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ClientList.this, Dashboard.class);
        startActivity(intent);
    }

    @Override
    public void IntenrnetNotAvailable() {

        AlertDialogClass.PopupWindowDismiss();

        Views.SweetAlert_NoDataAvailble(ClientList.this, "Internet Not Available");
        Log.e("IntenrnetNotAvailable: ", "internet");

    }

    @Override
    public void ConnectToserver(final ConnectionProcess connectionProcess) {

        try {
            AlertDialogClass.PopupWindowDismiss();
            Log.e("ConnectToserver11: ", "ConnectToserver11");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Connectivity.getNetworkState(getApplicationContext()))
                        new ConnectTOServer(ClientList.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Const.dismiss == true) {
                                if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ProgressDlgConnectSocket(ClientList.this, connectionProcess, "Server Not Available");
//                                        ConnectToserver(connectionProcess);
                                        }
                                    });
                                }
                            }
                        }
                    }, 1000);
                }
            });
        } catch (Exception e) {

            AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
        }
    }

    @Override
    public void SocketDisconnected() {

        try {
            AlertDialogClass.PopupWindowDismiss();
            Log.e("SocketDisconnected11: ", "SocketDisconnected");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (connectionProcess != null) {
                        ProgressDlgConnectSocket(ClientList.this, connectionProcess, "Server Not Available");
                    } else {
                        Toast.makeText(ClientList.this, "null", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {

            AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        connectionProcess = (ConnectionProcess) this;

        ///added this lines extra by shiva ....
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {

                try {
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
                                    new ConnectTOServer(ClientList.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                }
                            }
                        }
                    });

                } catch (Exception e) {
                    AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
                }

            }
        }, delay);
        super.onResume();

    }

    @SuppressLint("LongLogTag")
    public void ProgressDlgConnectSocket(Context context, final ConnectionProcess connectionProcess, String msg) {
        // 2. Confirmation message


        try {
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
                                SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(ClientList.this),
                                        "0",
                                        Methods.getVersionInfo(ClientList.this),
                                        Methods.getlogsDateTime(), "ClientList",
                                        Connectivity.getNetworkState(getApplicationContext()),
                                        ClientList.this);

                                finishAffinity();
                                finish();
                            }
                        });
                if (!ClientList.this.isFinishing()) {
                    sweetAlertDialog.show();
                } else {
                    Toast.makeText(ClientList.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
                }
                sweetAlertDialog.setCancelable(false);

            } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                if (connectionProcess == null) {
                    Log.e("DlgConnectSocket11111_null", "called");

                } else {
                    new ConnectTOServer(ClientList.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    connectionProcess.ConnectToserver(connectionProcess);
                }
                Log.e("DlgConnectSocket11111", "called");

            }

            Log.e("DlgConnectSocketMaxTry", String.valueOf(MaxTry));


        } catch (Exception e) {

            AlertDialogClass.ShowMsg(ClientList.this, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void filter(String text) {
        ArrayList<ClientlistData> filteredList = new ArrayList<>();
        for (ClientlistData item : clientlistDataArrayList) {
            if (item.ClientName.toLowerCase().contains(text.toLowerCase())||item.MobileNo.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        clientListAdapter.filterList(filteredList);
    }

    public ArrayList<ClientlistData> removeDuplicates(ArrayList<ClientlistData> list) {
        Set<ClientlistData> set = new LinkedHashSet<>(list);
        return new ArrayList<ClientlistData>(set);
    }

    public ArrayList<ClientlistData> getUniqueList(ArrayList<ClientlistData> alertList) {
        ArrayList<ClientlistData> uniqueAlerts = new ArrayList<ClientlistData>();
        for (ClientlistData alert : alertList) {
            if (!uniqueAlerts.contains(alert)) {
                uniqueAlerts.add(alert);
            }
        }
        return uniqueAlerts;
    }
}
