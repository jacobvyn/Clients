package com.alex.devops.net;

import android.os.Handler;
import android.os.Looper;

import com.alex.devops.R;
import com.alex.devops.db.Client;
import com.alex.devops.db.Credentials;
import com.alex.devops.utils.ExecutorHelper;

import java.util.List;

public class Synchronizer {
    private HttpService mHttpService;
    private Listener mListener;
    private Handler mHandler;
    private DBInterface mDBInterface;


    public Synchronizer(Credentials credentials, Listener listener, DBInterface dbInterface) {
        mDBInterface = dbInterface;
        mHttpService = new HttpService(credentials);
        mListener = listener;
        mHandler = new Handler((Looper.getMainLooper()));
    }

    public void syncing(final long lastTimeSync) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                List<Client> nonSyncedClients = mDBInterface.getAllClientsSyncAfter(lastTimeSync);
                if (nonSyncedClients.size() > 0) {
                    sendClientsBeforeSync(nonSyncedClients);
                } else {
                    simpleSyncing();
                }
            }
        };
        ExecutorHelper.submit(task);
    }

    private void sendClientsBeforeSync(List<Client> nonSyncedClients) {
        boolean isSuccess = mHttpService.doPostRequest(nonSyncedClients);
        if (isSuccess) {
            simpleSyncing();
        } else {
            onResult(false);
        }
    }

    private void simpleSyncing() {
        List<Client> clientList = mHttpService.getAllClients();
        if (clientList == null) {
            onResult(false);
        } else {
            if (clientList.size() > 0) {
                mDBInterface.deleteAllSync();
                mDBInterface.insertAllSync(clientList);
            }
            onResult(true);
        }
    }

    private void onResult(boolean result) {
        if (result) {
            onSyncResult(R.string.syncing_finished_success, true);
        } else {
            onSyncResult(R.string.syncing_failed, false);
        }
    }


    private void onSyncResult(final int resourceMessage, final boolean result) {
        mDBInterface = null;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onSyncResult(resourceMessage, result);
                }
            }
        });
    }

    private void runOnUiThread(Runnable runnable) {
        if (mHandler != null) {
            mHandler.post(runnable);
        }
    }

    public interface Listener {
        void onSyncResult(int resourceMessage, boolean result);
    }
}
