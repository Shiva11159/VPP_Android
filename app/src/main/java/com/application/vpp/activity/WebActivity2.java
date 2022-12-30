package com.application.vpp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.application.vpp.R;

public class WebActivity2 extends  NavigationDrawer {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_web, mDrawerLayout);
        webView = findViewById(R.id.webView);
        Intent intent = getIntent();
        String faq = intent.getStringExtra("link");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(faq);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
