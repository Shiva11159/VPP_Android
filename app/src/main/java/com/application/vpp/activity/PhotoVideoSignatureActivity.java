package com.application.vpp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.Upload;
import com.application.vpp.Datasets.UploadFileResponse;
import com.application.vpp.Interfaces.APiValidateAccount;
import com.application.vpp.Interfaces.Uploadfilecallback;
import com.application.vpp.Listener.UploadCallbacks;
import com.application.vpp.NetworkCall.APIClient;
import com.application.vpp.NetworkCall.ProgressRequestBody;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.ImagePickerActivity;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.Utility.AlertDialogClass;
import com.application.vpp.Utility.CameraPermission;
import com.application.vpp.videorecorder.Util;
import com.application.vpp.videorecorder.ui.VideoRecordingActivity;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoVideoSignatureActivity extends AppCompatActivity implements UploadCallbacks//implements Uploadfilecallback
{
    ImageView UploadImage, UploadVideo, UploadSignature;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    String userChoosenTask;
    File fileselfie;
    ScrollView scrollview;
    Uploadfilecallback uploadfilecallback;
    //    ProgressDialog progressDialog;
    private Handler handler;
    int totalSize = 0;
    LinearLayout linear_checkbox;
    //// for video capture..
//    private File fileUri_Video;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 333;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE11 = 111; //this is for android 11.
    public TextView textviewPhoto_Uploaded;
    public TextView textviewVideo_Uploaded;
    public TextView textviewSignature_Uploaded;
    public Button buttonPhoto_Upload;
    public Button buttonVideo_Upload;
    public Button buttonContinue;
    public Button btnPlay;
    public Button btnUpload;
    public LinearLayout linearlayoutPlay;
    RelativeLayout mainlayout;
    //    private Uri fileUri; // file url to store image/video
//    File fileUri_Video;
    // for signature
    private DrawingView dv;
    private Paint mPaint;
    LinearLayout lay;
    private Bitmap bmp;
    private File file;
    private Uri fileUri; // file url to store image/video
    private String selectedPath;
    ImageView imageview;
    public static final String IMAGE_DIRECTORY_NAME = "AndroidFileUpload";
    private VideoView vidPreview;
    private ImageView imgPreview;
    //    androidx.appcompat.app.AlertDialog alertDialog;
    GestureDetector mGestureDetector;
    CardView cardviewSignature, cardviewVideo;
    CheckBox TermsNCond_CheckBx;
    TextView TermsNCond_Textview;
    APiValidateAccount apiService;
    public static String uploadSignature = "Signature";
    public static String uploadSelfie = "Selfie";
    public static String uploadVideo = "Video";
    private int BUFFER_SIZE = 6 * 1024;
    int videoUploadCount = 0;
    ProgressDialog progressDialog;


    //    public static String uploadSignature = "Signature";
//    public static String uploadSelfie = "Selfie";
//    public static String uploadVideo = "Video";
    TextView tttt;
    public static boolean up_selfie = false;
    //public static boolean up_video = false;
    public static boolean up_signature = false;
    public static String signature_status = "0";
    public static String l_s_terms_condition_y_n = "Y";
    private static final int SELECT_VIDEO = 3;
    FancyButton btn_upload_document;

    String UrlUploadVideo = "https://vpp.ventura1.com/videoupload/uploadFile";
    String reponse_data;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    String mVideoPath = "";

    String err = "";
    UploadCallbacks uploadCallbacks;
    Call<UploadFileResponse> creatvideo;
    Call<UploadFileResponse> createPhoto;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_video_signature);
        //image
        tttt = findViewById(R.id.tttt);
        linear_checkbox = findViewById(R.id.linear_checkbox);
        scrollview = findViewById(R.id.scrollview);
        TermsNCond_Textview = findViewById(R.id.TermsNCond_Textview);
        TermsNCond_CheckBx = findViewById(R.id.TermsNCond_CheckBx);
        cardviewSignature = findViewById(R.id.cardviewSignature);
        cardviewVideo = findViewById(R.id.cardviewVideo);
        UploadImage = findViewById(R.id.UploadImage);
        UploadSignature = findViewById(R.id.UploadSignature);
        buttonPhoto_Upload = findViewById(R.id.buttonPhoto_Upload);
        textviewPhoto_Uploaded = findViewById(R.id.textviewPhoto_Uploaded);
        buttonContinue = findViewById(R.id.buttonContinue);
        btn_upload_document = findViewById(R.id.btn_upload_document);
        linearlayoutPlay = findViewById(R.id.linearlayoutPlay);
        btnUpload = findViewById(R.id.btnUpload);
        btnPlay = findViewById(R.id.btnPlay);
        mainlayout = findViewById(R.id.mainlayout);

        progressDialog = new ProgressDialog(this);
        uploadCallbacks = (UploadCallbacks) this;


        if (Connectivity.getNetworkState(getApplicationContext())) {
            // fetchESIGN();
        } else {
            TastyToast.makeText(PhotoVideoSignatureActivity.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);

        }

        //video
        UploadVideo = findViewById(R.id.UploadVideo);
        textviewVideo_Uploaded = findViewById(R.id.textviewVideo_Uploaded);
        textviewSignature_Uploaded = findViewById(R.id.textviewSignature_Uploaded);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);

        ImagePickerActivity.clearCache(this);
        //uploadfilecallback = (Uploadfilecallback) this;

        mGestureDetector = new GestureDetector(PhotoVideoSignatureActivity.this, mGestureListener);

//        if (getIntent().getStringExtra(Const.selfiestatus)!=null) {
//            if (getIntent().getStringExtra(Const.selfiestatus).equalsIgnoreCase("2") || getIntent().getStringExtra(Const.videostatus).equalsIgnoreCase("0")) {
//                cardviewselfie.setVisibility(View.VISIBLE);
//                cardviewVideo.setVisibility(View.GONE);
//                TermsNCond_CheckBx.setVisibility(View.GONE);
//                TermsNCond_Textview.setVisibility(View.GONE);
//
//            }
//
//        }
//
//        if (getIntent().getStringExtra(Const.videostatus)!=null) {
//            if (getIntent().getStringExtra(Const.videostatus).equalsIgnoreCase("2") || getIntent().getStringExtra(Const.videostatus).equalsIgnoreCase("0")) {
//                cardviewVideo.setVisibility(View.VISIBLE);
//                cardviewselfie.setVisibility(View.GONE);
//                TermsNCond_CheckBx.setVisibility(View.GONE);
//                TermsNCond_Textview.setVisibility(View.GONE);
//
//            }
//
//        }

        tttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chooseVideo();
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
        requestAudioPermissions();///audio request...
        //video

        TermsNCond_CheckBx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TermsNCond_CheckBx.isChecked()) {
//                   PopUpSignature();
                    alert();
                    //startActivity(new Intent(PhotoVideoSignatureActivity.this, WebActivity4.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("link", "https://vpp.ventura1.com/VPP/index.html"));
                }
            }
        });

        TermsNCond_Textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TermsNCond_CheckBx.setChecked(true);
                alert();

//                String url = "https://vpp.ventura1.com/VPP/index.html";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
                //  startActivity(new Intent(PhotoVideoSignatureActivity.this, WebActivity4.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("link", "https://vpp.ventura1.com/VPP/index.html"));

            }
        });

        /// upload signature..
