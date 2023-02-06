package com.application.vpp.Datasets;

public class QueryStatusData {
    public String status;
    public String date;
    public String remark;
    public String sr_no;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSr_no() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = sr_no;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryStatusData(String status, String date, String remark, String sr_no, String query) {
        this.status = status;
        this.date = date;
        this.remark = remark;
        this.sr_no = sr_no;
        this.query = query;
    }

    public String query;

}
