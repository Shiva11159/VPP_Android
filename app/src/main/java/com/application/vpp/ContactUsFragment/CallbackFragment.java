package com.application.vpp.ContactUsFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.application.vpp.activity.CallBackDetailsList;
import com.application.vpp.activity.Dashboard;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CallbackFragment extends Fragment implements RequestSent,ConnectionProcess {

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

    int MaxTry = 0;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    View view;

    public CallbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_callbackdetails, container, false);

        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        handlerCallback = new ViewHandler();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        searchView = (EditText) view.findViewById(R.id.searchView);
        tv_nodataavail = (TextView) view.findViewById(R.id.tv_nodataavail);
//        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_right);
        listClient = (RecyclerView) view.findViewById(R.id.listClient);
        mainLayout = view.findViewById(R.id.mainLayout);

        sendData();

        return view;

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
                        CallbackModel callbackModel = new CallbackModel(name, created_date, contact_number, status, remark);
                        clientlistDataArrayList.add(callbackModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                        clientlistDataArrayList = gson.fromJson(data, new TypeToken<ArrayList<ClientlistData>>() {
//                        }.getType());
                clientListAdapter = new CallBackAdapter(clientlistDataArrayList, getActivity());
                listClient.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        AlertDialogClass.PopupWindowShow(getActivity(), mainLayout);

        try {
            JSONObject jsonObject = new JSONObject();
            String vppid = Logics.getVppId(getActivity());
            jsonObject.put("vpp_id", vppid);
//            jsonObject.put("vpp_id", vppid);

            byte data[] = jsonObject.toString().getBytes();
            //   new SendTOServer(ClientList.this,ClientList.this, Const.MSGFETCHCLIENTLIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SendTOServer(getActivity(), requestSent, Const.MSGCallbackdetails, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(getActivity()),
                            "1",
                            Methods.getVersionInfo(getActivity()),
                            Methods.getlogsDateTime(), "CallBackDetailsList",
                            Connectivity.getNetworkState(getActivity()),
                            getActivity());
                    ;
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        AlertDialogClass.PopupWindowDismiss();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendData();
                //  TastyToast.makeText(getActivity(), "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
        AlertDialogClass.PopupWindowDismiss();

//        ringProgressDialog.dismiss();
//        ringProgressDialog.cancel();
        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(getActivity(), connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    @Override
    public void IntenrnetNotAvailable() {
//        ringProgressDialog.dismiss();
//        ringProgressDialog.cancel();
        AlertDialogClass.PopupWindowDismiss();

        Views.SweetAlert_NoDataAvailble(getActivity(), "Internet Not Available");
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

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getActivity()))
                    new ConnectTOServer(getActivity(), connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(getActivity(), connectionProcess, "Server Not Available");
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
//                    ConnectToserver(connectionProcess);
                    ProgressDlgConnectSocket(getActivity(), connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
//        if (!getActivity().isFinishing()) {
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(getActivity()),
                                    "0",
                                    Methods.getVersionInfo(getActivity()),
                                    Methods.getlogsDateTime(), "CallBackDetailsList",
                                    Connectivity.getNetworkState(getActivity()),
                                    getActivity());

                            getActivity().finishAffinity();
                            getActivity().finish();


                        }
                    });
            if (!getActivity().isFinishing()) {
                sweetAlertDialog.show();
            } else {
                Toast.makeText(getActivity(), "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess == null) {
                Log.e("DlgConnectSocket11111_null", "called");

            } else {
                new ConnectTOServer(getActivity(), connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                connectionProcess.ConnectToserver(connectionProcess);
            }
            Log.e("DlgConnectSocket11111", "called");

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