package com.application.vpp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.Views.Views;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class MonthlyEarrning extends NavigationDrawer implements AdapterView.OnItemSelectedListener, RequestSent {
    Spinner spinnerMonth;
    Spinner spinnerYear;
    FancyButton btnEarning;
    String month, year, vppid;
    String dates = null;
    TextView txt_earn;
    public static Handler handlerEarning;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.monthly_earning_layout, mDrawerLayout);
        toolbar.setTitle("Earning");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        handlerEarning = new ViewHandler();
        spinnerMonth = (Spinner) findViewById(R.id.spMonth);
        spinnerYear = (Spinner) findViewById(R.id.spYear);
        txt_earn = (TextView) findViewById(R.id.txt_earn);

        btnEarning = findViewById(R.id.btnEarning);
        // Spinner click listener
        spinnerMonth.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> Month = new ArrayList<String>();
        Month.add("01");
        Month.add("02");
        Month.add("03");
        Month.add("04");
        Month.add("05");
        Month.add("06");
        Month.add("07");
        Month.add("08");
        Month.add("09");
        Month.add("10");
        Month.add("11");
        Month.add("12");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Month);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerMonth.setAdapter(dataAdapter);


        spinnerYear.setOnItemSelectedListener(this);

        List<String> Year = new ArrayList<String>();
        Year.add("2016");

        Year.add("2017");
        Year.add("2018");
        Year.add("2019");
        Year.add("2020");
        ArrayAdapter<String> dataAdapterYear = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Year);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerYear.setAdapter(dataAdapterYear);


        btnEarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchEarningMonthly();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
//         month=item;
//
//        System.out.println("month"+month);
//        dates=
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void fetchEarningMonthly() {
        try {
            month = spinnerMonth.getSelectedItem().toString();
            System.out.println("month bin" + month);
            year = spinnerYear.getSelectedItem().toString();
            System.out.println("year bin" + year);
            dates = year + "-" + month + "-" + "01";
            System.out.println("date bin" + dates);
            //vppid = "72237";
            String vppid = Logics.getVppId(MonthlyEarrning.this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("vppid", vppid);
            jsonObject.put("dates", dates);

            Log.e("zzzz", jsonObject.toString());
            byte[] data = jsonObject.toString().getBytes();
            new SendTOServer(MonthlyEarrning.this, MonthlyEarrning.this, Const.MSGMONTHLYEARNING, data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.d("Message", "handleMessageLeadList: " + msg.toString());

//            if(ringProgressDialog!=null){
//
//                ringProgressDialog.dismiss();
//            }

            String data = (String) msg.obj;
            int msgCode = msg.arg1;

            switch (msgCode) {

                case Const.MSGMONTHLYEARNING: {
                    Log.e("response", data);
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.isNull("")){

                            Toast.makeText(MonthlyEarrning.this, "null", Toast.LENGTH_SHORT).show();
                        }else {
                            String Payout = jsonObject.getString("payout");
                            txt_earn.setVisibility(View.VISIBLE);
                            txt_earn.setText("Payout: " + Payout);
                            Views.toast(MonthlyEarrning.this, "Paout" + Payout);
                        }

                    } catch (JSONException e) {
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }

                }
                break;

            }

        }
    }

    @Override
    public void requestSent(int value) {

    }
}
