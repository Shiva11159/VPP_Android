package com.application.vpp.other;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.application.vpp.activity.AddLead;
import com.application.vpp.activity.Notifications;
import com.application.vpp.activity.Profile;
import com.application.vpp.Database.DatabaseHelper;
import com.application.vpp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageService";
    DatabaseHelper dbh;
    String  title, message, img_url = "0";
    Bitmap bitmap;

    //below link to send image notification even when app is in background or closed. so it is done using api hit from postmen.
    //https://www.youtube.com/watch?v=heJmdX_djHs

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent=null;
        dbh = new DatabaseHelper(this);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

//                String data = remoteMessage.getNotification().getBody();
//                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("message");
            img_url = remoteMessage.getData().get("img_url");

            //  action = remoteMessage.getNotification().getClickAction();

            //   bitmap = getBitmapfromUrl(img_url);
            //  .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))


            if (title != null && message != null) {
                dbh.insertNotificaton(title, message, img_url);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("VppNotifications",   "VppNotifications", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }

            ///only this thing changed....

          /*  Intent intent = new Intent(this, Notifications.class);

            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/

            //added new by shiva ..

            if (remoteMessage.getData().get("tag").equalsIgnoreCase("Notifications")){
                intent = new Intent(this, Notifications.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
            }else if (remoteMessage.getData().get("tag").equalsIgnoreCase("Profile")){
                intent = new Intent(this, Profile.class);
                intent.putExtra("from","0");

            }else if (remoteMessage.getData().get("tag").equalsIgnoreCase("AddLead")){
                intent = new Intent(this, AddLead.class);
                intent.putExtra("from","0");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"VppNotifications")
                    .setSmallIcon(R.drawable.vpp_logo)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setContentText(message)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            if(!img_url.equalsIgnoreCase("0")) {

                ImageRequest imageRequest = new ImageRequest(img_url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
                        notificationBuilder.setLargeIcon(response);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, notificationBuilder.build());

                    }
                }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                MySingleton.getInstance(this).addToRequestQueue(imageRequest);
            }else {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
            }

        }

    }
}


