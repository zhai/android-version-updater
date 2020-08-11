package com.zhaisoft.lib.updater;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.zhaisoft.lib.mvp.util.ThreadManager;
import com.zhaisoft.lib.updater.model.VOApk;
import com.zhaisoft.lib.updater.util.HttpUtil;
import com.zhaisoft.lib.updater.util.PropertyUtil;
import com.zhaisoft.lib.updater.util.VersionUtil;

import java.io.InputStream;
import java.util.Properties;

/***
 * author zhai
 */
public class CheckUpdateService extends IntentService {

    Context context;
    private static final String TAG = CheckUpdateService.class.getSimpleName();
    private String currentVersionName = "CheckUpdateService";

    VOApk mVOApk = new VOApk();

    public CheckUpdateService() {
        super("");
        this.context = CheckUpdateService.this;

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean needTips = intent.getBooleanExtra("needTips", false);
        String url = intent.getStringExtra("url");
        checkUpdate(needTips, url);
    }


    private boolean checkUpdate(boolean needTips, String update_url) {
        currentVersionName = VersionUtil.getLocalVersionName(context);
        try {
            InputStream is = HttpUtil.getInputStreamFormUrl(update_url);
            Properties prop = PropertyUtil.getPropertyFromInputStream(is);
            mVOApk.version_name = prop.getProperty("version_name");
            mVOApk.apk = prop.getProperty("apk");
            if (!mVOApk.apk.contains(".apk")) {
                mVOApk.apk = mVOApk.apk + "-"
                        + mVOApk.version_name + ".apk";
            }

            mVOApk.info = prop.getProperty("info");
            mVOApk.force_update = prop.getProperty("force_update")
                    .equals("1") ? true : false;


            Log.e(TAG, "UpdateConfig.version_name=" + mVOApk.version_name);
            Log.e(TAG, "currentVersionName=" + currentVersionName);
            Log.e(TAG, "compareVersion=" + compareVersion(mVOApk.version_name, currentVersionName));

            if (compareVersion(mVOApk.version_name, currentVersionName) > 0) {


                Log.e(TAG, "需要更新一次");
                showUpdateDialog();

                return true;
            } else {

                if (needTips) {
                    ThreadManager.runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "已经是最新版本",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return false;
            }
        } catch (final Exception e) {
            e.printStackTrace();

            if (needTips) {

                ThreadManager.runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "已经是最新版本", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        }
        return false;
    }

    private void showUpdateDialog() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle b = new Bundle();
        b.putSerializable(VOApk.class.getName(), mVOApk);
        intent.putExtras(b);
        intent.setClass(context, ActivityVersionUpdate.class);
        context.startActivity(intent);
    }


    /**
     * useful
     *
     * @param version1
     * @param version2
     * @return
     * @throws Exception
     */
    public int compareVersion(String version1, String version2) throws Exception {
        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }
}
