package com.application.vpp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Database.DatabaseHelper;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Interfaces.APiValidateAccount;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.NetworkCall.APIClient;
import com.application.vpp.NetworkCall.UploadLogs;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class AddLead extends NavigationDrawer implements ConnectionProcess, View.OnClickListener, RequestSent, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    boolean clicktrue = false;
    public static Handler handlerAddProspect;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    FancyButton btnSubmit;
    Button btn1;
    EditText edtName, edtEmail, edtMobile, edtCity, edtdate, edtTime, edtComments;
    TextInputLayout txtInputDate, txtInputTime;
    //    ProgressDialog ringProgressDialog;
    String mysqlDateFormat = "";
    Spinner spnr_product, spnr_schedule;
    JSONObject jsonObject = new JSONObject();
    String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    DatabaseHelper dbh;
    ArrayList<String> arrayList;
    int selectedProductId = 0;
    //    int selectedProductId2 = 2;
    int selectedLeadStatus = 1;
    String remark1 = Const.Check1UnChecked;
    String remark2 = Const.Check2UnChecked;
    CheckBox chk1, chk2;
    int selectedDay, selectedMonth, selectedYear;
    int currDay, currMonth, currYear;
    int currHour = 9, currMin = 0;
    boolean isDataProcessed = true;
    ImageView imgContactFetchIcon;
    public static final int REQUEST_CODE = 1;
    TextView prodlist;
    ArrayAdapter<String> adapter;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mProducts = new ArrayList<>();
    ArrayList<Integer> selectedprodids = new ArrayList<>();
    String[] outputStrArr;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;
    SweetAlertDialog sweetAlertDialog;
    boolean NOCheckINTERNET = false;
    LinearLayout mainLayout;

    boolean submitbtnPressed=false;

    APiValidateAccount apiService1;
    int MaxTry=0;

    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_lead, mDrawerLayout);
//        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
//        Const.tcpClient = new TCPClient(connectionProcess);
        handlerAddProspect = new ViewHandler();
        dbh = new DatabaseHelper(this);
        arrayList = new ArrayList<>();
        arrayList = dbh.getProductMaster();
        Log.d("arraylist", "onCreate: " + arrayList.get(0));
        listItems = GetStringArray(arrayList);
        checkedItems = new boolean[arrayList.size()];
        prodlist = (TextView) findViewById(R.id.list_prod);
//        btn1 = findViewById(R.id.btn1);
        mainLayout =  findViewById(R.id.lytMain);
        apiService1 = new APIClient().getClientsocketConn(AddLead.this).create(APiValidateAccount.class);


//        new SendTOServer(AddLead.this,connectionProcess);
        imgContactFetchIcon = (ImageView) findViewById(R.id.imgContactFetchIcon);
        btn1 =  findViewById(R.id.btn1);

        btnSubmit = (FancyButton) findViewById(R.id.btn_submit);
        // edtdate = (EditText)findViewById(R.id.edtDate);
        //  edtTime = (EditText)findViewById(R.id.edtTime);

        edtName = (EditText) findViewById(R.id.edt_p_name);
        edtMobile = (EditText) findViewById(R.id.edt_p_number);
        edtEmail = (EditText) findViewById(R.id.edt_p_email);
        edtCity = (EditText) findViewById(R.id.edt_p_city);
        edtComments = (EditText) findViewById(R.id.edtComments);
        //  spnr_product = (Spinner)findViewById(R.id.spnr_product);
        //spnr_schedule = (Spinner)findViewById(R.id.spnr_schedule);


        edtName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtCity.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        chk1 = (CheckBox) findViewById(R.id.chk1);
        chk2 = (CheckBox) findViewById(R.id.chk2);

