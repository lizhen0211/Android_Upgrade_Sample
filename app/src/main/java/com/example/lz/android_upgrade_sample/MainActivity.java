package com.example.lz.android_upgrade_sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private UpgradeManager upgradeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upgradeManager = new UpgradeManager(this);
    }

    public void appDownLoad(View view) {
        UpdateInfo updateInfo = upgradeManager.getVersionInfo();
        if (upgradeManager.checkUpdate(updateInfo)) {
            upgradeManager.readyToDownLoad(updateInfo.getUrl(), getResources().getString(R.string.app_name), "下载完成后点击打开");
        }
    }

    public void browerDownLoad(View view) {
        UpdateInfo updateInfo = upgradeManager.getVersionInfo();
        if (upgradeManager.checkUpdate(updateInfo)) {
            Uri uri = Uri.parse(updateInfo.getUrl());
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        }
    }
}
