package com.hafiz.ftp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

/**
 * Tab activity to have two tabs in it
 */
public class TabActivity extends FragmentActivity {
    public FragmentTabHost mTabHost;
    public String remoteAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        //set up tab host
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        //getting sitename from intent called from main activity
        String sitename = getIntent().getStringExtra("site");

        // setting up bundle to send to tabs
        Bundle tabBundle = new Bundle();
        tabBundle.putString("sitename", sitename);

        //Adding tabs with their target classes to provide content
        mTabHost.addTab(
                mTabHost.newTabSpec("remote").setIndicator("Remote", null),
                RemoteTabFragment.class, tabBundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("local").setIndicator("Local", null),
                LocalTabFragment.class, tabBundle);


    }

}