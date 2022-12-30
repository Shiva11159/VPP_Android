package com.application.vpp.Datasets;

import android.graphics.Bitmap;

public class Notification_data {

    public String title ;
    public String message ;
    public String imgurl ;

    public Notification_data(String title, String message, String imgurl) {
        this.title = title;
        this.message=message;
        this.imgurl=imgurl;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getImgurl() {
        return imgurl;
    }
}
