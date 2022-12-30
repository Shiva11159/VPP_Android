package com.application.vpp.ReusableLogics;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.activity.SplashScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Methods {

    public static void hideKeyboard(Context context, View view){
        InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
    }
    public static String getday(Date date) {
        String dayOfTheWeek = (String) DateFormat.format("EE", date); // Thursday
        return dayOfTheWeek;
    }

    public static String getmonth(Date date) {
        String monthString = (String) DateFormat.format("MMM", date); // Jun
        return monthString;
    }

    public static String getdate(Date date) {
        String day = (String) DateFormat.format("dd", date); // 20
        return day;
    }
    public static String getyear(Date date) {
        String year = (String) DateFormat.format("yyyy", date); // 2013
        return year;
    }
    public static String getVersionInfo(Context context) {
        String versionName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName="00";
         //   AlertDialogClass.ShowMsg(context, e.getMessage());

        }

        Log.d("versionName", "getVersionInfo: " + versionName);
        return versionName;

    }

    public static String getlogsDateTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }

}
