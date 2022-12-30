package com.application.vpp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Utility.EncodeDecodeImage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class EsignerNSDLWebView extends AppCompatActivity {
    private ProgressBar progressBar;
    private static final String ACTION_ESIGNRESPONSE = "com.nsdl.egov.esign.rdservice.fp.CAPTURE";
    private int REQUEST_CODE = 100;

    public String biometricenv = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esigner_n_s_d_l_web_view);
        TextView titleView = (TextView) findViewById(R.id.title);

//        GlobalClass.cdslResponse = "";

        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
//        list.add(new BasicNameValuePair("pan", "BQGPP6345A"));
        list.add(new BasicNameValuePair("pan", Logics.getPanNo(EsignerNSDLWebView.this)));
      //  list.add(new BasicNameValuePair("pan", "1111111"));     //AZTPT4416B  ,  AHQPB1061P  ,,   ,DQPPM9580K  (ALCPN5199G)
        list.add(new BasicNameValuePair("reqfrom", "tab"));
        list.add(new BasicNameValuePair("isotp", 1 + ""));

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        String reqData = "";

        try {
            reqData = getReqData(list);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /*ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair(JSONKeys.AADHAR, "279602779112"));
        list.add(new BasicNameValuePair(JSONKeys.CONSENT, ""));
        list.add(new BasicNameValuePair(JSONKeys.CONSENTYN, "1"));
        list.add(new BasicNameValuePair(JSONKeys.PAN, "AHQPB1061P"));
        list.add(new BasicNameValuePair("reqfrom", "tab"));

        try {
             reqData = ReadTaskPost.getReqData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

//        Intent intent = getIntent();
//        reqData = intent.getStringExtra("reqdata");
//        String segment = intent.getStringExtra(JSONKeys.SEGMENT);

//        if(segment.equalsIgnoreCase(Consts.KRA)){
//            titleView.setText("Sign KRA document");
//        }else if(segment.equalsIgnoreCase(Consts.SEGMENT_COMM)){
//            titleView.setText("Sign Commodity document");
//        }else if(segment.equalsIgnoreCase(Consts.SEGMENT_EQUITY)){
//            titleView.setText("Sign document");
//        }

        titleView.setText("Sign document");

        String url = "https://vpp.ventura1.com/eKYCServer_WebAPIs/ESignerWeb";
        WebView webview;
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(), "Android");
        webview.postUrl(url, reqData.getBytes());
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        webview.setWebViewClient(new myWebViewClient());

    }

    public class WebAppInterface {

        @JavascriptInterface
        public void sendData(String data) {
            //Get the string value to process
//            Log.e("data", data);
//            System.out.println("Data : " + data);
            String jsonString = EncodeDecodeImage.base64ToString(data);
//            SplashScreen.cdslResponse = jsonString;
////            Intent intent=new Intent();
////            setResult(101,intent);
////            finish();//finishing activity

            Log.e("jsonString",jsonString );

            try {
                JSONArray jsonArray=new JSONArray(jsonString);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                if (jsonObject.getString("status").equalsIgnoreCase("1")){
                    String pdfurl=jsonObject.getString("pdfurl");
                    redirectToNSDLEsignApp(pdfurl);
                }else {
                    String errmsg=jsonObject.getString("errmsg");
                    if (errmsg.equalsIgnoreCase("")){
                        String Message=jsonObject.getString("message");
                        ShowMsg(EsignerNSDLWebView.this, Message);
                       // Toast.makeText(EsignerNSDLWebView.this, Message, Toast.LENGTH_SHORT).show();
                    }else {
                        ShowMsg(EsignerNSDLWebView.this, errmsg);
                        //Toast.makeText(EsignerNSDLWebView.this, errmsg, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("jsonString",e.getMessage() );

            }
//            redirectToNSDLEsignApp();
        }
    }

    private void redirectToNSDLEsignApp(String esignXML) {
        Intent appStartIntent = new Intent();
        appStartIntent.setAction(ACTION_ESIGNRESPONSE);
        //Intent appStartIntent = new Intent(GlobalClass.mainContext, NsdlEsignActivity.class);
        appStartIntent.putExtra("msg", esignXML); // msg contains esign request xml from ASP.
        appStartIntent.putExtra("env", biometricenv); //Possible values PREPROD or PROD (case sensitive).
        appStartIntent.putExtra("returnUrl", getPackageName(getApplicationContext())); // your package name where esign response failure/success will be sent.
        startActivityForResult(appStartIntent, REQUEST_CODE);
    }


    public class myWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(EsignerNSDLWebView.this);
            String message = "SSL Certificate error.";
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = "The certificate authority is not trusted.";
                    break;
                case SslError.SSL_EXPIRED:
                    message = "The certificate has expired.";
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = "The certificate Hostname mismatch.";
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = "The certificate is not yet valid.";
                    break;
            }
            message += " Do you want to continue anyway?";

            builder.setTitle("SSL Certificate Error");
            builder.setMessage(message);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest wrr) {
            view.loadUrl(wrr.getUrl().toString());
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            try {
                System.out.println("Current URL : " + url);
                URL url1 = new URL(url);
                System.out.println("Current URL Query : " + url1.getQuery());
                Log.e("jsonString","Current URL : " + url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("jsonString",e.getMessage() );

            }
        }
    }

    public static String getReqData(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            String encodedValue = pair.getValue();
            result.append(URLEncoder.encode(encodedValue, "UTF-8"));
        }
        String encodedValue = "127.0.0.1";
        result.append("&").append("ipAddress").append("=").append(URLEncoder.encode(encodedValue, "UTF-8"));

        return result.toString();
    }

    //    private void redirectToNSDLEsignApp(String esignXML) {
//        Intent appStartIntent = new Intent();
//        appStartIntent.setAction(ACTION_ESIGNRESPONSE);
//        //Intent appStartIntent = new Intent(GlobalClass.mainContext, NsdlEsignActivity.class);
//        appStartIntent.putExtra("msg", esignXML); // msg contains esign request xml from ASP.
//        appStartIntent.putExtra("env", biometricenv); //Possible values PREPROD or PROD (case sensitive).
//        appStartIntent.putExtra("returnUrl", getPackageName(getApplicationContext())); // your package name where esign response failure/success will be sent.
//        startActivityForResult(appStartIntent, REQUEST_CODE);
//
//
//    }
    public static String getPackageName(Context context){
        String pkgName="";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            pkgName = pInfo.packageName;

        } catch (PackageManager.NameNotFoundException e) {
//            GlobalClass.onError("Error in "+className,e);
        }
        return  pkgName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            PhotoVideoSignatureActivity.up_signature = true;
            PhotoVideoSignatureActivity.signature_status = "1";
            startActivity(new Intent(EsignerNSDLWebView.this,PhotoVideoSignatureActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("from_EsignerWebView",true));

//            379818192005
//            ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(3);
//            list.add(new BasicNameValuePair("msg", eSignResponse));
//            list.add(new BasicNameValuePair("obj", GlobalClass.aadharNum));
//            list.add(new BasicNameValuePair("pan", GlobalClass.Pan + ""));
//            list.add(new BasicNameValuePair("roId", GlobalClass.ROId.trim()));
//            new ReadTaskPost(GlobalClass.mainContext, Esigner.this, list).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Config.WEBAPI_HTTP_URL + "EsignSDKResponse");
        }
    }

    public  void ShowMsg(Context context, String msg) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle("VPP");
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setIcon(R.drawable.vpp_logo);
//        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        alertDialogBuilder.setPositiveButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                arg0.dismiss();
                arg0.cancel();
                Intent intent = new Intent(EsignerNSDLWebView.this, Dashboard.class);
                startActivity(intent);
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
