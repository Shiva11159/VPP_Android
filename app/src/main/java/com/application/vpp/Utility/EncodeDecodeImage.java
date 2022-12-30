package com.application.vpp.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by XPC17 on 28-06-2016.
 */
public class EncodeDecodeImage {

    private byte[] keyValue = new byte[]{'X','T','R','E','M','S','O','F','T','T','E','C','H','N','O','L'};
    private String algorithm = "AES";

    private String base64String;
    private byte[] imgByteArr;

    public EncodeDecodeImage(ImageView imageView) {
        this.imgByteArr = getImgByteArr(imageView);
    }

    public EncodeDecodeImage(Bitmap imageBitmap) {
        this.imgByteArr = getImgByteArr(imageBitmap);
    }

    public EncodeDecodeImage(byte[] _imageByteArr) {
        this.imgByteArr = _imageByteArr;
    }

    public EncodeDecodeImage(String base64String){
        this.base64String = base64String;
    }

    private byte[] getImgByteArr(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return byteArray;
    }

    private byte[] getImgByteArr(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return byteArray;
    }

    public String getBase64StringofImage(){
        String base64String = Base64.encodeToString(imgByteArr, Base64.DEFAULT);
        return base64String;
    }

    public String encodeFile() {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey());
            String UnEncodedImgString = Base64.encodeToString(imgByteArr, Base64.DEFAULT);
            String encryptedString = Base64.encodeToString(cipher.doFinal(imgByteArr), Base64.DEFAULT);
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encodeBase64String() {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey());
            byte[] byteArr = Base64.decode(base64String, Base64.DEFAULT);
            String encryptedString = Base64.encodeToString(cipher.doFinal(byteArr), Base64.DEFAULT);
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encodeString() {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey());
            byte[] byteArr = base64String.getBytes();
            String encryptedString = Base64.encodeToString(cipher.doFinal(byteArr), Base64.DEFAULT);
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encodeString(int encodeType) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey());
            byte[] byteArr = base64String.getBytes("UTF-8");
            String encryptedString = Base64.encodeToString(cipher.doFinal(byteArr),encodeType);
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decodeFile() {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, generateKey());
            byte[] byteArr = Base64.decode(base64String, Base64.DEFAULT);

            return cipher.doFinal(byteArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getBitMapFromBase64String(){
        byte[] byteArr = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length);
        return bitmap;
    }

    private Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, algorithm);
        return key;
    }

    public static String base64ToString(String base64String){
        byte[] byteArr = Base64.decode(base64String, Base64.DEFAULT);
        String str = new String(byteArr);
        return str;
    }

}
