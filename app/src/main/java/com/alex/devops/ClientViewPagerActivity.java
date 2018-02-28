package com.alex.devops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.devops.commons.BaseActivity;
import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.alex.devops.utils.Utils;
import com.alex.devops.views.SlideClientFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClientViewPagerActivity extends BaseActivity implements OnPageChangeListener {
    private ViewPager mViewPager;
    private ArrayList<Client> mClientList;
    @BindView(R.id.pager_last_visited_text_view)
    protected TextView mLastVisitedTextView;
    @BindView(R.id.pager_visit_button)
    protected Button mVisitButton;
    private Client mCurrentClient;

    @BindView(R.id.view_pager_root_view)
    protected View mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_view_pager_layout);
        ButterKnife.bind(this);
        findViewById(R.id.view_pager_root_view).setBackgroundColor(getBackgroundColor());
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_pager_child_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.found_clients);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mClientList = getClients();
        mCurrentClient = mClientList.get(0);
        mViewPager.setAdapter(new ClientsViewPagerAdapter(getSupportFragmentManager(), mClientList));
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onSearchFinished(List<Client> clients) {
        // TODO: 2/28/18 think how to avoid these methods
    }

    private ArrayList<Client> getClients() {
        return getIntent().getParcelableArrayListExtra(Constants.ARG_VIEW_PAGER_CLIENTS);
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentClient = mClientList.get(position);
        updateVisitControls(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateVisitControls(true);
    }

    private void updateVisitControls(boolean show) {
        mLastVisitedTextView.setText(Utils.getFormattedDate(mCurrentClient.getLastVisit()));
        if (mCurrentClient.hasVisitedToday()) {
            mVisitButton.setEnabled(false);
            showAlreadyVisitedMessage(show);
        } else {
            mVisitButton.setEnabled(true);
        }
    }

    @OnClick(R.id.pager_visit_button)
    public void onVisitClicked() {
        if (!mCurrentClient.hasVisitedToday()) {
            mCurrentClient.incVisitCounter();
            updateClient(mCurrentClient);
        }
        updateVisitControls(false);
    }

    private void showAlreadyVisitedMessage(boolean show) {
        if (show) {
            Toast.makeText(this, R.string.client_already_visited, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
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
