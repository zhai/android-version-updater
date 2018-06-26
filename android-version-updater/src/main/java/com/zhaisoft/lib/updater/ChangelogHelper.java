package com.zhaisoft.lib.updater;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author zhai
 */
public final class ChangelogHelper {
    public static final String TAG = "ChangelogHelper";
    private static final String ASSET_CHAGELOG = "releasenote.txt";
    private static final String PREFS_LAST_RUN = "lastrun";

    private ChangelogHelper() {
    }

    public static boolean isNewVersion(final Context context) {
        final SharedPreferences p = PreferenceManager
                .getDefaultSharedPreferences(context);
        final String v0 = p.getString(PREFS_LAST_RUN, "");
        String v1 = null;
        try {
            v1 = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "package not found: " + context.getPackageName(), e);
        }
        if (v0.equals(v1)) {
            return false;
        }
        return true;
    }

    public static void showChangelog(final Context context, final String title,
                                     final String appname, final int resChanges, final int resNotes) {
        final SharedPreferences p = PreferenceManager
                .getDefaultSharedPreferences(context);
        final String v0 = p.getString(PREFS_LAST_RUN, "");
        String v1 = null;
        try {
            v1 = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "package not found: " + context.getPackageName(), e);
        }
        p.edit().putString(PREFS_LAST_RUN, v1).commit();
        if (v0.length() == 0) {
            Log.d(TAG, "first boot, skip changelog");
            // return;
        }
        if (v0.equals(v1)) {
            Log.d(TAG, "no changes");
            return;
        }

        String[] changes = context.getResources().getStringArray(resChanges);
        String[] notes = resNotes > 0 ? context.getResources().getStringArray(
                resNotes) : null;

        final SpannableStringBuilder sb = new SpannableStringBuilder();
        for (String s : notes) {
            SpannableString ss = new SpannableString(s + "\n");
            int j = s.indexOf(":");
            if (j > 0) {
                if (!TextUtils.isEmpty(s)) {
                    ss.setSpan(new StyleSpan(Typeface.BOLD), 0, j,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            sb.append(ss);
            sb.append("\n");
        }
        if (notes != null && notes.length > 0) {
            sb.append("\n");
        }
        for (String s : changes) {
            s = appname + " "
                    + s.replaceFirst(": ", ":\n* ").replaceAll(", ", "\n* ")
                    + "\n";
            SpannableString ss = new SpannableString(s);
            int j = s.indexOf(":");
            if (j > 0) {
                ss.setSpan(new StyleSpan(Typeface.BOLD), 0, j,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            sb.append(ss);
            sb.append("\n");
        }
        sb.setSpan(new RelativeSizeSpan(0.75f), 0, sb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        changes = null;
        notes = null;

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle(title);
        b.setMessage(sb);
        b.setCancelable(true);
        b.setPositiveButton(android.R.string.ok, null);
        b.show();
    }

    public static void showChangeLogFromAssets(Context context) {
        final SharedPreferences p = PreferenceManager
                .getDefaultSharedPreferences(context);
        final String v0 = p.getString(PREFS_LAST_RUN, "");
        String v1 = null;
        try {
            v1 = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "package not found: " + context.getPackageName(), e);
        }
        p.edit().putString(PREFS_LAST_RUN, v1).commit();
        if (v0.length() == 0) {
            Log.d(TAG, "第一次使用,显示更新日志");
            // return;
        }
        if (v0.equals(v1)) {
            Log.d(TAG, "没有更新");
            return;
        }
        // 继续后面的活动
        showChangeLogDialogFromAssets(context);
    }

    public static void showChangeLogDialogFromAssets(Context context) {
        CharSequence log = readAssetsFile(context);
        final SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(log);

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle(context.getResources().getString(R.string.app_name)
                + context.getResources().getString(R.string.app_changelog));
        b.setMessage(sb);
        b.setCancelable(true);
        b.setPositiveButton(android.R.string.ok, null);
        b.show();
    }

    private static CharSequence readAssetsFile(final Context context) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(context.getAssets()
                    .open(ASSET_CHAGELOG)));
            String line;
            SpannableStringBuilder sb = new SpannableStringBuilder();
            while ((line = in.readLine()) != null) {
                SpannableStringBuilder sbline;
                int j = line.indexOf(":");
                if (j > 0) {
                    String line_Version = line.substring(0, j - 5) + ":     \n";
                    sbline = new SpannableStringBuilder(line_Version);
                    if (!TextUtils.isEmpty(line)) {
                        sbline.setSpan(new StyleSpan(Typeface.BOLD), 0,
                                line_Version.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sbline.setSpan(new ForegroundColorSpan(Color.CYAN), 0,
                                line_Version.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sbline.setSpan(new RelativeSizeSpan(0.85f), 0,
                                line_Version.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    // 添加更新日期
                    String line_date = "更新时间: "
                            + line.substring(j - 4, j).substring(0, 2) + "月"
                            + line.substring(j - 4, j).substring(2, 4) + "日\n";
                    SpannableStringBuilder sb_date = new SpannableStringBuilder(
                            line_date);
                    sb_date.setSpan(new ForegroundColorSpan(Color.GRAY), 0,
                            line_date.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sb_date.setSpan(new RelativeSizeSpan(0.80f), 0,
                            sb_date.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sbline.append(sb_date);

                } else {
                    if (line.length() > 1) {
                        line = "      ◆ " + line + "\n";
                    } else {
                        line = line + "\n";
                    }
                    sbline = new SpannableStringBuilder(line);
                    sbline.setSpan(new RelativeSizeSpan(0.75f), 0,
                            sbline.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                sb.append(sbline);
                // sb.append(line).append('\n');
            }
            return sb;
        } catch (IOException e) {
            return "";
        } finally {
            closeStream(in);
        }
    }

    private static void closeStream(final Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}
