package com.application.vpp.ClientServer;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.application.vpp.activity.AddLead;
import com.application.vpp.activity.AuthenticateUpdateProfile;
import com.application.vpp.activity.BankValidation;
import com.application.vpp.activity.BrokerageList;
import com.application.vpp.activity.CallBackDetailsList;
import com.application.vpp.activity.ClientList;
import com.application.vpp.activity.Dashboard;
import com.application.vpp.activity.DiscripancyActivity;
import com.application.vpp.activity.InProcessLeads;
import com.application.vpp.activity.LoginScreen;
import com.application.vpp.activity.MyLeads;
import com.application.vpp.activity.NavigationDrawer;
import com.application.vpp.activity.NotInterested;
import com.application.vpp.activity.OtpVerfication;
import com.application.vpp.activity.PanValidation;
import com.application.vpp.activity.Payout;
import com.application.vpp.activity.Profile;
import com.application.vpp.activity.QueryStatus;
import com.application.vpp.activity.RejectedList;
import com.application.vpp.activity.SignupScreen;
import com.application.vpp.activity.SignupScreen2;
import com.application.vpp.activity.SplashScreen;
import com.application.vpp.activity.SubPatnerActivity;
import com.application.vpp.activity.UpdateOtpVerify;
import com.application.vpp.activity.UpiPayment;
import com.application.vpp.activity.Welcome;
import com.application.vpp.Const.Const;
import com.application.vpp.ContactUsFragment.BranchLocatorFragment;
import com.application.vpp.ContactUsFragment.ConnectFragment;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Razorpay.RazorpayActivity;

import java.io.IOException;
import java.util.Date;

import lib.TCPClientProcess;
import lib.XtremClientHandler;

/*pankaj*/

/**
 * @author XtremsoftTechnologies
 */
public class TCPClient extends Client implements TCPClientProcess {

    private final String className = getClass().getName();
    private String TAG = "TCP Connection";
    private ConnectionProcess process;
    Message msg = null;

    public TCPClient(ConnectionProcess process) {
        try {
            this.process = process;
            client = new XtremClientHandler("ClientList", "Connect to Server", 8, 24, this);
            client.setIsHtBt(false);
        } catch (Exception ex) {
            onError("Error in in creating object:" + getClass().getName(), ex);
        }
    }

