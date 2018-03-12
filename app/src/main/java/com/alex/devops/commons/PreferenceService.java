package com.alex.devops.commons;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.alex.devops.R;
import com.alex.devops.db.Credentials;

public class PreferenceService {
    private final SharedPreferences mPrefs;
    private static final String BACKGROUND_COLOR = "BACKGROUND_COLOR";
    private static final String LAST_SYNC = "LAST_SYNC";
    private static final String MAX_VISIT_AMOUNT = "MAX_VISIT_AMOUNT";
    private static final String SYNC_URL_KEY = "SYNC_URL_KEY";
    private static final String CREATE_URL_KEY = "CREATE_URL_KEY";
    private static final String DOMAIN_KEY = "DOMAIN_KEY";
    private String CREATE_PATH = "/chugger/create";
    private String SYNC_PATH = "/chugger/sync";
    private String LOGIN_KEY = "LOGIN_KEY";
    private String PASSWORD_KEY = "PASSWORD_KEY";

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
        return mPrefs.getLong(LAST_SYNC, 1520890510543L);
    }

    public void setMaxVisitAmount(int visitAmount) {
        mPrefs.edit().putInt(MAX_VISIT_AMOUNT, visitAmount).apply();
    }

    public int getMaxVisitAmount() {
        return mPrefs.getInt(MAX_VISIT_AMOUNT, 1);
    }

    private String getCreateURL() {
        return mPrefs.getString(CREATE_URL_KEY, "");
    }

    private String getSyncURL() {
        return mPrefs.getString(SYNC_URL_KEY, "");
    }

    public void setCredentials(Credentials credentials) {
        String domain = credentials.getDomain();
        mPrefs.edit().putString(DOMAIN_KEY, domain).apply();
        mPrefs.edit().putString(SYNC_URL_KEY, domain + SYNC_PATH).apply();
        mPrefs.edit().putString(CREATE_URL_KEY, domain + CREATE_PATH).apply();

        String login = credentials.getLogin();
        mPrefs.edit().putString(LOGIN_KEY, login).apply();

        String password = credentials.getPassword();
        mPrefs.edit().putString(PASSWORD_KEY, password).apply();
    }

    public Credentials getCredentials() {
        String domain = mPrefs.getString(DOMAIN_KEY, "");
        String login = mPrefs.getString(LOGIN_KEY, "");
        String password = mPrefs.getString(PASSWORD_KEY, "");
        String syncURL = getSyncURL();
        String createURL = getCreateURL();
        return new Credentials(login, password, domain, syncURL, createURL);
    }
}