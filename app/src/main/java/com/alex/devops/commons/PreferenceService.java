package com.alex.devops.commons;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.alex.devops.R;

public class PreferenceService {
    public static final String BACKGROUND_COLOR = "BACKGROUND_COLOR";


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
}