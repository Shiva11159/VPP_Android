package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.SubPartnerAdapter;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Datasets.SubPatnerActivityModel;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SubPatnerActivity extends NavigationDrawer implements ConnectionProcess, RequestSent {
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    public static Handler handlerSubpartner;
    TextView tv_nodataavail;
//    LinearLayout linearsubcategory;
    EditText searchView;
//    ProgressDialog ringProgressDialog;
    SubPartnerAdapter subPartnerAdapter;
    ArrayList<SubPatnerActivityModel> arrayList;
    RecyclerView listsubcategory;

    RelativeLayout mainlayout;

    int MaxTry=0;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_subpartner, mDrawerLayout);
        arrayList = new ArrayList<>();
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        handlerSubpartner = new ViewHandler();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        tv_nodataavail = (TextView) findViewById(R.id.tv_nodataavail);
//        linearsubcategory = (LinearLayout) findViewById(R.id.linearsubcategory);
        searchView = (EditText) findViewById(R.id.searchView);
        listsubcategory = (RecyclerView) findViewById(R.id.listsubcategory);
        mainlayout =  findViewById(R.id.mainlayout);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                subPartnerAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList,"SocketLogs", SubPatnerActivity.this);



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


        sendData();
        //method_comment();


    }

    private void sendData1(String vppid) {
//        ringProgressDialog = Views.showDialog(this);
        AlertDialogClass.PopupWindowShow(SubPatnerActivity.this, mainlayout);

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
//            jsonObject.put("imei",imei);
//            jsonObject.put("page",1);
//            jsonObject.put("size",10);

//            String vppid = Logics.getVppId(SubPatnerActivity.this);

//            jsonObject.put("VPPID", vppid);   /*72686*/
            jsonObject.put("VPPID", "72686");   /*72686*/
            jsonObject.put("reportType", "SubPartner");

            byte data[] = jsonObject.toString().getBytes();

            //   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SendTOServer(SubPatnerActivity.this, requestSent, Const.MSGSUBPARTNERCATEGORY, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void sendData() {
//        ringProgressDialog = Views.showDialog(this);

        AlertDialogClass.PopupWindowShow(SubPatnerActivity.this, mainlayout);

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
//            jsonObject.put("imei",imei);
//            jsonObject.put("page",1);
//            jsonObject.put("size",10);

            String vppid = Logics.getVppId(SubPatnerActivity.this);

            jsonObject.put("VPPID", vppid);   /*650666 */  // live me data regaa...
//            jsonObject.put("VPPID", "650666");   /*650666 */  // live me data regaa...
           // jsonObject.put("VPPID", "72686");   /*72686 */
            jsonObject.put("reportType", "SubPartner");

            byte data[] = jsonObject.toString().getBytes();

            //   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SendTOServer(SubPatnerActivity.this, requestSent, Const.MSGSUBPARTNERCATEGORY, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
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
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(SubPatnerActivity.this),
                            "1",
                            Methods.getVersionInfo(SubPatnerActivity.this),
                            Methods.getlogsDateTime(), "SubPatnerActivity",
                            Connectivity.getNetworkState(getApplicationContext()),
                            SubPatnerActivity.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(SubPatnerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        AlertDialogClass.PopupWindowDismiss();
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendData();
                //TastyToast.makeText(SubPatnerActivity.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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
                    ProgressDlgConnectSocket(SubPatnerActivity.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(SubPatnerActivity.this, "null", Toast.LENGTH_SHORT).show();
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

        Views.SweetAlert_NoDataAvailble(SubPatnerActivity.this, "Internet Not Available");

        Log.e("IntenrnetNotAvailable: ", "internet");

    }

    @Override
    public void ConnectToserver(ConnectionProcess connectionProcess) {
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
                    new ConnectTOServer(SubPatnerActivity.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(SubPatnerActivity.this, connectionProcess, "Server Not Available");
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
                    ProgressDlgConnectSocket(SubPatnerActivity.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(SubPatnerActivity.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void requestSent(int value) {

    }

    public class ViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

//            if (ringProgressDialog != null) {
//                ringProgressDialog.dismiss();
//            }

            AlertDialogClass.PopupWindowDismiss();

            String data = (String) msg.obj;

            Log.e("RESPONSESUBCATEGORY", data);

            int msgCode = msg.arg1;
            switch (msgCode) {

                case Const.MSGSUBPARTNERCATEGORY: {
                    try {
                        if (data.equalsIgnoreCase("[]")) {
                            tv_nodataavail.setVisibility(View.VISIBLE);
                            listsubcategory.setVisibility(View.GONE);
                        } else {
                            tv_nodataavail.setVisibility(View.GONE);
                            listsubcategory.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String ClientName = jsonObject.getString("ClientName");
                                    String AccountOpenedDate = jsonObject.getString("AccountOpenedDate");
                                    String ProductName = jsonObject.getString("ProductName");
                                    String ClientCode = jsonObject.getString("ClientCode");

                                    Log.e("ClientName", ClientName);
                                    Log.e("AccountOpenedDate", AccountOpenedDate);
                                    Log.e("ProductName", ProductName);
                                    Log.e("ClientCode", ClientCode);

                                    SubPatnerActivityModel subPatnerActivityModel = new SubPatnerActivityModel(ClientName,
                                            AccountOpenedDate,
                                            ProductName,
                                            ClientCode);

                                    arrayList.add(subPatnerActivityModel);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e( "handleMessage: ",e.getMessage() );
                                    FirebaseCrashlytics.getInstance().recordException(e);
                                }


                            }

                            Log.e("sizzeee", String.valueOf(arrayList.size()));

                            subPartnerAdapter = new SubPartnerAdapter(arrayList, SubPatnerActivity.this);
                            if (arrayList != null) {
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



                                listsubcategory.setLayoutManager(new LinearLayoutManager(SubPatnerActivity.this));
                                listsubcategory.setAdapter(subPartnerAdapter);
                                listsubcategory.setItemAnimator(new DefaultItemAnimator());


                            }

                        }

                    } catch (Exception e) {
                        Log.e("handleMessage11: ", e.getMessage());
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
                break;

            }


        }
    }

    void method_comment(){

        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("VPP-Sub Patner")
                .setMessage("Please Enter VPP ID")
                .setView(taskEditText)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                       // sendData1(task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

       /* final View view = layoutInflater.inflate(R.layout.enter_vppid, null);
        AlertDialog alertDialog = new AlertDialog.Builder(SubPatnerActivity.this).create();
        alertDialog.setTitle("Your Title Here");
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Your Message Here");


        final EditText etComments = (EditText) view.findViewById(R.id.etComments);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });


        alertDialog.setView(view);
        alertDialog.show();*/
    }
    private void filter(String text) {
        ArrayList<SubPatnerActivityModel> filteredList = new ArrayList<>();
        for (SubPatnerActivityModel item : arrayList) {
            if (item.getClientName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        subPartnerAdapter.filterList(filteredList);
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(SubPatnerActivity.this),
                                    "0",
                                    Methods.getVersionInfo(SubPatnerActivity.this),
                                    Methods.getlogsDateTime(), "SubPatnerActivity",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    SubPatnerActivity.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!SubPatnerActivity.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                Toast.makeText(SubPatnerActivity.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess==null){
                Log.e( "DlgConnectSocket11111_null", "called");

            }else {
                new ConnectTOServer(SubPatnerActivity.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
