package com.application.vpp.ClientServer;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.application.vpp.AES.EncryptDecrypt;
import com.application.vpp.Const.Const;
import com.application.vpp.Interfaces.NetProcess;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.TrustManager;


/**
 * <h1>ReadTaskPost</h1>
 * ReadTaskPost Is a Class That Provides Ability To Read Data From Internet and provide ResultCode and Result it will not throw and exception
 * This Class uses POST method
 * TODO: Test for Very Large Date Set ie More Than 100Kb it has been tested to read Data UP to 100Kb of data
 * after creating the object we pass URL to execute Method as it Extends AsyncTask
 * This Class Passes Result To NetProcess Object With Response Code
 * To pass URL follow the example<b>new ReadTaskPost(context,process,nameValuePairList).execute(URL);</b> to pass url use execute method
 * <i>Note: Belongs to AsyncTask and ReadTaskPost extends AsyncTask And All web activities are done in AsyncTask or else it will give an  exception</i>
 *
 * @author Azim Charaniya
 * @author Azim Charaniya
 * @version 1.1
 * @since 2014-11-05
 * GZIP support for web site to load data Supports GZIP encoding
 */


public class ReadTaskPost extends AsyncTask<String, Integer, String> {
    private boolean connectionAvailable = false;
    private boolean showProgressDialog = true;
    private String progresssMsg = "";

    private ProgressDialog mDialog;
    private Context context;
    private NetProcess process;
    private List<NameValuePair> nameValuePairList;
    private String resMsg;
    private int resCode;
    private int HTTP_RESULT = HttpsURLConnection.HTTP_OK;

    private boolean error = false;
    private int timeout = 0;


    public static SSLContext sslcontext;
    private String errorMsg = "";

    /**
     * @param context           it can be Object or Activity or something else;
     * @param process           Object of NetProcess
     * @param nameValuePairList This is a List Of Parameters Passed To the URL
     *                          this shows an ProgressDialog showing Please wait... if you don't want this then use ReadTaskPost(NetProcess process, List<NameValuePair> nameValuePairList)
     */

    public ReadTaskPost(Context context, NetProcess process, List<NameValuePair> nameValuePairList) {
        this.context = context;
        this.process = process;
        this.nameValuePairList = nameValuePairList;
        resCode = 0;
        resMsg = "na";
    }

    public ReadTaskPost(Context context, NetProcess process, List<NameValuePair> nameValuePairList, boolean showProgressDialog) {
        this.context = context;
        this.process = process;
        this.nameValuePairList = nameValuePairList;
        resCode = 0;
        resMsg = "na";
        this.showProgressDialog = showProgressDialog;
    }

    public ReadTaskPost(Context context, NetProcess process, List<NameValuePair> nameValuePairList, String msg) {
        this.context = context;
        this.process = process;
        this.progresssMsg = msg;
        this.nameValuePairList = nameValuePairList;
        resCode = 0;
        resMsg = "na";
    }


    /**
     * @param process           Object of NetProcess
     * @param nameValuePairList This is a List Of Parameters Passed To the URL
     *                          There is not ProgressDialog showing Please wait... is shown web data download completely done in background
     */

