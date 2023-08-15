package com.application.vpp.activity;

import static com.application.vpp.Const.Const.from;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
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
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class SignupScreen2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, RequestSent, ConnectionProcess {
    EditText edtHouseNo;
    EditText edtArea;
    AlertDialog alertDialog;

    //    EditText edtCity;

    EditText edtPin;
    static EditText edtDob;
    EditText edtProf;
    EditText edtlandmark;
    AutoCompleteTextView edtState;
    String city = "";
    String houseNum, area, pin, Dob, landmark;
    String state_str = "";
    TextView imgerror;

    //    TextInputLayout text_input_profession;
    ScrollView mainlayout;
    String mysqlDateFormat = "";
    String profession;
    int selectedDay, selectedMonth, selectedYear;
    int currDay, currMonth, currYear;
    public static Handler signupHandler;
    Button submit;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;
    byte[] data;
    byte[] data1;
    private Spinner spinnerStateName;
    private Spinner spinnercityName;
    public static DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
    static Calendar userAge, minAdultAge;
    static Context context;
    List<String> StateName;
    List<String> CityName;
    List<String> StateId;
    List<String> CityId;
    String str_stateID = "";
    private Spinner StateSpinner;

    String opt = "";

    StringBuffer stringBuffer;
    Spinner spinnerprofession;

    int MaxTry = 0;
    ArrayList<InserSockettLogs> inserSockettLogsArrayList;

    String email, mobile, vppPanName, pan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen3);
        context = getApplicationContext();
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;
        edtHouseNo = findViewById(R.id.edt_houseno);
        edtArea = findViewById(R.id.edtArea);
        edtlandmark = findViewById(R.id.edtlandmark);
        spinnerStateName = (Spinner) findViewById(R.id.spinnerstate);
        spinnercityName = (Spinner) findViewById(R.id.spinnercity);

        //SS();

        spinnerprofession = findViewById(R.id.spinnerprofession);
//        edtCity = findViewById(R.id.edtCity);
        edtPin = findViewById(R.id.edtPincode);
        edtState = findViewById(R.id.edtState);
        edtDob = findViewById(R.id.edtDob);
        edtProf = findViewById(R.id.edtCurrProf);
