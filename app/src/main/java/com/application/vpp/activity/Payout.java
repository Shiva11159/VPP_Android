package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.PayoutList;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.ClientlistData;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Datasets.PayoutListData;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.Calendar;

public class Payout extends NavigationDrawer implements RequestSent, ConnectionProcess, AdapterView.OnItemSelectedListener {

    boolean start=false;
    boolean end=false;

    CheckBox checkBox4, checkBox1, checkBox2, checkBox3;
    String MON, YEAR;
    Spinner spin_mon, spin_year;
    PayoutList payoutListAdapter;
    RecyclerView listPayout;
    ArrayList<PayoutListData> payoutListDataArrayList;
//    ArrayList<PayoutListData> filteredList;

    public static Handler handlerLeadList;
    static Gson gson;
    //    ProgressDialog ringProgressDialog;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    TextView tv_nodataavail;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;
    boolean NOCheckINTERNET = false;
    RelativeLayout mainlayout;
    String data = "";
    StringBuffer ssb = null;
    int MaxTry = 0;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    ArrayList<String> arrayListProduct;

    TextView txtProduct;
    TextView txtDate;

    AlertDialog alertDialog;

    //Spinner spinner;

    LinearLayout spinnerdate;

    private int currentYear;
    private int yearSelected;
    private int monthSelected;


    TextView txtFilter;
    ArrayList<String> spinMon, spinYear;

    ArrayAdapter<String> arrayAdapter1, arrayAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_payout, mDrawerLayout);
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;

        spinMon = new ArrayList<>();

        spinYear = new ArrayList<>();

        handlerLeadList = new Payout.ViewHandler();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        txtFilter = findViewById(R.id.txtFilter);
        tv_nodataavail = findViewById(R.id.tv_nodataavail);
        txtProduct = findViewById(R.id.txtProduct);
        txtDate = findViewById(R.id.txtDate);
        mainlayout = findViewById(R.id.mainlayout);
        ssb = new StringBuffer();

        arrayListProduct = new ArrayList<>();
        payoutListDataArrayList = new ArrayList<>();
        // filteredList = new ArrayList<>();


        SharedPref.savePreferences1(Payout.this, "M", "0");
        SharedPref.savePreferences1(Payout.this, "Y", "0");


        // Spinner element

        // Spinner click listener

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("MF");
        categories.add("COMM");
        categories.add("EQUITY");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        //spinner.setAdapter(dataAdapter);


        //

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        yearSelected = currentYear;
        monthSelected = calendar.get(Calendar.MONTH);

        //

        SharedPref.savePreferences1(Payout.this, "C1", "0");
        SharedPref.savePreferences1(Payout.this, "C2", "0");
        SharedPref.savePreferences1(Payout.this, "C3", "0");
        SharedPref.savePreferences1(Payout.this, "C4", "0");

        //

        txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertBoxProduct();
            }
        });

//        txtDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                displayMonthYearPickerDialogFragment(
//                        true,
//                        true
//                );
//
//            }
//        });

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList, "SocketLogs", Payout.this);

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                new ConnectTOServer(Payout.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }
//        });
//        sendData();`


        if (Connectivity.getNetworkState(getApplicationContext())) {
            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgConnectSocket(Payout.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                    }
                });
            } else {
                sendData();
            }
        }
    }


    @Override
    public void requestSent(int value) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if (payoutListDataArrayList != null) {
            // filter(item);
        }


        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class ViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listPayout = (RecyclerView) findViewById(R.id.listPayout);

            Log.d("Message", "handleMessageBrokerageList: " + msg.toString());

