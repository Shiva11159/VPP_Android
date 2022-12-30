package com.application.vpp.Utility;

import com.application.vpp.Datasets.InserSockettLogs;

import java.util.ArrayList;

public class Singleton {
    private static ArrayList<InserSockettLogs> arrayList;

    private static Singleton instance;

    private Singleton(){
        arrayList = new ArrayList<InserSockettLogs>();
    }

    public static Singleton getInstance(){
        if (instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public ArrayList<InserSockettLogs> getArrayList() {
        return arrayList;
    }
}
