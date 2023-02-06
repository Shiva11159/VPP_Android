package com.application.vpp.ReusableLogics;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.application.vpp.Const.Const;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by bpandey on 06-06-2018.
 */

public class Logics {

    public static String timeSet(int minute, int hourOfDay) {

        String time;

        if (minute < 10 & hourOfDay < 10) {

            time = "0" + hourOfDay + ":" + "0" + minute;
        } else if (minute < 10 & hourOfDay > 9) {
            time = hourOfDay + ":" + "0" + minute;
        } else if (hourOfDay < 10 & minute > 9) {

            time = "0" + hourOfDay + ":" + minute;
        } else {
            time = hourOfDay + ":" + minute;
        }

        return time;
    }

    public static String dateSet(int day, int month, int year) {
        String date;
        int monthValue = month + 1;
        if (monthValue < 10 & day < 10) {
            date = "0" + day + "-" + "0" + monthValue + "-" + year;
        } else if (monthValue < 10 & day > 9) {
            date = day + "-" + "0" + monthValue + "-" + year;
        } else if (day < 10 & monthValue > 9) {
            date = "0" + day + "-" + monthValue + "-" + year;
        } else {
            date = day + "-" + monthValue + "-" + year;
        }
        return date;
    }

    public static void setDeviceID(Context context, String deviceId, String simNumber) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.deviceID, MODE_PRIVATE).edit();
        sharedPreferences.putString(Const.deviceID, deviceId);
        sharedPreferences.putString(Const.simID, simNumber);
        sharedPreferences.apply();

    }

    public static String getDeviceID(Context context) {

        String deviceID;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.deviceID, MODE_PRIVATE);
        deviceID = sharedPreferences.getString(Const.deviceID, null);

        return deviceID;


    }

    public static void setVppDetails(Context context, String name, String number, String email, String city, String vppId, String pan_no) {


        Log.d("Data", "setVppDetails: " + name + number + city + vppId);
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.vppDetails, MODE_PRIVATE).edit();
        sharedPreferences.putString(Const.vpp_name, name);
        sharedPreferences.putString(Const.vpp_mobile, number);
        sharedPreferences.putString(Const.vpp_email, email);
        sharedPreferences.putString(Const.vpp_city, city);
        sharedPreferences.putString(Const.vpp_id, vppId);
        sharedPreferences.putString(Const.vpp_pan, pan_no);
        sharedPreferences.apply();

    }

    public static ArrayList<String> getProfile(Context context) {

        ArrayList<String> profileList = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppDetails, MODE_PRIVATE);
        String vppid = sharedPreferences.getString(Const.vpp_id, null);
        String vppname = sharedPreferences.getString(Const.vpp_name, null);
        String vppcity = sharedPreferences.getString(Const.vpp_city, null);
        String vpp_mobile = sharedPreferences.getString(Const.vpp_mobile, null);
        String vpp_pan = sharedPreferences.getString(Const.vpp_pan, null);
        String email = sharedPreferences.getString(Const.vpp_email, null);

        Log.d("Data", "getProfile: " + vppid + vppname + vppcity + vpp_mobile);

        profileList.add(vppid);
        profileList.add(vppname);
        profileList.add(vppcity);
        profileList.add(vpp_mobile);
        profileList.add(vpp_pan);
        profileList.add(email);
        return profileList;
    }


    public static String getVppName(Context context) {

        String name;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppDetails, MODE_PRIVATE);
        name = sharedPreferences.getString(Const.vpp_name, null);
        return name;
    }

    public static String getVppId(Context context) {

        String vpp_id;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppDetails, MODE_PRIVATE);
        vpp_id = sharedPreferences.getString(Const.vpp_id, null);
        return vpp_id;
    }


    public static String getSimId(Context context) {

        String simID;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.deviceID, MODE_PRIVATE);
        simID = sharedPreferences.getString(Const.simID, null);
        return simID;
    }


    public static String getEmail(Context context) {

        String email;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppDetails, MODE_PRIVATE);
        email = sharedPreferences.getString(Const.vpp_email, null);
        return email;
    }

    public static String getPanNo(Context context) {

        String panNo;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppDetails, MODE_PRIVATE);
        panNo = sharedPreferences.getString(Const.vpp_pan, null);
        return panNo;
    }

    public static void setInstScreenVal(Context context, int value) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("ScreenVal", MODE_PRIVATE).edit();
        sharedPreferences.putInt("isFirst", value);
        sharedPreferences.apply();


    }

    public static int getInstScreenVal(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("ScreenVal", MODE_PRIVATE);
        int val = sharedPreferences.getInt("isFirst", 0);
        return val;
    }

    public static void setContact(Context context, String number) {


        Log.d("Data", "setVppDetails: " + number);
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.vppDetails, MODE_PRIVATE).edit();
        sharedPreferences.putString(Const.vpp_mobile, number);
        sharedPreferences.apply();

    }

    public static String getContact(Context context) {

        ArrayList<String> profileList = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppDetails, MODE_PRIVATE);
        String vpp_mobile = sharedPreferences.getString(Const.vpp_mobile, null);

        Log.d("Data", "getProfile: " + vpp_mobile);

        profileList.add(vpp_mobile);
        return vpp_mobile;
    }

    public static void setEmail(Context context, String number) {


        Log.d("Data", "setVppDetails: " + number);
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.vppDetails, MODE_PRIVATE).edit();
        sharedPreferences.putString(Const.vpp_email, number);
        sharedPreferences.apply();

    }

    public static void setDate(Context context, String date) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.today, MODE_PRIVATE).edit();
        sharedPreferences.putString(Const.today, date);
        sharedPreferences.apply();

    }

    public static String getDate(Context context) {

        String today;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.today, MODE_PRIVATE);
        today = sharedPreferences.getString(Const.today, null);

        return today;


    }

    public static void setTokenID(Context context, String tokenId) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("TokenId", MODE_PRIVATE).edit();
        sharedPreferences.putString("reg_token", tokenId);
        sharedPreferences.apply();

    }

    public static String getTokenID(Context context) {

        String tokenID;
        SharedPreferences sharedPreferences = context.getSharedPreferences("TokenId", MODE_PRIVATE);
        tokenID = sharedPreferences.getString("reg_token", null);

        return tokenID;


    }

    public static void setVppPanDetails(Context context, String pan, String panName, int isPan) {
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("vppPanDetails", MODE_PRIVATE).edit();
        sharedPreferences.putString("pan", pan);
        sharedPreferences.putString("panName", panName);
        sharedPreferences.putInt("isPan_v", isPan);
        sharedPreferences.apply();

    }

    public static String getPan(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("vppPanDetails", MODE_PRIVATE);
        String val = sharedPreferences.getString("pan", null);
        return val;
    }

    public static String getPanName(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("vppPanDetails", MODE_PRIVATE);
        String val = sharedPreferences.getString("panName", null);
        return val;
    }

    public static int getisPanVerified(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("vppPanDetails", MODE_PRIVATE);
        int val = sharedPreferences.getInt("isPan_v", 0);
        return val;
    }

    public static void setOtpVerificationDetails(Context context, String number, String email, int isMobile_V, int isEmail_V, int isRegistered) {
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.vppOtpDetails, MODE_PRIVATE).edit();
        sharedPreferences.putString("Mobilev", number);
        sharedPreferences.putString("Emailv", email);
        sharedPreferences.putInt("isMobileV", isMobile_V);
        sharedPreferences.putInt("isEmailV", isEmail_V);
        sharedPreferences.putInt("isReg", isRegistered);

        sharedPreferences.apply();

    }
    public static void setmobileNo(Context context, String number) {
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.vppOtpDetails, MODE_PRIVATE).edit();
        sharedPreferences.putString("Mobilev", number);
        sharedPreferences.apply();

    }

    public static String getMobile_1(Context context) {
        String mobile;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppOtpDetails, MODE_PRIVATE);
        mobile = sharedPreferences.getString("Mobilev", null);
        return mobile;
    }

