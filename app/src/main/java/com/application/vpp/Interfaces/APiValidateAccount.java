package com.application.vpp.Interfaces;

import com.application.vpp.Datasets.BankValidateData;
import com.application.vpp.Datasets.Order;
import com.application.vpp.Datasets.RazorpayCheckoutReqRes;
import com.application.vpp.Datasets.UploadFileResponse;
import com.application.vpp.ReusableLogics.Validate_Signature_Class;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APiValidateAccount {


//   /* @Headers("Content-Type: application/json")*/
//    //@POST("MobileController")
//    @POST("PennyDropApi/penny")
//    Call<BankValidateData> validateBankAcc(@Body String jsonBody);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("PennyDropApi/penny")
    Call<BankValidateData> getbackdetails(@Body JsonObject bean);

    @POST("createOrderObj")
    Call<Order> createOrder(@Body Order order);

    @POST("Validate_Signature")
    Call<String> Validate_Signature(@Body Validate_Signature_Class validate_signature_class);

    @POST("SaveCheckout")
    Call<String> SaveCheckout(@Body RazorpayCheckoutReqRes checkoutReqRes);


    @Multipart
    @POST("uploadFile")
    Call<UploadFileResponse> UploadVideo(@Part MultipartBody.Part file, @Part("vpp_id") RequestBody vpp_id, @Part("proof_type") RequestBody proof_type);

    @POST("insertsocketlogs")
    Call<JsonObject> SendSocketLogs(@Query("ispojo") String insertLogs);


    @Headers("OCRAPIKey: OPSID1:e5EPWugygD_Wsdfsfsdfsdsc2wWHoC1D")
//    @FormUrlEnzcoded
    @POST("OPS/GetEMPBranchCode")
    Call<JsonObject> SendOps(@Body String vppid);
}
