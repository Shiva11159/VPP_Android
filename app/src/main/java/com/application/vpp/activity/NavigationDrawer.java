package com.application.vpp.activity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.DescrpncyIntrfce;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Views.Views;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

public class NavigationDrawer extends AppCompatActivity implements RequestSent, DescrpncyIntrfce {

    boolean checkDiscpncy = false;
    protected DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    FrameLayout frame;
    Toolbar toolbar;
    LinearLayout left_drawer, followUs;
    RelativeLayout layoutAddLead, layoutInProcess, layoutMyLeads, layoutRejected, layoutNI, layoutClientList, layoutBrokerage, layoutProfile, layoutReferralLink, layoutcallback,
            layoutAboutVpp, layoutFAQ, layoutUpcomingEvents, layoutContact, layoutQuery, layoutDashBoard2, layoutdiscrepancy, layoutdiscrepancyup, layoutsubpartner, layoutPrivacypolicy, layoutlogout;
    RelativeLayout layoutfollowUs, facebook, twitter, venturaBlog, youtube, instagram, linkedin;
    TextView txtName, txtVppId, txtVer;
    String name = "";
    ImageView imgConnection;
    public static Handler handlerNavigation;
    public static final String PREFS_NAME = "LoginPrefs";
    ConnectionProcess connectionProcess;
    LinearLayout lineinternet;
    TextView txtinternet;
    int duration = Snackbar.LENGTH_LONG;
    View view;
    RequestSent requestSent;
    RelativeLayout mainLayout;

//    public NavigationDrawer(boolean check) {
//        this.checkDiscpncy = check;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        frame = (FrameLayout) findViewById(R.id.frame);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        left_drawer = (LinearLayout) findViewById(R.id.left_drawer);
        view = findViewById(R.id.toolbar);
        requestSent = (RequestSent) this;
        imgConnection = (ImageView) findViewById(R.id.imgConnection);
        handlerNavigation = new ViewHandler();

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        layoutAddLead = (RelativeLayout) findViewById(R.id.layoutAddLead);
        layoutInProcess = (RelativeLayout) findViewById(R.id.layoutInProcess);
        layoutMyLeads = (RelativeLayout) findViewById(R.id.layoutMyLeads);
        layoutRejected = (RelativeLayout) findViewById(R.id.layoutRejected);
        layoutNI = (RelativeLayout) findViewById(R.id.layoutNotInterested);
        layoutClientList = (RelativeLayout) findViewById(R.id.layoutClientList);
        layoutBrokerage = (RelativeLayout) findViewById(R.id.layoutBrokerage);
        layoutProfile = (RelativeLayout) findViewById(R.id.layoutProfile);
        layoutReferralLink = (RelativeLayout) findViewById(R.id.layoutReferralLink);
        layoutcallback = (RelativeLayout) findViewById(R.id.layoutcallback);
        layoutAboutVpp = (RelativeLayout) findViewById(R.id.layoutAboutVpp);
        layoutContact = (RelativeLayout) findViewById(R.id.contactus);
        layoutQuery = (RelativeLayout) findViewById(R.id.layoutQuery);
        layoutDashBoard2 = (RelativeLayout) findViewById(R.id.layoutDashBoard2);
        layoutdiscrepancy = (RelativeLayout) findViewById(R.id.layoutdiscrepancy);
        layoutdiscrepancyup = (RelativeLayout) findViewById(R.id.layoutdiscrepancyup);
        layoutsubpartner = (RelativeLayout) findViewById(R.id.layoutsubpartner);
        layoutPrivacypolicy = (RelativeLayout) findViewById(R.id.layoutPrivacypolicy);//  added by pravin 28.01.2021
        layoutlogout = (RelativeLayout) findViewById(R.id.layoutlogout);
//        layoutDashBoard2.setVisibility(View.GONE);


        layoutFAQ = (RelativeLayout) findViewById(R.id.layoutFAQ);
        layoutUpcomingEvents = (RelativeLayout) findViewById(R.id.layoutUpcomingEvents);
        layoutfollowUs = (RelativeLayout) findViewById(R.id.layoutFollowUs);
        followUs = (LinearLayout) findViewById(R.id.FollowUs);

        facebook = (RelativeLayout) findViewById(R.id.facebook);
        twitter = (RelativeLayout) findViewById(R.id.twitter);
        venturaBlog = (RelativeLayout) findViewById(R.id.venturaBlog);
        youtube = (RelativeLayout) findViewById(R.id.youtube);
        instagram = (RelativeLayout) findViewById(R.id.instagram);
        linkedin = (RelativeLayout) findViewById(R.id.linkedin);


