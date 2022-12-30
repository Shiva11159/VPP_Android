package com.application.vpp.ClientServer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.ConnectionProcess;

/**
 * Created by Admin on 02/02/2016.
 */
public class ConnectTOServer extends AsyncTask<Object, Void, String> {
    private boolean showProgress;

    //region [ Fields ]

    private Context context;
    private ConnectionProcess process;

    //private CustomProgressDialog mDialog;
    private ProgressDialog mDialog;
    private String msg;
    //endregion

    //region [ Public methods ]

    //endregion

    //region [ Override methods of Async Task]

    @Override
    protected void onPreExecute() {
        try {
            super.onPreExecute();
//            if (showProgress)
            Const.dismiss=false;
                setDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Const.dismiss=true;
        try {
//            if (showProgress) {
                dismissDialog();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion

    //region [ Private methods ]

    private void setDialog() {
        if (context != null) {
            mDialog = new ProgressDialog(context);
            //mDialog = CustomProgressDialog.ctor(context);
            String strMsg;
            if (msg == null) {
                strMsg = "Connecting to server...";
            } else {
                strMsg = msg;
            }

            mDialog.setMessage(strMsg);
            mDialog.show();
        }
    }

    private void dismissDialog() {
        if (context != null) {
            mDialog.dismiss();
        }
    }

    private void connect() {
        Log.d("connect()", "connect: ");
        try {
            if(Const.tcpClient != null){
                Const.tcpClient.stopClient();
            }
            Const.tcpClient = null;
            Const.tcpClient = new TCPClient(process);
            Const.tcpClient.connect(Const.SERVER_IP,Const.SERVER_PORT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConnectTOServer(Context context,ConnectionProcess process) {
        this.context = context;
        this.process = process;
    }
}
