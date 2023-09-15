package com.application.vpp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.application.vpp.Const.Const;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.SharedPref.SharedPref;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class PermissionActivity extends AppCompatActivity {
    Button button_grant_permission;
    Context context;

    boolean permissionchecked=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        context = this;
        button_grant_permission = findViewById(R.id.button_grant_permission);
        button_grant_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(PermissionActivity.this)
                        .withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                Logics.storeSharedPref("permission_granted", true, context);
                                Intent intent = new Intent(PermissionActivity.this, SliderImages.class).putExtra("from", "");
                                startActivity(intent);
                                // startActivity(new Intent(PermissionActivity.this, LoginScreen.class));
                                finish();
                                SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");
                            }
                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
    }

//    ActivityResultLauncher<String> requestPermissionlaucnher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//
//        if (isGranted){
//            Toast.makeText(context, "granted", Toast.LENGTH_SHORT).show();
//            permissionchecked=true;
//
//            Logics.storeSharedPref("permission_granted", true, context);
//            Intent intent = new Intent(PermissionActivity.this, SliderImages.class).putExtra("from", "");
//            startActivity(intent);
//
//            // startActivity(new Intent(PermissionActivity.this, LoginScreen.class));
//            finish();
//            SharedPref.savePreferences(getApplicationContext(), Const.FromUpdate, "");
//        }else {
//            Toast.makeText(context, "Not granted", Toast.LENGTH_SHORT).show();
//            permissionchecked=false;
//        }
//
//
//    }); /*java8*/




}
