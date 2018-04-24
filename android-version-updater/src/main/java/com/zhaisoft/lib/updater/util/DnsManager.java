package com.zhaisoft.lib.updater.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DnsManager {

    private static final String TAG = "DnsManager";


    public static String[] getDns(Context context) throws IOException,
            InterruptedException {

        // 第一个
        StringBuffer sb = new StringBuffer();
        String cmd = String.format("getprop net.dns%d ", 1);
        Process p = Runtime.getRuntime().exec(cmd);

        DataOutputStream os = new DataOutputStream(p.getOutputStream());
        os.flush();
        os.close();
        InputStreamReader r = new InputStreamReader(p.getInputStream());
        final char buf[] = new char[1024];
        int read = 0;
        while ((read = r.read(buf)) != -1) {
            if (sb != null)
                sb.append(buf, 0, read);
        }

        // 第二个
        StringBuffer sb2 = new StringBuffer();
        String cmd2 = "getprop net.dns2";
        Process p2 = Runtime.getRuntime().exec(cmd2);

        DataOutputStream os2 = new DataOutputStream(p2.getOutputStream());

        os2.flush();
        os2.close();
        InputStreamReader r2 = new InputStreamReader(p2.getInputStream());
        final char buf2[] = new char[1024];
        int read2 = 0;
        while ((read2 = r2.read(buf2)) != -1) {
            if (sb2 != null)
                sb2.append(buf2, 0, read2);
        }

        return new String[]{sb.toString().replace("\n", ""),
                sb2.toString().replace("\n", "")};
    }

    public static String[] getDns_nouser(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(context.WIFI_SERVICE);
        String dns1 = intToIp(wifiManager.getDhcpInfo().dns1);
        String dns2 = intToIp(wifiManager.getDhcpInfo().dns2);
        return new String[]{dns1, dns2};
    }

    public static boolean setWIFIDNS(String ip, int index) throws IOException,
            InterruptedException {

        String cmd = String.format("setprop net.dns%d %s", index, ip);

        Log.i(TAG, cmd);
        Process p = Runtime.getRuntime().exec("su");

        // Attempt to write a file to a root-only
        DataOutputStream os = new DataOutputStream(p.getOutputStream());
        os.writeBytes(cmd + "\n");

        // Close the terminal
        os.writeBytes("exit\n");
        os.flush();

        p.waitFor();
        if (p.exitValue() != 255) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean setGPRSDNS(String ip, int index) throws IOException,
            InterruptedException {

        String cmd = String.format("setprop net.rmnet0.dns%d %s", index, ip);

        Log.i(TAG, cmd);
        Process p = Runtime.getRuntime().exec("su");

        // Attempt to write a file to a root-only
        DataOutputStream os = new DataOutputStream(p.getOutputStream());
        os.writeBytes(cmd + "\n");

        // Close the terminal
        os.writeBytes("exit\n");
        os.flush();

        p.waitFor();
        if (p.exitValue() != 255) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean setDNS() throws IOException,
            InterruptedException {
        return setDNS();
    }


    public static boolean setDNS(String ip, int index) throws IOException,
            InterruptedException {

        String cmd = String.format("setprop net.dns%d %s", index, ip);

        Log.i(TAG, cmd);
        Process p = Runtime.getRuntime().exec("su");

        // Attempt to write a file to a root-only
        DataOutputStream os = new DataOutputStream(p.getOutputStream());
        os.writeBytes(cmd + "\n");

        // Close the terminal
        os.writeBytes("exit\n");
        os.flush();

        p.waitFor();
        if (p.exitValue() != 255) {
            return true;
        } else {
            return false;
        }
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + ((i >> 24) & 0xFF);
    }
}
