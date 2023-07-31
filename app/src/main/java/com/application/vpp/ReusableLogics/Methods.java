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

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Utility.SnackBar;
import com.application.vpp.activity.SplashScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Methods {

    public static boolean isValidIFSCCode(String str)
    {
        // Regex to check valid IFSC Code.
        String regex = "^[A-Z]{4}0[A-Z0-9]{6}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher()
        // method to find matching between
        // the given string and
        // the regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }
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

    public static String attemps(int left){

        String msg;

        if (left==1){
            msg= "Password incorrect " + left + " attempt left";

        }else {
           msg= "Password incorrect " + left + " attempts left";
        }
        return msg;
    }

}
