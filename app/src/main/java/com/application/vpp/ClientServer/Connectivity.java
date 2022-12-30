package com.application.vpp.ClientServer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.telephony.TelephonyManager;

import com.application.vpp.Const.Const;
import com.application.vpp.Views.Views;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by bpandey on 09-07-2018.
 */

public class Connectivity {
    private static final String tag = Connectivity.class.getSimpleName();
    static int i = 0;

//    public static NetworkInfo getNetworkInfo(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        return cm.getActiveNetworkInfo();
//    }

//    public static boolean isConnected(Context context) {
//        NetworkInfo info = getNetworkInfo(context);
//        return (info != null && info.isConnected());
//    }

//    public static boolean isConnectedFast(Context context) {
//        NetworkInfo info = getNetworkInfo(context);
//        if (info != null && info.isConnected()) {
//            if (!isConnectionFast(info.getType(), info.getSubtype())) {
////                baseActivity.showToast(Const.ERR_MSG_SLOW_CONNECTION);
////                Views.ShowMsg(context, Const.ERR_MSG_SLOW_CONNECTION, "");
//                return false;
//            }
//            return true;
//        }
//        return false;
////        return (info != null && info.isConnected() && isConnectionFast(info.getType(),info.getSubtype()));
//    }

//    public static boolean IsConnectedtoInternet(Context context){
//        try{
//            NetworkInfo info = getNetworkInfo(context);
////            BaseActivity baseActivity = (BaseActivity) context;
//            if (info!= null && info.isConnected()){
//                if (!isConnectionFast(info.getType(),info.getSubtype())){
//                    baseActivity.showToast(Const.ERR_MSG_SLOW_CONNECTION);
//                }
//
//            }
//            baseActivity.showToast(Const.ERR_SERVER_CONNECTION);
//
//        }catch (Exception e){
//            VenturaException.Print(e);
//        }
//        return false;
//    }


//    public static boolean isConnectionFast(int type, int subType) {
//        if (type == ConnectivityManager.TYPE_WIFI) {
//            return true;
//        } else if (type == ConnectivityManager.TYPE_MOBILE) {
//            switch (subType) {
//                case TelephonyManager.NETWORK_TYPE_1xRTT:
//                    return false; // ~ 50-100 kbps
//                case TelephonyManager.NETWORK_TYPE_CDMA:
//                    return false; // ~ 14-64 kbps
//                case TelephonyManager.NETWORK_TYPE_EDGE:
//                    return false; // ~ 50-100 kbps
//                case TelephonyManager.NETWORK_TYPE_EVDO_0:
//                    return true; // ~ 400-1000 kbps
//                case TelephonyManager.NETWORK_TYPE_EVDO_A:
//                    return true; // ~ 600-1400 kbps
//                case TelephonyManager.NETWORK_TYPE_GPRS:
//                    return false; // ~ 100 kbps
//                case TelephonyManager.NETWORK_TYPE_HSDPA:
//                    return true; // ~ 2-14 Mbps
//                case TelephonyManager.NETWORK_TYPE_HSPA:
//                    return true; // ~ 700-1700 kbps
//                case TelephonyManager.NETWORK_TYPE_HSUPA:
//                    return true; // ~ 1-23 Mbps
//                case TelephonyManager.NETWORK_TYPE_UMTS:
//                    return true; // ~ 400-7000 kbps
//                case TelephonyManager.NETWORK_TYPE_EHRPD:
//                    return true; // ~ 1-2 Mbps
//                case TelephonyManager.NETWORK_TYPE_EVDO_B:
//                    return true; // ~ 5 Mbps
//                case TelephonyManager.NETWORK_TYPE_HSPAP:
//                    return true; // ~ 10-20 Mbps
//                case TelephonyManager.NETWORK_TYPE_IDEN:
//                    return false; // ~25 kbps
//                case TelephonyManager.NETWORK_TYPE_LTE:
//                    return true; // ~ 10+ Mbps
//                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
//                default:
//                    return false;
//            }
//        } else {
//            return false;
//        }
//    }

    public static boolean getNetworkState(Context context) {
        boolean flag = false;
        try {
            if (isConnected(context)) {
                if (!isConnectedFast(context)) {
                    //GlobalClass.showToast(context, Constants.ERR_MSG_SLOW_CONNECTION);
//                    Views.ShowMsg(context,Const.ERR_MSG_SLOW_CONNECTION,"");
                    TastyToast.makeText(context, Const.ERR_MSG_SLOW_CONNECTION, TastyToast.ERROR, TastyToast.LENGTH_LONG);
                }
                flag = true;
            } else {

                flag = false;

                //GlobalClass.showToast(context, Constants.ERR_MSG_CONNECTION);
                //ERR_MSG_CONNECTION= "Please check your internet connection";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static Boolean isOnline1() {

//        boolean isAvailable = false;
        boolean isAvailable = true;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL("https://stackoverflow.com/");
            HttpURLConnection httpURLConnection = null;
            httpURLConnection = (HttpURLConnection) url.openConnection();

            // 2 second time out.`
            httpURLConnection.setConnectTimeout(2000);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                isAvailable = true;
            } else {
                isAvailable = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            isAvailable = false;
        }

        if (isAvailable) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean getNetworkState1(Context context) {
        boolean status = false;

        Const.g_b_isInternetslow =false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = true;
                if (isOnline1()) {
                    i = 0;
                } else {
                    i++;
                    if (i < 2) {
                        {
//                            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    TastyToast.makeText(context, "Your internet connection is too slow, it may take time, try after sometime", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                                    Const.isInternetslow=true;
//
//                                }
//                            });

//                            AlertDailog.nointernetavaialableslow("",context);
                            Const.g_b_isInternetslow =true;
                            TastyToast.makeText(context, "Your internet connection is too slow, it may take time, try after sometime", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                    }
                }
                return status;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = true;
                if (isOnline1()) {
                    i = 0;
                } else {
                    i++;
                    if (i < 2) {
                        i++;
//                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Const.isInternetslow=true;
//
//                                TastyToast.makeText(context, "Your internet connection is too slow, it may take time, try after sometime", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//
////                                AlertDailog.nointernetavaialableslow(context,"",context);
//
//                            }
//                        });
                        Const.g_b_isInternetslow =true;

//
                        TastyToast.makeText(context, "Your internet connection is too slow, it may take time, try after sometime", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//


                    }
                }
                return status;
            }
        } else {
            status = false;
            return status;
        }
        return status;
    }

//    public static boolean getNetworkState1(Context context) {
//        boolean flag = false;
//        try {
//            if (isConnected(context)) {
//                if (!isConnectedFast(context)) {
//                    //GlobalClass.showToast(context, Constants.ERR_MSG_SLOW_CONNECTION);
////                    Views.ShowMsg(context,Const.ERR_MSG_SLOW_CONNECTION,"");
//                    TastyToast.makeText(context, Const.ERR_MSG_SLOW_CONNECTION, TastyToast.ERROR, TastyToast.LENGTH_LONG);
//                }
//                flag = true;
//            } else {
//
//                flag = false;
//
//                //GlobalClass.showToast(context, Constants.ERR_MSG_CONNECTION);
//                //ERR_MSG_CONNECTION= "Please check your internet connection";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return flag;
//    }


    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && Connectivity.isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }

}