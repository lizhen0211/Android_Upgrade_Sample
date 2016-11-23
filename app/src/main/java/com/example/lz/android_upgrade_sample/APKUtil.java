package com.example.lz.android_upgrade_sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by lz on 2016/11/23.
 */

public class APKUtil {

    public static void installApk(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
