package com.alex.devops.net;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.alex.devops.BuildConfig;
import com.alex.devops.db.Client;
import com.alex.devops.db.Credentials;
import com.alex.devops.utils.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;


public class HttpService {
    public static final String LOG_TAG = HttpService.class.getSimpleName();
    private static final String REQUEST_BODY_MEDIA_TYPE = "application/json; charset=utf-8";
    private MediaType JSON = MediaType.parse(REQUEST_BODY_MEDIA_TYPE);
    private OkHttpClient okHttpClient;
    private Credentials mCredentials;

    public HttpService(Credentials credentials) {
        mCredentials = credentials;
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(credentials))
                .build();
    }

    public List<Client> getAllClients() {
        if (TextUtils.isEmpty(mCredentials.getSyncURL())) {
            Log.e(LOG_TAG, " syncURL  is empty or null");
            return null;
        }
        try {
            Request request = new Request.Builder()
                    .url(mCredentials.getSyncURL())
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            return handleResponse(response);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean doPostRequest(List<Client> clientList) {
        if (TextUtils.isEmpty(mCredentials.getCreateURL())) {
            Log.e(LOG_TAG, " createURL is empty or null");
            return false;
        }
        try {
            Request request = new Request.Builder()
                    .url(mCredentials.getCreateURL())
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
            Log.e("HttpService.Debug", "bad response code: " + response.code() + " message: " + response.message());
        }

        return null;
    }
}
