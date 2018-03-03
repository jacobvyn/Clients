package com.alex.devops.commons;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.alex.devops.R;

import java.util.Date;

public class PreferenceService {
    private static final String BACKGROUND_COLOR = "BACKGROUND_COLOR";
    private static final String LAST_SYNC = "LAST_SYNC";
    private static final String MAX_VISIT_AMOUNT = "MAX_VISIT_AMOUNT";
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

    public void setMaxVisitAmount(int visitAmount) {
        mPrefs.edit().putInt(MAX_VISIT_AMOUNT, visitAmount).apply();
    }

    public int getMaxVisitAmount() {
        return mPrefs.getInt(MAX_VISIT_AMOUNT, 1);
    }
}