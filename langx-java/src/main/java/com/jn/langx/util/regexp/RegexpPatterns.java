package com.jn.langx.util.regexp;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;


public class RegexpPatterns {
    private RegexpPatterns() {
    }

    public static final String CHINESE_CHAR_PATTERN = "[\\u4e00-\\u9fa5]";
    public static final Regexp CHINESE_CHAR = Regexps.createRegexp(CHINESE_CHAR_PATTERN);
    public static final Regexp CHINESE_CHARS = Regexps.createRegexp(CHINESE_CHAR_PATTERN + "+");

    public static final Regexp COMMA_SPLIT_PATTERN = Regexps.createRegexp("\\s*[,]+\\s*");

    /**
     * 16进制字符串
     */
    public static final Regexp PATTERN_HEX = Regexps.createRegexp("^[a-f0-9]+$", Option.CASE_INSENSITIVE);

    /**
     * 中国邮编
     */
    public final static Regexp PATTERN_ZIP_CODE = Regexps.createRegexp("[1-9]\\d{5}(?!\\d)");

    /**
     * 手机号
     */
    public final static Regexp PATTERN_MOBILE = Regexps.createRegexp("(?:0|86|\\+86)?1[3-9]\\d{9}");

    /**
     * 18位身份证号码
     */
    public final static Regexp PATTERN_IDENTITY = Regexps.createRegexp("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)");

    /**
     * 中国车牌号码（兼容新能源车牌）
     */
    public final static Regexp PATTERN_PLATE_NUMBER = Regexps.createRegexp(
            "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|" +
                    "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领])|" +
                    "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$");

    /**
     * 社会统一信用代码
     * <pre>
     * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
     * 第二部分：机构类别代码1位 (数字或大写英文字母)
     * 第三部分：登记管理机关行政区划码6位 (数字)
     * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
     * 第五部分：校验码1位 (数字或大写英文字母)
     * </pre>
     */
    public static final Regexp PATTERN_SOCIAL_CREDIT_CODE = Regexps.createRegexp("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");

    /**
     * 邮件
     * http://emailregex.com/
     */
    public final static Regexp PATTERN_EMAIL = Regexps.createRegexp("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", Option.CASE_INSENSITIVE);

    /**
     * IP地址
     * https://ipregex.com/
     */
    public static Regexp PATTERN_IP = Regexps.createRegexp("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");

    /**
     * URL 地址
     * https://urlregex.com/
     */
    public static Regexp PATTERN_URL = Regexps.createRegexp("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    /**
     * QQ 号
     */
    public static Regexp PATTERN_QQ = Regexps.createRegexp("[1-9]\\d{4,9}");

    public static boolean test(String pattern, String string) {
        return test(Regexps.createRegexp(pattern), string);
    }

    public static boolean test(@Nullable Regexp pattern, @Nullable String string) {
        if (pattern == null) {
            if (Emptys.isEmpty(string)) {
                return true;
            }
            return false;
        }
        if (Emptys.isEmpty(string)) {
            return false;
        }
        return pattern.matcher(string).matches();
    }

}
