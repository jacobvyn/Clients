package com.alex.devops.db;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alex.devops.R;
import com.alex.devops.commons.BaseActivity;
import com.alex.devops.net.RemoteSync;
import com.alex.devops.utils.ExecutorHelper;

import java.util.ArrayList;
import java.util.List;


public class DataBaseWrapper {
    private static DataBaseWrapper sInstance;

    private volatile OnDataBaseChangedListener mListener;
    private ClientsDataBase mDataBase;
    private RemoteSync mRemoteSync;
    private Handler mHandler;


    private DataBaseWrapper(BaseActivity activity) {
        mDataBase = ClientsDataBase.getDataBase(activity);
        mRemoteSync = new RemoteSync();
        mHandler = new Handler((Looper.getMainLooper()));
    }

    public static DataBaseWrapper getInstance(BaseActivity activity) {
        if (sInstance == null) {
            sInstance = new DataBaseWrapper(activity);
        }
        return sInstance;
    }

    public void insertClient(final Client client) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                mDataBase.clientDao().insert(client);
                onClientSavedSuccess();
            }
        };
        ExecutorHelper.submit(task);
    }

    public void findClient(final @NonNull String secondName) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                List<Client> clientsList;
                if (TextUtils.equals(secondName, "all")) {
                    clientsList = mDataBase.clientDao().getAllClients();
                } else {
                    clientsList = mDataBase.clientDao().getClientsLike("%" + secondName + "%");
                }
                onSearchFinished(clientsList);
            }
        };
        ExecutorHelper.submit(task);
    }

    public void updateClient(final Client client) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                mDataBase.clientDao().update(client);
            }
        };
        ExecutorHelper.submit(task);
    }

    public void getAllClients() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                List<Client> clientsList = mDataBase.clientDao().getAllClients();
                onSearchFinished(clientsList);
            }
        };
        ExecutorHelper.submit(task);
    }

    public void findClientByIds(final ArrayList<Integer> clientsIds) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                List<Client> clientsList = mDataBase.clientDao().findClientByIds(clientsIds);
                onSearchFinished(clientsList);
            }
        };
        ExecutorHelper.submit(task);
    }

    public void syncing(final long lastTimeSync, final String syncURL, final String createURL) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                List<Client> nonSyncedClients = mDataBase.clientDao().getAllClientsAfter(lastTimeSync);
                if (nonSyncedClients.size() > 0) {
                    boolean result = mRemoteSync.doPostRequest(nonSyncedClients, createURL);
                    proceedSyncing(result, syncURL);
                } else {
                    proceedSyncing(true, syncURL);
                }
            }
        };
        ExecutorHelper.submit(task);
    }

    private void proceedSyncing(boolean proceedSync, String syncURL) {
        if (proceedSync) {
            List<Client> clientList = mRemoteSync.getAllClients(syncURL);
            if (clientList.size() > 0) {
                mDataBase.clientDao().deleteAll();
                mDataBase.clientDao().insertAll(clientList);
                onSearchFinished(clientList);
            }
            onResult(true);
        } else {
            onResult(false);
        }
    }

    private void onResult(boolean result) {
        if (result) {
            onSyncResult(R.string.syncing_finished_success);
        } else {
            onSyncResult(R.string.syncing_failed);
        }
    }

    private void onSearchFinished(final List<Client> clientsList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onSearchFinished(clientsList);
                }
            }
        });
    }

    private void onClientSavedSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onClientSavedSuccess();
                }
            }
        });
    }

    private void onSyncResult(final int resourceMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onSyncResult(resourceMessage);
                }
            }
        });
    }

    private void runOnUiThread(Runnable runnable) {
        if (mHandler != null) {
            mHandler.post(runnable);
        }
    }

    public void setDataBaseListener(OnDataBaseChangedListener listener) {
        mListener = listener;
    }

    public interface OnDataBaseChangedListener {
        void onClientSavedSuccess();

        void onSearchFinished(List<Client> clientsList);

        void onSyncResult(int resourceMessage);
    }
}
