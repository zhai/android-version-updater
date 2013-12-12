package com.zhai.updater;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class UpdateActivity extends Activity implements
		ViewSwitcher.ViewFactory {
	private ProgressBar pb;
	private TextSwitcher downloading_kb;
	private TextView downloading_percent;

	Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case UpdateConfig.DIALOG_UPDATE:
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
							.getExternalStorageDirectory(),
							UpdateConfig.apkName)),
							"application/vnd.android.package-archive");
					startActivity(intent);
					break;
				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(UpdateActivity.this, error, 1).show();
					break;
				default:
					break;
				}
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setTheme(R.style.Theme.Sherlock.Light.WallPaper);
		super.onCreate(savedInstanceState);
		// 检测更新

		UpdateUtil.checkUpdateThread(getApplicationContext(), updateHandler);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case UpdateConfig.DIALOG_DOWNLOADING:
			LayoutInflater inflater2 = LayoutInflater
					.from(getApplicationContext());
			View view2 = inflater2.inflate(R.layout.update_loading, null);
			pb = (ProgressBar) view2.findViewById(R.id.down_pb);
			downloading_kb = (TextSwitcher) view2
					.findViewById(R.id.downloading_kb);

			downloading_kb.setFactory(UpdateActivity.this);

			Animation in = AnimationUtils.loadAnimation(this,
					android.R.anim.fade_in);
			Animation out = AnimationUtils.loadAnimation(this,
					android.R.anim.fade_out);
			downloading_kb.setInAnimation(in);
			downloading_kb.setOutAnimation(out);

			downloading_percent = (TextView) view2
					.findViewById(R.id.downloading_percent);
			Builder builder = new Builder(UpdateActivity.this);
			builder.setView(view2);
			builder.setTitle("版本更新进度提示");
			builder.setNegativeButton("后台下载",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(getApplicationContext(),
									UpdateService.class);
							startService(intent);
							dialog.dismiss();
						}
					});
			return builder.create();

		case UpdateConfig.DIALOG_UPDATE:
			LayoutInflater inflater = LayoutInflater.from(UpdateActivity.this);
			View view = inflater.inflate(R.layout.update_dialog, null);
			WebView webview = (WebView) view.findViewById(R.id.webView1);
			webview.loadUrl(UpdateConfig.info);

			AlertDialog.Builder alert = new Builder(UpdateActivity.this);
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
									UpdateUtil.loadFile(UpdateConfig.apk,
											updateHandler);
								}
							}.start();
						}
					});

			if (UpdateConfig.force_update) {
				alert.setNegativeButton("退出",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								android.os.Process
										.killProcess(android.os.Process.myPid());
							}
						});
			} else {
				alert.setNegativeButton("先不升级",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
			}
			return alert.create();

		case UpdateConfig.DIALOG_RUNNING:
			AlertDialog.Builder alert2 = new Builder(UpdateActivity.this);
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

	@Override
	public View makeView() {
		TextView t = new TextView(this);
		t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		t.setTextSize(36);
		return t;
	}
}
