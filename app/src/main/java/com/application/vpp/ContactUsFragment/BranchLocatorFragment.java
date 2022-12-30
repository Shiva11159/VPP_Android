package com.application.vpp.ContactUsFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.vpp.Adapters.BranchLocatorAdapter;
import com.application.vpp.ClientServer.ConnectTOServer;
import com.application.vpp.ClientServer.Connectivity;
import com.application.vpp.ClientServer.SendTOServer;
import com.application.vpp.Const.Const;
import com.application.vpp.Datasets.BranchLocatorDetails;
import com.application.vpp.Interfaces.CallBack;
import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.application.vpp.R;
import com.application.vpp.Views.Views;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class BranchLocatorFragment extends Fragment implements RequestSent,CallBack, ConnectionProcess {
    RequestSent  requestsent;
    EditText branch_locator_searchView;
    public static Handler handlerBranchLocator;
    static Gson gson;
    ArrayList<BranchLocatorDetails> listDatasetArrayList = new ArrayList<>();
    RecyclerView branchlist;
    ProgressDialog ringProgressDialog;
    BranchLocatorAdapter branchListadapter;
    public  TextView textview_branch_name,textview_contact_person,textview_mobile_num,textview_email_id;
    Context context;
    View view;
    CallBack callBack;
    ImageView imageViewdlg;
    AlertDialog alertDialog;
    LinearLayout bottomlinear;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;

    public BranchLocatorFragment() {
        // Required empty public constructor
    }

    public static BranchLocatorFragment newInstance(String param1, String param2) {
        BranchLocatorFragment fragment = new BranchLocatorFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("","onCreateView");
        connectionProcess = (ConnectionProcess) this;
        requestSent = (RequestSent) this;

        view =  inflater.inflate(R.layout.branch_locator, container, false);
        context = getActivity();
        handlerBranchLocator = new ViewHandler();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        callBack = this;
        branch_locator_searchView = view.findViewById(R.id.branch_locator_searchView);
        branchlist = view.findViewById(R.id.recyclerview_branch);

      /*  if (Connectivity.getNetworkState(getActivity())) {
            if (Const.isSocketConnected) {
                sendData();
                ringProgressDialog = Views.showDialog(getActivity());
            }else {

                Views.toast(getActivity(),Const.checkConnection);
            }
        }*/

        if (Connectivity.getNetworkState(getActivity()))
            sendData();
        else
            Views.SweetAlert_NoDataAvailble(getActivity(),"No Internet");

        // autocomplete_textview.setAdapter(branchListadapter);


        branch_locator_searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                branchListadapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;

    }

    @Override
    public void requestSent(int value) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("","onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("","onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("","onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("","onStop");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("","onDetach");
    }




    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            {
                super.handleMessage(msg);

                Log.d("Message", "handleMessageLeadList: "+msg.toString());

                if(ringProgressDialog!=null){

                    ringProgressDialog.dismiss();
                }

                String data = (String)msg.obj;
                int msgCode = msg.arg1;

                switch (msgCode){

                /*case Const.MSGSOCKETCONNECTEDMYLEADS:{

                    sendData();
                }break;*/

                    case Const.MSGBRANCHLOCATOR:{
                        Log.d("response",data);

                        // data = "[\"[{\"LeadDate\":\"10 Sep 2019 10:04\",\"LeadNo\":\"2506305\",\"CustomerName\":\"CHARANJIT SINGH\",\"BranchCode\":\"9999\",\"VPPPAN\":\"72729\",\"UserName\":\"11030 - Momita Bose\",\"ClientName\":\"  \",\"Status\":\"Call Back\",\"ProductName\":\"Commodities\"}]\"]";
                        //listDatasetArrayList = gson.fromJson(data, new TypeToken<ArrayList<LeadDetailReportDataset>>() {}.getType());
                        Type userListType = new TypeToken<ArrayList<BranchLocatorDetails>>(){}.getType();


                        listDatasetArrayList  = gson.fromJson(data,userListType);
                        branchListadapter = new BranchLocatorAdapter(getActivity(),listDatasetArrayList,callBack);
                        if(listDatasetArrayList!=null){

                            branch_locator_searchView.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }
                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    branchListadapter.getFilter().filter(charSequence.toString());
                                }
                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
//                            branch_locator_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String s) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String s) {
//                                    return false;
//                                }
//                            });

                            branchlist.setLayoutManager(new LinearLayoutManager(getActivity()));
                            branchlist.setAdapter(branchListadapter);
//                            branchlist.setItemAnimator(new DefaultItemAnimator());
//                            branchlist.addItemDecoration(new DividerItemDecoration(branchlist.getContext(), DividerItemDecoration.VERTICAL));

                        }
                    }break;
                }
            }
        }

    }
    @Override
    public void getDetails(String branchname,String contactperson,String emailid,String mobileno) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View alertDialogView = inflater.inflate(R.layout.dialog_branch_details, null);
        //alertDialog.setView(alertDialogView);

        PopUp_ContactDetails(branchname,contactperson,emailid,mobileno);

