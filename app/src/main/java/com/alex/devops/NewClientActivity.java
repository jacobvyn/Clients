package com.alex.devops;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alex.devops.commons.SimpleActivity;
import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.alex.devops.views.ClientViewFragment;

public class NewClientActivity extends SimpleActivity
        implements View.OnClickListener,
        ClientViewFragment.OnParentsChanged {
    private FloatingActionButton mAddParentButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_client_layout);
        findViewById(R.id.new_client_root_view).setBackgroundColor(getBackgroundColor());
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

        mAddParentButton = (FloatingActionButton) findViewById(R.id.add_second_parent_fab);
        mAddParentButton.setOnClickListener(this);
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
            if (clientFragment.checkInputData()) {
                finishWithResult(clientFragment.getClient());
            }
        }
    }

    private void finishWithResult(Client client) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARG_NEW_CLIENT, client);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
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
