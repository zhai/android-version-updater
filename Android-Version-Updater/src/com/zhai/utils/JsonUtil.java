package com.zhai.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	/**
	 * 此类已修正完成，也学习过
	 * 
	 * @author zhai
	 * @param 由JsonString
	 *            转换成Json对象
	 * @return JSONObject jsonObject = new JSONObject(jsonStr);
	 */
	public static JSONObject getJSONObject(String str) {
		if (str == null || str.trim().length() == 0) {
			// str.trim() 去掉字符串首尾的空格
			return null;
		}
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace(System.err);
		}
		return jsonObject;
	}
}
