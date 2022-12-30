package com.application.vpp.AES;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by NEHA G on 17-04-2017.
 */
public class EncryptDecrypt {

    private byte[] keyValue = new byte[]{'X','T','R','E','M','S','O','F','T','T','E','C','H','N','O','L'};
    private String algorithm = "AES";

    private String data;

    public EncryptDecrypt(String data){
        this.data = data;
    }

    public String encodeString() {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey());
            //byte[] byteArr = Base64.decode(data.getBytes(), Base64.DEFAULT);
            String encryptedString = Base64.encodeToString(cipher.doFinal(data.getBytes()), Base64.DEFAULT);
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decodedString() {
        try {
            byte[] byteArr = Base64.decode(data.getBytes(),Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, generateKey());
            return new String(cipher.doFinal(byteArr));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, algorithm);
        return key;
    }

}
