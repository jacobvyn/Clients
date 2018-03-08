package com.alex.devops;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    private List<Client> mClientList;

    @BindView(R.id.pager_last_visited_text_view)
    protected TextView mLastVisitedTextView;

    @BindView(R.id.pager_client_visits_text_view)
    protected TextView mClientVisitsTextView;

    @BindView(R.id.pager_visit_button)
    protected Button mVisitButton;
    private Client mCurrentClient;

    @BindView(R.id.view_pager_root_view)
    protected View mRootView;

    private int mMaxVisits;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_view_pager_layout);
        ButterKnife.bind(this);
        mMaxVisits = getMaxVisitAmount();
        findViewById(R.id.view_pager_root_view).setBackgroundColor(getBackgroundColor());
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_pager_child_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.found_clients);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(this);
        findClientByIds(getClientsIds());
    }


    @Override
    protected void onResume() {
        super.onResume();
        setDataBaseListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset_visit_counter: {
                resetVisitCounter();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetVisitCounter() {
        if (mCurrentClient != null) {
            mCurrentClient.setVisitCounter(0);
            updateClient(mCurrentClient);
            updateVisitControls(false);
        }
    }

    private int retrieveCurrentPosition(Intent intent) {
        return intent.getExtras().getInt(Constants.ARG_CURRENT_POSITION);
    }

    @Override
    public void onSearchFinished(List<Client> clients) {
        if (clients.size() == 0) {
            finish();
        }
        mClientList = clients;
        mViewPager.setAdapter(new ClientsViewPagerAdapter(getSupportFragmentManager(), mClientList));
//        int current = retrieveCurrentPosition(getIntent());
        mCurrentClient = mClientList.get(0);
        updateVisitControls(true);
    }

    private ArrayList<Integer> getClientsIds() {
        return getIntent().getIntegerArrayListExtra(Constants.ARG_VIEW_PAGER_CLIENTS_IDS);
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentClient = mClientList.get(position);
        updateVisitControls(true);
    }

    private void updateVisitControls(boolean show) {
        mLastVisitedTextView.setText(Utils.getFormattedDate(mCurrentClient.getLastVisit()));
        String visits = mCurrentClient.getVisitCounter() + "/" + mMaxVisits;
        mClientVisitsTextView.setText(visits);
        if (mCurrentClient.hasVisitedToday()) {
            enableVisitButton(false);
            showAlreadyVisitedMessage(show);
        } else {
            enableVisitButton(true);
        }
        checkMaxVisitCounter();
    }

    private void enableVisitButton(boolean enable) {
        if (mVisitButton != null) {
            mVisitButton.setEnabled(enable);
        }
    }

    @OnClick(R.id.pager_visit_button)
    public void onVisitClicked() {
        if (!mCurrentClient.hasVisitedToday()) {
            mCurrentClient.incVisitCounter();
            updateClient(mCurrentClient);
            checkMaxVisitCounter();
        }
        updateVisitControls(false);
    }

    private void checkMaxVisitCounter() {
        int clientVisit = mCurrentClient.getVisitCounter();
        if (clientVisit >= mMaxVisits) {
            enableVisitButton(false);
            // TODO: 3/4/18  show on UI
        }
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
        private List<Client> mClients;

        public ClientsViewPagerAdapter(FragmentManager supportFragmentManager, List<Client> clients) {
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
