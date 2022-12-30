package com.application.vpp.Security;

import android.util.Base64;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by bpandey on 14-09-2018.
 */

public class AES {

    public static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    public static final String AES_ALGORITHM = "AES";
    public static final int ENC_BITS = 256;
    public static final String CHARACTER_ENCODING = "UTF-8";

    private static Cipher ENCRYPT_CIPHER;
    private static Cipher DECRYPT_CIPHER;
    private static KeyGenerator KEYGEN;

    static{
        try{
            ENCRYPT_CIPHER = Cipher.getInstance(AES_TRANSFORMATION);
            DECRYPT_CIPHER = Cipher.getInstance(AES_TRANSFORMATION);
            KEYGEN = KeyGenerator.getInstance(AES_ALGORITHM);
            KEYGEN.init(ENC_BITS);
        }catch(NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }


    public static String EncryptWithKey(byte[] plainText, byte[] secret)
    {
        try
        {


            SecretKeySpec sk = new SecretKeySpec(secret, AES_ALGORITHM);
            ENCRYPT_CIPHER.init(Cipher.ENCRYPT_MODE, sk);
            byte[] encrytedData = ENCRYPT_CIPHER.doFinal(plainText);
            String finaldata = Base64.encodeToString(encrytedData,Base64.NO_WRAP);
            return finaldata;

        }
        catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }


    public static byte[] DecryptWithKey(String plainText, byte[] secret)
            throws IOException, IllegalBlockSizeException,
            BadPaddingException,Exception {




        SecretKeySpec sk = new SecretKeySpec(secret, AES_ALGORITHM);
        DECRYPT_CIPHER.init(Cipher.DECRYPT_MODE, sk);
        byte[] output = Base64.decode(plainText,Base64.NO_WRAP);
        return DECRYPT_CIPHER.doFinal(output);


    }




    public static byte[] generateSecureKey() throws Exception{
        SecretKey secretKey = KEYGEN.generateKey();
        return secretKey.getEncoded();
    }



}
