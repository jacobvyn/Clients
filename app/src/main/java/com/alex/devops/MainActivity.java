package com.alex.devops;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alex.devops.commons.BaseActivity;
import com.alex.devops.db.Client;
import com.alex.devops.views.SyncDialog;
import com.alex.devops.views.SearchFragment;
import com.alex.devops.views.VisitSettingsFragment;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener,
        View.OnClickListener,
        ColorPickerClickListener, VisitSettingsFragment.Listener, SyncDialog.Listener {

    @BindView(R.id.root_view)
    protected View mRootView;
    protected MenuItem mSearchMenuItem;
    private int NEW_CLIENT_REQUEST_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layour);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.new_client_button).setOnClickListener(this);
        setBackgroundColor(getBackgroundColor());
        insertSearchFragment();
        findClient("all");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataBaseListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchMenuItem = menu.getItem(0);
        ((SearchView) mSearchMenuItem.getActionView()).setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync: {
                startSync();
                return true;
            }
            case R.id.action_color_pick: {
                chooseColor();
                return true;
            }
            case R.id.action_set_visits: {
                setMaxVisits();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void startSync() {
        SyncDialog dialog = SyncDialog.newInstance();
        dialog.setListener(this);
        dialog.show(getFragmentManager(), SyncDialog.TAG);
    }

    @Override
    public void onSyncConfirmed() {
        startSyncConfirmed();
    }

    @Override
    public void onBaseURLChanged(String newURL) {
        setBaseURL(newURL);
    }

    private void setMaxVisits() {
        VisitSettingsFragment fragment = VisitSettingsFragment.newInstance();
        fragment.setListener(this);
        fragment.show(getFragmentManager(), VisitSettingsFragment.TAG);
    }

    private void chooseColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle(R.string.choose_color)
                .initialColor(R.color.white)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(R.string.ok, this)
                .setNegativeButton(getString(R.string.abort), null)
                .build()
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
        setBackgroundColor(selectedColor);
        storeBackgroundColor(selectedColor);
    }

    private void setBackgroundColor(int color) {
        if (mRootView != null) {
            mRootView.setBackgroundColor(color);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        findClient(newText);
        return true;
    }

    private void insertSearchFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.search_container_view, SearchFragment.newInstance(), SearchFragment.TAG)
                .commit();
    }

    @Override
    public void onSearchFinished(List<Client> clients) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.search_container_view);
        if (fragment instanceof SearchFragment) {
            ((SearchFragment) fragment).onSearchFinished(clients);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_client_button: {
                starNewClientActivity();
                break;
            }
        }
    }

    private void starNewClientActivity() {
        Intent intent = new Intent(this, NewClientActivity.class);
        startActivityForResult(intent, NEW_CLIENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_CLIENT_REQUEST_CODE && resultCode == RESULT_OK) {
            findClient("all");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void onSetVisitCount(int count) {
        setMaxVisitAmount(count);
    }

    @Override
    public void onSyncResult(int resource) {
        super.onSyncResult(resource);
        Snackbar.make(mRootView, resource, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClientSavedSuccess() {
        Snackbar.make(mRootView, R.string.client_saved_success, Snackbar.LENGTH_LONG).show();
    }
}
