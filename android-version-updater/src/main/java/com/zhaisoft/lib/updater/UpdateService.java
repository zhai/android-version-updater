package com.zhaisoft.lib.updater;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;

public class UpdateService extends Service {
    String time;
    private NotificationManager notificationMrg;
    private int old_process = 0;
    private boolean isFirstStart = false;

    @SuppressLint("SimpleDateFormat")
    public void onCreate() {
        super.onCreate();
        isFirstStart = true;
        notificationMrg = (NotificationManager) this
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        System.out.println(UpdateConfig.loading_process + "==");
        mHandler.handleMessage(new Message());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm");
        time = sDateFormat.format(new java.util.Date());
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 1为出现，2为隐藏
            if (UpdateConfig.loading_process > 99) {
                notificationMrg.cancel(0);
                stopSelf();
                return;
            }
            if (UpdateConfig.loading_process > old_process) {
                displayNotificationMessage(UpdateConfig.loading_process);
            }

            new Thread() {
                @Override
                public void run() {
                    isFirstStart = false;
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }.start();
            old_process = UpdateConfig.loading_process;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void displayNotificationMessage(int count) {

        // Notification的Intent，即点击后转向的Activity
        Intent notificationIntent1 = new Intent(this, this.getClass());
        notificationIntent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent1 = PendingIntent.getActivity(this, 0,
                notificationIntent1, 0);
        // 创建Notifcation
        Notification notification = new Notification(
                R.drawable.ic_stat_download, getString(R.string.app_name)
                + "正在下载，正载完成后会自动提示安装", System.currentTimeMillis());// 设定Notification出现时的声音，一般不建议自定义
        if (isFirstStart || UpdateConfig.loading_process > 98) {
            notification.defaults |= Notification.DEFAULT_SOUND;// 设定是否振动
            // notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        // 创建RemoteViews用在Notification中
        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.update_notification);
        contentView.setTextViewText(R.id.notification_title, getResources()
                .getString(R.string.app_name));

        contentView.setTextViewText(R.id.notification_time, time);
        contentView.setProgressBar(R.id.notification_progress, 100, count,
                false);

        notification.contentView = contentView;
        notification.contentIntent = contentIntent1;

        notificationMrg.notify(0, notification);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
