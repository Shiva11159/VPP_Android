package com.application.vpp.Datasets;

import java.util.Objects;

/**
 * Created by bpandey on 15-06-2018.
 */

public class ClientlistData {
    public String getClientCode() {
        return ClientCode;
    }

    public void setClientCode(String clientCode) {
        ClientCode = clientCode;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getAccountOpenedDate() {
        return AccountOpenedDate;
    }

    public void setAccountOpenedDate(String accountOpenedDate) {
        AccountOpenedDate = accountOpenedDate;
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

    public String getLeadDate() {
        return LeadDate;
    }

    public void setLeadDate(String leadDate) {
        LeadDate = leadDate;
    }

    public String getNotInterestedreason() {
        return notInterestedreason;
    }

    public void setNotInterestedreason(String notInterestedreason) {
        this.notInterestedreason = notInterestedreason;
    }

    public ClientlistData(String MobileNo,String clientCode, String clientName, String productName, String accountOpenedDate, String leadNo, String customerName, String leadDate, String notInterestedreason) {
        ClientCode = clientCode;
        ClientName = clientName;
        ProductName = productName;
        AccountOpenedDate = accountOpenedDate;
        LeadNo = leadNo;
        CustomerName = customerName;
        LeadDate = leadDate;
        this.notInterestedreason = notInterestedreason;
        this.MobileNo = MobileNo;
    }

    public String ClientCode;
    public String ClientName;
    public String ProductName;
    public String AccountOpenedDate;
    public String LeadNo;
    public String CustomerName;
    public String LeadDate;
    public String notInterestedreason;

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String MobileNo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientlistData that = (ClientlistData) o;
        return Objects.equals(ClientCode, that.ClientCode) &&
                Objects.equals(ClientName, that.ClientName) &&
                Objects.equals(ProductName, that.ProductName) &&
                Objects.equals(AccountOpenedDate, that.AccountOpenedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ClientCode, ClientName, ProductName, AccountOpenedDate, LeadNo, CustomerName, LeadDate, notInterestedreason);
    }
}
