package com.application.vpp.Datasets;

public class BankValidateData {

    private String txnid;
    private String nwrespcode;
    private String nwrespmessg;
    private String nwtxnrefid;
    private String c_name;
    private String response;
    private  String respcode;
    private  String acc_no;
    private  String ifsc;

    public String getTxnid() {
        return txnid;
    }

    public String getNwrespcode() {
        return nwrespcode;
    }

    public String getNwrespmessg() {
        return nwrespmessg;
    }

    public String getNwtxnrefid() {
        return nwtxnrefid;
    }

    public String getC_name() {
        return c_name;
    }

    public String getResponse() {
        return response;
    }

    public String getRespcode() {
        return respcode;
    }

    public String getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(String acc_no) {
        this.acc_no = acc_no;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public void setNwrespcode(String nwrespcode) {
        this.nwrespcode = nwrespcode;
    }

    public void setNwrespmessg(String nwrespmessg) {
        this.nwrespmessg = nwrespmessg;
    }

    public void setNwtxnrefid(String nwtxnrefid) {
        this.nwtxnrefid = nwtxnrefid;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }
}
