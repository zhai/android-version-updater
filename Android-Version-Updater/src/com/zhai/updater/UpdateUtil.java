package com.zhai.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.zhai.utils.HttpUtil;
import com.zhai.utils.NetUtil;
import com.zhai.utils.PropertyUtil;
import com.zhai.utils.VersionUtil;

public class UpdateUtil {

	static int DIALOG_UPDATE = 100;

	private static String currentVersionName = "";

	public static boolean checkUpdate(Context context, Handler updateHandler) {

		// 1.检测网络是否连接
		if (!NetUtil.isNetWorkConnected(context)) {
			return false;
		}
		//
		currentVersionName = VersionUtil.getAppVersionName(context);

		String url = null;
		try {
			url = PropertyUtil.getPropertyFromSrcString("update.properties")
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

			UpdateConfig.hostUrl = prop.getProperty("host");
			if (UpdateConfig.version_name.compareTo(currentVersionName) != 0) {
				// 有更新，弹出对话框
				Message msg = updateHandler.obtainMessage(DIALOG_UPDATE, null);
				updateHandler.sendMessage(msg);

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

	public static void loadFile(String url, Handler updateHandler) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				sendMsg(-404, 0, updateHandler); // 文件不存在
				return;
			}

			HttpEntity entity = response.getEntity();
			float length = entity.getContentLength();

			UpdateConfig.MB = length;
			InputStream is = entity.getContent();
			FileOutputStream fileOutputStream = null;
			if (is != null) {
				File file = new File(Environment.getExternalStorageDirectory(),
						UpdateConfig.apkName);
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				float count = 0;
				while ((ch = is.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ch);
					count += ch;
					UpdateConfig.KB = count;
					sendMsg(1, (int) (count * 100 / length), updateHandler);
				}
			}
			sendMsg(2, 0, updateHandler);
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (Exception e) {
			sendMsg(-1, 0, updateHandler);
		}
	}

	private static void sendMsg(int what, int arg, Handler updateHandler) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = arg;
		updateHandler.sendMessage(msg);
	}

	public static void checkUpdateThread(final Context applicationContext,
			final Handler updateHandler) {
		Thread updateThread = new Thread() {
			@Override
			public void run() {
				UpdateUtil.checkUpdate(applicationContext, updateHandler);
				super.run();
			}
		};
		updateThread.start();

	}

}