//    public static void setmobileNO_lINK(Context context, String number) {
//        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("link", MODE_PRIVATE).edit();
//        sharedPreferences.putString("link", number);
//        sharedPreferences.apply();
//
//    }
//
//    public static String getMobile_LINK(Context context) {
//        String mobile;
//        SharedPreferences sharedPreferences = context.getSharedPreferences("link", MODE_PRIVATE);
//        mobile = sharedPreferences.getString("link", null);
//        return mobile;
//    }

    public static String getEmail_1(Context context) {

        String email;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppOtpDetails, MODE_PRIVATE);
        email = sharedPreferences.getString("Emailv", null);
        return email;
    }



    public static int getisMobile_V(Context context) {

        int isMobile_V;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppOtpDetails, MODE_PRIVATE);
        isMobile_V = sharedPreferences.getInt("isMobileV", 0);
        return isMobile_V;
    }

    public static int getisEmail_V(Context context) {

        int isEmail_V;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppOtpDetails, MODE_PRIVATE);
        isEmail_V = sharedPreferences.getInt("isEmailV", 0);
        return isEmail_V;
    }

    public static int getisRegisterd(Context context) {

        int isReg;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppOtpDetails, MODE_PRIVATE);
        isReg = sharedPreferences.getInt("isReg", 0);
        return isReg;
    }

    public static void setfmlName(Context context, String fName, String mName, String lName) {


        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.fmlName, MODE_PRIVATE).edit();
        sharedPreferences.putString(Const.fName, fName);
        sharedPreferences.putString(Const.mName, mName);
        sharedPreferences.putString(Const.lName, lName);

        sharedPreferences.apply();


    }


    public static String[] getfml(Context context) {

        String[] name = new String[3];

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.fmlName, MODE_PRIVATE);

        name[0] = sharedPreferences.getString(Const.fName, null);
        name[1] = sharedPreferences.getString(Const.mName, null);

        name[2] = sharedPreferences.getString(Const.lName, null);

        return name;

    }

    public static void showAlertDialog(final Context context, String msg) {
        boolean reLogin = false;
        if (msg.toLowerCase().contains("inactive session")) {
            msg = "Session expired. Please Re-login.";
            reLogin = true;
        }
        if (msg.toLowerCase().contains("not properly padded")) {
            msg = "Please Re-Login.";
            reLogin = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        /*TextView myMsg = new TextView(context);
        myMsg.setText(msg);
        myMsg.setGravity(Gravity.LEFT);
        builder.setView(myMsg);*/
        builder.setMessage(msg);
        final String finalMsg = msg;
        final boolean finalReLogin = reLogin;
        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        TextView alertTV = (TextView) alert.findViewById(android.R.id.message);
        alertTV.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    public static void setVppBankDetails(Context context, String BankAccNo, String Ifsc, String BankName, String TxnId, String respCode, String response, String nwrespcode, String nwrespmessg, String nwtxnrefid, int isBankVerified) {
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE).edit();
        sharedPreferences.putString("BankAccNo", BankAccNo);
        sharedPreferences.putString("Ifsc", Ifsc);
        sharedPreferences.putString("BankName", BankName);  // cust name ..
        sharedPreferences.putString("TxnId", TxnId);
        sharedPreferences.putString("respCode", respCode);
        sharedPreferences.putString("response", response);
        sharedPreferences.putString("nwrespcode", nwrespcode);
        sharedPreferences.putString("nwrespmessg", nwrespmessg);
        sharedPreferences.putString("nwtxnrefid", nwtxnrefid);

        sharedPreferences.putInt("isBankVerified", isBankVerified);

        sharedPreferences.apply();

    }

    public static String getBankIfsc(Context context) {
        String ifsc;

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE);
        ifsc = sharedPreferences.getString("Ifsc", null);
        return ifsc;
    }

    public static String getBankAccNo(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE);
        String val = sharedPreferences.getString("BankAccNo", null);
        return val;
    }

    public static String getVppBankName(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE);
        String val = sharedPreferences.getString("BankName", null);
        return val;
    }

    public static int getisBankVerified(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE);
        int val = sharedPreferences.getInt("isBankVerified", 0);
        return val;
    }

    public static void  setIsBankVerified(Context context , int isBankVerified) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE).edit();
        sharedPreferences.putInt("isBankVerified", isBankVerified);

        sharedPreferences.apply();


    }



    public static String getVppTxnId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE);
        String val = sharedPreferences.getString("TxnId", null);
        return val;
    }

    public static String getrespCode(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE);
        String val = sharedPreferences.getString("respCode", null);
        return val;
    }

    public static String getresponse(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE);
        String val = sharedPreferences.getString("response", null);
        return val;
    }

    public static String getnwrespcode(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE);
        String val = sharedPreferences.getString("nwrespcode", null);
        return val;
    }

    public static String getnwrespmessg(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE);
        String val = sharedPreferences.getString("nwrespmessg", null);
        return val;
    }

    public static String getnwtxnrefid(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppBankDetails, MODE_PRIVATE);
        String val = sharedPreferences.getString("nwtxnrefid", null);
        return val;
    }

    public static void setLoginPan(Context context, String value) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("loginPan", MODE_PRIVATE).edit();
        sharedPreferences.putString("isFirst", value);
        sharedPreferences.apply();


    }

    public static String getLoginPan(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("loginPan", MODE_PRIVATE);
        String val = sharedPreferences.getString("isFirst", null);
        return val;
    }
