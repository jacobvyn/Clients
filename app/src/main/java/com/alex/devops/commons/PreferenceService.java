package com.alex.devops.commons;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.alex.devops.R;

public class PreferenceService {
    private final SharedPreferences mPrefs;
    private static final String BACKGROUND_COLOR = "BACKGROUND_COLOR";
    private static final String LAST_SYNC = "LAST_SYNC";
    private static final String MAX_VISIT_AMOUNT = "MAX_VISIT_AMOUNT";
    private static final String SYNC_URL_KEY = "SYNC_URL_KEY";
    private static final String CREATE_URL_KEY = "CREATE_URL_KEY";
    private static final String BASE_URL_KEY = "BASE_URL_KEY";
    private String CREATE_PATH = "/chugger/create";
    private String SYNC_PATH = "/chugger/sync";

    public PreferenceService(Context context) {
        mPrefs = getPrefs(context);
    }

    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PreferenceService.class.getName(), Application.MODE_PRIVATE);
    }

    public void setBackgroundColor(int color) {
        mPrefs.edit().putInt(BACKGROUND_COLOR, color).apply();
    }

    public int getBackgroundColor() {
        return mPrefs.getInt(BACKGROUND_COLOR, R.color.white);
    }

    public void setLastTimeSync() {
        mPrefs.edit().putLong(LAST_SYNC, System.currentTimeMillis()).apply();
    }

    public long getLastTimeSync() {
        return mPrefs.getLong(LAST_SYNC, System.currentTimeMillis());
    }

    public void setMaxVisitAmount(int visitAmount) {
        mPrefs.edit().putInt(MAX_VISIT_AMOUNT, visitAmount).apply();
    }

    public int getMaxVisitAmount() {
        return mPrefs.getInt(MAX_VISIT_AMOUNT, 1);
    }

    public void setBaseURL(String baseURL) {
        mPrefs.edit().putString(BASE_URL_KEY, baseURL).apply();
        mPrefs.edit().putString(SYNC_URL_KEY, baseURL + SYNC_PATH).apply();
        mPrefs.edit().putString(CREATE_URL_KEY, baseURL + CREATE_PATH).apply();

    }

    public String getBaseURL() {
        return mPrefs.getString(BASE_URL_KEY, "");
    }

    public String getCreateURL() {
        return mPrefs.getString(CREATE_URL_KEY, "");
    }

    public String getSyncURL() {
        return mPrefs.getString(SYNC_URL_KEY, "");
    }
}