package com.application.vpp.Datasets;

/**
 * Created by bpandey on 28-06-2018.
 */

public class LeadDetailReportDataset {

    public String LeadDate ;
    public String LeadNo;
    public String CustomerName;
    public String BranchCode ;
    public String VPPPAN;

    public String getLeadDate() {
        return LeadDate;
    }

    public void setLeadDate(String leadDate) {
        LeadDate = leadDate;
    }

    public String getLeadNo() {
        return LeadNo;
    }

    public void setLeadNo(String leadNo) {
        LeadNo = leadNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getBranchCode() {
        return BranchCode;
    }

    public void setBranchCode(String branchCode) {
        BranchCode = branchCode;
    }

    public String getVPPPAN() {
        return VPPPAN;
    }

    public void setVPPPAN(String VPPPAN) {
        this.VPPPAN = VPPPAN;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getClientCode() {
        return ClientCode;
    }

    public void setClientCode(String clientCode) {
        ClientCode = clientCode;
    }

    public String getAccountOpenedDate() {
        return AccountOpenedDate;
    }

    public void setAccountOpenedDate(String accountOpenedDate) {
        AccountOpenedDate = accountOpenedDate;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public LeadDetailReportDataset(String leadDate, String leadNo, String customerName, String branchCode, String VPPPAN, String userName, String response, String clientCode, String accountOpenedDate, String clientName, String status, String productName, String mobileNo) {
        LeadDate = leadDate;
        LeadNo = leadNo;
        CustomerName = customerName;
        BranchCode = branchCode;
        this.VPPPAN = VPPPAN;
        UserName = userName;
        Response = response;
        ClientCode = clientCode;
        AccountOpenedDate = accountOpenedDate;
        ClientName = clientName;
        Status = status;
        ProductName = productName;
        MobileNo = mobileNo;
    }

    public String UserName;
    public String Response ;
    public String ClientCode ;
    public String AccountOpenedDate ;
    public String ClientName ;
    public String Status ;
    public String ProductName ;
    public String MobileNo ;



}