//        UploadSignature.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopUpSignature();
//            }
//        });

        // for singature

        // this is for video capture
        UploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.getNetworkState(getApplicationContext())) {
                    // fetchESIGN();
                    Dexter.withActivity(PhotoVideoSignatureActivity.this)
                            .withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {

                                    //temp committed..

//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                                            if (Environment.isExternalStorageManager()) {
//                                                Intent intent = new Intent(getApplicationContext(), VideoRecordingActivity.class);
//                                                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);// Activity is started with requestCode 333
//                                                //Log.e("run: ", "permissionchecked");
//                                            }else {
//                                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                                                intent.setData(uri);
//                                                startActivity(intent);
//                                                //Log.e("run: ", "settings");
//                                            }
//
//                                        }
//
//                                    }else {
//                                        Intent intent = new Intent(getApplicationContext(), VideoRecordingActivity.class);
//                                        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);// Activity is started with requestCode 333
//                                        Log.e("run: ", "permissionchecked");
//                                    }

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                        Intent intent = new Intent(getApplicationContext(), VideoRecordingActivity.class);
                                        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);// Activity is started with requestCode 333


                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), VideoRecordingActivity.class);
                                        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);// Activity is started with requestCode 333
//                                    Log.e("run: ", "permissnew Fileionchecked");
                                    }


                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                    Log.e("permissionNot: ", "permissionNotchecked");
                                }
                            }).check();


//                    startActivity(intent);
                } else {
                    TastyToast.makeText(PhotoVideoSignatureActivity.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                }
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
//                if (Connectivity.getNetworkState(getApplicationContext())) {
//                   // fetchESIGN();
//
//                    Intent intent = new Intent(getApplicationContext(), VideoRecordingActivity.class);
//                    startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);// Activity is started with requestCode 333
//
////                    startActivity(intent);
//                } else {
//                    TastyToast.makeText(PhotoVideoSignatureActivity.this, "No Internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//
//                }
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressDialog = ProgressDialog.show(PhotoVideoSignatureActivity.this, "Please wait ...", "Uploading Video ..", true);
//                progressDialog.setCancelable(true);
//                progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

                //setup params
//                new Task_finder().execute();

                // call_method();   //0 for video

//                uploadVideo();
//                uploadVideoToServer(getVideoFilePath());

//                new UploadFile(getApplicationContext(), uploadfilecallback, struploadVideo).execute(convertToBase64(), struploadVideo, "11159");
            }
        });

        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(PhotoVideoSignatureActivity.this)
                        .withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                //PopUpChooseCamORgallery(UploadImage);
                                Intent intent = new Intent(PhotoVideoSignatureActivity.this, ImagePickerActivity.class);
                                intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
                                // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
                                // setting maximum bitmap width and height
                                intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
                                startActivityForResult(intent, REQUEST_CAMERA);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        if (getIntent().getExtras() != null) {
            // Toast.makeText(this,"ssssss",Toast.LENGTH_SHORT).show();
            if (getIntent().getBooleanExtra("from_EsignerWebView", false) == true) {
                //     Toast.makeText(this,"ssssss11",Toast.LENGTH_SHORT).show();

                //selfie ke liye
                textviewPhoto_Uploaded.setVisibility(View.VISIBLE);
                textviewPhoto_Uploaded.setText("Selfie uploaded successfully.");
                textviewPhoto_Uploaded.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_, 0);

                up_selfie = true;

                String uri = SharedPref.getPreferences(PhotoVideoSignatureActivity.this, "uri");
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(uri));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("urierror", String.valueOf(e.getCause()));
                }
//            loadProfile(bitmap, struploadProfile, UploadImage);
                /// set image
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
                byte[] b = baos.toByteArray();
                UploadImage.setImageBitmap(bitmap);
                UploadImage.setScaleType(ImageView.ScaleType.FIT_XY);

                //video ke liye


//                commented for video from here shiva
               /* textviewVideo_Uploaded.setVisibility(View.VISIBLE);
                textviewVideo_Uploaded.setText("Video uploaded successfully.");
                textviewVideo_Uploaded.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_, 0);

                linearlayoutPlay.setVisibility(View.GONE);
                up_video = true;

                String mVideoPath = SharedPref.getPreferences(PhotoVideoSignatureActivity.this, "mVideoPath");

                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mVideoPath, MediaStore.Video.Thumbnails.MINI_KIND);
                Log.e("thumb", String.valueOf(thumb));
                if (thumb == null) {
                    UploadImage.setVisibility(View.INVISIBLE);

                } else {
                    // videoFilePreview.setVisibility(thumb);
                    UploadVideo.setImageBitmap(thumb);
//                    linearlayoutPlay.setVisibility(View.VISIBLE);
                    UploadVideo.setScaleType(ImageView.ScaleType.FIT_XY);
                }
*/
                //                commented for video till here shivatumma


//                checkbox ke liye ...

                TermsNCond_CheckBx.setVisibility(View.GONE);
                TermsNCond_Textview.setVisibility(View.GONE);

                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(View.FOCUS_DOWN);
                    }
                });

                up_signature = true;

                Uploadstatus11(uploadSignature, "1");
            }

        }

        btn_upload_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhotoVideoSignatureActivity.this, Dashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    protected void playVideo() {
        if (new File(mVideoPath).exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            android.net.Uri data = android.net.Uri.parse(mVideoPath);
            intent.setDataAndType(data, "video/webm");
            startActivity(intent);
        }
    }

    @SuppressLint("RestrictedApi")
    public void PopUpChooseCamORgallery(ImageView imageView) {
        PopupMenu popup = new PopupMenu(PhotoVideoSignatureActivity.this, imageView);
        try {
            // Reflection apis to enforce show icon
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(POPUP_CONSTANT)) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(POPUP_FORCE_SHOW_ICON, boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
//        popup.setOnMenuItemClickListener(this);
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cameraclick:
                        cameraIntent();
                        break;

                    case R.id.galleryclick:
                        galleryIntent();
                        break;
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }

    private void galleryIntent() {
        Intent intent = new Intent(PhotoVideoSignatureActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        startActivityForResult(intent, SELECT_FILE);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = new Intent(PhotoVideoSignatureActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode != RESULT_CANCELED) {

                Log.e("requestCode", String.valueOf(requestCode));
                Log.e("resultCode", String.valueOf(resultCode));
                Log.e("mVideoPath", data.getStringExtra("mVideoPath"));

                //  mVideoPath = data.getStringExtra("mVideoPath");
                mVideoPath = (data.getStringExtra("mVideoPath") != null) ? mVideoPath = data.getStringExtra("mVideoPath") : "";

                SharedPref.savePreferences(PhotoVideoSignatureActivity.this, "mVideoPath", mVideoPath);
                file = new File(mVideoPath);

                Log.e("SIZE000: ", Util.getFileSize(getBaseContext(), file.length()));

                Log.e("mVideoPath", mVideoPath);

                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mVideoPath, MediaStore.Video.Thumbnails.MINI_KIND);
                Log.e("thumb", String.valueOf(thumb));
//            if (thumb == null) {
//                UploadImage.setVisibility(View.INVISIBLE);
//
//            } else {
                // videoFilePreview.setVisibility(thumb);
                //  UploadVideo.setImageBitmap(thumb);
                linearlayoutPlay.setVisibility(View.VISIBLE);
                UploadVideo.setScaleType(ImageView.ScaleType.FIT_XY);
                //}
            }


        }


//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_FILE)
//                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
//            else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
//
//                if (resultCode == RESULT_OK) {
//
//                    output.setText("Video File : " +data.getData());
//
//                    // Video captured and saved to fileUri specified in the Intent
//                    Toast.makeText(getApplicationContext(), "Video saved to: " +
//                            data.getData(), Toast.LENGTH_LONG).show();
//
//                } else if (resultCode == RESULT_CANCELED) {
//
//                    output.setText("User cancelled the video capture.");
//
//                    // User cancelled the video capture
//                    Toast.makeText(this, "User cancelled the video capture.",
//                            Toast.LENGTH_LONG).show();
//
//                } else {
//
//                    output.setText("Video capture failed.");
//
//                    // Video capture failed, advise user
//                    Toast.makeText(this, "Video capture failed.",
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//
//        }

//        if (requestCode == SELECT_VIDEO) {
//            System.out.println("SELECT_VIDEO");
//            Uri selectedImageUri = data.getData();
//            selectedPath = getPath(selectedImageUri);
//            Log.e("selectedPath", selectedPath);
//            linearlayoutPlay.setVisibility(View.VISIBLE);
////            UploadVideo.setImageBitmap(thumb);
//
////            textView.setText(selectedPath);
//        }


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);


