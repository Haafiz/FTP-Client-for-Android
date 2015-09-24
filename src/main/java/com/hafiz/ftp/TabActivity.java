package com.hafiz.ftp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TextView;

public class TabActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("remote").setIndicator("Remote", null),
                RemoteTabFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("local").setIndicator("Local", null),
                LocalTabFragment.class, null);

        new FtpTask(this).execute();
    }

    public void setLog(String message) {
        TextView tv = (TextView) findViewById(R.id.parentlog);
        tv.setText(message);
    }
}