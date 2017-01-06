package com.escuelapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.escuelapp.adapter.PagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends BaseAppActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
//        initializeInvitation();
    }

    private void initializeView() {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        ((TabLayout) findViewById(R.id.sliding_tabs)).setupWithViewPager(viewPager);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}