package com.application.vpp.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.Interfaces.GstProceed;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.Utility.AlertDialogClass;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.vpp.R;

import org.w3c.dom.Text;

public class ShareLinkDlg extends DialogFragment {

    static GstProceed gstProceedInterface;
    static ShareLinkDlg shareLinkDlg;
    static Activity ctx;

    EditText et_link;
    TextView Close_Popup_Share;
    Button btn_share;

    public static ShareLinkDlg newInstance(GstProceed gstProceed, Activity context) {
        shareLinkDlg = new ShareLinkDlg();
        gstProceedInterface = gstProceed;
        ctx = context;
        return shareLinkDlg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.actvtyflg, container, false);
        getDialog().setCanceledOnTouchOutside(true);
//        gstProceedInterface=(GstProceed)this;
        et_link = (EditText) rootView.findViewById(R.id.et_link_);
        Close_Popup_Share = (TextView) rootView.findViewById(R.id.Close_Popup_Share);
        btn_share = (Button) rootView.findViewById(R.id.btn_share);
        String link = Logics.getPLFOA(ctx);
        et_link.setText(link);

        Close_Popup_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLinkDlg.dismiss();
                dismiss();

            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.getNetworkState(getActivity())) {
                    String link = Logics.getPLFOA(getActivity());
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra(Intent.EXTRA_TEXT, "" + Logics.getPLFOA(getActivity()));
                    intent.setType("text/plain");
                    Intent chooserIntent = Intent.createChooser(intent, "share your referral link");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
                    startActivity(chooserIntent);
                    shareLinkDlg.dismiss();
////
                }
            }
        });

        return rootView;
    }

}
