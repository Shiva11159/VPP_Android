package com.application.vpp.ContactUsFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

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
import com.application.vpp.activity.AddLead;
import com.application.vpp.activity.ContactUs;
import com.google.android.material.tabs.TabLayout;
import com.sdsmdg.tastytoast.TastyToast;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;


public class ConnectFragment extends Fragment implements RequestSent, ConnectionProcess {
    AlertDialog alertquery, alertcallback;

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;
    ScrollView scrollView;
    ImageView image1, image2;
    RelativeLayout relativeLayout_callBack, relativeLayout_post_ur_query;
    LinearLayout linearLayout_callback_form, linearLayout_post_ur_form;
    //    FancyButton btn_callback_submit, submit_query;
    TextView callback_submit, query_submit;
    //EditText edt_name, edt_phone_number, edt_callback_date, edt_callback_time, query;
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
    public void onAttach(@NonNull Context context) {

        Log.e("onAttach", "ccc ");

        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("onCreate", "ccc ");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.e("onViewCreated", "ccc ");

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e("onStart", "ccc ");

        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("onResume", "ccc ");

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Log.e(" ccc", "ccc ");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.connect_layout, container, false);
        mainlayout = (ScrollView) view.findViewById(R.id.mainLayout);
        connectionProcess = (ConnectionProcess) this;

        handlerConnect = new ViewHandler();
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        relativeLayout_callBack = view.findViewById(R.id.card_view_callBack);
        linearLayout_callback_form = view.findViewById(R.id.card_view_callback_form);
        linearLayout_callback_form.setVisibility(View.GONE);
        linearLayout_post_ur_form = view.findViewById(R.id.card_view_post_query_form);
        linearLayout_post_ur_form.setVisibility(View.GONE);


        relativeLayout_post_ur_query = view.findViewById(R.id.relative_post_ur_query);
        callback_submit = view.findViewById(R.id.callback_submit);
        query_submit = view.findViewById(R.id.query_submit);
        //edt_name = view.findViewById(R.id.edt_name);
//        edt_phone_number = view.findViewById(R.id.edt_phone_number);
//        query = view.findViewById(R.id.q_edt);


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */

        // viewPager.setOffscreenPageLimit(1);

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });


        relativeLayout_callBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mainlayout.getVerticalScrollbarPosition() != mainlayout.getBottom())  // if scrollview is not already on the bottom
//                    mainlayout.post(() -> mainlayout.scrollTo(0, mainlayout.getBottom()));
//                if (visible_hide == 0) {
//                    linearLayout_callback_form.setVisibility(View.GONE);
//                    visible_hide = 1;
//                } else {
//                    edt_name.setText(Logics.getVppName(getContext()));
//                    linearLayout_callback_form.setVisibility(View.VISIBLE);
//                    visible_hide = 0;
//                }


                CallbackMethod();
            }
        });
        relativeLayout_post_ur_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mainlayout.getVerticalScrollbarPosition() != mainlayout.getBottom())  // if scrollview is not already on the bottom
//                    mainlayout.post(() -> mainlayout.scrollTo(0, mainlayout.getBottom()));
//                if (visible_hide2 == 0) {
//                    linearLayout_post_ur_form.setVisibility(View.GONE);
//                    visible_hide2 = 1;
//                } else {
//                    linearLayout_post_ur_form.setVisibility(View.VISIBLE);
//                    visible_hide2 = 0;
//                }

                QueryMethod();
            }
        });

//        callback_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (Connectivity.getNetworkState(getContext())) {
//                    validation();
//                } else {
//                    Views.SweetAlert_NoDataAvailble(getActivity(), "No Internet ! ");
//                }
//
//
//            }
//        });

