package com.zhai.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class UpdateUtil {

	public static String updateTipsUrl;
	public static String MarketUpdateUrl ;
	public static String WebsiteUpdateUrl ;

	public static boolean isNeedUpdate(Activity activity, int time) {

		SharedPreferences prefs = activity.getPreferences(0);
		long lastUpdateTime = prefs.getLong("lastUpdateTime",
				System.currentTimeMillis());

		/* Should Activity Check for Updates Now? */
		if ((lastUpdateTime + time) < System.currentTimeMillis()) {
			// if ((lastUpdateTime + (24 * 60 * 60 * 1000)) <
			// System.currentTimeMillis()) { //一天检测一次
			/* Save current timestamp for next Check */
			lastUpdateTime = System.currentTimeMillis();
			SharedPreferences.Editor editor = activity.getPreferences(0).edit();
			editor.putLong("lastUpdateTime", lastUpdateTime);
			editor.commit();

			/* Start Update */
			// checkUpdate.start();
			return true;
		} else {
			return false;
		}

	}

	public static boolean CheckUpdate(Context context,
			String mCorrentAplicationVersionName, String checkUpdateUrl) {

		boolean returnValue = false;
		HttpGet httpRequest = new HttpGet(checkUpdateUrl);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				String[] response = strResult.split(";");

				if (!response[3].equals("")) {
					updateTipsUrl = response[3];
				}
				if (response[0].equals(mCorrentAplicationVersionName)) // 版本相同不用更新
				{

					// goIndex(); // . do nothing

					// 没有更新
					
					returnValue= false;

				} else {
					// 提示是否更新

					// 有更新, 弹出对话框
					MarketUpdateUrl = response[1];
					WebsiteUpdateUrl = response[2];
					returnValue=  true;

				}
			}
		} catch (ClientProtocolException e) {
			returnValue= false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			returnValue= false;
			e.printStackTrace();
		}
		return returnValue;

	}
	
	
	public static  String getCurrentAplicationVersionName(Activity activity)

	{

		PackageManager manager = activity.getPackageManager();
		String versionName = "";
		try {
			PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
			String packageName = info.packageName;
			int versionCode = info.versionCode;
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
		}

		return versionName;

	}
	

}
