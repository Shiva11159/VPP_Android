package com.application.vpp.Datasets;

public class SubPatnerActivityModel {

    String ClientName;
    String AccountOpenedDate;

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
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

    public String getClientCode() {
        return ClientCode;
    }

    public void setClientCode(String clientCode) {
        ClientCode = clientCode;
    }

    public SubPatnerActivityModel(String clientName, String accountOpenedDate, String productName, String clientCode) {
        ClientName = clientName;
        AccountOpenedDate = accountOpenedDate;
        ProductName = productName;
        ClientCode = clientCode;
    }

    String ProductName;
    String ClientCode;
}
