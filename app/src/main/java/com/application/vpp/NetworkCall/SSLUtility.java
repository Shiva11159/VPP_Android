package com.application.vpp.NetworkCall;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class SSLUtility {
    public static SSLContext getSslContextForCertificateFile(Context context, String fileName){

        try {
            KeyStore keyStore = SSLUtility.getKeyStore(context, fileName);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null,trustManagerFactory.getTrustManagers(),new SecureRandom());
            return sslContext;

        }catch (Exception e){
            String msg = "Error during creating SslContext for certificate from assets";
            e.printStackTrace();
            throw new RuntimeException(msg);
        }
    }

    public static KeyStore getKeyStore(Context context, String fileName){
        KeyStore keyStore = null;
        try {
            AssetManager assetManager=context.getAssets();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput=assetManager.open(fileName);
            Certificate ca;
            try {
                ca=cf.generateCertificate(caInput);

            }finally {
                caInput.close();
            }
            String keyStoreType=KeyStore.getDefaultType();
            keyStore=KeyStore.getInstance(keyStoreType);
            keyStore.load(null,null);
            keyStore.setCertificateEntry("ca",ca);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (java.security.KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyStore;
    }
}

