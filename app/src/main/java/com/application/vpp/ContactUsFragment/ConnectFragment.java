package com.application.vpp.ContactUsFragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.sdsmdg.tastytoast.TastyToast;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;


public class ConnectFragment extends Fragment implements RequestSent, ConnectionProcess {

    RelativeLayout relativeLayout_callBack, relativeLayout_post_ur_query;
    CardView cardView_callbackform, card_view_post_ur_query1;
    LinearLayout linearLayout_callback_form, linearLayout_post_ur_form;
    FancyButton btn_callback_submit, submit_query;
    EditText edt_name, edt_phone_number, edt_callback_date, edt_callback_time, query;
    ConnectionProcess connectionProcess;
    ProgressDialog ringProgressDialog;
    public static Handler handlerConnect;
    RequestSent requestSent;
    int selectedDay, selectedMonth, selectedYear;
    int currDay, currMonth, currYear;
    String mysqlDateFormat = "";

    boolean clickedvalidation = false;
    boolean clickedquery = false;

    View view;
    int visible_hide = 1;
    int visible_hide2 = 1;
    ScrollView mainlayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.connect_layout, container, false);
        mainlayout=(ScrollView) view.findViewById(R.id.mainLayout);

        connectionProcess = (ConnectionProcess) this;
        handlerConnect = new ViewHandler();
        relativeLayout_callBack = view.findViewById(R.id.card_view_callBack);
        linearLayout_callback_form = view.findViewById(R.id.card_view_callback_form);
        linearLayout_callback_form.setVisibility(View.GONE);
        linearLayout_post_ur_form = view.findViewById(R.id.card_view_post_query_form);
        linearLayout_post_ur_form.setVisibility(View.GONE);
        cardView_callbackform = view.findViewById(R.id.cardView_callbackform);
        cardView_callbackform.setVisibility(View.GONE);
        card_view_post_ur_query1 = view.findViewById(R.id.card_view_post_ur_query1);
        card_view_post_ur_query1.setVisibility(View.GONE);

        relativeLayout_post_ur_query = view.findViewById(R.id.relative_post_ur_query);
        btn_callback_submit = view.findViewById(R.id.btn_callback_submit);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone_number = view.findViewById(R.id.edt_phone_number);
        // edt_callback_date = view.findViewById(R.id.edt_callback_date);
        //   edt_callback_time = view.findViewById(R.id.edt_callback_time);
        submit_query = view.findViewById(R.id.btn_submit_query);
        query = view.findViewById(R.id.q_edt);
        relativeLayout_callBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (visible_hide == 0) {
                    linearLayout_callback_form.setVisibility(View.GONE);
                    cardView_callbackform.setVisibility(View.GONE);
                    visible_hide = 1;
                } else {
                    linearLayout_callback_form.setVisibility(View.VISIBLE);
                    edt_name.setText(Logics.getVppName(getContext()));
                    cardView_callbackform.setVisibility(View.VISIBLE);
                    visible_hide = 0;
                }

            }
        });
        relativeLayout_post_ur_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible_hide2 == 0) {
                    linearLayout_post_ur_form.setVisibility(View.GONE);
                    card_view_post_ur_query1.setVisibility(View.GONE);
                    visible_hide2 = 1;
                } else {
                    linearLayout_post_ur_form.setVisibility(View.VISIBLE);
                    card_view_post_ur_query1.setVisibility(View.VISIBLE);
                    visible_hide2 = 0;
                }
            }
        });

        btn_callback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Connectivity.getNetworkState(getContext())) {
                    validation();
//                    if (Const.isSocketConnected) {
//                    }else {
//                        Views.toast(getContext(),Const.checkConnection);
//                    }
                } else {
                    Views.SweetAlert_NoDataAvailble(getActivity(), "No Internet ! ");
                }


            }
        });

        submit_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                if (Connectivity.getNetworkState(getContext())) {
                    validationQuery();
                }else {
                    Views.SweetAlert_NoDataAvailble(getActivity(), "No Internet ! ");
                }
            }
        });



        return view;
    }

   /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }*/
   void validationQuery(){
       if(query.getText().toString().toUpperCase().trim()==null || query.getText().toString().toUpperCase().trim().equalsIgnoreCase("")){
           query.setError("Please Enter Query");
       }
       else{
           sendQuery();
       }

   }

    void validation() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        if (edt_name.getText().toString().toUpperCase().trim() == null || edt_name.getText().toString().toUpperCase().trim().equalsIgnoreCase("")) {
            edt_name.setError("please enter name");
        } else if (edt_phone_number.getText().toString().toUpperCase().trim() == null || edt_phone_number.getText().toString().toUpperCase().trim().equalsIgnoreCase("")) {
            edt_phone_number.setError("please enter name");
        }
