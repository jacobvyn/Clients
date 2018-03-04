package com.alex.devops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alex.devops.commons.BaseActivity;
import com.alex.devops.db.Client;
import com.alex.devops.views.ClientViewFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewClientActivity extends BaseActivity implements View.OnClickListener,
        ClientViewFragment.OnParentsChanged {

    @BindView(R.id.add_second_parent_fab)
    protected FloatingActionButton mAddParentButton;
    @BindView(R.id.new_client_root_view)
    protected View mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client_layout);
        ButterKnife.bind(this);
        mRootView.setBackgroundColor(getBackgroundColor());
        Toolbar toolbar = (Toolbar) findViewById(R.id.child_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_new_client);

        ClientViewFragment clientFragment = ClientViewFragment.newInstance();
        clientFragment.setOnParentsChangedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.client_view_root_layout, clientFragment, ClientViewFragment.TAG)
                .commit();

        mAddParentButton.setOnClickListener(this);
    }

    @Override
    public void onSearchFinished(List<Client> clients) {
    }

    @Override
    public void onReceivedClientsSuccess(List<Client> list) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_cleint_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_client: {
                saveClient();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveClient() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.client_view_root_layout);
        if (fragment instanceof ClientViewFragment) {
            ClientViewFragment clientFragment = (ClientViewFragment) fragment;
//            if (clientFragment.checkInputData()) {
                finishWithResult(clientFragment.getClient());
//            }
        }
    }

    private void finishWithResult(Client client) {
        insertClient(client);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_second_parent_fab: {
                onFabClicked();
                break;
            }
        }
    }

    private void onFabClicked() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.client_view_root_layout);
        if (fragment instanceof ClientViewFragment) {
            ((ClientViewFragment) fragment).onAddParentClicked();
        }
        changeFabIcon();
    }

    private void changeFabIcon() {

    }

    @Override
    public void onParentsChanged(boolean added) {
        if (added) {
            mAddParentButton.setImageResource(R.drawable.ic_minus_one_parent);
        } else {
            mAddParentButton.setImageResource(R.drawable.ic_plus_one_parent);
        }
    }
}
