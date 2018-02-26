package com.alex.devops.commons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.alex.devops.R;
import com.alex.devops.db.Client;
import com.alex.devops.db.ClientsDataBase;
import com.alex.devops.utils.ExecutorHelper;
import com.alex.devops.utils.PermissionHelper;
import com.alex.devops.commons.SimpleActivity;

import java.util.List;


public abstract class BaseActivity extends SimpleActivity {
    private ClientsDataBase mDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBase = ClientsDataBase.getDataBase(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionHelper.hasPermission(this)) {
            PermissionHelper.requestPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!PermissionHelper.hasPermission(this)) {
            Toast.makeText(this, R.string.need_permission, Toast.LENGTH_LONG).show();
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBase = null;
    }

    public void insertClient(final Client client) {
        ExecutorHelper.submit(new Runnable() {
            @Override
            public void run() {
                mDataBase.clientDao().insert(client);
                Log.e("+++ insertClient", client.getMainParentFirstName());
                onClientSavedSuccess();
            }
        });
    }

    public void searchClient(final String secondName) {
        ExecutorHelper.submit(new Runnable() {
            @Override
            public void run() {
                List<Client> clientsList;
                if (secondName != null && secondName.equalsIgnoreCase("all")) {
                    clientsList = mDataBase.clientDao().getAllClients();
                } else {
                    clientsList = mDataBase.clientDao().getClientsLike("%" + secondName + "%");
                }
                onSearchFinishedOnUiThread(clientsList);
            }
        });
    }

    private void onSearchFinishedOnUiThread(final List<Client> clientsList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSearchFinished(clientsList);
            }
        });
    }

    public void getAllClientsAsync() {
        ExecutorHelper.submit(new Runnable() {
            @Override
            public void run() {
                List<Client> clientsList = mDataBase.clientDao().getAllClients();
                Log.e("+++ ALL", clientsList.toString());
            }
        });
    }

    protected void syncRemoteDB() {
        ExecutorHelper.submit(new Runnable() {
            @Override
            public void run() {
                List<Client> nonSyncedClients = mDataBase.clientDao().getAllClientsAfter(getLastTimeSync());
                // TODO: 2/23/18  syncing logic here
                setLastTimeSync();
            }
        });
        Snackbar.make(findViewById(R.id.root_view), R.string.syncing, Snackbar.LENGTH_SHORT).show();
    }

    public abstract void onSearchFinished(List<Client> clients);

    public abstract void onClientSavedSuccess();

}
