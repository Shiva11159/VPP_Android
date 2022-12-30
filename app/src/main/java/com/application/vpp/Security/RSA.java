package com.application.vpp.Security;





import android.util.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;


import javax.crypto.Cipher;

/**
 * Created by bpandey on 14-09-2018.
 */

public class RSA {

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/NoPadding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.encodeToString(cipherText, Base64.NO_WRAP);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.decode(cipherText,Base64.NO_WRAP);
        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(decriptCipher.doFinal(bytes), "UTF-8");
    }

    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes("UTF-8"));
        byte[] signature = privateSignature.sign();
        return Base64.encodeToString(signature, Base64.NO_WRAP);
    }

//    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
//        Signature publicSignature = Signature.getInstance("SHA256withRSA");
//        publicSignature.initVerify(publicKey);
//        publicSignature.update(plainText.getBytes("UTF-8"));
//        byte[] signatureBytes = Base64.decode(signature,Base64.NO_WRAP);
//        return publicSignature.verify(signatureBytes);
//    }
}
