package com.application.vpp.Const;

import com.application.vpp.ClientServer.TCPClient;

/**
 * Created by bpandey on 08-06-2018.
 */

public class Const {

    public static final int MSGLOGIN =55;
    public static final int MSGSIGNUP = 2;
    public static final int MSGDASHBOARD = 3;
    public static final int MSGUpdateEMailMobile = 4;
    public static final int MSGADDLEAD = 36;
    public static final int MSGINPROCESS = 5;
    public static final int MSGVERIFYOTP = 6;
    public static final int MSGVERIFYIMEI = 7;
    public static final int MSGPRODUCTMASTER = 8;
    public static final int MSGSOCKETCONNECTED = 9;
    public static final int MSGVERIFYIMEILOGIN = 10;
    public static final int MSGFETCHLEADDETAILS = 11;
    public static final int MSGFETCHBROKERAGELIST = 12;
    public static final int MSGFETCHCLIENTLIST = 13;
    public static final int MSGFETCHDASHBOARD = 14;
    public static final int MSGFETCHDASHBOARDDesign = 70;
    public static final int MSGFETCHLEADDETAILREPORT = 17;
    public static final int MSGFETCHLEADDETAILCLIENT = 18;
    public static final int MSGFETCHLEADDETAILDEAD = 19;
    public static final int MSGFETCHLEADINPROCESS = 20;
    public static final int MSGFETCHLEADREJECTED = 21;
    public static final int MSGFETCHVERSION = 22;
    public static final int MSGFETCHPAYOUT = 23;
    public static final int MSGFETCHDOCSTAT = 24;
    public static final int MSGFETCHVPPDETAILSONLOGIN = 26;
    public static final int MSGUPDATEPROFILE= 27;
    public static final int MSGUPDATCONTACT= 28;
    public static  final  int MSGUPDATEPAYMENTSTATUS=29;
    public static  final  int MSGAUTHENTICATE=30;

    public static  final  int MSGAUTHENTICATERESEND=31;
    public static final int MSGCHECKPAN = 32;
    public static final int MSGSAVEPAN = 33;
    public static final int MSGUPDATENAMEBANK = 34;
    public static final int MSGSIGNUP2= 35;
    public static final int MSGQUERY= 37;
    public static final int MSGCALLBACK= 39;
    public static final int MSGBRANCHLOCATOR= 40;
    public static  final  int MSGUPDATERESEND=41;
    public static  final  int MSGQUERYLIST=42;
    public static  final  int MSGSAVEBANKDETAILS=44;

    public static  final  int MSGMONTHLYEARNING=45;
    public static  final  int MONTHLYLEAD=46;

    public static final int MSGORDERBOOK= 48;

    public static final int MSGVERIFYVPP = 51;
    public static final int MSGFETCHVPPDETAILS = 52;
    public static final int MSGVERIFYOTPPAN= 53;

    public static final int MSGRESENDOTP = 54;
    public static final int MSGCREATEORDEROBJ = 56;
    public static final int MSGVALIDATESIGNATURE = 57;
    public static final int MSGSAVECHECKOUT = 58;
    public static final int MSGMOUNT = 59;
    public static  final  int MSGSAVEGPAYRESPONSE=60;
    public static final int MSGTECHPROCREQ = 62;
    public static final int MSGSUBPARTNERCATEGORY = 61;
    public static final int MSGTECHPROCRESP =63;
    //public static final int MSGNotificationtoken =00;
    public static final int MSGCallbackdetails =64;
    public static final int MSGCallPromocode =65;
    public static final int MSG_GST =66;
    public static final int MSG_StateCity =67;
    public static final int MSG_LOGS =68;

    public static final int MSGSOCKETCONNECTEDSPLASH = 101;
    public static final int MSGSOCKETCONNECTEDADDLEAD = 102;
    public static final int MSGSOCKETCONNECTEDINPROCESS = 103;
    public static final int MSGSOCKETCONNECTEDMYLEADS = 104;
    public static final int MSGSOCKETCONNECTEDREJECTED = 105;
    public static final int MSGSOCKETCONNECTEDNOTINT = 106;
    public static final int MSGSOCKETCONNECTEDCLIENT = 107;
    public static final int MSGBANKVERIFICATION = 71;
    public static final int MSGPERSONALIZED_LINK_FOR_ACCOUNT_OPENING = 72;
    public static final int MSGPERSONALIZED_LINK_FOR_ACCOUNT_OPENING_NEW = 78;

    public static final int MSG_UPDATEACCOUNT_IFSCCODE = 73;

