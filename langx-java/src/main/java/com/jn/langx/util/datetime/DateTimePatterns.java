package com.jn.langx.util.datetime;

/**
 * 常用格式
 *
 * @since 4.5.2
 */
public class DateTimePatterns {

    // ==================================yyyy-MM-dd相关Pattern==================================

    /**
     * yyyy-MM-dd <pre>  2020-05-23</pre>
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * yyyy-M-d 不补0 <pre>  2020-5-23</pre>
     */
    public static final String YYYY_M_D = "yyyy-M-d";

    /**
     * yyyyMMdd  <pre>  20200523</pre>
     */
    public static final String YYYYMMDD = "yyyyMMdd";

    /**
     * yyyy/MM/dd  <pre>  2020/05/23</pre>
     */
    public static final String YYYY_MM_DD_EN = "yyyy/MM/dd";

    /**
     * yyyy/M/d 不补0  <pre>  2020/5/23</pre>
     */
    public static final String YYYY_M_D_EN = "yyyy/M/d";

    /**
     * yyyy年MM月dd日  <pre> 2020年05月23日</pre>
     */
    public static final String YYYY_MM_DD_CN = "yyyy年MM月dd日";

    /**
     * yyyy年M月d日 不补0  <pre> 2020年5月23日</pre>
     */
    public static final String YYYY_M_D_CN = "yyyy年M月d日";

    /**
     * yyyy.MM.dd  <pre>2020.05.23</pre>
     */
    public static final String YYYY_MM_DD_POINT = "yyyy.MM.dd";

    /**
     * yyyy.M.d 不补0  <pre>2020.5.23</pre>
     */
    public static final String YYYY_M_D_POINT = "yyyy.M.d";

    /**
     * yy/MM/dd  <pre>20/05/23</pre>
     */
    public static final String YY_MM_DD_EN = "yy/MM/dd";

    /**
     * yy/M/d  <pre>20/5/23</pre>
     */
    public static final String YY_M_D_EN = "yy/M/d";

    /**
     * MM/dd/yy  <pre>05/23/20</pre>
     */
    public static final String MM_DD_YY_EN = "MM/dd/yy";
    public static final String dd_MM_yyyy = "dd/MM/yyyy";
    public static final String MM_dd_yyyy = "MM/dd/yyyy";
    public static final String M_d_yyyy = "M/d/yyyy";
    /**
     * M/d/yy  <pre>5/23/20</pre>
     */
    public static final String M_D_YY_EN = "M/d/yy";

    /**
     * yyyy-MM-dd E  <pre>2020-05-23 星期六</pre>
     */
    public static final String YYYY_MM_DD_E = "yyyy-MM-dd E";

    /**
     * yy 年的后2位  <pre> 20</pre>
     */
    public static final String YY = "yy";

    /**
     * yyyy  <pre>2020</pre>
     */
    public static final String YYYY = "yyyy";

    /**
     * yyyy-MM  <pre>2020-05</pre>
     */
    public static final String YYYY_MM = "yyyy-MM";

    /**
     * yyyyMM  <pre>202005</pre>
     */
    public static final String YYYYMM = "yyyyMM";

    /**
     * yyyy/MM  <pre>2020/05</pre>
     */
    public static final String YYYY_MM_EN = "yyyy/MM";

    /**
     * yyyy年MM月  <pre>2020年05月</pre>
     */
    public static final String YYYY_MM_CN = "yyyy年MM月";

    /**
     * yyyy年M月  <pre>2020年5月</pre>
     */
    public static final String YYYY_M_CN = "yyyy年M月";

    /**
     * MM-dd  <pre>05-23</pre>
     */
    public static final String MM_DD = "MM-dd";

    /**
     * MMdd  <pre>0523</pre>
     */
    public static final String MMDD = "MMdd";

    /**
     * MM/dd  <pre>05/23</pre>
     */
    public static final String MM_DD_EN = "MM/dd";

    /**
     * M/d 不补0  <pre>5/23</pre>
     */
    public static final String M_D_EN = "M/d";

    /**
     * MM月dd日  <pre>05月23日</pre>
     */
    public static final String MM_DD_CN = "MM月dd日";

    /**
     * M月d日 不补0  <pre>5月23日</pre>
     */
    public static final String M_D_CN = "M月d日";


    // ==================================HH:mm:ss 相关Pattern==================================

