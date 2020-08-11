package com.zhaisoft.lib.updater.example;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.zhaisoft.lib.mvp.base.BaseMultiDexApplication;


public class BaseApplication extends BaseMultiDexApplication {
    String TAG = "BaseApplication";
    private static BaseApplication instance;


    public void onCreate() {
        super.onCreate();
        instance = this;
        //打开日志开关

        //CrashWoodpecker.init(this);


    }


}
