package com.alex.devops.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.alex.devops.utils.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemoteSync {
    private static final String REQUEST_BODY_MEDIA_TYPE = "application/json; charset=utf-8";
    private MediaType JSON = MediaType.parse(REQUEST_BODY_MEDIA_TYPE);
    private Callback mCallback;
    private Handler mHandler;
    private OkHttpClient okHttpClient;

    public RemoteSync(Callback callback) {
        mCallback = callback;
        mHandler = new Handler((Looper.getMainLooper()));
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .build();
    }

    public List<Client> getAllClients() {
        List<Client> list = new ArrayList<>();
        try {
            Request request = new Request.Builder()
                    .url(Constants.URL_SYNC)
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {
                if (response.body() != null) {
                    String respAsString = response.body().string();
                    return Utils.parseResponse(respAsString);
                }
            } else {
                Log.e("+++", "fail");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void doPostRequest(List<Client> clientList, boolean sync) {
        try {
            Request request = new Request.Builder()
                    .url(Constants.URL_CREATE)
                    .post(retrieveBody(clientList))
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            handleResponse(response, sync);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RequestBody retrieveBody(List<Client> clientList) {
        String json = Utils.toJson(clientList);
        return RequestBody.create(JSON, json);
    }

    private void handleResponse(Response response, boolean sync) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            onRequestSuccess(sync);
        } else {
            onRequestFailed(response.message());
        }
    }

    private void onRequestSuccess(final boolean sync) {
        runOnUi(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onRequestSuccess(sync);
                }
            }
        });
    }

    private void onRequestFailed(final String message) {
        runOnUi(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onRequestFailed(message);
                }
            }
        });
    }

    private void runOnUi(Runnable runnable) {
        if (mHandler != null) {
            mHandler.post(runnable);
        }
    }

    public interface Callback {
        void onRequestSuccess(boolean sync);

        void onRequestFailed(String message);
    }
}
