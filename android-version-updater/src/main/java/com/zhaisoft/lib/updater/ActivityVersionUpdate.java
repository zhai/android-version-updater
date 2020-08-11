package com.zhaisoft.lib.updater;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhaisoft.lib.mvp.RuntimeRationale;
import com.zhaisoft.lib.mvp.base.BaseMultiDexApplication;
import com.zhaisoft.lib.updater.model.VOApk;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * @author zhai
 */
public class ActivityVersionUpdate extends AppCompatActivity {

    final int DIALOG_EXIT = 1000;
    // 以下部分是添加的自动更新
    private NumberProgressBar pb;
    private TextView downloading_kb;
    private TextSwitcher download_speed;

    VOApk mVOApk = new VOApk();

    public static boolean installAPK(Context context, File apkFile) {
        if (apkFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, context.getApplicationInfo().processName + ".install.fileProvider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                context.startActivity(intent);
            }
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().
                addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //setContentView(R.layout.popup);
        if (getIntent().getExtras() != null) {
            mVOApk = (VOApk) getIntent().getExtras().getSerializable(VOApk.class.getName());
        }
        //enableStrictMode();
        showUpdateDialog();
        //OkDownload.setSingletonInstance(new OkDownload.Builder(ActivityVersionUpdate.this.getApplicationContext()).build());

    }

    final int DIALOG_NEED_UPDATE = 1;
    final int DIALOG_UPDATING = 2;

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
//        Bundle b = getIntent().getExtras();
//
//        if (b == null)
//            return;
//        int command = b.getInt("command");
//        switch (command) {
//            case UpdateConfig.DIALOG_UPDATE:
//                showDialog(UpdateConfig.DIALOG_UPDATE);
//                break;
//            case UpdateConfig.DIALOG_DOWNLOADING:
//                showDialog(UpdateConfig.DIALOG_DOWNLOADING);
//                break;
//            default:
//                break;
//        }
        super.onStart();
    }

    AlertDialog alertDialog1;
    AlertDialog alertDialog2;
    AlertDialog alertDialog3;

    void showUpdateDialog() {

        if (alertDialog1 != null)
            return;

        LayoutInflater inflater = LayoutInflater
                .from(ActivityVersionUpdate.this);
        View view = inflater.inflate(R.layout.update_dialog, null);
        WebView webview = (WebView) view.findViewById(R.id.webView1);
        webview.loadUrl(mVOApk.info);

        alertDialog1 = new AlertDialog.Builder(this)
                .setTitle("发现新版本:" + mVOApk.version_name)
                //.setMessage("经度：" + lng + "纬度：" + lat)
                //添加输入框
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("立即升级", null)
                .setNegativeButton("退出", null)
                .setNeutralButton("取消", null)
                .show();

        //mVOApk.force_update = false;
        if (mVOApk.force_update) {
            alertDialog1.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
            alertDialog1.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
        } else {
            alertDialog1.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
            alertDialog1.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);

        }

        alertDialog1.setView(view);
        alertDialog1.setTitle("发现新版本:" + mVOApk.version_name);
        alertDialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRightAndStartDownload(alertDialog1);
            }
        });
        alertDialog1.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //强制退出应用
        alertDialog1.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestRightAndStartDownload(dialog);

                alertDialog1.dismiss();
                finish();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                } else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });


    }

    ;

    void showDownloadErrorDialog() {

        if (alertDialog3 != null)
            return;

//        LayoutInflater inflater = LayoutInflater
//                .from(ActivityVersionUpdate.this);
////        View view = inflater.inflate(R.layout.update_dialog, null);
////        WebView webview = (WebView) view.findViewById(R.id.webView1);
////        webview.loadUrl(mVOApk.info);

        alertDialog3 = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("网络不稳定，更新失败，请在网络正常的时候再尝试！")
                //添加输入框
                //.setView(view)
                .setCancelable(false)
                .setPositiveButton("确定", null)
                //.setNegativeButton("退出", null)
                .show();


        alertDialog3.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog3.dismiss();
                finish();
            }
        });
        alertDialog3.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog3.dismiss();
                finish();
            }
        });

    }


    void showDownloadDialog() {

        if (alertDialog2 != null)
            return;

        //Util.enableConsoleLog();

        LayoutInflater inflater2 = LayoutInflater
                .from(getApplicationContext());
        View view = inflater2.inflate(R.layout.update_host_dialog, null);
        pb = (NumberProgressBar) view.findViewById(R.id.down_pb);
        downloading_kb = (TextView) view.findViewById(R.id.downloading_kb);
        download_speed = (TextSwitcher) view
                .findViewById(R.id.download_speed);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        download_speed.setInAnimation(in);
        download_speed.setOutAnimation(out);

        download_speed.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(ActivityVersionUpdate.this);
                textView.setTextSize(10);
                return textView;
            }
        });


        alertDialog2 = new AlertDialog.Builder(this)
                //.setTitle("下载中")
                //.setMessage("经度：" + lng + "纬度：" + lat)
                //添加输入框
                .setView(view)
                .setCancelable(false)
