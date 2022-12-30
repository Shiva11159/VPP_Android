package com.application.vpp.Utility;

import android.app.Application;
import android.database.CursorWindow;

import androidx.appcompat.app.AppCompatDelegate;

import java.lang.reflect.Field;

public class MyApplication extends Application {

    public static final String FONT_PATH_CIVIL = "fonts/Roboto-Regular.ttf";
    public static final String FONT_PATH_PERSIAN = "fonts/IRANSans.ttf";
    public static final String FONT_PATH_HIJRI = "fonts/Amiri-Regular.ttf";
    public static final String FONT_PATH_JAPANESE = "fonts/Roboto-Regular.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
//            if (DEBUG_MODE) {
//            }
        }
    }

}
