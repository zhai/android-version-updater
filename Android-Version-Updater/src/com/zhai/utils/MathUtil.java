package com.zhai.utils;

import java.text.DecimalFormat;

public class MathUtil {
	public static DecimalFormat df = new DecimalFormat("#############.##");
	public static DecimalFormat df_int = new DecimalFormat("#############");
	public static String formatNum(float v){
		return df.format(v);
	}
	public static String formatNum2Int(float v){
		return df_int.format(v);
	}
	public static int getRndNum(int min, int max) {
		// 产生一个min到max之间的随机整数
		return (int) Math.round((max - min) * Math.random() + min);
	}

	public static int getRndNum(int min, int max, int[] nomatchs) {
		// 产生一个min到max之间的随机整数
		int i = getRndNum(min, max);
		if (nomatchs == null || nomatchs.length == 0)
			return i;
		int count = 0;
		while (true) {
			if (count > 5000) {
				return i;
			}
			boolean bEquals = false;
			for (int k = 0; k < nomatchs.length; k++) {
				if (nomatchs[k] == i) {
					i = getRndNum(min, max);
					bEquals = true;
					break;
				}
			}
			if (!bEquals) {
				return i;
			}
			count++;
		}
	}
}