//            if (ringProgressDialog != null) {
//
//                ringProgressDialog.dismiss();
//            }


            data = "";
            data = (String) msg.obj;
            data = String.valueOf(ssb.append(data));

            if (data.equalsIgnoreCase("[]")) {
                tv_nodataavail.setVisibility(View.VISIBLE);
                listPayout.setVisibility(View.GONE);
                ssb.setLength(0);

                AlertDialogClass.PopupWindowDismiss();

            } else {
                tv_nodataavail.setVisibility(View.GONE);
                listPayout.setVisibility(View.VISIBLE);

                payoutListDataArrayList.clear();
                //filteredList.clear();

                if (data.endsWith("]")) {
                    payoutListDataArrayList = gson.fromJson(data, new TypeToken<ArrayList<PayoutListData>>() {
                    }.getType());

//                    filteredList = gson.fromJson(data, new TypeToken<ArrayList<PayoutListData>>() {
//                    }.getType());

                    Log.e("sizzeeee", String.valueOf(payoutListDataArrayList.size()));
                    payoutListAdapter = new PayoutList(Payout.this, payoutListDataArrayList);

                    Log.e("sss", String.valueOf(payoutListDataArrayList.size()));
                    if (payoutListDataArrayList != null) {
                        for (int i = 0; i < payoutListDataArrayList.size(); i++) {
                            String[] arr = payoutListDataArrayList.get(i).month.split("\\s");

                            //

                            if (!spinMon.contains(payoutListDataArrayList.get(i).month)) {
                                spinMon.add(payoutListDataArrayList.get(i).month);
                            }

                            if (!spinYear.contains(payoutListDataArrayList.get(i).month)) {
                                spinYear.add(payoutListDataArrayList.get(i).month);
                            }

                            Collections.sort(spinMon, new Comparator<String>() {

                                @Override
                                public int compare(String arg0, String arg1) {
                                    SimpleDateFormat format = new SimpleDateFormat(
                                            "MMM yyyy");
                                    int compareResult = 0;
                                    try {
                                        Date arg0Date = format.parse(arg0);
                                        Date arg1Date = format.parse(arg1);
                                        compareResult = arg0Date.compareTo(arg1Date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        compareResult = arg0.compareTo(arg1);
                                    }
                                    return compareResult;
                                }
                            });

                            Collections.sort(spinYear, new Comparator<String>() {

                                @Override
                                public int compare(String arg0, String arg1) {
                                    SimpleDateFormat format = new SimpleDateFormat(
                                            "MMM yyyy");
                                    int compareResult = 0;
                                    try {
                                        Date arg0Date = format.parse(arg0);
                                        Date arg1Date = format.parse(arg1);
                                        compareResult = arg0Date.compareTo(arg1Date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        compareResult = arg0.compareTo(arg1);
                                    }
                                    return compareResult;
                                }
                            });

                            //



                        }

                        listPayout.setLayoutManager(new LinearLayoutManager(Payout.this));
                        listPayout.setAdapter(payoutListAdapter);
                        listPayout.setItemAnimator(new DefaultItemAnimator());
                        ssb.setLength(0);
                        //spinner.setOnItemSelectedListener(Payout.this);
                    }

                    arrayAdapter1 = new ArrayAdapter<String>(Payout.this, android.R.layout.simple_spinner_item, spinMon);
                    arrayAdapter2 = new ArrayAdapter<String>(Payout.this, android.R.layout.simple_spinner_item, spinYear);
                    arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                }
            }
        }
//            ssb.setLength(0);
    }

    public void sendData() {
//        ringProgressDialog = ProgressDialog.show(Payout.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(true);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//        ringProgressDialog = new ProgressDialog(this);
//        ringProgressDialog.setMessage("Please wait ..\n Loading Your Data ...");
//        ringProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        ringProgressDialog.setIndeterminate(true);
//        if (!Payout.this.isFinishing()){
//            ringProgressDialog.show();
//        }

        AlertDialogClass.PopupWindowShow(Payout.this, mainlayout);


        try {
            JSONObject jsonObject = new JSONObject();
            String vppid = Logics.getVppId(Payout.this);

            jsonObject.put("VPPID", "73352");     //73352 ...
//            jsonObject.put("VPPID", vppid);     //73352 ..
//            jsonObject.put("imei",imei);
//            jsonObject.put("simNo",simID);
            byte data[] = jsonObject.toString().getBytes();
            new SendTOServer(Payout.this, Payout.this, Const.MSGFETCHPAYOUT, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }


    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(Payout.this),
                            "1",
                            Methods.getVersionInfo(Payout.this),
                            Methods.getlogsDateTime(), "Payout",
                            Connectivity.getNetworkState(getApplicationContext()),
                            Payout.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(Payout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        AlertDialogClass.PopupWindowDismiss();
        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendData();
                // TastyToast.makeText(Payout.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();

        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(Payout.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(Payout.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();

        Views.SweetAlert_NoDataAvailble(Payout.this, "Internet Not Available");
        //        if (pDialog.isShowing()) {
        //            pDialog.dismiss();
        //            pDialog.cancel();
        //        }
        Log.e("IntenrnetNotAvailable: ", "internet");

        //        AlertDailog.ProgressDlgDiss();
        //        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ConnectToserver(final ConnectionProcess connectionProcess) {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(Payout.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(Payout.this, connectionProcess, "Server Not Available");
//                                        ConnectToserver(connectionProcess);
                                    }
                                });
                            }
                        }
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void SocketDisconnected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }

        AlertDialogClass.PopupWindowDismiss();
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(Payout.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(Payout.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        connectionProcess = (ConnectionProcess) this;


        ///added this lines extra by shiva ....
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!Connectivity.getNetworkState(getApplicationContext())) {
                            NOCheckINTERNET = true;   /// no net ....
                            Log.e("run: ", "no net");
                            imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol_red));
