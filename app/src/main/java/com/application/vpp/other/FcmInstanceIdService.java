package com.application.vpp.other;

import android.util.Log;

import com.application.vpp.Const.Const;
import com.application.vpp.ReusableLogics.Logics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class FcmInstanceIdService extends FirebaseInstanceIdService {
    public  static  final String REG_TOKEN="REG_TOKEN";
    public void onTokenRefresh() {
        String reg_token="";
        reg_token=Const.reg_token;
        String recent_token=FirebaseInstanceId.getInstance().getToken();
        Log.e(REG_TOKEN,recent_token);
        Logics.setTokenID(FcmInstanceIdService.this,recent_token);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }
}