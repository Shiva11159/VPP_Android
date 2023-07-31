//package com.application.vpp.NetworkCall;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.application.vpp.Interfaces.Uploadfilecallback;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.URL;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//
////import static com.application.vpp.Activity.PhotoVideoSignatureActivity.struploadProfile;
////import static com.application.vpp.Activity.PhotoVideoSignatureActivity.struploadSignature;
////import static com.application.vpp.Activity.PhotoVideoSignatureActivity.struploadVideo;
//import static com.application.vpp.activity.UploadDocScreen.struploadadharback;
//import static com.application.vpp.activity.UploadDocScreen.struploadadharfront;
//import static com.application.vpp.activity.UploadDocScreen.struploadcheckback;
//import static com.application.vpp.activity.UploadDocScreen.struploadcheckfront;
//import static com.application.vpp.activity.UploadDocScreen.struploadpan;
//
//public class UploadFile extends AsyncTask<String, String, String> {
//    public static int status1;
//    Context context;
//    ProgressDialog progressDialog;
//    Uploadfilecallback uploadfilecallback;
//    String type;
//    public UploadFile(Context context, Uploadfilecallback uploadfilecallback,String Type) {
//        this.context = context;
//        this.uploadfilecallback = uploadfilecallback;
//        this.type = Type;
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//        try {
//            SSLContext sc = SSLContext.getInstance("SSL");
//            try {
//                sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
//            } catch (KeyManagementException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            Log.e("params[0]", type);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("vpp_id", params[2]);
//            jsonObject.put("pan", params[0]);
//            jsonObject.put("addressproof", params[1]);
//            String JsonResponse = null;
//            String JsonDATA = jsonObject.toString();
//            // HttpsURLConnection urlConnection = null;
//            HttpsURLConnection urlConnection = null;
//            BufferedReader reader = null;
//            //  URL url = new URL("http://172.16.11.111:8084/SaveImgDocs/webresources/img/");
//
////              URL url = new URL("http://172.16.11.113:8084/SaveImgDocs/webresources/img/");
//            URL url = new URL("https://vpp.ventura1.com/SaveImgDocs/webresources/img/");      ///live
////            URL url = new URL("https://vpp.ventura1.com/SaveImgDocs_Uat/webresources/img/");   ///testing
//            urlConnection = (HttpsURLConnection) url.openConnection();
//            urlConnection.setDoOutput(true);
//            // is output buffer writter
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Content-Type", "application/json");
//            urlConnection.setRequestProperty("Accept", "application/json");
//            urlConnection.setSSLSocketFactory(SSLUtil.getSSLSocketFactory(SSLUtil.getFromRaw(context),"SSL"));
//
//          //  urlConnection.setSSLSocketFactory(sc.getSocketFactory());
////set headers and method
//
//            OutputStream os = urlConnection.getOutputStream();
//            os.write(JsonDATA.getBytes());
//            os.flush();
//            os.close();
//
//            InputStream inputStream = urlConnection.getInputStream();
////input stream
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//            String inputLine;
//            while ((inputLine = reader.readLine()) != null)
//                buffer.append(inputLine + "\n");
//            if (buffer.length() == 0) {
//                // Stream was empty. No point in parsing.
//                return null;
//            }
//            JsonResponse = buffer.toString();
//            return JsonResponse;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    @Override
//    protected void onPreExecute() {
//    }
//    @Override
//    protected void onPostExecute(String JsonResponse) {
//      //  Log.e("JsonResponse", JsonResponse);
//       // Log.e("typePOst", type);
//        JSONObject jsonObject1 = null;
//        try {
//            jsonObject1 = new JSONObject(JsonResponse);
//            String status = jsonObject1.getString("status");
////            status1 = Integer.parseInt(status);
//
//            if (type.equalsIgnoreCase(struploadadharfront)){
//                uploadfilecallback.Uploadstatus(struploadadharfront,status);
//            }else if (type.equalsIgnoreCase(struploadadharback)){
//                uploadfilecallback.Uploadstatus(struploadadharback,status);
//            }else if (type.equalsIgnoreCase(struploadpan)){
//                uploadfilecallback.Uploadstatus(struploadpan,status);
//            }else if (type.equalsIgnoreCase(struploadcheckfront)){
//                uploadfilecallback.Uploadstatus(struploadcheckfront,status);
//            }else if (type.equalsIgnoreCase(struploadcheckback)) {
//                uploadfilecallback.Uploadstatus(struploadcheckback, status);
//            }
////            else if (type.equalsIgnoreCase(struploadVideo)){
////                uploadfilecallback.Uploadstatus(struploadVideo,status);
////            }else if (type.equalsIgnoreCase(struploadSignature)){
////                uploadfilecallback.Uploadstatus(struploadSignature,status);
////            }else if (type.equalsIgnoreCase(struploadProfile)){
////                uploadfilecallback.Uploadstatus(struploadProfile,status);
////            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
////response data
//
//    }
//
//
//}
