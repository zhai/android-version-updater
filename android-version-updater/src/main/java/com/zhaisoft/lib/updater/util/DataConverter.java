package com.zhaisoft.lib.updater.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class DataConverter {
	public static String TAG = "DataConverter";

	public static String removeSuffix(String str, String suffix) {
		if (str.endsWith(suffix)) {
			int index = str.lastIndexOf(suffix);
			return str.substring(0, index);
		}
		return str;
	}

	public static String StringFilter(String str) {
		if (str == null)
			return "";
		return str.replaceAll("<p>", "").replaceAll("</p>", "</br>").replaceAll("【", " [").replaceAll("】", "] ")
				.replaceAll("！", "! ").replaceAll(":", ": ").replaceAll("：", ": ").replaceAll("；", "; ")
				.replaceAll("，", ", ").replaceAll("（", " (").replaceAll("。", ". ").replaceAll("）", ") ")
				.replace('\r', ' ').trim();
	}

	public static String StringFilterNoSpace(String str) {
		if (str == null)
			return "";
		return str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll(":", ":")
				.replaceAll("：", ":").replaceAll("；", ";").replaceAll("，", ",").replaceAll("（", "(")
				.replaceAll("。", ".").replaceAll("）", ")").replace('\r', ' ').trim();
	}

	public static String StringFilterDotNoSpace(String str) {
		if (str == null)
			return "";
		return str.replaceAll("【", " [").replaceAll("】", "] ").replaceAll("！", "! ").replaceAll(":", ": ")
				.replaceAll("：", ": ")
				// .replaceAll("、", ", ")
				.replaceAll("；", "; ").replaceAll("，", ", ").replaceAll("（", " (").replaceAll("。", ".")
				.replaceAll("）", ") ").replace('\r', ' ').trim();
	}

	/**
	 * 
	 * @param addPos
	 *            will create strings len = ori.length + addPos
	 * @param ori
	 * @param PaddingLeft
	 *            whether inserts tab before string or not
	 * @return result or null
	 */
	public static String[] newStringArray(int addPos, String[] ori, boolean PaddingLeft) {
		if (ori == null)
			return null;
		String[] strings;
		int size;

		size = ori.length;
		strings = new String[size + addPos];
		if (PaddingLeft == false) {
			for (int i = 0; i < size; i++) {
				strings[i + addPos] = ori[i];
			}
		} else {
			for (int i = 0; i < size; i++) {
				strings[i + addPos] = "\t\t" + ori[i];
			}
		}

		return strings;
	}

	/**
	 * split string to strings, each string with fix len, except the last string
	 * 
	 * @param len
	 *            the length to split with
	 * @return str="12345678",len=3 --> {"123","456","78"}
	 */
	public static String[] Split(String str, int len) {
		if (len == 0 || str == null)
			return null;
		int i = 0;
		int length = str.length();
		char[] chas = str.toCharArray();
		int maxSize = length * (len > 1 ? 1 : 2);
		int[] lastCharPoses = new int[maxSize];
		lastCharPoses[0] = 0;
		int bytelen = 0;
		int num = 1;
		for (i = 0; i < length; i++) {
			if (chas[i] > 0xff)
				bytelen += 2;
			else
				bytelen++;
			if (bytelen > num * len) {
				lastCharPoses[num] = i;
				num++;
			}
		}
		lastCharPoses[num] = i;

		String[] result = new String[num];

		for (i = 0; i < num - 1; i++) {
			result[i] = str.substring(lastCharPoses[i], lastCharPoses[i + 1]);
		}
		result[num - 1] = str.substring(lastCharPoses[i]);
		return result;
	}

	/**
	 * when string contains Chinese, we should get the byte length separately
	 * <br>
	 * Traditional, a Chinese character is 2 byte length, others are 1 byte
	 * length;
	 * 
	 * @param ChStr
	 * @return
	 */
	public static int getChBytelen(String ChStr) {
		int length = ChStr.length();
		char[] chas = ChStr.toCharArray();
		int bytelen = 0;
		for (int i = 0; i < length; i++) {
			if (chas[i] > 0xff)
				bytelen += 2;
			else
				bytelen++;
		}
		return bytelen;
	}

	public static String TrimLongerString(String str, int maxbytelen) {
		if (str == null)
			return "";
		if (str.getBytes().length <= maxbytelen)
			return str;
		String Pseudo = str;
		int length = str.length();

		char[] chas = str.toCharArray();
		int bytelen = 0;
		for (int i = 0; i < length; i++) {
			if (chas[i] > 0xff)
				bytelen += 2;
			else
				bytelen++;
			if (bytelen > maxbytelen) {
				Pseudo = str.substring(0, i) + "...";
				break;
			}
		}
		return Pseudo;
	}

	/**
	 * if secondStr.startsWith(firstStr) return secondStr
	 * 
	 * @param firstStr
	 * @param secondStr
	 * @return firstStr+secondStr, or firstStr, or secondStr, or empty
	 */
	public static String joinStrings(String firstStr, String secondStr) {
		if (ValidatorUtil.isEffective(firstStr) == false && ValidatorUtil.isEffective(secondStr) == false)
			return "";
		if (ValidatorUtil.isEffective(firstStr) == true && ValidatorUtil.isEffective(secondStr) == false)
			return firstStr;
		if (ValidatorUtil.isEffective(firstStr) == false && ValidatorUtil.isEffective(secondStr) == true)
			return secondStr;
		if (secondStr.startsWith(firstStr))
			return secondStr;
		return firstStr + secondStr;
	}

	public static String joinStringsWithSeparator(List<String> list, String separator) {
		if (list == null)
			return null;
		if (separator == null)
			separator = "";
		int size = list.size();
		if (size <= 0)
			return null;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < size - 1; i++) {
			buffer.append(list.get(i));
			buffer.append(separator);
		}
		buffer.append(list.get(size - 1));
		return buffer.toString();
	}

	/**
	 * Compose strings as a fixed len, fill with " "(space)
	 * 
	 * @param len
	 *            the length to fill
	 * @return str="123",str2="4567", len=20 --> {"123            4567"}
	 */
	public static String Compose(String str1, String str2, int len) {
		int len1 = str1.length();
		int len2 = str2.length();
		String result = str1;
		int num = len - len1 - len2;

		for (int i = 0; i < num; i++) {
			result += "\t";
		}
		result += str2;
		return result;
	}

	public static String convertMD5(byte[] byteMD5) {
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16 进制需要
										// 32 个字符
		int k = 0; // 表示转换结果中对应的字符位置
		for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节 转换成 16 进制字符的转换
			byte byte0 = byteMD5[i]; // 取第 i 个字节
			str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, // >>>
														// 为逻辑右移，将符号位一起右移
			str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
		}
		return new String(str); // 换后的结果转换为字符串
	}

	/**
	 * 
	 * @param source
	 * @return 32 characters which represent a 128 bits long integer
	 */
	public static String getMD5(byte[] source) {
		String s = "";

		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			s = convertMD5(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 
	 * @param ByteSize
	 * @return 12345678 -> 11.8Wcc
	 */
	public static String RenameByteSize(long ByteSize) {
		if (ByteSize < 1024)
			return "" + ByteSize + "B";
		if (ByteSize / 1024 < 1024) {
			return GetDouble1(ByteSize / 1024.0) + "KB";
		}
		return GetDouble1(ByteSize / 1048576.0) + "Mb";
	}

	/**
	 * 
	 * @param url
	 *            which contain filename at last taged by "/xxx.extname";
	 * @return filename or null
	 */
	public static String ExtractFileName(String url) {
		if (url == null)
			return null;
		int len = url.length();
		int start = url.lastIndexOf("/") + 1;
		if (start > len)
			return null;
		String filename = url.substring(start);
		if (filename.contains("."))
			return filename;
		else
			return null;
	}

	/**
	 * http://xxx.xxx.xxx/xxx.extname
	 * 
	 * @param url
	 *            which contain filename at last taged by "/xxx.extname";
	 * @return fileroot http://xxx.xxx.xxx/
	 */
	public static String ExtractFileRoot(String url) {
		if (url == null)
			return null;
		int len = url.length();
		int start = url.lastIndexOf("/") + 1;
		if (start > len)
			return null;
		return url.substring(0, start);

	}

	/**
	 * 
	 * @param filePath
	 * @return String[] (dirPath, fileName)
	 */
	public static String[] ExtractDirPathAndFileName(String filePath) {
		if (filePath == null)
			return null;
		int len = filePath.length();
		int start = filePath.lastIndexOf("/") + 1;
		if (start > len)
			return null;
		String filename = filePath.substring(start);
		if (filename.contains(".")) {
			String[] result = new String[2];
			result[0] = filePath.substring(0, start);
			result[1] = filename;
			return result;
		} else
			return null;
	}

	/**
	 * Write for the format of price
	 * 
	 * @param DecimalPrice
	 *            12.3
	 * @return price string 12.30
	 */
	public static String PriceDecimalFormat(String DecimalPrice) {
		String result = "0.00";
		double value = 0.0;
		try {
			value = Double.parseDouble(DecimalPrice.replaceAll("[,，]", ""));
			DecimalFormat df = new DecimalFormat("0.00");
			result = df.format(value);
			return result;
		} catch (Exception e) {
			return "0.00";
		}
	}

	/**
	 * remove useless 0 after the decimal
	 * 
	 * @param str
	 *            100.00 to 100
	 * @return result or empty
	 */
	public static String RemoveZeroAndDot(String str) {
		if (str == null) {
			return "";
		} else if (str.indexOf(".") > 0) {
			str = str.replaceAll("0*$", "");
			str = str.replaceAll("[.]*$", "");
		}
		return str;
	}

	/**
	 * the decimal point move 1E5 to right
	 * 
	 * @param data
	 *            = 123.456789555
	 * @return 12345679
	 */
	public static int MoveRightE5(double data) {
		return (int) (data * 1E5 + 0.5);
	}

	/**
	 * the decimal point move 1E6 to right
	 * 
	 * @param data
	 *            = 123.456789555
	 * @return 123456790
	 */
	public static int MoveRightE6(double data) {
		return (int) (data * 1E6 + 0.5);
	}

	/**
	 * @param lng
	 *            123.456789555
	 * @param lat
	 *            98.765432555
	 * @return 123459876
	 */
	public static int GetGpsHigh(double lng, double lat) {
		int iLat = (int) (lat * 1E6);
		int iLng = (int) (lng * 1E6);
		int result = (iLng / 10000) * 10000;
		result += (iLat / 10000);
		return result;
	}

	/**
	 * 
	 * @param lng
	 *            123.456789555
	 * @param lat
	 *            98.765432555
	 * @return 67895432
	 */
	public static int GetGpsLow(double lng, double lat) {
		int iLat = (int) (lat * 1E6);
		int iLng = (int) (lng * 1E6);
		int result = (iLat % 10000) * 10000;
		result += (iLng % 10000);
		return result;
	}

	/**
	 * 
	 * @param num
	 *            123.12
	 * @return 123.1
	 */
	public static String GetDouble1(double num) {
		DecimalFormat a = new DecimalFormat("0.0");
		return a.format(num);

	}

	/**
	 * 
	 * @param num
	 *            123.123
	 * @return 123.12
	 */
	public static String GetDouble2(double num) {
		DecimalFormat a = new DecimalFormat("0.00");
		return a.format(num);

	}

	/**
	 * Integer = Decimal * 100
	 * 
	 * @param Decimal
	 * @return Integer String
	 */
	public static String DecimalToInteger(String Decimal) {
		return "" + (int) (parseDouble(Decimal) * 100);
	}

	/**
	 * Decimal = Integer / 100.0
	 * 
	 * @param Integer
	 * @return Decimal String 0.00
	 */
	public static String IntegerToDecimal(String Integer) {
		return GetDouble2((parseInt(Integer)) / 100.0);
	}

	/**
	 * 
	 * @param strInt
	 * @return null or empty return 0
	 */
	public static int parseInt(String strInt) {
		return parseInt(strInt, 0);
	}

	/**
	 * 
	 * @param strInt
	 * @return null or empty return defaultValue
	 */
	public static int parseInt(String strInt, int defaultValue) {
		if (strInt == null || "".equals(strInt))
			return defaultValue;
		try {
			return Integer.parseInt(strInt);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param strFloat
	 * @return null or empty return 0
	 */
	public static float parseFloat(String strFloat) {
		if (strFloat == null || "".equals(strFloat))
			return 0;
		try {
			return Float.parseFloat(strFloat);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 
	 * @param strDouble
	 * @return null or empty return 0
	 */
	public static double parseDouble(String strDouble) {
		if (strDouble == null || "".equals(strDouble))
			return 0;
		try {
			return Double.parseDouble(strDouble);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 
	 * @param strDouble
	 * @return null or empty return defaultValue
	 */
	public static double parseDouble(String strDouble, double defaultValue) {
		if (strDouble == null || "".equals(strDouble))
			return defaultValue;
		try {
			return Double.parseDouble(strDouble);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String urlEncode(String str) {
		if (null == str || "".equals(str)) {
			return "";
		} else {
			return URLEncoder.encode(str);
		}
	}

	public static String urlDecode(String str) {
		if (null == str || "".equals(str)) {
			return "";
		} else {

			return URLDecoder.decode(str);
		}
	}

	/**
	 * 
	 * @param str
	 *            which need be masked
	 * @param masker
	 *            like "*", "#" etc
	 * @param startPos
	 *            start from zero
	 * @param masklen
	 *            the length fill with masker
	 * @return such as 123456 -> 12**56 with masker=*, startPos=2,len=2
	 */
	public static String Mask(String str, String masker, int startPos, int masklen) {
		if (str == null)
			return "";
		if (masklen < 0)
			return str;
		int l = str.length();
		if (startPos > l)
			startPos = l;
		String a = str.substring(0, startPos);

		startPos += masklen;
		if (startPos > l)
			startPos = l;

		String b = str.substring(startPos);
		for (int i = 0; i < masklen; i++)
			a += masker;
		a += b;
		return a;
	}

	/**
	 * 
	 * @param str
	 *            ab/cd\ef:gh*ij?kl"<"m|"n>"
	 * @return abcdefghijklmn
	 */
	public static String FilterFileChar(String str) {
		if (str == null)
			return "";
		String regex = "[//,\\,:,*,?,\",|,<,>,-,_]";
		return str.replaceAll(regex, "");
	}

	public static String ReplaceSpace(String str) {
		if (str == null)
			return "";
		String regex = "[ ,\t,\r,\n]+";
		return str.replaceAll(regex, "");
	}

	/**
	 * check whether contains jsonObject or not<br>
	 * support ..{..{..{..{..}..}..}..}.. four level
	 * 
	 * @param str
	 * @return JsonObject or null
	 */
	public static String getJsonObject(String str) {
		if (str == null)
			return null;
		if (ValidatorUtil.IsContainJson(str)) {
			int start = str.indexOf("{");
			int end = str.lastIndexOf("}");
			if (start > 0 && start <= end && end <= str.length())
				return str.substring(start, end);
			else
				return null;
		}
		return null;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 
	 * @return m/km
	 */
	public static String getDistanceEN(double lat1, double lng1, double lat2, double lng2) {
		double dis = GetDistance(lat1, lng1, lat2, lng2);
		if (dis > 0 && dis < 1000)
			return "" + (int) dis + "m";
		if (dis >= 1000 && dis < 100000)
			return "" + DataConverter.GetDouble1(dis / 1000.0) + "km";
		else if (dis >= 100000) {
			return "" + (int) (dis / 1000) + "km";
		} else
			return "";
	}

	/**
	 * 
	 * @return 米/千米
	 */
	public static String getDistanceCN(double lat1, double lng1, double lat2, double lng2) {
		double dis = GetDistance(lat1, lng1, lat2, lng2);
		if (dis > 0 && dis < 1000)
			return "" + (int) dis + "米";
		if (dis >= 1000 && dis < 100000)
			return "" + DataConverter.GetDouble1(dis / 1000.0) + "千米";
		else if (dis >= 100000) {
			return "" + (int) (dis / 1000) + "千米";
		} else
			return "";
	}

	/**
	 * 
	 * @param distance
	 *            千米
	 * @return 米/千米
	 */
	public static String getDistanceCN(String distance) {
		if (!ValidatorUtil.isEffective(distance)) {
			return "";
		}
		double dis = Double.parseDouble(distance);
		dis *= 1000;
		if (dis >= 0 && dis < 1000)
			return "" + (int) dis + "米";
		if (dis >= 1000 && dis < 100000)
			return "" + DataConverter.GetDouble1(dis / 1000.0) + "千米";
		else if (dis >= 100000) {
			return "" + (int) (dis / 1000) + "千米";
		} else
			return "";
	}

	/**
	 * 
	 * @return meter
	 */
	public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
		double EARTH_RADIUS = 6378.137;// 地球半径
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS * 1000;
		return s;
	}

	/**
	 * //url encode
	 */
	/*
	 * public static String convert(String str, int index) {
	 * 
	 * try { char[] a = str.toCharArray(); int len = a.length; int len2 =
	 * Constant.gchar.length(); for (int i = 0; i < len; i++) { a[i] = (char)
	 * (a[i] ^ Constant.gchar.charAt((i + index) % len2)); }
	 * 
	 * return new String(a); } catch (Exception e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); return str; } }
	 */

	/**
	 * 
	 * @param seconds
	 * @return mm:ss
	 */
	public static String ConvertTime(int seconds) {
		if (seconds <= 0)
			return "00:00";
		// int hours = (seconds/3600)%24;
		int mins = (seconds % 3600) / 60;
		int secs = seconds % 60;

		String time = "";
		// if(hours<10) time+=("0"+hours);
		// else time+=hours;
		// time+=":";
		if (mins < 10)
			time += ("0" + mins);
		else
			time += mins;
		time += ":";
		if (secs < 10)
			time += ("0" + secs);
		else
			time += secs;

		return time;
	}

	/**
	 * 
	 * @param seconds
	 * @return mm'ss"
	 */
	public static String ConvertTime2(int seconds) {
		if (seconds <= 0)
			return "00\"";
		// int hours = (seconds/3600)%24;
		int mins = (seconds % 3600) / 60;
		int secs = seconds % 60;

		String time = "";
		// if(hours<10) time+=("0"+hours);
		// else time+=hours;
		// time+=":";
		if (mins > 0) {
			time += mins;
			time += "'";
		}
		if (secs < 10) {
			time += ("0" + secs);
		} else {
			time += secs;
		}

		return time + "\"";
	}

	/**
	 * match the first, key start with \n\r;\t or at the first of the string
	 * 
	 * @param str
	 * @param keys
	 * @return -1 or index of key end
	 */
	public static int hasKeys(String str, String[] keys) {
		if (str == null || keys == null)
			return -1;
		int len = keys.length;
		int size = str.length();
		int pos = 0;
		int index = -1;
		for (int i = 0; i < len; i++) {
			for (pos = 0; pos < size;) {
				index = str.indexOf(keys[i], pos);
				if (index >= 0) {
					if (index == 0)
						return index + keys[i].length();
					if (("" + str.charAt(index - 1)).replaceAll("[\n\r\t;]", "").length() == 0)
						return index + keys[i].length();
					pos = index + keys[i].length();
				} else
					break;
			}
			index = -1;
		}
		return index;
	}

	/**
	 * match the first, key start with \n\r;\t or at the first of the string
	 * 
	 * @param str
	 * @param key
	 * @return -1 or index of key end
	 */
	public static int hasKey(String str, String key) {
		if (str == null || key == null)
			return -1;

		int index = str.indexOf(key);
		if (index >= 0) {
			if (index == 0)
				return index + key.length();
			if (("" + str.charAt(index - 1)).replaceAll("[\n\r\t;]", "").length() == 0)
				return index + key.length();
		}
		return index;

	}

	/**
	 * match the first,key start with spliter or at the first of the string
	 * 
	 * @param str
	 * @param keys
	 * @return -1 or index of key end
	 */
	public static int hasKeys(String str, String[] keys, String spliter) {
		if (str == null || keys == null)
			return -1;
		int len = keys.length;
		int size = str.length();
		int pos = 0;
		int index = -1;
		for (int i = 0; i < len; i++) {
			for (pos = 0; pos < size;) {
				index = str.indexOf(keys[i], pos);
				if (index >= 0) {
					if (index == 0)
						return index + keys[i].length();
					if (("" + str.charAt(index - 1)).replaceAll(spliter, "").length() == 0)
						return index + keys[i].length();
					pos = index + keys[i].length();
				} else
					break;
			}
			index = -1;
		}
		return index;
	}

	/**
	 * key start with spliter or at the first of the string
	 * 
	 * @param fields
	 * @param keys
	 * @return -1 or index of key end
	 */
	public static List<String> getValueofKeys(String[] fields, String[] keys) {
		List<String> values = new ArrayList<String>();
		if (fields == null || keys == null)
			return values;
		int size = fields.length;
		int len = keys.length;

		for (int i = 0; i < size; i++) {
			String value = fields[i];
			for (int j = 0; j < len; j++) {
				if (value.startsWith(keys[j])) {
					int index = keys[j].length();
					values.add(value.substring(index));
					j = len;
				}
			}
		}
		return values;
	}

	/**
	 * key start with spliter or at the first of the string
	 * 
	 * @param fields
	 * @param key
	 * @return -1 or index of key end
	 */
	public static List<String> getValueofKey(String[] fields, String key) {
		List<String> values = new ArrayList<String>();
		if (fields == null || key == null)
			return values;
		int size = fields.length;

		for (int i = 0; i < size; i++) {
			String value = fields[i];
			if (value.startsWith(key)) {
				int index = key.length();
				values.add(value.substring(index));
			}
		}
		return values;
	}

	/**
	 * key start with spliter or at the first of the string
	 * 
	 * @param str
	 * @param key
	 * @return -1 or index of key end
	 */
	public static int hasKey(String str, String key, String spliter) {
		if (str == null || key == null)
			return -1;

		int index = str.indexOf(key);
		if (index >= 0) {
			if (index == 0)
				return index + key.length();
			if (("" + str.charAt(index - 1)).replaceAll(spliter, "").length() == 0)
				return index + key.length();
		}
		return index;
	}

	/**
	 * key start and end with spliter or at the first or end of the string
	 * 
	 * @param str
	 * @param keys
	 * @return null or the value
	 */
	public static String getValueofKey(String str, String[] keys, String spliter) {
		int index = DataConverter.hasKeys(str, keys, spliter);
		if (index != -1) {
			String[] tmps = str.substring(index).split(spliter);
			if (tmps != null && tmps.length > 0)
				return tmps[0];
			else
				return null;
		}
		return null;
	}

	/**
	 * key start and end with spliter or at the first or end of the string
	 * 
	 * @param str
	 * @param key
	 * @return null or the value
	 */
	public static String getValueofKey(String str, String key, String spliter) {
		int index = DataConverter.hasKey(str, key, spliter);
		if (index != -1) {
			String[] tmps = str.substring(index).split(spliter);
			if (tmps != null && tmps.length > 0)
				return tmps[0];
			else
				return null;
		}
		return null;
	}

	/**
	 * key start and end with \n\r;\t or at the first or end of the string
	 * 
	 * @param str
	 * @param keys
	 * @return null or the value
	 */
	public static String getValueofKey(String str, String[] keys) {
		int index = DataConverter.hasKeys(str, keys);
		if (index != -1) {
			String[] tmps = str.substring(index).split("[\n\r;\t]");
			if (tmps != null && tmps.length > 0)
				return tmps[0];
			else
				return null;

		}
		return null;
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return halfCorner or empty
	 */
	public static String fullToHalfCorner(String input) {

		if (input != null) {
			char c[] = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == '\u3000') {
					c[i] = ' ';
				} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
					c[i] = (char) (c[i] - 65248);
				}
			}
			return new String(c);
		} else {
			return "";
		}
	}

	/**
	 * @param currencyId
	 *            0-人民币；1-港币；2-台币；3-澳门元；4-美元；5-英镑;6-欧元
	 * @return 对应的货币符号，其他的返回人民币符号
	 */
	public static String getCurrencySyWccolById(String currencyId) {

		int type = DataConverter.parseInt(currencyId);
		switch (type) {
		case 1:
			return Currency.getInstance("HKD").getSymbol();
		case 2:
			return Currency.getInstance("TWD").getSymbol();
		case 3:
			return Currency.getInstance("MOP").getSymbol();
		case 4:
			return Currency.getInstance("USD").getSymbol();
		case 5:
			return Currency.getInstance("GBP").getSymbol();
		case 6:
			return Currency.getInstance("EUR").getSymbol();
		default:
			return Currency.getInstance("CNY").getSymbol();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// 根据汉字获取拼音首字母
	private static final String firstLetterArray = "ydkqsxnwzssxjbymgcczqpssqbycdscdqldylybssjgyqzjjfgcclzznwdwzjljpfyynnjjtmynzwzhflzppqhgccyynmjqyxxgd"
			+ "nnsnsjnjnsnnmlnrxyfsngnnnnqzggllyjlnyzssecykyyhqwjssggyxyqyjtwktjhychmnxjtlhjyqbyxdldwrrjnwysrldzjpc"
			+ "bzjjbrcfslnczstzfxxchtrqggddlyccssymmrjcyqzpwwjjyfcrwfdfzqpyddwyxkyjawjffxjbcftzyhhycysWccyxsclcxxwz"
			+ "cxnbgnnxbxlzsqsbsjpysazdhmdzbqbscwdzzyytzhbtsyyfzgntnxjywqnknphhlxgybfmjnbjhhgqtjcysxstkzglyckglysmz"
			+ "xyalmeldccxgzyrjxjzlnjzcqkcnnjwhjczccqljststbnhbtyxceqxkkwjyflzqlyhjxspsfxlmpbysxxxytccnylllsjxfhjxp"
			+ "jbtffyabyxbcczbzyclwlczggbtssmdtjcxpthyqtgjjxcjfzkjzjqnlzwlslhdzbwjncjzyzsqnycqynzcjjwybrtwpyftwexcs"
			+ "kdzctbyhyzqyyjxzcfbzzmjyxxsdczottbzljwfckscsxfyrlrygWccdthjxsqjccsbxyytswfbjdztnbcnzlcyzzpsacyzzsqqcs"
			+ "hzqydxlbpjllmqxqydzxsqjtzpxlcglqdcwzfhctdjjsfxjejjtlbgxsxjmyjjqpfzasyjnsydjxkjcdjsznbartcclnjqmwnqnc"
			+ "lllkbdbzzsyhqcltwlccrshllzntylnewyzyxczxxgdkdmtcedejtsyyssdqdfmxdbjlkrwnqlybglxnlgtgxbqjdznyjsjyjcjm"
			+ "rnymgrcjczgjmzmgxmmryxkjnymsgmzzymknfxWccdtgfbhcjhkylpfmdxlxjjsmsqgzsjlqdldgjycalcmzcsdjllnxdjffffjcn"
			+ "fnnffpfkhkgdpqxktacjdhhzdddrrcfqyjkqccwjdxhwjlyllzgcfcqjsmlzpbjjblsbcjggdckkdezsqcckjgcgkdjtjllzycxk"
			+ "lqccgjcltfpcqczgwbjdqyzjjbyjhsjddwgfsjgzkcjctllfspkjgqjhzzljplgjgjjthjjyjzccmlzlyqbgjwmljkxzdznjqsyz"
			+ "mljlljkywxmkjlhskjhbmclyymkxjqlbmllkmdxxkwyxwslmlpsjqqjqxyqfjtjdxmxxllcrqbsyjbgwynnggbcnxpjtgpapfgdj"
			+ "qbhbncfjyzjkjkhxqfgqckfhygkhdkllsdjqxpqyaybnqsxqnszswhbsxwhxwbzzxdmndjbsbkbbzklylxgwxjjwaqzmywsjqlsj"
			+ "xxjqwjeqxnchetlzalyyyszzpnkyzcptlshtzcfycyxyljsdcjqagyslcllyyysslqqqnldxzsccscadycjysfsgbfrsszqsbxjp"
			+ "sjysdrckgjlgtkzjzbdktcsyqpyhstcldjnhmymcgxyzhjdctmhltxzhylamoxyjcltyfbqqjpfbdfehthsqhzywwcncxcdwhowg"
			+ "yjlegmdqcwgfjhcsntmydolbygnqwesqpwnmlrydzszzlyqpzgcwxhnxpyxshmdqjgztdppbfbhzhhjyfdzwkgkzbldnzsxhqeeg"
			+ "zxylzmmzyjzgszxkhkhtxexxgylyapsthxdwhzydpxagkydxbhnhnkdnjnmyhylpmgecslnzhkxxlbzzlbmlsfbhhgsgyyggbhsc"
			+ "yajtxglxtzmcwzydqdqmngdnllszhngjzwfyhqswscelqajynytlsxthaznkzzsdhlaxxtwwcjhqqtddwzbcchyqzflxpslzqgpz"
			+ "sznglydqtbdlxntctajdkywnsyzljhhdzckryyzywmhychhhxhjkzwsxhdnxlyscqydpslyzwmypnkxyjlkchtyhaxqsyshxasmc"
			+ "hkdscrsgjpwqsgzjlwwschsjhsqnhnsngndantbaalczmsstdqjcjktscjnxplggxhhgoxzcxpdmmhldgtybynjmxhmrzplxjzck"
			+ "zxshflqxxcdhxwzpckczcdytcjyxqhlxdhypjqxnlsyydzozjnhhqezysjyayxkypdgxddnsppyzndhthrhxydpcjjhtcnnctlhb"
			+ "ynyhmhzllnnxmylllmdcppxhmxdkycyrdltxjchhznxclcclylnzsxnjzzlnnnnwhyqsnjhxynttdkyjpychhyegkcwtwlgjrlgg"
			+ "tgtygyhpyhylqyqgcwyqkpyyettttlhyylltyttsylnyzwgywgpydqqzzdqnnkcqnmjjzzbxtqfjkdffbtkhzkbxdjjkdjjtlbwf"
			+ "zpptkqtztgpdwntpjyfalqmkgxbcclzfhzcllllanpnxtjklcclgyhdzfgyddgcyyfgydxkssendhykdndknnaxxhbpbyyhxccga"
			+ "pfqyjjdmlxcsjzllpcnbsxgjyndybwjspcwjlzkzddtacsbkzdyzypjzqsjnkktknjdjgyepgtlnyqnacdntcyhblgdzhbbydmjr"
			+ "egkzyheyybjmcdtafzjzhgcjnlghldwxjjkytcyksssmtwcttqzlpbszdtwcxgzagyktywxlnlcpbclloqmmzsslcWccjcsdzkydc"
			+ "zjgqjdsmcytzqqlnzqzxssbpkdfqmddzzsddtdmfhtdycnaqjqkypbdjyyxtljhdrqxlmhkydhrnlklytwhllrllrcxylbnsrnzz"
			+ "symqzzhhkyhxksmzsyzgcxfbnbsqlfzxxnnxkxwymsddyqnggqmmyhcdzttfgyyhgsbttybykjdnkyjbelhdypjqnfxfdnkzhqks"
			+ "byjtzbxhfdsbdaswpawajldyjsfhblcnndnqjtjnchxfjsrfwhzfmdrfjyxwzpdjkzyjympcyznynxfbytfyfwygdbnzzzdnytxz"
			+ "emmqbsqehxfznbmflzzsrsyqjgsxwzjsprytjsjgskjjgljjynzjjxhgjkymlpyyycxycgqzswhwlyrjlpxslcxmnsmwklcdnkny"
			+ "npsjszhdzeptxmwywxyysywlxjqcqxzdclaeelmcpjpclwbxsqhfwrtfnjtnqjhjqdxhwlbyccfjlylkyynldxnhycstyywncjtx"
			+ "ywtrmdrqnwqcmfjdxzmhmayxnwmyzqtxtlmrspwwjhanbxtgzypxyyrrclmpamgkqjszycymyjsnxtplnbappypylxmyzkynldgy"
			+ "jzcchnlmzhhanqnbgwqtzmxxmllhgdzxnhxhrxycjmffxywcfsbssqlhnndycannmtcjcypnxnytycnnymnmsxndlylysljnlxys"
			+ "sqmllyzlzjjjkyzzcsfbzxxmstbjgnxnchlsnmcjscyznfzlxbrnnnylmnrtgzqysatswryhyjzmgdhzgzdwybsscskxsyhytsxg"
			+ "cqgxzzbhyxjscrhmkkbsczjyjymkqhzjfnbhmqhysnjnzybknqmcjgqhwlsnzswxkhljhyybqcbfcdsxdldspfzfskjjzwzxsddx"
			+ "jseeegjscssygclxxnwwyllymwwwgydkzjggggggsycknjwnjpcxbjjtqtjwdsspjxcxnzxnmelptfsxtllxcljxjjljsxctnswx"
			+ "lennlyqrwhsycsqnybyaywjejqfwqcqqcjqgxaldbzzyjgkgxbltqyfxjltpydkyqhpmatlcndnkxmtxynhklefxdllegqtymsaw"
			+ "hzmljtkynxlyjzljeeyybqqffnlyxhdsctgjhxywlkllxqkcctnhjlqmkkzgcyygllljdcgydhzwypysjbzjdzgyzzhywyfqdtyz"
			+ "szyezklymgjjhtsmqwyzljyywzcsrkqyqltdxwcdrjalwsqzwbdcqyncjnnszjlncdcdtlzzzacqqzzddxyblxcbqjylzllljddz"
			+ "jgyqyjzyxnyyyexjxksdaznyrdlzyyynjlslldyxjcykywnqcclddnyyynycgczhjxcclgzqjgnwnncqqjysbzzxyjxjnxjfzbsb"
			+ "dsfnsfpzxhdwztdmpptflzzbzdmyypqjrsdzsqzsqxbdgcpzswdwcsqzgmdhzxmwwfybpngphdmjthzsmWccgzWcczjcfzhfcbbnmq"
			+ "dfWcccmcjxlgpnjbbxgyhyyjgptzgzmqbqdcgybjxlwnkydpdymgcftpfxyztzxdzxtgkptybbclbjaskytssqyymscxfjhhlslls"
			+ "jpqjjqaklyldlycctsxmcwfgngbqxllllnyxtyltyxytdpjhnhgnkbyqnfjyyzbyyessessgdyhfhwtcqbsdzjtfdmxhcnjzymqw"
			+ "srxjdzjqbdqbbsdjgnfbknbxdkqhmkwjjjgdllthzhhyyyyhhsxztyyyccbdbpypzyccztjpzywcbdlfwzcwjdxxhyhlhwczxjtc"
			+ "nlcdpxnqczczlyxjjcjbhfxwpywxzpcdzzbdccjwjhmlxbqxxbylrddgjrrctttgqdczwmxfytmmzcwjwxyywzzkybzcccttqnhx"
			+ "nwxxkhkfhtswoccjybcmpzzykbnnzpbthhjdlszddytyfjpxyngfxbyqxzbhxcpxxtnzdnnycnxsxlhkmzxlthdhkghxxsshqyhh"
			+ "cjyxglhzxcxnhekdtgqxqypkdhentykcnymyyjmkqyyyjxzlthhqtbyqhxbmyhsqckwwyllhcyylnneqxqwmcfbdccmljggxdqkt"
			+ "lxkknqcdgcjwyjjlyhhqyttnwchhxcxwherzjydjccdbqcdgdnyxzdhcqrxcbhztqcbxwgqwyybxhWccymykdyecmqkyaqyngyzsl"
			+ "fnkkqgyssqyshngjctxkzycssbkyxhyylstycxqthysmnscpmmgcccccmnztasmgqzjhklosjylswtmqzyqkdzljqqyplzycztcq"
			+ "qpbbcjzclpkhqcyyxxdtdddsjcxffllchqxmjlwcjcxtspycxndtjshjwhdqqqckxyamylsjhmlalygxcyydmamdqmlmcznnyybz"
			+ "xkyflmcncmlhxrcjjhsylnmtjggzgywjxsrxcwjgjqhqzdqjdcjjskjkgdzcgjjyjylxzxxcdqhhheslmhlfsbdjsyyshfyssczq"
			+ "lpbdrfnztzdkykhsccgkwtqzckmsynbcrxqbjyfaxpzzedzcjykbcjwhyjbqzzywnyszptdkzpfpbaztklqnhbbzptpptyzzybhn"
			+ "ydcpzmmcycqmcjfzzdcmnlfpbplngqjtbttajzpzbbdnjkljqylnbzqhksjznggqstzkcxchpzsnbcgzkddzqanzgjkdrtlzldwj"
			+ "njzlywtxndjzjhxnatncbgtzcsskmljpjytsnwxcfjwjjtkhtzplbhsnjssyjbhbjyzlstlsbjhdnwqpslmmfbjdwajyzccjtbnn"
			+ "nzwxxcdslqgdsdpdzgjtqqpsqlyyjzlgyhsdlctcbjtktyczjtqkbsjlgnnzdncsgpynjzjjyyknhrpwszxmtncszzyshbyhyzax"
			+ "ywkcjtllckjjtjhgcssxyqyczbynnlwqcglzgjgqyqcczssbcrbcskydznxjsqgxssjmecnstjtpbdlthzwxqwqczexnqczgwesg"
			+ "ssbybstscslccgbfsdqnzlccglllzghzcthcnmjgyzazcmsksstzmmzckbjygqljyjppldxrkzyxccsnhshhdznlzhzjjcddcbcj"
			+ "xlbfqbczztpqdnnxljcthqzjgylklszzpcjdscqjhjqkdxgpbajynnsmjtzdxlcjyryynhjbngzjkmjxltbsllrzpylssznxjhll"
			+ "hyllqqzqlsymrcncxsljmlzltzldwdjjllnzggqxppskyggggbfzbdkmwggcxmcgdxjmcjsdycabxjdlnbcddygskydqdxdjjyxh"
			+ "saqazdzfslqxxjnqzylblxxwxqqzbjzlfbblylwdsljhxjyzjwtdjcyfqzqzzdzsxzzqlzcdzfxhwspynpqzmlpplffxjjnzzyls"
			+ "jnyqzfpfzgsywjjjhrdjzzxtxxglghtdxcskyswmmtcwybazbjkshfhgcxmhfqhyxxyzftsjyzbxyxpzlchmzWccxhzzssyfdmncw"
			+ "dabazlxktcshhxkxjjzjsthygxsxyyhhhjwxkzxssbzzwhhhcwtzzzpjxsyxqqjgzyzawllcwxznxgyxyhfmkhydwsqmnjnaycys"
			+ "pmjkgwcqhylajgmzxhmmcnzhbhxclxdjpltxyjkdyylttxfqzhyxxsjbjnayrsmxyplckdnyhlxrlnllstycyyqygzhhsccsmcct"
			+ "zcxhyqfpyyrpbflfqnntszlljmhwtcjqyzwtlnmlmdwWcczzsnzrbpdddlqjjbxtcsnzqqygwcsxfwzlxccrszdzmcyggdyqsgtnn"
			+ "nlsmymmsyhfbjdgyxccpshxczcsbsjyygjmpbwaffyfnxhydxzylremzgzzyndsznlljcsqfnxxkptxzgxjjgbmyyssnbtylbnlh"
			+ "bfzdcyfbmgqrrmzszxysjtznnydzzcdgnjafjbdknzblczszpsgcycjszlmnrznbzzldlnllysxsqzqlcxzlsgkbrxbrbzcycxzj"
			+ "zeeyfgklzlnyhgzcgzlfjhgtgwkraajyzkzqtsshjjxdzyznynnzyrzdqqhgjzxsszbtkjbbfrtjxllfqwjgclqtyWcclpzdxtzag"
			+ "bdhzzrbgjhwnjtjxlkscfsmwlldcysjtxkzscfwjlbnntzlljzllqblcqmqqcgcdfpbphzczjlpyyghdtgwdxfczqyyyqysrclqz"
			+ "fklzzzgffcqnwglhjycjjczlqzzyjbjzzbpdcsnnjgxdqnknlznnnnpsntsdyfwwdjzjysxyyczcyhzwbbyhxrylybhkjksfxtjj"
			+ "mmchhlltnyymsxxyzpdjjycsycwmdjjkqyrhllngpngtlyycljnnnxjyzfnmlrgjjtyzbsyzmsjyjhgfzqmsyxrszcytlrtqzsst"
			+ "kxgqkgsptgxdnjsgcqcqhmxggztqydjjznlbznxqlhyqgggthqscbyhjhhkyygkggcmjdzllcclxqsftgjslllmlcskctbljszsz"
			+ "mmnytpzsxqhjcnnqnyexzqzcpshkzzyzxxdfgmwqrllqxrfztlystctmjcsjjthjnxtnrztzfqrhcgllgcnnnnjdnlnnytsjtlny"
			+ "xsszxcgjzyqpylfhdjsbbdczgjjjqzjqdybssllcmyttmqnbhjqmnygjyeqyqmzgcjkpdcnmyzgqllslnclmholzgdylfzslncnz"
			+ "lylzcjeshnyllnxnjxlyjyyyxnbcljsswcqqnnyllzldjnllzllbnylnqchxyyqoxccqkyjxxxyklksxeyqhcqkkkkcsnyxxyqxy"
			+ "gwtjohthxpxxhsnlcykychzzcbwqbbwjqcscszsslcylgddsjzmmymcytsdsxxscjpqqsqylyfzychdjynywcbtjsydchcyddjlb"
			+ "djjsodzyqyskkyxdhhgqjyohdyxwgmmmazdybbbppbcmnnpnjzsmtxerxjmhqdntpjdcbsnmssythjtslmltrcplzszmlqdsdmjm"
			+ "qpnqdxcfrnnfsdqqyxhyaykqyddlqyyysszbydslntfgtzqbzmchdhczcwfdxtmqqsphqwwxsrgjcwnntzcqmgwqjrjhtqjbbgwz"
			+ "fxjhnqfxxqywyyhyscdydhhqmrmtmwctbszppzzglmzfollcfwhmmsjzttdhlmyffytzzgzyskjjxqyjzqbhWcczclyghgfmshpcf"
			+ "zsnclpbqsnjyzslxxfpmtyjygbxlldlxpzjyzjyhhzcywhjylsjexfszzywxkzjlnadymlymqjpwxxhxsktqjezrpxxzghmhwqpw"
			+ "qlyjjqjjzszcnhjlchhnxjlqwzjhbmzyxbdhhypylhlhlgfwlcfyytlhjjcwmscpxstkpnhjxsntyxxtestjctlsslstdlllwwyh"
			+ "dnrjzsfgxssyczykwhtdhwjglhtzdqdjzxxqgghltzphcsqfclnjtclzpfstpdynylgmjllycqhynspchylhqyqtmzyWccywrfqyk"
			+ "jsyslzdnjmpxyyssrhzjnyqtqdfzbwwdwwrxcwggyhxmkmyyyhmxmzhnksepmlqqmtcwctmxmxjpjjhfxyyzsjzhtybmstsyjznq"
			+ "jnytlhynbyqclcycnzwsmylknjxlggnnpjgtysylymzskttwlgsmzsylmpwlcwxwqcssyzsyxyrhssntsrwpccpwcmhdhhxzdzyf"
			+ "jhgzttsbjhgyglzysmyclllxbtyxhbbzjkssdmalhhycfygmqypjyjqxjllljgclzgqlycjcctotyxmtmshllwlqfxymzmklpszz"
			+ "cxhkjyclctyjcyhxsgyxnnxlzwpyjpxhjwpjpwxqqxlxsdhmrslzzydwdtcxknstzshbsccstplwsscjchjlcgchssphylhfhhxj"
			+ "sxallnylmzdhzxylsxlmzykcldyahlcmddyspjtqjzlngjfsjshctsdszlblmssmnyymjqbjhrzwtyydchjljapzwbgqxbkfnbjd"
			+ "llllyylsjydwhxpsbcmljpscgbhxlqhyrljxyswxhhzlldfhlnnymjljyflyjycdrjlfsyzfsllcqyqfgqyhnszlylmdtdjcnhbz"
			+ "llnwlqxygyyhbmgdhxxnhlzzjzxczzzcyqzfngwpylcpkpykpmclgkdgxzgxwqbdxzzkzfbddlzxjtpjpttbythzzdwslcpnhslt"
			+ "jxxqlhyxxxywzyswttzkhlxzxzpyhgzhknfsyhntjrnxfjcpjztwhplshfcrhnslxxjxxyhzqdxqwnnhyhmjdbflkhcxcwhjfyjc"
			+ "fpqcxqxzyyyjygrpynscsnnnnchkzdyhflxxhjjbyzwttxnncyjjymswyxqrmhxzwfqsylznggbhyxnnbwttcsybhxxwxyhhxyxn"
			+ "knyxmlywrnnqlxbbcljsylfsytjzyhyzawlhorjmnsczjxxxyxchcyqryxqzddsjfslyltsffyxlmtyjmnnyyyxltzcsxqclhzxl"
			+ "wyxzhnnlrxkxjcdyhlbrlWccrdlaxksnlljlyxxlynrylcjtgncmtlzllcyzlpzpzyawnjjfybdyyzsepckzzqdqpbpsjpdyttbdb"
			+ "bbyndycncpjmtmlrmfmmrwyfbsjgygsmdqqqztxmkqwgxllpjgzbqrdjjjfpkjkcxbljmswldtsjxldlppbxcwkcqqbfqbccajzg"
			+ "mykbhyhhzykndqzybpjnspxthlfpnsygyjdbgxnhhjhzjhstrstldxskzysybmxjlxyslbzyslzxjhfybqnbylljqkygzmcyzzym"
			+ "ccslnlhzhwfwyxzmwyxtynxjhbyymcysbmhysmydyshnyzchmjjmzcaahcbjbbhblytylsxsnxgjdhkxxtxxnbhnmlngsltxmrhn"
			+ "lxqqxmzllyswqgdlbjhdcgjyqyymhwfmjybbbyjyjwjmdpwhxqldyapdfxxbcgjspckrssyzjmslbzzjfljjjlgxzgyxyxlszqkx"
			+ "bexyxhgcxbpndyhwectwwcjWcctxchxyqqllxflyxlljlssnwdbzcmyjclwswdczpchqekcqbwlcgydblqppqzqfnqdjhymmcxtxd"
			+ "rmzwrhxcjzylqxdyynhyyhrslnrsywwjjymtltllgtqcjzyabtckzcjyccqlysqxalmzynywlwdnzxqdllqshgpjfjljnjabcqzd"
			+ "jgthhsstnyjfbswzlxjxrhgldlzrlzqzgsllllzlymxxgdzhgbdphzpbrlwnjqbpfdwonnnhlypcnjccndWcccpbzzncyqxldomzb"
			+ "lzwpdwyygdstthcsqsccrsssyslfybnntyjszdfndpdhtqzWccqlxlcmyffgtjjqwftmnpjwdnlbzcmmcngbdzlqlpnfhyymjylsd"
			+ "chdcjwjcctljcldtljjcbddpndsszycndbjlggjzxsxnlycybjjxxcbylzcfzppgkcxqdzfztjjfjdjxzbnzyjqctyjwhdyczhym"
			+ "djxttmpxsplzcdwslshxypzgtfmlcjtacbbmgdewycyzxdszjyhflystygwhkjyylsjcxgywjcbllcsnddbtzbsclyzczzssqdll"
			+ "mjyyhfllqllxfdyhabxggnywyypllsdldllbjcyxjznlhljdxyyqytdlllbngpfdfbbqbzzmdpjhgclgmjjpgaehhbwcqxajhhhz"
			+ "chxyphjaxhlphjpgpzjqcqzgjjzzgzdmqyybzzphyhybwhazyjhykfgdpfqsdlzmljxjpgalxzdaglmdgxmmzqwtxdxxpfdmmssy"
			+ "mpfmdmmkxksyzyshdzkjsysmmzzzmdydyzzczxbmlstmdyemxckjmztyymzmzzmsshhdccjewxxkljsthwlsqlyjzllsjssdppmh"
			+ "nlgjczyhmxxhgncjmdhxtkgrmxfwmckmwkdcksxqmmmszzydkmsclcmpcjmhrpxqpzdsslcxkyxtwlkjyahzjgzjwcjnxyhmWccml"
			+ "gjxmhlmlgmxctkzmjlyscjsyszhsyjzjcdajzhbsdqjzgwtkqxfkdmsdjlfmnhkzqkjfeypzyszcdpynffmzqykttdzzefmzlbnp"
			+ "plplpbpszalltnlkckqzkgenjlwalkxydpxnhsxqnwqnkxqclhyxxmlnccwlymqyckynnlcjnszkpyzkcqzqljbdmdjhlasqlbyd"
			+ "wqlwdgbqcryddztjybkbwszdxdtnpjdtcnqnfxqqmgnseclstbhpwslctxxlpwydzklnqgzcqapllkqcylbqmqczqcnjslqzdjxl"
			+ "ddhpzqdljjxzqdjyzhhzlkcjqdwjppypqakjyrmpzbnmcxkllzllfqpylllWccsglzysslrsysqtmxyxzqzbscnysyztffmzzsmzq"
			+ "hzssccmlyxwtpzgxzjgzgsjzgkddhtqggzllbjdzlsbzhyxyzhzfywxytymsdnzzyjgtcmtnxqyxjscxhslnndlrytzlryylxqht"
			+ "xsrtzcgyxbnqqzfhykmzjbzymkbpnlyzpblmcnqyzzzsjztjctzhhyzzjrdyzhnfxklfzslkgjtctssyllgzrzbbjzzklpkbczys"
			+ "nnyxbjfbnjzzxcdwlzyjxzzdjjgggrsnjkmsmzjlsjywqsnyhqjsxpjztnlsnshrnynjtwchglbnrjlzxwjqxqkysjycztlqzybb"
			+ "ybyzjqdwgyzcytjcjxckcwdkkzxsnkdnywwyyjqyytlytdjlxwkcjnklccpzcqqdzzqlcsfqchqqgssmjzzllbjjzysjhtsjdysj"
			+ "qjpdszcdchjkjzzlpycgmzndjxbsjzzsyzyhgxcpbjydssxdzncglqWcctsfcbfdzdlznfgfjgfsmpnjqlnblgqcyyxbqgdjjqsrf"
			+ "kztjdhczklbsdzcfytplljgjhtxzcsszzxstjygkgckgynqxjplzbbbgcgyjzgczqszlbjlsjfzgkqqjcgycjbzqtldxrjnbsxxp"
			+ "zshszycfwdsjjhxmfczpfzhqhqmqnknlyhtycgfrzgnqxcgpdlbzcsczqlljblhbdcypscppdymzzxgyhckcpzjgslzlnscnsldl"
			+ "xbmsdlddfjmkdqdhslzxlsznpqpgjdlybdskgqlbzlnlkyyhzttmcjnqtzzfszqktlljtyyllnllqyzqlbdzlslyyzxmdfszsnxl"
			+ "xznczqnbbwskrfbcylctnblgjpmczzlstlxshtzcyzlzbnfmqnlxflcjlyljqcbclzjgnsstbrmhxzhjzclxfnbgxgtqncztmsfz"
			+ "kjmssncljkbhszjntnlzdntlmmjxgzjyjczxyhyhwrwwqnztnfjscpyshzjfyrdjsfscjzbjfzqzchzlxfxsbzqlzsgyftzdcszx"
			+ "zjbjpszkjrhxjzcgbjkhcggtxkjqglxbxfgtrtylxqxhdtsjxhjzjjcmzlcqsbtxwqgxtxxhxftsdkfjhzyjfjxnzldlllcqsqqz"
			+ "qwqxswqtwgwbzcgcllqzbclmqjtzgzyzxljfrmyzflxnsnxxjkxrmjdzdmmyxbsqbhgzmwfwygmjlzbyytgzyccdjyzxsngnyjyz"
			+ "nbgpzjcqsyxsxrtfyzgrhztxszzthcbfclsyxzlzqmzlmplmxzjssfsbysmzqhxxnxrxhqzzzsslyflczjrcrxhhzxqndshxsjjh"
			+ "qcjjbcynsysxjbqjpxzqplmlxzkyxlxcnlcycxxzzlxdlllmjyhzxhyjwkjrwyhcpsgnrzlfzwfzznsxgxflzsxzzzbfcsyjdbrj"
			+ "krdhhjxjljjtgxjxxstjtjxlyxqfcsgswmsbctlqzzwlzzkxjmltmjyhsddbxgzhdlbmyjfrzfcgclyjbpmlysmsxlszjqqhjzfx"
			+ "gfqfqbphngyyqxgztnqwyltlgwgwwhnlfmfgzjmgmgbgtjflyzzgzyzaflsspmlbflcwbjztljjmzlpjjlymqtmyyyfbgygqzgly"
			+ "zdxqyxrqqqhsxyyqxygjtyxfsfsllgnqcygycwfhcccfxpylypllzqxxxxxqqhhsshjzcftsczjxspzwhhhhhapylqnlpqafyhxd"
			+ "ylnkmzqgggddesrenzltzgchyppcsqjjhclljtolnjpzljlhymhezdydsqycddhgznndzclzywllznteydgnlhslpjjbdgwxpcnn"
			+ "tycklkclwkllcasstknzdnnjttlyyzssysszzryljqkcgdhhyrxrzydgrgcwcgzqffbppjfzynakrgywyjpqxxfkjtszzxswzddf"
			+ "bbqtbgtzkznpzfpzxzpjszbmqhkyyxyldkljnypkyghgdzjxxeaxpnznctzcmxcxmmjxnkszqnmnlwbwwqjjyhclstmcsxnjcxxt"
			+ "pcnfdtnnpglllzcjlspblpgjcdtnjjlyarscffjfqwdpgzdwmrzzcgodaxnssnyzrestyjwjyjdbcfxnmwttbqlwstszgybljpxg"
			+ "lbnclgpcbjftmxzljylzxcltpnclcgxtfzjshcrxsfysgdkntlbyjcyjllstgqcbxnhzxbxklylhzlqzlnzcqwgzlgzjncjgcmnz"
			+ "zgjdzxtzjxycyycxxjyyxjjxsssjstsstdppghtcsxwzdcsynptfbchfbblzjclzzdbxgcjlhpxnfzflsyltnwbmnjhszbmdnbcy"
			+ "sccldnycndqlyjjhmqllcsgljjsyfpyyccyltjantjjpwycmmgqyysxdxqmzhszxbftwwzqswqrfkjlzjqqyfbrxjhhfwjgzyqac"
			+ "myfrhcyybynwlpexcczsyyrlttdmqlrkmpbgmyyjprkznbbsqyxbhyzdjdnghpmfsgbwfzmfqmWcczmzdcgjlnnnxyqgmlrygqccy"
			+ "xzlwdkcjcggmcjjfyzzjhycfrrcmtznzxhkqgdjxccjeascrjthpljlrzdjrbcqhjdnrhylyqjsymhzydwcdfryhbbydtssccwbx"
			+ "glpzmlzjdqsscfjmmxjcxjytycghycjwynsxlfemwjnmkllswtxhyyyncmmcyjdqdjzglljwjnkhpzggflccsczmcbltbhbqjxqd"
			+ "jpdjztghglfjawbzyzjltstdhjhctcbchflqmpwdshyytqwcnntjtlnnmnndyyyxsqkxwyyflxxnzwcxypmaelyhgjwzzjbrxxaq"
			+ "jfllpfhhhytzzxsgqjmhspgdzqwbwpjhzjdyjcqwxkthxsqlzyymysdzgnqckknjlwpnsyscsyzlnmhqsyljxbcxtlhzqzpcycyk"
			+ "pppnsxfyzjjrcemhszmnxlxglrwgcstlrsxbygbzgnxcnlnjlclynymdxwtzpalcxpqjcjwtcyyjlblxbzlqmyljbghdslssdmxm"
			+ "bdczsxyhamlczcpjmcnhjyjnsykchskqmczqdllkablwjqsfmocdxjrrlyqchjmybyqlrhetfjzfrfksryxfjdwtsxxywsqjysly"
			+ "xwjhsdlxyyxhbhawhwjcxlmyljcsqlkydttxbzslfdxgxsjkhsxxybssxdpwncmrptqzczenygcxqfjxkjbdmljzmqqxnoxslyxx"
			+ "lylljdzptymhbfsttqqwlhsgynlzzalzxclhtwrrqhlstmypyxjjxmnsjnnbryxyjllyqyltwylqyfmlkljdnlltfzwkzhljmlhl"
			+ "jnljnnlqxylWcchhlnlzxqchxcfxxlhyhjjgbyzzkbxscqdjqdsndzsygzhhmgsxcsymxfepcqwwrbpyyjqryqcyjhqqzyhmwffhg"
			+ "zfrjfcdbxntqyzpcyhhjlfrzgpbxzdbbgrqstlgdgylcqmgchhmfywlzyxkjlypjhsywmqqggzmnzjnsqxlqsyjtcbehsxfszfxz"
			+ "wfllbcyyjdytdthwzsfjmqqyjlmqsxlldttkghybfpwdyysqqrnqwlgwdebzwcyygcnlkjxtmxmyjsxhybrwfymwfrxyymxysctz"
			+ "ztfykmldhqdlgyjnlcryjtlpsxxxywlsbrrjwxhqybhtydnhhxmmywytycnnmnssccdalwztcpqpyjllqzyjswjwzzmmglmxclmx"
			+ "nzmxmzsqtzppjqblpgxjzhfljjhycjsrxwcxsncdlxsyjdcqzxslqyclzxlzzxmxqrjmhrhzjbhmfljlmlclqnldxzlllfyprgjy"
			+ "nxcqqdcmqjzzxhnpnxzmemmsxykynlxsxtljxyhwdcwdzhqyybgybcyscfgfsjnzdrzzxqxrzrqjjymcanhrjtldbpyzbstjhxxz"
			+ "ypbdwfgzzrpymnnkxcqbyxnbnfyckrjjcmjegrzgyclnnzdnkknsjkcljspgyyclqqjybzssqlllkjftbgtylcccdblsppfylgyd"
			+ "tzjqjzgkntsfcxbdkdxxhybbfytyhbclnnytgdhryrnjsbtcsnyjqhklllzslydxxwbcjqsbxnpjzjzjdzfbxxbrmladhcsnclbj"
			+ "dstblprznswsbxbcllxxlzdnzsjpynyxxyftnnfbhjjjgbygjpmmmmsszljmtlyzjxswxtyledqpjmpgqzjgdjlqjwjqllsdgjgy"
			+ "gmscljjxdtygjqjjjcjzcjgdzdshqgzjggcjhqxsnjlzzbxhsgzxcxyljxyxyydfqqjhjfxdhctxjyrxysqtjxyefyyssyxjxncy"
			+ "zxfxcsxszxyyschshxzzzgzzzgfjdldylnpzgsjaztyqzpbxcbdztzczyxxyhhscjshcggqhjhgxhsctmzmehyxgebtclzkkwytj"
			+ "zrslekestdbcyhqqsayxcjxwwgsphjszsdncsjkqcxswxfctynydpccczjqtcwjqjzzzqzljzhlsbhpydxpsxshhezdxfptjqyzc"
			+ "xhyaxncfzyyhxgnqmywntzsjbnhhgymxmxqcnssbcqsjyxxtyyhybcqlmmszmjzzllcogxzaajzyhjmchhcxzsxsdznleyjjzjbh"
			+ "zwjzsqtzpsxzzdsqjjjlnyazphhyysrnqzthzhnyjyjhdzxzlswclybzyecwcycrylchzhzydzydyjdfrjjhtrsqtxyxjrjhojyn"
			+ "xelxsfsfjzghpzsxzszdzcqzbyyklsgsjhczshdgqgxyzgxchxzjwyqwgyhksseqzzndzfkwyssdclzstsymcdhjxxyweyxczayd"
			+ "mpxmdsxybsqmjmzjmtjqlpjyqzcgqhyjhhhqxhlhdldjqcfdwbsxfzzyyschtytyjbhecxhjkgqfxbhyzjfxhwhbdzfyzbchpnpg"
			+ "dydmsxhkhhmamlnbyjtmpxejmcthqbzyfcgtyhwphftgzzezsbzegpbmdskftycmhbllhgpzjxzjgzjyxzsbbqsczzlzscstpgxm"
			+ "jsfdcczjzdjxsybzlfcjsazfgszlwbczzzbyztzynswyjgxzbdsynxlgzbzfygczxbzhzftpbgzgejbstgkdmfhyzzjhzllzzgjq"
			+ "zlsfdjsscbzgpdlfzfzszyzyzsygcxsnxxchczxtzzljfzgqsqqxcjqccccdjcdszzyqjccgxztdlgscxzsyjjqtcclqdqztqchq"
			+ "qyzynzzzpbkhdjfcjfztypqyqttynlWccdktjcpqzjdzfpjsbnjlgyjdxjdcqkzgqkxclbzjtcjdqbxdjjjstcxnxbxqmslyjcxnt"
			+ "jqwwcjjnjjlllhjcwqtbzqqczczpzzdzyddcyzdzccjgtjfzdprntctjdcxtqzdtjnplzbcllctdsxkjzqdmzlbznbtjdcxfczdb"
			+ "czjjltqqpldckztbbzjcqdcjwynllzlzccdwllxwzlxrxntqjczxkjlsgdnqtddglnlajjtnnynkqlldzntdnycygjwyxdxfrsqs"
			+ "tcdenqmrrqzhhqhdldazfkapbggpzrebzzykyqspeqjjglkqzzzjlysyhyzwfqznlzzlzhwcgkypqgnpgblplrrjyxcccgyhsfzf"
			+ "wbzywtgzxyljczwhncjzplfflgskhyjdeyxhlpllllcygxdrzelrhgklzzyhzlyqszzjzqljzflnbhgwlczcfjwspyxzlzlxgccp"
			+ "zbllcxbbbbnbbcbbcrnnzccnrbbnnldcgqyyqxygmqzwnzytyjhyfwtehznjywlccntzyjjcdedpwdztstnjhtyWccjnyjzlxtsst"
			+ "phndjxxbyxqtzqddtjtdyztgwscszqflshlnzbcjbhdlyzjyckwtydylbnydsdsycctyszyyebgexhqddwnygyclxtdcystqnygz"
			+ "ascsszzdzlcclzrqxyywljsbymxshzdeWccbllyyllytdqyshymrqnkfkbfxnnsbychxbwjyhtqbpbsbwdzylkgzskyghqzjxhxjx"
			+ "gnljkzlyycdxlfwfghljgjybxblybxqpqgntzplncybxdjyqydymrbeyjyyhkxxstmxrczzjwxyhybmcflyzhqyzfwxdbxbcwzms"
			+ "lpdmyckfmzklzcyqycclhxfzlydqzpzygyjyzmdxtzfnnyttqtzhgsfcdmlccytzxjcytjmkslpzhysnwllytpzctzccktxdhxxt"
			+ "qcyfksmqccyyazhtjplylzlyjbjxtfnyljyynrxcylmmnxjsmybcsysslzylljjgyldzdlqhfzzblfndsqkczfyhhgqmjdsxyctt"
			+ "xnqnjpyybfcjtyyfbnxejdgyqbjrcnfyyqpghyjsyzngrhtknlnndzntsmgklbygbpyszbydjzsstjztsxzbhbscsbzczptqfzlq"
			+ "flypybbjgszmnxdjmtsyskkbjtxhjcegbsmjyjzcstmljyxrczqscxxqpyzhmkyxxxjcljyrmyygadyskqlnadhrskqxzxztcggz"
			+ "dlmlwxybwsyctbhjhcfcwzsxwwtgzlxqshnyczjxemplsrcgltnzntlzjcyjgdtclglbllqpjmzpapxyzlaktkdwczzbncctdqqz"
			+ "qyjgmcdxltgcszlmlhbglkznnwzndxnhlnmkydlgxdtwcfrjerctzhydxykxhwfzcqshknmqqhzhhymjdjskhxzjzbzzxympajnm"
			+ "ctbxlsxlzynwrtsqgscbptbsgzwyhtlkssswhzzlyytnxjgmjrnsnnnnlskztxgxlsammlbwldqhylakqcqctmycfjbslxclzjcl"
			+ "xxknbnnzlhjphqplsxsckslnhpsfqcytxjjzljldtzjjzdlydjntptnndskjfsljhylzqqzlbthydgdjfdbyadxdzhzjnthqbykn"
			+ "xjjqczmlljzkspldsclbblnnlelxjlbjycxjxgcnlcqplzlznjtsljgyzdzpltqcssfdmnycxgbtjdcznbgbqyqjwgkfhtnbyqzq"
			+ "gbkpbbyzmtjdytblsqWccsxtbnpdxklemyycjynzdtldykzzxtdxhqshygmzsjycctayrzlpwltlkxslzcggexclfxlkjrtlqjaqz"
			+ "ncWccqdkkcxglczjzxjhptdjjmzqykqsecqzdshhadmlzfmmzbgntjnnlhbyjbrbtmlbyjdzxlcjlpldlpcqdhlhzlycblcxccjad"
			+ "qlmzmmsshmybhbnkkbhrsxxjmxmdznnpklbbrhgghfchgmnklltsyyycqlcskymyehywxnxqywbawykqldnntndkhqcgdqktgpkx"
			+ "hcpdhtwnmssyhbwcrwxhjmkmzngwtmlkfghkjyldyycxwhyyclqhkqhtdqkhffldxqwytyydesbpkyrzpjfyyzjceqdzzdlattpb"
			+ "fjllcxdlmjsdxegwgsjqxcfbssszpdyzcxznyxppzydlyjccpltxlnxyzyrscyyytylwwndsahjsygyhgywwaxtjzdaxysrltdps"
			+ "syxfnejdxyzhlxlllzhzsjnyqyqyxyjghzgjcyjchzlycdshhsgczyjscllnxzjjyyxnfsmwfpyllyllabmddhwzxjmcxztzpmlq"
			+ "chsfwzynctlndywlslxhymmylWccwwkyxyaddxylldjpybpwnxjmmmllhafdllaflbnhhbqqjqzjcqjjdjtffkmmmpythygdrjrdd"
			+ "wrqjxnbysrmzdbyytbjhpymyjtjxaahggdqtmystqxkbtzbkjlxrbyqqhxmjjbdjntgtbxpgbktlgqxjjjcdhxqdwjlwrfmjgwqh"
			+ "cnrxswgbtgygbwhswdwrfhwytjjxxxjyzyslphyypyyxhydqpxshxyxgskqhywbdddpplcjlhqeewjgsyykdpplfjthkjltcyjhh"
			+ "jttpltzzcdlyhqkcjqysteeyhkyzyxxyysddjkllpymqyhqgxqhzrhbxpllnqydqhxsxxwgdqbshyllpjjjthyjkyphthyyktyez"
			+ "yenmdshlzrpqfbnfxzbsftlgxsjbswyysksflxlpplbbblnsfbfyzbsjssylpbbffffsscjdstjsxtryjcyffsyzyzbjtlctsbsd"
			+ "hrtjjbytcxyyeylycbnebjdsysyhgsjzbxbytfzwgenhhhthjhhxfwgcstbgxklstyymtWccyxjskzscdyjrcythxzfhmymcxlzns"
			+ "djtxtxrycfyjsbsdyerxhljxbbdeynjghxgckgscyWcclxjmsznskgxfbnbbthfjyafxwxfbxmyfhdttcxzzpxrsywzdlybbktyqw"
			+ "qjbzypzjznjpzjlztfysbttslmptzrtdxqsjehbnylndxljsqmlhtxtjecxalzzspktlzkqqyfsyjywpcpqfhjhytqxzkrsgtksq"
			+ "czlptxcdyyzsslzslxlzmacpcqbzyxhbsxlzdltztjtylzjyytbzypltxjsjxhlbmytxcqrblzssfjzztnjytxmyjhlhpblcyxqj"
			+ "qqkzzscpzkswalqsplczzjsxgwwwygyatjbbctdkhqhkgtgpbkqyslbxbbckbmllndzstbklggqkqlzbkktfxrmdkbftpzfrtppm"
			+ "ferqnxgjpzsstlbztpszqzsjdhljqlzbpmsmmsxlqqnhknblrddnhxdkddjcyyljfqgzlgsygmjqjkhbpmxyxlytqwlwjcpbmjxc"
			+ "yzydrjbhtdjyeqshtmgsfyplwhlzffnynnhxqhpltbqpfbjwjdbygpnxtbfzjgnnntjshxeawtzylltyqbwjpgxghnnkndjtmszs"
			+ "qynzggnwqtfhclssgmnnnnynzqqxncjdqgzdlfnykljcjllzlmzznnnnsshthxjlzjbbhqjwwycrdhlyqqjbeyfsjhthnrnwjhwp"
			+ "slmssgzttygrqqwrnlalhmjtqjsmxqbjjzjqzyzkxbjqxbjxshzssfglxmxnxfghkzszggslcnnarjxhnlllmzxelglxydjytlfb"
			+ "kbpnlyzfbbhptgjkwetzhkjjxzxxglljlstgshjjyqlqzfkcgnndjsszfdbctwwseqfhqjbsaqtgypjlbxbmmywxgslzhglsgnyf"
			+ "ljbyfdjfngsfWccyzhqffwjsyfyjjphzbyyzffwotjnlmftwlbzgyzqxcdjygzyyryzynyzwegazyhjjlzrthlrmgrjxzclnnnljj"
			+ "yhtbwjybxxbxjjtjteekhwslnnlbsfazpqqbdlqjjtyyqlyzkdksqjnejzldqcgjqnnjsncmrfqthtejmfctyhypymhydmjncfgy"
			+ "yxwshctxrljgjzhzcyyyjltkttntmjlzclzzayyoczlrlbszywjytsjyhbyshfjlykjxxtmzyyltxxypslqyjzyzyypnhmymdyyl"
			+ "blhlsyygqllnjjymsoycbzgdlyxylcqyxtszegxhzglhwbljheyxtwqmakbpqcgyshhegqcmwyywljyjhyyzlljjylhzyhmgsljl"
			+ "jxcjjyclycjbcpzjzjmmwlcjlnqljjjlxyjmlszljqlycmmgcfmmfpqqmfxlqmcffqmmmmhnznfhhjgtthxkhslnchhyqzxtmmqd"
			+ "cydyxyqmyqylddcyaytazdcymdydlzfffmmycqcwzzmabtbyctdmndzggdftypcgqyttssffwbdttqssystwnjhjytsxxylbyyhh"
			+ "whxgzxwznnqzjzjjqjccchykxbzszcnjtllcqxynjnckycynccqnxyewyczdcjycchyjlbtzyycqwlpgpyllgktltlgkgqbgychj"
			+ "xy";
	private final static int HANZI_START = 19968;
	private final static int HANZI_COUNT = 20902;

	public static char getFirstLetter(String str) {
		if (str == null || str.length() == 0)
			return '#';

		char firstCh = str.charAt(0);
		int index = firstCh - HANZI_START;
		if (index >= 0 && index <= HANZI_COUNT) {
			return firstLetterArray.charAt(index);
		} else {
			if (firstCh >= 65 && firstCh <= 90)
				return (char) (firstCh + 32);
			if (firstCh >= 97 && firstCh <= 122)
				return firstCh;
			return '#';
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	public static String encodeValue(String value) {
		try {
			String newValue = Base64.encodeToString(value.getBytes("UTF-8"), Base64.NO_WRAP | Base64.URL_SAFE)
					.replace('=', '*');
			int len = newValue.length();
			int half = len / 2;
			if (len % 2 == 0) {
				return newValue.substring(half, len) + newValue.substring(0, half);
			} else {
				return newValue.substring(half + 1, len) + newValue.charAt(half) + newValue.substring(0, half);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String encodeBase64String(String value) {
		try {
			String newValue = Base64.encodeToString(value.getBytes("UTF-8"), Base64.NO_WRAP | Base64.URL_SAFE);
			return newValue;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 
	 * @return 0~7
	 */
	public static int getRandomOct() {
		return (int) (Math.random() * 10) % 8;
	}

	/**
	 * List 数据分页
	 * 
	 * @param list
	 * @param pageSize
	 * @return List<?>[]
	 */
	public static List<?>[] splitList(List<?> list, int pageSize) {
		if (list == null)
			return null;
		int total = list.size();
		// 总页数
		int pageCount = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
		List<?>[] result = new List[pageCount];
		for (int i = 0; i < pageCount; i++) {
			int start = i * pageSize;
			// 最后一条可能超出总数
			int end = (start + pageSize) > total ? total : (start + pageSize);
			List<?> subList = list.subList(start, end);
			result[i] = subList;
		}
		return result;
	}

	/**
	 * 
	 * @param num
	 *            :int
	 * @return String
	 */
	public static String StringFormat(int num) {
		String result = "";
		if (num < 10000) {
			result = "" + num;
		} else if (num < 100000) {
			result = result.format("%1$,.01f万", (double) num / (double) 10000);
		} else if (num < 100000000) {
			result = result.format("%1$,d万", num / 10000);
		} else {
			result = result.format("%1$,.03f亿", (double) num / (double) 100000000);
		}
		return result;
	}

	/**
	 * 
	 * @param src
	 * @param value
	 *            替换的值
	 * @param color
	 *            替换值的颜色 可为null
	 * @return
	 */
	public static String StringFormat(String src, String value, String color) {
		if (!ValidatorUtil.isEffective(src))
			return "";
		if (!ValidatorUtil.isEffective(value))
			value = "";
		if (ValidatorUtil.isEffective(color))
			value = "<font color='" + color + "'><b>" + value + "</b></font>";
		return String.format(src, value);
	}

//	public static String MakeJsonArray(MapCache map) {
//		String result = "";
//
//		return result;
//	}
}
