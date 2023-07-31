package com.application.vpp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.application.vpp.Const.Const;
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
import com.application.vpp.Views.Views;
import com.bumptech.glide.Glide;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class UploadDocScreen extends AppCompatActivity implements View.OnClickListener, Uploadfilecallback, UploadCallbacks {
    int j = 0, k = 0;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    Handler handler = new Handler();
    Runnable runnable;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask;

    int delay = 1 * 1000; //Delay for 15 seconds.  One second = 1000 milliseconds.
    String Document_img1;
    private static final String TAG = UploadDocScreen.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    int p = 0;
    ProgressDialog progressDialog;
    @BindView(R.id.imgCameraAdd)
    ImageView imgCameraAdd;
    @BindView(R.id.imgGalleryAdd)
    ImageView imgGalleryAdd;
    @BindView(R.id.imgAdd)
    ImageView imgAdd;
    @BindView(R.id.imgCameraPAN)
    ImageView imgCameraPAN;
    @BindView(R.id.imgGalleryPAN)
    ImageView imgGalleryPAN;
    @BindView(R.id.imgPAN)
    ImageView imgPAN;
    @BindView(R.id.imgCameraCheque)
    ImageView imgCameraCheque;
    @BindView(R.id.imgGalleryCheque)
    ImageView imgGalleryCheque;
    @BindView(R.id.imgCheque)
    ImageView imgCheque;
    @BindView(R.id.imgCheque1)
    ImageView imgCheque1;
    @BindView(R.id.imgAdd1)
    ImageView imgAdd1;
    @BindView(R.id.btn_upload_document)
    FancyButton btn_upload_document;
    @BindView(R.id.imgCorrectCheque)
    ImageView imgCorrectCheque;
    @BindView(R.id.imgCorrectAdd)
    ImageView imgCorrectAdd;
    @BindView(R.id.imgCorrectPAN)
    ImageView imgCorrectPAN;
    @BindView(R.id.titleCheque)
    TextView titleCheque;

    @BindView(R.id.txt_adharback)
    TextView txt_adharback;

    @BindView(R.id.txt_Cheque1)
    TextView txt_Cheque1;

    @BindView(R.id.RbtnFront)
    RadioButton RbtnFront;

    @BindView(R.id.RbtnFrontBack)
    RadioButton RbtnFrontBack;

    @BindView(R.id.RbtnCheque)
    RadioButton RbtnCheque;

    @BindView(R.id.RbtnBankStatement)
    RadioButton RbtnBankStatement;

    boolean uploadadharfront = false;
    boolean uploadadharback = false;
    boolean uploadpan = false;
    boolean uploadcheckfront = false;
    boolean uploadcheckback = false;
    public static String struploadadharfront = "adhar";
    public static String struploadadharback = "adhar1";
    public static String struploadpan = "pan";
    public static String struploadcheckfront = "bank";
    public static String struploadcheckback = "bank1";
    Uploadfilecallback uploadfilecallback;
    public int isDocument;


    CardView cardviewadhar, cardviewpan, cardbank;

    boolean checkadhar = false;
    boolean checkbank = false;
    boolean checkpan = false;
    RadioGroup indexationRd;
    RadioGroup indexationRd1;

    boolean adharUploadedBack = false;
    boolean bankUploadBack = false;

    //-------------------------
    public static String uploadSelfie = "Selfie";
    public static String aadhar_img_link1 = "aadhar_img_link1";
    public static String aadhar_img_link2 = "aadhar_img_link2";
    public static String pan_img_link1 = "pan_img_link1";
    public static String bank_img_link1 = "bank_img_link1";
    public static String bank_img_link2 = "bank_img_link2";


    boolean up_aadhar_img_link1 = false;
    boolean up_aadhar_img_link2 = false;
    boolean up_pan_img_link1 = false;
    boolean up_bank_img_link1 = false;
    boolean up_bank_img_link2 = false;

    boolean up_selfie = false;
    boolean up_video = false;
    boolean up_signature = false;

    Context context;

    UploadCallbacks uploadCallbacks;

    Call<UploadFileResponse> create;

    String err = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_doc_screen);
        ButterKnife.bind(this);
        uploadfilecallback = (Uploadfilecallback) this;

        uploadCallbacks = (UploadCallbacks) this;

        cardviewadhar = findViewById(R.id.cardviewadhar);
        cardviewpan = findViewById(R.id.cardviewpan);
        cardbank = findViewById(R.id.cardbank);
        indexationRd = findViewById(R.id.indexationRd);
        indexationRd1 = findViewById(R.id.indexationRd1);
        indexationRd.setOnCheckedChangeListener(checkChange);
        indexationRd1.setOnCheckedChangeListener(checkChange1);

        uploadadharback = true;
        uploadcheckback = true;

        context = UploadDocScreen.this;

        progressDialog = new ProgressDialog(this);

