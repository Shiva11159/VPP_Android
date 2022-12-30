package com.application.vpp.Datasets;

import java.util.Arrays;

public class Order {
    private int sr_no;
    private String vpp_id;
    private String amount;
    private String currency;
    private String receipt;
    private Boolean payment_capture;
    private int amount_paid;
    private String[] notes;
    private int created_at;
    private int amount_due;
    private String id;
    private String entity;
    private String offer_id;
    private String status;
    private int attempts;
    private int transaction_id;
    private String key_id;
    private String secret_id;
    private String pan_number;
    private String mobile_app_version;
    private String created_by;
    private String created_date;
    private String updated_by;
    private String updated_date;

    public int getSr_no() {
        return sr_no;
    }

    public void setSr_no(int sr_no) {
        this.sr_no = sr_no;
    }

    public String getVpp_id() {
        return vpp_id;
    }

    public void setVpp_id(String vpp_id) {
        this.vpp_id = vpp_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public Boolean getPayment_capture() {
        return payment_capture;
    }

    public void setPayment_capture(Boolean payment_capture) {
        this.payment_capture = payment_capture;
    }

    public int getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(int amount_paid) {
        this.amount_paid = amount_paid;
    }

    public String[] getNotes() {
        return notes;
    }

    public void setNotes(String[] notes) {
        this.notes = notes;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public int getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(int amount_due) {
        this.amount_due = amount_due;
    }

    public String getId() {
        return id;
    }

    public void setId(String order_id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getSecret_id() {
        return secret_id;
    }

    public void setSecret_id(String secret_id) {
        this.secret_id = secret_id;
    }

    public String getPan_number() {
        return pan_number;
    }

    public void setPan_number(String pan_number) {
        this.pan_number = pan_number;
    }

    public String getMobile_app_version() {
        return mobile_app_version;
    }

    public void setMobile_app_version(String mobile_app_version) {
        this.mobile_app_version = mobile_app_version;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public Order(int sr_no, String vpp_id, String amount, String currency, String receipt, Boolean payment_capture, int amount_paid, String[] notes, int created_at, int amount_due, String id, String entity, String offer_id, String status, int attempts, int transaction_id, String key_id, String secret_id, String pan_number, String mobile_app_version, String created_by, String created_date, String updated_by, String updated_date) {
        this.sr_no = sr_no;
        this.vpp_id = vpp_id;
        this.amount = amount;
        this.currency = currency;
        this.receipt = receipt;
        this.payment_capture = payment_capture;
        this.amount_paid = amount_paid;
        this.notes = notes;
        this.created_at = created_at;
        this.amount_due = amount_due;
        this.id = id;
        this.entity = entity;
        this.offer_id = offer_id;
        this.status = status;
        this.attempts = attempts;
        this.transaction_id = transaction_id;
        this.key_id = key_id;
        this.secret_id = secret_id;
        this.pan_number = pan_number;
        this.mobile_app_version = mobile_app_version;
        this.created_by = created_by;
        this.created_date = created_date;
        this.updated_by = updated_by;
        this.updated_date = updated_date;
    }

    public Order() {
        super();
    }

    @Override
    public String toString() {
        return "Order{" +
                "sr_no=" + sr_no +
                ", vpp_id='" + vpp_id + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", receipt='" + receipt + '\'' +
                ", payment_capture=" + payment_capture +
                ", amount_paid=" + amount_paid +
                ", notes=" + Arrays.toString(notes) +
                ", created_at=" + created_at +
                ", amount_due=" + amount_due +
                ", id='" + id + '\'' +
                ", entity='" + entity + '\'' +
                ", offer_id='" + offer_id + '\'' +
                ", status='" + status + '\'' +
                ", attempts=" + attempts +
                ", transaction_id=" + transaction_id +
                ", key_id='" + key_id + '\'' +
                ", secret_id='" + secret_id + '\'' +
                ", pan_number='" + pan_number + '\'' +
                ", mobile_app_version='" + mobile_app_version + '\'' +
                ", created_by='" + created_by + '\'' +
                ", created_date='" + created_date + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", updated_date='" + updated_date + '\'' +
                '}';
    }
}