    // new method created for individual otp send replaced by msgcode 2 ..
    public static final int MSG_SIGNUP_OTP_NEW = 74;

    //    new method created for individual otp send  public static  final  int MSGAUTHENTICATE=30;
    public static final int MSGAUTHENTICATERESENDOTP = 75;

    public static final int MSG_POSTREVENUESHARING = 76;


    public static String simNumber = "12345678";
    public static String deviceID = "deviceID";
    public static String simID = "simID";
    public static String simID1 = "simID1";

    public static String tokenID = "TokenID";
    public static String reg_token = "Reg_Token";
    public static String vppDetails = "VPPDETAILS";
    public static String vpp_name = "name";
    public static String vpp_mobile = "mobile";
    public static String vpp_email = "email";
    public static String vpp_city = "city";
    public static String vpp_id = "vppid";
    public static String vpp_pan = "vpp_pan";
    public static String SOCKETCONNECTION = "SOCKETCONNECTION";
    public static boolean isSocketConnected = false;
    public static boolean isServerConnected = false;
    public static boolean dismiss = false;

    public static String today = "DATE";
    public static final String Check1Checked = "Ventura Products Explained";
    public static final String Check1UnChecked = "Ventura Products Not Explained";
    public static final String Check2Checked = "Informed about Ventura Officials calling for Account";
    public static final String Check2UnChecked = "Not Informed about Ventura Officials calling for Account";
    public static final String checkConnection = "Please check your internet connection";

//    public static final String URL_PANVerifyClient = "https://ekyc.ventura1.com:443/EKYCServer/VerifyPANBipin";
    public static final String URL_PANVerifyClient = "https://ekyc.ventura1.com:51530/EKYCServer/VerifyPANBipin";
    public static final String URL_VerifyAccount_Retrofit = "https://vpp.ventura1.com/";

    public static String vppOtpDetails = "VPPOTPDETAILS";
    public static String vppAddressDetails = "VPPADDRESSDETAILS";
    public static String vppBankDetails = "VPPBANKDETAILS";

    public static String fmlName = "fmlName";
    public static String fName = "fName";
    public static String mName = "mName";
    public static String lName = "lName";
    public static String FromUpdate = "Update";
    public static String FromProfile = "FromProfile";
    public static String FromPhotoVideoSignature = "FromPhotoVideoSignature";
    public static String FromPanLoginMob = "FromPanLoginMob";
    public static String FromPanLoginEMail = "FromPanLoginEMail";
    public static String MobileVerified = "MobileVerified";
    public static String EmailVerified = "EmailVerified";

    //added by shiva ..
    public static String from = "from";
    public static String doc = "doc";
    public static String payment = "payment";
    public static String bankstatus = "bankstatus";
    public static String adharstatus = "adharstatus";
    public static String panstatus = "panstatus";

    public static String selfiestatus = "selfiestatus";
    public static String videostatus = "videostatus";
    public static String StateMaster = "StateMaster";
    public static String CityMaster = "CityMaster";

    public static boolean g_b_isInternetslow = false;

    public static int timeout = 0;  //added by pravin 05.03.2020
    public static String imei = "imei"; //added by pravin 05.03.2020

    public static TCPClient tcpClient = null;

    // public static String SERVER_IP = "vpp.ventura1.com";  //VPP SERVER
    // public static String SERVER_IP = "43.242.213.117"; // HRDM SERVER
    //public static final int SERVER_PORT = 8888;  // LIVE PORT

    //Live app on new port


//#user=Peng
//#database=vpp
//#pwd=Vpp@54321
//            #dbip=172.16.102.161:3306
//            #dbip=localhost:3306
//
//
//    user=root
//            database=vpp
//    pwd=root
//            dbip=172.16.102.55:3306


    //
//    public static String SERVER_IP = "vpp.ventura1.com";
    public static String SERVER_IP = "43.242.213.117";  // main ip live ...

    //
//        public static final int SERVER_PORT = 2020 ; //shiva PORT testing..
    public static final int SERVER_PORT = 7778 ; //shiva PORT production.

    //    public static String SERVER_IP = "192.168.43.65"; //Local Server
    //public static final int SERVER_PORT = 8081;     //Local PORT

