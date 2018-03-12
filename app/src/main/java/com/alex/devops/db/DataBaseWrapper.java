package com.alex.devops.db;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alex.devops.R;
import com.alex.devops.commons.BaseActivity;
import com.alex.devops.net.DBInterface;
import com.alex.devops.net.HttpService;
import com.alex.devops.utils.ExecutorHelper;

import java.util.ArrayList;
import java.util.List;


public class DataBaseWrapper implements DBInterface {
    private static DataBaseWrapper sInstance;

    private volatile OnDataBaseChangedListener mListener;
    private ClientsDataBase mDataBase;
    private Handler mHandler;


    private DataBaseWrapper(BaseActivity activity) {
        mDataBase = ClientsDataBase.getDataBase(activity);
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


    private void runOnUiThread(Runnable runnable) {
        if (mHandler != null) {
            mHandler.post(runnable);
        }
    }

    public void setDataBaseListener(OnDataBaseChangedListener listener) {
        mListener = listener;
    }

    @Override
    public List<Client> getAllClientsSyncAfter(long time) {
        if (mDataBase != null) {
            return mDataBase.clientDao().getAllClientsAfter(time);
        } else {
            return new ArrayList<Client>();
        }
    }

    @Override
    public void deleteAllSync() {
        if (mDataBase != null) {
            mDataBase.clientDao().deleteAll();
        }
    }

    @Override
    public void insertAllSync(List<Client> clientList) {
        if (mDataBase != null) {
            mDataBase.clientDao().insertAll(clientList);
        }
    }

    public interface OnDataBaseChangedListener {
        void onClientSavedSuccess();

        void onSearchFinished(List<Client> clientsList);
    }
}
