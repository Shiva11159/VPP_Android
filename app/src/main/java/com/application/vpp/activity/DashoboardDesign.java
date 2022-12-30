//package com.application.vpp.activity;
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.widget.Toolbar;
//import androidx.drawerlayout.widget.DrawerLayout;
//
//import com.aminography.primecalendar.PrimeCalendar;
//import com.aminography.primecalendar.common.CalendarFactory;
//import com.aminography.primecalendar.common.CalendarType;
//import com.aminography.primedatepicker.common.PickType;
//import com.aminography.primedatepicker.picker.PrimeDatePicker;
//import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback;
//import com.aminography.primedatepicker.picker.theme.DarkThemeFactory;
//import com.aminography.primedatepicker.picker.theme.base.ThemeFactory;
//import com.application.vpp.ClientServer.SendTOServer;
//import com.application.vpp.Const.Const;
//import com.application.vpp.Database.DatabaseHelper;
//import com.application.vpp.Datasets.ProductMasterDataset;
//import com.application.vpp.Interfaces.ConnectionProcess;
//import com.application.vpp.Interfaces.RequestSent;
//import com.application.vpp.R;
//import com.application.vpp.ReusableLogics.Logics;
//import com.application.vpp.Utility.AlertDialogClass;
//import com.application.vpp.Views.Views;
//import com.daasuu.cat.CountAnimationTextView;
//import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
//import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
//import com.google.firebase.crashlytics.FirebaseCrashlytics;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import org.jetbrains.annotations.Nullable;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//
//import me.toptas.fancyshowcase.FancyShowCaseQueue;
//import me.toptas.fancyshowcase.FancyShowCaseView;
//import me.toptas.fancyshowcase.listener.OnViewInflateListener;
//
//import static com.application.vpp.Utility.MyApplication.FONT_PATH_CIVIL;
//
//public class DashoboardDesign extends NavigationDrawer implements View.OnClickListener, RequestSent, ConnectionProcess {
//    private DatePicker datePicker2;
//    private Calendar calendar;
//    private int year, month, day;
//
//    ConnectionProcess connectionProcess;
//    RequestSent requestSent;
//    int yearSelected;
//    int monthSelected;
////    ProgressDialog ringProgressDialog;
//
//    //    SpinKitView spin_kit;
//    protected DrawerLayout mDrawerLayout;
//    public static Handler handlerDashoboardDesign;
//    DatabaseHelper dbh;
//    byte[] data = null;
//    private FancyShowCaseQueue fancyShowCaseQueue;
//    FancyShowCaseView fancyShowCaseView1, fancyShowCaseView2, fancyShowCaseView3, fancyShowCaseView4, fancyShowCaseView5;
//    TextView text1, text2, text3, text4, text5;
//    int screenValue = 1;
//    public static int isfirstTime = 0;
//    static Gson gson;
//    TextView accMonth, leadMonth, earningMonth;
//    CountAnimationTextView txtAccountOpened, texttotalLeads, txtClientTraded;
//    ArrayList<ProductMasterDataset> productMasterDatasetArrayList = new ArrayList<>();
//    private PrimeDatePicker datePicker = null;
//    private static final String PICKER_TAG = "PrimeDatePickerBottomSheet";
////    RelativeLayout leadMonthRelative, earningMonthRelative;
//    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
//
//    CountAnimationTextView txtAccountOpenedPrev,txtAccountOpenedPrev1;
//    CountAnimationTextView txtTotalLeadPrev1,txtTotalLeadPrev;
//
//    RelativeLayout mainLayout;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        getLayoutInflater().inflate(R.layout.activity_dashboard_design1, frame);
//
//        dbh = new DatabaseHelper(this);
//
//        connectionProcess = (ConnectionProcess) this;
//        requestSent = (RequestSent) this;
//        handlerDashoboardDesign = new ViewHandler();
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
//        gson = gsonBuilder.create();
//
//        text1 = (TextView) findViewById(R.id.text1);
//        text2 = (TextView) findViewById(R.id.text2);
//        text3 = (TextView) findViewById(R.id.text3);
//        txtAccountOpenedPrev = findViewById(R.id.txtAccountOpenedPrev);
//        txtAccountOpenedPrev1 =  findViewById(R.id.txtAccountOpenedPrev1);
//        txtTotalLeadPrev1 =  findViewById(R.id.txtTotalLeadPrev1);
//        txtTotalLeadPrev =  findViewById(R.id.txtTotalLeadPrev);
//        // text4 = (TextView)findViewById(R.id.text4);
//        // text5 = (TextView)findViewById(R.id.text5);
//
////        txtAccountOpened = (CountAnimationTextView) findViewById(R.id.txtAccountOpened);
////        texttotalLeads = (CountAnimationTextView) findViewById(R.id.texttotalLeads);
////        txtClientTraded = (CountAnimationTextView) findViewById(R.id.txtClientTraded);
////        txtEarn = (CountAnimationTextView) findViewById(R.id.txtEarn);
////        accMonth = (TextView) findViewById(R.id.accMonth);
////        leadMonth = (TextView) findViewById(R.id.leadMonth);
////        earningMonth = (TextView) findViewById(R.id.earningMonth);
//       // leadMonthRelative = findViewById(R.id.leadMonthRelative);
//        //earningMonthRelative = findViewById(R.id.earningMonthRelative);
//        mainLayout = findViewById(R.id.mainLayout);
////        spin_kit = findViewById(R.id.spin_kit);
//
//        isfirstTime = Logics.getInstScreenVal(this);
//        JSONObject jsonObject = new JSONObject();
//        try {
//            String vppid = Logics.getVppId(DashoboardDesign.this);
//            jsonObject.put("vpp_id", vppid);
////            jsonObject.put("reportType", "LeadsCount");
////            jsonObject.put("version", "android");
//            data = jsonObject.toString().getBytes();
//
//
//            Log.e("jsonObjectREQUEST", jsonObject.toString());
////            new SendTOServer(DashoboardDesign.this, DashoboardDesign.this, Const.MSGPRODUCTMASTER,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////            new SendTOServer(DashoboardDesign.this, DashoboardDesign.this, Const.MSGFETCHDOCSTAT,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////            ringProgressDialog = ProgressDialog.show(DashoboardDesign.this, "Please wait ...", "Loading Your Data ...", true);
////            ringProgressDialog.setCancelable(true);
////            ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//            AlertDialogClass.PopupWindowShow(DashoboardDesign.this,mainLayout);
//
//
//
////            spin_kit.setVisibility(View.VISIBLE);
//            new SendTOServer(DashoboardDesign.this, DashoboardDesign.this, Const.MSGFETCHDASHBOARDDesign, data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//            //   new SendTOServer(DashoboardDesign.this,DashoboardDesign.this, Const.MSGFETCHVERSION,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            //    new SendTOServer(DashoboardDesign.this,DashoboardDesign.this, Const.MSGFETCHLEADDETAILREPORT,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            FirebaseCrashlytics.getInstance().recordException(e);
//        }
//
////        earningMonthRelative.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//////                Intent intent = new Intent(DashoboardDesign.this, MonthlyEarrning.class);
//////                startActivity(intent);
//////                v.startAnimation(buttonClick );
////                fn_MonthlyEarningtMethod();
////            }
////        });
////        leadMonthRelative.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//////                Intent intent = new Intent(DashoboardDesign.this, MonthlyLeadCount.class);
//////                startActivity(intent);
//////
//////                v.startAnimation(buttonClick );
////                fn_LeadCountMethod();
//////                Cal2();
////
////            }
////        });
////        fancyShowCaseView1 = new FancyShowCaseView.Builder(this)
////                .title("First Queue Item")
////                .focusOn(text1)
////                .closeOnTouch(false)
////                .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
////                    @Override
////                    public void onViewInflated(@NonNull View view) {
////                        Button btn = (Button) view.findViewById(R.id.btn_action_1);
////                        btn.setOnClickListener(mClickListener);
////                        btn.setText("Next");
////                        TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
////                        TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
////                        txtView.setText("ACCOUNT OPENED");
////                        //  subtitle.setText("Check me out for Application snapshots & upcoming holidays.");
////                        //  subtitle.setText(" Check me out for Application snapshots , viewing  & applying your EWM .. also upcoming holidays.");
////                        subtitle.setText("Total number of your latest month clients who have a account with us.!");
////                        Log.d("0", "account opened: ");
////                        screenValue = 1;
////                    }
////                })
////
////                .build();
//
////        fancyShowCaseView2 = new FancyShowCaseView.Builder(this)
////                .title("Second Queue Item")
////                .focusOn(text2)
////                .closeOnTouch(false)
////                .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
////                    @Override
////                    public void onViewInflated(@NonNull View view) {
////                        Button btn = (Button) view.findViewById(R.id.btn_action_1);
////                        btn.setOnClickListener(mClickListener);
////                        btn.setText("Next");
////                        TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
////                        TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
////                        txtView.setText("TOTAL LEADS");
////                        subtitle.setText("Total number of your latest month leads.!");
////                        Log.d("0", "total leads: ");
////
////                        screenValue = 2;
////
////                    }
////                })
////
////                .build();
//
////        fancyShowCaseView3 = new FancyShowCaseView.Builder(this)
////                .title("Third Queue Item")
////                .focusOn(text3)
////                .closeOnTouch(false)
////                .customView(R.layout.layout_custom_help, new OnViewInflateListener() {
////                    @Override
////                    public void onViewInflated(@NonNull View view) {
////                        Button btn = (Button) view.findViewById(R.id.btn_action_1);
////                        btn.setOnClickListener(mClickListener);
////                        btn.setText("Next");
////                        TextView txtView = (TextView) view.findViewById(R.id.custome_layout_title);
////                        TextView subtitle = (TextView) view.findViewById(R.id.custome_layout_subtitle);
////                        txtView.setText("MONTHLY EARNING");
////                        subtitle.setText("Here you can see your monthly earning.!");
////                        Log.d("0", "monthly earning: ");
////
////
////                        screenValue = 3;
////                        isfirstTime = 1;
////                        Logics.setInstScreenVal(DashoboardDesign.this, isfirstTime);
////
////                    }
////                })
////
////                .build();
//
//
//        if (isFirstTime1()) {
//            fancyShowCaseQueue = new FancyShowCaseQueue()
//                    .add(fancyShowCaseView1)
//                    .add(fancyShowCaseView2)
//                    .add(fancyShowCaseView3);
//            fancyShowCaseQueue.show();
//
//        }
//
////
//
//    }
//
//    View.OnClickListener mClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (screenValue == 1) {
//                fancyShowCaseView1.hide();
//            } else if (screenValue == 2) {
//                fancyShowCaseView2.hide();
//            } else if (screenValue == 3) {
//                fancyShowCaseView3.hide();
//            }
////            else if(screenValue==4) {
////                fancyShowCaseView4.hide();
////            }else if(screenValue==5) {
////                fancyShowCaseView5.hide();
////            }
//
//        }
//    };
//
//    private boolean isFirstTime1() {
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        boolean ranBefore = preferences.getBoolean("RanBefore", false);
//        if (!ranBefore) {
//            // first time
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("RanBefore", true);
//            editor.commit();
//        }
//        return !ranBefore;
//    }
//
//    @Override
//    public void requestSent(int value) {
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    @Override
//    public void connected() {
//
//    }
//
//    @Override
//    public void serverNotAvailable() {
//
//    }
//
//    @Override
//    public void IntenrnetNotAvailable() {
//
//    }
//
//    @Override
//    public void ConnectToserver(ConnectionProcess connectionProcess) {
//
//    }
//
//    @Override
//    public void SocketDisconnected() {
//
//    }
//
//
//    public class ViewHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
////            spin_kit.setVisibility(View.GONE);
////            ringProgressDialog.dismiss();
//
//            AlertDialogClass.PopupWindowDismiss();
//
//            switch (msg.arg1) {
////                case Const.MSGPRODUCTMASTER: {
////                    String data = (String) msg.obj;
////                    productMasterDatasetArrayList = gson.fromJson(data, new TypeToken<ArrayList<ProductMasterDataset>>() {
////                    }.getType());
////                    if (productMasterDatasetArrayList != null) {
////                        dbh.insertProductMaster(productMasterDatasetArrayList);
////                    }
////
////
////                }
////                break;
////                case Const.MSGFETCHVPPDETAILS: {
////
////                    String data = (String) msg.obj;
////
////                    Log.d("vppDetails", "handleMessage: " + data);
////                    try {
////                        JSONObject jsonObject = new JSONObject(data);
////                        String name = jsonObject.getString("name");
////                        String city = jsonObject.getString("city");
////                        String mobile = jsonObject.getString("mobile");
////                        String email = jsonObject.getString("email");
////                        String vppid = jsonObject.getString("vpp_id");
////                        String pan_no = jsonObject.getString("pan_no");
////                        Logics.setVppDetails(DashoboardDesign.this, name, mobile, email, city, vppid, pan_no);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////
////
////                }
////                break;
////                case Const.MSGFETCHDOCSTAT: {
////                    String data = (String) msg.obj;
////
////                    try {
////                        String strTextCongrats = "A gentle reminder to complete your documentation and payment.We would be happy to provide any further assistance, if required. Contact us on vpp@ventura1.com Keep referring, keep earning !";
////                        //String strTextCongrats = "Congratulations ! Your VPP code is : "+Logics.getVppId(DashoboardDesign.this)+". We are glad to inform you that your provisional partner registration is completed. Please click the link below to know the further process to complete the documentation and final registration.";
////                        JSONObject jsonObject = new JSONObject(data);
////                        String days = jsonObject.getString("days");
////                        String is_document_v = jsonObject.getString("is_document_v");
////                        String is_payment_p = jsonObject.getString("is_payment_p");
////                        String is_email_v = jsonObject.getString("is_email_v");
////                        Logics.setPaymentStatus(DashoboardDesign.this, is_payment_p);
////                        Logics.setDocStatus(DashoboardDesign.this, is_document_v);
////                        Log.d("MSGFETCHDOCSTAT", "handleMessage: " + days);
////
////
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////
////                }
////                break;
//
//                case Const.MSGFETCHDASHBOARDDesign: {
//
//                    String data = (String) msg.obj;
//                    Log.e("MSGFETCHDASHBOARDDesign", data);
//
//                 try {
//                     JSONObject jsonObject=new JSONObject(data);
//
//                     String c_m_l_c=jsonObject.getString("c_m_l_c");
//                     String p_m_l_c=jsonObject.getString("p_m_l_c");
//                     String c_m_ao_c=jsonObject.getString("c_m_ao_c");
//                     String p_m_ao_c=jsonObject.getString("p_m_ao_c");
//
////                     txtAccountOpenedPrev.setText(c_m_l_c);
////                     txtAccountOpenedPrev1.setText(p_m_l_c);
////                     txtTotalLeadPrev.setText(c_m_ao_c);
////                     txtTotalLeadPrev1.setText(p_m_ao_c);
//
//                     txtAccountOpenedPrev.setAnimationDuration(500).countAnimation(0, Integer.parseInt(c_m_ao_c));
//                     txtAccountOpenedPrev1.setAnimationDuration(500).countAnimation(0, Integer.parseInt(p_m_ao_c));
//                     txtTotalLeadPrev.setAnimationDuration(500).countAnimation(0, Integer.parseInt(c_m_l_c));
//                     txtTotalLeadPrev1.setAnimationDuration(500).countAnimation(0, Integer.parseInt(p_m_l_c));
//                 }catch (Exception e){
//                     Toast.makeText(DashoboardDesign.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                 }
//
////                    try {
////                        JSONArray jsonArray = new JSONArray(data);
////                        JSONObject jsonObject = jsonArray.getJSONObject(0);
////                        JSONObject jsonEarning = jsonArray.getJSONObject(1);
////
//////                        if (jsonObject.length() != 0) {
//////
//////                            String AccountOpened = jsonObject.getString("AccountOpened");
//////                            String LeadsCount = jsonObject.getString("LeadsCount");
//////                            String ConvertedLeads = jsonObject.getString("ConvertedLeads");
//////                            String ProgressLeads = jsonObject.getString("ProgressLeads");
//////
//////                            txtAcc.setText(AccountOpened);
//////                            txtLead.setText(LeadsCount);
//////
//////                            txtAcc.setAnimationDuration(2000)
//////                                    .countAnimation(0, Integer.parseInt(AccountOpened));
//////                            txtLead.setAnimationDuration(2000)
//////                                    .countAnimation(0, Integer.parseInt(LeadsCount));
//////                        }
////
//////                        if (jsonEarning.length() != 0) {
//////                            String earning = jsonEarning.getString("earning");
//////                            String month = jsonEarning.getString("month");
//////                            String year = jsonEarning.getString("year");
//////
//////                            earningMonth.setText(month + "  " + year);
//////                            txtEarn.setAnimationDuration(2000)
//////                                    .countAnimation(0, Integer.parseInt(earning));
//////                        }
////
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                        FirebaseCrashlytics.getInstance().recordException(e);
////                    }
//
//
//                }
//                break;
//                case Const.MSGFETCHVERSION: {
//
//                    Log.d("", "handleMessage: " + data);
//
//                }
//                break;
//                case Const.MONTHLYLEAD: {
//                    String data = (String) msg.obj;
//                    Log.e("MONTHLYLEADresponse", data);
//                    try {
//                        JSONObject jsonObject = new JSONObject(data);
//                        String count = jsonObject.getString("leadcount");
////                        txt_lead.setVisibility(View.VISIBLE);
////                        txt_lead.setText("Leads Count: "+count);
//                        Views.ShowMsg(DashoboardDesign.this, "Leads Count:", count);
//                    } catch (JSONException e) {
//                        FirebaseCrashlytics.getInstance().recordException(e);
//                    }
//
//                }
//                break;
//
//                case Const.MSGMONTHLYEARNING: {
//                    String data = (String) msg.obj;
//
//                    Log.e("response", data);
//                    try {
//                        JSONObject jsonObject = new JSONObject(data);
//                        if (jsonObject.isNull("")) {
//                            Views.ShowMsg(DashoboardDesign.this, "Earnings", "no data available");
//
//                        } else {
//                            String Payout = jsonObject.getString("payout");
//                            Views.ShowMsg(DashoboardDesign.this, "Payout", Payout);
//                        }
//
//                    } catch (JSONException e) {
//                        FirebaseCrashlytics.getInstance().recordException(e);
//                    }
//
//                }
//                break;
//
//            }
//        }
//    }
//
//    public static View getToolbarNavigationIcon(Toolbar toolbar) {
//        //check if contentDescription previously was set
//        boolean hadContentDescription = TextUtils.isEmpty(toolbar.getNavigationContentDescription());
//        String contentDescription = !hadContentDescription ? (String) toolbar.getNavigationContentDescription() : "navigationIcon";
//        toolbar.setNavigationContentDescription(contentDescription);
//        ArrayList<View> potentialViews = new ArrayList<View>();
//        //find the view based on it's content description, set programatically or with android:contentDescription
//        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
//        //Nav icon is always instantiated at this point because calling setNavigationContentDescription ensures its existence
//        View navIcon = null;
//        if (potentialViews.size() > 0) {
//            navIcon = potentialViews.get(0);
//
//            //navigation icon is ImageButton
//
//        }
//        //Clear content description if not previously present
//        if (hadContentDescription)
//            toolbar.setNavigationContentDescription(null);
//        return navIcon;
//    }
//
//    private PrimeCalendar getMinDateCalendar(CalendarType calendarType) {
//        PrimeCalendar minDateCalendar = null;
////        if (minDateCheckBox.isChecked()) {
//        minDateCalendar = CalendarFactory.newInstance(calendarType);
//        minDateCalendar.add(Calendar.YEAR, -4);
////        }
//        return minDateCalendar;
//    }
//
//    private PrimeCalendar getMaxDateCalendar(CalendarType calendarType) {
//        PrimeCalendar maxDateCalendar = null;
////        if (maxDateCheckBox.isChecked()) {
//        maxDateCalendar = CalendarFactory.newInstance(calendarType);
//        maxDateCalendar.add(Calendar.YEAR, 0);
////        }
//        return maxDateCalendar;
//    }
//
//    private String getTypeface(CalendarType calendarType) {
//        switch (calendarType) {
//            case CIVIL:
//                return FONT_PATH_CIVIL;
//            default:
//                return null;
//        }
//    }
//
//    private ThemeFactory getDefaultTheme(final String typeface) {
//       /* if (lightThemeRadioButton.isChecked()) {
//            return new LightThemeFactory() {
//                @Nullable
//                @Override
//                public String getTypefacePath() {
//                    return typeface;
//                }
//            };
//        } else {
//            return new DarkThemeFactory() {
//                @Nullable
//                @Override
//                public String getTypefacePath() {
//                    return typeface;
//                }
//            };
//        }*/
//        return new DarkThemeFactory() {
//            @Nullable
//            @Override
//            public String getTypefacePath() {
//                return typeface;
//            }
//        };
//    }
//
//    private RangeDaysPickCallback rangeDaysPickCallback = new RangeDaysPickCallback() {
//        @Override
//        public void onRangeDaysPicked(PrimeCalendar startDay, PrimeCalendar endDay) {
//            Log.e("kk", String.format("From: %s\nTo: %s", startDay.getLongDateString(), endDay.getLongDateString()));
//            Log.e("ooo: ", startDay.getLongDateString());
//            SimpleDateFormat formatter1 = new SimpleDateFormat("E, dd MMM yyyy");
//            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-M-dd");
//            try {
//                Date date_startDay = formatter1.parse(startDay.getLongDateString());
//                Date date_endDay = formatter1.parse(endDay.getLongDateString());
//                Log.e("qqqq", formatter2.format(date_startDay));
//                fetchLeadMonthly(formatter2.format(date_startDay), formatter2.format(date_endDay));
//            } catch (ParseException e) {
//                Log.e("date44", String.valueOf(e.getMessage()));
//                e.printStackTrace();
//                FirebaseCrashlytics.getInstance().recordException(e);
//            }
//        }
//    };
//
////    private SingleDayPickCallback singleDayPickCallback = new SingleDayPickCallback() {
////        @Override
////        public void onSingleDayPicked(PrimeCalendar singleDay) {
////
////            SimpleDateFormat formatter1 = new SimpleDateFormat("E, dd MMM yyyy");
////            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
////            try {
////                Date date_startDay = formatter1.parse(singleDay.getLongDateString());
//////                fetchLeadMonthly(formatter2.format(date_startDay),formatter2.format(date_startDay));
////                fetchEarningMonthly(formatter2.format(date_startDay));
////                Log.e("1111: ", formatter2.format(date_startDay));
////            } catch (ParseException e) {
////                Log.e("date44", String.valueOf(e.getMessage()));
////                e.printStackTrace();
////            }
////        }
////    };
//
//    public void fn_LeadCountMethod() {
//        CalendarType calendarType = CalendarType.CIVIL;
//        Locale locale = CalendarFactory.newInstance(calendarType).getLocale();
//        PickType pickType = PickType.RANGE_START;
//        PrimeCalendar minDateCalendar = getMinDateCalendar(calendarType);
//        PrimeCalendar maxDateCalendar = getMaxDateCalendar(calendarType);
//        final String typeface = getTypeface(calendarType);
//        ThemeFactory theme = getDefaultTheme(typeface);
//        PrimeCalendar today = CalendarFactory.newInstance(calendarType, locale);
//
//        datePicker = PrimeDatePicker.Companion.dialogWith(today)
//                .pickRangeDays(rangeDaysPickCallback)
//                .minPossibleDate(minDateCalendar)
//                .maxPossibleDate(maxDateCalendar)
////                .applyTheme(theme)
//                .build();
//
//        datePicker.show(getSupportFragmentManager(), PICKER_TAG);
//
//    }
//
//    public void fn_MonthlyEarningtMethod() {
//        /*CalendarType calendarType = CalendarType.CIVIL;
//        Locale locale = CalendarFactory.newInstance(calendarType).getLocale();
//        PickType pickType = PickType.RANGE_START;
//        PrimeCalendar minDateCalendar = getMinDateCalendar(calendarType);
//        PrimeCalendar maxDateCalendar = getMaxDateCalendar(calendarType);
//        final String typeface = getTypeface(calendarType);
//        ThemeFactory theme = getDefaultTheme(typeface);
//        PrimeCalendar today = CalendarFactory.newInstance(calendarType, locale);
//
//        datePicker = PrimeDatePicker.Companion.bottomSheetWith(today)
//                .pickSingleDay(singleDayPickCallback)
//                .minPossibleDate(minDateCalendar)
//                .maxPossibleDate(maxDateCalendar)
////                        .applyTheme(theme)
//                .build();
//
//        datePicker.show(getSupportFragmentManager(), PICKER_TAG);*/
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.clear();
//        calendar.set(2018, 0, 1); // Set minimum date to show in dialog
//        long minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date
//
//        calendar.clear();
//        calendar.set(2020, 11, 31); // Set maximum date to show in dialog
//        long maxDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date
//        yearSelected = calendar.get(Calendar.YEAR);
//        monthSelected = calendar.get(Calendar.MONTH);
//
//        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
//                .getInstance(monthSelected, yearSelected, minDate, maxDate);
//
//        dialogFragment.show(getSupportFragmentManager(), null);
//
//        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(int year, int monthOfYear) {
//                // do something
//
//                Log.e("onDateSet: ", year + "___" + monthOfYear);
//
//                fetchEarningMonthly(year + "-" + monthOfYear + "-01");
//
//            }
//        });
//    }
//
//    public void fetchEarningMonthly(String dates) {
//        try {
//            String vppid = Logics.getVppId(DashoboardDesign.this);/*"72020"*/
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("vppid", vppid);
//            jsonObject.put("dates", dates);
//            Log.e("fetchEarningMonthlyREQ", jsonObject.toString());
//            byte[] data = jsonObject.toString().getBytes();
////            ringProgressDialog = ProgressDialog.show(DashoboardDesign.this, "Please wait ...", "Loading Your Data ...", true);
////            ringProgressDialog.setCancelable(true);
////            ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//
//            AlertDialogClass.PopupWindowShow(DashoboardDesign.this,mainLayout);
//
//
//            new SendTOServer(DashoboardDesign.this, requestSent, Const.MSGMONTHLYEARNING, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            FirebaseCrashlytics.getInstance().recordException(e);
//        }
//    }
//
//    public void fetchLeadMonthly(String fromdate, String todate) {
//        try {
////            String vppid = "ALRPV1053A";
//            String vppid = Logics.getVppId(DashoboardDesign.this);/*"72020"*/
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("vppid", vppid);
//            jsonObject.put("fromdate", fromdate);
//            jsonObject.put("todate", todate);
//            Log.e("jsonObject111", jsonObject.toString());
//            byte[] data = jsonObject.toString().getBytes();
////            spin_kit.setVisibility(View.VISIBLE);
////            ringProgressDialog = ProgressDialog.show(DashoboardDesign.this, "Please wait ...", "Loading Your Data ...", true);
////            ringProgressDialog.setCancelable(true);
////            ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//
//            AlertDialogClass.PopupWindowShow(DashoboardDesign.this,mainLayout);
//
//
//            new SendTOServer(DashoboardDesign.this, requestSent, Const.MONTHLYLEAD, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            FirebaseCrashlytics.getInstance().recordException(e);
//        }
//    }
///*
//    public static void buttonEffect(View button){
//        button.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
//                        v.invalidate();
//                        break;
//                    }
//                    case MotionEvent.ACTION_UP: {
//                        v.getBackground().clearColorFilter();
//                        v.invalidate();
//                        break;
//                    }
//                }
//                return false;
//            }
//        });
//    }
//*/
//
//public void Cal2(){
//    calendar = Calendar.getInstance();
//    year = calendar.get(Calendar.YEAR);
//
//    month = calendar.get(Calendar.MONTH);
//    day = calendar.get(Calendar.DAY_OF_MONTH);
//    showDate(year, month+1, day);
//}
//
//
//    private void showDate(int year, int month, int day) {
//
//        text2.setText(new StringBuilder().append(year).append("-")
//                .append(month).append("-").append(year));
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        // TODO Auto-generated method stub
//        if (id == 999) {
//            return new DatePickerDialog(this, myDateListener, year, month, day);
//        }
//        return null;
//    }
//    private DatePickerDialog.OnDateSetListener myDateListener = new
//            DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker arg0,
//                                      int arg1, int arg2, int arg3) {
//                    // TODO Auto-generated method stub
//                    // arg1 = year
//                    // arg2 = month
//                    // arg3 = day
//                    showDate(arg1, arg2+1, arg3);
//                }
//            };
//}
//
