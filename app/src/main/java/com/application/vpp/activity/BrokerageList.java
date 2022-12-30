package com.application.vpp.activity;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.application.vpp.Adapters.BrokerageListAdapter;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.BrokerageListData;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Logics;
import com.application.vpp.Utility.AlertDialogClass;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BrokerageList extends NavigationDrawer implements RequestSent{

    RecyclerView listBrokerage;
    ArrayList<BrokerageListData> brokerageListArrayList;
    public static Handler handlerLeadList;
    static Gson gson;

    LinearLayout mainLayout;
//    ProgressDialog ringProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_brokrage_list,mDrawerLayout);
        mainLayout=findViewById(R.id.mainLayout);

/*
        handlerLeadList = new BrokerageList.ViewHandler();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
*/

//        ringProgressDialog = ProgressDialog.show(BrokerageList.this, "Please wait ...", "Loading Your Data ...", true);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));


        AlertDialogClass.PopupWindowShow(BrokerageList.this,mainLayout);


        try {
//            String imei;
//            String simID;
//            String osversion="normal";
//            imei = Logics.getDeviceID(this);
//            simID = Logics.getSimId(this);
//            if (android.os.Build.VERSION.SDK_INT>=29){
//                imei=Logics.getTokenID(this);
//                osversion="oxy";
//                simID="12345";
//            }
        String vppid=    Logics.getVppId(BrokerageList.this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("VPPID",vppid);
            jsonObject.put("page",1);
            jsonObject.put("size",10);

            byte data[] = jsonObject.toString().getBytes();

            new SendTOServer(BrokerageList.this,BrokerageList.this, Const.MSGFETCHBROKERAGELIST,data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }







    }

    @Override
    public void requestSent(int value) {

    }

    public class ViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listBrokerage = (RecyclerView)findViewById(R.id.listBrokerage);
            brokerageListArrayList = new ArrayList<>();

            Log.d("Message", "handleMessageBrokerageList: "+msg.toString());

//            if(ringProgressDialog!=null){
//
//                ringProgressDialog.dismiss();
//            }

            AlertDialogClass.PopupWindowDismiss();


            String data = (String)msg.obj;

            brokerageListArrayList = gson.fromJson(data, new TypeToken<ArrayList<BrokerageListData>>() {}.getType());
            if(brokerageListArrayList!=null){

                BrokerageListAdapter brokerageListAdapter = new BrokerageListAdapter(brokerageListArrayList,BrokerageList.this);
                listBrokerage.setLayoutManager(new LinearLayoutManager(BrokerageList.this));
                listBrokerage.setAdapter(brokerageListAdapter);
                listBrokerage.setItemAnimator(new DefaultItemAnimator());


            }

        }
    }
}
