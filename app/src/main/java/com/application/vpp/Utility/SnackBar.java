package com.application.vpp.Utility;

import android.graphics.Color;

import com.application.vpp.R;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;


public class SnackBar {
    public static void SnackBar(CoordinatorLayout coordinatorLayout_, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout_, message, Snackbar.LENGTH_LONG);
//                .setAction("RETRY", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                    }
//                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
}
