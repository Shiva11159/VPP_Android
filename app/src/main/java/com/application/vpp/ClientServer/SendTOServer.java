package com.application.vpp.ClientServer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.Views.Views;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by bpandey on 01-06-2018.
 */

public class SendTOServer extends AsyncTask<Object, Void, String> {
    SweetAlertDialog pDialog;
    Context context;
    RequestSent requestSent;
    int msgcode;
    byte data[];
    ConnectionProcess connectionProcess;
    @Override
    protected void onPreExecute() {
        try {
            super.onPreExecute();
//            if (showProgress)
//            Views.showDialog(context).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Object... objects) {
        sendData(data);
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        dismissDialog();
    }

    public SendTOServer(Context context, RequestSent requestSent, int msgcode, byte data[], ConnectionProcess connectionProcess) {
        this.context = context;
        this.requestSent = requestSent;
        this.msgcode = msgcode;
        this.data = data;
        this.connectionProcess = connectionProcess;
    }

    public SendTOServer(Context context, RequestSent requestSent, int msgcode, byte data[]) {
        this.context = context;
        this.requestSent = requestSent;
        this.msgcode = msgcode;
        this.data = data;

    }
    public SendTOServer(Context context, ConnectionProcess process) {
        this.context = context;
        this.connectionProcess = process;
    }

    //    private void sendData(byte data[]) {
//        try {
//            if (Connectivity.getNetworkState(context)) {
//                if (Const.isSocketConnected) {
//                    sendDataFinal(data);
//                } else {
//
//                    Activity activity = (Activity) context;
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            Views.toast(context,"Please check your internet connection");
//                            new ConnectTOServer(context,SendTOServer.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                            // connect();
//                        }
//                    });
//
//
//                    //GlobalClass.showToast(context, Constants.ERR_SERVER_CONNECTION);
//                    //Could not connect to server. Please check your internet connection
//
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void sendData(byte data[]) {
        try {
            if (Connectivity.getNetworkState(context)) {
                if (Const.isSocketConnected) {
                    sendDataFinal(data);

                } else if (!Const.isSocketConnected){
                    Activity activity = (Activity) context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            connectionProcess.SocketDisconnected();
                        }
                    });
                    //GlobalClass.showToast(context, Constants.ERR_SERVER_CONNECTION);
                    //Could not connect to server. Please check your internet connection
                }else if (Const.isServerConnected) {
                    connectionProcess.serverNotAvailable();

                }
            } else {
                Activity activity = (Activity) context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Views.toast(context,"Please check your internet connection");
//                        DateTimePicker.toast(context, "Please check your internet connection");
                        connectionProcess.IntenrnetNotAvailable();
                    }
                });
//            else {
//                //DateTimePicker.toast(context, "Please check your internet connection");
//                Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
//            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendDataFinal(byte data[]) {
        try {
            TCPClient tcpClient;
            byte[] bytearray = new byte[0];
            tcpClient = Const.tcpClient;
            bytearray = tcpClient.getByteArr(msgcode, data);
            tcpClient.send(bytearray);
            requestSent.requestSent(msgcode);
            Log.d("msgCode", "sendData: " + msgcode + "," + data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void connect() {
//        Log.d("connect()", "connect: ");
//        try {
//            if(Const.tcpClient != null){
//                Const.tcpClient.stopClient();
//            }
//            Const.tcpClient = null;
//            Const.tcpClient = new TCPClient(SendTOServer.this);
//            Const.tcpClient.connect(Const.SERVER_IP,Const.SERVER_PORT);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
private void setDialog() {

}

    private void dismissDialog() {
        pDialog.dismiss();
        pDialog.cancel();
    }



}
