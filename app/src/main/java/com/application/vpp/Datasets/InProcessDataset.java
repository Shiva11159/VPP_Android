package com.application.vpp.Datasets;

/**
 * Created by bpandey on 28-06-2018.
 */

public class InProcessDataset {
    public String LeadDate;
    public String ProspectName;
    public String RejectionDate;

    public String getRejectionDate() {
        return RejectionDate;
    }

    public void setRejectionDate(String rejectionDate) {
        RejectionDate = rejectionDate;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public InProcessDataset(String prospectName, String rejectionDate, String reason, String productName, String mobileNo) {
        ProspectName = prospectName;
        RejectionDate = rejectionDate;
        Reason = reason;
        ProductName = productName;
        MobileNo = mobileNo;
    }

    public String Reason;


    public String ProductName;

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String MobileNo;

    public String getLeadDate() {
        return LeadDate;
    }

    public void setLeadDate(String leadDate) {
        LeadDate = leadDate;
    }

    public String getProspectName() {
        return ProspectName;
    }

    public void setProspectName(String prospectName) {
        ProspectName = prospectName;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

//    public  InProcessDataset(String LeadDate, String ProspectName, String ProductName){
//
//        this.LeadDate = LeadDate;
//        this.ProspectName = ProspectName;
//        this.ProductName = ProductName;
//
//    }
}
