package com.application.vpp.NetworkCall;

import android.content.Context;
import android.util.Log;

import com.application.vpp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by anupamchugh on 05/01/17.
 */

public class APIClient {

    private static Retrofit retrofit = null;
    private static Retrofit retrofit2 = null;
    private static Retrofit retrofit3 = null;
    private static Retrofit retrofit4 = null;
    private static Retrofit retrofit5 = null;
    private static Retrofit retrofit6 = null;

    public static String BASE_URL1 = "https://vpp.ventura1.com/OrderDetail/";   // not using actually, we use method only for order not api.
//    public static String BASE_URL1 = "https://43.242.213.117/OrderDetoail/";   // not using actually, we use method only for order not api.
    public static String BASE_URL3 = "https://vpp.ventura1.com/videoupload/";
//    public static String BASE_URL3 = "https://43.242.213.117/videoupload/";
    public static String BASE_URL0 = "https://vpp.ventura1.com/FranchiseLogAPI/";

    public static String BASE_URL_OPS = "https://ocrapi.ventura1.com/";

    Gson gson = new GsonBuilder().setLenient().create();

    public static Retrofit getClient(Context context) {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        /*Interceptor interceptor1 = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request rr = chain.request();
                rr=rr.newBuilder();
              //  .header( "X-API-KEY","abcdefghijklmn")
               // .header("Authorization", "Basic c3VwZXJhZG1pbjpCb1NAUzEyMw==").build();
                return chain.proceed(rr);
            }
        };*/
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).addInterceptor(interceptor)
                .build();

        // SSLSocketFactory sslSocketFactory = SSLUtil.getSSLSocketFactory(SSLUtil.getFromRaw(context,"",""),"SSL") ;

        //client.sslSocketFactory(getSslContextForCertificateFile(context, "my_certificate.pem").socketFactory)



        retrofit = new Retrofit.Builder()
                //.baseUrl("https://reqres.in")
                .baseUrl("https://vpp.ventura1.com/")
                //.baseUrl("https://msrtcvtspis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient(context).build())

                // .client(sslSocketFactory)
                // .client(client.sslSocketFactory(getSslContextForCertificateFile(this, "my_certificate.pem").socketFactory))
                //   .client(client)
                .build();

        return retrofit;
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient(Context context) {

        try {

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(SSLUtil.getFromRaw(context));

            TrustManager[]  trustAllCerts = tmf.getTrustManagers();

            SSLContext context1 = SSLContext.getInstance("SSL");//"SSL" "TLS"
            context1.init(null, trustAllCerts, null);
            SSLSocketFactory sslSocketFactory = context1.getSocketFactory();//SSLUtil.getSSLSocketFactory(SSLUtil.getFromRaw(context,"",""),"SSL") ;
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(120, TimeUnit.MINUTES);
            builder.readTimeout(120, TimeUnit.MINUTES);
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {

                    Log.e( "verify: ", hostname);
                    if (hostname.equalsIgnoreCase("vpp.ventura1.com") ||hostname.equalsIgnoreCase("ocrapi.ventura1.com") ||
                            hostname.equalsIgnoreCase("172.16.102.55") || hostname.equalsIgnoreCase("43.242.213.117")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Retrofit getClient2() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).addInterceptor(interceptor)
                .build();

        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL1)
                    .addConverterFactory(GsonConverterFactory.create(gson))

                    // .client(getUnsafeOkHttpClient().build())
                    // .client(client)
                    .build();
        }
        return retrofit2;
    }
    public Retrofit getClient3(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).addInterceptor(interceptor)
                .build();
        if (retrofit3 == null) {
            retrofit3 = new Retrofit.Builder()
                    .baseUrl(BASE_URL3)   //Hostname 43.242.213.117 not verified:  if certification nt given .
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getUnsafeOkHttpClient(context).build())
                    // .client(client)
                    .build();
        }
        return retrofit3;
    }
    public Retrofit getClientOPS(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //
//        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                .connectTimeout(40, TimeUnit.SECONDS)
//                .readTimeout(40, TimeUnit.SECONDS)
//                .writeTimeout(40, TimeUnit.SECONDS)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Interceptor.Chain chain) throws IOException {
//                        Request original = chain.request();
//                        Request request = original.newBuilder()
//                                .addHeader("OCRAPIKey", "OPSID1:e5EPWugygD_Wsdfsfsdfsdsc2wWHoC1D")
////                                .header("Key", "Value")
//                                .method(original.method(), original.body())
//                                .build();
//
//                        return chain.proceed(request);
//                    }
//                })
//                .build();

        if (retrofit6 == null) {
            retrofit6 = new Retrofit.Builder()
                    .baseUrl(BASE_URL_OPS)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getUnsafeOkHttpClient(context).build())
//                     .client(okHttpClient)
                    .build();
        }
        return retrofit6;
    }
    public Retrofit getClientAllLogs(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).addInterceptor(interceptor)
                .build();
        if (retrofit4 == null) {
            retrofit4 = new Retrofit.Builder()
                    .baseUrl(BASE_URL0)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getUnsafeOkHttpClient(context).build())
                    .addConverterFactory(new NullOnEmptyConverterFactory())

                    // .client(client)
                    .build();
        }
        return retrofit4;
    }
    public Retrofit getClientsocketConn(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).addInterceptor(interceptor)
                .build();
        if (retrofit5 == null) {
            retrofit5 = new Retrofit.Builder()
                    .baseUrl(BASE_URL0)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getUnsafeOkHttpClient(context).build())
                    .addConverterFactory(new NullOnEmptyConverterFactory())

                    // .client(client)
                    .build();
        }
        return retrofit5;
    }


}
