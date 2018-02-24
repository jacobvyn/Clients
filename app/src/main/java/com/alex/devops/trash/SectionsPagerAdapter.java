package com.alex.devops.trash;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.test.app.jacob.mygalleryapp.R;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private int fragmentsCount = 2;
    private Context context;
    private ArrayList<Fragment> mFragmentsList = new ArrayList<Fragment>();

    public SectionsPagerAdapter(FragmentManager fm, Context con) {
        super(fm);
        this.context = con;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
//                fragment = SearchFragment.newInstance();
                break;
            case 1:
//                fragment = NewClientActivity.newInstance();
                break;
        }
        mFragmentsList.add(fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentsCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.title_tab_0);
            case 1:
                return context.getResources().getString(R.string.title_tab_1);
        }
        return "empty";
    }
}
