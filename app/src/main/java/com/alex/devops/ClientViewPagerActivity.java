package com.alex.devops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.alex.devops.commons.SimpleActivity;
import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.alex.devops.views.SlideClientFragment;

import java.util.ArrayList;

public class ClientViewPagerActivity extends SimpleActivity {
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_view_pager_layout);
        findViewById(R.id.view_pager_root_view).setBackgroundColor(getBackgroundColor());
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_pager_child_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.found_clients);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new ClientsViewPagerAdapter(getSupportFragmentManager(), getClients()));
    }

    private ArrayList<Client> getClients() {
        return getIntent().getParcelableArrayListExtra(Constants.ARG_VIEW_PAGER_CLIENTS);
    }

    private class ClientsViewPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Client> mClients;

        public ClientsViewPagerAdapter(FragmentManager supportFragmentManager, ArrayList<Client> clients) {
            super(supportFragmentManager);
            mClients = clients;
        }

        @Override
        public int getCount() {
            return mClients.size();
        }

        @Override
        public Fragment getItem(int position) {
            return SlideClientFragment.newInstance(mClients.get(position));
        }
    }
}
