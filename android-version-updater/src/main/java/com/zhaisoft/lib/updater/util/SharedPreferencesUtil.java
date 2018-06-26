package com.zhaisoft.lib.updater.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * @author zhai
 */
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
     * @param preferencesName 配置名
     * @return
     */

    public static SharedPreferences getPreferences(Context context,
                                                   String preferencesName) {
        return context.getSharedPreferences(preferencesName,
                Context.MODE_PRIVATE);
    }

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void clearAllDefaultSharedPreferences(Context context,
                                                        String preferencesName) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context)
                .edit();
        editor.clear();
        editor.commit();
    }

    public static void clearSharedPreferencesKey(Context context, String key) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context)
                .edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * @param preferencesName
     * @param name
     * @param defaultValue    默认值
     * @return
     */
    public static String getString(SharedPreferences preferencesName,
                                   String name, String defaultValue) {
        return preferencesName.getString(name, defaultValue);
    }

    public static String getDefaultSharedPreferencesString(Context context,
                                                           String name) {
        return getDefaultSharedPreferences(context).getString(name, "");
    }

    public static int getInt(SharedPreferences preferencesName, String name,
                             int defaultValue) {
        return preferencesName.getInt(name, defaultValue);
    }

    /**
     * @param preferencesName
     * @param name
     * @param value           设置的值
     */

    public static void setString(SharedPreferences preferencesName,
                                 String name, String value) {
        SharedPreferences.Editor editor = preferencesName.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static void setInt(SharedPreferences preferencesName, String name,
                              int value) {
        SharedPreferences.Editor editor = preferencesName.edit();
        editor.putInt(name, value);
        editor.commit();
    }
    // 3.得到一系列的Stirng

    // 4.设置一系列的String
}
