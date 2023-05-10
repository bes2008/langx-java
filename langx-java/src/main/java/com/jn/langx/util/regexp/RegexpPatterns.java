package com.jn.langx.util.regexp;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;


public class RegexpPatterns {
    private RegexpPatterns() {
    }


    /**
     * ref: https://unicode-table.com/en/blocks/
     * <p>
     * \u2E80 - \u2EFF 偏旁部首
     * \u2F00 - \u2FDF 康熙字典
     * \u3000 - \u303F 标点符号
     * \u31C0 - \u31EF 笔顺
     * \u3400 - \u4DBF 象形文字
     * \u4E00 - \u9FFF 常用文字
     * \uF900 - \uFAFF 兼容象形文字
     * \uFE30 - \uFE4F 纵向标点符号
     * \uFF01 - \uFF1F 标点符号
     */

    // "[\\x{4E00}-\\x{9FFF}\\x{2E80}-\\x{2EFF}\\x{2F00}-\\x{2FDF}\\x{3000}-\\x{303F}\\x{31C0}-\\x{31EF}\\x{3400}-\\x{4DBF}\\x{F900}-\\x{FAFF}\\x{FE30}-\\x{FE4F}\\x{FF01}-\\x{FF1F}\\x{20000}-\\x{2A6DF}\\x{2A700}-\\x{2EBEF}\\x{2F800}—\\x{2FA1F}\\x{30000}—\\x{3134F}]";
    public static final String CHINESE_CHAR_PATTERN = "[\\x{4E00}-\\x{9FFF}\\x{2E80}-\\x{2EFF}\\x{2F00}-\\x{2FDF}\\x{3000}-\\x{303F}\\x{31C0}-\\x{31EF}\\x{3400}-\\x{4DBF}\\x{F900}-\\x{FAFF}\\x{FE30}-\\x{FE4F}\\x{FF01}-\\x{FF1F}\\x{20000}-\\x{2A6DF}\\x{2A700}-\\x{2EBEF}\\x{2F800}—\\x{2FA1F}\\x{30000}—\\x{3134F}]";
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
    public static final Regexp PATTERN_ZIP_CODE = Regexps.createRegexp("[1-9]\\d{5}(?!\\d)");

    /**
     * 手机号
     */
    public static final Regexp PATTERN_MOBILE = Regexps.createRegexp("(?:0|86|\\+86)?1[3-9]\\d{9}");

    /**
     * 18位身份证号码
     */
    public static final Regexp PATTERN_IDENTITY = Regexps.createRegexp("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)");

    /**
     * 中国车牌号码（兼容新能源车牌）
     */
    public static final Regexp PATTERN_PLATE_NUMBER = Regexps.createRegexp(
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
    public static final Regexp PATTERN_EMAIL = Regexps.createRegexp("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", Option.CASE_INSENSITIVE);

    /**
     * IP地址
     * 192.168.0.3
     */
    public static final Regexp PATTERN_IPv4 = Regexps.createRegexp("((2(5[0-5]|[0-4]\\d))|([0-1]?\\d{1,2}))(\\.((2(5[0-5]|[0-4]\\d))|([0-1]?\\d{1,2}))){3}(/\\d{1,2})?");
    public static final Regexp PATTERN_IP = PATTERN_IPv4;
    public static final Regexp PATTERN_IPv6 = Regexps.createRegexp("(([0-9A-Fa-f]{0,4}::[0-9A-Fa-f]{0,4})|([0-9A-Fa-f]{1,4}:([0-9A-Fa-f]{1,4}:){0,5}:[0-9A-Fa-f]{1,4})|([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){7})|((((0{1,4}:){5})|(::))FFFF:(\\d{1,3}\\.){3}\\d{1,3}))(/\\d{1,6})?");

    /**
     * URL 地址
     * https://urlregex.com/
     */
    public static final Regexp PATTERN_URL = Regexps.createRegexp("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    /**
     * QQ 号
     */
    public static final Regexp PATTERN_QQ = Regexps.createRegexp("[1-9]\\d{4,9}");

    public static boolean test(String pattern, String string) {
        return test(Regexps.createRegexp(pattern), string);
    }

    /**
     * 判断字符串是否与pattern
     */
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

    /**
     * 判断是否存在匹配的部分
     *
     * @since 4.6.13
     */
    public static boolean contains(@Nullable String text, @Nullable Regexp pattern) {
        if (pattern == null) {
            if (Emptys.isEmpty(text)) {
                return true;
            }
            return false;
        }
        if (Emptys.isEmpty(text)) {
            return false;
        }
        return pattern.matcher(text).find();
    }

    public static final Regexp PATTERN_LAMBDA_CLASS = Regexps.createRegexp(".*\\$\\$Lambda\\$[0-9]+/.*");
}
