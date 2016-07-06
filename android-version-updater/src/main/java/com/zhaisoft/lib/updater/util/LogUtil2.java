package com.zhaisoft.lib.updater.util;

import android.util.Log;

import com.zhaisoft.lib.updater.BuildConfig;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 更新LogCat类，增加两个方法
 */
public class LogUtil2 {

    private static final String TAG = "LogUtil2-Zhai";
    public static boolean bLog = BuildConfig.DEBUG;


    public static void d(String tag, String msg) {

        Log.d(tag,    "bLog=" + bLog);
        if (bLog) {
            Log.d(tag, getCurrentMethodName() + "\n" + msg);
        }

    }

    public static void i(String tag, String msg) {
        if (bLog) {
            Log.i(tag, getCurrentMethodName() + "\n" + msg);
        }

    }

    public static void e(String tag, String msg) {
        if (bLog) {
            Log.e(tag, getCurrentMethodName() + "\n" + msg);
        }
    }

    public static void v(String tag, String msg) {
        if (bLog) {
            Log.v(tag, getCurrentMethodName() + "\n" + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (bLog) {
            Log.w(tag, getCurrentMethodName() + "\n" + msg);
        }
    }


    /**
     * 打印对象
     *
     * @param outClass
     * @param <T>
     * @return
     */

    public static <T> String printObject(T outClass) {
        StringBuffer sb = new StringBuffer();
        if (bLog) {
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

    /**
     * 打印一个List
     *
     * @param outClassList
     * @param <T>
     * @return
     */
    public static <T> String printObjectList(List<T> outClassList) {
        StringBuffer sb = new StringBuffer();
        if (bLog) {
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


    /**
     * 通过反射得到当前的方法名， Android专用，Java不能使用此方法
     *
     * @return 方法名
     */
    public static String getCurrentMethodName() {
        int level = 1;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String methodName = stacks[level].getMethodName();
        return methodName;
    }


    /**
     * 通过反射得到当前的类名， Android专用
     *
     * @return 方法名
     */
    public static String getCurrentClassName() {
        int level = 1;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String className = stacks[level].getClassName();
        return className;
    }


}
