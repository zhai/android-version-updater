package com.zhaisoft.lib.updater.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class ValidatorUtil {

    public static boolean isEmail(String emailString) {
        String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        return match(regex, emailString);
    }


    public static boolean isIP(String ipString) {
        String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
        String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num
                + "$";
        return match(regex, ipString);
    }


    public static boolean IsUrl(String url_string) {

        String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
        String Ip4 = "(" + num + "\\." + num + "\\." + num + "\\." + num + ")";
        String regex = "(?i)http(?-i)([sS])?[:��]//"
                + "(([0-9A-Za-z]([\\w-]*\\.)+[A-Za-z]+)|" + Ip4 + ")"
                + "(?::(\\d{1,5}))?(/[\\w- ./?!%&=#:~|]*)?";
        return match(regex, url_string);
    }


    public static boolean IsUrl2(String str) {

        String regex = "(?i)http(?-i)([sS])?[:��]//" + "([\\w-]+\\.)+[\\w-]+"
                + "(?::(\\d{1,5}))?([\\w- ./?!%&=#$_:~|'@;,*+()\\[\\]]*)?";
        return match(regex, str);
    }


    public static boolean IsTelephone(String str) {
        String regex = "^(\\+)?(\\d{2,4}-)?(\\d{2,4}-)?\\d{4,13}(-\\d{2,4})?$";
        return match(regex, str);
    }


    public static boolean IsPassword(String str) {
        String regex = "[A-Za-z]+[0-9]";
        return match(regex, str);
    }

    public static boolean IsPasswLength(String str) {
        String regex = "^\\d{6,18}$";
        return match(regex, str);
    }

    public static boolean IsPostalcode(String str) {
        String regex = "^\\d{6}$";
        return match(regex, str);
    }


    public static boolean IsHandset(String str) {
        String regex = "^1[3,4,5,7,8]\\d{9}$";
        return match(regex, str);
    }


    public static boolean IsIDcard(String str) {
        String regex = "(^\\d{18}$)|(^\\d{15}$)";
        return match(regex, str);
    }


    public static boolean IsDecimal(String str) {
        String regex = "^[0-9]+(.[0-9]{2})?$";
        return match(regex, str);
    }

    public static boolean IsMonth(String str) {
        String regex = "^(0?[[1-9]|1[0-2])$";
        return match(regex, str);
    }


    public static boolean IsDay(String str) {
        String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
        return match(regex, str);
    }


    public static boolean isDate(String str) {
        // �ϸ���֤ʱ���ʽ��(ƥ��[2002-01-31], [1997-04-30],
        // [2004-01-01])��ƥ��([2002-01-32], [2003-02-29], [04-01-01])
        // String regex =
        // "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((01,3-9])|(1[0-2]))-(29|30)))))$";
        // û��ʱ����֤��YYYY-MM-DD
        // String regex =
        // "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";
        // ����ʱ����֤��YYYY-MM-DD 00:00:00
        String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
        return match(regex, str);
    }


    public static boolean isNumber(String str) {
        if (str == null) return false;
        String regex = "-?[0-9]*$";
        return match(regex, str);
    }


    public static boolean IsIntNumber(String str) {
        String regex = "^\\+?[1-9][0-9]*$";
        return match(regex, str);
    }


    public static boolean IsUpChar(String str) {
        String regex = "^[A-Z]+$";
        return match(regex, str);
    }

    public static boolean IsLowChar(String str) {
        String regex = "^[a-z]+$";
        return match(regex, str);
    }


    public static boolean IsAllowedLetter(String str) {
        String regex = "^[0-9A-Za-z\u4e00-\u9fa5\\(\\)_ .&-]+$";
        return match(regex, str);
    }


    public static boolean IsLetter(String str) {
        String regex = "^[A-Za-z]+$";
        return match(regex, str);
    }

    public static boolean IsLetterOrNuWccer(String str) {
        String regex = "^[0-9A-Za-z]";
        return match(regex, str);
    }

    public static boolean IsLettersOrNuWccers(String str) {
        if (str == null) return false;
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            String c = str.charAt(i) + "";
            if (!IsLetterOrNuWccer(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsChinese(String str) {
        String regex = "^[\u4e00-\u9fa5]+";
        return match(regex, str);
    }


    public static boolean isLatitude(String string, boolean acceptZero) {
        try {
            double lat = Double.parseDouble(string);
            if (lat > 90 || lat < -90) return false;
            if (lat == 0 && acceptZero == false) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean isLongitude(String string, boolean acceptZero) {
        try {
            double lat = Double.parseDouble(string);
            if (lat > 180 || lat < -180) return false;
            if (lat == 0 && acceptZero == false) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean IsContainJson(String str) {
        String base = "(\\{[^\\{\\}]*\\})";
        String wrap1 = "(\\{([^\\{\\}]*" + base + "*[^\\{\\}]*)+\\})";
        String wrap2 = "(\\{([^\\{\\}]*" + wrap1 + "*[^\\{\\}]*)+\\})";
        String wrap3 = "(\\{([^\\{\\}]*" + wrap2 + "*[^\\{\\}]*)+\\})";

        String regex = "[^\\{\\}]*" + wrap3 + "+[^\\{\\}]*$";

        return match(regex, str);
    }

    private static boolean match(String regex, String str) {
        if (str == null)
            return false;
        if (regex == null)
            return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    public static boolean isEffective(String string) {
        if ((string == null) || ("".equals(string)) || (" ".equals(string))
                || ("null".equals(string)) || ("\n".equals(string)))
            return false;
        else
            return true;
    }


    public static boolean isUtf8Data(byte[] b, int index, int type) {
        int lLen = b.length, lCharCount = 0;
        for (int i = index; i < lLen && lCharCount < type; ++lCharCount) {
            byte lByte = b[i++];
            if (lByte >= 0)
                continue;
            if (lByte < (byte) 0xc0 || lByte > (byte) 0xfd)
                return false;
            int lCount = lByte > (byte) 0xfc ? 5 : lByte > (byte) 0xf8 ? 4 : lByte > (byte) 0xf0 ? 3 : lByte > (byte) 0xe0 ? 2 : 1;
            if (i + lCount > lLen)
                return false;
            for (int j = 0; j < lCount; ++j, ++i)
                if (b[i] >= (byte) 0xc0)
                    return false;
        }
        return true;

    }
}