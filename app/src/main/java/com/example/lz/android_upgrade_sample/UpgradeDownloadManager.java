package com.example.lz.android_upgrade_sample;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by lz on 2016/11/22.
 */

public class UpgradeDownloadManager {

    private DownloadManager downloadManager;


    public UpgradeDownloadManager(Context context) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * 开始下载
     *
     * @param uriStr
     * @param title
     * @param description
     * @return
     */
    public long startDownLoad(String uriStr, String title, String description) {
        DownloadManager.Request request = buildRequest(uriStr, title, description);
        return downloadManager.enqueue(request);
    }

    /**
     * 构建下载请求
     *
     * @param uriStr
     * @param title
     * @param description
     * @return
     */
    private DownloadManager.Request buildRequest(String uriStr, String title, String description) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uriStr));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(title);
        request.setDescription(description);
        request.setAllowedOverRoaming(false);
        //file:///storage/emulated/0/Download/update.apk
        String apkName = "defaultApkName";
        int index = uriStr.lastIndexOf("/");
        if (uriStr.length() > index + 1) {
            apkName = uriStr.substring(index + 1);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);
        return request;
    }

    /**
     * 获取保存文件的地址
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     */
    public Uri getDownloadUri(long downloadId) {
        return downloadManager.getUriForDownloadedFile(downloadId);
    }

    /**
     * 获取下载状态
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return int
     * @see DownloadManager#STATUS_PENDING
     * @see DownloadManager#STATUS_PAUSED
     * @see DownloadManager#STATUS_RUNNING
     * @see DownloadManager#STATUS_SUCCESSFUL
     * @see DownloadManager#STATUS_FAILED
     */
    public int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));

                }
            } finally {
                c.close();
            }
        }
        return -1;
    }
}