//        edtdate.setOnClickListener(this);
//        edtTime.setOnClickListener(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayList);
       /* btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(AddLead.this);
                mBuilder.setTitle("Select Products for Leads");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(! mProducts.contains(position)){
                                mProducts.add(position);
                            }
                        }
                        else  if(mProducts.contains(position)){
                            mProducts.remove(position);
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item="";

                        outputStrArr = new String[mProducts.size()];
                        for (int i=0;i<mProducts.size();i++){
                            item=item+listItems[mProducts.get(i)];
                            outputStrArr[i]=listItems[mProducts.get(i)];
                            Log.d("Selected products",listItems[mProducts.get(i)]);
                            int selectid=dbh.getProductId(listItems[mProducts.get(i)]);
                            selectedprodids.add(selectid);
                            if(i!=mProducts.size()-1){
                                item=item+",";
                            }
                        }
                        prodlist.setText(item);
                    }
                });
                mBuilder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.show();
            }
        });*/

       // inserSockettLogsArrayList=new ArrayList<>();

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList,"SocketLogs", AddLead.this);




//        if (inserSockettLogsArrayList != null) {
//            if (inserSockettLogsArrayList.size() > 0) {
//                Log.e("arrayListsize", String.valueOf(inserSockettLogsArrayList.size()));
//                InsertLogsMethod(inserSockettLogsArrayList);
//            }
//        }


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddLead.this);
                mBuilder.setTitle("Select Products for Leads");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        Log.d("ADDLead==", "" + position);
                        Log.d("mProducts==", "" + mProducts.size());
                        if (isChecked) {
                            if (!mProducts.contains(position)) {
                                mProducts.add(position);
                            }
                        } else if (mProducts.contains(position)) {
                            for (int i = 0; i < mProducts.size(); i++) {
                                if (position == mProducts.get(i)) {
                                    mProducts.remove(i);
                                }
                            }

                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";

                        outputStrArr = new String[mProducts.size()];

                        for (int i = 0; i < mProducts.size(); i++) {

                            item = item + listItems[mProducts.get(i)];

                            outputStrArr[i] = listItems[mProducts.get(i)];

                            Log.d("Selected products", listItems[mProducts.get(i)]);

                            int selectid = dbh.getProductId(listItems[mProducts.get(i)]);

                            selectedprodids.add(selectid);

                            if (i != mProducts.size() - 1) {
                                item = item + ",";
                            }
                        }
                        prodlist.setText(item);
                    }
                });
                mBuilder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.show();
            }
        });

        // spnr_product.setOnItemSelectedListener(this);
        // spnr_schedule.setOnItemSelectedListener(this);

        chk1.setOnCheckedChangeListener(this);
        chk2.setOnCheckedChangeListener(this);
        imgContactFetchIcon.setOnClickListener(this);

        //    spnr_product.setAdapter(adapter);


        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.vpp_schedule));
        //   spnr_schedule.setAdapter(adapter2);

        edtMobile.addTextChangedListener(this);
        btnSubmit.setOnClickListener(this);
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                validation();
//            }
//        });

