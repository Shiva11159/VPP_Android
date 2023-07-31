package com.application.vpp.Interfaces;

import com.application.vpp.Datasets.BranchLocatorDetails;

public interface CallBack {
     void getDetails(String branchname,String contactperson,String emailid,String mobileno);
     void  getReason(String reason,String name,String leadNo,String updatedDate);
}
