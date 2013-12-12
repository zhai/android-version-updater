package com.zhai.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.zhai.updater.Activity_Verison_Update;
import com.zhai.updater.UpdateConfig;
import com.zhai.utils.HttpUtil;
import com.zhai.utils.NetUtil;
import com.zhai.utils.PropertyUtil;
import com.zhai.utils.VersionUtil;

/**
 * 查测自动更新的工具类
 * 
 * @author michael
 * 
 */
public class AndroidUpdateSDK2 {

	private static final String TAG = AndroidUpdateSDK2.class.getSimpleName();

	// 注意，这里没有final
	private static AndroidUpdateSDK2 instance = null;

	// 静态工厂方法
	public synchronized static AndroidUpdateSDK2 getInstance() {
		if (instance == null) {
			instance = new AndroidUpdateSDK2();
		}
		return instance;
	}

	private String currentVersionName = "";
	CheckUpdateTask mCheckUpdateTask;
	Context context;

	public void runUpdateHostTask(Context _context) {

		this.context = _context;
		if (mCheckUpdateTask != null
				&& mCheckUpdateTask.getStatus() != AsyncTask.Status.FINISHED) {
			// != AsyncTask.Status.FINISHED loginTask.getStatus() ==
			// AsyncTask.Status.RUNNING
			// 判断AsyncTask是否正在运行,如果正在运行 那么取消掉这个
			mCheckUpdateTask.cancel(true);
			// progressBar_login.setVisibility(View.GONE);
		}
		mCheckUpdateTask = new CheckUpdateTask(); // 实例化抽象AsyncTask
		mCheckUpdateTask.execute(); // 调用AsyncTask，传入url参数
	}

	public void stopUpdateHostTask() {

		if (mCheckUpdateTask != null
				&& mCheckUpdateTask.getStatus() != AsyncTask.Status.FINISHED) {
			// != AsyncTask.Status.FINISHED loginTask.getStatus() ==
			// AsyncTask.Status.RUNNING
			// 判断AsyncTask是否正在运行,如果正在运行 那么取消掉这个
			mCheckUpdateTask.cancel(true);
			// progressBar_login.setVisibility(View.GONE);
		}

	}

	// void Update(Context context) {
	// if (!NetUtil.isNetWorkConnected(context)) {
	// return; // 如果网络不能链接， 退出
	// }
	// initSDK(context);
	// checkUpdate(context);
	// }

	/**
	 * 初始化一些配置
	 */
	static void initSDK(Context context) {
		// 更新周期，是否更新
	}

	private boolean checkUpdate() {
		// TODO Auto-generated method stub
		currentVersionName = VersionUtil.getAppVersionName(context);
		String url = null;
		try {
			url = PropertyUtil.getPropertyFromSrcString("update.config")
					.getProperty("Update_Url");
		} catch (Exception e1) {
			// 不存在的情况
			e1.printStackTrace();
		}
		try {
			InputStream is = HttpUtil.getInputStreamFormUrl(url);
			Properties prop = PropertyUtil.getPropertyFromInputStream(is);
			UpdateConfig.version_name = prop.getProperty("version_name");
			UpdateConfig.apk = prop.getProperty("apk");
			// 更新新的apk格式
			// apk=http://code.taobao.org/svn//zhaisoft/branches/zhaisms/zhaisms
			if (!UpdateConfig.apk.contains(".apk")) {
				UpdateConfig.apk = UpdateConfig.apk + "-"
						+ UpdateConfig.version_name + ".apk";
			}

			UpdateConfig.info = prop.getProperty("info");
			UpdateConfig.force_update = prop.getProperty("force_update")
					.equals("1") ? true : false;

			if (UpdateConfig.version_name.compareTo(currentVersionName) != 0 || true) {
				// 有更新，弹出对话框
				// Message msg = updateHandler.obtainMessage(DIALOG_UPDATE,
				// null);
				// updateHandler.sendMessage(msg);

				Log.e(TAG, "需要更新一次");
				Intent intent = new Intent();
				intent.setClass(context, Activity_Verison_Update.class);
				context.startActivity(intent);

				return true;
			} else {

				return false;
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public class CheckUpdateTask extends AsyncTask<Context, Integer, Boolean> {

		CheckUpdateTask() {
			Log.e(TAG, "CheckUpdateTask(Context context)");
		}

		@Override
		protected Boolean doInBackground(Context... params) {
			// 1.检查网络

			if (!NetUtil.isNetWorkConnected(context)) {
				return false; // 如果网络不能链接，直接 退出
			}
			// 执行成功 执行true
			// 2.检查是否有新版本
			return checkUpdate();
		}
	}

}
