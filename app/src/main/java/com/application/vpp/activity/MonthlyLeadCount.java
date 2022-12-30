package com.application.vpp.activity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.Views.Views;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

public class MonthlyLeadCount extends NavigationDrawer implements RequestSent {
    EditText date,todate;
    DatePickerDialog datePickerDialog;
    public static Handler handlerLeadCount;
    TextView txt_lead;

    FancyButton btnSubmit;
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.monthly_lead_layout, mDrawerLayout);

        date = (EditText) findViewById(R.id.date);
        // perform click event on edit text
        todate=(EditText) findViewById(R.id.todate);
        btnSubmit=findViewById(R.id.btnSub);
        handlerLeadCount= new ViewHandler();
        txt_lead= (TextView) findViewById(R.id.txt_lead);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(MonthlyLeadCount.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                System.out.println("test"+date.getText());

            }
        });







        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(MonthlyLeadCount.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                todate.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                System.out.println("test"+date.getText());

            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchLeadMonthly();

            }
        });
    }
    public void fetchLeadMonthly () {
        try {
          String vppid="ALRPV1053A";
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("vppid", vppid);
            jsonObject.put("fromdate",date.getText().toString().toUpperCase());
            jsonObject.put("todate",todate.getText().toString().toUpperCase());

            Log.e("jsonObject111", jsonObject.toString());

            byte[] data = jsonObject.toString().getBytes();
            new SendTOServer(MonthlyLeadCount.this, MonthlyLeadCount.this, Const.MONTHLYLEAD,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.d("Message", "handleMessageLeadList: "+msg.toString());

//            if(ringProgressDialog!=null){
//
//                ringProgressDialog.dismiss();
//            }

            String data = (String)msg.obj;
            int msgCode = msg.arg1;

            switch (msgCode){

                case Const.MONTHLYLEAD:{
                    Log.e("response",data);

                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String count=  jsonObject.getString("leadcount");
                        txt_lead.setVisibility(View.VISIBLE);
                        txt_lead.setText("Leads Count: "+count);
                        Views.toast(MonthlyLeadCount.this,"Lead :"+count);

                    } catch (JSONException e) {

                        FirebaseCrashlytics.getInstance().recordException(e);
                    }


                }break;

            }




        }
    }


    @Override
    public void requestSent(int value) {

    }
}

