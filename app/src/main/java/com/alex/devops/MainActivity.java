package com.alex.devops;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.example.test.app.jacob.mygalleryapp.R;

import java.util.List;

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener,
        View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layour);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.new_client_button).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.getItem(0);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchItem.setOnActionExpandListener(this);
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
            default:
                return super.onOptionsItemSelected(item);
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
}
