package com.escuelapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.escuelapp.CreateFragment;
import com.escuelapp.GroupFragment;
import com.escuelapp.PublicFragment;
import com.escuelapp.R;
import com.escuelapp.utility.Constants;
import com.escuelapp.AnswerFragment;

/**
 * Created by alan on 10/15/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public PagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constants.TAB_CREATE:
                return CreateFragment.newInstance();
            case Constants.TAB_ANSWER:
                return AnswerFragment.newInstance();
            case Constants.TAB_PUBLIC:
                return PublicFragment.newInstance();
            case Constants.TAB_GROUPS:
                return GroupFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return Constants.NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case Constants.TAB_CREATE:
                return context.getString(R.string.tab_title_create);
            case Constants.TAB_ANSWER:
                return context.getString(R.string.tab_title_answer);
            case Constants.TAB_PUBLIC:
                return context.getString(R.string.tab_title_public);
            case Constants.TAB_GROUPS:
                return context.getString(R.string.tab_title_groups);
            default:
                return null;
        }
    }
}
