package com.application.vpp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.NotificationAdapter;
import com.application.vpp.Database.DatabaseHelper;
import com.application.vpp.Datasets.Notification_data;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.SharedPref.SharedPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;

public class Notifications extends NavigationDrawer {
    Gson gson;
    final Handler handler = new Handler();
    RecyclerView notificationDetails;
    ArrayList<Notification_data> notifiactionDataList= new ArrayList<>();
    NotificationAdapter notificationAdapter;
    public DatabaseHelper dbh = null;
    /*JSONObject json1 = new JSONObject();
    JSONObject json2 = new JSONObject();
    JSONArray jsonArray = new JSONArray();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().getStringExtra("AnotherActivity");
        getLayoutInflater().inflate(R.layout.notification_display, frame);
        //  getLayoutInflater().inflate(R.layout.notification_display,null);
        //toolbar.setTitle("NOTIFICATION");
        dbh = new DatabaseHelper(this);
        //  toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

//            json1.put("title", "Hello");
//            json1.put("message", "how are you");
//            json2.put("title", "Hello");
//            json2.put("message", "how are you");
//
//            jsonArray.put(json1);
//            jsonArray.put(json2);
        displaydata();


    }

    private void displaydata() {
        notifiactionDataList = dbh.getNotification();
//        notifiactionDataList=gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<Notification_data>>() {
//        }.getType());
        notificationDetails = (RecyclerView)findViewById(R.id.total_message_recycler);
        notificationAdapter = new NotificationAdapter(getApplicationContext(),notifiactionDataList);
        notificationDetails.setAdapter(notificationAdapter);
        notificationDetails.setItemAnimator(new DefaultItemAnimator());
        notificationDetails.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
b
    }
    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(Notifications.this, Dashboard.class);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Methods.InsertLogs(Notifications.this);
       // SharedPref.insert(SharedPref.LogsALL,this.getClass().getSimpleName(), Logics.getVppId(Notifications.this),String.valueOf(Calendar.getInstance().getTime()),Notifications.this);

    }
}
