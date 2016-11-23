package com.example.lz.android_upgrade_sample;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by lz on 2016/11/23.
 */

public class DownLoadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downLoadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            long downLoadIDCache = DownLoadPreferenceManager.getInstance(context).getDownLoadID();
            if (downLoadID == downLoadIDCache) {
                DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = dManager.getUriForDownloadedFile(downLoadID);
                APKUtil.installApk(context, uri);
            }
        }
    }
}
