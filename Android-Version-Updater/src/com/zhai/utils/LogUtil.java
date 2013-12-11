package com.zhai.utils;

import java.lang.reflect.Field;
import java.util.List;

import android.util.Log;

import com.zhai.updater.BuildConfig;

public class LogUtil {

	/* Updateed */
	private static final String TAG = "LogUtil";

	public static void d(String tag, String msg) {
		// if (bLog && Log.isLoggable(tag, Log.DEBUG)) {
		// Log.d(tag, msg);
		// }

		if (BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}

	}

	public static void i(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.i(tag, msg);
		}

	}

	public static void e(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.v(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.w(tag, msg);
		}
	}

	public static <T> String printObject(T outClass) {
		StringBuffer sb = new StringBuffer();
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "打印类:" + outClass.getClass().getName());
			for (Field filed : outClass.getClass().getFields()) {
				try {
					Object value = filed.get(outClass);
					if (value != null) {
						Log.e(TAG, filed.getName() + ":" + value);
						sb.append(filed.getName() + ":" + value + "\n");
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public static <T> String printObjectList(List<T> outClassList) {
		StringBuffer sb = new StringBuffer();
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "打印类:" + outClassList.getClass().getName());
			int i = 0;

			for (T outClass : outClassList) {
				sb.append("第" + i + "个:\n");
				for (Field filed : outClass.getClass().getFields()) {
					try {
						Object value = filed.get(outClass);
						if (value != null) {
							Log.e(TAG, filed.getName() + ":" + value);
							sb.append(filed.getName() + ":" + value + "\n");
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				i++;
			}
		}
		return sb.toString();
	}

}