//            else
//                if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
//                if (resultCode == RESULT_OK) {
//                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(getVideoFilePath(), MediaStore.Video.Thumbnails.MINI_KIND);
//                    Log.e("thumb", String.valueOf(thumb));
//                    if (thumb == null) {
//                        UploadImage.setVisibility(View.INVISIBLE);
//
//                    } else {
//                        // videoFilePreview.setVisibility(thumb);
//                        UploadImage.setImageBitmap(thumb);
//
//                        progressDialog = ProgressDialog.show(PhotoVideoSignatureActivity.this, "Please wait ...", "Uploading Video ..", true);
//                        progressDialog.setCancelable(true);
//                        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//
//                        new UploadFile(getApplicationContext(), uploadfilecallback, struploadVideo).execute(convertToBase64(), struploadVideo, "11159");
//
//                    }
//
//
//                } else if (resultCode == RESULT_CANCELED) {
//                    textviewVideo_Uploaded.setVisibility(View.VISIBLE);
//                    textviewVideo_Uploaded.setText("User cancelled the video capture.");
//
//                    // User cancelled the video capture
//                    Toast.makeText(this, "User cancelled the video capture.",
//                            Toast.LENGTH_LONG).show();
//
//                } else {
//                    textviewVideo_Uploaded.setVisibility(View.VISIBLE);
//                    textviewVideo_Uploaded.setText("Video capture failed.");
//
//                    // Video capture failed, advise user
//                    Toast.makeText(this, "Video capture failed.",
//                            Toast.LENGTH_LONG).show();
//                }
//            }

        }
    }

    private void onCaptureImageResult(Intent data) {
        Uri uri = data.getParcelableExtra("path");

        SharedPref.savePreferences(PhotoVideoSignatureActivity.this, "uri", String.valueOf(uri));
        try {
            // You can update this bitmap to your server
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            // loading profile image from local cache

//            loadProfile(bitmap, struploadProfile, UploadImage);

            fileselfie = new File(uri.getPath());
            call_methodSelfie(fileselfie);

            /// set image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
            byte[] b = baos.toByteArray();
            UploadImage.setImageBitmap(bitmap);
            UploadImage.setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
//        Bitmap bm = null;
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        Uri uri = data.getParcelableExtra("path");
        SharedPref.savePreferences(PhotoVideoSignatureActivity.this, "uri", String.valueOf(uri));


        try {

            File file = new File(uri.getPath());

            call_methodSelfie(file);
            // You can update this bitmap to your server
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//            loadProfile(bitmap, struploadProfile, UploadImage);


            /// set image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
            byte[] b = baos.toByteArray();
            UploadImage.setImageBitmap(bitmap);
            UploadImage.setScaleType(ImageView.ScaleType.FIT_XY);


        } catch (Exception e) {
            Log.e("GalleryRe", e.getMessage());
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CameraPermission.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
//                    recordAudio();
                    Toast.makeText(this, "Permissions Granted to record audio", Toast.LENGTH_LONG).show();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    //// this is video


    /**
     * Create a file Uri for saving an image or video
     */
//        private static Uri getOutputMediaFileUri ( int type){
//
//            return Uri.fromFile(getOutputMediaFile(type));
//        }

    /**
     * Create a File for saving an image or video
     */
//        private static File getOutputMediaFile ( int type){
//
//            // Check that the SDCard is mounted
//            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES), "MyCameraVideo");
//
//
//            // Create the storage directory(MyCameraVideo) if it does not exist
//            if (!mediaStorageDir.exists()) {
//
//                if (!mediaStorageDir.mkdirs()) {
//
//                    output.setText("Failed to create directory MyCameraVideo.");
//
////                Toast.makeText(PhotoVideoSignatureActivity.this, "Failed to create directory MyCameraVideo.",
////                        Toast.LENGTH_LONG).show();
//
//                    Log.e("MyCameraVideo", "Failed to create directory MyCameraVideo.");
//                    return null;
//                }
//            }
//
//
//            // Create a media file name
//
//            // For unique file name appending current timeStamp with file name
//            java.util.Date date = new java.util.Date();
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//                    .format(date.getTime());
//
//            File mediaFile;
//
//            if (type == MEDIA_TYPE_VIDEO) {
//
//                // For unique video file name appending current timeStamp with file name
//                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                        "VID_" + timeStamp + ".mp4");
//
//            } else {
//                return null;
//            }
//
//            return mediaFile;
//        }

    /**
     * Create a File for saving an image
     */
    private File getOutputMediaFile1(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Vpp_Video");

        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private File getOutputMediaFileSignature(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyApplication");

        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_Singature" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "onError", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        progressDialog.setProgress(100);
    }

//    @Override
//    public boolean handleMessage(Message msg) {
//        Log.i("File Upload", "Response :: " + msg.obj);
//        String success = 1 == msg.arg1 ? "File Upload Success" : "File Upload Error";
//        Log.e("okok", success);
//        buttonVideo_Upload.setText(success);
//        buttonVideo_Upload.setVisibility(View.VISIBLE);
//        return false;
//    }

//    / for signature..

    public class DrawingView extends View {

        public int width;
        public int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        public DrawingView(Context c) {
            super(c);
            context = c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLACK);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(2f);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
            canvas.drawPath(circlePath, circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }


    private void captureScreen() {
        View v = lay;
        v.setDrawingCacheEnabled(true);
        bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        try {

            file = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//            file = new File(file.getPath() + File.separator+ "SCREEN"
//                    + System.currentTimeMillis() + ".png");
////
//            file = new File(Environment
//                    .getExternalStorageDirectory().toString(), "SCREEN"
//                    + System.currentTimeMillis() + ".png");


            Log.e("here", "------------" + file);
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                cardviewSignature.setVisibility(View.VISIBLE);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
                byte[] b = baos.toByteArray();
                String urlimg = Base64.encodeToString(b, Base64.DEFAULT);
                UploadImage.setImageBitmap(myBitmap);
                UploadImage.setScaleType(ImageView.ScaleType.FIT_XY);
//                loadProfile(myBitmap, struploadSignature, UploadSignature);
//                loadProfile(myBitmap, struploadadharfront,UploadImage);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Creating file uri to store image/video
     */
//    public Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));
//    }

    /**
     * returning image / video
     */
//    private static File getOutputMediaFile(int type) {
//
//        // External sdcard location
//        File mediaStorageDir = new File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                IMAGE_DIRECTORY_NAME);
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.e("Oops! Failed create ", IMAGE_DIRECTORY_NAME + " directory");
//                return null;
//            }
//        }
//
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");
//        } else if (type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "VID_" + timeStamp + ".mp4");
//        } else {
//            return null;
//        }
//
//        return mediaFile;
//    }
    private void previewMedia() {
        // Checking whether captured media is image or video

        vidPreview.setOnTouchListener(mTouchListener);
        imgPreview.setVisibility(View.GONE);

        vidPreview.setVisibility(View.VISIBLE);
        Log.e("sssss", fileUri.getPath());
        Log.e("sssss00", String.valueOf(fileUri));
//        Log.e("sssss11", String.valueOf(fileUri));

        vidPreview.setVideoPath(fileUri.getPath());
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vidPreview);
        vidPreview.setMediaController(mediaController);
        // start playing
        vidPreview.seekTo(1);
//        vidPreview.setBackgroundDrawable(bitmapDrawable);
        vidPreview.start();
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            return true;
        }
    };
    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (vidPreview.isPlaying())
                vidPreview.pause();
            else
                vidPreview.start();
            return true;
        }

    };

    public static int byteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

//    private void loadProfile(Bitmap bitmap, String strurl, ImageView UploadImage) {
//
//        Log.e("xxxxxx000", String.valueOf(byteSizeOf(bitmap)));
//
//        String VppId = Logics.getVppId(PhotoVideoSignatureActivity.this);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
//        byte[] b = baos.toByteArray();
//        String urlimg = Base64.encodeToString(b, Base64.DEFAULT);
//        UploadImage.setImageBitmap(bitmap);
//        UploadImage.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        Log.e("xxxxxx111", String.valueOf(byteSizeOf(bitmap)));
////        BitmapFactory.Options Options = new BitmapFactory.Options();
////        Options.inSampleSize = 4;
////        Options.inJustDecodeBounds = false;
////        Bitmap action_bitmap = BitmapFactory.decodeFile(urlimg, Options);
//
//        AlertDialogClass.PopupWindowShow(PhotoVideoSignatureActivity.this, mainlayout);
//
////        progressDialog = ProgressDialog.show(PhotoVideoSignatureActivity.this, "Please wait ...", "Uploading ..", true);
////        progressDialog.setCancelable(false);
////        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//        new UploadFile(getApplicationContext(), uploadfilecallback, strurl).execute(urlimg, strurl, VppId);
////        imgAdd.setColorFilter(Cont/extCompat.getColor(this, android.R.color.transparent));
//    }

//    private void loadProfileVideo() {
//
////        Log.e("xxxxxx000", String.valueOf(byteSizeOf(bitmap)));
//
//        String VppId = Logics.getVppId(PhotoVideoSignatureActivity.this);
////        Log.e("convertToBase64()",convertToBase64());
//
////        Log.e("xxxxxx111", String.valueOf(byteSizeOf(bitmap)));
////        BitmapFactory.Options Options = new BitmapFactory.Options();
////        Options.inSampleSize = 4;
////        Options.inJustDecodeBounds = false;
////        Bitmap action_bitmap = BitmapFactory.decodeFile(urlimg, Options);
////        selectedPath = getRealPathFromURI(fileUri);
//
//
//        progressDialog = ProgressDialog.show(PhotoVideoSignatureActivity.this, "Please wait ...", "Uploading ..", true);
//        progressDialog.setCancelable(true);
//        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//        new UploadFile(getApplicationContext(), uploadfilecallback, "video").execute(convertToBase64(), "video", "11159");
//
////        new VideoUploadTask().execute();
//
////        UploadFile.doFileUpload(fileUri.getPath(),handler);
//
//        //        imgAdd.setColorFilter(Cont/extCompat.getColor(this, android.R.color.transparent));
//    }

//    @Override
//    public void Uploadstatus(String type, String status) {
//        Log.e("type", type);
//
//
//        if (status.equalsIgnoreCase("1")) {
//
//            if (up_selfie == true && up_video == true && up_signature == true) {
//                buttonContinue.setVisibility(View.VISIBLE);
//            }
//
//            if (type.equalsIgnoreCase(uploadSelfie)) {
//                textviewPhoto_Uploaded.setVisibility(View.VISIBLE);
//                textviewPhoto_Uploaded.setText("Selfie Uploaded successFully.");
//                progressDialog.dismiss();
//                progressDialog.cancel();
//                up_selfie = true;
//            } else if (type.equalsIgnoreCase(uploadSignature)) {
//                textviewSignature_Uploaded.setVisibility(View.VISIBLE);
//                textviewSignature_Uploaded.setText("Signature uploaded SuccessFully.");
////                if (alertDialog.isShowing()) {
////                    alertDialog.cancel();
////                    progressDialog.dismiss();
////                    progressDialog.cancel();
////                }
//                up_signature = true;
//            } else if (type.equalsIgnoreCase(uploadVideo)) {
//                textviewVideo_Uploaded.setVisibility(View.VISIBLE);
//                textviewVideo_Uploaded.setText("Video uploaded SuccessFully.");
//                linearlayoutPlay.setVisibility(View.GONE);
////                if (alertDialog.isShowing()) {
////                    alertDialog.cancel();
////                }
//                up_video = true;
//                progressDialog.dismiss();
//                progressDialog.cancel();
//            }
//        } else if (status.equalsIgnoreCase("0")) {
//            if (type.equalsIgnoreCase(uploadSelfie)) {
//                textviewPhoto_Uploaded.setVisibility(View.VISIBLE);
//                textviewPhoto_Uploaded.setText("Not Uploaded.");
//                textviewPhoto_Uploaded.setTextColor(getResources().getColor(R.color.red));
//                up_selfie = false;
//                progressDialog.dismiss();
//                progressDialog.cancel();
//
//            } else if (type.equalsIgnoreCase(uploadSignature)) {
//                up_signature = false;
//                textviewSignature_Uploaded.setVisibility(View.VISIBLE);
//                textviewSignature_Uploaded.setTextColor(getResources().getColor(R.color.red));
//                textviewSignature_Uploaded.setText("Not uploaded.");
////                if (alertDialog.isShowing()) {
////                    alertDialog.cancel();
////                }
//                progressDialog.dismiss();
//                progressDialog.cancel();
//            } else if (type.equalsIgnoreCase(uploadVideo)) {
//                up_video = false;
//                textviewVideo_Uploaded.setTextColor(getResources().getColor(R.color.red));
//                textviewVideo_Uploaded.setVisibility(View.VISIBLE);
//                textviewVideo_Uploaded.setText("Not uploaded.");
////                if (alertDialog.isShowing()) {
////                    alertDialog.cancel();
////                }
//                progressDialog.dismiss();
//                progressDialog.cancel();
//            }
//        }
//
//    }


//    @Override
//    public void UploadstatusDoc2(String type, String status, String url) {
//        if (status.equalsIgnoreCase("1")) {
//            Log.e("UploadstatusDoc2: ", up_selfie + "\n" + up_video + "\n" + up_signature);
//
//            if (type.equalsIgnoreCase(uploadSelfie)) {
//                textviewPhoto_Uploaded.setVisibility(View.VISIBLE);
//                textviewPhoto_Uploaded.setText("Selfie Uploaded successFully.");
//                progressDialog.dismiss();
//                progressDialog.cancel();
//                up_selfie = true;
//
//            } else if (type.equalsIgnoreCase(uploadSignature)) {
//                textviewSignature_Uploaded.setVisibility(View.VISIBLE);
//                textviewSignature_Uploaded.setText("Signature uploaded SuccessFully.");
//
//                progressDialog.dismiss();
//                progressDialog.cancel();
//
//                up_signature = true;
//
//            } else if (type.equalsIgnoreCase(uploadVideo)) {
//                textviewVideo_Uploaded.setVisibility(View.VISIBLE);
//                textviewVideo_Uploaded.setText("Video uploaded SuccessFully.");
//                linearlayoutPlay.setVisibility(View.GONE);
//                up_video = true;
//                progressDialog.dismiss();
//                progressDialog.cancel();
//
//            }
//
//            if (up_selfie == true && up_video == true && up_signature == true) {
//                buttonContinue.setVisibility(View.VISIBLE);
//            }
//
//        } else if (status.equalsIgnoreCase("0")) {
//            if (type.equalsIgnoreCase(uploadSelfie)) {
//                textviewPhoto_Uploaded.setVisibility(View.VISIBLE);
//                textviewPhoto_Uploaded.setText("Not Uploaded.");
//                textviewPhoto_Uploaded.setTextColor(getResources().getColor(R.color.red));
//                up_selfie = false;
//                progressDialog.dismiss();
//                progressDialog.cancel();
//
//            } else if (type.equalsIgnoreCase(uploadSignature)) {
//                up_signature = false;
//                textviewSignature_Uploaded.setVisibility(View.VISIBLE);
//                textviewSignature_Uploaded.setTextColor(getResources().getColor(R.color.red));
//                textviewSignature_Uploaded.setText("Not uploaded.");
//
//                progressDialog.dismiss();
//                progressDialog.cancel();
//            } else if (type.equalsIgnoreCase(uploadVideo)) {
//                up_video = false;
//                textviewVideo_Uploaded.setTextColor(getResources().getColor(R.color.red));
//                textviewVideo_Uploaded.setVisibility(View.VISIBLE);
//                textviewVideo_Uploaded.setText("Not uploaded.");
//
//                progressDialog.dismiss();
//                progressDialog.cancel();
//            }
//        }
//    }

//    private void PopUpSignature() {
//
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_signature, viewGroup, false);
//        ImageView imageclose = (ImageView) dialogView.findViewById(R.id.forgot_closeimage);
//        btnUploadSignature = dialogView.findViewById(R.id.btnUploadSignature);
//        btnUploadSignatureClear = dialogView.findViewById(R.id.btnUploadSignatureClear);
//        btnUploadSignatureView = dialogView.findViewById(R.id.btnUploadSignatureView);
//        TranslateAnimation animation = new TranslateAnimation(100.0f, 0.0f, 100.0f, 0.0f);
//        animation.setDuration(1000);  // animation duration
//        animation.setRepeatCount(0);  // animation repeat count
////                    animation.setRepeatMode(1);   // repeat animation (left to right, right to left )
//
//        //for signature
//        lay = (LinearLayout) dialogView.findViewById(R.id.lay);
//        dv = new DrawingView(PhotoVideoSignatureActivity.this);
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setDither(true);
//        mPaint.setColor(Color.BLACK);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeWidth(8);
//        lay.addView(dv);
//
//        lay.startAnimation(animation);
//        imageclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TermsNCond_CheckBx.isChecked()) {
//                    TermsNCond_CheckBx.setChecked(false);
//                }
////                alertDialog.dismiss();
////                alertDialog.cancel();
//            }
//        });
//
//        btnUploadSignature.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Dexter.withActivity(PhotoVideoSignatureActivity.this)
//                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .withListener(new MultiplePermissionsListener() {
//                            @Override
//                            public void onPermissionsChecked(MultiplePermissionsReport report) {
//                                captureScreen();
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                                token.continuePermissionRequest();
//                            }
//                        }).check();
//
//            }
//        });
//        btnUploadSignatureClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                lay.removeAllViews();
//                dv = new DrawingView(PhotoVideoSignatureActivity.this);
//                mPaint = new Paint();
//                mPaint.setAntiAlias(true);
//                mPaint.setDither(true);
//                mPaint.setColor(Color.BLACK);
//                mPaint.setStyle(Paint.Style.STROKE);
//                mPaint.setStrokeJoin(Paint.Join.ROUND);
//                mPaint.setStrokeCap(Paint.Cap.ROUND);
//                mPaint.setStrokeWidth(8);
//                lay.addView(dv);
//            }
//        });
////        btnUploadSignatureView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
//////Bitmap bitmap = (Bitmap) ;
////
////                if (file.exists()) {
////
////                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
////
////                    //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
////                    loadProfile(myBitmap, struploadSignature);
////                    imageview.setImageBitmap(myBitmap);
////
////                } else {
////                    Toast.makeText(PhotoVideoSignatureActivity.this, "DoesNt Exist", Toast.LENGTH_SHORT).show();
////                }
////
////            }
////        });
//
//        imageclose.setVisibility(View.VISIBLE);
//
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setView(dialogView);
//        alertDialog = builder.create();
////        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//    }


    // video

    /**
     * Creating file uri to store image/video
     */
//    public Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));
//    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Android File Upload");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Oops!Failedcreate", " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".webm");
        } else {
            return null;
        }

        return mediaFile;
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // save file url in bundle as it will be null on screen orientation
//        // changes
//        outState.putParcelable("file_uri", fileUri_Video.toString());
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        // get the file url
//        fileUri = savedInstanceState.getParcelable("file_uri");
//    }

    //    private void launchUploadActivity(){
//        Intent i = new Intent(PhotoVideoSignatureActivity.this, UploadActivity.class);
//        i.putExtra("filePath", fileUri.getPath());
//        startActivity(i);
//    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
//    private String convertToBase64() {
//        String strEncodedBase64 = null;
//        file = new File(mVideoPath);
//        try {
//
//            FileInputStream fileInputStream = new FileInputStream(file);
//            byte[] bytes = new byte[(int) file.length()];
//            fileInputStream.read(bytes);
//
//            if (fileInputStream.read(bytes) != -1) {
//
//                Log.d("read incomplete", "convertToBase64: ");
//                Toast.makeText(this, "Read Incomplete", Toast.LENGTH_SHORT).show();
//            }
//
//            strEncodedBase64 = new String(Base64.encode(bytes, Base64.DEFAULT));
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return strEncodedBase64;
//    }

//    private String convertToByteArray() {
//        String strFile = null;
//        File file = new File(getVideoFilePath());
//        try {
//            byte[] data = FileUtils.readFileToByteArray(file);//Convert any file, image or video into byte array
//            strFile = Base64.encodeToString(data, Base64.DEFAULT);//Convert byte array into string
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return strFile;
//    }
    @SuppressLint("NewApi")
    public String getRealPathFromURI(Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

//    public class VideoUploadTask extends AsyncTask<String, String, Integer> {
//        /* This Class is an AsynchTask to upload a video to a server on a background thread
//         *
//         */
//
//        @SuppressLint("LongLogTag")
//        @Override
//        protected Integer doInBackground(String... params) {
//            //Upload the video in the background
//            Log.d("VideoUploadTask","doInBackground");
//
//            //Get the Server URL and the local video path from the parameters
////            if (params.length == 2) {
////                videoPath = params[1];
////            } else {
////                //One or all of the params are not present - log an error and return
////                Log.d("VideoUploadTask doInBackground","One or all of the params are not present");
////                return -1;
////            }
//
//
//            //Create a new Multipart HTTP request to upload the video
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost("https://vpp.ventura1.com/SaveImgDocs_Uat/webresources/img/");
//            FileBody filebodyVideo = new FileBody(new File(convertToBase64()));
//
//            //Create a Multipart entity and add the parts to it
//            try {
//                StringBody vpp_id = new StringBody("11159");
//                StringBody video = new StringBody("video");
////                Log.d("VideoUploadTask doInBackground","Building the request for file: " + videoPath);
//                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//                builder.addPart("pan", filebodyVideo);
//                builder.addPart("vpp_id", vpp_id);
//                builder.addPart("addressproof", video);
//                HttpEntity entity = builder.build();
//                httppost.setEntity(entity);
//            } catch (UnsupportedEncodingException e1) {
//                //Log the error
//                Log.d("VideoUploadTask doInBackground","UnsupportedEncodingException error when setting StringBody for title or description");
//                e1.printStackTrace();
//                return -1;
//            }
//
//            //Send the request to the server
//            HttpResponse serverResponse = null;
//            try {
//                Log.d("VideoUploadTask doInBackground","Sending the Request");
//                serverResponse = httpclient.execute( httppost );
//            } catch (ClientProtocolException e) {
//                //Log the error
//                Log.d("VideoUploadTask doInBackground","ClientProtocolException");
//                e.printStackTrace();
//            } catch (IOException e) {
//                //Log the error
//                Log.d("VideoUploadTask doInBackground","IOException");
//                e.printStackTrace();
//            }
//
//            //Check the response code
//            Log.d("VideoUploadTask doInBackground","Checking the response code");
//            if (serverResponse != null) {
//                Log.d("VideoUploadTask doInBackground","ServerRespone" + serverResponse.getStatusLine());
//                HttpEntity responseEntity = serverResponse.getEntity( );
//                if (responseEntity != null) {
//                    //log the response code and consume the content
//                    Log.d("VideoUploadTask doInBackground","responseEntity is not null");
//                    try {
//                        responseEntity.consumeContent( );
//                    } catch (IOException e) {
//                        //Log the (further...) error...
//                        Log.d("VideoUploadTask doInBackground","IOexception consuming content");
//                        e.printStackTrace();
//                    }
//                }
//            } else {
//                //Log that response code was null
//                Log.d("VideoUploadTask doInBackground","serverResponse = null");
//                return -1;
//            }
//
//            //Shut down the connection manager
//            httpclient.getConnectionManager( ).shutdown( );
//            return 1;
//        }
//
//        @SuppressLint("LongLogTag")
//        @Override
//        protected void onPostExecute(Integer result) {
//            //Check the return code and update the listener
//            Log.d("VideoUploadTask onPostExecute","updating listener after execution"+result);
////            thisTaskListener.onUploadFinished(result);
//        }
//
//    }

//    public String getVideoFilePath() {
//
////        Log.d("", "getVideoFilePathh: "+Logics.getLeadId(this));
//
//        return getAndroidMoviesFolder() + "/" + "00" + "_" + "00" + ".mp4";
//    }

    private File getAndroidMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

//    private void uploadVideoToServer(String pathToVideoFile) {
//        File videoFile = new File(pathToVideoFile);
//        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
//        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://vpp.ventura1.com/videoupload/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        APiValidateAccount apiService = retrofit.create(APiValidateAccount.class);
//        JSONObject paramObject = new JSONObject();
//
//        try {
//            paramObject.put("vpp_id", "11159");
//        } catch (Exception e) {
//            Log.e("dddd", e.getMessage());
//        }
//        Call<String> validateSignature = apiService.UploadVideo(vFile, paramObject.toString());
//        validateSignature.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//                Log.e("success", "" + response.toString());
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.e("failure", "" + call.toString() + "   throwable===" + t.toString());
//            }
//        });
//    }

    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(PhotoVideoSignatureActivity.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();

                Toast.makeText(PhotoVideoSignatureActivity.this, s, Toast.LENGTH_SHORT).show();
//                textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
//                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();
                String msg = u.uploadVideo(selectedPath);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

//    public class Task_finder extends AsyncTask<Void, Void, Void> {
//        private final ProgressDialog dialog = new ProgressDialog(PhotoVideoSignatureActivity.this);
//
//        // can use UI thread here
//        protected void onPreExecute() {
//            this.dialog.setMessage("Loading...");
//            this.dialog.setCancelable(false);
//            this.dialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            // TODO Auto-generated method stub
//            HttpURLConnection conn = null;
//            DataOutputStream dos = null;
//            DataInputStream inStream = null;
//            String existingFileName = getVideoFilePath();
//            String lineEnd = "\r\n";
//            String twoHyphens = "--";
//            String boundary = "*****";
//            int bytesRead, bytesAvailable, bufferSize;
//            byte[] buffer;
//            int maxBufferSize = 1 * 1024 * 1024;
//            String urlString = UrlUploadVideo;
//            try {
//                //------------------ CLIENT REQUEST
//                FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));
//                // open a URL connection to the Servlet
//                URL url = new URL(urlString);
//                // Open a HTTP connection to the URL
//                conn = (HttpURLConnection) url.openConnection();
//                // Allow Inputs
//                conn.setDoInput(true);
//                // Allow Outputs
//                conn.setDoOutput(true);
//                // Don't use a cached copy.
//                conn.setUseCaches(false);
//                // Use a post method.
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Connection", "Keep-Alive");
//                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                dos = new DataOutputStream(conn.getOutputStream());
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + "file" + "\"" + lineEnd); // uploaded_file_name is the Name of the File to be uploaded
//                dos.writeBytes(lineEnd);
//                bytesAvailable = fileInputStream.available();
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                buffer = new byte[bufferSize];
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                while (bytesRead > 0) {
//                    dos.write(buffer, 0, bufferSize);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                }
//                dos.writeBytes(lineEnd);
//                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//                fileInputStream.close();
//                dos.flush();
//                dos.close();
//            } catch (MalformedURLException ex) {
//                Log.e("Debug", "error: " + ex.getMessage(), ex);
//            } catch (IOException ioe) {
//                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
//            }
//            //------------------ read the SERVER RESPONSE
//            try {
//                inStream = new DataInputStream(conn.getInputStream());
//                String str;
//                while ((str = inStream.readLine()) != null) {
//                    Log.e("Debug", "Server Response " + str);
//                    reponse_data = str;
//                }
//                inStream.close();
//            } catch (IOException ioex) {
//                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//
//            if (this.dialog.isShowing()) {
//                this.dialog.dismiss();
//            }
//        }
//    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;

            }
        }
        return true;
    }


