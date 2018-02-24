package com.alex.devops;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alex.devops.db.Client;
import com.alex.devops.db.ClientsDataBase;
import com.alex.devops.utils.ExecutorHelper;
import com.alex.devops.utils.PermissionHelper;
import com.example.test.app.jacob.mygalleryapp.R;

import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {
    private ClientsDataBase mDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
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
            Toast.makeText(this, "Need permission to work with app", Toast.LENGTH_LONG).show();
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
                client.prepare();
                mDataBase.clientDao().insert(client);
                Log.e("+++ insertClient", client.toString());
            }
        });
    }

    public void searchClient(final String secondName) {
        ExecutorHelper.submit(new Runnable() {
            @Override
            public void run() {
                final List<Client> clientsList = mDataBase.clientDao().getClientsLike("%" + secondName + "%");
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
                List<Client> clientsList = mDataBase.clientDao().getAllClients();

                // TODO: 2/23/18  syncing logic
            }
        });
        Snackbar.make(findViewById(R.id.root_view), "Syncing ...", Snackbar.LENGTH_SHORT).show();
    }

    public abstract void onSearchFinished(List<Client> clients);
}
