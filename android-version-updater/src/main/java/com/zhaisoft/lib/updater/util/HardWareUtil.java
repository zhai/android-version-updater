package com.zhaisoft.lib.updater.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.zhaisoft.lib.updater.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Proxy;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HardWareUtil {
//    private final static String TAG = "HardWareUtil";
//
//    static volatile HardWareUtil instance;
//
//    private static int screenHeight = 0;
//    private static int screenWidth = 0;
//    private static int densityDpi = 0;
//
//    private static int status_bar_height = -1;
//    private static String udid = "";
//    private static String mac = "";
//
//    private SparseArray<Handler> handlers = null;
//
//    public final static int kNetType = 0;
//    public final static int kWapType = 1;
//    private final static int kWapType_172 = 3;
//    private final static int kWapType_200 = 5;
//
//    public static int netTryNum = 3;
//    private static int netTryIndex = 0;
//    private static int netTypeArray[] = new int[3];
//    private static boolean netAvailable = false;
//    public static boolean isWiFi = true;
//
//    //   private BroadcastReceiver mScreenActionReceiver;
//    private static long ScreenOffTime = 0;
//
//    private static MediaPlayer mediaPlayer;
//    private static final float BEEP_VOLUME = 0.10f;
//    private static final long VIBRATE_DURATION = 200L;
//
//    public HardWareUtil(Context context) {
//
//        LogUtil2.d(TAG, "construct hardware");
//        disableConnectionReuseIfNecessary();
//
//
//        setScreenScale(context);
//
//        handlers = new SparseArray<Handler>();
//
//        if (context != null) {
//            mac = getMacAddress(context);
//            getDeviceInfo(context);
//            checkNetworkStatus(context);
//        }
//    }
//
//    public static HardWareUtil getInstance(Context MbApp) {
//        if (instance == null) {
//            synchronized (HardWareUtil.class) {
//                if (instance == null)
//                    instance = new HardWareUtil(MbApp);
//            }
//        }
//        return instance;
//    }
//
//    /**
//     * @param app
//     * @return height or -1;
//     */
//    @SuppressWarnings("rawtypes")
//    public static int getStatusBarHeight(Context app) {
//        if (status_bar_height == -1)
//            try {
//                Class c = Class.forName("com.android.internal.R$dimen");
//                Object obj = c.newInstance();
//                Field field = c.getField("status_bar_height");
//                int x = Integer.parseInt(field.get(obj).toString());
//                status_bar_height = app.getResources().getDimensionPixelSize(x);
//            } catch (Exception e) {
//
//            }
//        return status_bar_height;
//    }
//
//    /**
//     * 19之后状态栏一体化算法
//     *
//     * @param app
//     */
//    @SuppressWarnings("rawtypes")
//    public static int getScreenHeightByVersion(Context app) {
//        int height;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            height = ((Activity) app).getWindowManager().getDefaultDisplay().getHeight();
//        } else {
//            height = ((Activity) app).getWindowManager().getDefaultDisplay().getHeight() - getStatusBarHeight(app);
//        }
//        return height;
//    }
//
//    public static int getScreenHeightWithoutStatueBar(Context app) {
//        int heigth = HardWareUtil.getScreenHeight(app);
//        int statusHeight = HardWareUtil.getStatusBarHeight(app);
//        return heigth - statusHeight;
//    }
//
//    public static int getScreenHeightWithoutStatueBarAndTitleBar(Context app) {
//        int heigth = HardWareUtil.getScreenHeight(app);
//        int statusHeight = HardWareUtil.getStatusBarHeight(app);
//        return heigth - statusHeight - HardWareUtil.dip2px(app, 44);
//    }
//
//    public static void setScreenWidth(int width) {
//        screenWidth = width;
//    }
//
//    public static void setScreenHeight(int height) {
//        screenHeight = height;
//    }
//
//    private static void getScreenSize(Context con) {
//        DisplayMetrics metrics = new DisplayMetrics();
//        WindowManager manager = (WindowManager) con.getSystemService(Context.WINDOW_SERVICE);
//        manager.getDefaultDisplay().getMetrics(metrics);
//        screenWidth = metrics.widthPixels;
//        screenHeight = metrics.heightPixels;
//        densityDpi = metrics.densityDpi;
//    }
//
//    public static int getScreenWidth(Context app) {
//        if (screenWidth == 0 && app != null) {
//            getScreenSize(app);
//        }
//        return screenWidth;
//    }
//
//    public static int getScreenHeight(Context app) {
//        if (screenHeight == 0 && app != null) {
//            getScreenSize(app);
//        }
//        return screenHeight;
//    }
//
//    public static int getDensityDpi() {
//        return densityDpi;
//    }
//
//    /**
//     * @param app
//     * @return scale[](width, height)
//     */
//    public static int[] getScreenScale(Context app) {
//        if (screenWidth == 0 && app != null) {
//            getScreenSize(app);
//        }
//        return new int[]{screenWidth, screenHeight};
//    }
//
//    private void setScreenScale(Context context) {
//        if (screenHeight == 0 && context != null) {
//            getScreenSize(context);
//
//            LogUtil2.d(TAG, "ScreenScale, width=" + screenWidth + ", height=" + screenHeight + ", densityDpi=" + densityDpi);
//        }
//    }
//
//    public static void setViewLayoutParams(View view, double screenWidthRatio, double HeightWidthRatio) {
//        int[] scale = getScale(screenWidthRatio, HeightWidthRatio);
//        view.getLayoutParams().width = scale[0];
//        view.getLayoutParams().height = scale[1];
//    }
//
//    public void Destroy(Context context) {
//        if (handlers != null)
//            handlers.clear();
//        handlers = null;
//
//        instance = null;
//    }
//
//
//    /**
//     * want get msg more comfortable and sharable, you must register a handler
//     * at assigned position using ACT_TAG and, <br>
//     * When you quit from an activity, you should remeWccer to unregister the act
//     * handler
//     *
//     * @param TargetHandler
//     */
//    public void RegisterHandler(Handler TargetHandler, int ActTag) {
//        if (handlers != null) {
//            handlers.put(ActTag, TargetHandler);
//        }
//    }
//
//    /**
//     * when you quit from an activity, you should remeWccer to unregister the act
//     * handler
//     *
//     * @param ActTag
//     */
//    public void UnRegisterHandler(int ActTag) {
//        if (handlers != null) {
//            handlers.remove(ActTag);
//        }
//    }
//
//    public static int[] getScale(double screenRatio) {
//        int width = (int) (screenWidth * screenRatio);
//        int height = (int) (screenHeight * screenRatio);
//        return new int[]{width, height};
//    }
//
//    /**
//     * @param screenWidthRatio
//     * @param HeightWidthRatio Height/Width
//     * @return
//     */
//    public static int[] getScale(double screenWidthRatio, double HeightWidthRatio) {
//        int width = (int) (screenWidth * screenWidthRatio);
//        int height = (int) (width * HeightWidthRatio);
//        return new int[]{width, height};
//    }
//
//    /**
//     * @param width            view width
//     * @param heightWidthRatio view height/width
//     * @return
//     */
//    public static int getScale(int width, double heightWidthRatio) {
//        return (int) (width * heightWidthRatio);
//    }
//
//    /**
//     * @param Columns ����
//     * @return
//     */
//    public static int getCommonGridViewSpace(int Columns) {
//        int space = 2;
//        if (2 == Columns) {
//            if (screenWidth > 480)
//                space = 16;
//            else if (screenWidth > 320)
//                space = 14;
//            else if (screenWidth > 240)
//                space = 8;
//            else
//                space = 6;
//        } else if (3 == Columns) {
//            if (screenWidth > 480)
//                space = 12;
//            else if (screenWidth > 320)
//                space = 10;
//            else if (screenWidth > 240)
//                space = 4;
//            else
//                space = 2;
//        }
//        return space;
//    }
//
//    /**
//     * @param con
//     * @param edge   �߾� dp
//     * @param gap    ��� dp
//     * @param column ����
//     * @return view width px
//     */
//    public static int getGridItemWidth(Context con, int edge, int gap, int column) {
//        double edgePix = dip2px(con, edge);
//        double gapPix = dip2px(con, gap);
//        return (int) ((screenWidth - (edgePix * 2) - (gapPix * (column - 1))) / column);
//    }
//
//    public static int[] getCommonGalleryScale(double num, double SpaceWidthRatio, double HeightWidthRatio) {
//        int width;
//        int height;
//        int space;
//        width = (int) (screenWidth / num * (1 - SpaceWidthRatio));
//        height = (int) (width * HeightWidthRatio);
//        space = (int) (screenWidth / num * SpaceWidthRatio);
//
//        return new int[]{width, height, space};
//    }
//
//
//    public static int[] getSquareScale(double screenWidthRatio) {
//        LogUtil2.i(TAG, "getSquareScale :: screenWidth = " + screenWidth);
//        int width = (int) (screenWidth * screenWidthRatio);
//        int height = width;
//        return new int[]{width, height};
//    }
//
//    public static int[] getSquareScale(Context con, double screenWidthRatio) {
//        LogUtil2.i(TAG, "getSquareScale :: screenWidth = " + screenWidth);
//        int width = (int) (getScreenWidth(con) * screenWidthRatio);
//        int height = width;
//        return new int[]{width, height};
//    }
//
//
//    private void checkNetworkStatus(Context context) {
//        netTryIndex = 0;
//        netTryNum = 3;
//        netTypeArray[0] = kNetType;
//        netTypeArray[1] = kWapType_172;
//        netTypeArray[2] = kWapType_200;
//
//        if (isNetworkAvailableBySystem(context)) {
//            getCurNetworkType(context);
//            netAvailable = true;
//        } else {
//            netAvailable = false;
//        }
//    }
//
//    public static boolean isNetworkAvailable(Context context) {
//        return netAvailable;
//    }
//
//    public static void setNetworkAvailable(boolean v) {
//        netAvailable = v;
//    }
//
//    /**
//     * it is not always actual, but we are sure to return true. so, call it carefully
//     *
//     * @param context
//     * @return
//     */
//    private static boolean isNetworkAvailableBySystem(Context context) {
//        try {
//            NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//            if (info == null)
//                return false;
//
//            if (info.isAvailable() == false)
//                return false;
//            else
//                return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public static boolean isSIMCardAvailable(Context context) {
//        TelephonyManager tm = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        int simState = tm.getSimState();
//        if (simState != TelephonyManager.SIM_STATE_ABSENT)
//            return true;
//        else
//            return false;
//    }
//
//    public static boolean isSDCardAvailable() {
//        boolean result = false;
//        try {
//            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                result = true;
//            } else {
//                result = false;
//            }
//        } catch (Exception e) {
//            result = false;
//        }
//        return result;
//    }
//
//    /**
//     * @return false if remain space <= 50kb
//     */
//    public static boolean isSDCardFull() {
//        if (isSDCardAvailable() == true) {
//            try {
//                File path = Environment.getExternalStorageDirectory();
//                StatFs statFs = new StatFs(path.getPath());
//                long blockSize = statFs.getBlockSize();
//                long availableBlocks = statFs.getAvailableBlocks();
//                if (availableBlocks * blockSize <= 50 * 1024) {
//                    return true;
//                }
//            } catch (Exception e) {
//            }
//        }
//        return false;
//    }
//
//    /**
//     * @param needSpace bytes
//     * @return
//     */
//    public static boolean isSDCardEnoughSpace(long needSpace) {
//        boolean isEnough = false;
//        if (isSDCardAvailable()) {
//            try {
//                File path = Environment.getExternalStorageDirectory();
//                long available = getFreeSpace(path.getPath());
//                if (available >= (10 * 1024 + needSpace)) {
//                    isEnough = true;
//                }
//                if (isEnough == false) {
//                    // release some space
//                }
//                return isEnough;
//            } catch (Exception e) {
//            }
//        }
//        return false;
//    }
//
//    /**
//     * @param path
//     * @return in bytes
//     */
//    private static long getFreeSpace(String path) {
//        StatFs stat = new StatFs(path);
//        long blockSize = stat.getBlockSize();
//        long freeBlocks = stat.getAvailableBlocks();
//        return blockSize * freeBlocks;
//    }
//
//    /**
//     * @return in bytes
//     */
//    public static long getFreeExternalSpace() {
//        if (isSDCardAvailable()) {
//            File path = Environment.getExternalStorageDirectory();
//            return getFreeSpace(path.getPath());
//        } else {
//            return -1;
//        }
//    }
//
//
//    /**
//     * @return in bytes
//     */
//    public static long getFreeInternalSpace() {
//        File path = Environment.getDataDirectory();
//        return getFreeSpace(path.getPath());
//    }
//
//    public static boolean isWifiAvailable(Context mb) {
//        WifiManager mWifiManager = (WifiManager) mb.getSystemService(Context.WIFI_SERVICE);
//        if (mWifiManager != null) {
//            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
//            if (wifiInfo != null) {
//                int ipAddress = wifiInfo.getIpAddress();
//                if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
//                    isWiFi = true;
//                    return true;
//                }
//            }
//        }
//        isWiFi = false;
//        return false;
//    }
//
//
//    /**
//     * @return MAC or empty
//     */
//    public static String getMacAddress(Context con) {
//        try {
//            WifiManager mWifiManager = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
//            if (mWifiManager != null) {
//                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
//                if (wifiInfo != null) {
//                    return wifiInfo.getMacAddress();
//                }
//            }
//        } catch (Exception e) {
//        }
//        return "";
//    }
//
//    private void getCurNetworkType(Context context) {
//        try {
//            if (isWifiAvailable(context)) {
//                netTryNum = 1;
//                netTypeArray[0] = kNetType;
//                return;
//            }
//
//            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//            if (networkInfo != null) {
//                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                    String proxy = android.net.Proxy.getDefaultHost();
//                    if (proxy == null) {
//                        netTryNum = 1;
//                        netTypeArray[0] = kNetType;
//                        return;
//                    } else {
//                        if (proxy.equals("010.000.000.172"))
//                            proxy = "10.0.0.172";
//                        if (proxy.equals("010.000.000.200"))
//                            proxy = "10.0.0.200";
//
//                        if (proxy.equals("10.0.0.172")) {
//                            netTryNum = 2;
//                            netTypeArray[0] = kNetType;
//                            netTypeArray[1] = kWapType_172;
//                            return;
//                        } else if (proxy.equals("10.0.0.200")) {
//                            netTryNum = 2;
//                            netTypeArray[0] = kNetType;
//                            netTypeArray[1] = kWapType_200;
//                            return;
//                        }
//                    }
//                } else
//                    return;
//            } else
//                return;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static boolean checkUdidZero() {
//        try {
//            int val = Integer.parseInt(udid);
//            if (val == 0)
//                return true;
//            else
//                return false;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private static boolean checkUdidValid() {
//        if (udid != null && !"".equals(udid) && !"null".equals(udid)
//                && !"NULL".equals(udid) && !checkUdidZero()
//                && !"9774d56d682e549c".equals(udid)) // SDK version 2.2, some devices have the same id
//        {
//            int len = 10 - udid.length();
//            for (int i = 0; i < len; i++) {
//                udid = "0" + udid;
//            }
//            return true;
//        } else
//            return false;
//    }
//
//    public static String getUdid(Context context) {
//        if (checkUdidValid() == false) {
//            SharedPreferences sharepre = PreferenceManager.getDefaultSharedPreferences(context);
//            udid = sharepre.getString(Constant.KeyDeviceID, "");
//            if (checkUdidValid() == false) {
//                try {
//                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//                    udid = "" + tm.getDeviceId();
//                } catch (Exception e) {
//                    udid = "";
//                }
//
//                if (checkUdidValid() == false) {
//                    if (ValidatorUtil.isEffective(mac))
//                        udid = "MAC" + mac.replace(':', '0').replace('.', '0');
//                    else
//                        udid = "";
//                    if (checkUdidValid() == false) {
//                        try {
//                            udid = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//                        } catch (Exception e) {
//                            udid = "";
//                        }
//                        if (checkUdidValid() == false) {
//                            getRandomUdidFromFile(context);
//                        }
//                    }
//                }
//
//                if (checkUdidValid() == true) {
//                    Editor editor = sharepre.edit();
//                    editor.putString(Constant.KeyDeviceID, udid);
//                    editor.commit();
//                }
//            }
//        }
//
//        return udid.toUpperCase();
//    }
//
//    public static void getDeviceId(Context con, HashMap<String, String> map) {
//        if (map == null) return;
//
//        getUdid(con);
//
//        map.put("udid", udid);
//        if (isWiFi)
//            map.put("conn", "wifi");
//        else
//            map.put("conn", "mobile");
//        if (ValidatorUtil.isEffective(mac))
//            map.put("mac", DataConverter.urlEncode(mac));
//    }
//
//    public static String getLanguage() {
//        String language = Locale.getDefault().getLanguage();
//        if (!ValidatorUtil.isEffective(language)) {
//            String country = Locale.getDefault().getCountry();
//            if (ValidatorUtil.isEffective(country)) {
//                language = country;
//            }
//        }
//        return language;
//    }
//
//    /**
//     * @param con
//     * @return udid + connectnet + mac
//     */
//    public static String getDeviceId(Context con) {
//        getUdid(con);
//
//        String ret;
//        if (isWiFi)
//            ret = udid + "&connectnet=" + "wifi";
//        else
//            ret = udid + "&connectnet=" + "mobile";
//        if (ValidatorUtil.isEffective(mac))
//            return ret + "&mac=" + DataConverter.urlEncode(mac);
//        else
//            return ret;
//    }
//
//    private synchronized static void getRandomUdidFromFile(Context context) {
//        File installation = new File(context.getFilesDir(), "INSTALLATION");
//        try {
//            if (!installation.exists())
//                writeInstallationFile(installation);
//            udid = readInstallationFile(installation);
//        } catch (Exception e) {
//            udid = "";
//        }
//    }
//
//
//    private static String readInstallationFile(File installation)
//            throws IOException {
//        RandomAccessFile f = new RandomAccessFile(installation, "r");
//        byte[] bytes = new byte[(int) f.length()];
//        f.readFully(bytes);
//        f.close();
//        return new String(bytes);
//    }
//
//    private static void writeInstallationFile(File installation)
//            throws IOException {
//        FileOutputStream out = new FileOutputStream(installation);
//        String id = UUID.randomUUID().toString();
//        out.write(id.getBytes());
//        out.close();
//    }
//
//    public static String getPhoneImsi(Context context) {
//        try {
//            TelephonyManager tm = (TelephonyManager) context
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//            return tm.getSubscriberId();
//        } catch (Exception e) {
//            return "";
//        }
//    }
//
//    /**
//     * get DeviceInfo info responded<br>
//     * organized as &dos=....., should be attached suffix
//     */
//    public String getDeviceInfo(Context con) {
//        getDeviceId(con);
//
//
//        LogUtil2.e(TAG, " device info: BOARD=" + Build.BOARD + ", BRAND=" + Build.BRAND + ", DEVICE=" + Build.DEVICE
//                + ", DISPLAY=" + Build.DISPLAY
//                // +", HARDWARE="+Build.HARDWARE
//                + ", ID=" + Build.ID + ", MANUFACTURER=" + Build.MANUFACTURER + ", MODEL=" + Build.MODEL
//                + ", PRODUCT=" + Build.PRODUCT + ", TYPE=" + Build.TYPE
//                + ", VERSION=" + Build.VERSION.SDK);
//
//        String DeviceInfo = "&dos=Android&dosv="
//                + DataConverter.urlEncode(Build.VERSION.SDK) + "&model="
//                + DataConverter.urlEncode(Build.MODEL) + "&hres=" + screenWidth
//                + "&vres=" + screenHeight;
//        return DeviceInfo;
//    }
//
//    public static String getLocalIpAddress() {
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface
//                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf
//                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()) {
//                        return inetAddress.getHostAddress().toString();
//                    }
//                }
//            }
//        } catch (SocketException ex) {
//        }
//        return null;
//    }
//
//    public static String getCPUSerial() {
//        String cpuAddress = "0000000000000000";
//        try {
//            // ��ȡCPU��Ϣ
//            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
//            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
//            LineNumberReader input = new LineNumberReader(ir);
//            // ����CPU���к�
//            for (int i = 1; i < 100; i++) {
//                String str = input.readLine();
//                if (str != null) {
//                    // ���ҵ����к�������
//                    if (str.indexOf("Serial") > -1) {
//                        // ��ȡ���к�
//                        String strCPU = str.substring(str.indexOf(":") + 1,
//                                str.length());
//                        // ȥ�ո�
//                        cpuAddress = strCPU.trim();
//                        break;
//                    }
//                } else {
//                    // �ļ���β
//                    break;
//                }
//            }
//        } catch (IOException ex) {
//            // ����Ĭ��ֵ
//            ex.printStackTrace();
//        }
//        return cpuAddress;
//    }
//
//    public static boolean isBackground(Context context) {
//        try {
//            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
//            for (RunningAppProcessInfo appProcess : appProcesses) {
//                if (appProcess.processName.equals(context.getPackageName())) {
//                    if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND
//                            && appProcess.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
//        return false;
//    }
//
//    public static boolean isForeground(Context context) {
//        try {
//            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
//            for (RunningAppProcessInfo appProcess : appProcesses) {
//                if (appProcess.processName.equals(context.getPackageName())) {
//                    if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
//                            || appProcess.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            if (BuildConfig.DEBUG)
//                e.printStackTrace();
//        }
//        return false;
//    }
//
//
//    private void disableConnectionReuseIfNecessary() {
//        // HTTP connection reuse which was buggy pre-froyo(8)
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
//            System.setProperty("http.keepAlive", "false");
//        }
//    }
//
//    public static void setKeepAlive(HttpURLConnection con) {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
//            con.setRequestProperty("Connection", "Keep-Alive");
//        }
//    }
//
//    public static HttpURLConnection getNetworkConnection(String urlString, boolean reqSecurity, boolean resSecurity) {
//        HttpURLConnection uc = getHttpConnection(urlString, reqSecurity, resSecurity);
//        return uc;
//    }
//
//    public static int getNetType() {
//        final int netType = netTypeArray[netTryIndex];
//        if (netType == kWapType_172 || netType == kWapType_200)
//            return kWapType;
//        else
//            return kNetType;
//    }
//
//    public static int getRealNetType() {
//        return netTypeArray[netTryIndex];
//    }
//
//    public static void changeNetType() {
//        netTryIndex++;
//        netTryIndex = netTryIndex % netTryNum;
//    }
//
//    private static void trustAllHosts() {
//        // Create a trust manager that does not validate certificate chains
//        // Android ����X509��֤����Ϣ����
//        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                return new java.security.cert.X509Certificate[]{};
//            }
//
//            @Override
//            public void checkClientTrusted(
//                    java.security.cert.X509Certificate[] chain, String authType)
//                    throws java.security.cert.CertificateException {
//            }
//
//            @Override
//            public void checkServerTrusted(
//                    java.security.cert.X509Certificate[] chain, String authType)
//                    throws java.security.cert.CertificateException {
//            }
//        }};
//
//        // Install the all-trusting trust manager
//        try {
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection
//                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
//        public boolean verify(String hostname, SSLSession session) {
//            return true;
//        }
//    };
//
//    private static HttpURLConnection makeConnection(URL url, Proxy p) {
//        HttpURLConnection http = null;
//        try {
//            if ("https".equals(url.getProtocol().toLowerCase(Locale.ENGLISH))) {
//                trustAllHosts();
//                HttpsURLConnection https;
//                if (p != null)
//                    https = (HttpsURLConnection) url.openConnection(p);
//                else
//                    https = (HttpsURLConnection) url.openConnection();
//                https.setHostnameVerifier(DO_NOT_VERIFY);
//                http = https;
//            } else {
//                if (p != null)
//                    http = (HttpURLConnection) url.openConnection(p);
//                else
//                    http = (HttpURLConnection) url.openConnection();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return http;
//    }
//
//    private static HttpURLConnection getHttpConnection(String urlString, boolean reqSecurity, boolean resSecurity) {
//        if (urlString == null)
//            return null;
//
//        String newUrl = null;
//        URL url = null;
//        String newLinkPata = null;
//
//        try {
//            int skipNum = 7;
//            int end = urlString.indexOf("://");
//            if (end != -1) {
//                skipNum = end + 3;
//            }
//
//            String protocol = urlString.substring(0, skipNum);
//            if (protocol.startsWith("file://")) // not a http protocol
//                return null;
//            String realLink = urlString.substring(skipNum);
//            String realHost = realLink.substring(0, realLink.indexOf("/"));
//            String linkPara = realLink.substring(realLink.indexOf("/") + 1);
//            if (reqSecurity) {
//                String tmp;
//                if (resSecurity)
//                    tmp = linkPara + "&secure_return=1";
//                else
//                    tmp = linkPara;
////				byte[] input = tmp.getBytes();
////				tmp = new String(WccBarcode.enReq(input, input.length));
//                newLinkPata = "zz" + tmp;
//            } else {
//                if (resSecurity)
//                    newLinkPata = linkPara + "&secure_return=1";
//                else
//                    newLinkPata = linkPara;
//            }
//
//            newUrl = protocol + realHost + "/" + newLinkPata;
//            url = new URL(newUrl);
////			if (AppConfig.DEBUG)
////				Log.e(TAG, "getHttpConnection, url----:" + url);
//
//            if (getNetType() == kWapType) {
//                String proxy;
//                final int netType = getRealNetType();
//                if (netType == kWapType_172) proxy = "10.0.0.172";
//                else proxy = "10.0.0.200";
//
//                LogUtil2.e(TAG, "use proxy----:" + proxy);
//                Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, 80));
//                return makeConnection(url, p);
//            }
//            return makeConnection(url, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    /**
//     * if you want to use this function, you have to register handlers at first
//     *
//     * @param what
//     */
//    public void sendMessage(int what) {
//        if (handlers != null) {
//            Message message;
//            int size = handlers.size();
//
//            for (int i = 0; i < size; i++) {
//                Handler handler = handlers.valueAt(i);
//                if (handler != null) {
//                    message = Message.obtain(handler, what);
//                    message.sendToTarget();
//                }
//            }
//        }
//    }
//
//    /**
//     * if you want to use this function, you have to register handlers at first
//     *
//     * @param what
//     * @param obj
//     */
//    public void sendMessage(int what, Object obj) {
//        if (handlers != null) {
//            Message message;
//            int size = handlers.size();
//
//            for (int i = 0; i < size; i++) {
//                Handler handler = handlers.valueAt(i);
//                if (handler != null) {
//                    message = Message.obtain(handler, what, obj);
//                    message.sendToTarget();
//                }
//            }
//        }
//    }
//
//
//    /**
//     * if you want to use this function, you have to register handlers at first
//     *
//     * @param what
//     */
//    public void sendMessage(int what, int arg1, int arg2) {
//        if (handlers != null) {
//            Message message;
//            int size = handlers.size();
//
//            for (int i = 0; i < size; i++) {
//                Handler handler = handlers.valueAt(i);
//                if (handler != null) {
//                    message = Message.obtain(handler, what, arg1, arg2);
//                    message.sendToTarget();
//                }
//            }
//        }
//    }
//
//    /**
//     * if you want to use this function, you have to register handlers at first
//     *
//     * @param what
//     */
//    public void sendMessage(int what, int arg1, int arg2, Object obj) {
//        if (handlers != null) {
//            Message message;
//            int size = handlers.size();
//
//            for (int i = 0; i < size; i++) {
//                Handler handler = handlers.valueAt(i);
//                if (handler != null) {
//                    message = Message.obtain(handler, what, arg1, arg2, obj);
//                    message.sendToTarget();
//                }
//            }
//        }
//    }
//
//    public static void sendMessage(Handler targetHandler, int what, int arg1, int arg2, Object obj) {
//        try {
//            if (targetHandler != null) {
//                Message message = Message.obtain(targetHandler, what, arg1, arg2, obj);
//                message.sendToTarget();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void sendMessage(Handler targetHandler, int what, int arg1, int arg2) {
//        try {
//            if (targetHandler != null) {
//                Message message = Message.obtain(targetHandler, what, arg1, arg2);
//                message.sendToTarget();
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    public static void sendMessage(Handler targetHandler, int what, Object obj) {
//        LogUtil2.d("Eagle", "hardware sendMessage");
//        try {
//            if (targetHandler != null) {
//                LogUtil2.d("Eagle", "targetHandler!=null ");
//                Message message = Message.obtain(targetHandler, what, obj);
//                message.sendToTarget();
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    public static void sendMessage(Handler targetHandler, int what) {
//        try {
//            if (targetHandler != null) {
//                Message message = Message.obtain(targetHandler, what);
//                message.sendToTarget();
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    public static void sendMessageDelayed(Handler targetHandler, int what, int delay) {
//        try {
//            if (targetHandler != null) {
//                Message message = Message.obtain(targetHandler, what);
//                targetHandler.sendMessageDelayed(message, delay);
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    public static void sendMessageDelayed(Handler targetHandler, int what, Object obj, long delay) {
//        try {
//            if (targetHandler != null) {
//                Message message = Message.obtain(targetHandler, what, obj);
//                targetHandler.sendMessageDelayed(message, delay);
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    public static final int ACTION_DIAL = 0;
//    public static final int ACTION_SMS = 1;
//
//    public boolean checkSIM(Context context, int type) {
//        if (isSIMCardAvailable(context) == false) {
//            AlertDialog.Builder b = new AlertDialog.Builder(context);
//            b.setTitle("��ʾ");
//            if (type == ACTION_DIAL)
//                b.setMessage("δ��⵽SIM�����޷�����绰!");
//            else
//                b.setMessage("δ��⵽SIM�����޷����Ͷ���!");
//            b.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            b.show();
//            return false;
//        }
//        return true;
//    }
//
//    public static boolean needRotateActivity() {
//
//        LogUtil2.d(TAG, "Build.VERSION.SDK_INT =" + Build.VERSION.SDK_INT);
//        if (needRotate180Layout() || Build.VERSION.SDK_INT < 11) /*��������ĳ���ֻ�*/
//            return true;
//        else
//            return false;
//    }
//
//    public static boolean needRotate180Layout() {
//        if (Build.MANUFACTURER.equals("HTC") && Build.MODEL.equals("HTC A810e"))
//            return true;
//        else
//            return false;
//    }
//
//    public static void unbindDrawables(View view) {
//        if (view == null) return;
//        if (view.getBackground() != null) {
//            view.getBackground().setCallback(null);
//        }
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                unbindDrawables(((ViewGroup) view).getChildAt(i));
//            }
//            try {
//                ((ViewGroup) view).removeAllViews();
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//        }
//    }
//
//    public static void ToastShort(Context context, int strResId) {
//        if (strResId > 0) {
//            try {
//                Toast.makeText(context, HardWareUtil.getString(context, strResId), Toast.LENGTH_SHORT).show();
//            } catch (Exception ex) {
//            }
//        }
//    }
//
//    public static void ToastLong(Context context, int strResId) {
//        if (strResId > 0) {
//            try {
//                Toast.makeText(context, HardWareUtil.getString(context, strResId), Toast.LENGTH_LONG).show();
//            } catch (Exception ex) {
//            }
//        }
//    }
//
//    public static void ToastShort(Context context, String msg) {
//        if (ValidatorUtil.isEffective(msg)) {
//            try {
//                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//            } catch (Exception ex) {
//            }
//        }
//    }
//
//
//    public static void ToastLong(Context context, String msg) {
//        if (ValidatorUtil.isEffective(msg)) {
//            try {
//                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//            } catch (Exception ex) {
//            }
//        }
//    }
//
//
//    /**
//     * will be reset after calling
//     *
//     * @return last elaspse time, milliseconds
//     */
//    public static long resetScreenOffElapseTime() {
//        LogUtil2.e(TAG, "resetScreenOffElapseTime, ScreenOffTime=" + ScreenOffTime);
//        if (ScreenOffTime != 0) {
//            long elapseTime = System.currentTimeMillis() - ScreenOffTime;
//            ScreenOffTime = 0;
//            return elapseTime;
//        }
//        return 0;
//
//    }
//
//    /**
//     * @param context
//     * @return the uid or -1;
//     */
//    public static int getUidByName(Context context, String processName) {
//        if (processName == null)
//            return -1;
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<RunningAppProcessInfo> runnings = am.getRunningAppProcesses();
//        for (RunningAppProcessInfo runningAppProcessInfo : runnings) {
//            if (processName.equals(runningAppProcessInfo.processName))
//                return runningAppProcessInfo.uid;
//        }
//        return -1;
//    }
//
//    /**
//     * ����ֻ�ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
//     */
//    public static int dip2px(Context context, float dpValue) {
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        // update HardWareUtil.densityDpi
//        densityDpi = dm.densityDpi;
//        final float scale = dm.density;
//        return (int) (dpValue * scale + 0.5f);
//    }
//
//    /**
//     * ����ֻ�ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
//     */
//    public static int px2dip(Context context, float pxValue) {
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        // update HardWareUtil.densityDpi
//        densityDpi = dm.densityDpi;
//        final float scale = dm.density;
//        return (int) (pxValue / scale + 0.5f);
//    }
//
//    private static boolean invokeIsDocumentUri(Context context, Uri uri) {
//        try {
//            Class<?> ownerClass = Class.forName("android.provider.DocumentsContract");
//            Method method = ownerClass.getMethod("isDocumentUri", Context.class, Uri.class);
//            return (Boolean) (method.invoke(null, context, uri));
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private static String invokeGetDocumentId(Uri uri) {
//        try {
//            Class<?> ownerClass = Class.forName("android.provider.DocumentsContract");
//            Method method = ownerClass.getMethod("getDocumentId", Uri.class);
//            return (String) (method.invoke(null, uri));
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     */
//    public static boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     */
//    public static boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }
//
//
//    @NonNull
//    public static String getString(Context mContext, int resId) {
//        return mContext.getResources().getString(resId);
//    }


}
