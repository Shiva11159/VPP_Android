package com.application.vpp.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Utility.Singleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPref {

    public static String vppid="vppid";
    public static String amount="amount";
    public static String state="state";

    public static String UPIPayment="UPIPayment";
    public static String UPIPaymentDONE="UPIPaymentDONE";

    public static String is_payment_p="is_payment_p";

    public static String isPanStatus="isPanStatus";
    public static String ispan_remark="ispan_remark";

    public static String isAdharStatus="isAdharStatus";
    public static String isAdharremark="isAdharremark";
    public static String LoggedIN="LoggedIN";

    public static String isBankStatus="isBankStatus";
    public static String isbank_remark="isbank_remark";
    public static String LogsALL="LogsALL";

    public static ArrayList<InserSockettLogs>arrayList;
    public static void savePreferences(Context mContext, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    public static String getPreferences(Context context, String keyValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(keyValue, "");

    }

    /**
     * @param mContext
     */
    public static void removeSharedPreferences(Context mContext, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.remove(key);
        editor.clear().apply();
    }

    //SOCKET LOGS HANDLED.... HERE ...

    public static ArrayList<InserSockettLogs> getLogsArrayList(ArrayList<InserSockettLogs>arrayList,String key, Context activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<InserSockettLogs>>() {}.getType();
//        if (arrayList==null){
//            arrayList=new ArrayList<>();
//        }
        return gson.fromJson(json, type);
    }

    ///insert logs here ...
    public static void insertSocketLogs(ArrayList<InserSockettLogs>arrayList,String vpp_id, String flag, String app_version, String log_date, String screenname, boolean isinternetAvailable,Context context){
        InserSockettLogs insertLogs=new InserSockettLogs(vpp_id,flag,app_version,log_date,screenname,isinternetAvailable);

        if (arrayList==null){
            arrayList=new ArrayList<>();
        }
        arrayList.add(insertLogs);
        SharedPref.saveLogsArrayList(arrayList,context);
    }

    public static void saveLogsArrayList(ArrayList<InserSockettLogs> list, Context activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("SocketLogs", json);
        editor.apply();     // This line is IMPORTANT !!!
    }
    public static void clearLogsArrayList(ArrayList<InserSockettLogs>arrayList,Context activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
        editor.remove("SocketLogs");
        editor.apply();     // This line is IMPORTANT !!!
        arrayList.clear();
    }


}
