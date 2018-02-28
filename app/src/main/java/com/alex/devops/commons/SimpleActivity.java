package com.alex.devops.commons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.alex.devops.R;

public class SimpleActivity extends AppCompatActivity {
    private PreferenceService mPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = new PreferenceService(this);
//        Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    public void storeBackgroundColor(int color) {
        mPrefs.setBackgroundColor(color);
    }

    public int getBackgroundColor() {
        if (mPrefs != null) {
            return mPrefs.getBackgroundColor();
        } else {
            return R.color.colorPrimaryDark;
        }
    }

    public void setLastTimeSync() {
        if (mPrefs != null) {
            mPrefs.setLastTimeSync();
        }
    }

    public long getLastTimeSync() {
        return mPrefs.getLastTimeSync();
    }
}
