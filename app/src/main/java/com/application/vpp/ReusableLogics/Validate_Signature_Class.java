package com.application.vpp.ReusableLogics;

public class Validate_Signature_Class {

    public String getRazorpay_payment_id() {
        return razorpay_payment_id;
    }

    public void setRazorpay_payment_id(String razorpay_payment_id) {
        this.razorpay_payment_id = razorpay_payment_id;
    }

    public String getRazorpay_order_id() {
        return razorpay_order_id;
    }

    public void setRazorpay_order_id(String razorpay_order_id) {
        this.razorpay_order_id = razorpay_order_id;
    }

    public String getSignature_by_checkout() {
        return signature_by_checkout;
    }

    public void setSignature_by_checkout(String signature_by_checkout) {
        this.signature_by_checkout = signature_by_checkout;
    }

    String razorpay_payment_id,	razorpay_order_id, signature_by_checkout;

    public Validate_Signature_Class(String razorpay_payment_id, String razorpay_order_id, String signature_by_checkout) {
        super();
        this.razorpay_payment_id = razorpay_payment_id;
        this.razorpay_order_id = razorpay_order_id;
        this.signature_by_checkout = signature_by_checkout;
    }

    @Override
    public String toString() {
        return "Validate_Signature_Class [razorpay_payment_id=" + razorpay_payment_id + ", razorpay_order_id="
                + razorpay_order_id + ", signature_by_checkout=" + signature_by_checkout + "]";
    }

}
