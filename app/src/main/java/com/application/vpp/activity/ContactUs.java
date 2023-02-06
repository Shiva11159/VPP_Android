package com.application.vpp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.application.vpp.Interfaces.ConnectionProcess;
import com.application.vpp.Interfaces.RequestSent;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.application.vpp.ContactUsFragment.BranchLocatorFragment;
import com.application.vpp.ContactUsFragment.ConnectFragment;
import com.application.vpp.R;

import java.util.ArrayList;
import java.util.List;

public class ContactUs extends NavigationDrawer {

    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ConnectionProcess connectionProcess;
    RequestSent requestSent;

    //  TelephonyManager tm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.contactus_layout, mDrawerLayout);
        toolbar.setTitle("Contact Us");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.vpPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        // tm  =  ( TelephonyManager )getSystemService( Context.TELEPHONY_SERVICE );

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ConnectFragment(), "Connect");
        adapter.addFragment(new BranchLocatorFragment(), "Branch Locator");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
    @Override
    public void onBackPressed() {
        Intent intent =new Intent(ContactUs.this,Dashboard.class);
        startActivity(intent);
    }
}
