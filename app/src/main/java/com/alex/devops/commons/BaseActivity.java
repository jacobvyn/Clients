package com.alex.devops.commons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.alex.devops.R;
import com.alex.devops.db.Client;
import com.alex.devops.db.DataBaseWrapper;
import com.alex.devops.utils.PermissionHelper;
import com.alex.devops.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity
        implements DataBaseWrapper.OnDataBaseChangedListener {
    private PreferenceService mPrefs;
    private DataBaseWrapper mDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBase = DataBaseWrapper.getInstance(this);
        mPrefs = new PreferenceService(this);
    }

    protected void setDataBaseListener(DataBaseWrapper.OnDataBaseChangedListener listener) {
        if (mDataBase != null) {
            mDataBase.setDataBaseListener(listener);
        }
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

    public void insertClient(Client client) {
        if (mDataBase != null) {
            mDataBase.insertClient(client);
        }
    }
    public void findClient(String secondName) {
        if (mDataBase != null) {
            mDataBase.findClient(secondName);
        }
    }

    public void getAllClients() {
        if (mDataBase != null) {
            mDataBase.getAllClients();
        }
    }

    public void updateClient(Client client) {
        if (mDataBase != null) {
            mDataBase.updateClient(client);
        }
    }

    protected void startSyncConfirmed() {
        if (Utils.isOnline(this)) {
            if (mDataBase != null) {
                mDataBase.syncing(getLastTimeSync());
            }
            Snackbar.make(findViewById(R.id.root_view), R.string.syncing, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(findViewById(R.id.root_view), R.string.no_connection, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void findClientByIds(ArrayList<Integer> clientsIds) {
        if (mDataBase != null) {
            mDataBase.findClientByIds(clientsIds);
        }
    }

    @Override
    public void onClientSavedSuccess() {
    }

    @Override
    public void onSyncResult(int resource) {
        setLastTimeSyncNow();
    }

    public void setMaxVisitAmount(int visitAmount) {
        if (mPrefs != null) {
            mPrefs.setMaxVisitAmount(visitAmount);
        }
    }

    public int getMaxVisitAmount() {
        if (mPrefs != null) {
            return mPrefs.getMaxVisitAmount();
        }
        return 1;
    }

    public void storeBackgroundColor(int color) {
        mPrefs.setBackgroundColor(color);
    }

    public int getBackgroundColor() {
        if (mPrefs != null) {
            return mPrefs.getBackgroundColor();
        } else {
            return R.color.colorPrimaryDark;
        }
    }

    public void setLastTimeSyncNow() {
        if (mPrefs != null) {
            mPrefs.setLastTimeSync();
        }
    }

    public long getLastTimeSync() {
        return mPrefs.getLastTimeSync();
    }

    @Override
    public void onSearchFinished(List<Client> clients) {
    }
}
