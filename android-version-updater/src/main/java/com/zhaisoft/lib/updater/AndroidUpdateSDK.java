package com.zhaisoft.lib.updater;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

public class AndroidUpdateSDK {
	private static final String TAG = AndroidUpdateSDK.class.getSimpleName();
	Context context;
	// 注意，这里没有final
	private static AndroidUpdateSDK instance = null;

	// 静态工厂方法
	public synchronized static AndroidUpdateSDK getInstance() {
		if (instance == null) {
			instance = new AndroidUpdateSDK();
		}
		return instance;
	}

	String MESSAGE_IN = "message_in";

	Intent CheckUpdateServiceIntent;

	public void init(Context _context) {
		this.context = _context;
		// 开启一个service
		Intent CheckUpdateServiceIntent = new Intent(context,
				CheckUpdateService.class);

		CheckUpdateServiceIntent.putExtra(MESSAGE_IN, "Activity传给serive的参数");
		context.startService(CheckUpdateServiceIntent);
		Log.i(TAG, "启动一个服务");

	}

	public void init(Context _context, boolean needTips) {
		this.context = _context;
		// 开启一个service
		Intent CheckUpdateServiceIntent = new Intent(context,
				CheckUpdateService.class);

		CheckUpdateServiceIntent.putExtra(MESSAGE_IN, "Activity传给serive的参数");
		CheckUpdateServiceIntent.putExtra("needTips", needTips);
		context.startService(CheckUpdateServiceIntent);
		Log.i(TAG, "启动一个服务");

	}

	public void stop() {
		boolean running = isServiceRunning(context,
				CheckUpdateService.class.getName());
		Log.i(TAG, "runing? =" + running);
	}

	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
}