    //public static final String PUBLICKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyzc9kBvFRFB+PwBJceFNHdvXScht9qCvOHyH6TZavVadgjxYFJAP5HQBSK/qf8zDzrwCmU2DKoTTkTtBBxr1U/B2YCAh6w/HEaY07BjSg+S4MphMTrGj5t1PwZDrbs9E6RePs+9UHN6W5Rk73le2wpciOaXT5WlEw3QJEszmnuL18rKtQ0XJpxkkpmVxA+tmNUsmgqBFlZw5DnQUVGZ8L+bwEsTHhTtsb0Uigo2aiowQxeZXCiEQaKkPhzJ1WSh6m2X+9PfHFsnDt4hZ0NzxOeOTOvz6/Qm3uwk87ANl934ul90xGe+y0BTAJnwdWqMLn72CVTz38/LpXw04kWAgAwIDAQAB";
    //public static final String PRIVATEKEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDLNz2QG8VEUH4/AElx4U0d29dJyG32oK84fIfpNlq9Vp2CPFgUkA/kdAFIr+p/zMPOvAKZTYMqhNORO0EHGvVT8HZgICHrD8cRpjTsGNKD5LgymExOsaPm3U/BkOtuz0TpF4+z71Qc3pblGTveV7bClyI5pdPlaUTDdAkSzOae4vXysq1DRcmnGSSmZXED62Y1SyaCoEWVnDkOdBRUZnwv5vASxMeFO2xvRSKCjZqKjBDF5lcKIRBoqQ+HMnVZKHqbZf7098cWycO3iFnQ3PE545M6/Pr9Cbe7CTzsA2X3fi6X3TEZ77LQFMAmfB1aowufvYJVPPfz8ulfDTiRYCADAgMBAAECggEBAIwda3ooW6yNn25LAVje0nAq+1bpNkCdbBMTchZvnsL7rKB2gPi0l/CZt6TpflNd2Qh7A4O2OSEg8kspwt1mTg7xNn1yylAr0cMuiUDcoJHGKubfsYV04CC3KV26UUpW6RmaYD/dXW6GXx5ZdrkIW3/x+uwqlV6RZZMqN3NaCg00D36VqfQeXqeZa2Y4a7AvWCgM+62rZS1mcCEw+oNwJJ/kr6T0D3DOrHABoLYTgLNezN1FRd4+7XRVMYGovYETDbg9S1BT9lltXh3xnbaR8YjPY0uGhLrf6VsxT+5bTbvS+Q6zhmB62zXh62FVNAQ9fi7lo0E62kG1csYnZsY1vMkCgYEA5MItcGBowMVvejFSV0yt0lDSZZPu/qb9mDc9kvBviSHCx69W48PPc/t6CqufibUZBh4huStP7FXjsQJX+z8BugncTXBwCFY+AjurFiqHNuH9Ivfj9ZMHXCJT/aYTmsV1vRGpGUyCWWZeUvztBQ3I44jXuRg7VMyAUuR5B5Bm/gUCgYEA42phNuqIT4uoHoUHfKyz60/re+4xipgw+PYpfmnwGBEV4MfMVxAnBZkFG51HNhQYDJY6Mz1t7OcX2KLmqYWruvsWvv9o+Qj2RFLAndXYronFy1DnvhXNMWC1Or4nbUqmjARpq3JvSGyOTY3PQ6p5jHi0hPXWdAsmn8Wx2CDH/GcCgYEAjss66/PyT6qurHrPKFlgA3GZ8/hE7ka08+g/klJtfQ9K87TaqzBWddKbWr/IxX/7D9uNN3X23NK8X4jT/KxW2pc/IkO2Wy3iZShz4lFHuBWXGtXcQTW3GWcAQMwDXtA1b4DvqjAdCcshw/HHzijNpmKMLikXGBbTX2rotbUI5TUCgYAryO2SrzEzb4Yc6iv42x+A7NA/+ipbrtWu+WYfyrHJUHDP6Gf1U0zQDRgoqu1tAg1nTv1BpFAnzo6OzT5v4SahR3qnp3FEkW8o1G31dZjUcf49GQSlWnk5So9aEgZxSnAqgSb/NXcrkIsNb4I6WvYwD4ViHBsAGK2FEYEeRaF7fQKBgHbF8rS34rtNS5KXjzLx9C6zwNwkJmLylxDrVegoHROHBcvfhTWx4RBT5FFUpnkyQ1hFatPP/iLGBoS+eqgCL0zn6MZ3VBH4DpUMOC0KLyJdpI7P0+mHwkaFZ00XZ9IBw8mYfBujewibtkPIEEAsjlJ/2Jm4XBZ6sauU8Q1+fUMD";

    public final static String ERR_MSG_SLOW_CONNECTION="Your internet connection is too slow, it may take some time";
    public final static String ERR_SERVER_CONNECTION = "Could not connect to server. Please check your internet connection";
    public static final String Psn_lnk_acct_opn = "accnt_opn_link";

    public static int HTTP_RESULT = 0;
    public static String PAN_Error_Msg = "";

}