    /**
     * HH:mm:ss  <pre>17:26:30</pre>
     */
    public static String HH_MM_SS = "HH:mm:ss";

    /**
     * H:m:s  <pre>17:6:30</pre>
     */
    public static String H_M_S = "H:m:s";

    /**
     * HHmmss  <pre>170630</pre>
     */
    public static String HHMMSS = "HHmmss";

    /**
     * HH时mm分ss秒  <pre>17时06分30秒</pre>
     */
    public static String HH_MM_SS_CN = "HH时mm分ss秒";

    /**
     * HH:mm  <pre>17:06</pre>
     */
    public static String HH_MM = "HH:mm";

    /**
     * H:m  <pre>17:6</pre>
     */
    public static String H_M = "H:m";

    /**
     * HH时mm分 <pre>17时06分</pre>
     */
    public static String HH_MM_CN = "HH时mm分";

    /**
     * hh:mm a <pre>05:06 下午 如果需要 显示PM 需要设置 Locale.ENGLISH</pre>
     */
    public static String HH_MM_A = "hh:mm a";

    // ==================================HH:mm:ss.SSS 相关Pattern==================================

    /**
     * HH:mm:ss.SSS  <pre>17:26:30.272</pre>
     */
    public static String HH_MM_SS_SSS = "HH:mm:ss.SSS";


    // ==================================HH:mm:ss.SSSSSS 相关Pattern==================================



    // ==================================yyyy-MM-dd HH:mm:ss 相关Pattern==================================

    /**
     * yyyy-MM-dd HH:mm:ss <pre>2020-05-23 17:06:30</pre>
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-M-d H:m:s <pre>2020-5-23 17:6:30</pre>
     */
    public static final String YYYY_M_D_H_M_S = "yyyy-M-d H:m:s";

    /**
     * yyyyMMddHHmmss <pre>20200523170630</pre>
     */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * yyyy/MM/dd HH:mm:ss <pre>2020/05/23 17:06:30</pre>
     */
    public static final String YYYY_MM_DD_HH_MM_SS_EN = "yyyy/MM/dd HH:mm:ss";

    /**
     * yyyy/M/d H:m:s <pre>2020/5/23 17:6:30</pre>
     */
    public static final String YYYY_M_D_H_M_S_EN = "yyyy/M/d H:m:s";

    /**
     * yyyy年MM月dd日 HH:mm:ss <pre>2020年05月23日 17:06:30</pre>
     */
    public static final String YYYY_MM_DD_HH_MM_SS_CN = "yyyy年MM月dd日 HH:mm:ss";

    /**
     * yyyy年MM月dd日 HH时mm分ss秒 <pre>2020年05月23日 17时06分30秒</pre>
     */
    public static final String YYYY_MM_DD_HH_MM_SS_CN_ALL = "yyyy年MM月dd日 HH时mm分ss秒";

    /**
     * yyyy-MM-dd HH:mm <pre>2020-05-23 17:06</pre>
     */
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * yyyy-M-d H:m <pre>2020-5-23 17:6</pre>
     */
    public static final String YYYY_M_D_H_M = "yyyy-M-d H:m";

    /**
     * yyyyMMddHHmm <pre>202005231706</pre>
     */
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";

    /**
     * yyyy/MM/dd HH:mm <pre>2020/05/23 17:06</pre>
     */
    public static final String YYYY_MM_DD_HH_MM_EN = "yyyy/MM/dd HH:mm";

    /**
     * yyyy/M/d H:m <pre>2020/5/23 17:6</pre>
     */
    public static final String YYYY_M_D_H_M_EN = "yyyy/M/d H:m";

    /**
     * yyyy/M/d h:m a <pre>2020/5/23 5:6 下午</pre>
     */
    public static final String YYYY_M_D_H_M_A_EN = "yyyy/M/d h:m a";

    /**
     * MM-dd HH:mm <pre>05-23 17:06</pre>
     */
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";

    /**
     * MM月dd日 HH:mm <pre>05月23日 17:06</pre>
     */
    public static final String MM_DD_HH_MM_CN = "MM月dd日 HH:mm";

    /**
     * MM-dd HH:mm:ss <pre>05-23 17:06:30</pre>
     */
    public static final String MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";

    /**
     * MM月dd日 HH:mm:ss <pre>05月23日 17:06:30</pre>
     */
    public static final String MM_DD_HH_MM_SS_CN = "MM月dd日 HH:mm:ss";

