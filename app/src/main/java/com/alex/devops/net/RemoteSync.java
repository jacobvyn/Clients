package com.alex.devops.net;

import android.util.Log;

import com.alex.devops.db.Client;
import com.alex.devops.utils.Constants;
import com.alex.devops.utils.Utils;

import org.json.JSONException;

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
    private OkHttpClient okHttpClient;

    public RemoteSync() {
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
            return handleResponse(response);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean doPostRequest(List<Client> clientList) {
        try {
            Request request = new Request.Builder()
                    .url(Constants.URL_CREATE)
                    .post(retrieveBody(clientList))
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            return response.code() == HttpURLConnection.HTTP_OK;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private RequestBody retrieveBody(List<Client> clientList) {
        String json = Utils.toJson(clientList);
        return RequestBody.create(JSON, json);
    }

    private List<Client> handleResponse(Response response) throws IOException, JSONException {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            if (response.body() != null) {
                String respAsString = response.body().string();
                return Utils.parseResponse(respAsString);
            }
        } else {
            Log.e("RemoteSync.Debug", "bad response code: " + response.code() + " message: " + response.message());
        }

        return new ArrayList<Client>();
    }
}