//        text_input_profession = findViewById(R.id.text_input_profession);
        submit = findViewById(R.id.btn_signup_submit2);
        //imgerror = (TextView) findViewById(R.id.imgerror);
        mainlayout = (ScrollView) findViewById(R.id.mainlayout);


        signupHandler = new ViewHandler();
        edtDob.setOnClickListener(this);
        submit.setOnClickListener(this);

        stringBuffer = new StringBuffer();

        inserSockettLogsArrayList = SharedPref.getLogsArrayList(inserSockettLogsArrayList, "SocketLogs", SignupScreen2.this);


        edtDob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //  Toast.makeText(getApplicationContext(),""+b,Toast.LENGTH_SHORT).show();
                if (b == true) {
                    DialogFragment dFragment = new DatePickerFragment();

                    // Show the date picker dialog fragment
                    dFragment.show(getFragmentManager(), "Date Picker");

                }
            }
        });


        //create adapter
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.profession_array1,
                        android.R.layout.simple_spinner_dropdown_item);

        //how the spinner will look when it drop downs on click
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //setting adapter to spinner
        spinnerprofession.setAdapter(adapter);


        spinnerprofession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                profession = adapterView.getItemAtPosition(position).toString();

                mainlayout.scrollTo(0, mainlayout.getBottom());

                if (profession.equalsIgnoreCase("Others")) {
                    edtProf.setVisibility(View.VISIBLE);
                } else {
                    edtProf.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///////////-----------------------

        StateSpinner = (Spinner) findViewById(R.id.spinnerstate);
        StateName = new ArrayList<>();
        CityName = new ArrayList<>();
        StateId = new ArrayList<>();
        CityId = new ArrayList<>();

        opt = Const.StateMaster;
        sendDataStateCity(opt, "", "");
    }

    private void validation() {

        boolean check = false;
//        houseNum = edtHouseNo.getText().toString().toUpperCase().trim();
//        area = edtArea.getText().toString().toUpperCase().trim();
//        pin = edtPin.getText().toString().toUpperCase().trim();
//        landmark = edtlandmark.getText().toString().toUpperCase().trim();
//        String dob = edtDob.getText().toString().toUpperCase().trim();

        if (edtHouseNo.getText().toString().length() < 1 || edtHouseNo.getText().toString().equalsIgnoreCase("")) {
            edtHouseNo.setError("Enter Valid House No./Wing/ Name of the Bldg.");
            edtHouseNo.startAnimation(shakeError());
            check = true;
        } else if (edtArea.getText().toString().length() < 3) {
            edtArea.setError("Enter Valid Area");
            edtHouseNo.startAnimation(shakeError());
            check = true;

        } else if (state_str.equalsIgnoreCase("") || state_str.equalsIgnoreCase("Select State")) {
            spinnerStateName.startAnimation(shakeError());
            check = true;

        } else if (city.equalsIgnoreCase("")) {
            spinnercityName.startAnimation(shakeError());
            check = true;

        } else if (edtPin.getText().toString().length() != 6) {
            edtPin.setError("Enter Valid Pin");
            check = true;

            edtPin.startAnimation(shakeError());
        } else if (edtDob.getText().toString() == null || edtDob.getText().toString().equalsIgnoreCase("")) {
            edtDob.setError("Enter Valid DOB");
            edtDob.startAnimation(shakeError());
            check = true;

        } else if (profession.equalsIgnoreCase("Others")) {
            if (edtProf.getText().toString().equalsIgnoreCase("")) {
                edtProf.setError("Enter Profession");
                edtProf.startAnimation(shakeError());
                check = true;

            }
        }

        if (check == false) {
            sendData();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    private void sendData() {
        AlertDialogClass.PopupWindowShow(SignupScreen2.this, mainlayout);
        String imei = Logics.getDeviceID(this);
        String ip = Logics.getSimId(this);
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            imei = Logics.getTokenID(SignupScreen2.this);
            ip = "12345";
        }
        Dob = edtDob.getText().toString().toUpperCase().trim();
        profession = edtProf.getText().toString().toUpperCase().trim();

        // pan validation.
//        String pan = Logics.getPan(SignupScreen2.this);
        pan = SharedPref.getPreferences1(SignupScreen2.this, "panNo");
//        String vppPanName = Logics.getPanName(SignupScreen2.this);
        vppPanName = SharedPref.getPreferences1(SignupScreen2.this, "panName");


        String namearr[] = Logics.getfml(SignupScreen2.this);
        String name = namearr[0] + " " + namearr[1] + " " + namearr[2];

        String ref = Logics.getRef(SignupScreen2.this);
        String refCode = Logics.getRefCode(SignupScreen2.this);

        // signup 1
//        String mobile = Logics.getMobile_1(SignupScreen2.this);
        mobile = SharedPref.getPreferences(SignupScreen2.this, "mobileNo");

//        int isMobileV = Logics.getisMobile_V(SignupScreen2.this);
        String isMobileV = SharedPref.getPreferences(SignupScreen2.this, "isMobile");


        // signup 2
//        String email = Logics.getEmail_1(SignupScreen2.this);
        email = SharedPref.getPreferences(SignupScreen2.this, "email");

//        int isEmailV = Logics.getisEmail_V(SignupScreen2.this);
        String isEmailV = SharedPref.getPreferences(SignupScreen2.this, "isEmail");


        // bank details.
        int isBankV = Logics.getisBankVerified(SignupScreen2.this); // not changed..
        String accNo = Logics.getBankAccNo(SignupScreen2.this); // not changed..
        String ifsc = Logics.getBankIfsc(SignupScreen2.this); // not changed..
        String vppBankName = Logics.getVppBankName(SignupScreen2.this); // not changed..

        //

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("imei", imei);
            //  jsonObject.put("ip","1234566666");
            jsonObject.put("ip", ip);
            jsonObject.put("name", vppPanName);
            jsonObject.put("ref", ref);
            jsonObject.put("refCode", refCode);
            jsonObject.put("mobile", mobile);
            jsonObject.put("email", email);
            jsonObject.put("isMobileV", isMobileV);
            jsonObject.put("isEmailV", isEmailV);
            jsonObject.put("pan", pan);
            jsonObject.put("vpppanname", vppPanName);
            jsonObject.put("isBankV", isBankV);
            jsonObject.put("bankaccno", accNo);
            jsonObject.put("Ifsc", ifsc);
            jsonObject.put("vppbankname", vppBankName);
            jsonObject.put("houseNum", edtHouseNo.getText().toString().toUpperCase().trim());
            jsonObject.put("area", edtArea.getText().toString().toUpperCase().trim());
            jsonObject.put("city", city);
            jsonObject.put("pin", edtPin.getText().toString().toUpperCase().trim());
            jsonObject.put("state", state_str);
            //jsonObject.put("dob", DTToN(dd+"-"+MMM+"-"+yy));
            jsonObject.put("dob", DTToN(Dob));
            jsonObject.put("prof", profession);
            jsonObject.put("landmuserark", edtlandmark.getText().toString().toUpperCase().trim());
            jsonObject.put("regpegno", 4);
            Log.e("sendData: ", jsonObject.toString());

            data = jsonObject.toString().getBytes();
//
//            data1="{\"area\":\"AHMEDNAGAR\",\"vppbankname\":\"null null null\",\"city\":\"AHMEDNAGAR\",\"ip\":\"12345\",\"regpegno\":4,\"mobile\":\"8830191346\",\"isBankV\":0,\"bankaccno\":\"168201000017428\",\"prof\":\"HOUSE WIFE\",\"Ifsc\":\"IOBA0001682\",\"isEmailV\":\"1\",\"isMobileV\":\"1\",\"landmuserark\":\"VARQUA SCHOOL\",\"pin\":\"414002\",\"dob\":-313286400,\"name\":\"SMT REKHA J MANJHI\",\"state\":\"MADHYA PRADESH\",\"pan\":\"DDMPM8092Q\",\"vpppanname\":\"SMT REKHA J MANJHI\",\"email\":\"neelammanjhikhushi1019@gmail.com\",\"houseNum\":\"PLOT NO 15 JHAMKHED ROAD GENERAL ARUN VADIYA COLONY\"}".getBytes();
            new SendTOServer(this, this, Const.MSGSIGNUP2, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
//            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(SignupScreen2.this, e.getMessage());
        }


//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("imei", "dfsafd");
//            //  jsonObject.put("ip","1234566666");
//            jsonObject.put("ip", "dfsf");
//            jsonObject.put("name", "Dummy");
//            jsonObject.put("ref", "dfsf");
//            jsonObject.put("refCode", "dfsf");
//            jsonObject.put("mobile", "9890871432");
//            jsonObject.put("email", "tumma9867328538@gmail.com");
//            jsonObject.put("isMobileV", "0");
//            jsonObject.put("isEmailV", "1");
//            jsonObject.put("pan", "ALZPT6417A");
//            jsonObject.put("vpppanname", "shiva");
//            jsonObject.put("isBankV", "5345345435");
//            jsonObject.put("bankaccno", "423423342");
//            jsonObject.put("Ifsc", "shiva");
//            jsonObject.put("vppbankname", "shiva");
//            jsonObject.put("houseNum", "dfsf");
//            jsonObject.put("area", "dfsf");
//            jsonObject.put("city", "mumbai");
//            jsonObject.put("pin", "dfsf");
//            jsonObject.put("state", "maharashtra");
//            //jsonObject.put("dob", DTToN(dd+"-"+MMM+"-"+yy));
//            jsonObject.put("dob", DTToN(Dob));
//            jsonObject.put("prof", profession);
//            jsonObject.put("landmuserark", "dfsf");
//            jsonObject.put("regpegno", 4);
//
//            data = jsonObject.toString().getBytes();
//            Log.e("sendData: ", jsonObject.toString());
//            new SendTOServer(this, this, Const.MSGSIGNUP2, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } catch (JSONException e) {
////            e.printStackTrace();
//            FirebaseCrashlytics.getInstance().recordException(e);
//            AlertDialogClass.ShowMsg(SignupScreen2.this, e.getMessage());
//        }


    }

    private void sendDataStateCity(String statecity, String Stateid, String SearchText) {

        AlertDialogClass.PopupWindowShow(SignupScreen2.this, mainlayout);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Opt", statecity);
            jsonObject.put("StateId", Stateid);
            jsonObject.put("SearchText", SearchText);
            data = jsonObject.toString().getBytes();
            Log.e("RequestStateCity: ", jsonObject.toString());
            new SendTOServer(this, this, Const.MSG_StateCity, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(SignupScreen2.this, e.getMessage());
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class DatePickerFragment extends android.app.DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();

            calendar.set(2005, 11, 30);//Year,Mounth -1,Day

            //
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            //
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            calendar.add(Calendar.DATE, 1);
            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            calendar.add(Calendar.YEAR, -100);
            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
            return dpd;

        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // HERE IS AGE LIMIT ....
            Calendar userAge = new GregorianCalendar(year, month, day);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);

            Log.e("chosenDate", String.valueOf(chosenDate));

            if (minAdultAge.before(userAge)) {
                TastyToast.makeText(getActivity(), "Age limit is 18 years", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                edtDob.setText("");
            } else {
                String formattedDate = dateFormat.format(chosenDate);
                edtDob.setText(formattedDate);
            }

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.edtDob: {

                DialogFragment dFragment = new DatePickerFragment();

                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");
            }
            break;
            case R.id.btn_signup_submit2: {

//                sendData();

                if (Connectivity.getNetworkState(getApplicationContext()))
                    //if (state_str.equalsIgnoreCase("") || state_str.equalsIgnoreCase("Select State")) {
//                        Toast.makeText(this, "select state", Toast.LENGTH_SHORT).show();
                    //  TastyToast.makeText(SignupScreen2.this, "Please select state", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    //} else {
                    //submit.setEnabled(false);
                    validation();
                    //}

                    // sendData();
//                if (state_str.equalsIgnoreCase(edtState.getText().toString().toUpperCase())) {
//                    validation();
//
//                } else {
//                    edtState.setError("select select");
//                }
                else
//                    Views.SweetAlert_NoDataAvailble(Welcome.this,"No Internet");
                    TastyToast.makeText(SignupScreen2.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);

            }
            break;
        }
    }

    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
/*
            if (ringProgressDialog != null) {
                ringProgressDialog.dismiss();
            }
*/

            AlertDialogClass.PopupWindowDismiss();

            switch (msg.arg1) {

                case Const.MSGSIGNUP2: {
                    submit.setEnabled(true);

                    try {
//                        ringProgressDialog.dismiss();

                        String data = (String) msg.obj;
                        Log.e("MessageResponse", data);
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(data);

                        int status = jsonObject.getInt("status");
                        if (status == 0) {
                            String Message = jsonObject.getString("message");
                            if (Message.contains("PAN number already")) {
                                alert(Message);
                            } else {
                                alert(Message);
                            }
//                            Toast.makeText(SignupScreen2.this, Message, Toast.LENGTH_LONG).show();
                            TastyToast.makeText(getApplicationContext(), Message, TastyToast.LENGTH_LONG, TastyToast.INFO);
                        } else if (status == 1) {
                            String VppId = jsonObject.getString("vppcode");
                            //  Logics.setVppCode(SignupScreen2.this,VppId);
                            Date today = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String curdate = dateFormat.format(today);
                            Logics.setDate(SignupScreen2.this, curdate);

                            Logics.setVppDetails(SignupScreen2.this, vppPanName, mobile, email, city, VppId, pan);

                            SharedPref.savePreferences1(SignupScreen2.this, "New", "1");
                            //existing users showing popup ... revenue sharing ..

                            Intent intent = new Intent(SignupScreen2.this, com.application.vpp.activity.Welcome.class);
                            intent.putExtra("issignup", 1);
                            startActivity(intent);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        AlertDialogClass.ShowMsg(SignupScreen2.this, e.getMessage());

                    }
                }
                break;
                case Const.MSG_StateCity: {

                    //                        ringProgressDialog.dismiss();
                    String data = (String) msg.obj;
                    Log.e("MSG_StateCity", data);

                    if (opt.equalsIgnoreCase(Const.StateMaster)) {
                        AlertDialogClass.PopupWindowDismiss();
                        StateName.clear();
                        StateId.clear();

                        StateName.add("Select State");

                        // StateName.add("Select State");
                        //StateId.add("Select State");
                        try {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String StateName_ = jsonObject.getString("StateName");
                                String StateId_ = jsonObject.getString("StateId");
                                StateName.add(StateName_);
                                StateId.add(StateId_);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialogClass.ShowMsg(SignupScreen2.this, e.getMessage());

                        }

                        Log.e("NAME", String.valueOf(StateName.size()));
                        Log.e("ID", String.valueOf(StateId.size()));

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupScreen2.this, android.R.layout.simple_spinner_item, StateName);
                        spinnerStateName.setAdapter(adapter);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerStateName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                                // On selecting a spinner item
//                            ((TextView) adapter.getChildAt(0)).setTextColor(Color.BLACK);

                                if (!StateName.get(position).equalsIgnoreCase("Select State")) {
                                    str_stateID = StateId.get(position - 1);
                                    state_str = StateName.get(position - 1);

                                    Log.e("str_stateID", str_stateID);
                                    Log.e("state_str", state_str);

                                    SharedPref.savePreferences(getApplicationContext(), SharedPref.state, state_str);

                                    spinnercityName.setVisibility(View.VISIBLE);
                                    opt = Const.CityMaster;

                                    sendDataStateCity(opt, str_stateID, "");

                                } else {
                                    state_str = "";
                                    spinnercityName.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        });

                    } else {

                        AlertDialogClass.PopupWindowDismiss();
                        CityName.clear();
                        CityId.clear();
//                         CityName.add("Select City");
                        //CityId.add("Select City");

                        data = String.valueOf(stringBuffer.append(data));

                        if (data.endsWith("]")) {
                            try {
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String CityName_ = jsonObject.getString("CityName");
                                    String CityId_ = jsonObject.getString("CityId");
                                    CityName.add(CityName_);
                                    CityId.add(CityId_);
                                    stringBuffer.setLength(0);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogClass.ShowMsg(SignupScreen2.this, e.getMessage());
                                stringBuffer.setLength(0);


                            }
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupScreen2.this, android.R.layout.simple_spinner_dropdown_item, CityName);
                        spinnercityName.setAdapter(adapter);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnercityName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                                // On selecting a spinner item
//                            ((TextView) adapter.getChildAt(0)).setTextColor(Color.BLACK);
                                // str_stateID = CityId.get(position);
                                city = CityName.get(position);

                                //  Toast.makeText(SignupScreen2.this, city, Toast.LENGTH_SHORT).show();
                                opt = Const.CityMaster;

//                                sendDataStateCity(opt,str_stateID,"");

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        });

                    }

                }
                break;


            }
        }

    }

    @Override
    public void requestSent(int value) {

    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
    }


    @Override
    public void connected() {
        /*if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }*/
        //        AlertDailog.ProgressDlgDiss();

        if (inserSockettLogsArrayList != null) {
            if (inserSockettLogsArrayList.size() > 0) {
                try {
                    SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(SignupScreen2.this),
                            "1",
                            Methods.getVersionInfo(SignupScreen2.this),
                            Methods.getlogsDateTime(), "SignupScreen2",
                            Connectivity.getNetworkState(getApplicationContext()),
                            SignupScreen2.this);

                } catch (Exception e) {
                    Toast.makeText(SignupScreen2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }


        AlertDialogClass.PopupWindowDismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendDataStateCity(opt, str_stateID, "");


                /*else if (checksaverequest){

                    CallSAVECHECKOUT(jsonObjectrequest);
                }else if (checksaveresponse){
                    CallSAVECHECKOUT(jsonObjectresponse);
                }*/
//                else if (checkbalance){
//                    CALLAMOUNT();
//                }
                // TastyToast.makeText(SignupScreen2.this, "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {

        submit.setEnabled(true);
/*
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
*/

        AlertDialogClass.PopupWindowDismiss();
        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(SignupScreen2.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(SignupScreen2.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
       /* if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }*/
        submit.setEnabled(true);


        AlertDialogClass.PopupWindowDismiss();
        Views.SweetAlert_NoDataAvailble(SignupScreen2.this, "Internet Not Available");
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
       /* if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }*/

        AlertDialogClass.PopupWindowDismiss();
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getApplicationContext()))
                    new ConnectTOServer(SignupScreen2.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDlgConnectSocket(SignupScreen2.this, connectionProcess, "Server Not Available");
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
        /*if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }*/
        submit.setEnabled(true);

        AlertDialogClass.PopupWindowDismiss();
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    ProgressDlgConnectSocket(SignupScreen2.this, connectionProcess, "Server Not Available");
//                    ConnectToserver(connectionProcess);
                } else {
                    Toast.makeText(SignupScreen2.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                            SharedPref.insertSocketLogs(inserSockettLogsArrayList, Logics.getVppId(SignupScreen2.this),
                                    "0",
                                    Methods.getVersionInfo(SignupScreen2.this),
                                    Methods.getlogsDateTime(), "SignupScreen2",
                                    Connectivity.getNetworkState(getApplicationContext()),
                                    SignupScreen2.this);

                            finishAffinity();
                            finish();


                        }
                    });
            if (!SignupScreen2.this.isFinishing()) {
                sweetAlertDialog.show();
            } else {
                // Toast.makeText(SignupScreen2.this, "ggggggggggg", Toast.LENGTH_SHORT).show();
            }
            sweetAlertDialog.setCancelable(false);

        } else {
//            new ConnectTOServer(InProcessLeads.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            if (connectionProcess == null) {
                Log.e("DlgConnectSocket11111_null", "called");

            } else {
                new ConnectTOServer(SignupScreen2.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    public static long DTToN(String strDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = sdf.parse(strDate);

            long seconds = date.getTime() / 1000;

            SimpleDateFormat prevDateFormat = new SimpleDateFormat("MMMM d, yyyy, HH:mm:ss z");

            Date prevDate = prevDateFormat.parse("January 1, 1980, 00:00:00 GMT");

            seconds = seconds - prevDate.getTime() / 1000;

            return seconds;
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return 0;
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }


    private void alert(String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.
                verify_update, null);
        builder.setView(dialogView);

        TextView txtMsg = dialogView.findViewById(R.id.txtMsg);
        txtMsg.setText(msg);
        TextView txtclose = dialogView.findViewById(R.id.txtclose);
        txtclose.setText("please Login");

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                alertDialog.cancel();

                startActivity(new Intent(SignupScreen2.this, LoginScreen.class));

            }
        });

        builder.setCancelable(true);
        alertDialog = builder.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    //    String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
//    String day          = (String) DateFormat.format("dd",   date); // 20
//    String monthString  = (String) DateFormat.format("MMM",  date); // Jun
//    String monthNumber  = (String) DateFormat.format("MM",   date); // 06
//    String year         = (String) DateFormat.format("yyyy", date); // 2013


}


