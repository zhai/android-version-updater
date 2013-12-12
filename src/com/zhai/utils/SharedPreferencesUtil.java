package com.zhai.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

	/**
	 * 此类已修正完成
	 * 
	 * @author zhai
	 * 
	 */

	/**
	 * 使用实例
	 * 
	 * SharedPreferencesUtil.setString(SharedPreferencesUtil.
	 * getSharedPreferences(context,"preferencesName"),"name","value");
	 * SharedPreferencesUtil
	 * .getString(SharedPreferencesUtil.getSharedPreferences
	 * (context,"preferencesName"),"name","defvalue");
	 */

	/**
	 * 1.通过context得妻当前的preferencesName
	 * 
	 * @param context
	 * @param preferencesName
	 *            配置名
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Context context,
			String preferencesName) {
		return context.getSharedPreferences(preferencesName,
				Context.MODE_PRIVATE);
	}

	/**
	 * 
	 * @param preferencesName
	 * @param name
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static String getString(SharedPreferences preferencesName,
			String name, String defaultValue) {
		return preferencesName.getString(name, defaultValue);
	}

	/**
	 * 
	 * @param preferencesName
	 * @param name
	 * @param value
	 *            设置的值
	 */

	public static void setString(SharedPreferences preferencesName,
			String name, String value) {
		SharedPreferences.Editor editor = preferencesName.edit();
		editor.putString(name, value);
		editor.commit();
	}

	// 3.得到一系列的Stirng

	// 4.设置一系列的String
}