//       spnr_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//           @Override
//           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//           }
//
//           @Override
//           public void onNothingSelected(AdapterView<?> adapterView) {
//
//           }
//       });


        edtName.addTextChangedListener(this);


    }

    public String datePicker() {

        String dateTime = "";
        Calendar calendar = Calendar.getInstance();

        currDay = calendar.get(Calendar.DAY_OF_MONTH);
        currMonth = calendar.get(Calendar.MONTH);
        currYear = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                selectedDay = dayOfMonth;
                selectedMonth = monthOfYear;
                selectedYear = year;
                String selecteddate = Logics.dateSet(dayOfMonth, monthOfYear, year);
                Log.d("selectedDate", "onDateSet: " + selecteddate);
                edtdate.setText(selecteddate);
                mysqlDateFormat = selecteddate + " ";
            }
        }, currYear, currMonth, currDay);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, 1);
        datePickerDialog.setMinDate(calendar1);
        datePickerDialog.show(this.getFragmentManager(), "DatePicker");

        // if(mysqlDateFormat.matches())

        return dateTime;
    }

    public String[] GetStringArray(ArrayList<String> arr) {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {
            // Assign each value to String array
            str[j] = arr.get(j);
        }
        return str;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.imgContactFetchIcon: {
                Intent intent = new Intent(this, PhoneDirectory.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
            break;
            case R.id.btn_submit: {
                submitbtnPressed=true;
                btnSubmit.setEnabled(false);
                validation();
            }
            break;
        }
    }

    private void validation() {

        clicktrue = true;
        String name = edtName.getText().toString().toUpperCase().trim();
        String mobile = edtMobile.getText().toString().toUpperCase().trim();
        String email = edtEmail.getText().toString().toUpperCase().trim();

        String city = edtCity.getText().toString().toUpperCase().trim();
        //    String date = edtdate.getText().toString().toUpperCase().trim();
        // String time = edtTime.getText().toString().toUpperCase().trim();
        String time = "";
        String comments = edtComments.getText().toString().toUpperCase().trim();
        if (mobile.length() > 10) {
            mobile = mobile.replaceAll("\\s", "");
            edtMobile.setText(mobile);
            // mobile = mobile.substring(mobile.length() - 10);
        }
        if (name.length() < 4) {
            btnSubmit.setEnabled(true);

            TastyToast.makeText(
                    getApplicationContext(),
                    "Name should be greater than 4 characters",
                    TastyToast.LENGTH_LONG,
                    TastyToast.ERROR
            );

        }else if (mobile.contains("-") || mobile.contains("*") || mobile.contains("#") || mobile.contains("+")) {
            Views.toast(this, "Enter mobile number without '+91 & special characters -,@,#'");
            btnSubmit.setEnabled(true);

        } else if (mobile.length() != 10) {
            Views.toast(this, "Enter 10 digit mobile number");
            btnSubmit.setEnabled(true);

//            TastyToast.makeText(AddLead.this,"",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
        }  else if (city.length() < 3) {
            TastyToast.makeText(
                    getApplicationContext(),
                    "City Name should be more than 3 characters.",
                    TastyToast.LENGTH_LONG,
                    TastyToast.ERROR  );
                    btnSubmit.setEnabled(true);



        } else {
            String imei = Logics.getDeviceID(AddLead.this);
            sendData(imei, name, mobile, email, city, time, comments);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            String number = data.getStringExtra("number");
            number = number.replaceAll("\\s", "");
            number = number.replaceAll("\\D", "");
            number = number.replaceFirst("91", "");

            edtMobile.setText(number);
        }
    }

    @Override
    public void requestSent(int value) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Log.d("i", "onItemSelected: " + i);
        int id = adapterView.getId();
        switch (id) {

//            case R.id.spnr_product:{
//                Log.d("case1", "onItemSelected: "+i);
//
//                if(i>0) {
//
//                    String selectedItem = (String) spnr_product.getSelectedItem();
//                    Log.d("selectedItem", "onItemSelected: " + selectedItem);
//                    selectedProductId = dbh.getProductId(selectedItem);
//                }else {
//
//                    selectedProductId = 0;
//                }
//
//            }break;

//            case R.id.spnr_schedule:{
//
//                Log.d("case2", "onItemSelected: ");
//
//
//                selectedLeadStatus = i+1;
//                if(selectedLeadStatus>1){
//
//                    edtdate.setVisibility(View.VISIBLE);
//                    edtTime.setVisibility(View.VISIBLE);
//                }else {
//
//                    edtdate.setVisibility(View.GONE);
//                    edtTime.setVisibility(View.GONE);
//                }
//            }
//
//
        }

        Log.d("item", "onItemSelected: " + selectedProductId + selectedLeadStatus);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        switch (id) {

            case R.id.chk1: {
                if (b) {
                    remark1 = Const.Check1Checked;
                } else {

                    remark1 = Const.Check1UnChecked;
                }
            }
            break;
            case R.id.chk2: {

                if (b) {
                    remark2 = Const.Check2Checked;
                } else {

                    remark2 = Const.Check2UnChecked;
                }
            }
            break;

        }

        Log.d("remark1", "onItemSelected: " + remark1);
        Log.d("remark2", "onItemSelected: " + remark2);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//       int mobileLength =  edtMobile.length();
//        if(mobileLength >1 && mobileLength!=10 ){
//
//            edtMobile.setError("Enter Valid Number");
//        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AlertDialogClass.PopupWindowDismiss();


//            if (ringProgressDialog != null) {
//                ringProgressDialog.dismiss();
//                ringProgressDialog.cancel();
//            }
            String message = (String) msg.obj;
            Log.e("message", message);
            switch (msg.arg1) {
                case Const.MSGPRODUCTMASTER: {
                    try {
                        String data = (String) msg.obj;
                        JSONArray jsonArray = null;
                        jsonArray = new JSONArray(data);
                        Log.d("jsonArray", "handleMessage: " + jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialogClass.ShowMsg(AddLead.this,e.getMessage());

                    }

                }
                break;

                case Const.MSGADDLEAD: {
                    try {
//                        if (ringProgressDialog != null) {
//                            ringProgressDialog.dismiss();
//                            ringProgressDialog.cancel();
//                        }
//

                        btnSubmit.setEnabled(true);
                        AlertDialogClass.PopupWindowDismiss();

                        JSONObject jsonObject1 = new JSONObject(message);
                        JSONArray jsonArray = jsonObject1.getJSONArray("array");
                        StringBuffer display = new StringBuffer("Reference Added Succesfully For: ");
                        StringBuffer display1 = new StringBuffer("Reference Already Exist For: ");
                        // TastyToast.makeText(getApplicationContext(), display.length()+"------------" + display1.length(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String product = "";
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int status = jsonObject.getInt("status");
                            String mesg = jsonObject.getString("message");
                            if (status != 0) {
                                int pid = jsonObject.getInt("product");
                                if (pid == 2) {
                                    product = "Equity";
                                } else if (pid == 6) {
                                    product = "Mutual Fund";
                                } else if (pid == 4) {
                                    product = "Commodity";
                                } else if (pid == 39) {
                                    product = "VPP";
                                } else {
                                    product = "PMS";
                                }
                                if (status == 1) {
                                    display.append(product + ",");
                                } else if (status == 2) {
                                    display1.append(product + ",");
                                }
                                String s;
                                if (i == jsonArray.length() - 1) {
                                    display.setLength(display.length() - 1);
                                    display1.setLength(display1.length() - 1);

                                    if (display.length() > 33 && display1.length() > 29) {
                                        s = display + "\n" + display1;
                                        showPopup(s);
                                        // TastyToast.makeText(getApplicationContext(), display+"  \n" + display1, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                    } else if (display.length() > 33) {
                                        String s1 = display + "";
                                        showPopup(s1);

                                        //  TastyToast.makeText(getApplicationContext(), display + "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                                    } else {
                                        s = display1 + "";
                                        showPopup(s);
                                        // TastyToast.makeText(getApplicationContext(), display1 + "", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                                    }
                                }
                            } else {
/*
                                TastyToast.makeText(
                                        getApplicationContext(),
                                        mesg,
                                        TastyToast.LENGTH_LONG,
                                        TastyToast.SUCCESS


                                );

                                 showPopup();
*/
                                //   Views.toast(AddLead.this,mesg);
//                                Intent intent = new Intent(AddLead.this, AddLead.class).putExtra("from","");;
//                                startActivity(intent);

                                showPopup(mesg);
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialogClass.ShowMsg(AddLead.this,e.getMessage());
                        btnSubmit.setEnabled(true);

                    }


                }
                break;


//                case Const.MSGSOCKETCONNECTEDADDLEAD:{
//
//                    validation();
//
//                }break;
            }


            Log.d("Message", "AddLead: ");
        }
    }

    void showPopup(String s) {
        sweetAlertDialog = new SweetAlertDialog(AddLead.this, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText("")
                .setContentText(s + "                          ")
//                .setContentTextSize(20)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent(AddLead.this, AddLead.class).putExtra("from","");
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void sendData(String imei, String name, String mobile, String email, String city, String time, String comments) {

        JSONArray jsonArray = new JSONArray();
        if (selectedprodids.size() != 0) {
//                ringProgressDialog = ProgressDialog.show(AddLead.this, "Please wait ...", "
//
//                Your Data ...", true);
//                ringProgressDialog.setCancelable(true);
//                ringProgressDialog.getWindow().setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.color.white));
//                ringProgressDialog.show();
            btnSubmit.setEnabled(false);

            AlertDialogClass.PopupWindowShow(AddLead.this,mainLayout);
//                ringProgressDialog = Views.showDialog(AddLead.this);
            try {
                for (int j = 0; j < selectedprodids.size(); j++) {
                    JSONObject jsonObject = new JSONObject();
                    selectedProductId = selectedprodids.get(j);
//
                    String vppid = Logics.getVppId(AddLead.this);
                    jsonObject.put("lead_name", name);
                    jsonObject.put("mobile", mobile);
                    jsonObject.put("email", email);
                    jsonObject.put("city", city);
                    jsonObject.put("product_id", selectedProductId);
                    jsonObject.put("appoint_datetime", mysqlDateFormat + time);
                    jsonObject.put("remark1", remark1 + "," + remark2 + "," + comments);
                    jsonObject.put("remark2", "");
                    jsonObject.put("VPPID", vppid);
                    jsonObject.put("vpp_name", Logics.getVppName(AddLead.this));
                    jsonObject.put("lead_status", selectedLeadStatus);
                    jsonArray.put(jsonObject);

                }

                JSONObject json = new JSONObject();
                json.put("data", jsonArray);
                byte[] data = json.toString().getBytes();

                if (Connectivity.getNetworkState(getApplicationContext())) {
                    new SendTOServer(this, this, Const.MSGADDLEAD, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    AlertDialogClass.PopupWindowDismiss();
                    Views.SweetAlert_NoDataAvailble(AddLead.this, "Connect internet !");
                    btnSubmit.setEnabled(true);

                }
            }catch (Exception e){
                AlertDialogClass.PopupWindowDismiss();
                Views.toast(this, e.getMessage());
                FirebaseCrashlytics.getInstance().recordException(e);
                AlertDialogClass.ShowMsg(AddLead.this,e.getMessage());
                btnSubmit.setEnabled(true);

            }
        } else {
            AlertDialogClass.PopupWindowDismiss();
            Views.toast(this, "Please Select Product");
            btnSubmit.setEnabled(true);

        }


    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        // Log.d("AddLead", "onDestroy: ");
//    }


    @Override
    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//            ringProgressDialog.cancel();
//        }
        btnSubmit.setEnabled(true);

        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(AddLead.this),
                            "1",
                            Methods.getVersionInfo(AddLead.this),
                            Methods.getlogsDateTime(), "AddLead",
                            Connectivity.getNetworkState(getApplicationContext()),
                            AddLead.this);
                    ;
                } catch (Exception e) {
                    Toast.makeText(AddLead.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }

        AlertDialogClass.PopupWindowDismiss();


        //        AlertDailog.ProgressDlgDiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    if (clicktrue)
                        validation();
//                    else
//                        TastyToast.makeText(AddLead.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                Views.SweetAlert_NoDataAvailble(AddLead.this, "No Internet");
//                        TastyToast.makeText(AddLead.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);

            }
        });

        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList,Logics.getVppId(AddLead.this),
                            "1",
                            Methods.getVersionInfo(AddLead.this),
                            Methods.getlogsDateTime(),"AddLead",
                            Connectivity.getNetworkState(getApplicationContext()),
                            AddLead.this);
                    ;
                }catch (Exception e){
                    Toast.makeText(AddLead.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("arrayListsize", String.valueOf(inserSockettLogsArrayList.size()));
                JSONArray jsonArray = new JSONArray();

                try {
                    for (int i = 0; i < inserSockettLogsArrayList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("vpp_id", inserSockettLogsArrayList.get(i).getVpp_id());
                        jsonObject.put("flag", inserSockettLogsArrayList.get(i).getFlag());
                        jsonObject.put("app_version", inserSockettLogsArrayList.get(i).getApp_version());
                        jsonObject.put("log_date", inserSockettLogsArrayList.get(i).getLog_date());
                        jsonObject.put("screen_name", inserSockettLogsArrayList.get(i).getScreenname());
                        jsonObject.put("internet_connection", inserSockettLogsArrayList.get(i).getIsinternetAvailable());
                        Log.e("jsonObject00", jsonObject.toString());
                        jsonArray.put(jsonObject);
                    }

//                    Log.e("jsonObject11", jsonObject.toString());
                    Log.e("jsonArray11", jsonArray.toString());

                    UploadLogs.InsertLogsMethod(jsonArray,apiService1,inserSockettLogsArrayList,AddLead.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exexex", e.getMessage());
                }


            }
        }


       // TastyToast.makeText(AddLead.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

    }

    @Override
    public void serverNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//            ringProgressDialog.cancel();