    public ReadTaskPost(NetProcess process, List<NameValuePair> nameValuePairList) {
        this(null, process, nameValuePairList);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (Connectivity.getNetworkState(context)) {
                connectionAvailable = true;
                if (null != context && showProgressDialog) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog = new ProgressDialog(context);
                            if (progresssMsg.equalsIgnoreCase("")) {
                                mDialog.setMessage("Please wait...");
                            } else {
                                mDialog.setMessage(progresssMsg);
                            }
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        Log.v("URL", params[0]);
        if (connectionAvailable) {
            if(params[0].contains("https")) {
                return getHTTPS_ResponseFromUrl(params[0]);
            }else{
                return getHTTP_ResponseFromUrl(params[0]);
            }
        } else {
            return "";
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.e("resultxxxx: ", result);
        if (connectionAvailable) {
            try {
                if (null != context && showProgressDialog) {
                    mDialog.dismiss();
                }
            } catch (Exception e) {
            }

            try {
                if (null != process) {
                    result = decodeResponse(result);
                    process.process(result, HTTP_RESULT);
                }
                if (error == true && Const.timeout >= 3) {
                    Logics.showAlertDialog(context, "Server not available.Please try after some time\n"+errorMsg);
                } else if (error == true && Const.timeout < 3) {
                    Logics.showAlertDialog(context, "Server not available.Please try after some time\n"+errorMsg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String decodeResponse(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject == null) {
                return result;
            }
            String encryptedData = jsonObject.getString("data");
            return new EncryptDecrypt(encryptedData).decodedString();
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    private String getHTTPS_ResponseFromUrl(String url) {
        String resultString = "";
        HttpResponse httpResponse = null;
        try {
            BufferedReader reader = null;
            StringBuilder sb;

            URL url1 = new URL(url);

            //TODO Comment for ssl
            //HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

            //TODO UnComment for ssl
            HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();
            connection.setSSLSocketFactory(socketFactory());
            //connection.setHostnameVerifier(new DummyHostnameVerifier());

            connection.setRequestMethod("POST");

            // enable writing output to this url
            connection.setDoOutput(true);

            connection.setConnectTimeout(15 * 60 * 1000);
            connection.setReadTimeout(15 * 60 * 1000);

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(getQuery(nameValuePairList));
            writer.flush();
            writer.close();


            resCode = connection.getResponseCode();
            resMsg = connection.getResponseMessage();


            InputStream in;
            if (resCode != HttpURLConnection.HTTP_OK)
                in = connection.getErrorStream();
            else
                in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            sb.append(resCode).append(" : ").append(resMsg);
            resultString = sb.toString();

            Log.v("URL_RESULT", resultString);

        } catch (UnsupportedEncodingException e) {
            this.HTTP_RESULT = HttpURLConnection.HTTP_BAD_GATEWAY;
            errorMsg = e.getMessage();
            e.printStackTrace();

        } catch (IOException e) {
            error = true;
            errorMsg = e.getMessage();
            Const.timeout++;
            if (httpResponse != null)
                HTTP_RESULT = httpResponse.getStatusLine().getStatusCode();
            e.printStackTrace();
        }

        return resultString;
    }

    private String getHTTP_ResponseFromUrl(String url) {
        String resultString = "";
        HttpResponse httpResponse = null;
        try {
            BufferedReader reader = null;
            StringBuilder sb;

            URL url1 = new URL(url);

            //TODO Comment for ssl
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

            //TODO UnComment for ssl
            /*HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();
            connection.setSSLSocketFactory(socketFactory());*/
            //connection.setHostnameVerifier(new DummyHostnameVerifier());

            connection.setRequestMethod("POST");

            // enable writing output to this url
            connection.setDoOutput(true);

            connection.setConnectTimeout(15 * 60 * 1000);
            connection.setReadTimeout(15 * 60 * 1000);

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(
                    connection.getOutputStream());
            writer.write(getQuery(nameValuePairList));
            writer.flush();
            writer.close();


            resCode = connection.getResponseCode();
            resMsg = connection.getResponseMessage();


            InputStream in;
            if (resCode != HttpURLConnection.HTTP_OK)
                in = connection.getErrorStream();
            else
                in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            sb.append(resCode).append(" : ").append(resMsg);
            resultString = sb.toString();

            Log.v("URL_RESULT", resultString);

        } catch (UnsupportedEncodingException e) {
            this.HTTP_RESULT = HttpURLConnection.HTTP_BAD_GATEWAY;
            e.printStackTrace();
        }  catch (IOException e) {
            error = true;
            Const.timeout++;
            if (httpResponse != null)
                HTTP_RESULT = httpResponse.getStatusLine().getStatusCode();
            e.printStackTrace();
        }

        return resultString;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (!pair.getName().equalsIgnoreCase(Const.imei) && !pair.getName().equalsIgnoreCase("ipAddress")) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                String encodedValue = new EncryptDecrypt(pair.getValue()).encodeString();
                result.append(URLEncoder.encode(encodedValue, "UTF-8"));
            }
        }
        String encodedValue = new EncryptDecrypt("172.16.11.190").encodeString();
        result.append("&").append("ipAddress").append("=").append(URLEncoder.encode(encodedValue, "UTF-8"));

        String imei = new EncryptDecrypt("").encodeString();
        result.append("&").append(Const.imei).append("=").append(URLEncoder.encode(imei, "UTF-8"));
        return result.toString();
    }


   /* public static class DummyTrustManager implements X509TrustManager {

        public DummyTrustManager() {
        }

        public boolean isClientTrusted(X509Certificate cert[]) {
            return true;
        }

        public boolean isServerTrusted(X509Certificate cert[]) {
            return true;
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

        }
    }
*/
//    public static class DummyHostnameVerifier implements HostnameVerifier {
//
//        public boolean verify(String urlHostname, String certHostname) {
//            return true;
//        }
//
//        public boolean verify(String arg0, SSLSession arg1) {
//            return true;
//        }
//    }

    private SSLSocketFactory socketFactory() {
        try {
            //region OLD CODE
            /*sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(new KeyManager[0],
                    new TrustManager[]{new DummyTrustManager()},
                    new SecureRandom());*/
            //endregion


            /*
            //region New Code
            if (sslcontext == null) {
                //CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
                CertificateFactory cf = CertificateFactory.getInstance("X.509");

                // Generate the certificate using the certificate file under res/raw/cert.cer
                InputStream caInput = new BufferedInputStream(context.getResources().openRawResource(R.raw.ventura_cert));
                Certificate ca = cf.generateCertificate(caInput);
                caInput.close();

                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                // Create a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // Create an SSLContext that uses our TrustManager
                sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, tmf.getTrustManagers(), null);
            }
            //endregion
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
            //out.write(logMsg);
            //out.close();
        } catch (KeyManagementException e) {
            e.printStackTrace(System.err);
            //out.write(logMsg);
            //out.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            //out.write(logMsg);
            //out.close();
        }*/


            if(sslcontext == null) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream caInput = context.getResources().openRawResource(R.raw.ventura);
                Certificate ca = cf.generateCertificate(caInput);
                caInput.close();

                KeyStore keyStore = KeyStore.getInstance("BKS");
                keyStore.load(null, null);

                keyStore.setCertificateEntry("ca", ca);

                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                //KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                //kmf.init(keyStore, "xxxxxxx".toCharArray());
                sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, tmf.getTrustManagers(), null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        SSLSocketFactory factory = sslcontext.getSocketFactory();
        return factory;
    }

}