//                .setPositiveButton("立即升级", null)
//                .setNegativeButton("退出", null)
//                .setNeutralButton("取消", null)
                .show();

        //OKhttp建立一个单例


        final DownloadListener4WithSpeed mDownloadListener = new DownloadListener4WithSpeed() {

            private long totalLength;

            @Override
            public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info, boolean fromBreakpoint, @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {
                totalLength = info.getTotalLength();
                pb.setMax((int) totalLength);

            }

            @Override
            public void progressBlock(@NonNull DownloadTask task, int blockIndex, long currentBlockOffset, @NonNull SpeedCalculator blockSpeed) {

            }

            @Override
            public void progress(@NonNull DownloadTask task, long currentOffset, @NonNull SpeedCalculator taskSpeed) {
                download_speed.setText(taskSpeed.speed());
                float percent = (float) currentOffset / totalLength * 100;
                //pb.setProgress((int) percent);
                pb.setProgress((int) currentOffset);
                downloading_kb.setText(getNetFileSizeDescription(currentOffset) + "|" + getNetFileSizeDescription(totalLength));
                //System.out.println("taskSpeed=" + taskSpeed.speed());


                if (percent >= 100) {
//                    alertDialog.dismiss();
//                    installApk(url);
                }

                //System.out.println("taskSpeed=" + taskSpeed.speed() + ",progress=" + percent + "," + currentOffset + "|" + totalLength);
            }

            @Override
            public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info, @NonNull SpeedCalculator blockSpeed) {
//                System.out.println("7、blockEnd】" + blockIndex);

            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {

                System.out.println("taskEnd=");

                boolean isCompleted = StatusUtil.isCompleted(task);
                System.out.println("isCompleted=" + isCompleted);

                if (isCompleted) {
                    alertDialog2.dismiss();
                    installApk(mVOApk.apk);
                } else {
                    //task.enqueue(this);
                    alertDialog2.dismiss();
                    showDownloadErrorDialog();
                }

            }

            @Override
            public void taskStart(@NonNull DownloadTask task) {

            }

            @Override
            public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {

            }

            @Override
            public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {


            }
        };


        String fileName = FilenameUtils.getName(mVOApk.apk);

        String targetApkPath = "/sdcard/" + getCurrentPacakgeName(ActivityVersionUpdate.this.getApplicationContext()) + "/";
        DownloadTask task = new DownloadTask.Builder(mVOApk.apk, targetApkPath, fileName)
                .setFilename(fileName)
                //.setPassIfAlreadyCompleted(false)
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(30)
                //.setPriority(10)
                //.setReadBufferSize(8192)
                .setConnectionCount(1)

// set the flush buffer to 32768 bytes for the buffered output-stream.(default is 16384)
                //.setFlushBufferSize(32768)

                //.setPreAllocateLength(false)
                // do re-download even if the task has already been completed in the past.
                .setPassIfAlreadyCompleted(false)
                .build();

        task.enqueue(mDownloadListener);

    }


    ;

    public static String getCurrentPacakgeName(Context mContext) {

        try {
            ActivityManager am = (ActivityManager) mContext
                    .getSystemService(Activity.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            return cn.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    private void installApk(String url) {
        //String fileName = FilenameUtils.getName(url);


        String fileName = FilenameUtils.getName(mVOApk.apk);

        String targetApkPath = "/sdcard/" + getCurrentPacakgeName(ActivityVersionUpdate.this.getApplicationContext()) + "/";

        File apkFile = new File(targetApkPath + fileName);

        if (apkFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = AndPermission.getFileUri(ActivityVersionUpdate.this.getApplicationContext(), apkFile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            startActivity(intent);
            finish();
        } else {
            finish();
            Toast.makeText(BaseMultiDexApplication.getInstance().getApplicationContext(), targetApkPath + fileName + "文件不存在！", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    AlertDialog mDialog;

    private void requestRightAndStartDownload(final AlertDialog dialog) {

        mDialog = dialog;

        AndPermission.with(this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE)

                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        startDownload(mDialog);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {

                        if (AndPermission.hasAlwaysDeniedPermission(ActivityVersionUpdate.this, permissions)) {
                            //showSettingDialog(Activity_GoodLoading.this, permissions);
                            setPermission();
                            Toast.makeText(ActivityVersionUpdate.this, "更新功能需要申请SD卡访问权限，请手动打开应用程序权限！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivityVersionUpdate.this, "更新功能需要申请SD卡访问权限！", Toast.LENGTH_SHORT).show();

                        }

                    }
                })
                .start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog3 != null)
            alertDialog3.dismiss();
        if (alertDialog2 != null)
            alertDialog2.dismiss();
        if (alertDialog1 != null)
            alertDialog1.dismiss();

        getWindow().
                clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static final int REQUEST_CODE_SETTING = 1;

    protected void setPermission() {
        AndPermission.with(this).runtime().setting().start(REQUEST_CODE_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_SETTING)
            requestRightAndStartDownload(mDialog);
    }

    private void startDownload(AlertDialog dialog) {
        dialog.dismiss();
        //Toast.makeText(ActivityVersionUpdate.this, "开始下载！", Toast.LENGTH_SHORT).show();
        showDownloadDialog();

//        showDialog(UpdateConfig.DIALOG_DOWNLOADING);
//        new Thread() {
//            public void run() {
//                UpdateUtil.loadFile(
//                        ActivityVersionUpdate.this,
//                        UpdateConfig.apk, updateHandler);
//            }
//        }.start();
    }

    OkDownload mOkDownload;

    boolean first = true;


    private void enableStrictMode() {

        final StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyLog();
        final StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder()
                .detectAll().penaltyLog();

        threadPolicyBuilder.penaltyFlashScreen();
        StrictMode.setThreadPolicy(threadPolicyBuilder.build());
        StrictMode.setVmPolicy(vmPolicyBuilder.build());

    }


}