    public void stopClient() {
        try {
            disconnect(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    private void initClients(boolean isServerNotAvailable) {
//        if (process != null) {
//            if (isServerNotAvailable) {
//                process.serverNotAvailable();
//            }
//            process = null;
//        }
//    }
    // nirmalsir said aumuser still facing  issue shared some client codes.
    //called sagar sir, so theytold one old project they did changes. i asked to call devidas.

    @Override
    public void socketConnected() {
//        createLog("socketConnected");
        Log.e("socketConnected", "socketConnected");
        initReconnectionValues();
        Const.isSocketConnected = true;
        process.connected();
    }

//    @Override
//    public void socketConnected() {
////        createLog("socketConnected");
//        Log.e("connection" ,"socketConnected: ");
//
//        initReconnectionValues();
//        Const.isSocketConnected = true;
//        msg = new Message();
//        msg.arg1 = 1;
//        if(NavigationDrawer.handlerNavigation!=null) {
//            NavigationDrawer.handlerNavigation.sendMessage(msg);
//        }
//        if(process != null){
//            process.connected();
//            process = null;
//        }
//    }

    @Override
    public void socketDisconnected() {
        try {
            Log.e("socketDisconnected1", "socketDisconnected1");
            Const.isSocketConnected = false;
//            process.SocketDisconnected();
//            if (!isManualDisconnected) {
//                createLog("Reconnect : " + count);
//              //  tryToReconnect();
//            }
//            isManualDisconnected = false;


            // tryToReconnect(0);

        } catch (Exception e) {
            onError("Error in " + className, e);
        }
    }

    @Override
    public void socketNotAvailable() {
        try {
            Log.e("serverNotAvailable", "serverNotAvailable:");
//            process.serverNotAvailable();
            Const.isServerConnected = true;
//            initClients(true);
            // tryToReconnect();
        } catch (Exception e) {
            onError("Error in in " + className, e);
        }
    }

//    @Override
//    public void socketNotAvailable() {
//        try {
//            Log.e("connection" ,"socketNotAvailable: ");
//            Const.isSocketConnected = false;
//            msg = new Message();
//            msg.arg1 = 0;
//            NavigationDrawer.handlerNavigation.sendMessage(msg);
//            initClients(true);
//            tryToReconnect();
//
//        } catch (Exception e) {
//            onError("Error in in " + className, e);
//        }
//    }

//    @Override
//    public void socketDisconnected() {
//        try {
////            createLog("socketDisconnected1");
//            Log.e("connection" ,"socketDisconnected1: ");
//
//            Const.isSocketConnected = false;
//            msg = new Message();
//            msg.arg1 = 0;
//            NavigationDrawer.handlerNavigation.sendMessage(msg);
//            if (!isManualDisconnected) {
//                createLog("Reconnect : " + count);
//                tryToReconnect();
//            }
//            isManualDisconnected = false;
//        } catch (Exception e) {
//            onError("Error in " + className, e);
//        }
//    }

    private void tryToReconnect(int i) {
        if (maxTry > 2) {

            // display msg(You have been disconnected from) and on ok click exit.

        } else {
            reConnection();
        }
    }

    private long delay = 0;
    private int count = 1;
    private int maxTry = 0;
    private Handler handler;
    private Runnable runnable;

    public void initReconnectionValues() {
        delay = 0;
        count = 1;
        maxTry = 0;
        handler = null;
        runnable = null;
    }

    public void reConnection() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
        }
        runnable = new Runnable() {
            public void run() {
                try {
                    if (maxTry < 3) {
                        if (count == 10) {
                            maxTry++;
                            count = 1;
                            delay = 60000;
                        } else {
                            count++;
                            delay = 6000;
                        }
                        if (cnt == null) {
                            cnt = new connect();
                        } else {
                            cnt.cancel(true);
                            cnt = null;
                            cnt = new connect();
                        }
                        cnt.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, delay);
    }

    private connect cnt = null;

    private class connect extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                createLog("connect: ");
                Const.tcpClient.connect(Const.SERVER_IP, Const.SERVER_PORT);
            } catch (Exception e) {
                onError("Error in connect method: " + getClass().getName(), e);
            }
            return null;
        }
    }

    @Override
    public void dataSendingFailed(byte[] data) {
        System.err.println("data sending failed");
    }


    @Override
    public void onDataReceived(byte[] bytes, int i) {
        System.out.println("msgCode:" + i);
        Log.d(TAG, "onDataReceived: ");
        onDataProcess(bytes, i);
    }

    @Override
    public void dataSent(byte[] bytes) {
        createLog("dataSent");
    }

    @Override
    public void onError(String string, Exception excptn) {
        System.err.println(string);
        if (excptn != null) {
            excptn.printStackTrace();
        }
    }

    @Override
    public void createLog(String string) {

        System.out.println(string + ":" + new Date());

    }

    private void onDataProcess(byte data[], int msgCode) {
        try {

            String response = new String(data, "UTF-8");
            Log.d("msgCode", "onDataProcess: " + msgCode);

            Message msg = new Message();
            msg.obj = response;
            msg.arg1 = msgCode;

            ProcessData(msgCode, msg);

        } catch (Exception e) {
            onError("Error in in data receive:" + className, e);
        }
    }


    @SuppressLint("LongLogTag")
    private void ProcessData(int msgCode, Message msg) {

        switch (msgCode) {

            case Const.MSGLOGIN: {
                LoginScreen.handlerLogin.sendMessage(msg);
            }
            break;
            case Const.MSGSIGNUP: {
                SignupScreen.handlerSignup.sendMessage(msg);
            }
            break;

            case Const.MSGVERIFYVPP: {
                if (SplashScreen.handlerSplash != null) {
                    SplashScreen.handlerSplash.sendMessage(msg);
                } else if (Dashboard.handlerDashboard != null) {
                    Dashboard.handlerDashboard.sendMessage(msg);
                }

            }
            break;

            case Const.MSGADDLEAD: {
                AddLead.handlerAddProspect.sendMessage(msg);
            }
            break;
            case Const.MSGSOCKETCONNECTEDADDLEAD: {
                AddLead.handlerAddProspect.sendMessage(msg);
            }
            break;
            case Const.MSGFETCHLEADDETAILS: {
//                Log.e("!!!MSGFETCHLEADDETAILS: ", String.valueOf(msg));
//                String data = (String) msg.obj;
//                Log.e("MSGFETCHLEADDETAILS!!!!!", data);
                MyLeads.handlerLeadList.sendMessage(msg);
            }
            break;
            case Const.MSGSOCKETCONNECTEDMYLEADS: {
                MyLeads.handlerLeadList.sendMessage(msg);
            }
            break;
            case Const.MSGFETCHBROKERAGELIST: {
                BrokerageList.handlerLeadList.sendMessage(msg);
            }
            break;
            case Const.MSGFETCHCLIENTLIST: {
                ClientList.handlerClientList.sendMessage(msg);
            }
            break;
            case Const.MSGFETCHDASHBOARD: {
                if (Dashboard.handlerDashboard != null) {
                    Dashboard.handlerDashboard.sendMessage(msg);
                } else {
                    //DashoboardDesign.handlerDashoboardDesign.sendMessage(msg);
                }
            }
            break;
            case Const.MSGFETCHDASHBOARDDesign: {
                if (Dashboard.handlerDashboard != null) {
                    Dashboard.handlerDashboard.sendMessage(msg);
                } else {
                    //  DashoboardDesign.handlerDashoboardDesign.sendMessage(msg);
                }
            }
            break;
            case Const.MSGPRODUCTMASTER: {
                Dashboard.handlerDashboard.sendMessage(msg);
            }
            break;
            case Const.MSGFETCHVPPDETAILSONLOGIN: {
                OtpVerfication.otpVerfhandler.sendMessage(msg);
                Log.e("msg1111111", String.valueOf(msg));
            }
            break;

            case Const.MSGFETCHVPPDETAILS: {
                Welcome.handlerWelcome.sendMessage(msg);
            }
            break;

            case Const.MSGVERIFYOTP: {
                Welcome.handlerWelcome.sendMessage(msg);
            }
            break;

            //  case Const.MSGVERIFYOTP:{LoginScreen.handlerLogin.sendMessage(msg);} break;

            case Const.MSGFETCHLEADDETAILREPORT: {
                Dashboard.handlerDashboard.sendMessage(msg);
            }
            break;

            case Const.MSGFETCHLEADDETAILCLIENT: {
                ClientList.handlerClientList.sendMessage(msg);
            }
            break;

            case Const.MSGFETCHLEADDETAILDEAD: {
                NotInterested.handlerNotInterested.sendMessage(msg);
            }
            break;

            case Const.MSGFETCHLEADINPROCESS: {
                if (InProcessLeads.handlerInProcessLeads != null) {
                    InProcessLeads.handlerInProcessLeads.sendMessage(msg);
                } else {
                    Log.e("handlerInProcessLeads", "handlerInProcessLeads");
                }
            }
            break;

            case Const.MSGSOCKETCONNECTEDINPROCESS: {
                InProcessLeads.handlerInProcessLeads.sendMessage(msg);
            }
            break;

            case Const.MSGFETCHLEADREJECTED: {
                RejectedList.handlerRejectedList.sendMessage(msg);
            }
            break;

            case Const.MSGSOCKETCONNECTEDREJECTED: {
                RejectedList.handlerRejectedList.sendMessage(msg);
            }
            break;

            case Const.MSGFETCHVERSION: {
                Dashboard.handlerDashboard.sendMessage(msg);
            }
            break;

            case Const.MSGFETCHPAYOUT: {
                Payout.handlerLeadList.sendMessage(msg);
            }
            break;

            case Const.MSGFETCHDOCSTAT: {
                //Log.e("ProcessData00: ", String.valueOf(Profile.handlerProfile));
                //Log.e("ProcessData11: ", String.valueOf(DiscripancyActivity.handlerDiscripancy));
                //Log.e("ProcessData22: ", String.valueOf(Dashboard.handlerDashboard));
                if (Profile.handlerProfile != null) {
                    Profile.handlerProfile.sendMessage(msg);
                } else if (DiscripancyActivity.handlerDiscripancy != null) {
                    DiscripancyActivity.handlerDiscripancy.sendMessage(msg);
                } else if (Dashboard.handlerDashboard != null) {
                    Dashboard.handlerDashboard.sendMessage(msg);
                }
            }
            break;
            case Const.MSGSIGNUP2: {
                SignupScreen2.signupHandler.sendMessage(msg);
            }
            break;

            case Const.MSGSAVEPAN: {
                PanValidation.handlerPan.sendMessage(msg);
            }
            break;
            case Const.MSGCHECKPAN: {
                PanValidation.handlerPan.sendMessage(msg);
            }
            break;
            case Const.MSGUpdateEMailMobile: {
                LoginScreen.handlerLogin.sendMessage(msg);
            }
            break;
            case Const.MSGUPDATENAMEBANK: {
                BankValidation.handlerBank.sendMessage(msg);
            }
            break;

            case Const.MSGVERIFYOTPPAN: {
                OtpVerfication.otpVerfhandler.sendMessage(msg);
            }
            break;

            case Const.MSGRESENDOTP: {
                OtpVerfication.otpVerfhandler.sendMessage(msg);
            }
            break;
            case Const.MSGQUERY: {
                ConnectFragment.handlerConnect.sendMessage(msg);
            }
            break;
            case Const.MSGCALLBACK: {
                ConnectFragment.handlerConnect.sendMessage(msg);
            }
            break;

            case Const.MSGBRANCHLOCATOR: {
                BranchLocatorFragment.handlerBranchLocator.sendMessage(msg);
            }
            break;

            case Const.MSGUPDATEPROFILE: {
                AuthenticateUpdateProfile.resendAuthenticateHandler.sendMessage(msg);
            }
            break;
            case Const.MSGUPDATEPAYMENTSTATUS: {
                Dashboard.handlerDashboard.sendMessage(msg);
            }
            break;
            case Const.MSGAUTHENTICATE: {
                if (Profile.handlerProfile != null) {
                    Profile.handlerProfile.sendMessage(msg);
                } else {
                    OtpVerfication.otpVerfhandler.sendMessage(msg);
                }
            }
            break;
            case Const.MSGUPDATCONTACT: {
                UpdateOtpVerify.otpVerfhandler.sendMessage(msg);
            }
            break;
            case Const.MSGAUTHENTICATERESEND: {
                AuthenticateUpdateProfile.resendAuthenticateHandler.sendMessage(msg);
            }
            break;

            case Const.MSGUPDATERESEND: {
                UpdateOtpVerify.otpVerfhandler.sendMessage(msg);
            }
            break;
            case Const.MSGSAVEGPAYRESPONSE: {
                UpiPayment.upiHandler.sendMessage(msg);
            }
            break;
            case Const.MSGSAVEBANKDETAILS: {
                BankValidation.handlerBank.sendMessage(msg);
            }
            break;
            case Const.MSGCREATEORDEROBJ: {
                //RazorpayActivity.handlerrazorpay.sendMessage(msg);
                UpiPayment.upiHandler.sendMessage(msg);
            }
            break;
            case Const.MSGVALIDATESIGNATURE: {
                //RazorpayActivity.handlerrazorpay.sendMessage(msg);
                UpiPayment.upiHandler.sendMessage(msg);
            }
            break;
            case Const.MSGSAVECHECKOUT: {
                //RazorpayActivity.handlerrazorpay.sendMessage(msg);
                UpiPayment.upiHandler.sendMessage(msg);
            }
            break;
            case Const.MSGQUERYLIST: {
                QueryStatus.handlerQueryList.sendMessage(msg);
                //QueryFragment.handlerQueryList1.sendMessage(msg);
            }
            break;
            case Const.MONTHLYLEAD: {
                // DashoboardDesign.handlerDashoboardDesign.sendMessage(msg);
            }
            break;
            case Const.MSGMONTHLYEARNING: {
                // DashoboardDesign.handlerDashoboardDesign.sendMessage(msg);
            }
            break;
            case Const.MSGTECHPROCREQ: {
                UpiPayment.techprocessHandler.sendMessage(msg);
            }
            break;
            case Const.MSGSUBPARTNERCATEGORY: {
                SubPatnerActivity.handlerSubpartner.sendMessage(msg);
            }
            break;
            case Const.MSGTECHPROCRESP: {
                UpiPayment.techprocessHandler.sendMessage(msg);
            }
            break;

            case Const.MSGMOUNT: {
                if (RazorpayActivity.handlerrazorpay != null) {
                    RazorpayActivity.handlerrazorpay.sendMessage(msg);
                } else {
                    UpiPayment.upiHandler.sendMessage(msg);
                }
            }
            break;
            case Const.MSG_GST: {
                UpiPayment.upiHandler.sendMessage(msg);
            }
            break;
            case Const.MSGCallPromocode: {
                UpiPayment.upiHandler.sendMessage(msg);
            }
            break;
            case Const.MSGCallbackdetails: {
                CallBackDetailsList.handlerCallback.sendMessage(msg);
            }
            break;
            case Const.MSG_StateCity: {
                SignupScreen2.signupHandler.sendMessage(msg);
            }
            break;
            case Const.MSGBANKVERIFICATION: {
                BankValidation.handlerbankverify.sendMessage(msg);
            }
            break;
            case Const.MSGPERSONALIZED_LINK_FOR_ACCOUNT_OPENING: {
                NavigationDrawer.handlerNavigation.sendMessage(msg);
            }
            break;
        }
    }
}