        txtName = (TextView) findViewById(R.id.txtVPPName);
        txtVppId = (TextView) findViewById(R.id.txtVPPId);
        txtVer = (TextView) findViewById(R.id.txtVer);

//        connectionProcess = (ConnectionProcess) this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String prodUat = "";
        if (Const.SERVER_PORT == 7778) {
            prodUat = "Prod";
        } else {
            prodUat = "Uat";
        }

        if (name != null) {
            txtVppId.setText("VPP ID : " + Logics.getVppId(this));
            txtName.setText(Logics.getVppName(this));
            txtVer.setText("v" + getVersionInfo() + " " + prodUat);
        }

//        if (!Const.isSocketConnected) {
//            imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol_red));
//        }else{
//            imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol));
//        }
        //  layoutAddLead.setOnClickListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                left_drawer.bringToFront();

                txtName.setText(Logics.getVppName(NavigationDrawer.this));
                txtVppId.setText("VPP ID : " + Logics.getVppId(NavigationDrawer.this));

                //       mDrawerLayout.requestLayout();


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }


        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

//    imgConnection.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (!Const.isSocketConnected) {
//                        if (Connectivity.getNetworkState(getApplicationContext())) {
//                            imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol));
//                            new ConnectTOServer(NavigationDrawer.this, connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        } else {
//                            TastyToast.makeText(getApplicationContext(), "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                            imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol_red));
//                        }
//                    }
//                   /* new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (Const.dismiss == true) {
//                                if (Const.isServerConnected == true && Const.isSocketConnected == false) {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                          //  Views.ProgressDlgConnectSocket(NavigationDrawer.this, connectionProcess, "Server Not Available");
//                                             ConnectToserver(connectionProcess);
//                                          //  imgConnection.setImageResource((R.drawable.ic_up_and_down_arrows_symbol_red));
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    }, 1000);*/
//                }
//            });
//
//        }
//    });
    }


    public void onDrawerClick(final View view) {
        //   Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawers();

        Log.d("click", "onDrawerClick: ");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int id = view.getId();

                switch (id) {

                    case R.id.layoutAddLead: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(getApplicationContext(), AddLead.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("from", ""));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    }
                    break;

                }

                switch (id) {

                    case R.id.layoutInProcess: {

//                        if (Connectivity.getNetworkState(getApplicationContext())) {
//                            startActivity(new Intent(NavigationDrawer.this, InProcessLeads.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                        } else {
//                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                        }
                    }
                    break;

                }

                switch (id) {

                    case R.id.layoutMyLeads: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, MyLeads.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                    break;

                }

                switch (id) {

                    case R.id.layoutRejected: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, RejectedList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                    break;

                }

                switch (id) {

                    case R.id.layoutNotInterested: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, NotInterested.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                    break;

                }


                switch (id) {

                    case R.id.layoutClientList: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, ClientList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                    break;

                }


                switch (id) {

                    case R.id.layoutBrokerage: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, Payout.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                    break;

                }

                switch (id) {

                    case R.id.notifications: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, Notifications.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                    break;

                }

                switch (id) {
                    case R.id.layoutProfile: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, Profile.class).putExtra("from", "").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                    break;

                }

                switch (id) {
//                    case R.id.layoutReferralLink: {
//                        if (Connectivity.getNetworkState(getApplicationContext())) {
//
//                            //startActivity(new Intent(Dashboard.this, DiscripancyActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
////                    Intent sendIntent = new Intent();
////                    sendIntent.setAction(Intent.ACTION_SEND);
////                    sendIntent.putExtra(Intent.EXTRA_TEXT, "" + Logics.getPLFOA(Dashboard.this));
////                    sendIntent.setType("text/plain");
////                    startActivity(sendIntent);
//
//
//                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                            shareIntent.setType("text/plain");
//                            String shareSubText = "WhatsApp - The Great Chat App";
//                            String shareBodyText = "https://play.google.com/store/apps/details?id=com.whatsapp&hl=en";
//                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
//                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
//                            startActivity(Intent.createChooser(shareIntent, "Share With"));
//
////                            String link = Logics.getPLFOA(NavigationDrawer.this);
////                            Log.e("link", link);
//                            layoutReferralLink.setVisibility(View.VISIBLE);
////                            Intent intent = new Intent();
////                            intent.setAction(Intent.ACTION_SEND);
////                            intent.putExtra(Intent.EXTRA_TEXT, "" + Logics.getPLFOA(NavigationDrawer.this));
////                            intent.setType("text/plain");
////                            Intent chooserIntent = Intent.createChooser(intent, "share your referral link : \n" + Logics.getPLFOA(NavigationDrawer.this));
////                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
////                            startActivity(chooserIntent);
//
//                          /*  if (link.length() > 0) {
//
////                                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
////                                clipboard.setText(Logics.getPLFOA(NavigationDrawer.this));
//
//                               *//* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                                    final android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getApplicationContext()
//                                            .getSystemService(Context.CLIPBOARD_SERVICE);
//                                    final android.content.ClipData clipData = android.content.ClipData
//                                            .newPlainText("text label", Logics.getPLFOA(NavigationDrawer.this));
//                                    clipboardManager.setPrimaryClip(clipData);
//                                } else {
//                                    final android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) getApplicationContext()
//                                            .getSystemService(Context.CLIPBOARD_SERVICE);
//                                    clipboardManager.setText(Logics.getPLFOA(NavigationDrawer.this));
//                                }*//*
//
//                                if (link.equalsIgnoreCase("NOT IDENTIFIED")) {
//                                    layoutReferralLink.setVisibility(View.GONE);
//                                } else if (link.equalsIgnoreCase("Not VPP")) {
//                                    layoutReferralLink.setVisibility(View.GONE);
//                                }else {
//                                    layoutReferralLink.setVisibility(View.VISIBLE);
//                                    Intent intent = new Intent();
//                                    intent.setAction(Intent.ACTION_SEND);
//                                    intent.putExtra(Intent.EXTRA_TEXT, "" + Logics.getPLFOA(NavigationDrawer.this));
//                                    intent.setType("text/plain");
//                                    Intent chooserIntent = Intent.createChooser(intent, "share your referral link : \n" + Logics.getPLFOA(NavigationDrawer.this));
//                                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
//                                    startActivity(chooserIntent);
//                                }
//                            }else {
//                                layoutReferralLink.setVisibility(View.GONE);
//                            }*/
//
//                        } else {
//                            Views.SweetAlert_NoDataAvailble(NavigationDrawer.this, "Connect internet !");
//                        }
//                    }
//                    break;

                }

                switch (id) {
                    case R.id.layoutAboutVpp: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, SliderImages.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("navigate", 1).putExtra("from", "navigate"));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                    break;
                }
                switch (id) {

                    case R.id.contactus: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, ContactUs.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("navigate", 1));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                    break;
                }
                switch (id) {

                    case R.id.layoutFAQ: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, WebActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("link", "https://vpp.ventura1.com/FAQ&Events/FAQ.jsp"));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                        //    case R.id.layoutFAQ:{startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vpp.ventura1.com/FAQ&Events/FAQ.jsp")));

                    }
                    break;
                }
                switch (id) {

                    case R.id.layoutUpcomingEvents: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, WebActivity2.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("link", "https://vpp.ventura1.com/FAQ&Events/events.jsp"));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    }
                    break;
                }
                switch (id) {
                    case R.id.layoutQuery: {

                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, QueryStatus.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("link", "https://vpp.ventura1.com/FAQ&Events/events.jsp"));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    }
                    break;
                }
                switch (id) {
                    case R.id.layoutDashBoard2: {
//                        if (Connectivity.getNetworkState(getApplicationContext())) {
//                            startActivity(new Intent(NavigationDrawer.this, DashoboardDesign.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                        } else {
//                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                        }

                    }
                    break;
                }
                switch (id) {
                    case R.id.layoutdiscrepancy: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, DiscripancyActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    }
                    break;
                }
                switch (id) {
                    case R.id.layoutdiscrepancyup: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, DiscripancyActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    }
                    break;
                }
                switch (id) {
                    case R.id.layoutsubpartner: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, SubPatnerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    }
                    break;
                }
                switch (id) {
                    case R.id.layoutPrivacypolicy: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, com.application.vpp.activity.PrivacyPolicy.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("link", "https://vpp.ventura1.com/VPP/privacy.html"));
                            //startActivity(new Intent(NavigationDrawer.this, LoginScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    }
                    break;
                }


                switch (id) {
                    case R.id.layoutlogout: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.remove("logged");
                            editor.commit();
                            finish();
                            startActivity(new Intent(NavigationDrawer.this, LoginScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }

                    }
                    break;
                }
