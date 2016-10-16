package com.escuelapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.escuelapp.adapter.PagerAdapter;
import com.escuelapp.utility.Utility;

public class MainActivity extends BaseActivity {

    private FragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
    }

    private void initializeView() {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        ((TabLayout) findViewById(R.id.sliding_tabs)).setupWithViewPager(viewPager);
    }
}