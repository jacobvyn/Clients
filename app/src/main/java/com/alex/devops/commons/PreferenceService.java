package com.alex.devops.commons;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.alex.devops.R;

import java.util.Date;

public class PreferenceService {
    public static final String BACKGROUND_COLOR = "BACKGROUND_COLOR";
    public static final String LAST_SYNC = "LAST_SYNC";


    private final SharedPreferences mPrefs;

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
}