//        if (RbtnFront.isChecked()) {
//
//        } else {
//            uploadadharback = false;
//        }
//
//        if (RbtnCheque.isChecked()) {
//
//        } else {
//            uploadcheckback = false;
//        }

      /*  Log.e("000", getIntent().getStringExtra(Const.adharstatus));
        Log.e("111", getIntent().getStringExtra(Const.bankstatus));
        Log.e("222", getIntent().getStringExtra(Const.panstatus));*/

//
//        // email ID of Recipient.
//        String recipient = "shiva.tumma@ventura1.com";
//
//        // email ID of  Sender.
//        String sender = "noreply@ventura1.com";
//
//        // using host as localhost
//        String host = "180.173.114.36";
//
//        // Getting system properties
//        Properties properties = System.getProperties();
//
//        // Setting up mail server
//        properties.setProperty("mail.smtp.host", host);
//
//        // creating session object to get properties
//        Session session = Session.getDefaultInstance(properties);
//
//        try
//        {
//            // MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From Field: adding senders email to from field.
//            message.setFrom(new InternetAddress(sender));
//
//            // Set To Field: adding recipient's email to from field.
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
//
//            // Set Subject: subject of the email
//            message.setSubject("This is Subject");
//
//            // set body of the email.
//            message.setContent("<h1>This is a HTML text</h1>","text/html");
//
//            // Send email.
//            Transport.send(message);
//            System.out.println("Mail successfully sent");
//        }
//        catch (MessagingException mex)
//        {
//            mex.printStackTrace();
//        }


        if (getIntent().getStringExtra(Const.from).equalsIgnoreCase(Const.doc)) {
            if (getIntent().getStringExtra(Const.adharstatus).equalsIgnoreCase("2") || getIntent().getStringExtra(Const.adharstatus).equalsIgnoreCase("0")) {
                cardviewadhar.setVisibility(View.VISIBLE);
                cardviewpan.setVisibility(View.GONE);
                cardbank.setVisibility(View.GONE);
                if (RbtnFront.isChecked()) {
                    uploadadharfront = false;
                    uploadadharback = true;
                } else {
                    uploadadharback = false;
                }
                uploadpan = true;
                uploadcheckfront = true;
                uploadcheckback = true;
                checkadhar = true;

                Log.e("uploadadharback", String.valueOf(uploadadharback) + "" + cardviewadhar.getVisibility());
            }
            if (getIntent().getStringExtra(Const.bankstatus).equalsIgnoreCase("2") || getIntent().getStringExtra(Const.bankstatus).equalsIgnoreCase("0")) {
                cardviewpan.setVisibility(View.GONE);
                cardbank.setVisibility(View.VISIBLE);
                if (checkadhar == true) {
                    cardviewadhar.setVisibility(View.VISIBLE);

                } else {
                    uploadadharfront = true;
                    uploadadharback = true;
                    cardviewadhar.setVisibility(View.GONE);
                }

                if (RbtnCheque.isChecked()) {
                    uploadcheckfront = false;
                    uploadcheckback = true;
                } else {
                    uploadcheckback = false;
                }

                uploadpan = true;
                uploadcheckfront = false;
                uploadcheckback = false;
                checkbank = true;

                Log.e("222bank", String.valueOf(uploadadharfront) + " " + cardviewadhar.getVisibility() + "");

            }
            if (getIntent().getStringExtra(Const.panstatus).equalsIgnoreCase("2") || getIntent().getStringExtra(Const.panstatus).equalsIgnoreCase("0")) {
                cardviewadhar.setVisibility(View.GONE);
                cardviewpan.setVisibility(View.VISIBLE);
                uploadadharfront = true;
                uploadadharback = true;
                uploadpan = false;
                if (checkbank == true) {
//                    uploadcheckfront = false;
//                    uploadcheckback = false;

                    if (RbtnCheque.isChecked()) {
                        uploadcheckfront = false;
                        uploadcheckback = true;
                    } else {
                        uploadcheckfront = false;
                        uploadcheckback = false;
                    }

                    cardbank.setVisibility(View.VISIBLE);


                } else {
                    cardbank.setVisibility(View.GONE);
                    uploadcheckfront = true;
                    uploadcheckback = true;
                }

                if (checkadhar == true) {
                    if (RbtnFront.isChecked()) {
                        uploadadharfront = false;
                        uploadadharback = true;
                    } else {
                        uploadadharfront = false;
                        uploadadharback = false;
                    }
                    cardviewadhar.setVisibility(View.VISIBLE);

                } else {
                    uploadadharfront = true;
                    uploadadharback = true;
                    cardviewadhar.setVisibility(View.GONE);

                }
                checkpan = true;
            }

            Log.e("pan", String.valueOf(uploadpan) + cardviewadhar.getVisibility());


        }


        /*else if (getIntent().getStringExtra(Const.from).equalsIgnoreCase(Const.adharstatus)) {
            cardviewadhar.setVisibility(View.VISIBLE);
            cardviewpan.setVisibility(View.GONE);
            cardbank.setVisibility(View.GONE);
            uploadadharfront = false;
            uploadadharback = false;
            uploadpan = true;
            uploadcheckfront = true;
            uploadcheckback = true;
        } else if (getIntent().getStringExtra(Const.from).equalsIgnoreCase(Const.bankstatus)) {
            cardviewadhar.setVisibility(View.GONE);
            cardviewpan.setVisibility(View.GONE);
            cardbank.setVisibility(View.VISIBLE);
            uploadadharfront = true;
            uploadadharback = true;
            uploadpan = true;
            uploadcheckfront = false;
            uploadcheckback = false;
        } else if (getIntent().getStringExtra(Const.from).equalsIgnoreCase(Const.panstatus)) {
            cardviewadhar.setVisibility(View.GONE);
            cardviewpan.setVisibility(View.VISIBLE);
            cardbank.setVisibility(View.GONE);
            uploadadharfront = true;
            uploadadharback = true;
            uploadpan = false;
            uploadcheckfront = true;
            uploadcheckback = true;
        }
*/
        if (getIntent().getExtras() != null) {
            isDocument = getIntent().getExtras().getInt("isDocument", 0);
        }
        if (isDocument == 0) {
           // alert();

        }

        btn_upload_document.setOnClickListener(this);
        ImagePickerActivity.clearCache(this);


        if (RbtnFront.isChecked()) {
            imgAdd.setVisibility(View.VISIBLE);
            imgAdd1.setVisibility(View.GONE);
            txt_adharback.setVisibility(View.GONE);
            uploadadharback = true;
        } else {
            imgAdd.setVisibility(View.VISIBLE);
            imgAdd1.setVisibility(View.VISIBLE);
            txt_adharback.setVisibility(View.VISIBLE);
            if (!adharUploadedBack) {
                uploadadharback = false;
            }
        }

        if (RbtnCheque.isChecked()) {
            imgCheque.setVisibility(View.VISIBLE);
            imgCheque1.setVisibility(View.GONE);
            txt_Cheque1.setVisibility(View.GONE);
            uploadcheckback = true;
            titleCheque.setText("Cancelled Cheque");
        } else {
            imgCheque.setVisibility(View.VISIBLE);
            imgCheque1.setVisibility(View.VISIBLE);
            txt_Cheque1.setVisibility(View.VISIBLE);

            if (!bankUploadBack) {
                uploadcheckback = false;
            }
            titleCheque.setText("Bank Statement");

        }


    }

    private void loadProfile(Bitmap bitmap, File file) {
        String VppId = Logics.getVppId(UploadDocScreen.this);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray();
        String urlimg = Base64.encodeToString(b, Base64.DEFAULT);
        if (p == 1) {
            if (j == 0) {
                imgAdd.setImageBitmap(bitmap);
                imgAdd.setScaleType(ImageView.ScaleType.FIT_XY);
                ProgressDlg(" Uploading Address proof");
                uploadadharfront = false;
                call_methodSelfie(file, "aadhar_img_link1");
//                new UploadFile(getApplicationContext(), uploadfilecallback, struploadadharfront).execute(urlimg, "adhar", VppId);
            } else if (j == 1) {
                imgAdd1.setImageBitmap(bitmap);
                imgAdd1.setScaleType(ImageView.ScaleType.FIT_XY);
                ProgressDlg(" Uploading Address proof");
                uploadadharback = false;
                call_methodSelfie(file, "aadhar_img_link2");
                //  new UploadFile(getApplicationContext(), uploadfilecallback, struploadadharback).execute(urlimg, "adhar1", VppId);
            }
        } else if (p == 2) {
            imgPAN.setImageBitmap(bitmap);
            imgPAN.setScaleType(ImageView.ScaleType.FIT_XY);
            ProgressDlg("Uploading PAN");
            uploadpan = false;
            call_methodSelfie(file, "pan_img_link1");
//            new UploadFile(getApplicationContext(), uploadfilecallback, struploadpan).execute(urlimg, "pan", VppId);
        } else if (p == 3) {
            if (k == 0) {
                imgCheque.setImageBitmap(bitmap);
                imgCheque.setScaleType(ImageView.ScaleType.FIT_XY);
                ProgressDlg("Uploading Bank Proof");
                uploadcheckfront = false;
                call_methodSelfie(file, "bank_img_link1");
                //  new UploadFile(getApplicationContext(), uploadfilecallback, struploadcheckfront).execute(urlimg, "bank", VppId);
            } else if (k == 1) {
                imgCheque1.setImageBitmap(bitmap);
                imgCheque1.setScaleType(ImageView.ScaleType.FIT_XY);

                ProgressDlg("Uploading Bank Proof");
                uploadcheckback = false;
                call_methodSelfie(file, "bank_img_link2");
//                new UploadFile(getApplicationContext(), uploadfilecallback, struploadcheckback).execute(urlimg, "bank1", VppId);
            }
        }

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);

                if (uploadadharfront && uploadadharback) {
                    imgCorrectAdd.setVisibility(View.VISIBLE);
                } else {
                    imgCorrectAdd.setVisibility(View.GONE);
                }

                if (uploadpan) {
                    imgCorrectPAN.setVisibility(View.VISIBLE);
                } else {
                    imgCorrectPAN.setVisibility(View.GONE);
                }

                if (uploadcheckfront && uploadcheckback) {
                    imgCorrectCheque.setVisibility(View.VISIBLE);
                } else {
                    imgCameraCheque.setVisibility(View.GONE);
                }
                Log.e("run: ", uploadadharfront + "_" + uploadadharback + "_" + uploadcheckfront + "_" + uploadcheckback + "_" + uploadpan);
                if (uploadadharfront && uploadadharback && uploadcheckfront && uploadcheckback && uploadpan) {
                    Logics.setIsImgUpload(UploadDocScreen.this, 1);
                    btn_upload_document.setVisibility(View.VISIBLE);
                } else {
                    Logics.setIsImgUpload(UploadDocScreen.this, 0);
                    btn_upload_document.setVisibility(View.GONE);
                }

            }
        }, delay);