//        }
        btnSubmit.setEnabled(true);

        AlertDialogClass.PopupWindowDismiss();

        Log.e("called: ", "serverNotAvailable");

        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(AddLead.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(AddLead.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//            ringProgressDialog.cancel();
//        }
        btnSubmit.setEnabled(true);

        AlertDialogClass.PopupWindowDismiss();


        Views.SweetAlert_NoDataAvailble(AddLead.this, "Internet Not Available");
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
        btnSubmit.setEnabled(true);

//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//            ringProgressDialog.cancel(
//            );
//        }
        AlertDialogClass.PopupWindowDismiss();

//        connected = false;
        Log.e("called: ", "ConnectToserver");

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(AddLead.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(AddLead.this, connectionProcess, "Server Not Available");
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
    protected void onResume() {

        btnSubmit.setEnabled(true);

        connectionProcess = (ConnectionProcess) this;
        Log.e("called: ", "onResume");

//        dbh.insertLogs(Logics.getVppId(AddLead.this),this.getClass().getSimpleName(), String.valueOf(Calendar.getInstance().getTime()));


       // SharedPref.insert(SharedPref.LogsALL,this.getClass().getSimpleName(),Logics.getVppId(AddLead.this),String.valueOf(Calendar.getInstance().getTime()),AddLead.this);


        if (getIntent().getStringExtra("from").equalsIgnoreCase("0")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Connectivity.getNetworkState(getApplicationContext()))
                        new ConnectTOServer(AddLead.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

/*
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Const.dismiss == true) {
                                if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Views.ProgressDlgConnectSocket(Profile.this, connectionProcess, "Server Not Available");
//                                        ConnectToserver(connectionProcess);
                                        }
                                    });
                                }
                            }
                        }
                    }, 1000);
*/
                }
            });

        }else {
            if (Connectivity.getNetworkState(getApplicationContext())) {
                if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("run: ", "resumse");
                            ProgressDlgConnectSocket(AddLead.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                        }
                    });
                }


            }

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
                                    new ConnectTOServer(AddLead.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                }
                            }
                        }
                    });
                }
            }, delay);
        }


        super.onResume();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddLead.this, Dashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public void SocketDisconnected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//            ringProgressDialog.cancel();
