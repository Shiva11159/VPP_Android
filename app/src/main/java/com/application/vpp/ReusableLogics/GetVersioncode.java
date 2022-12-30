package com.application.vpp.ReusableLogics;

import android.os.AsyncTask;

import com.application.vpp.BuildConfig;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GetVersioncode extends AsyncTask<String, String, String> {

    private String newVersion;

    @Override
    protected String doInBackground(String... params) {

        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newVersion;
    }

    public static int GetPlayVersionName() {
        GetVersioncode versionChecker = new GetVersioncode();
        String playstoreLatestVersion = "";
        int i=0;
        try {
            playstoreLatestVersion = versionChecker.execute().get();
            if (Double.parseDouble(BuildConfig.VERSION_NAME) < Double.parseDouble(playstoreLatestVersion)) {
                //perform your task here like show alert dialogue "Need to upgrade app"
                i=1;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return i;
    }
}

