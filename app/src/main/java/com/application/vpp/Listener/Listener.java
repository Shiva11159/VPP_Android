package com.application.vpp.Listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by bpandey on 18-07-2018.
 */

public class Listener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

//        if(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){
//
//            Bundle bundle = intent.getExtras();
//            SmsMessage[] smsMessages = null;
//            String msg_from = "";
//            String msg_body = "";
//            if(bundle!=null){
//
//                try {
//                    Object[] pdus = (Object[]) bundle.get("pdus");
//                    smsMessages = new SmsMessage[pdus.length];
//                    for (int i = 0; i < smsMessages.length; i++) {
//                        smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                        msg_body = smsMessages[i].getMessageBody();
//                        msg_from = smsMessages[i].getOriginatingAddress();
//                      //  LoginScreen.setOTPText(msg_body);
//                    }
//
//                }catch (Exception e){
//
//                }
//
//
//
//            }
//        }

    }
}