//        else
//        if(edt_callback_date.getText().toString().toUpperCase().trim()==null || edt_callback_date.getText().toString().toUpperCase().trim().equalsIgnoreCase("")){
//            edt_callback_date.setError("please enter name");
//        }else
//        if(edt_callback_time.getText().toString().toUpperCase().trim()==null || edt_callback_time.getText().toString().toUpperCase().trim().equalsIgnoreCase("")){
//            edt_callback_time.setError("please enter name");
//        }
        else {
            sendData();
        }
    }

    void sendData() {
        try {
            String vppid = Logics.getVppId(getContext());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", edt_name.getText().toString().toUpperCase().trim());
            jsonObject.put("contact_number", edt_phone_number.getText().toString().toUpperCase().trim());
            jsonObject.put("callback_date", "Nodata");
            jsonObject.put("callback_time", "No data");
            jsonObject.put("vppid", vppid);
            byte data[] = jsonObject.toString().getBytes();
            new SendTOServer(getActivity(), requestSent, Const.MSGCALLBACK, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
            ringProgressDialog.dismiss();
        }
    }

    public void sendQuery() {
        String VppId;
        JSONObject jsonObject = new JSONObject();
        VppId = Logics.getVppId(getContext());

        String Query = query.getText().toString().toUpperCase();

        try {

            jsonObject.put("Subject", "Query Raised By " + VppId);
            jsonObject.put("VppId", VppId);
            jsonObject.put("Query", Query);

            jsonObject.put("intvalue", 2);


            byte[] data = jsonObject.toString().getBytes();
            new SendTOServer(getActivity(), requestSent, Const.MSGQUERY, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestSent(int value) {

    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            Log.d("Message", "handleMessageLeadList: " + msg.toString());


            String data = (String) msg.obj;
            int msgCode = msg.arg1;
            switch (msgCode) {
                case Const.MSGQUERY: {
//                    if (progressDialog != null) {
//                        progressDialog.dismiss();
//                    }
                    clickedquery = true;
                    Log.d("Mail", "mail status :  " + msg.toString());


                    try {
                        JSONObject jsonObject = new JSONObject(data);

                        int status = jsonObject.getInt("status");
                        String message = jsonObject.getString("message");

                        if (status != 0) {
                            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            query.setText("");
                            card_view_post_ur_query1.setVisibility(View.GONE);


//                            time.setText("");


                        } else {
                            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.INFO);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        AlertDialogClass.ShowMsg(getActivity(),e.getMessage());

                    }
                }
                break;

                case Const.MSGCALLBACK: {
                    try {
                        clickedvalidation = true;
                        JSONObject jsonObject = new JSONObject(data);
                        int status = jsonObject.getInt("status");
                        String message = jsonObject.getString("mesage");
                        if (status == 1) {
                            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                           // Views.ShowMsg(getActivity(), message, "");
                            //linearLayout_callback_form.setVisibility(View.GONE);
                            cardView_callbackform.setVisibility(View.GONE);
                        }else {
                            AlertDialogClass.ShowMsg(getActivity(),message);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertDialogClass.ShowMsg(getActivity(),e.getMessage());

                    }
                }
            }
        }
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
                edt_callback_date.setText(selecteddate);

                mysqlDateFormat = selecteddate + " ";

            }
        }, currYear, currMonth, currDay);


//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.add(Calendar.DATE,1);
//        datePickerDialog.setMinDate(calendar1);
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePicker");

        // if(mysqlDateFormat.matches())

        return dateTime;
    }


    private void TimePickerDialog() {

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

                String time = Logics.timeSet(minute, hourOfDay);
                edt_callback_time.setText(time);

            }
        }, 0, 0, true);

        timePickerDialog.setMinTime(9, 0, 0);
        timePickerDialog.setMaxTime(18, 0, 0);
        timePickerDialog.show(getActivity().getFragmentManager(), "TimePicker");
    }

    @Override
    public void connected() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        Log.e("connected11", "connected11");
        //        AlertDailog.ProgressDlgDiss();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getActivity()))
                    if (clickedvalidation == false) {
                        validation();
                    } else {
                        sendQuery();
                    }
                else
                    Views.SweetAlert_NoDataAvailble(getActivity(), "No Internet");
               // TastyToast.makeText(getActivity(), "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }


//    @Override
//    public void connected() {
//        if (ringProgressDialog != null) {
//            ringProgressDialog.dismiss();
//        }
//        //        AlertDailog.ProgressDlgDiss();
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                sendData();
//                TastyToast.makeText(getActivity(), "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
//            }
//        });
//    }

    @Override
    public void serverNotAvailable() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(getActivity(), connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }

        Views.SweetAlert_NoDataAvailble(getActivity(), "Internet Not Available");
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
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getActivity()))
                    new ConnectTOServer(getActivity(), connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss==true){
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Views.ProgressDlgConnectSocket(getActivity(), connectionProcess, "Server Not Available");
                                    }
                                });
                            }
                        }
                    }
                },3000);
            }
        });
    }

    @Override
    public void SocketDisconnected() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(getActivity(), connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}


