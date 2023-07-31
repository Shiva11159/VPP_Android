package com.application.vpp.Datasets;

public class NotInterestedData {
    String Status;
    String MobileNo;
    String notInterestedreason;
    String AccountOpenedDate;
    String ProductName;
    String LeadUpdateDate;
    String VPPPAN;
    String LeadNo;
    String ClientName;
    String ClientCode;
    String CustomerName;
    String LeadDate;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getNotInterestedreason() {
        return notInterestedreason;
    }

    public void setNotInterestedreason(String notInterestedreason) {
        this.notInterestedreason = notInterestedreason;
    }

    public String getAccountOpenedDate() {
        return AccountOpenedDate;
    }

    public void setAccountOpenedDate(String accountOpenedDate) {
        AccountOpenedDate = accountOpenedDate;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getLeadUpdateDate() {
        return LeadUpdateDate;
    }

    public void setLeadUpdateDate(String leadUpdateDate) {
        LeadUpdateDate = leadUpdateDate;
    }

    public String getVPPPAN() {
        return VPPPAN;
    }

    public void setVPPPAN(String VPPPAN) {
        this.VPPPAN = VPPPAN;
    }

    public String getLeadNo() {
        return LeadNo;
    }

    public void setLeadNo(String leadNo) {
        LeadNo = leadNo;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientCode() {
        return ClientCode;
    }

    public void setClientCode(String clientCode) {
        ClientCode = clientCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getLeadDate() {
        return LeadDate;
    }

    public void setLeadDate(String leadDate) {
        LeadDate = leadDate;
    }

    public String getBranchCode() {
        return BranchCode;
    }

    public void setBranchCode(String branchCode) {
        BranchCode = branchCode;
    }

    public NotInterestedData(String status, String mobileNo, String notInterestedreason, String accountOpenedDate, String productName, String leadUpdateDate, String VPPPAN, String leadNo, String clientName, String clientCode, String customerName, String leadDate, String branchCode) {
        Status = status;
        MobileNo = mobileNo;
        this.notInterestedreason = notInterestedreason;
        AccountOpenedDate = accountOpenedDate;
        ProductName = productName;
        LeadUpdateDate = leadUpdateDate;
        this.VPPPAN = VPPPAN;
        LeadNo = leadNo;
        ClientName = clientName;
        ClientCode = clientCode;
        CustomerName = customerName;
        LeadDate = leadDate;
        BranchCode = branchCode;
    }

    String BranchCode;
}
