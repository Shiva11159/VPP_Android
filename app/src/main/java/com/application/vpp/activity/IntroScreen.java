//package com.application.vpp.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.CheckBox;
//
//import com.application.vpp.Const.Const;
//import com.application.vpp.R;
//import com.application.vpp.SharedPref.SharedPref;
//import com.application.vpp.Views.Views;
//
//import mehdi.sakout.fancybuttons.FancyButton;
//
//public class IntroScreen extends AppCompatActivity {
//
//    FancyButton btnAgree;
//    CheckBox chkAgree;
//    int isChecked = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_intro_screen);
//
//        btnAgree = (FancyButton)findViewById(R.id.btnAgree);
//        chkAgree = (CheckBox)findViewById(R.id.chkAgree);
//
////        int PERMISSION_ALL = 1;
////        String[] PERMISSIONS = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE};
////
////        if(!hasPermissions(this, PERMISSIONS)){
////            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
////        }
//
//
//        btnAgree.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                isChecked = chkAgree.getInputType();
//                if(chkAgree.isChecked()){
//
//                    startActivity(new Intent(IntroScreen.this,LoginScreen.class));
//                    finish();
//                    SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate,"");
//
//                }else {
//
//                    Views.toast(IntroScreen.this,"Select checkbox to proceed");
//
//                }
//
//
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//
//        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
//
//            Log.d("granted", "onRequestPermissionsResult: ");
//        }else {
//
//            finish();
//            Views.toast(IntroScreen.this,"Permissions are required");
//        }
//    }
//
//    public static boolean hasPermissions(Context context, String... permissions) {
//        if (context != null && permissions != null) {
//            for (String permission : permissions) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//}
