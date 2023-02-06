package com.application.vpp.Views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.application.vpp.Interfaces.ConfirmPayment;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import android.animation.ObjectAnimator;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by bpandey on 05-06-2018.
 */

public class Views {
    private static SweetAlertDialog pDialog;

    public String dateTimepicker(Context context) {
        String dateTime = "";
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

            }
        }, year, month, day);


        return dateTime;
    }

    public static void toast(Context context, String text) {

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        Log.d("" + context.getPackageResourcePath(), "toast: ");

    }

    public static ProgressDialog showDialog(Context context) {
        ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "Loading Your Data ...", true);
        ringProgressDialog.setCancelable(true);
        ringProgressDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.color.white));

        return ringProgressDialog;
    }

    public static void ProgressDlgConnectSocket(final Activity context, final ConnectionProcess connectionProcess, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

        sweetAlertDialog.setTitleText(msg)
//                .setContentText("Socket Not Available")
                .setConfirmText("Reconnect!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                        connectionProcess.ConnectToserver(connectionProcess);
                    }
                })
                .show();
        sweetAlertDialog.setCancelable(false);
//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();
    }

    public static void SweetAlert_NoDataAvailble(Context context, String msg) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
//        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        sweetAlertDialog.setTitleText("No internet connection")
                .setContentText("Please make sure that you are connected to the internet")
                .show();
        sweetAlertDialog.setCancelable(false);
    }

    public static void ShowMsg(Context context, String title, String msg) {
        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(msg)
                .show();
    }


    public static void ProgressDlgPaymentDone(final Activity context, ConfirmPayment confirmPayment, String amount) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog =
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Thank You !");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setContentText("We acknowledge the payment of Rs. " + amount + "/-");
        sweetAlertDialog.setConfirmText("Continue");
        sweetAlertDialog.setCustomImage(R.drawable.vpp_logo);
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                        confirmPayment.Confirm();

                    }
                })
//                .show();
//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
                .show();
    }


    public static void ProgressDlgPaymentFail(final Activity context, ConfirmPayment confirmPayment, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog =
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText("Error!!");
        sweetAlertDialog.setContentText(msg);
        sweetAlertDialog.setConfirmText("OK");
        sweetAlertDialog.setCustomImage(R.drawable.vpp_logo);
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
//                        confirmPayment.Confirm();

                    }
                })
                .show();

        /*new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();*/
    }


    public static SweetAlertDialog ProgressDlg(Context context, String msg) {
        if (pDialog == null) {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        }
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(true);
//        pDialog.show();
        return pDialog;
    }

    public static void ProgressDlgDiss() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public static ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}
