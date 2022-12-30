package com.application.vpp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.application.vpp.Datasets.Notification_data;
import com.application.vpp.Datasets.ProductMasterDataset;
import com.application.vpp.Interfaces.SliderImagesPojo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bpandey on 13-06-2018.
 */

 public class DatabaseHelper extends SQLiteOpenHelper {



    public static final String DATABASE_NAME = "DB_VPP";
     public static final String TABLE_PRODUCT_MASTER = "PRODUCT_MASTER";
     public static final int DATABASE_VERSION = 1;
     public static final String COL_PRO_ID = "PROD_ID";
    public static final String COL_PRO_NAME = "PROD_NAME";


    private static final String TABLE_NOTIFICATION = "tbl_notification";
    private static final String SR_NO = "SR_no";
    private static final String COL_TITLE = "title";
    private static final String COL_MESSAGE = "message";
    private static final String COL_IMGURL = "img_url";


  private static final String TABLE_IMAGR = "tbl_image";
    private static final String KEY_ID = "KEY_ID";
    private static final String KEY_IMAGE = "KEY_IMAGE";

    private static final String TABLE_SLIDER = "tbl_SliderImage";

    public static final String SLIDER_ID = "SLIDER_ID";
    public static final String SLIDER_URL = "SLIDER_URL";    //09

    String CREATE_SLIDER = "CREATE TABLE " + TABLE_SLIDER + "(" +
            SLIDER_ID + " INTEGER PRIMARY KEY," +
            SLIDER_URL + " TEXT" + ")";


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 5);


     }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

         sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_PRODUCT_MASTER + " ( "+ COL_PRO_ID + " INTEGER PRIMARY KEY, " + COL_PRO_NAME + " VARCHAR(45)"+")");
        sqLiteDatabase.execSQL(CREATE_SLIDER);
