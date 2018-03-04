package com.alex.devops;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alex.devops.commons.BaseActivity;
import com.alex.devops.commons.OnSwipeListener;
import com.alex.devops.db.Client;
import com.alex.devops.views.SearchFragment;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener,
        View.OnClickListener,
        OnSwipeListener.OnSwipe,
        ColorPickerClickListener, VisitSettingsFragment.Listener {

    @BindView(R.id.root_view)
    protected View mRootView;
    protected MenuItem mSearchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layour);
        setDataBaseListener(this);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.new_client_button).setOnClickListener(this);
        mRootView.setOnTouchListener(new OnSwipeListener(this, this));
        setBackgroundColor(getBackgroundColor());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchMenuItem = menu.getItem(0);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchMenuItem.setOnActionExpandListener(this);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync: {
                syncRemoteDB();
                return true;
            }
            case R.id.action_color_pick: {
                chooseColor();
                return true;
            }
            case R.id.action_settings: {
                setMaxVisits();
                return true;
            }
            case R.id.action_reload_db: {
                reloadDB();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void reloadDB() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.data_base_reload);
        builder.setMessage(R.string.notice_db_reload);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getAllFromServer();
            }
        });
        builder.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
        if (newText.length() >= 1) {
            findClient(newText);
        } else {
            clearSearch();
        }
        return true;
    }

    private void updateSearchFragment(List<Client> clients) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.search_container_view);
        if (fragment instanceof SearchFragment) {
            ((SearchFragment) fragment).onSearchFinished(clients);
        }
    }

    private void insertSearchFragment() {
        findViewById(R.id.root_view).postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .add(R.id.search_container_view, SearchFragment.newInstance(), SearchFragment.TAG)
                        .commit();
            }
        }, 100);
    }

    private void removeSearchFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SearchFragment.TAG);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    @Override
    public void onSearchFinished(List<Client> clients) {
        updateSearchFragment(clients);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        insertSearchFragment();
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        removeSearchFragment();
        return true;
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
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public boolean clearSearch() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.search_container_view);
        if (fragment instanceof SearchFragment) {
            ((SearchFragment) fragment).clear();
        }
        return true;
    }

    @Override
    public void onSwipeLeft() {
        starNewClientActivity();
    }

    @Override
    public void onSwipeBottom() {
        if (mSearchMenuItem != null) {
            mSearchMenuItem.expandActionView();
        }
    }

    @Override
    public void onOkPressed(int count) {
        setMaxVisitAmount(count);
    }

    @Override
    public void onReceivedClientsSuccess(List<Client> list) {
        replaceClients(list);
    }
}
