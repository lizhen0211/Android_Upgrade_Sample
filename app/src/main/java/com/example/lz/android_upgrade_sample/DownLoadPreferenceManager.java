package com.example.lz.android_upgrade_sample;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lz on 2016/11/23.
 */

public class DownLoadPreferenceManager {

    private static DownLoadPreferenceManager instance;

    private SharedPreferences downLoadPreference;

    private static final String DOWNLOAD_ID = "downLoadID";

    private DownLoadPreferenceManager(Context context) {
        downLoadPreference = context.getApplicationContext().getSharedPreferences("downLoadPreference", Context.MODE_PRIVATE);
    }

    public synchronized static DownLoadPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new DownLoadPreferenceManager(context);
        }
        return instance;
    }

    public long getDownLoadID() {
        return downLoadPreference.getLong(DOWNLOAD_ID, -1);
    }

    public void putDownLoadID(long downLoadID) {
        downLoadPreference.edit().putLong(DOWNLOAD_ID, downLoadID).apply();
    }

    public void removeDownLoadID() {
        downLoadPreference.edit().remove(DOWNLOAD_ID).apply();
    }
}