//        }
        btnSubmit.setEnabled(true);

        AlertDialogClass.PopupWindowDismiss();


        Log.e("called", "SocketDisconnected");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(AddLead.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(AddLead.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    public void ProgressDlgConnectSocket(Context context, final ConnectionProcess connectionProcess, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        btnSubmit.setEnabled(true);

        Log.e( "DlgConnectSocket", "called");
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(AddLead.this),
                                    "0",
                                    Methods.getVersionInfo(AddLead.this),
                                    Methods.getlogsDateTime(), "AddLead",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    AddLead.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!AddLead.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                Toast.makeText(AddLead.this, "", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess==null){
                Log.e( "DlgConnectSocket11111_null", "called");

            }else {
                new ConnectTOServer(AddLead.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                connectionProcess.ConnectToserver(connectionProcess);
            }
            Log.e( "DlgConnectSocket11111", "called");

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


//    public void ProgressDlgSomethingIssue(Context context, String msg) {
//        // 2. Confirmation message
//        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
//
//        sweetAlertDialog.setTitleText(msg)
////                .setContentText("Something went wrong try again later..")
//                .setConfirmText("Close!")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                        sDialog.dismiss();
//                        sDialog.cancel();
//                        finish();
//                        finishAffinity();
//                        ProgressDlgSomethingIssue = true;
//
//                    }
//                });
//        if (!AddLead.this.isFinishing()) {
//            sweetAlertDialog.show();
//        }
//        sweetAlertDialog.setCancelable(false);
////                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
////                    @Override
////                    public void onClick(SweetAlertDialog sDialog) {
////                        sDialog.dismissWithAnimation();
////                    }
////                })
////                .show();
//    }

    void InsertLogsMethod(ArrayList<InserSockettLogs> arrayList) {
//        JSONArray js = null;
//        try {
//            js = dbh.createJsonArray();
//            Log.e("LOGS_", js.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JSONObject paramObject = new JSONObject();
//        try {
//            paramObject.put("ispojo", js.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                jsonObject.put("vpp_id", arrayList.get(i).getVpp_id());
                jsonObject.put("flag", arrayList.get(i).getFlag());
                jsonObject.put("app_version", arrayList.get(i).getApp_version());
                jsonObject.put("log_date", arrayList.get(i).getLog_date());
                jsonObject.put("screen_name", arrayList.get(i).getScreenname());
                jsonObject.put("internet_connection", arrayList.get(i).getIsinternetAvailable());
            }
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("logsRequest", jsonArray.toString());

        // SharedPref.clearLogsArrayList(SplashScreen.this);

        //Call<JsonObject> validateSignature = apiService1.SendSocketLogs(jsonArray.toString());
//        validateSignature.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//                String err = "";
//
//                if (response.isSuccessful()) {
//                    SharedPref.clearLogsArrayList(SplashScreen.this);
////                    Log.e("DASHBOARD_success", "" + response.body());
////                    Log.e("DASHBOARD_success", "" + response.toString());
//                    Toast.makeText(SplashScreen.this, response.body().toString(), Toast.LENGTH_SHORT).show();
//
//
//                } else {
//                    switch (response.code()) {
//                        case 404:
////                            Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
//                            err = "Server Not Found";
//                            break;
//                        case 500:
////                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
//                            err = "Server Unavailable";
//                            break;
//                        case 503:
////                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
//                            err = "Server Overloaded try after sometime";
//                            break;
//                        default:
//                            err = String.valueOf(response.code());
//                            err = "Something went wrong try again.";
////                            Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//
//                }
//                Toast.makeText(SplashScreen.this, err, Toast.LENGTH_SHORT).show();
//
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                // Log.e("DASHBOARD_failure", "   throwable===" + t.getMessage());
//            }
//        });


    }

//    private void saveData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//
//        //adding
//
//        mExampleList.add(new InserSockettLogs(Logics.getVppId(AddLead.this),
//                "1",
//                Methods.getVersionInfo(AddLead.this),
//                Methods.getlogsDateTime(),
//                "AddLead",
//                Connectivity.getNetworkState(getApplicationContext())));
//
//        String json = gson.toJson(mExampleList);
//        editor.putString("task list", json);
//        editor.apply();
//    }
//
//    private void loadData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("task list", null);
//        Type type = new TypeToken<ArrayList<InserSockettLogs>>() {}.getType();
//        mExampleList = gson.fromJson(json, type);
//
//        if (mExampleList == null) {
//            mExampleList = new ArrayList<>();
//        }
//
//        Log.e("kkkkkkkkk ", String.valueOf(mExampleList.size()));
//    }


}
