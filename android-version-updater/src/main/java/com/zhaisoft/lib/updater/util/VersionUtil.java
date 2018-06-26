package com.zhaisoft.lib.updater.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.zhaisoft.lib.updater.BuildConfig;

/**
 * @author zhai
 */
public class VersionUtil {
    public static String getAppVersionName(Context context) {
        return BuildConfig.VERSION_NAME;
    }

    public static int getAppVersionCode(Context context) {
        return BuildConfig.VERSION_CODE;
    }
}
