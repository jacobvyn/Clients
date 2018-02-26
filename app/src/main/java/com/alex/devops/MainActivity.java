package com.alex.devops;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alex.devops.commons.BaseActivity;
import com.alex.devops.commons.OnSwipeListener;
import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.alex.devops.views.SearchFragment;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.List;

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener,
        View.OnClickListener,
        OnSwipeListener.OnSwipe,
        ColorPickerClickListener {
    private View mRootView;
    protected MenuItem mSearchMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layour);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.new_client_button).setOnClickListener(this);
        mRootView = findViewById(R.id.activity_main_scroll_layout);
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
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
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
            searchClient(newText);
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
        findViewById(R.id.activity_main_scroll_layout).postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.search_container_view, SearchFragment.newInstance(), SearchFragment.TAG);
                transaction.commit();
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
    public void onClientSavedSuccess() {
        Snackbar.make(mRootView, R.string.client_saved_success, Snackbar.LENGTH_LONG).show();
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
        startActivityForResult(intent, Constants.CREATE_NEW_CLIENT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CREATE_NEW_CLIENT_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            Client client = (Client) bundle.getParcelable(Constants.ARG_NEW_CLIENT);
            insertClient(client);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
