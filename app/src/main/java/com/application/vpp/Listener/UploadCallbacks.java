package com.application.vpp.Listener;

public interface UploadCallbacks {
    void onProgressUpdate(int percentage);
    void onError();
    void onFinish();
}