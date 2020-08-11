package com.zhaisoft.lib.updater;

import android.content.Context;
import android.content.Intent;

public class AndroidUpdateSDK {
    private static AndroidUpdateSDK instance = null;

    public synchronized static AndroidUpdateSDK getInstance() {
        if (instance == null) {
            instance = new AndroidUpdateSDK();
        }
        return instance;
    }

    /**
     * @param _context   ApplicationContext
     * @param needTips   是否需要弹出提示  true:是 已经是最新版本  false:不提出提示
     * @param upgradeUrl 检测更新的网址
     */
    public void init(Context _context, boolean needTips, String upgradeUrl) {

        Intent CheckUpdateServiceIntent = new Intent(_context,
                CheckUpdateService.class);
        CheckUpdateServiceIntent.putExtra("needTips", needTips);
        CheckUpdateServiceIntent.putExtra("url", upgradeUrl);
        _context.startService(CheckUpdateServiceIntent);
    }
}