    /**
     * yyyy年MM月dd日 hh:mm:ss a <pre>2020年05月23日 05:06:30 下午  如果需要 显示PM 需要设置 Locale.ENGLISH</pre>
     */
    public static final String YYYY_MM_DD_HH_MM_SS_A_CN = "yyyy年MM月dd日 hh:mm:ss a";

    /**
     * yyyy年MM月dd日 hh时mm分ss秒 a <pre>2020年05月23日 17时06分30秒 下午  如果需要 显示PM 需要设置 Locale.ENGLISH</pre>
     */
    public static final String YYYY_MM_DD_HH_MM_SS_A_CN_ALL = "yyyy年MM月dd日 hh时mm分ss秒 a";


    // ==================================yyyy-MM-dd HH:mm:ss.SSS 相关Pattern==================================

    /**
     * yyyy-MM-dd HH:mm:ss.SSS <pre>2020-05-23 17:06:30.272</pre>
     */
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * yyyy-MM-dd HH:mm:ss,SSS <pre>2020-05-23 17:06:30,272</pre>
     */
    public static final String YYYY_MM_DD_HH_MM_SS_SSS_COMMA = "yyyy-MM-dd HH:mm:ss,SSS";

    /**
     * yyyyMMddHHmmssSSS <pre>20200523170630272</pre>
     */
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    /**
     * yyyy-M-d H:m:s.SSS <pre>2020-5-23 17:6:30.272</pre>
     */
    public static final String YYYY_M_D_H_M_S_SSS = "yyyy-M-d H:m:s.SSS";

    /**
     * yyyy/M/d H:m:s.SSS <pre>2020/5/23 17:6:30.272</pre>
     */
    public static final String YYYY_M_D_H_M_S_SSS_EN = "yyyy/M/d H:m:s.SSS";

    /**
     * yyyy-M-d H:m:s,SSS <pre>2020-5-23 17:6:30,272</pre>
     */
    public static final String YYYY_M_D_H_M_S_SSS_COMMA = "yyyy-M-d H:m:s,SSS";


    // ==================================yyyy-MM-dd HH:mm:ss.SSSSSS 相关Pattern==================================



    // ==================================Iso相关Pattern 包含 T==================================
    /**
     * yyyy-MM-dd'T'HH:mm:ss <pre>
     * 2020-05-23T17:06:30
     * 2020-05-23T09:06:30</pre>
     */
    public static final String YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
    /**
     * yyyy-MM-dd'T'HH:mm:ssZ <pre>2020-05-23T17:06:30+0800 2020-05-23T09:06:30+0000</pre>
     */
    public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * yyyy-MM-dd'T'HH:mm:ssxxx <pre>2020-05-23T17:06:30+08:00 2020-05-23T09:06:30+00:00 0时区时末尾 为+00:00</pre>
     */
    public static final String YYYY_MM_DD_T_HH_MM_SS_XXX = "yyyy-MM-dd'T'HH:mm:ssxxx";

    /**
     * yyyy-MM-dd'T'HH:mm:ssXXX <pre>2020-05-23T17:06:30+08:00 2020-05-23T09:06:30Z 0时区时末尾 为Z</pre>
     */
    public static final String YYYY_MM_DD_T_HH_MM_SS_XXX_Z = "yyyy-MM-dd'T'HH:mm:ssXXX";

    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ <pre>2020-05-23T17:06:30.272+0800 2020-05-23T09:06:30.272+0000</pre>
     */
    public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSSxxx
     * <pre>
     * 2020-05-23T17:06:30.272+08:00
     * 2020-05-23T09:06:30.272+00:00
     * </pre>
     */
    public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_XXX = "yyyy-MM-dd'T'HH:mm:ss.SSSxxx";

    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSSXXX
     * <pre>
     * 2020-05-23T17:06:30.272+08:00
     * 2020-05-23T09:06:30.272Z 0时区时末尾 为Z
     * </pre>
     */
    public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_XXX_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";





    // ==================================其他格式 Pattern==================================

    /**
     * EEE MMM dd HH:mm:ss zzz yyyy
     * <pre>
     * Sat May 23 17:06:30 CST 2020
     * </pre>
     */
    public static final String EEE_MMM_DD_HH_MM_SS_ZZZ_YYYY = "EEE MMM dd HH:mm:ss zzz yyyy";

}