//    public void ShowMsg(Context context, String msg) {
//        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
//        alertDialogBuilder.setTitle("VPP");
//        alertDialogBuilder.setMessage(msg);
//        alertDialogBuilder.setIcon(R.drawable.vpp_logo);
////        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
//        alertDialogBuilder.setPositiveButton("Try again..", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
//                arg0.dismiss();
//                arg0.cancel();
//
//                call_methodSelfie();
//            }
//        });
//
//        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        if (!PhotoVideoSignatureActivity.this.isFinishing()) {
//            alertDialog.show();
//        }
//    }

    public void ProgressDlgSomethingIssue(Context context, String msg) {
        // 2. Confirmation message
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

        sweetAlertDialog.setTitleText(msg)
//                .setContentText("Something went wrong try again later..")
                .setConfirmText("Close!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //AlertDialogClass.PopupWindowDismiss();

                        progressDialog.dismiss();
                        progressDialog.cancel();

                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                        finish();
                        finishAffinity();

                        //socket logs need to handle here when socket not avail...
                    }
                });
        if (!PhotoVideoSignatureActivity.this.isFinishing()) {
            sweetAlertDialog.show();
        }
        sweetAlertDialog.setCancelable(false);
//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();
    }


    public void ShowMsg1(Context context, String msg) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle("VPP");
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setIcon(R.drawable.vpp_logo);
//        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        alertDialogBuilder.setPositiveButton("Try again..", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                arg0.dismiss();
                arg0.cancel();

                if (videoUploadCount > 2) {
                    ProgressDlgSomethingIssue(PhotoVideoSignatureActivity.this, "Something went wrong try after sometime..");
                } else {
                    call_methodSelfie(fileselfie);
                }
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


