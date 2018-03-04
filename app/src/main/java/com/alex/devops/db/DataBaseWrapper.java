package com.alex.devops.db;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alex.devops.commons.BaseActivity;
import com.alex.devops.net.RemoteSync;
import com.alex.devops.utils.ExecutorHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DataBaseWrapper implements RemoteSync.Callback {
    private static DataBaseWrapper sInstance;

    private volatile OnDataBaseChangedListener mListener;
    private ClientsDataBase mDataBase;
    private RemoteSync mRemoteSync;
    private Handler mHandler;


    private DataBaseWrapper(BaseActivity activity) {
        mDataBase = ClientsDataBase.getDataBase(activity);
        mRemoteSync = new RemoteSync(this);
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

    public void sendToServer(final long lastTimeSync) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                List<Client> nonSyncedClients = mDataBase.clientDao().getAllClientsAfter(lastTimeSync);
                if (nonSyncedClients.size() > 0) {
                    mRemoteSync.doPostRequest(nonSyncedClients, true);
                }
            }
        };
        ExecutorHelper.submit(task);
    }

    public void getAllFromServer() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                List<Client> clientList = mRemoteSync.getAllClients();
                onReceivedClientsSuccess(clientList);
            }
        };
        ExecutorHelper.submit(task);
    }

    public void insertClientRemote(final Client client) {
        ExecutorHelper.submit(new Runnable() {
            @Override
            public void run() {
                mRemoteSync.doPostRequest(Arrays.asList(client), false);
            }
        });

    }

    public void replaceClients(final List<Client> clients) {
        ExecutorHelper.submit(new Runnable() {
            @Override
            public void run() {
                mDataBase.clientDao().deleteAll();
                mDataBase.clientDao().insertAll(clients);
                onSearchFinished(clients);
                onSyncResult();
            }
        });
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

    private void onSyncResult() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onSyncResult();
                }
            }
        });
    }

    private void onReceivedClientsSuccess(final List<Client> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onReceivedClientsSuccess(list);
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

    @Override
    public void onRequestSuccess(boolean sync) {
        if (sync) {
            onSyncResult();
        }
    }

    @Override
    public void onRequestFailed(String message) {
//        Toast.makeText(this, "Client sent to server failed", Toast.LENGTH_SHORT).show();
        // TODO: 3/1/18
    }

    public interface OnDataBaseChangedListener {
        void onClientSavedSuccess();

        void onSearchFinished(List<Client> clientsList);

        void onSyncResult();

        void onReceivedClientsSuccess(List<Client> list);
    }
}
