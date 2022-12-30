package com.application.vpp.activity;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.application.vpp.Adapters.PhoneDirectoryAdapter;
import com.application.vpp.Datasets.PhoneDirectoryData;
import com.application.vpp.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class PhoneDirectory extends AppCompatActivity {
    private MaterialSearchView searchView;
    TextView txtSearch;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    RecyclerView listContactDirectory;
    PhoneDirectoryAdapter phoneDrectoryAdapter;
    ArrayList<PhoneDirectoryData>phoneDirectoryArraylist;
    ArrayList<PhoneDirectoryData>phoneDirectoryArraylistMain = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_directory);

        Toolbar toolbar = (Toolbar) findViewById(R.id.row_TITLE);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        listContactDirectory = (RecyclerView)findViewById(R.id.readContacts);
        phoneDirectoryArraylist = new ArrayList<>();
        listContactDirectory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        phoneDrectoryAdapter = new PhoneDirectoryAdapter(PhoneDirectory.this,phoneDirectoryArraylist);
        listContactDirectory.setAdapter(phoneDrectoryAdapter);
        listContactDirectory.setItemAnimator(new DefaultItemAnimator());


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.container), "Query: " + query, Snackbar.LENGTH_LONG)
                        .show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                //Do some magic


                filter(newText);



                return false;

            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic


            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic

            }
        });



        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                searchView.setQuery("Data",false);
                searchView.dismissSuggestions();
                searchView.closeSearch();


            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);


        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},1);


        } else {
            //TODO


            searchcontacts();


        }





    }



    public void filter(String charText) {
        int arrayListCount = phoneDirectoryArraylistMain.size();

        phoneDirectoryArraylist.clear();

        if(charText.length()>0){
            for(int i=0;i<arrayListCount;i++){


                if(phoneDirectoryArraylistMain.get(i).getName().toLowerCase().startsWith(charText.toLowerCase())){



                    phoneDirectoryArraylist.add(phoneDirectoryArraylistMain.get(i));

                }



            }

        }else {

            phoneDirectoryArraylist.addAll(phoneDirectoryArraylistMain);
        }



        phoneDrectoryAdapter.notifyDataSetChanged();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchview, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }


    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    public ArrayList<PhoneDirectoryData> searchcontacts(){

        ArrayList<String>  nameList = new ArrayList<>();

        ArrayList<PhoneDirectoryData> phoneDirectoryArraylist = new ArrayList<>();

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC ");

        if(cursor!=null){
            cursor.moveToFirst();
            while (cursor.moveToNext()){

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                if(nameList.contains(name)){



                }else {

                    PhoneDirectoryData phoneDirectoreyData = new PhoneDirectoryData(name,number);
                    phoneDirectoryArraylist.add(phoneDirectoreyData);

                    nameList.add(name);

                }



            }

        }


        cursor.close();

        this.phoneDirectoryArraylist.clear();
        this.phoneDirectoryArraylist.addAll(phoneDirectoryArraylist);

        if(phoneDirectoryArraylistMain!=null){
            phoneDirectoryArraylistMain.clear();
        }

        phoneDirectoryArraylistMain.addAll(phoneDirectoryArraylist);

        phoneDrectoryAdapter.notifyDataSetChanged();

        return phoneDirectoryArraylist;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            searchcontacts();

        }

    }
}
