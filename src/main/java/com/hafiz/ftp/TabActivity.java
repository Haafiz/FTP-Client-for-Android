package com.hafiz.ftp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TextView;

import java.util.Map;

public class TabActivity extends FragmentActivity {
        private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        String sitename = getIntent().getStringExtra("site");

        Bundle remotetabBundle = new Bundle();
        b.putString("sitename", sitename);
        mTabHost.addTab(
                mTabHost.newTabSpec("remote").setIndicator("Remote", null),
                RemoteTabFragment.class, remotetabBundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("local").setIndicator("Local", null),
                LocalTabFragment.class, null);
    }

    public void setLog(String message) {
        TextView tv = (TextView) findViewById(R.id.parentlog);
        tv.setText(message);
    }
}