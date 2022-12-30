package com.application.vpp.NetworkCall;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.application.vpp.Datasets.InserSockettLogs;
import com.application.vpp.Interfaces.APiValidateAccount;
import com.application.vpp.SharedPref.SharedPref;
import com.application.vpp.activity.SplashScreen;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadLogs {

   public static void  InsertLogsMethod(JSONArray jsonArray, APiValidateAccount apiService1, ArrayList<InserSockettLogs>inserSockettLogsArrayList, Context context) {
        Log.e("jsonArray", String.valueOf(jsonArray.length()));
        Log.e("jsonArraySIZE", jsonArray.toString());

//        JSONArray js = null;
//        try {
//            js = dbh.createJsonArray();
//            Log.e("LOGS_", js.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JSONObject paramObject = new JSONObject();
//        try {
//            paramObject.put("ispojo", js.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(jsonObject);

//        Log.e("logsRequest", jsonArray.toString());

        Call<JsonObject> validateSignature = apiService1.SendSocketLogs(jsonArray.toString());
        validateSignature.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String err = "";

                if (response.isSuccessful()) {
                    SharedPref.clearLogsArrayList(inserSockettLogsArrayList, context);
//                    Log.e("DASHBOARD_success", "" + response.body());
//                    Log.e("DASHBOARD_success", "" + response.toString());
                    Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show();


                } else {
                    switch (response.code()) {
                        case 404:
//                            Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
                            err = "Server Not Found";
                            break;
                        case 500:
//                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
                            err = "Server Unavailable";
                            break;
                        case 503:
//                            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
                            err = "Server Overloaded try after sometime";
                            break;
                        default:
                            err = String.valueOf(response.code());
                            err = "Something went wrong try again.";
//                            Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
                Toast.makeText(context, err, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log.e("DASHBOARD_failure", "   throwable===" + t.getMessage());
            }
        });


    }

}
