package com.zhai.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtil {
	public static boolean isPackageInstalled(Context context, String packageName) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo != null) {
			System.out.println("已经安装");
			return true;
		} else {
			System.out.println("没有安装");
		}
		return false;

	}
}