//        query_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
//                if (Connectivity.getNetworkState(getContext())) {
//                    validationQuery();
//                } else {
//                    Views.SweetAlert_NoDataAvailble(getActivity(), "No Internet ! ");
//                }
//            }
//        });

        return view;
    }

    /* @Override
     public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);


     }*/
    void validationQuery(EditText query) {
        if (query.getText().toString().toUpperCase().trim() == null || query.getText().toString().toUpperCase().trim().equalsIgnoreCase("")) {
            query.setError("Please Enter Query");
        } else {
            sendQuery(query);
        }

    }

    void validationCallBack(EditText edt_name, EditText edt_phone_number) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        if (edt_name.getText().toString().toUpperCase().trim() == null || edt_name.getText().toString().toUpperCase().trim().equalsIgnoreCase("")) {
            edt_name.setError("please enter name");
        } else if (edt_phone_number.getText().toString().toUpperCase().trim() == null || edt_phone_number.getText().toString().toUpperCase().trim().equalsIgnoreCase("")) {
            edt_phone_number.setError("please enter number");
        } else {
            sendDataCallBack(edt_name, edt_phone_number);
        }
    }

    void sendDataCallBack(EditText edt_name, EditText edt_phone_number) {
        try {


            AlertDialogClass.PopupWindowShow(getActivity(), edt_name);
            String vppid = Logics.getVppId(getContext());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", edt_name.getText().toString().toUpperCase().trim());
            jsonObject.put("contact_number", edt_phone_number.getText().toString().toUpperCase().trim());
            jsonObject.put("callback_date", "Nodata");
            jsonObject.put("callback_time", "No data");
            jsonObject.put("vppid", vppid);

            Log.e( "sendDataCallBack: ", jsonObject.toString());
            byte data[] = jsonObject.toString().getBytes();
            new SendTOServer(getActivity(), requestSent, Const.MSGCALLBACK, data, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e( "sendDataCallBack: ", e.getMessage().toString());

            ringProgressDialog.dismiss();
        }
    }

    public void sendQuery(EditText query) {
        AlertDialogClass.PopupWindowShow(getActivity(), query);

        String VppId;
        JSONObject jsonObject = new JSONObject();
        VppId = Logics.getVppId(getContext());
        String Query = query.getText().toString().toUpperCase();
        try {
            jsonObject.put("Subject", "Query Raised By " + VppId);
            jsonObject.put("VppId", VppId);
            jsonObject.put("Query", Query);
            jsonObject.put("intvalue", 2);
            Log.e("sendQuery: ", jsonObject.toString());
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

                    AlertDialogClass.PopupWindowDismiss();
                    clickedquery = true;
                    Log.e( "MSGQUERY" , msg.toString());

                    try {
                        JSONObject jsonObject = new JSONObject(data);

                        int status = jsonObject.getInt("status");
                        String message = jsonObject.getString("message");

                        alertquery.dismiss();
                        alertquery.cancel();

                        if (status != 0) {
                            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            Intent intent = new Intent(getActivity(), ContactUs.class);
                            startActivity(intent);

//                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.fragment_container, fragNews);
//                            ft.detach(fragNews);
//                            ft.attach(fragNews);
//                            ft.commit();
//                            time.setText("");
                        } else {
                            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.INFO);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        AlertDialogClass.ShowMsg(getActivity(), e.getMessage());

                    }
                }
                break;

                case Const.MSGCALLBACK: {
                    Log.e("MSGCALLBACK","zzzzzzzzzzzz");
                    try {
                        Log.e("MSGCALLBACK", data);
                        clickedvalidation = true;
                        AlertDialogClass.PopupWindowDismiss();
                        JSONObject jsonObject = new JSONObject(data);
                        int status = jsonObject.getInt("status");
                        String message = jsonObject.getString("mesage");

                        alertcallback.dismiss();
                        alertcallback.cancel();

                        if (status == 1) {
                            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
//                            linearLayout_callback_form.setVisibility(View.GONE);
                            Intent intent1 = new Intent(getActivity(), ContactUs.class);
                            startActivity(intent1);

                        } else {
                            AlertDialogClass.ShowMsg(getActivity(), message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertDialogClass.ShowMsg(getActivity(), e.getMessage());
                        Log.e( "handleMessage: ", "eee");

                    }
                }
            }
        }
    }


    @Override
    public void connected() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getActivity()))
                    if (clickedvalidation == false) {
                        // validation();
                    } else {
                        // sendQuery();
                    }
                else
                    Views.SweetAlert_NoDataAvailble(getActivity(), "No Internet");
            }
        });
    }


    @Override
    public void serverNotAvailable() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
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
    }

    @Override
    public void ConnectToserver(final ConnectionProcess connectionProcess) {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getActivity()))
                    new ConnectTOServer(getActivity(), connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss == true) {
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
                }, 3000);
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


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CallbackFragment();
                case 1:
                    return new QueryFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    String recent_news = "Callback details";
                    return recent_news;
                case 1:
                    String category = "Query details";
                    return category;
            }
            return null;
        }
    }


    private void CallbackMethod() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_callback, null);
        dialogBuilder.setView(dialogView);

        final EditText name = (EditText) dialogView.findViewById(R.id.et_Name);
        final EditText mobileno = (EditText) dialogView.findViewById(R.id.et_mobileno_);
        name.setText(Logics.getVppName(getContext()));

        final Button button = (Button) dialogView.findViewById(R.id.btn_proceed);

        // dialogBuilder.setTitle("Post Your Query");
//        dialogBuilder.setMessage("please send us your query.");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                if (Connectivity.getNetworkState(getContext())) {
                    validationCallBack(name, mobileno);
                } else {
                    Views.SweetAlert_NoDataAvailble(getActivity(), "No Internet ! ");
                }
            }
        });
//        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//
//
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//            }
//        });

        alertcallback = dialogBuilder.create();
        alertcallback.show();
    }

    private void QueryMethod() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_query, null);
        dialogBuilder.setView(dialogView);

        final EditText et_Query = (EditText) dialogView.findViewById(R.id.etqueryyy);
        final Button button = (Button) dialogView.findViewById(R.id.btn_proceed_query);

        //dialogBuilder.setTitle("Post Your Query");
//        dialogBuilder.setMessage("please send us your query.");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                if (Connectivity.getNetworkState(getContext())) {
                    validationQuery(et_Query);
                } else {
                    Views.SweetAlert_NoDataAvailble(getActivity(), "No Internet ! ");
                }
            }
        });
//        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//
//
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//            }
//        });

        alertquery = dialogBuilder.create();
        alertquery.show();
    }

//    private void QueryALert() {
//
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText name = (EditText) dialogView.findViewById(R.id.customName);
//        final EditText email = (EditText) dialogView.findViewById(R.id.customEmail);
//        final EditText message = (EditText) dialogView.findViewById(R.id.customFeedback);
//
//        dialogBuilder.setTitle("Send FeedBack");
//        dialogBuilder.setMessage("please send me to your feedback.");
//        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String nameStr = name.getText().toString().trim();
//                String emailStr = email.getText().toString();
//                String messageStr = message.getText().toString().trim();
//
//                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                emailIntent.setData(Uri.parse("mailto:"));
//                emailIntent.setType("message/rfc822");
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"Shudiptotrafder@gmail.com"});
//                emailIntent.putExtra(Intent.EXTRA_CC, emailStr);
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Life Scheduler Feedback");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, nameStr + ":" + messageStr);
//
//                if (emailIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(Intent.createChooser(emailIntent, "Send Email ..."));
//                } else {
//                    Toast.makeText(MainActivity.this, "Sorry you don't have any email app", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//            }
//        });
//
//        AlertDialog b = dialogBuilder.create();
//        b.show();
//    }
}


