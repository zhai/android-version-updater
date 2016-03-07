package com.zhaisoft.lib.updater;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaisoft.lib.utils.LogUtil;
import com.zhaisoft.lib.utils.SystemUtil;

import java.io.File;

public class Activity_Verison_Update extends BaseCompatActivity {

    final int DIALOG_EXIT = 1000;
    // 以下部分是添加的自动更新
    private ProgressBar pb;
    private TextView downloading_kb;
    private TextView downloading_percent;

    Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case UpdateConfig.DIALOG_UPDATE:

                        if (UpdateConfig.force_update)
                            showDialog(UpdateConfig.DIALOG_UPDATE);
                        break;
                    case 1:
                        pb.setProgress(msg.arg1);
                        UpdateConfig.loading_process = msg.arg1;
                        if (UpdateConfig.MB < 1024) {
                            UpdateConfig.total = String.format("%.2f",
                                    UpdateConfig.MB) + "B";
                        } else if (UpdateConfig.MB < 1024 * 1024
                                && UpdateConfig.MB > 1024) {
                            UpdateConfig.total = String.format("%.2f",
                                    UpdateConfig.MB / 1024) + "K";
                        } else {
                            UpdateConfig.total = String.format("%.2f",
                                    UpdateConfig.MB / 1024 / 1024) + "M";
                        }

                        if (UpdateConfig.KB < 1024) {
                            UpdateConfig.now = String.format("%.2f",
                                    UpdateConfig.KB) + "B";
                        } else if (UpdateConfig.KB < 1024 * 1024
                                && UpdateConfig.KB > 1024) {
                            UpdateConfig.now = String.format("%.2f",
                                    UpdateConfig.KB / 1024) + "K";
                        } else {
                            UpdateConfig.now = String.format("%.2f",
                                    UpdateConfig.KB / 1024 / 1024) + "M";
                        }

                        String text = UpdateConfig.now + "/" + UpdateConfig.total;

