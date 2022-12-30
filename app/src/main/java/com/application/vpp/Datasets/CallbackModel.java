package com.application.vpp.Datasets;

public class CallbackModel {
    String name;
    String created_date;



    String contact_number;
    String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CallbackModel(String name, String created_date, String contact_number, String status, String remark) {
        this.name = name;
        this.created_date = created_date;
        this.contact_number = contact_number;
        this.status = status;
        this.remark = remark;
    }

    String remark;
}

