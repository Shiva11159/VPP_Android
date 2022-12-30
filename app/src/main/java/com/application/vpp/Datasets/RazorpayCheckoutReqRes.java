package com.application.vpp.Datasets;

public class RazorpayCheckoutReqRes {
    private String order_id;
    private String vpp_id;
    private String request;

    private String razorpay_order_id;
    private String razorpay_payment_id;
    private String payment_status;

    private String created_by;
    private String created_date;
    private String updated_by;
    private String updated_date;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getVpp_id() {
        return vpp_id;
    }

    public void setVpp_id(String vpp_id) {
        this.vpp_id = vpp_id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRazorpay_order_id() {
        return razorpay_order_id;
    }

    public void setRazorpay_order_id(String razorpay_order_id) {
        this.razorpay_order_id = razorpay_order_id;
    }

    public String getRazorpay_payment_id() {
        return razorpay_payment_id;
    }

    public void setRazorpay_payment_id(String razorpay_payment_id) {
        this.razorpay_payment_id = razorpay_payment_id;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
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

    public RazorpayCheckoutReqRes(String order_id, String vpp_id, String request, String razorpay_order_id, String razorpay_payment_id, String payment_status, String created_by, String created_date, String updated_by, String updated_date) {
        this.order_id = order_id;
        this.vpp_id = vpp_id;
        this.request = request;
        this.razorpay_order_id = razorpay_order_id;
        this.razorpay_payment_id = razorpay_payment_id;
        this.payment_status = payment_status;
        this.created_by = created_by;
        this.created_date = created_date;
        this.updated_by = updated_by;
        this.updated_date = updated_date;
    }

    public RazorpayCheckoutReqRes() {
        super();
    }

    @Override
    public String toString() {
        return "RazorpayCheckoutReqRes{" +
                "order_id='" + order_id + '\'' +
                ", vpp_id='" + vpp_id + '\'' +
                ", request='" + request + '\'' +
                ", razorpay_order_id='" + razorpay_order_id + '\'' +
                ", razorpay_payment_id='" + razorpay_payment_id + '\'' +
                ", payment_status='" + payment_status + '\'' +
                ", created_by='" + created_by + '\'' +
                ", created_date='" + created_date + '\'' +
                ", updated_by='" + updated_by + '\'' +
                ", updated_date='" + updated_date + '\'' +
                '}';
    }
}