//                            lineinternet.setBackgroundColor(getResources().getColor(R.color.red));
//                            txtinternet.setText("Online");
                            showSnackbar("Internet Not Available");

                        } else {
//                            NOCheckINTERNET = false;   /// no net ....
                            imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol));
//                            lineinternet.setBackgroundColor(getResources().getColor(R.color.btn_active));
//                            showSnackbar("Online");

//                            imgConnection.setBackground(getResources().getDrawable(R.drawable.ic_up_and_down_arrows_symbol));
                            Log.e("run: ", " net available");

                        }

                        if (NOCheckINTERNET == true) {
                            if (Connectivity.getNetworkState(getApplicationContext())) {
                                NOCheckINTERNET = false;
//                                if (Const.dismiss == true)
                                new ConnectTOServer(Payout.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            }
                        }
                    }
                });
            }
        }, delay);
        super.onResume();

    }

    @SuppressLint("LongLogTag")
    public void ProgressDlgConnectSocket(Context context, final ConnectionProcess connectionProcess, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

        Log.e("DlgConnectSocket", "called");
        MaxTry++;
        if (MaxTry > 3) {
            sweetAlertDialog.setTitleText(msg)
//                .setContentText("Socket Not Available")
                    .setConfirmText("Try Again later!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            sDialog.dismiss();
                            sDialog.cancel();

                            //call here
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(Payout.this),
                                    "0",
                                    Methods.getVersionInfo(Payout.this),
                                    Methods.getlogsDateTime(), "Payout",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    Payout.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!Payout.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                Toast.makeText(Payout.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess == null) {
                Log.e("DlgConnectSocket11111_null", "called");

            } else {
                new ConnectTOServer(Payout.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                connectionProcess.ConnectToserver(connectionProcess);
            }
            Log.e("DlgConnectSocket11111", "called");

        }

        Log.e("DlgConnectSocketMaxTry", String.valueOf(MaxTry));


//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }


    private void alertBoxProduct() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.
                payoutfilter, null);
        builder.setView(dialogView);

        checkBox4 = dialogView.findViewById(R.id.SelectDate);
        checkBox1 = dialogView.findViewById(R.id.MF);
        checkBox2 = dialogView.findViewById(R.id.Comm);
        checkBox3 = dialogView.findViewById(R.id.Equity);
        Button btn_search = dialogView.findViewById(R.id.btn_search);

        spin_year = dialogView.findViewById(R.id.spin_year);
        spin_mon = dialogView.findViewById(R.id.spin_mon);
        spinnerdate = dialogView.findViewById(R.id.linearLayoutspinner);

        spin_mon.setAdapter(arrayAdapter1);
        spin_year.setAdapter(arrayAdapter2);

        if (SharedPref.getPreferences1(Payout.this, "C1").equalsIgnoreCase("1")) {
            checkBox1.setChecked(true);
            arrayListProduct.add("MF");
        }
        if (SharedPref.getPreferences1(Payout.this, "C2").equalsIgnoreCase("1")) {
            checkBox2.setChecked(true);
            arrayListProduct.add("COMM");
        }

        if (SharedPref.getPreferences1(Payout.this, "C3").equalsIgnoreCase("1")) {
            checkBox3.setChecked(true);
            arrayListProduct.add("Equity");
        }

        if (SharedPref.getPreferences1(Payout.this, "C4").equalsIgnoreCase("1")) {
            checkBox4.setChecked(true);
        }

        spin_mon.setSelection(Integer.parseInt(SharedPref.getPreferences1(Payout.this, "M")));
        spin_year.setSelection(Integer.parseInt(SharedPref.getPreferences1(Payout.this, "Y")));

//        spin_mon.setSelection(1);
        spin_mon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MON = parent.getItemAtPosition(position).toString();
//                Toast.makeText(parent.getContext(), "Selected: " + MON, Toast.LENGTH_LONG).show();

                SharedPref.savePreferences1(Payout.this, "M", String.valueOf(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        spin_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YEAR = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + YEAR, Toast.LENGTH_LONG).show();
                SharedPref.savePreferences1(Payout.this, "Y", String.valueOf(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if (checkBox1.isChecked()){

                if (checkBox1.isChecked()) {
                    SharedPref.savePreferences1(Payout.this, "C1", "1");
                } else {
                    SharedPref.savePreferences1(Payout.this, "C1", "0");

                }

                if (!arrayListProduct.contains("MF")) {
                    arrayListProduct.add("MF");
                } else if (arrayListProduct.contains("MF")) {
                    for (int i = 0; i < arrayListProduct.size(); i++) {
                        if ("MF" == arrayListProduct.get(i)) {
                            arrayListProduct.remove("MF");
                        }
                    }
                }


                Log.e("hii", arrayListProduct.toString());


                // }
            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (checkBox1.isChecked()){

                if (checkBox2.isChecked()) {
                    SharedPref.savePreferences1(Payout.this, "C2", "1");
                } else {
                    SharedPref.savePreferences1(Payout.this, "C2", "0");
                }
                if (!arrayListProduct.contains("COMM".toUpperCase())) {
                    arrayListProduct.add("COMM".toUpperCase());
                } else if (arrayListProduct.contains("COMM".toUpperCase())) {
                    for (int i = 0; i < arrayListProduct.size(); i++) {
                        if ("COMM" == arrayListProduct.get(i).toUpperCase()) {
                            arrayListProduct.remove("COMM".toUpperCase());
                        }
                    }
                    //  }

                    Log.e("hii", arrayListProduct.toString());

                }
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (checkBox1.isChecked()){

                if (checkBox3.isChecked()) {
                    SharedPref.savePreferences1(Payout.this, "C3", "1");
                } else {
                    SharedPref.savePreferences1(Payout.this, "C3", "0");

                }
                if (!arrayListProduct.contains("Equity")) {
                    arrayListProduct.add("Equity");
                } else if (arrayListProduct.contains("Equity")) {
                    for (int i = 0; i < arrayListProduct.size(); i++) {
                        if ("Equity" == arrayListProduct.get(i)) {
                            arrayListProduct.remove("Equity");
                        }
                    }
                }
                Log.e("hii", arrayListProduct.toString());


                //   }
            }
        });


        if (checkBox4.isChecked()){
            spinnerdate.setVisibility(View.VISIBLE);
        }else {
            spinnerdate.setVisibility(View.GONE);
        }

        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox4.isChecked()) {
                    spinnerdate.setVisibility(View.VISIBLE);
                    SharedPref.savePreferences1(Payout.this, "C4", "1");
                } else {
                    spin_mon.setSelection(0);
                    spin_year.setSelection(0);
                    spinnerdate.setVisibility(View.GONE);
                    SharedPref.savePreferences1(Payout.this, "C4", "0");
                }
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox4.isChecked()){

                    try{

                        SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
                        String str1 = MON;
                        Date date1 = formatter.parse(str1);

                        String str2 = YEAR;
                        Date date2 = formatter.parse(str2);

                        if (date1.compareTo(date2)<0) {
                            System.out.println("date2 is Greater than my date1");
                        }else if (date1.compareTo(date2)>0){
                            TastyToast.makeText(getApplicationContext(), "Please select above dates.", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }else {
                            filter(arrayListProduct);
                            arrayListProduct.clear();

                            alertDialog.cancel();
                            alertDialog.dismiss();

                        }

                    }catch (ParseException e1){
                        e1.printStackTrace();
                    }

                }else {

                    filter(arrayListProduct);
                    arrayListProduct.clear();
                    alertDialog.cancel();
                    alertDialog.dismiss();

                }






                //filter(MON+" "+YEAR);


            }
        });

        ImageView txtclose = dialogView.findViewById(R.id.forgot_closeimage);


        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                alertDialog.cancel();

            }
        });

        builder.setCancelable(true);
        alertDialog = builder.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void filter(ArrayList<String> arrayList1) {

        ArrayList<PayoutListData> filteredList = new ArrayList<>();
        ArrayList<PayoutListData> filteredList2 = new ArrayList<>();
        ArrayList<PayoutListData> filteredList3 = new ArrayList<>();

        boolean product = false;
        boolean date = false;
        boolean dateproduct = false;


        if (checkBox1.isChecked() == true || checkBox2.isChecked() == true || checkBox3.isChecked() == true) {
            for (PayoutListData item : payoutListDataArrayList) {
                for (int i = 0; i < arrayList1.size(); i++) {
                    if (item.product.toUpperCase().contains(arrayList1.get(i).toUpperCase())) {
                        filteredList.add(item);
                        Log.e("sizee111", String.valueOf(filteredList.size()));
                    }
                }
            }

            product = true;

        }

        if (checkBox4.isChecked() == true) {
            for (PayoutListData item1 : payoutListDataArrayList) {
                if (item1.month.toUpperCase().contains(MON.toUpperCase())) {
                    filteredList2.add(item1);
                    Log.e("sizee222", String.valueOf(filteredList2.size()));
                }
            }

            product = false;
            date = true;

        }

        Log.e("product_date", product + "" + date);

        if (checkBox1.isChecked() == true || checkBox2.isChecked() == true || checkBox3.isChecked() == true && checkBox4.isChecked() == true) {
            for (PayoutListData item3 : filteredList) {
                if (item3.month.toUpperCase().equalsIgnoreCase(MON.toUpperCase() + " " + YEAR.toUpperCase())) {
                    filteredList3.add(item3);
                    Log.e("sizee333", String.valueOf(filteredList3.size()));
                }
            }

            date = false;
            dateproduct = true;
        }


        if (product == true) {
            payoutListAdapter.filterList(filteredList);

        } else if (date == true) {
            payoutListAdapter.filterList(filteredList2);

        } else if (dateproduct == true) {
            payoutListAdapter.filterList(filteredList3);

        } else {
            payoutListAdapter.filterList(payoutListDataArrayList);

        }

//    private void filter(String txt) {
//
//
//        if (SelectDate.isChecked()){
//            for (PayoutListData item : filteredList) {
//                if (item.month.toUpperCase().contains(txt.toUpperCase() ){
//
//                    filteredList.add(androidVersion);
//                }
//            }
//
//        }
//
//
//
//        payoutListAdapter.filterList(filteredList);
//
    }

    private void displayMonthYearPickerDialogFragment(boolean withRanges,
                                                      boolean customTitle) {
        MonthYearPickerDialogFragment dialogFragment = withRanges ?
                createDialogWithRanges(customTitle) :
                createDialog(customTitle);

        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int monthOfYear) {
                monthSelected = monthOfYear;
                yearSelected = year;


                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, 1);

                //

                SimpleDateFormat format = new SimpleDateFormat("MMM yyyy");
                String strDate = format.format(calendar.getTime());
                //filter(strDate);
                Log.e("onDateSet: ", strDate);

            }
        });

        dialogFragment.show(getSupportFragmentManager(), null);
    }

    private MonthYearPickerDialogFragment createDialogWithRanges(boolean customTitle) {
        final int minYear = 2017;
        final int maxYear = currentYear;
        final int maxMoth = 11;
        final int minMoth = 0;
        final int minDay = 1;
        final int maxDay = 31;
        long minDate;
        long maxDate;

        Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(minYear, minMoth, minDay);
        minDate = calendar.getTimeInMillis();

        calendar.clear();
        calendar.set(maxYear, maxMoth, maxDay);
        maxDate = calendar.getTimeInMillis();

        return MonthYearPickerDialogFragment
                .getInstance(monthSelected,
                        yearSelected,
                        minDate,
                        maxDate,
                        customTitle ? "Select".toUpperCase() : null,
                        true ? MonthFormat.SHORT : MonthFormat.LONG);
    }

    private MonthYearPickerDialogFragment createDialog(boolean customTitle) {
        return MonthYearPickerDialogFragment
                .getInstance(monthSelected,
                        yearSelected,
                        customTitle ? "Select".toUpperCase() : null,
                        true ? MonthFormat.SHORT : MonthFormat.LONG);
    }



//    public static List<Date> getDaysBetweenDates(Date startDate, Date endDate){
//        ArrayList<Date> dates = new ArrayList<Date>();
//        Calendar cal1 = Calendar.getInstance();
//        cal1.setTime(startDate);
//
//        Calendar cal2 = Calendar.getInstance();
//        cal2.setTime(endDate);
//
//        while(cal1.before(cal2) || cal1.equals(cal2))
//        {
//            dates.add(cal1.getTime());
//            cal1.add(Calendar.DATE, 1);
//        }
//        return dates;
//    }
}

