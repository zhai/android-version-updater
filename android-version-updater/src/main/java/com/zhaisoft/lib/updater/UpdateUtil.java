package com.zhaisoft.lib.updater;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;


import com.zhaisoft.lib.updater.util.NetUtil;
import com.zhaisoft.lib.updater.util.PropertyUtil;
import com.zhaisoft.lib.updater.util.ThreadsManager;
import com.zhaisoft.lib.updater.util.VersionUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;


public class UpdateUtil {

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

            if (UpdateConfig.version_name.compareTo(currentVersionName) != 0) {
                // 有更新，弹出对话框
                Message msg = updateHandler.obtainMessage(
                        UpdateConfig.DIALOG_UPDATE, null);
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

    public static void loadFile(Context context, String url,
                                Handler updateHandler) {
//		HttpClient client = new DefaultHttpClient();
//		HttpGet get = new HttpGet(url);

        int responseCode;
        try {

            URL _url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) _url.openConnection(); //使用URL打开一个链接
            conn.setDoInput(true); //允许输入流，即允许下载
            //conn.setDoOutput(true); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("GET"); //使用get请求
            //response = client.execute(get);
            responseCode = conn.getResponseCode();


            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                sendMsg(UpdaterConfig.MESSAGE_DOWNLOAD_ERROR, 0, updateHandler); // 文件不存在
                return;
            }

            //HttpEntity entity = response.getEntity();
            float length = conn.getContentLength();

            UpdateConfig.MB = length;
            InputStream is = conn.getInputStream();
            FileOutputStream fileOutputStream = null;
            if (is != null) {
                File file = new File(Environment.getExternalStorageDirectory(),
                        context.getResources().getString(R.string.app_name)
                                + ".apk");
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
            sendMsg(UpdaterConfig.MESSAGE_DOWNLOAD_SUCCESS, 0, updateHandler);
            fileOutputStream.flush();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMsg(UpdaterConfig.MESSAGE_DOWNLOAD_ERROR, 0, updateHandler);
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

        ThreadsManager.post(new Runnable() {
            @Override
            public void run() {
                UpdateUtil.checkUpdate(applicationContext, updateHandler);
            }
        });

    }

}
