package com.application.vpp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {


    //     Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    String abcd;
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {



                for(int i=0;i<pdus.length;i++){
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    // b=sender.endsWith("WNRCRP");  //Just to fetch otp sent from WNRCRP
                    String messageBody = smsMessage.getMessageBody();
                    abcd=messageBody.replaceAll("[^0-9]","");

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message",abcd);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    // Show Alert

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }



//    private static SmsListener mListener;
//    Boolean b;
//    String abcd,xyz;
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Bundle data  = intent.getExtras();
//        Object[] pdus = (Object[]) data.get("pdus");
//        for(int i=0;i<pdus.length;i++){
//            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
//            String sender = smsMessage.getDisplayOriginatingAddress();
//            // b=sender.endsWith("WNRCRP");  //Just to fetch otp sent from WNRCRP
//            String messageBody = smsMessage.getMessageBody();
//            abcd=messageBody.replaceAll("[^0-9]","");   // here abcd contains otp which is in number format
//            //Pass on the text to our listener.
//
//                mListener.messageReceived(abcd);  // attach value to interface object
//
//        }
//
//        }
//
//    public static void bindListener(SmsListener listener) {
//        mListener = listener;
//    }
}