//                switch (id) {
//                    case R.id.layoutShareLink: {
//                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//                        share.setType("text/plain");
//                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                        // Add data to the intent, the receiving app will decide
//                        // what to do with it.
//                        share.putExtra(Intent.EXTRA_SUBJECT, "Vpp Link Share");
//                        share.putExtra(Intent.EXTRA_TEXT, "Vpp Testing Link  " + "https://ekyc.ventura1.com:51530/EKYCServer/VerifyPANBipin");
//                        startActivity(Intent.createChooser(share, "Share link!"));
//
//                    }
//                    break;
//                }

                switch (id) {

                    case R.id.layoutcallback: {
                        if (Connectivity.getNetworkState(getApplicationContext())) {
                            startActivity(new Intent(NavigationDrawer.this, CallBackDetailsList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            TastyToast.makeText(NavigationDrawer.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                    break;

                }

            }
        }, 300);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);


    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }



    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            try {
//                AlertDialogClass.PopupWindowDismiss();
//                String data = (String) msg.obj;
//                JSONObject js = new JSONObject(data);
//
//                Log.e("LINK", data);
//
//                if (js.getInt("Status") == 1) {
//                    Toast.makeText(NavigationDrawer.this, "" + js.getString("ErrorMessage"), Toast.LENGTH_SHORT).show();
//                } else {
//
//                    String link = (js.getString("URL") != null) ? js.getString("URL") : "";
//                    Logics.setPLFOA(NavigationDrawer.this, link);
//
//                    if (link.length() > 0) {
//                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                        clipboard.setText(Logics.getPLFOA(NavigationDrawer.this));
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_SEND);
//                        intent.putExtra(Intent.EXTRA_TEXT, "" + clipboard.getText());
//                        intent.setType("text/plain");
//                        Intent chooserIntent = Intent.createChooser(intent, "Chooser Title");
//                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
//                        startActivity(chooserIntent);
//                    } else {
//                        personlized_link_for_accnt_opnApicall();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    public void buttonOnClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.layoutFollowUs: {

                if (followUs.getVisibility() == View.GONE) {

                    followUs.setVisibility(View.VISIBLE);
                } else {
                    followUs.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.facebook: {
                Intent facebookView = new Intent("android.intent.action.VIEW", Uri.parse("https://m.facebook.com/venturasecurities/"));
                startActivity(facebookView);
                break;
            }
            case R.id.twitter: {
                Intent twitterView = new Intent("android.intent.action.VIEW", Uri.parse("https://mobile.twitter.com/Ventura_Sec"));
                startActivity(twitterView);
                break;
            }
            case R.id.venturaBlog: {
                Intent venturaBlogView = new Intent("android.intent.action.VIEW", Uri.parse("https://blog.ventura1.com/"));
                startActivity(venturaBlogView);
                break;
            }
            case R.id.youtube: {
                Intent youtubeView = new Intent("android.intent.action.VIEW", Uri.parse("https://www.youtube.com/channel/UC_cqLWN4uMEOAHbocqFI63w"));
                startActivity(youtubeView);
                break;
            }
            case R.id.instagram: {
                Intent instagramView = new Intent("android.intent.action.VIEW", Uri.parse("https://instagram.com/ventura_securities?igshid=mg3s5yexzure"));
                startActivity(instagramView);
                break;
            }
            case R.id.linkedin: {
                Intent linkedinView = new Intent("android.intent.action.VIEW", Uri.parse("https://www.linkedin.com/authwall?trk=bf&trkInfo=AQEgrmFtUEl6hgAAAWv_STA4X1G54_4hRD61wu0AfxEPQYKi_kR0q1OnobNUh0lJWbe87Aqj3pI5mV-FrIT5nN30MoKstBInBlkEar60vaoladEIrspRE-5LZwak0r2Cj46H7ww=&originalReferer=&sessionRedirect=https%3A%2F%2Fwww.linkedin.com%2Fcompany%2Fventura-securities-ltd%2F"));
                startActivity(linkedinView);
                break;
            }


        }
    }

    private String getVersionInfo() {
        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        Log.d("versionName", "getVersionInfo: " + versionName);
        return versionName;

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (!Const.isSocketConnected) {
//           // Toast.makeText(NavigationDrawer.this,"on Resume",Toast.LENGTH_SHORT).show();
//            //imgConnection.setBackground(getResources().getDrawable(R.drawable.ic_up_and_down_arrows_symbol_red));
////            imgConnection.setImageDrawable(getResources().getDrawable(R.drawable.ic_aadhar));
////            imgConnection.setImageResource(R.drawable.ic__25_bell_icon);
//            imgConnection.setImageResource(R.drawable.ic_up_and_down_arrows_symbol_red);
//        }else{
//            imgConnection.setBackground(getResources().getDrawable(R.drawable.ic_up_and_down_arrows_symbol));
//        }
//    }

    public void showSnackbar(String message) {
        Snackbar.make(view, message, duration).show();
    }

    @Override
    public void requestSent(int value) {

    }



    @Override
    public void CheckUp() {
        layoutdiscrepancyup.setVisibility(View.VISIBLE); //setting up..
        layoutdiscrepancy.setVisibility(View.GONE);
    }

    @Override
    public void CheckDown() {
        layoutdiscrepancyup.setVisibility(View.GONE);
        layoutdiscrepancy.setVisibility(View.VISIBLE); //setting down..
    }
}
