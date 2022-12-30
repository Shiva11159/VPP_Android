package com.application.vpp.Datasets;

public class InserSockettLogs {
    String vpp_id;
    String flag ;
    String app_version;
    String log_date;
    String screenname;
    boolean IsinternetAvailable;

    public String getVpp_id() {
        return vpp_id;
    }

    public void setVpp_id(String vpp_id) {
        this.vpp_id = vpp_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getLog_date() {
        return log_date;
    }

    public void setLog_date(String log_date) {
        this.log_date = log_date;
    }

    public String getScreenname() {
        return screenname;
    }

    public void setScreenname(String screenname) {
        this.screenname = screenname;
    }

    public boolean getIsinternetAvailable() {
        return IsinternetAvailable;
    }

    public InserSockettLogs(String vpp_id, String flag, String app_version, String log_date, String screenname, boolean isinternetAvailable) {
        this.vpp_id = vpp_id;
        this.flag = flag;
        this.app_version = app_version;
        this.log_date = log_date;
        this.screenname = screenname;
        IsinternetAvailable = isinternetAvailable;
    }
    // Other fields
}
