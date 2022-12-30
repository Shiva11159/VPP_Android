package com.application.vpp.Datasets;

/**
 * Created by bpandey on 14-06-2018.
 */

public class LeadListDataset {

   public int leadnumber;
    public String prospect;
    public String product;
    public String response;

    public LeadListDataset( int leadnumber, String prospect, String product, String response){
        this.leadnumber = leadnumber;
        this.prospect = prospect;
        this.product = product;
        this.response = response;
    }


}