//        TextView textDialog = (TextView) alertDialogView.findViewById(R.id.textview_branch_name);
//        textDialog.setText("example");

//        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        alertDialog.show();

    }

    @Override
    public void getReason(String reason, String name, String leadNo) {

    }



    private void sendData(){
        ringProgressDialog = Views.showDialog(getActivity());

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("JUST_HIT","get");
            byte data[] = jsonObject.toString().getBytes();
            new SendTOServer(getActivity(), requestsent, Const.MSGBRANCHLOCATOR, data,connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void PopUp_ContactDetails(String branchname,String contactperson,String emailid,String mobileno) {
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.contactdetails_layout, viewGroup, false);
        textview_branch_name=view.findViewById(R.id.textview_branch_name);
        textview_mobile_num=view.findViewById(R.id.textview_mobile_num);
        textview_email_id=view.findViewById(R.id.textview_email_id);
        textview_contact_person=view.findViewById(R.id.textview_contact_person);
        imageViewdlg=view.findViewById(R.id.imageViewdlg);
        textview_branch_name.setText("Branch Name : "+ branchname);
        textview_contact_person.setText("Contact Person : "+contactperson);

        textview_mobile_num.setText("Contact No :"+mobileno);
        textview_email_id.setText("Email Id :"+emailid);
        imageViewdlg.setVisibility(View.VISIBLE);
        imageViewdlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
//                finish();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        alertDialog = builder.create();
//        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();
    }

    @Override
    public void connected() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        //        AlertDailog.ProgressDlgDiss();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendData();
              //  TastyToast.makeText(getActivity(), "Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void serverNotAvailable() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        Log.e("serverNotAvailable00: ", "serverNotAvailable00");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(getActivity(), connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void IntenrnetNotAvailable() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }

        Views.SweetAlert_NoDataAvailble(getActivity(), "Internet Not Available");
        //        if (pDialog.isShowing()) {
        //            pDialog.dismiss();
        //            pDialog.cancel();
        //        }
        Log.e("IntenrnetNotAvailable: ", "internet");

        //        AlertDailog.ProgressDlgDiss();
        //        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ConnectToserver(final ConnectionProcess connectionProcess) {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
//        connected = false;

        Log.e("ConnectToserver11: ", "ConnectToserver11");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.getNetworkState(getActivity()))
                    new ConnectTOServer(getActivity(), connectionProcess).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Const.dismiss==true){
                            if (Const.isServerConnected == true && Const.isSocketConnected == false) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Views.ProgressDlgConnectSocket(getActivity(), connectionProcess, "Server Not Available");
                                    }
                                });
                            }
                        }
                    }
                },3000);
            }
        });
    }

    @Override
    public void SocketDisconnected() {
        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
        Log.e("SocketDisconnected11: ", "SocketDisconnected");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionProcess != null) {
                    Views.ProgressDlgConnectSocket(getActivity(), connectionProcess, "Server Not Available");
                } else {
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
