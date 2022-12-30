package com.application.vpp.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.application.vpp.R;

public class AlertDialogClass {

    public static PopupWindow popupWindow;

    public static View customView;

    public static void PopupWindowShow(Activity context, View linearLayout1) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = layoutInflater.inflate(R.layout.layout_progressbar, null);
        //instantiate popup window
        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //display the popup window
        new Handler().postDelayed(new Runnable() {

            public void run() {
                if (!context.isFinishing()){
                    popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);
                }
            }

        }, 200L);

        if (!popupWindow.isShowing()) {
            customView.setVisibility(View.GONE);
            customView.setVisibility(View.VISIBLE);
        } else {
            customView.setVisibility(View.GONE);
        }

    }

    public static void PopupWindowDismiss() {
        if (popupWindow != null) {
//            if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            customView.setVisibility(View.GONE);
//            }
        }

    }

    public static void ShowMsg(Context context, String msg) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle("VPP");
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setIcon(R.drawable.vpp_logo);
//        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        alertDialogBuilder.setPositiveButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                arg0.dismiss();
                arg0.cancel();
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
