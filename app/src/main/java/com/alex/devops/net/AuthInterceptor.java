package com.alex.devops.net;

import android.util.Base64;

import com.alex.devops.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String authorValue = encodeCredentialsForBasicAuthorization();
        Request request = chain
                .request()
                .newBuilder()
                .addHeader("Authorization", authorValue)
                .build();
        return chain.proceed(request);
    }


    private String encodeCredentialsForBasicAuthorization() {
        final String userAndPassword = BuildConfig.USER_NAME + ":" + BuildConfig.PASSWORD;
        return "Basic " + Base64.encodeToString(userAndPassword.getBytes(), Base64.NO_WRAP);
    }
}