//    private void fetchESIGN() {
//        final JsonObject jsonObject = new JsonObject();
//
//        try {
//
//            jsonObject.addProperty("pan", "ALCPN5199G");
//            jsonObject.addProperty("reqfrom", "tab");
//            jsonObject.addProperty("isotp", "1");
//
//            //    Retrofit retrofit = APIClient.getClient1();
//
//            //    APiValidateAccount aPiValidateAccount = retrofit.create(APiValidateAccount.class);
//
////            Call<String> call = aPiValidateAccount.ESignerWeb(jsonObject.toString());
////
////            call.enqueue(new Callback<String>() {
////
////                @Override
////                public void onResponse(Call<String> call, Response<String> response) {
////                    Log.e("response", response.toString());
////
////                }
////
////                @Override
////                public void onFailure(Call<String> call, Throwable t) {
////
////                    Log.e("failure", "onFailure: " + t.toString());
////
////                }
////            });
////
//
//         /*   new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    getHTTPS_ResponseFromUrl(Const.URL_VerifyAccount,jsonObject.toString());
//
//                }
//            }).start();
//*/
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            FirebaseCrashlytics.getInstance().recordException(e);
//        }
//
//
//    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
//            recordAudio();
        }
    }

    public String getDateCurrentTimeZone() {
        try {
            String date = (DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
            return date;
        } catch (Exception e) {
        }
        return "";
    }

//    public void call_method() {   //video  1
//        File file = null;
//        selectedPath = mVideoPath;
//        file = new File(selectedPath);
////        progressDialog = ProgressDialog.show(PhotoVideoSignatureActivity.this, "Please wait ...", "Uploading  video", true);
////        progressDialog.setCancelable(false);
////        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//
//        ProgressDlg("Uploading Please wait", 1);
//
////        RequestBody requestFile =
////                RequestBody.create(
////                        MediaType.parse("video/mp4"),
////                        file
////                );
//
//        ProgressRequestBody1 requestFile = new ProgressRequestBody1(file, uploadCallbacks);
//
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("file", "video_link.web", requestFile);
//        String vpp_id = Logics.getVppId(PhotoVideoSignatureActivity.this);
//        RequestBody vppid = RequestBody.create(MediaType.parse("text/plain"), vpp_id);
//        RequestBody proof_type = RequestBody.create(MediaType.parse("text/plain"), uploadVideo);
//
//        APiValidateAccount apiService;
//        apiService = new APIClient().getClient3(PhotoVideoSignatureActivity.this).create(APiValidateAccount.class);
//        creatvideo = apiService.UploadVideo(body, vppid, proof_type);
//        creatvideo.enqueue(new Callback<UploadFileResponse>() {
//            @Override
//            public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
//                //   Order order = response.body();
//
//                if (response.isSuccessful()) {
//
//                    UploadFileResponse responseBody = response.body();
//
////                Log.e("responseBody", responseBody.getMessage());
////                Log.e("Proof_type", responseBody.getProof_type());
//                    String proof_type = null;
//
//                    if (responseBody.getStatus().equalsIgnoreCase("1")) {
//                        String url = responseBody.getFileDownloadUri();
//                        long video_size = responseBody.getSize();
//                        String file_name = responseBody.getFileName();
//                        String message = responseBody.getMessage();
//                        String fileType = responseBody.getFileType();
//                        proof_type = responseBody.getProof_type();
////                    TastyToast.makeText(PhotoVideoSignatureActivity.this,"video uploaded",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
//                        Uploadstatus11(proof_type, responseBody.getStatus());
//                    } else {
//                        String message = responseBody.getMessage();
//                        proof_type = responseBody.getProof_type();
//
//                        Uploadstatus11(proof_type, responseBody.getStatus());
//                        TastyToast.makeText(PhotoVideoSignatureActivity.this, message, TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
//
//                    }
//
//                } else {
//
//
//                    switch (response.code()) {
//
//
//                        case 404:
//                            videoUploadCount++;
//
////                            Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
//                            err = "Server Not Found";
//                            break;
//                        case 500:
//                            videoUploadCount++;
//
////                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
//                            err = "Server Unavailable";
//                            break;
//                        case 503:
//                            videoUploadCount++;
//
////                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
//                            err = "Server Overloaded try after sometime";
//                            break;
//                        default:
//                            videoUploadCount++;
//
//                            err = String.valueOf(response.code());
//                            err = "Something went wrong try again.";
////                            Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//
//                    progressDialog.dismiss();
//                    progressDialog.cancel();
//                    ShowMsg1(PhotoVideoSignatureActivity.this, err);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UploadFileResponse> call, Throwable t) {
////                Toast.makeText(getApplicationContext(), "Error--- " + t.getMessage(), Toast.LENGTH_LONG).show();
////                Log.i(" ", t.getMessage());
//
//                progressDialog.dismiss();
//                progressDialog.cancel();
//
//                if (call.isCanceled()) {
//                    Bitmap myLogo = BitmapFactory.decodeResource(PhotoVideoSignatureActivity.this.getResources(), R.drawable.video_selfie);
//                    UploadVideo.setImageBitmap(myLogo);
//                    UploadVideo.setScaleType(ImageView.ScaleType.CENTER);
//                    linearlayoutPlay.setVisibility(View.GONE);
//                } else {
//                    ShowMsg(PhotoVideoSignatureActivity.this, t.getMessage());
//                }
//
//            }
//        });
//
//    }

    public void ProgressDlg(String type, int ii) {  // 0 for photo 1 for video
//        progressDialog = ProgressDialog.show(UploadDocScreen.this, "Please wait ..." + type, "Uploading ..", true);
//        progressDialog.setCancelable(false);
//        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setMax(100); // Progress Dialog Max Value


        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100); // Progress Dialog Max Value
        progressDialog.setMessage(type);

        progressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog.dismiss();
                progressDialog.cancel();

                if (ii == 0) {
                    createPhoto.cancel();
                } else {
                    creatvideo.cancel();
                }
            }
        });

        if (progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog.show();
    }


    public void call_methodSelfie(File file) {   // photo 0

//        progressDialog = ProgressDialog.show(PhotoVideoSignatureActivity.this, "Please wait ...", "Uploading selfie", true);
//        progressDialog.setCancelable(false);
//        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        //AlertDialogClass.PopupWindowShow(PhotoVideoSignatureActivity.this, mainlayout);

        ProgressDlg("Uploading Please wait", 0);
//        RequestBody requestFile =
//                RequestBody.create(
//                        MediaType.parse("image/*"),
//                        file
//                );

        ProgressRequestBody requestFile = new ProgressRequestBody(file, uploadCallbacks);


        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", "selfie_link.jpg", requestFile);
        String VppId = Logics.getVppId(PhotoVideoSignatureActivity.this);
        RequestBody vppid = RequestBody.create(MediaType.parse("text/plain"), VppId);
        RequestBody proof_type = RequestBody.create(MediaType.parse("text/plain"), uploadSelfie);

        APiValidateAccount apiService;
        apiService = new APIClient().getClient3(PhotoVideoSignatureActivity.this).create(APiValidateAccount.class);
        createPhoto = apiService.UploadVideo(body, vppid, proof_type);
        createPhoto.enqueue(new Callback<UploadFileResponse>() {
            @Override
            public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
                System.out.println("response");
                if (response.isSuccessful()) {
                    UploadFileResponse responseBody = response.body();
                    Log.e("", responseBody.getStatus());
                    if (responseBody.getStatus().equalsIgnoreCase("1")) {
                        String url = responseBody.getFileDownloadUri();
                        long video_size = responseBody.getSize();
                        String file_name = responseBody.getFileName();
                        String message = responseBody.getMessage();
                        String fileType = responseBody.getFileType();
                        String proof_type = responseBody.getProof_type();

                        Log.e("url", url);
                        Log.e("video_size", String.valueOf(video_size));
                        Log.e("file_name", file_name);
                        Log.e("message", message);
                        Log.e("fileType", fileType);
                        Log.e("Proof_type", proof_type);
//                    uploadfilecallback.UploadstatusDoc2(Proof_type, responseBody.getStatus(), url);

                        Uploadstatus11(proof_type, responseBody.getStatus());


                        progressDialog.cancel();
                        progressDialog.dismiss();
                        //progressDialog.dismiss();
                        //progressDialog.cancel();

                    } else {
                        String message = responseBody.getMessage();
                        TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    }
                } else {
                    switch (response.code()) {


                        case 404:
                            videoUploadCount++;

//                            Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
                            err = "Server Not Found";
                            break;
                        case 500:
                            videoUploadCount++;

//                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
                            err = "Server Unavailable";
                            break;
                        case 503:
                            videoUploadCount++;

//                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
                            err = "Server Overloaded try after sometime";
                            break;
                        default:
                            videoUploadCount++;

                            err = String.valueOf(response.code());
                            err = "Something went wrong try again.";
//                            Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    ShowMsg1(PhotoVideoSignatureActivity.this, err);
                }
            }

            @Override
            public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                progressDialog.dismiss();
                progressDialog.cancel();
                if (call.isCanceled()) {
                    Bitmap myLogo = BitmapFactory.decodeResource(PhotoVideoSignatureActivity.this.getResources(), R.drawable.hold_id_selfie);
                    UploadImage.setImageBitmap(myLogo);
                    UploadImage.setScaleType(ImageView.ScaleType.CENTER);
                } else {
                    ShowMsg1(PhotoVideoSignatureActivity.this, t.getMessage());
                }

                Log.e("onFailure: ", t.getMessage());
            }
        });

    }

    private void alert() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog
                .setContentText("To verify your kyc, an OTP will be sent to your mobile number linked to Aadhar, do you want to proceed ?")  //Do you wish to proceed for Kyc verification now?
                .setConfirmText("Yes")
                .setCancelText("No")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        TermsNCond_CheckBx.setChecked(false);

                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                       // up_signature = true;