//        imgAdd.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void alert() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(UploadDocScreen.this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog
                .setContentText("Do you wish to upload your Documents now?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        if (SharedPref.getPreferences(getApplicationContext(), SharedPref.UPIPayment).equalsIgnoreCase(SharedPref.UPIPaymentDONE)) {
                            Intent intent = new Intent(UploadDocScreen.this, Dashboard.class);
                            intent.putExtra("isDocument", 0);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(UploadDocScreen.this, UpiPayment.class);
                            intent.putExtra("isDocument", 0);
                            startActivity(intent);
                        }
                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
//                        Intent intent = new Intent(UploadDocScreen.this, UpiPayment.class);
//                        intent.putExtra("isDocument", 0);
//                        startActivity(intent);
                        finish();

                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sDialog.dismiss();
                        sDialog.cancel();
                    }
                })
                .show();
        sweetAlertDialog.setCancelable(false);

    }

    private void alert1() {
        builder = new AlertDialog.Builder(UploadDocScreen.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_upload_img_now, null);
        builder.setView(dialogView);
        FancyButton btn_positive = dialogView.findViewById(R.id.btnIsRegYes);
        FancyButton btn_negative = dialogView.findViewById(R.id.btnIsRegNo);

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   startActivity(new Intent(UploadDocScreen.this, PaymentGateway.class));
                //  startActivity(new Intent(UploadDocScreen.this, UpiPayment.class).putExtra("isDocument",0));
                Intent intent = new Intent(UploadDocScreen.this, UpiPayment.class);
                intent.putExtra("isDocument", 0);
                startActivity(intent);
                finish();
            }
        });

        builder.setCancelable(false);
        alertDialog = builder.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void loadProfileDefault() {
        Glide.with(this).load(R.drawable.ic_aadhar).into(imgAdd);
        //imgAdd.setColorFilter(ContextCompat.getColor(this, R.color.profile_default_tint));

        Glide.with(this).load(R.drawable.ic_pan_80_4).into(imgPAN);
        //imgPAN.setColorFilter(ContextCompat.getColor(this, R.color.profile_default_tint));

        Glide.with(this).load(R.drawable.ic_cheque_80_4).into(imgCheque);
        //imgCheque.setColorFilter(ContextCompat.getColor(this, R.color.profile_default_tint));

    }

    @OnClick({R.id.imgCameraAdd, R.id.imgCameraPAN, R.id.imgCameraCheque, R.id.imgGalleryAdd,
            R.id.imgGalleryPAN, R.id.imgGalleryCheque, R.id.imgAdd1, R.id.imgAdd, R.id.imgCheque1, R.id.imgCheque, R.id.imgPAN})
    void onProfileImageClick(final View view) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        int id = view.getId();
                        switch (id) {
                            case R.id.imgAdd:
                                p = 1;
                                j = 0;
                                PopUpChooseCamORgallery(imgAdd);
                                break;
                            case R.id.imgAdd1:
                                p = 1;
                                j = 1;
                                PopUpChooseCamORgallery(imgAdd1);
                                break;
                            case R.id.imgPAN:
                                p = 2;
                                PopUpChooseCamORgallery(imgPAN);
                                break;
                            case R.id.imgCheque:
                                p = 3;
                                k = 0;
                                PopUpChooseCamORgallery(imgCheque);
                                break;
                            case R.id.imgCheque1:
                                p = 3;
                                k = 1;
                                PopUpChooseCamORgallery(imgCheque1);
                                break;
/*
                            case R.id.imgCameraAdd:
                                p = 1;
                                launchCameraIntent();
                                break;
                            case R.id.imgCameraPAN:
                                p = 2;
                                launchCameraIntent();
                                break;
                            case R.id.imgCameraCheque:
                                p = 3;
                                launchCameraIntent();
                                break;
                            case R.id.imgGalleryAdd:
                                p = 1;
                                launchGalleryIntent();
                                break;
                            case R.id.imgGalleryPAN:
                                p = 2;
                                launchGalleryIntent();
                                break;
                            case R.id.imgGalleryCheque:
                                p = 3;
                                launchGalleryIntent();
                                break;
*/
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            //showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

//    public static String getFileToByte(String filePath) {
//        try {
//            InputStream is = new FileInputStream(filePath);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            Base64OutputStream b64os = new Base64OutputStream(baos, Base64.DEFAULT);
//            byte[] buffer = new byte[8192];
//            int bytesRead;
//            try {
//                while ((bytesRead = is.read(buffer)) > -1) {
//                    b64os.write(buffer, 0, bytesRead);
//                }
//                return baos.toString();
//            } catch (IOException e) {
//                Log.e(TAG, "Cannot read file " + filePath, e);
//                return "";
//
//            } finally {
//            }
//        } catch (FileNotFoundException e) {
//            return "";
//        }
//    }

/*
    private void launchCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Intent intent = new Intent(UploadDocScreen.this, ImagePickerActivity.class);
//        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
//
//        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
*/

//    private void launchGalleryIntent() {
//        Intent intent = new Intent(UploadDocScreen.this, ImagePickerActivity.class);
//        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
//        startActivityForResult(intent, REQUEST_IMAGE);
//    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
//    private void showSettingsDialog() {
           /* AlertDialog.Builder builder = new AlertDialog.Builder(UploadDocScreen.this);
            builder.setTitle(getString(R.string.dialog_permission_title));
            builder.setMessage(getString(R.string.dialog_permission_message));
            builder.setPositiveButton(getString(R.string.go_to_settings),(dialo))


            builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
                dialog.cancel();
                openSettings();
            });
            builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
            builder.show();*/

    //  }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_upload_document: {
                int status = Logics.getIsImgUpload(UploadDocScreen.this);
                if (status == 1) {
                    if (isDocument == 0) {
                        TastyToast.makeText(getApplicationContext(), "Images Uploaded Succesfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        if (Logics.getPaymentStatus(UploadDocScreen.this).equalsIgnoreCase("0")) {
                            Intent intent = new Intent(UploadDocScreen.this, UpiPayment.class);
                            intent.putExtra("isDocument", 0);
                            startActivity(intent);
                        } else if(Logics.getEsignStatus(UploadDocScreen.this).equalsIgnoreCase("0")){
                            Intent intent = new Intent(UploadDocScreen.this, PhotoVideoSignatureActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(UploadDocScreen.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                      /*  Intent intent = new Intent(UploadDocScreen.this, UpiPayment.class);
                        intent.putExtra("isDocument", 0);
                        startActivity(intent);*/
                    } else if (isDocument == 1) {
                        TastyToast.makeText(
                                getApplicationContext(),
                                "Images Uploaded Succesfully",
                                TastyToast.LENGTH_LONG,
                                TastyToast.SUCCESS
                        );
                        Intent intent = new Intent(UploadDocScreen.this, Dashboard.class);
                        startActivity(intent);
                    }
                } else {
                    TastyToast.makeText(getApplicationContext(), "Please select image", TastyToast.LENGTH_LONG, TastyToast.ERROR
                    );
                }
                //  Intent intent = new Intent(Welcome.this,DashoboardDesign.class);
            }
            break;
        }
    }

    @Override
    public void Uploadstatus(String type, String status) {
        Log.e("type", type);

        progressDialog.cancel();
        progressDialog.dismiss();

        if (status.equalsIgnoreCase("1")) {
            if (type.equalsIgnoreCase(struploadadharfront)) {
                uploadadharfront = true;
            } else if (type.equalsIgnoreCase(struploadadharback)) {
                uploadadharback = true;
                adharUploadedBack = true;
            } else if (type.equalsIgnoreCase(struploadpan)) {
                uploadpan = true;
            } else if (type.equalsIgnoreCase(struploadcheckfront)) {
                uploadcheckfront = true;
            } else if (type.equalsIgnoreCase(struploadcheckback)) {
                uploadcheckback = true;
                bankUploadBack = true;
            }

        } else {
//            TastyToast.makeText(UploadDocScreen.this,"Upload failed upload again",TastyToast.LENGTH_LONG,TastyToast.ERROR);
            Views.ShowMsg(UploadDocScreen.this, "Upload Failed", "Try Again later");
            if (type.equalsIgnoreCase(struploadadharfront)) {
                uploadadharfront = false;
            } else if (type.equalsIgnoreCase(struploadadharback)) {
                uploadadharback = false;
            } else if (type.equalsIgnoreCase(struploadpan)) {
                uploadpan = false;
            } else if (type.equalsIgnoreCase(struploadcheckfront)) {
                uploadcheckfront = false;
            } else if (type.equalsIgnoreCase(struploadcheckback)) {
                uploadcheckback = false;
            }
        }
    }


    public void UploadstatusDoc2(String type, String status, String url) {


        progressDialog.cancel();
        progressDialog.dismiss();

        if (status.equalsIgnoreCase("1")) {
            if (type.equalsIgnoreCase(aadhar_img_link1)) {
                uploadadharfront = true;
            } else if (type.equalsIgnoreCase(aadhar_img_link2)) {
                uploadadharback = true;
                adharUploadedBack = true;
            } else if (type.equalsIgnoreCase(pan_img_link1)) {
                uploadpan = true;
            } else if (type.equalsIgnoreCase(bank_img_link1)) {
                uploadcheckfront = true;
            } else if (type.equalsIgnoreCase(bank_img_link2)) {
                uploadcheckback = true;
                bankUploadBack = true;
            }

        } else {
//            TastyToast.makeText(UploadDocScreen.this,"Upload failed upload again",TastyToast.LENGTH_LONG,TastyToast.ERROR);
            Views.ShowMsg(UploadDocScreen.this, "Upload Failed", "Try Again later");
            if (type.equalsIgnoreCase(aadhar_img_link1)) {
                uploadadharfront = false;
            } else if (type.equalsIgnoreCase(aadhar_img_link2)) {
                uploadadharback = false;
            } else if (type.equalsIgnoreCase(pan_img_link1)) {
                uploadpan = false;
            } else if (type.equalsIgnoreCase(bank_img_link1)) {
                uploadcheckfront = false;
            } else if (type.equalsIgnoreCase(bank_img_link2)) {
                uploadcheckback = false;
            }
        }
    }

    public void ProgressDlg(String type) {
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
                create.cancel();
            }
        });

        if (progressDialog.isShowing())
            progressDialog.dismiss();

        progressDialog.show();
    }

    void launch_instructions() {
        // sequence example
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "Button");
        sequence.setConfig(config);
        sequence.addSequenceItem(imgCameraAdd,
                "This is for Open Camera , User redirect to camera to click Aadhaar card image picture", "NEXT");
        sequence.addSequenceItem(imgGalleryAdd,
                "This is for Gallery, User can select image/Aadhaar Card image from gallery.", "NEXT");
        sequence.addSequenceItem(imgAdd,
                "Caputred or Selected Aadhaar Card image will show here.", "NEXT");
        sequence.addSequenceItem(imgAdd1,
                "Select this Aadhaar Card option if you want to select or carpture back side of Aadhar card", "NEXT");
        sequence.addSequenceItem(btn_upload_document,
                "Submit documents for approval", "GOT IT");
        sequence.start();
    }

    @SuppressLint("RestrictedApi")
    public void PopUpChooseCamORgallery(ImageView imageView) {
        PopupMenu popup = new PopupMenu(UploadDocScreen.this, imageView);
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
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(UploadDocScreen.this, e.getMessage());
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
        }
    }


    private void galleryIntent() {
        Intent intent = new Intent(UploadDocScreen.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(UploadDocScreen.this, ImagePickerActivity.class);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Uri uri = data.getParcelableExtra("path");
        try {
            // You can update this bitmap to your server
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            // loading profile image from local cache
            File file = new File(uri.getPath());
            loadProfile(bitmap, file);

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(UploadDocScreen.this, e.getMessage());
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
        try {
            // You can update this bitmap to your server
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            File file = new File(uri.getPath());
            loadProfile(bitmap, file);

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            AlertDialogClass.ShowMsg(UploadDocScreen.this, e.getMessage());
        }
    }

    private RadioGroup.OnCheckedChangeListener checkChange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
            switch (checkId) {
                case R.id.RbtnFront:
                    imgAdd.setVisibility(View.VISIBLE);
                    imgAdd1.setVisibility(View.GONE);
                    txt_adharback.setVisibility(View.GONE);
                    uploadadharback = true;
                    break;
                case R.id.RbtnFrontBack:
                    imgAdd.setVisibility(View.VISIBLE);
                    imgAdd1.setVisibility(View.VISIBLE);
                    txt_adharback.setVisibility(View.VISIBLE);
                    if (!adharUploadedBack) {
                        uploadadharback = false;
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener checkChange1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
            switch (checkId) {
                case R.id.RbtnCheque:
                    imgCheque.setVisibility(View.VISIBLE);
                    imgCheque1.setVisibility(View.GONE);
                    txt_Cheque1.setVisibility(View.GONE);
                    uploadcheckback = true;
                    titleCheque.setText("Cancelled Cheque");

                    break;
                case R.id.RbtnBankStatement:
                    imgCheque.setVisibility(View.VISIBLE);
                    imgCheque1.setVisibility(View.VISIBLE);
                    txt_Cheque1.setVisibility(View.VISIBLE);

                    if (!bankUploadBack) {
                        uploadcheckback = false;
                    }
                    titleCheque.setText("Bank Statement");

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }


    public void call_methodSelfie(File file, String proof_type1) {

//        progressDialog = ProgressDialog.show(UploadDocScreen.this, "Please wait ...", "Uploading ..", true);
//        progressDialog.setCancelable(true);
//        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
        String VppId = Logics.getVppId(UploadDocScreen.this);
//        RequestBody requestFile =
//                RequestBody.create(
//                        MediaType.parse("image/*"),
//                        file
//                );

        ProgressRequestBody requestFile = new ProgressRequestBody(file, this);

        //file name should be vppid_proof_type.jgp.  (prooftype means column name of tbl_img_docs)

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", proof_type1 + ".jpg", requestFile);

        RequestBody vppid = RequestBody.create(MediaType.parse("text/plain"), VppId);
        RequestBody proof_type = RequestBody.create(MediaType.parse("text/plain"), proof_type1);

        APiValidateAccount apiService;
        apiService = new APIClient().getClient3(context).create(APiValidateAccount.class);
        create = apiService.UploadVideo(body, vppid, proof_type);
        create.enqueue(new Callback<UploadFileResponse>() {
            @Override
            public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {

                try {
                    System.out.println("response");
                    UploadFileResponse responseBody = response.body();
                    Log.e("XXXXXXXXX", responseBody.getStatus());
                    if (response.isSuccessful()) {

                        if (responseBody.getStatus().equalsIgnoreCase("1")) {
                            String url = responseBody.getFileDownloadUri();
                            long video_size = responseBody.getSize();
                            String file_name = responseBody.getFileName();
                            String message = responseBody.getMessage();
                            String fileType = responseBody.getFileType();
                            String Proof_type = responseBody.getProof_type();

                            Log.e("url", url);
                            Log.e("video_size", String.valueOf(video_size));
                            Log.e("file_name", file_name);
                            Log.e("message", message);
                            Log.e("fileType", fileType);
                            Log.e("Proof_type", Proof_type);
                            //    Toast.makeText(context,"1",Toast.LENGTH_SHORT).show();
                            UploadstatusDoc2(Proof_type, responseBody.getStatus(), url);
                            //uploadfilecallback.Uploadstatus(Proof_type,responseBody.getStatus().toString());
                            //     Toast.makeText(context,"2",Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                            //                  progressDialog.cancel();

                        } else {
                            String message = responseBody.getMessage();
                            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            Log.e("message",message);

                        }
                    } else {

                        switch (response.code()) {


                            case 404:
                                //  videoUploadCount++;

//                            Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
                                err = "Server Not Found";
                                break;
                            case 500:
                                // videoUploadCount++;

//                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
                                err = "Server Unavailable";
                                break;
                            case 503:
                                // videoUploadCount++;

//                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
                                err = "Server Overloaded try after sometime";
                                break;
                            default:
                                // videoUploadCount++;

                                err = String.valueOf(response.code());
                                err = "Something went wrong try again.";
//                            Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), "Error--- " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i(" ", t.getMessage());
                if (call.isCanceled()) {
                    Log.e(TAG, "request is cancelled");
                    Toast.makeText(context, "Cancelled by user upload again..", Toast.LENGTH_SHORT).show();
                    Bitmap myLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.iconimageadd);

                    if (proof_type1.equalsIgnoreCase("aadhar_img_link1")) {
                        imgAdd.setImageBitmap(myLogo);
                        imgAdd.setScaleType(ImageView.ScaleType.CENTER);

                    } else if (proof_type1.equalsIgnoreCase("aadhar_img_link2")) {
                        imgAdd1.setImageBitmap(myLogo);
                        imgAdd1.setScaleType(ImageView.ScaleType.CENTER);

                    } else if (proof_type1.equalsIgnoreCase("pan_img_link1")) {
                        imgPAN.setImageBitmap(myLogo);
                        imgPAN.setScaleType(ImageView.ScaleType.CENTER);

                    } else if (proof_type1.equalsIgnoreCase("bank_img_link1")) {
                        imgCheque.setImageBitmap(myLogo);
                        imgCheque.setScaleType(ImageView.ScaleType.CENTER);

                    } else if (proof_type1.equalsIgnoreCase("bank_img_link2")) {
                        imgCheque1.setImageBitmap(myLogo);
                        imgCheque1.setScaleType(ImageView.ScaleType.CENTER);
                    }

                } else {
                    Log.e(TAG, "other larger issue, i.e. no network connection?");
                    progressDialog.dismiss();
                    progressDialog.cancel();

                    Bitmap myLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.iconimageadd);

                    if (proof_type1.equalsIgnoreCase("aadhar_img_link1")) {
                        imgAdd.setImageBitmap(myLogo);
                        imgAdd.setScaleType(ImageView.ScaleType.CENTER);

                    } else if (proof_type1.equalsIgnoreCase("aadhar_img_link2")) {
                        imgAdd1.setImageBitmap(myLogo);
                        imgAdd1.setScaleType(ImageView.ScaleType.CENTER);

                    } else if (proof_type1.equalsIgnoreCase("pan_img_link1")) {
                        imgPAN.setImageBitmap(myLogo);
                        imgPAN.setScaleType(ImageView.ScaleType.CENTER);

                    } else if (proof_type1.equalsIgnoreCase("bank_img_link1")) {
                        imgCheque.setImageBitmap(myLogo);
                        imgCheque.setScaleType(ImageView.ScaleType.CENTER);

                    } else if (proof_type1.equalsIgnoreCase("bank_img_link2")) {
                        imgCheque1.setImageBitmap(myLogo);
                        imgCheque1.setScaleType(ImageView.ScaleType.CENTER);
                    }
                }

                Toast.makeText(getApplicationContext(), "Error--- " + t.getMessage(), Toast.LENGTH_LONG).show();

                Log.e( "onFailure: ",  t.getMessage());

            }


        });


    }

    @Override
    public void onProgressUpdate(int percentage) {

        progressDialog.setProgress(percentage);

        Log.e("onProgressUpdate:", percentage + "");

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        progressDialog.setProgress(100);

    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }
}