                        downloading_kb.setText(text);
                        downloading_percent.setText(UpdateConfig.loading_process
                                + "%");
                        break;
                    case 2:
                        finish();

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(Environment
                                        .getExternalStorageDirectory(), getResources()
                                        .getString(R.string.app_name) + ".apk")),
                                "application/vnd.android.package-archive");
                        startActivity(intent);

                        if (true)
                            return;

                        String apkFilePath = Environment
                                .getExternalStorageDirectory()
                                + getResources().getString(R.string.app_name)
                                + ".apk";
                        String cmd = "adb install -r " + apkFilePath;

                        LogUtil.e("update", "cmd=" + cmd);

                        if (SystemUtil.runRootCmd(cmd)) {
                            // 安装成功
                            ComponentName componetName = new ComponentName(
                                    "com.zhai.ads", "com.zhai.touchhome.Loading"); // AMIsh相册

                            Intent intent2 = new Intent();
                            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent2.setComponent(componetName);
                            try {
                                startActivity(intent2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            // 安装失败
                            Toast.makeText(Activity_Verison_Update.this, "安装失败",
                                    Toast.LENGTH_LONG).show();
                        }

                        //
                        // String apkFilePath = Environment
                        // .getExternalStorageDirectory()
                        // + getResources().getString(R.string.app_name)
                        // + ".apk";
                        // String cmd = "adb install -r " + apkFilePath;
                        //
                        // if (SystemUtil.runRootCmd(cmd)) {
                        // ComponentName componetName = new ComponentName(
                        // "com.zhai.ads", "com.zhai.touchhome.Loading"); // AMIsh相册
                        //
                        // Intent intent2 = new Intent();
                        // intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // intent2.setComponent(componetName);
                        // try {
                        // startActivity(intent2);
                        // } catch (Exception e) {
                        // e.printStackTrace();
                        // }
                        // } else {
                        // Log.e("install", "安装失败");
                        // }

                        break;
                    case -1:
                        String error = msg.getData().getString("error");
                        Toast.makeText(Activity_Verison_Update.this, error,
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.popup);
    }

    final int DIALOG_NEED_UPDATE = 1;
    final int DIALOG_UPDATING = 2;

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        Bundle b = getIntent().getExtras();

        if (b == null)
            return;
        int command = b.getInt("command");
        switch (command) {
            case UpdateConfig.DIALOG_UPDATE:
                showDialog(UpdateConfig.DIALOG_UPDATE);
                break;
            case UpdateConfig.DIALOG_DOWNLOADING:
                showDialog(UpdateConfig.DIALOG_DOWNLOADING);
                break;
            default:
                break;
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	/*
     * public boolean onKeyDown(int keyCode, KeyEvent event) { switch (keyCode)
	 * { case KeyEvent.KEYCODE_BACK: return true; } return
	 * super.onKeyDown(keyCode, event); }
	 */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_EXIT:
                Builder builder_exit = new Builder(
                        Activity_Verison_Update.this);
                builder_exit.setMessage("感谢对本程序的支持,如果程序好用，请给我们好评");
                builder_exit.setTitle("提示");
                builder_exit.setPositiveButton("退出",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Activity_Verison_Update.this.finish();
                            }
                        });

                builder_exit.setNegativeButton("评价",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // dialog.dismiss();
                                Uri uri = Uri
                                        .parse("market://details?id=com.zhai.network");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                            }
                        });
                return builder_exit.create();

            case UpdateConfig.DIALOG_DOWNLOADING:
                LayoutInflater inflater2 = LayoutInflater
                        .from(getApplicationContext());
                View view2 = inflater2.inflate(R.layout.update_host_dialog, null);
                pb = (ProgressBar) view2.findViewById(R.id.down_pb);
                downloading_kb = (TextView) view2.findViewById(R.id.downloading_kb);
                downloading_percent = (TextView) view2
                        .findViewById(R.id.downloading_percent);
                Builder builder = new Builder(Activity_Verison_Update.this);
                builder.setView(view2);
                builder.setTitle("版本更新进度提示");

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface arg0) {
                        Intent intent = new Intent(Activity_Verison_Update.this,
                                UpdateService.class);
                        startService(intent);

                        finish();
                    }
                });
                builder.setNegativeButton("后台下载",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(
                                        Activity_Verison_Update.this,
                                        UpdateService.class);
                                startService(intent);
                                dialog.dismiss();
                                finish();
                            }
                        });
                return builder.create();
            case UpdateConfig.DIALOG_UPDATE:
                LayoutInflater inflater = LayoutInflater
                        .from(Activity_Verison_Update.this);
                View view = inflater.inflate(R.layout.update_dialog, null);
                WebView webview = (WebView) view.findViewById(R.id.webView1);
                webview.loadUrl(UpdateConfig.info);

                Builder alert = new Builder(
                        Activity_Verison_Update.this);
                alert.setCancelable(false);
                alert.setView(view);
                alert.setTitle("发现新版本:" + UpdateConfig.version_name);
                alert.setPositiveButton("立即升级",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                showDialog(UpdateConfig.DIALOG_DOWNLOADING);
                                new Thread() {
                                    public void run() {
                                        UpdateUtil.loadFile(
                                                Activity_Verison_Update.this,
                                                UpdateConfig.apk, updateHandler);
                                    }
                                }.start();
                            }
                        });

                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface arg0) {

                    }
                });
                if (UpdateConfig.force_update) {
                    alert.setNegativeButton("退出",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    finish();

//                                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                                    intent.addCategory(Intent.CATEGORY_HOME);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        finishAffinity();
                                    } else {
                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
//                                    int pid = android.os.Process.myPid();
//                                    android.os.Process.killProcess(pid);
//                                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                                    intent.addCategory(Intent.CATEGORY_HOME);
//                                    startActivity(intent);
                                }
                            });
                } else {
                    alert.setNegativeButton("先不升级",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                }
                return alert.create();

            case UpdateConfig.DIALOG_RUNNING:
                Builder alert2 = new Builder(
                        Activity_Verison_Update.this);
                alert2.setMessage("正在更新");
                alert2.setTitle("提示");
                alert2.setPositiveButton("停止",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(),
                                        UpdateService.class);
                                // stopService(intent);
                                dialog.dismiss();
                            }
                        });
                return alert2.create();
        }
        return super.onCreateDialog(id);
    }

}
