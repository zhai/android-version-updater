package com.zhai.exception;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.zhai.updater.R;
import com.zhai.utils.SystemUtil;
import com.zhai.utils.VersionUtil;

public class BugReportActivity extends Activity {
	static final String STACKTRACE = "BugReportActivity";

	protected static final int MENU_SEND = 5012;
	protected static final int MENU_CANCEL = 5013;

	private static final int DIALOG_NOEMAILAPP = 5014;

	String stackTrace;
	TextView reportTextView;

	String versionName;

	private String getVersionName() {
		try {
			return this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (Exception e) {
			return "";

		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.bug_report_view);

		this.getSupportActionBar().setDisplayUseLogoEnabled(false);

		this.getSupportActionBar().setTitle(this.getString(R.string.app_name) + "出现未知问题");
		this.stackTrace = this.getIntent().getStringExtra(STACKTRACE);
		this.reportTextView = (TextView) this.findViewById(R.id.report_text);

		final String versionName = this.getVersionName();
		if (this.reportTextView != null) {
			this.reportTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
			this.reportTextView.setClickable(false);
			this.reportTextView.setLongClickable(false);
			StringBuffer buf = new StringBuffer();

			buf.append("程序出现异常\n");
			buf.append("version: " + versionName + " \n");
			buf.append("详细信息 " + " \n");
			buf.append("---------------------------------------------------------- " + " \n");
			buf.append(this.stackTrace);
			this.reportTextView.setText(buf.toString());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Used to put dark icons on light action bar
		// boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;
		menu.add(0, MENU_SEND, 0, "发送报告").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, MENU_CANCEL, 0, "取消").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {

		case MENU_SEND:
			this.feedback(0);
			return true;
		case MENU_CANCEL:
			android.os.Process.killProcess(android.os.Process.myPid());
			return true;

		}

		return false;
	}

	private void feedback(final int position) {
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "masterzxg@gmail.com" });
		StringBuffer sb = new StringBuffer();

		switch (position) {
		case 0:
			// 程序出错
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.app_name) + "["
					+ VersionUtil.getAppVersionName(BugReportActivity.this) + "]" + "【错误提交】");
			break;
		case 1:
			// 程序出错
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.app_name) + "["
					+ VersionUtil.getAppVersionName(BugReportActivity.this) + "]" + "【Host提交】");
			break;
		case 2:
			// 程序出错

			sendIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.app_name) + "["
					+ VersionUtil.getAppVersionName(BugReportActivity.this) + "]" + "【功能界面建议】");
			break;

		default:
			break;
		}

		// <item>1.程序使用Bug</item>
		// <item>2.Host不完整，我有更好的Host文件</item>
		// <item>3.我对程序的界面等设计有自己的想法</item>

		// version[0] = arrayOfString[2];// KernelVersion
		// version[1] = Build.VERSION.RELEASE;// firmware version
		// version[2] = Build.MODEL;// model
		// version[3] = Build.DISPLAY;// system version

		String[] info = SystemUtil.getVersion();
		sb.append("机型:" + info[2] + "  " + info[1] + " \n");
		sb.append("温馨提示：如果有截图，作者可以更好的发现和更改程序\n");
		sb.append("\n");
		sb.append("①.\n");
		sb.append("\n");
		sb.append("②.\n");
		sb.append("\n");
		sb.append("③.\n");
		sb.append("\n");
		sb.append("④.\n");
		sb.append("\n");
		sb.append("⑤.\n");
		sb.append("\n");
		sb.append("\n");
		sb.append(this.stackTrace);
		sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());

		sendIntent.setType("message/rfc822");

		try {
			this.startActivity(sendIntent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.showDialog(DIALOG_NOEMAILAPP);
			e.printStackTrace();
		}
	}

	@Override
	protected Dialog onCreateDialog(final int id) {
		switch (id) {
		case DIALOG_NOEMAILAPP:
			AlertDialog.Builder alert_noemail = new Builder(BugReportActivity.this);
			alert_noemail.setMessage("手机上没有安装任何电子邮件客户端，建议下载使用Gmail");
			alert_noemail.setTitle("提示");
			alert_noemail.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, final int which) {
					BugReportActivity.this.dismissDialog(DIALOG_NOEMAILAPP);
				}
			});
			return alert_noemail.create();

		}
		return super.onCreateDialog(id);
	}

}