//        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS "+TABLE_NOTIFICATION +"("+SR_NO+" INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE + " VARCHAR(80),"+COL_MESSAGE +  " VARCHAR(1000)"+")");
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS "+TABLE_NOTIFICATION +"("+SR_NO+" INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE + " VARCHAR(80),"+ COL_MESSAGE + " VARCHAR(1000),"+COL_IMGURL +  " VARCHAR(1000)"+")");
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_IMAGR + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_IMAGE + " BLOB" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {
//        Log.e( "DBonUpgrade: ", "called");

         if (oldversion<newversion){
             Log.e( "DBonUpgrade: ", "called");
             Log.e( "DBonUpgradeoldversion: ", String.valueOf(oldversion));
             Log.e( "DBonUpgradenewversion: ", String.valueOf(newversion));
            // sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+TABLE_IMAGR);
             String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_IMAGR + "("
                     + KEY_ID + " INTEGER PRIMARY KEY,"
                     + KEY_IMAGE + " BLOB" + ")";
             sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
         }

       // sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+TABLE_PRODUCT_MASTER);

         //   onCreate(sqLiteDatabase);

    }


    public void insertProductMaster(ArrayList<ProductMasterDataset> productMasterDatasetArrayList){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        db.execSQL("DELETE FROM PRODUCT_MASTER"); //delete all rows in a table

        for (int i = 0; i < productMasterDatasetArrayList.size(); i++) {
            int productId = productMasterDatasetArrayList.get(i).Pcode;
            String productName = productMasterDatasetArrayList.get(i).PName;
            Log.d("productCode", "insertProductMaster: "+productId);

            contentValues.put(COL_PRO_ID,productId);
            contentValues.put(COL_PRO_NAME,productName);

            long result = db.insertWithOnConflict(TABLE_PRODUCT_MASTER,COL_PRO_ID,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public int getCount(){
        String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void insertNotificaton(String title, String message, String img_url){
        SQLiteDatabase db = getWritableDatabase();
        int count = getCount(); // check for number of records
        if(count == 10) {
            // Delete from table_name where rowid IN (Select rowid from table_name limit X);
            //db.execSQL("DELETE FROM  " + TABLE_NOTIFICATION+ "   WHERE SR_no IN(SELECT SR_no FROM  ORDER BY id ASC LIMIT 100)  );

            db.execSQL(" DELETE FROM tbl_notification WHERE SR_no IN (SELECT SR_no FROM tbl_notification ORDER BY SR_no ASC LIMIT 1)");
            // db.execSQL("DELETE FROM " + TABLE_NOTIFICATION+ " ORDER BY SR_no LIMIT 1 "); // will delete oldest record
        } // add till here

        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE,title);
        cv.put(COL_MESSAGE,message);

        if(img_url == null || img_url.equals(null)) {
            cv.put(COL_IMGURL, "");
        }else {
            cv.put(COL_IMGURL, img_url);
        }
        db.insert(TABLE_NOTIFICATION,null,cv);
//        db.delete(TABLE_NOTIFICATION,
//                "ROWID NOT IN (SELECT ROWID FROM " + TABLE_NOTIFICATION + " ORDER BY ROWID DESC LIMIT 10)",
//                null);
        db.close();
    }

    public ArrayList<Notification_data> getNotification(){
        ArrayList<Notification_data> arrayList = new ArrayList<>();
        Cursor cr;
        SQLiteDatabase db = getReadableDatabase();

        String query = "Select * from "+TABLE_NOTIFICATION +" ORDER BY SR_no DESC LIMIT 10";
        //   String query = "Select * from tbl_notification Limit 10 ";//
        Cursor cursor = db.rawQuery(query,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    String title = cursor.getString(1);
                    String message = cursor.getString(2);
                    String imgurl = cursor.getString(3);

//                    ContactData contactData = new ContactData(name,number,email);
                    Notification_data notifiaction_data=new Notification_data(title,message, imgurl);
                    arrayList.add(notifiaction_data);

                }while (cursor.moveToNext());
            }
        }
        cursor.close();


        return arrayList;


    }

    public ArrayList<String> getProductMaster(){

      ArrayList<String>arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "select * from "+TABLE_PRODUCT_MASTER;

        Cursor cursor = db.rawQuery(query,null);
        if(cursor!=null){
            if (cursor.moveToFirst()){
               // arrayList.add("Select Product");
                do{

                    String productName = cursor.getString(1);
                    arrayList.add(productName);

                }   while (cursor.moveToNext());

            }



        }

        return arrayList;
    }

    public int getProductId(String prod){

       int productId = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = "select PROD_ID from "+TABLE_PRODUCT_MASTER + " WHERE "+ COL_PRO_NAME + " = '"+prod +"'";

        Cursor cursor = db.rawQuery(query,null);
        if(cursor!=null){
            if (cursor.moveToFirst()){

                productId = cursor.getInt(0);
                Log.d("productId", "getProductId: "+productId);
            }



        }

        return productId;
    }

    public void addImage(byte _image[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, _image); // Contact Phone
// Inserting Row
        db.insert(TABLE_IMAGR, null, values);
        db.close(); // Closing database connection
    }

    public Bitmap getimage() {
        Bitmap bmp = null;
        String selectQuery = "SELECT * FROM tbl_image";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToNext())
        {
            byte[] image = c.getBlob(1);
             bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
        }
        return bmp;
    }
    public Integer getid() {
         int i=0;
        String selectQuery = "SELECT * FROM tbl_image";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToNext())
        {
            i = c.getInt(0);
        }
        return i;
    }

    public void deleteContact(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGR, KEY_ID + " = ?",
                new String[] { id });
        db.close();
    }
    public void InsertSliderImages(ArrayList<SliderImagesPojo>arrayList){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < arrayList.size(); i++) {
            String SliderLink = arrayList.get(i).getLink();
            contentValues.put(SLIDER_URL,SliderLink);
            long result = db.insertWithOnConflict(TABLE_SLIDER,SLIDER_ID,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

//    public boolean InsertSliderImages(String SLIDER_URL) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(SLIDER_URL, SLIDER_URL);
//        db.insert(TABLE_SLIDER, null, values);
//        return true;
//    }
    public void delete() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting rows
        sqLiteDatabase.delete(TABLE_SLIDER, null, null);
        sqLiteDatabase.close();
    }
    public ArrayList<SliderImagesPojo> getSliderImages() {
        ArrayList<SliderImagesPojo> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tbl_SliderImage", null);
        if (cursor.moveToFirst()) {
            do {
                SliderImagesPojo cartModel = new SliderImagesPojo(cursor.getString(1));
                arrayList.add(cartModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact
        return arrayList;

//        String stringQuery = "Select Image from ImageTable where Name=\"MyImage\"";
//        Cursor cursor = db.rawQuery(stringQuery, null);
//        try {
//            cursor.moveToFirst();
//            byte[] bytesImage = cursor.getBlob(0);
//            cursor.close();
//            Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length);
//            // imageView.setImageBitmap(bitmapImage);
//
    }
}
