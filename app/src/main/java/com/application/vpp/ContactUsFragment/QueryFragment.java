package com.application.vpp.ContactUsFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.QueryAdapter;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Datasets.QueryStatusData;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.OnclickQuery;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.application.vpp.activity.Dashboard;
import com.application.vpp.activity.QueryStatus;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class QueryFragment extends Fragment implements OnclickQuery,RequestSent, ConnectionProcess {

    public static Handler handlerQueryList1;
    static Gson gson;

    List<QueryStatusData> arralist;
    RecyclerView recyclerView;
    //    ProgressDialog ringProgressDialog;
    OnclickQuery onclickQuery;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    LinearLayout mainlayout;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;
    TextView tv_nodataavail;
    View view;
    int MaxTry = 0;

    AlertDialog alertQuerydatashow;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_query_status, container, false);
        handlerQueryList1 = new ViewHandler();
        onclickQuery=(OnclickQuery)this;
        connectionProcess = (ConnectionProcess) this;
        // requestSent = (RequestSent) this;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        tv_nodataavail = view.findViewById(R.id.tv_nodataavail);
        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList, "SocketLogs", getActivity());
        mainlayout = view.findViewById(R.id.mainlayout);

        arralist=new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.listQuery);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initData();



        sendData();
        return view;
    }
    @Override
    public void requestSent(int value) {

    }

    @Override
    public void OnclickQuery(String query, String remark) {
        AlertQueryDetails(query,remark);
    }

    private void AlertQueryDetails(String query,String remark) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_alertquery, null);
        dialogBuilder.setView(dialogView);

        final TextView querytxt = (TextView) dialogView.findViewById(R.id.querytxt);
        final TextView remarktxt = (TextView) dialogView.findViewById(R.id.remarktxt);
        final ImageView close= (ImageView) dialogView.findViewById(R.id.buttonImageclose);

        if (remark.equalsIgnoreCase("")) {
            remarktxt.setText("-");
        } else {
            remarktxt.setText(remark);

        }

        querytxt.setText(query);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertQuerydatashow.dismiss();
                alertQuerydatashow.cancel();
            }
        });

        alertQuerydatashow = dialogBuilder.create();
        alertQuerydatashow.show();
    }


    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("Message", "handleMessageLeadList: " + msg.toString());
            AlertDialogClass.PopupWindowDismiss();
            String data = (String) msg.obj;
            int msgCode = msg.arg1;
            switch (msgCode) {
                case Const.MSGQUERYLIST: {
                    if (data.equalsIgnoreCase("[]")) {
                        //tv_nodataavail.setVisibility(View.VISIBLE);
                        //recyclerView.setVisibility(View.GONE);
                    } else {
                        //tv_nodataavail.setVisibility(View.GONE);
                        //recyclerView.setVisibility(View.VISIBLE);
//                        Type userListType = new TypeToken<ArrayList<QueryStatusData>>() {
//                        }.getType();

                        Log.e("MSGQUERYLIST", data);

                        arralist.clear();
                        //arralist = gson.fromJson(data, userListType);

                        try {
                            JSONArray jsonArray=new JSONArray(data);

                            for (int i =0;i<jsonArray.length();i++){

                                JSONObject  jsonObject=jsonArray.getJSONObject(i);
                                String date=jsonObject.getString("date");
                                String query=jsonObject.getString("query");
                                String sr_no=jsonObject.getString("sr_no");
                                String remark=jsonObject.getString("remark");
                                String status=jsonObject.getString("status");
                                QueryStatusData queryStatusData=new QueryStatusData(status,date,remark,sr_no,query);
                                arralist.add(queryStatusData);

                            }

                        }catch (Exception e){

                            Log.e("exception",e.getMessage() );
                        }


//                        Log.e("queryStatusData", String.valueOf(arralist.size()));
//                        Log.e( "handleMessage: ",arralist.get(1).getQuery() );
//                        Log.e( "handleMessage: ",arralist.get(1).getDate());
//                        Log.e( "handleMessage: ",arralist.get(1).getStatus());


                        recyclerView.setAdapter(new QueryAdapter(arralist,getActivity(),onclickQuery));
//                        adapter.notifyDataSetChanged();

//                        if(adapter == null) {
//                            Log.e("RecyclerView111", "No adapter attached; skipping layout");
//                        } else if(arralist== null) {
//                            Log.e("RecyclerView", "No layout manager attached; skipping layout");
//                        }
                    }
                }
                break;
            }
        }
    }

    private void sendData() {
//        ringProgressDialog = Views.showDialog(this);
        AlertDialogClass.PopupWindowShow(getActivity(), mainlayout);
        try {
            String vppId = Logics.getVppId(getActivity());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("VppId", vppId);
            Log.e("request", jsonObject.toString());
            byte data[] = jsonObject.toString().getBytes();
            new SendTOServer(getActivity(), requestSent, Const.MSGQUERYLIST, data,connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("sendData: ", e.getMessage());
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    public void connected() {

        Log.e( "connected: ","connected" );
        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(getActivity()),
                            "1",
                            Methods.getVersionInfo(getActivity()),
                            Methods.getlogsDateTime(), "QueryStatus",
                            Connectivity.getNetworkState(getActivity()),
                            getActivity());
                    ;
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        AlertDialogClass.PopupWindowDismiss();
        //        AlertDailog.ProgressDlgDiss();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendData();
                //TastyToast.makeText( getActivity(), "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        Log.e( "connected: ","serverNotAvailable00" );


        AlertDialogClass.PopupWindowDismiss();
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
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        Log.e( "connected: ","no internet" );

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
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");
        Log.e( "connected: ","ConnectToserver11" );

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
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
        Log.e( "connected: ","SocketDisconnected" );


        AlertDialogClass.PopupWindowDismiss();
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
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
                                    Methods.getlogsDateTime(), "QueryStatus",
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