//                        signature_status = "1";
//                        l_s_terms_condition_y_n = "N";
//                        if (up_selfie == true && up_video == true && up_signature == true) {
//                        if (up_selfie == true && up_signature == true) {
//                            //buttonContinue.setVisibility(View.VISIBLE);
//                            btn_upload_document.setVisibility(View.VISIBLE);
//                        }


                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                        startActivity(new Intent(PhotoVideoSignatureActivity.this, WebActivity4.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        TermsNCond_CheckBx.setChecked(true);


                    }
                })
                .show();
        sweetAlertDialog.setCancelable(false);

    }


    public void zip(String[] files, String zipFile) throws IOException {
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                } finally {
                    origin.close();
                }
            }
        } finally {
            out.close();
        }
    }

    public void Uploadstatus11(String type, String status) {
        Log.e("type", type);

//        if (progressDialog!=null){
//            progressDialog.dismiss();
//            progressDialog.cancel();
//        }

        // AlertDialogClass.PopupWindowDismiss();

        progressDialog.dismiss();
        progressDialog.cancel();

        if (status.equalsIgnoreCase("1")) {
            if (type.equalsIgnoreCase(uploadSelfie)) {
                textviewPhoto_Uploaded.setVisibility(View.VISIBLE);
                textviewPhoto_Uploaded.setText("Selfie uploaded successfully.");
                textviewPhoto_Uploaded.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_, 0);


                up_selfie = true;
            } else if (type.equalsIgnoreCase(uploadSignature)) {
                textviewSignature_Uploaded.setVisibility(View.VISIBLE);
                textviewSignature_Uploaded.setText("Signature uploaded successfully.");
                textviewSignature_Uploaded.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_, 0);

                cardviewSignature.setVisibility(View.VISIBLE);
//                if (alertDialog.isShowing()) {
//                    alertDialog.cancel();
//                    progressDialog.dismiss();
//                    progressDialog.cancel();
//                }
                up_signature = true;
            } /*else if (type.equalsIgnoreCase(uploadVideo)) {
                textviewVideo_Uploaded.setVisibility(View.VISIBLE);
                textviewVideo_Uploaded.setText("Video uploaded successfully.");
                textviewVideo_Uploaded.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_, 0);

                linearlayoutPlay.setVisibility(View.GONE);
//                if (alertDialog.isShowing()) {
//                    alertDialog.cancel();
//                }
                up_video = true;

            }*/
        } else if (status.equalsIgnoreCase("0") || (status.equalsIgnoreCase("-1"))) {
            if (type.equalsIgnoreCase(uploadSelfie)) {
                textviewPhoto_Uploaded.setVisibility(View.VISIBLE);
                textviewPhoto_Uploaded.setText("Not Uploaded.");
                textviewPhoto_Uploaded.setTextColor(getResources().getColor(R.color.red));
                up_selfie = false;


            } else if (type.equalsIgnoreCase(uploadSignature)) {
                up_signature = false;
                textviewSignature_Uploaded.setVisibility(View.VISIBLE);
                textviewSignature_Uploaded.setTextColor(getResources().getColor(R.color.red));
                textviewSignature_Uploaded.setText("Not uploaded.");
                cardviewSignature.setVisibility(View.VISIBLE);
//                if (alertDialog.isShowing()) {
//                    alertDialog.cancel();
//                }

            } /*else if (type.equalsIgnoreCase(uploadVideo)) {
                up_video = false;
                textviewVideo_Uploaded.setTextColor(getResources().getColor(R.color.red));
                textviewVideo_Uploaded.setVisibility(View.VISIBLE);
                textviewVideo_Uploaded.setText("Not uploaded.");
//                if (alertDialog.isShowing()) {
//                    alertDialog.cancel();
//                }

            }*/
        }

        if (up_selfie == true/* && up_video == true*/) {
            linear_checkbox.setVisibility(View.VISIBLE);
        } else {
            linear_checkbox.setVisibility(View.GONE);
        }
        if (up_selfie == true/* && up_video == true*/ && up_signature == true) {
            //buttonContinue.setVisibility(View.VISIBLE);
            btn_upload_document.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }
}