//    public static void setVppAddressDetails(Context context,String houseNum,String Area,String landmark,String city,String state,String pincode,String dob,String proffession){
//
//
//
//        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.vppAddressDetails,MODE_PRIVATE).edit();
//
//        sharedPreferences.putString("houseNum",houseNum);
//        sharedPreferences.putString("Area",Area);
//        sharedPreferences.putString("landmark",landmark);
//        sharedPreferences.putString("city",city);
//        sharedPreferences.putString("state",state);
//        sharedPreferences.putString("pincode",pincode);
//        sharedPreferences.putString("dob",dob);
//        sharedPreferences.putString("proffession",proffession);
//        sharedPreferences.apply();
//
//    }
//    public static String gethouseNum(Context context){
//
//        String houseNum;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppAddressDetails,MODE_PRIVATE);
//        houseNum =  sharedPreferences.getString("houseNum",null);
//        return houseNum;
//    }
//    public static String getArea(Context context){
//
//        String Area;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppAddressDetails,MODE_PRIVATE);
//        Area =  sharedPreferences.getString("Area",null);
//        return Area;
//    }
//    public static String getLandmark(Context context){
//
//        String landmark;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppAddressDetails,MODE_PRIVATE);
//        landmark =  sharedPreferences.getString("landmark",null);
//        return landmark;
//    }
//    public static String getCity(Context context){
//
//        String city;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppAddressDetails,MODE_PRIVATE);
//        city =  sharedPreferences.getString("city",null);
//        return city;
//    }
//    public static String getState(Context context){
//
//        String state;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppAddressDetails,MODE_PRIVATE);
//        state =  sharedPreferences.getString("state",null);
//        return state;
//    }
//    public static String getPincode(Context context){
//
//        String pincode;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppAddressDetails,MODE_PRIVATE);
//        pincode =  sharedPreferences.getString("pincode",null);
//        return pincode;
//    }
//    public static String getDob(Context context){
//
//        String dob;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppAddressDetails,MODE_PRIVATE);
//        dob =  sharedPreferences.getString("dob",null);
//        return dob;
//    }
//    public static String getProffession(Context context){
//
//        String proffession;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.vppAddressDetails,MODE_PRIVATE);
//        proffession =  sharedPreferences.getString("proffession",null);
//        return proffession;
//    }

    public static void setIsImgUpload(Context context, int value) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("isImgUpload", MODE_PRIVATE).edit();
        sharedPreferences.putInt("isFirst", value);
        sharedPreferences.apply();


    }

    public static int getIsImgUpload(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("isImgUpload", MODE_PRIVATE);
        int val = sharedPreferences.getInt("isFirst", 0);
        return val;
    }

    public static void setRef(Context context, String value) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("refVal", MODE_PRIVATE).edit();
        sharedPreferences.putString("isFirst", value);
        sharedPreferences.apply();


    }

    public static String getRef(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("refVal", MODE_PRIVATE);
        String val = sharedPreferences.getString("isFirst", null);
        return val;
    }

    public static void setRefCode(Context context, String value) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("refcodeVal", MODE_PRIVATE).edit();
        sharedPreferences.putString("isFirst", value);
        sharedPreferences.apply();


    }

    public static String getRefCode(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("refcodeVal", MODE_PRIVATE);
        String val = sharedPreferences.getString("isFirst", null);
        return val;
    }

    public static void setDeviceID1(Context context, String deviceId, String simNumber, String simNumber1) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Const.deviceID, MODE_PRIVATE).edit();
        sharedPreferences.putString(Const.deviceID, deviceId);
        sharedPreferences.putString(Const.simID, simNumber);
        sharedPreferences.putString(Const.simID1, simNumber1);

        sharedPreferences.apply();

    }

    public static String getDeviceID1(Context context) {

        String deviceID;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.deviceID, MODE_PRIVATE);
        deviceID = sharedPreferences.getString(Const.deviceID, null);

        return deviceID;


    }

    public static String getSimId1(Context context) {

        String simID;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.deviceID, MODE_PRIVATE);
        simID = sharedPreferences.getString(Const.simID1, null);
        return simID;
    }

    public static void setPaymentStatus(Context context, String value) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("paymentStatus", MODE_PRIVATE).edit();
        sharedPreferences.putString("isFirst", value);
        sharedPreferences.apply();


    }

    public static String getPaymentStatus(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("paymentStatus", MODE_PRIVATE);
        String val = sharedPreferences.getString("isFirst", null);
        return val;
    }

    public static void setDescrStatus(Context context, String value) {

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("DESC", MODE_PRIVATE).edit();
        sharedPreferences.putString("DESC", value);
        sharedPreferences.apply();


    }

    public static String getDescrStatus(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("DESC", MODE_PRIVATE);
        String val = sharedPreferences.getString("DESC", null);
        return val;
    }

    public static void setDocStatus(Context context, String value) {
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("docStatus", MODE_PRIVATE).edit();
        sharedPreferences.putString("isFirst", value);
        sharedPreferences.apply();
    }

    public static String getDocStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("docStatus", MODE_PRIVATE);
        String val = sharedPreferences.getString("isFirst", null);
        return val;
    }

    public static String getVersionInfo(Context context) {
        String versionName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Log.d("versionName", "getVersionInfo: " + versionName);
        return versionName;

    }


    // permission granted or not
    public static boolean getSharedPrefFromTag(String tagName, boolean defaultValue,Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("permission", MODE_PRIVATE);
            return sharedPreferences.getBoolean(tagName, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    public static void storeSharedPref(String tagName, boolean tagValue,Context context) {
        try {
            SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("permission", MODE_PRIVATE).edit();
            sharedPreferences.putBoolean(tagName, tagValue);
            sharedPreferences.apply();
        } catch (Exception e) {
           // VenturaException.Print(e);
        }
    }

    public static void setPLFOA(Context context,String link){
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("LINK",MODE_PRIVATE).edit();
        sharedPreferences.putString(Const.Psn_lnk_acct_opn,link);
        sharedPreferences.apply();
    }

    public static String getPLFOA(Context context){
        String clientCode = "";
        SharedPreferences sharedPreferences = context.getSharedPreferences("LINK",MODE_PRIVATE);
        clientCode =   sharedPreferences.getString(Const.Psn_lnk_acct_opn,"");

        return clientCode;

    }


    public static void setDocumentStatus(Context context, String status) {
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("DOC", MODE_PRIVATE).edit();
        sharedPreferences.putString("DOC", status);
        sharedPreferences.apply();

    }

    public static String getDocumentStatus(Context context) {
        String mobile;
        SharedPreferences sharedPreferences = context.getSharedPreferences("DOC", MODE_PRIVATE);
        mobile = sharedPreferences.getString("DOC", null);
        return mobile;
    }

    public static void setEsignStatus(Context context, String status) {
        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("esign", MODE_PRIVATE).edit();
        sharedPreferences.putString("esign", status);
        sharedPreferences.apply();

    }

    public static String getEsignStatus(Context context) {
        String mobile;
        SharedPreferences sharedPreferences = context.getSharedPreferences("esign", MODE_PRIVATE);
        mobile = sharedPreferences.getString("esign", null);
        return mobile;
    }

}
