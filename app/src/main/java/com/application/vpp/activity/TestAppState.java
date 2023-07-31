//package com.application.vpp.activity;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//
//import com.application.vpp.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestAppState extends AppCompatActivity {
//
//    List<String> statename;
//    List<String> stateid;
//    List<String> cityname;
//    List<String> citynameTemp;
//    List<String> cityid;
//    Spinner spinnerstate,spinnercity;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_app_state);
//
//        Statename stateNAme=new Statename("hyderabad","1");
////        Statename stateNAme=new Statename("hyderabad","1");
//        statename=new ArrayList<>();
//        statename.add("hyderabad");
//        statename.add("maharastra");
//        statename.add("Up");
//        stateid=new ArrayList<>();
//        stateid.add("1");
//        stateid.add("2");
//        stateid.add("3");
//
//
//        citynameTemp=new ArrayList<>();
//        cityname=new ArrayList<>();
//        cityid=new ArrayList<>();
//
//
//        cityid.add("1");
//        cityname.add("uppal");
//        cityid.add("1");
//        cityname.add("kondapur");
//
//
//
//        cityid.add("2");
//        cityname.add("mumbai");
//        cityid.add("2");
//        cityname.add("pune");
//
//
//
//        cityid.add("3");
//        cityname.add("up_village1");
//        cityid.add("3");
//        cityname.add("up_village2");
//
//
//
//        spinnerstate=findViewById(R.id.spinnerstate);
//        spinnercity=findViewById(R.id.spinnercity);
//
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TestAppState.this, android.R.layout.simple_spinner_dropdown_item, statename);
//        spinnerstate.setAdapter(adapter);
//        spinnerstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
//
//
//                String st_id=stateid.get(position);
//
//
//                Log.e("st_id", st_id);
//                Log.e("cityID_size", String.valueOf(cityid.size()));
//
//                citynameTemp.clear();
//
//                for (int i=0;i<cityid.size();i++){
//
//                    if (cityid.get(i).contains(st_id))
//
//                    {
//                        Log.e("cityNames", cityname.get(i));
//
//                        citynameTemp.add(cityname.get(i));
//                    }
//
//                }
//
//
//                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(TestAppState.this, android.R.layout.simple_spinner_dropdown_item, citynameTemp);
//                spinnercity.setAdapter(adapter1);
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//    }
//
//
//}