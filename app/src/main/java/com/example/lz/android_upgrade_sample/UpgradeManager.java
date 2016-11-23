package com.example.lz.android_upgrade_sample;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.util.List;

/**
 * Created by lz on 2016/11/22.
 */

public class UpgradeManager {

    private Context mContext;

    private UpgradeDownloadManager downloadManager;

    private DownLoadPreferenceManager preferenceManager;

    public UpgradeManager(Context context) {
        mContext = context;
        downloadManager = new UpgradeDownloadManager(context);
        preferenceManager = DownLoadPreferenceManager.getInstance(context);
    }

    /**
     * 从服务器获取版本信息
     *
     * @return
     */
    public UpdateInfo getVersionInfo() {
        UpdateInfo version = new UpdateInfo();
        version.setVersionName("1.0");
        version.setVersionCode(2);
        version.setUrl("http://dldir1.qq.com/weixin/android/weixin6331android940.apk");
        return version;
    }

    public boolean checkUpdate(UpdateInfo updateInfo) {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            int versionCode = packageInfo.versionCode;
            if (updateInfo.getVersionCode() > versionCode) {
                return true;
            }
        }
        return false;
    }

    /**
     * 准备下载APK：
     * <p>
     * 此时已确认版本需要升级，此处校验只是校验APK是否需要重新下载。
     * 如果是新版APK，则安装；否则
     *
     * @param uriStr
     * @param title
     * @param description
     */
    public void readyToDownLoad(String uriStr, String title, String description) {
        long downLoadID = preferenceManager.getDownLoadID();
        if (downLoadID == -1L) {//未下载新版APK
            startDownLoad(uriStr, title, description);
        } else {//已下载新版APK
            int downloadStatus = downloadManager.getDownloadStatus(downLoadID);
            switch (downloadStatus) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    Uri uri = downloadManager.getDownloadUri(downLoadID);
                    if (uri != null) {
                        //本地APK是最新版本
                        if (compareVersionCode(getApkInfo(mContext, uri.getPath()), mContext)) {
                            APKUtil.installApk(mContext, uri);
                        } else {
                            //本地APK不是最新版本，需要重新下载
                            preferenceManager.removeDownLoadID();
                            startDownLoad(uriStr, title, description);
                        }
                    }
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Log.v("download", "应用正在下载，请稍后");
                    break;
                case DownloadManager.STATUS_PENDING:
                    Log.v("download", "应用已开始下载，请稍后");
                    break;
                case DownloadManager.STATUS_PAUSED:
                    Log.v("download", "应用下载已暂停，请重新开启下载");
                    break;
                case DownloadManager.STATUS_FAILED:
                    startDownLoad(uriStr, title, description);
                    break;
                default:
                    startDownLoad(uriStr, title, description);
                    break;
            }
        }
    }

    private void startDownLoad(String uriStr, String title, String description) {
        if (checkDownLoadState()) {
            long downLoadID = downloadManager.startDownLoad(uriStr, title, description);
            preferenceManager.putDownLoadID(downLoadID);
        } else {
            showDownloadSetting();
        }
    }


    /**
     * 显示设置下载组件窗口
     */
    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (isIntentAvailable(intent)) {
            mContext.startActivity(intent);
        }
    }

    /**
     * 是否有可用组件接收intent
     *
     * @param intent
     * @return
     */
    private boolean isIntentAvailable(Intent intent) {
        PackageManager packageManager = mContext.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 检测下载组件是否可用
     *
     * @return
     */
    private boolean checkDownLoadState() {
        try {
            int state = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取apk信息
     *
     * @param context
     * @param path
     * @return
     */
    private PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        return info;
    }

    /**
     * 比较已下载的APK与当前应用版本号
     *
     * @param apkInfo
     * @param context
     * @return
     */
    private boolean compareVersionCode(PackageInfo apkInfo, Context context) {
        if (apkInfo == null) {
            return false;
        }
        String localPackage = context.getPackageName();
        if (apkInfo.packageName.equals(localPackage)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
