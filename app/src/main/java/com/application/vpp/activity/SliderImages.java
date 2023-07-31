package com.application.vpp.activity;

import android.content.Intent;

import com.application.vpp.Const.Const;
import com.application.vpp.Database.DatabaseHelper;
import com.application.vpp.Interfaces.SliderImagesPojo;
import com.application.vpp.SharedPref.SharedPref;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.application.vpp.Adapters.ImageAdapter;
import com.application.vpp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mehdi.sakout.fancybuttons.FancyButton;

public class SliderImages extends AppCompatActivity implements View.OnClickListener {

    private static ViewPager viewPager;
    FancyButton btnFinish, btnLogin;
    int navigate = 0;
    TextView txtTerms, txtNote;
    ArrayList<SliderImagesPojo> arrayListSliderImages;
    DatabaseHelper databaseHelper;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_images);

        databaseHelper = new DatabaseHelper(SliderImages.this);
        navigate = getIntent().getIntExtra("navigate", 0);
        Log.d("navigate", "onCreate: " + navigate);

        arrayListSliderImages = databaseHelper.getSliderImages();

        for (int k = 0; k < arrayListSliderImages.size(); k++) {
            Log.e("onCreate: ", String.valueOf(arrayListSliderImages.size()));
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ImageAdapter adapter = new ImageAdapter(this, navigate, arrayListSliderImages);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager, true);

        btnFinish = (FancyButton) findViewById(R.id.btnFinish);
        btnLogin = (FancyButton) findViewById(R.id.btnLogin);

        txtTerms = (TextView) findViewById(R.id.txtTerms);

        txtNote = (TextView) findViewById(R.id.txtNote);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 7) {
                    if (navigate == 0) {
                        btnFinish.setVisibility(View.VISIBLE);
                        btnLogin.setVisibility(View.VISIBLE);
                        txtNote.setVisibility(View.VISIBLE);
                    }
                } else {
                    btnLogin.setVisibility(View.GONE);
                    btnFinish.setVisibility(View.GONE);
                    txtNote.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        Log.d("buttonclick", "onClick: ");

        int id = view.getId();
        switch (id) {

            case R.id.btnFinish: {
                startActivity(new Intent(SliderImages.this, SignupScreen.class));
            }
            break;
            case R.id.btnLogin: {
                startActivity(new Intent(SliderImages.this, LoginScreen.class));
                SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");
            }
            break;

            case R.id.txtTerms: {
                startActivity(new Intent(SliderImages.this, TermsConditions.class));

            }
            break;
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (getIntent().getStringExtra("from").equalsIgnoreCase("splashscreen")) {
            finishAffinity();
        } else {
            finish();
        }
    }


}
