package com.application.vpp.other;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.application.vpp.Interfaces.GstProceed;
import com.application.vpp.R;
import com.application.vpp.Utility.AlertDialogClass;

public class GstDlg extends DialogFragment {

    EditText et_gstNo;
    EditText et_gstName;
    EditText et_gstAddress;
    TextView Close_Popup;
    Button btn_proceed;
    RelativeLayout relative1;
    static GstProceed gstProceedInterface;
    static GstDlg gstDlg;
    static Activity ctx;

    public static GstDlg newInstance(GstProceed gstProceed, Activity context) {
        gstDlg=new GstDlg();
        gstProceedInterface=gstProceed;
        ctx=context;
        return gstDlg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.layout_gst, container, false);
        getDialog().setCanceledOnTouchOutside(false);
//        gstProceedInterface=(GstProceed)this;
        et_gstNo = (EditText) rootView.findViewById(R.id.et_gstNo);
        et_gstName = (EditText) rootView.findViewById(R.id.et_gstName);
        et_gstAddress = (EditText) rootView.findViewById(R.id.et_gstAddress);
        btn_proceed = rootView.findViewById(R.id.btn_proceed);
        relative1 = rootView.findViewById(R.id.relative1);
        Close_Popup = (TextView) rootView.findViewById(R.id.Close_Popup_Share);
        Close_Popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();

                if (et_gstNo.getText().toString().equalsIgnoreCase("")) {
                    et_gstNo.setError("Please enter GST No.");
                } else if (et_gstNo.getText().toString().length()!=15) {
                    et_gstNo.setError("GST No should be of 15 characters.");

                } else if (et_gstName.getText().toString().equalsIgnoreCase("")) {
                    et_gstName.setError("Please enter company name");

                }else if (et_gstAddress.getText().toString().equalsIgnoreCase("")) {
                    et_gstAddress.setError("please enter company address");

                }else {
                    AlertDialogClass.PopupWindowShow(ctx,relative1);
//                    dismiss();
                    gstProceedInterface.GstProceedclick(gstDlg,et_gstNo.getText().toString(),et_gstName.getText().toString(),et_gstAddress.getText().toString());
                }
            }
        });
        return rootView;
    }

}
