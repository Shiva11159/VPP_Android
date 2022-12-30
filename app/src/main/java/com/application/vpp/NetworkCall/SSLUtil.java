package com.application.vpp.NetworkCall;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import androidx.annotation.RawRes;

import com.application.vpp.R;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLUtil {
    public static KeyStore getFromRaw(Context context, String algorithm, String filePassword)
    {
        try
        {
            //keystorePass="vensec@123" keystoreType="PKCS12"
            InputStream inputStream =  context.getResources().openRawResource(R.raw.ventura2023);
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(inputStream, "vensec@123".toCharArray());
            inputStream.close();

            return keystore;
        }
        catch(Exception e)
        {}

        return null;
    }

    public static SSLSocketFactory getSSLSocketFactory(KeyStore trustKey, String SSLAlgorithm)
    {
        try
        {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustKey);

            SSLContext context = SSLContext.getInstance(SSLAlgorithm);//"SSL" "TLS"
            context.init(null, tmf.getTrustManagers(), null);

            return context.getSocketFactory();
        }
        catch(Exception e){}

        return null;
    }
}



