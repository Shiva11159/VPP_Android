package com.application.vpp.Datasets;

/**
 * Created by bpandey on 28-06-2018.
 */

public class InProcessDataset {
    public String LeadDate;
    public String ProspectName;
    public String ProductName;

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

    public  InProcessDataset(String LeadDate, String ProspectName, String ProductName){

        this.LeadDate = LeadDate;
        this.ProspectName = ProspectName;
        this.ProductName = ProductName;

    }
}
