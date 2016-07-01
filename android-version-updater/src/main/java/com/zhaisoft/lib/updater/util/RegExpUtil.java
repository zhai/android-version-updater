package com.zhaisoft.lib.updater.util;

public class RegExpUtil {

	// 能用的表达式
	static String reg_phonenum_telecom = "^[1](33|53|80|81|89)[0-9]{8}$"; // 是否天翼号码
																			// 电信
	static String reg_phonenum_all = "^(1(([35][0-9])|(47)|[8][01236789]))\\d{8}$"; // 电话号码

	static String reg_password = "[a-zA-Z0-9]{6,16}";

	static String reg_email = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

	// str.matches("[0-9]{3,11}")

	// 1. 简单的电话号码，3-11位 String reg =" [0-9]{3,11} ";

	// 2. 电信号码 String reg = "^[1](33|53|80|89)[0-9]{8}$";

	// 3. 全部的手机号码 (现在的手机号码增加了150,153,156,158,159，157，188，189,180,147,183)

	// String reg ="^(1(([35][0-9])|(47)|[8][01236789]))\d{8}$";

	// 4. 严格的座机 ^0\d{2,3}(\-)?\d{7,8}$

	/**
	 * 是否是手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobilePhoneNumber(String str) {
		if (str.matches(reg_phonenum_all))
			return true;
		return false;
	}

	/**
	 * 是否是电信号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isTelecomMobilePhoneNumber(String str) {
		if (str.matches(reg_phonenum_telecom))
			return true;

		return false;
	}

	/**
	 * 6-16位的密码字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPasswordRight(String str) {
		if (str.matches(reg_password))
			return true;
		return false;
	}

	/**
	 * 验证邮箱格式是否正确
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmailRight(String str) {
		if (str.matches(reg_email))
			return true;
		return false;
	}
}
