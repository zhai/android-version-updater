package com.zhaisoft.lib.updater.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtil {

	public static Date getDateByString(String time) {
		Date date = null;
		if (time == null)
			return date;
		String date_format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(date_format);
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getShortTime(String timeString) {
		String shortstring = null;
		// String time = timestampToStr(dateline);
		Date date = getDateByString(timeString);
		if (date == null)
			return shortstring;
		long now = Calendar.getInstance().getTimeInMillis();
		long deltime = (now - date.getTime()) / 1000;
		if (deltime > 365 * 24 * 60 * 60) {
			shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "年前";
		} else if (deltime > 24 * 60 * 60 * 30) {
			shortstring = (int) (deltime / (24 * 60 * 60 * 30)) + "月前";
		} else if (deltime > 24 * 60 * 60 * 7) {
			shortstring = (int) (deltime / (24 * 60 * 60 * 7)) + "周前";
		} else if (deltime > 24 * 60 * 60) {
			shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
		} else if (deltime > 60 * 60) {
			shortstring = (int) (deltime / (60 * 60)) + "小时前";
		} else if (deltime > 60 * 30) {
			shortstring = "半小时前";
		} else if (deltime > 60) {
			shortstring = (int) (deltime / (60)) + "分钟前";
		} else if (deltime > 1) {
			shortstring = deltime + "秒前";
		} else {
			shortstring = "1秒前";
		}
		return shortstring;
	}

	public static String getShortTime(long dateline) {
		String shortstring = null;
		String time = timestampToStr(dateline);
		Date date = getDateByString(time);
		if (date == null)
			return shortstring;

		long now = Calendar.getInstance().getTimeInMillis();
		long deltime = (now - date.getTime()) / 1000;
		if (deltime > 365 * 24 * 60 * 60) {
			shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "年前";
		} else if (deltime > 24 * 60 * 60) {
			shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
		} else if (deltime > 60 * 60) {
			shortstring = (int) (deltime / (60 * 60)) + "小时前";
		} else if (deltime > 60) {
			shortstring = (int) (deltime / (60)) + "分前";
		} else if (deltime > 1) {
			shortstring = deltime + "秒前";
		} else {
			shortstring = "1秒前";
		}
		return shortstring;
	}

	// Timestamp转化为String:
	public static String timestampToStr(long dateline) {
		Timestamp timestamp = new Timestamp(dateline * 1000);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
		return df.format(timestamp);
	}

	/*
	 * public static void main(String[] args) { long dateline = 1335189486;
	 * System.out.println(getShortTime(dateline)); // String time =
	 * "2012-04-20 10:40:55"; // System.out.println(getShortTime(time)); }
	 */

	public static Date String2Date(String format, String dateStr) {
		DateFormat df = new SimpleDateFormat(format);
		// SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		// Date dt2 = sdf.parse(sDt);
		Date date;
		try {
			date = df.parse(dateStr);

			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}

	}

	public static Calendar String2Calendar(String format, String dateStr) {
		Calendar d11 = new GregorianCalendar();
		Date d1 = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);// 时间格式自己设置
		String baseDate = "2008-7-8 8:9:55";// 测试数据
		try { // 一定要放到try里面,不然会报错的
			d1 = sdf.parse(dateStr);
		} catch (Exception e) {
		}

		d11.setTime(d1); // OK了,d11就是结果了

		return d11;
	}

	public static String convertMill2DateString(long mill) {
		Date date = new Date(mill);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	public static String getDate(String unixDate) {

		/*
		 * String date = new java.text.SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
		 * .format(new java.util.Date(Long.parseLong("1348549680") * 1000));
		 * System.out.println(date); return date;
		 */
		// SimpleDateFormat fm1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat fm2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// TimeZone.setDefault(TimeZone.getTimeZone("GMT+0"));
		fm2.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		long unixLong = 0;
		String date = "";
		try {
			unixLong = Long.parseLong(unixDate) * 1000;
		} catch (Exception ex) {
			System.out.println("String转换Long错误，请确认数据可以转换！");
		}
		try {
			// date = fm1.format(unixLong);
			date = fm2.format(unixLong);
		} catch (Exception ex) {
			System.out.println("String转换Date错误，请确认数据可以转换！");
		}
		System.out.println(date);

		return date;
	}

	public static String getDate(String timeformat, String unixDate) {

		/*
		 * String date = new java.text.SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
		 * .format(new java.util.Date(Long.parseLong("1348549680") * 1000));
		 * System.out.println(date); return date;
		 */
		// SimpleDateFormat fm1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat fm2 = new SimpleDateFormat(timeformat);
		// TimeZone.setDefault(TimeZone.getTimeZone("GMT+0"));
		fm2.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		long unixLong = 0;
		String date = "";
		try {
			unixLong = Long.parseLong(unixDate) * 1000;
		} catch (Exception ex) {
			System.out.println("String转换Long错误，请确认数据可以转换！");
		}
		try {
			// date = fm1.format(unixLong);
			date = fm2.format(unixLong);
		} catch (Exception ex) {
			System.out.println("String转换Date错误，请确认数据可以转换！");
		}
		System.out.println(date);

		return date;
	}

	public String getTimeString(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.getTime().toLocaleString();

	}

}
