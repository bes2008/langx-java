package com.jn.langx.util.datetime.calendarist;

import java.util.TimeZone;

/**
 * @since 5.2.0
 */
public class CalendaristConstants {
    private CalendaristConstants() {

    }

    /**
     * 默认时区
     */
    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT+8");

    /**
     * 农历起始时间戳 （1900年1月31）
     */
    public static final long LUNAR_INIT_TIMESTAMP = -2206425943000L;

    /**
     * 支持转换的最小农历年份
     */
    public static final int MIN_YEAR = 1900;

    /**
     * 支持转换的最大农历年份
     */
    public static final int MAX_YEAR = 2100;

    /**
     * 阴历年份编码
     * <p>
     * 从1900年到2100年的农历月份数据代码 20位二进制代码表示一个年份的数据，
     * 前四位0:表示闰月为29天，1:表示闰月为30天
     * 中间12位：从左起表示1-12月每月的大小，1为30天，0为29天
     * 最后四位：表示闰月的月份，0表示当年无闰月
     * 前四位和最后四位应该结合使用，如果最后四位为0，则不考虑前四位
     * 例：
     * 1901年代码为 19168，转成二进制为 0b100101011100000, 最后四位为0，当年无闰月，月份数据为 010010101110 分别代表12月的大小情况
     * 1903年代码为 21717，转成二进制为 0b101010011010101，最后四位为5，当年为闰五月，首四位为0，闰月为29天， 月份数据为 010101001101，分别代表12月的大小情况
     */
    static final int[] LUNAR_CODE = new int[]{
            19416,
            19168, 42352, 21717, 53856, 55632, 91476, 22176, 39632,
            21970, 19168, 42422, 42192, 53840, 119381, 46400, 54944,
            44450, 38320, 84343, 18800, 42160, 46261, 27216, 27968,
            109396, 11104, 38256, 21234, 18800, 25958, 54432, 59984,
            92821, 23248, 11104, 100067, 37600, 116951, 51536, 54432,
            120998, 46416, 22176, 107956, 9680, 37584, 53938, 43344,
            46423, 27808, 46416, 86869, 19872, 42416, 83315, 21168,
            43432, 59728, 27296, 44710, 43856, 19296, 43748, 42352,
            21088, 62051, 55632, 23383, 22176, 38608, 19925, 19152,
            42192, 54484, 53840, 54616, 46400, 46752, 103846, 38320,
            18864, 43380, 42160, 45690, 27216, 27968, 44870, 43872,
            38256, 19189, 18800, 25776, 29859, 59984, 27480, 23232,
            43872, 38613, 37600, 51552, 55636, 54432, 55888, 30034,
            22176, 43959, 9680, 37584, 51893, 43344, 46240, 47780,
            44368, 21977, 19360, 42416, 86390, 21168, 43312, 31060,
            27296, 44368, 23378, 19296, 42726, 42208, 53856, 60005,
            54576, 23200, 30371, 38608, 19195, 19152, 42192, 118966,
            53840, 54560, 56645, 46496, 22224, 21938, 18864, 42359,
            42160, 43600, 111189, 27936, 44448, 84835, 37744, 18936,
            18800, 25776, 92326, 59984, 27296, 108228, 43744, 37600,
            53987, 51552, 54615, 54432, 55888, 23893, 22176, 42704,
            21972, 21200, 43448, 43344, 46240, 46758, 44368, 21920,
            43940, 42416, 21168, 45683, 26928, 29495, 27296, 44368,
            84821, 19296, 42352, 21732, 53600, 59752, 54560, 55968,
            92838, 22224, 19168, 43476, 41680, 53584, 62034, 54560
    };

    /**
     * 阳历年份编码
     */
    static final int[] SOLAR_CODE = {
            1887, 0xec04c, 0xec23f, 0xec435, 0xec649,
            0xec83e, 0xeca51, 0xecc46, 0xece3a, 0xed04d, 0xed242, 0xed436,
            0xed64a, 0xed83f, 0xeda53, 0xedc48, 0xede3d, 0xee050, 0xee244,
            0xee439, 0xee64d, 0xee842, 0xeea36, 0xeec4a, 0xeee3e, 0xef052,
            0xef246, 0xef43a, 0xef64e, 0xef843, 0xefa37, 0xefc4b, 0xefe41,
            0xf0054, 0xf0248, 0xf043c, 0xf0650, 0xf0845, 0xf0a38, 0xf0c4d,
            0xf0e42, 0xf1037, 0xf124a, 0xf143e, 0xf1651, 0xf1846, 0xf1a3a,
            0xf1c4e, 0xf1e44, 0xf2038, 0xf224b, 0xf243f, 0xf2653, 0xf2848,
            0xf2a3b, 0xf2c4f, 0xf2e45, 0xf3039, 0xf324d, 0xf3442, 0xf3636,
            0xf384a, 0xf3a3d, 0xf3c51, 0xf3e46, 0xf403b, 0xf424e, 0xf4443,
            0xf4638, 0xf484c, 0xf4a3f, 0xf4c52, 0xf4e48, 0xf503c, 0xf524f,
            0xf5445, 0xf5639, 0xf584d, 0xf5a42, 0xf5c35, 0xf5e49, 0xf603e,
            0xf6251, 0xf6446, 0xf663b, 0xf684f, 0xf6a43, 0xf6c37, 0xf6e4b,
            0xf703f, 0xf7252, 0xf7447, 0xf763c, 0xf7850, 0xf7a45, 0xf7c39,
            0xf7e4d, 0xf8042, 0xf8254, 0xf8449, 0xf863d, 0xf8851, 0xf8a46,
            0xf8c3b, 0xf8e4f, 0xf9044, 0xf9237, 0xf944a, 0xf963f, 0xf9853,
            0xf9a47, 0xf9c3c, 0xf9e50, 0xfa045, 0xfa238, 0xfa44c, 0xfa641,
            0xfa836, 0xfaa49, 0xfac3d, 0xfae52, 0xfb047, 0xfb23a, 0xfb44e,
            0xfb643, 0xfb837, 0xfba4a, 0xfbc3f, 0xfbe53, 0xfc048, 0xfc23c,
            0xfc450, 0xfc645, 0xfc839, 0xfca4c, 0xfcc41, 0xfce36, 0xfd04a,
            0xfd23d, 0xfd451, 0xfd646, 0xfd83a, 0xfda4d, 0xfdc43, 0xfde37,
            0xfe04b, 0xfe23f, 0xfe453, 0xfe648, 0xfe83c, 0xfea4f, 0xfec44,
            0xfee38, 0xff04c, 0xff241, 0xff436, 0xff64a, 0xff83e, 0xffa51,
            0xffc46, 0xffe3a, 0x10004e, 0x100242, 0x100437, 0x10064b, 0x100841,
            0x100a53, 0x100c48, 0x100e3c, 0x10104f, 0x101244, 0x101438,
            0x10164c, 0x101842, 0x101a35, 0x101c49, 0x101e3d, 0x102051,
            0x102245, 0x10243a, 0x10264e, 0x102843, 0x102a37, 0x102c4b,
            0x102e3f, 0x103053, 0x103247, 0x10343b, 0x10364f, 0x103845,
            0x103a38, 0x103c4c, 0x103e42, 0x104036, 0x104249, 0x10443d,
            0x104651, 0x104846, 0x104a3a, 0x104c4e, 0x104e43, 0x105038,
            0x10524a, 0x10543e, 0x105652, 0x105847, 0x105a3b, 0x105c4f,
            0x105e45, 0x106039, 0x10624c, 0x106441, 0x106635, 0x106849,
            0x106a3d, 0x106c51, 0x106e47, 0x10703c, 0x10724f, 0x107444,
            0x107638, 0x10784c, 0x107a3f, 0x107c53, 0x107e48
    };

    /**
     * 从1900年，至2100年每年的农历春节对应的阳历日期
     */
    static final int[] CHINESE_NEW_YEAR = new int[]{
            19000131,
            19010219, 19020208, 19030129, 19040216, 19050204,
            19060125, 19070213, 19080202, 19090122, 19100210,
            19110130, 19120218, 19130206, 19140126, 19150214,
            19160203, 19170123, 19180211, 19190201, 19200220,
            19210208, 19220128, 19230216, 19240205, 19250124,
            19260213, 19270202, 19280123, 19290210, 19300130,
            19310217, 19320206, 19330126, 19340214, 19350204,
            19360124, 19370211, 19380131, 19390219, 19400208,
            19410127, 19420215, 19430205, 19440125, 19450213,
            19460202, 19470122, 19480210, 19490129, 19500217,
            19510206, 19520127, 19530214, 19540203, 19550124,
            19560212, 19570131, 19580218, 19590208, 19600128,
            19610215, 19620205, 19630125, 19640213, 19650202,
            19660121, 19670209, 19680130, 19690217, 19700206,
            19710127, 19720215, 19730203, 19740123, 19750211,
            19760131, 19770218, 19780207, 19790128, 19800216,
            19810205, 19820125, 19830213, 19840202, 19850220,
            19860209, 19870129, 19880217, 19890206, 19900127,
            19910215, 19920204, 19930123, 19940210, 19950131,
            19960219, 19970207, 19980128, 19990216, 20000205,
            20010124, 20020212, 20030201, 20040122, 20050209,
            20060129, 20070218, 20080207, 20090126, 20100214,
            20110203, 20120123, 20130210, 20140131, 20150219,
            20160208, 20170128, 20180216, 20190205, 20200125,
            20210212, 20220201, 20230122, 20240210, 20250129,
            20260217, 20270206, 20280126, 20290213, 20300203,
            20310123, 20320211, 20330131, 20340219, 20350208,
            20360128, 20370215, 20380204, 20390124, 20400212,
            20410201, 20420122, 20430210, 20440130, 20450217,
            20460206, 20470126, 20480214, 20490202, 20500123,
            20510211, 20520201, 20530219, 20540208, 20550128,
            20560215, 20570204, 20580124, 20590212, 20600202,
            20610121, 20620209, 20630129, 20640217, 20650205,
            20660126, 20670214, 20680203, 20690123, 20700211,
            20710131, 20720219, 20730207, 20740127, 20750215,
            20760205, 20770124, 20780212, 20790202, 20800122,
            20810209, 20820129, 20830217, 20840206, 20850126,
            20860214, 20870203, 20880124, 20890210, 20900130,
            20910218, 20920207, 20930127, 20940215, 20950205,
            20960125, 20970212, 20980201, 20990121, 21000209
    };

    /**
     * 从1900年，至2100年每年的农历春节对应的阳历日期时间戳
     */
    static final long[] CHINESE_NEW_YEAR_TIMESTAMP = new long[]{
            -2206425943000L, -2173248000000L, -2142662400000L, -2111990400000L, -2078899200000L,
            -2048313600000L, -2017641600000L, -1984464000000L, -1953878400000L, -1923206400000L,
            -1890028800000L, -1859443200000L, -1826265600000L, -1795680000000L, -1765094400000L,
            -1731916800000L, -1701331200000L, -1670659200000L, -1637481600000L, -1606809600000L,
            -1573632000000L, -1543046400000L, -1512460800000L, -1479283200000L, -1448697600000L,
            -1418112000000L, -1384848000000L, -1354262400000L, -1323590400000L, -1290412800000L,
            -1259827200000L, -1226736000000L, -1196150400000L, -1165478400000L, -1132300800000L,
            -1101628800000L, -1071043200000L, -1037865600000L, -1007280000000L, -974102400000L,
            -943516800000L, -912931200000L, -879753600000L, -849081600000L, -818496000000L,
            -785232000000L, -754646400000L, -724060800000L, -690883200000L, -660297600000L,
            -627120000000L, -596534400000L, -565862400000L, -532684800000L, -502099200000L,
            -471427200000L, -438249600000L, -407664000000L, -374572800000L, -343900800000L,
            -313315200000L, -280137600000L, -249465600000L, -218880000000L, -185702400000L,
            -155030400000L, -124531200000L, -91353600000L, -60681600000L, -27504000000L,
            3081600000L, 33753600000L, 66931200000L, 97516800000L, 128102400000L,
            161280000000L, 191865600000L, 225043200000L, 255628800000L, 286300800000L,
            319478400000L, 350150400000L, 380736000000L, 413913600000L, 444499200000L,
            477676800000L, 508262400000L, 538848000000L, 572025600000L, 602697600000L,
            633369600000L, 666547200000L, 697132800000L, 727718400000L, 760809600000L,
            791481600000L, 824659200000L, 855244800000L, 885916800000L, 919094400000L,
            949680000000L, 980265600000L, 1013443200000L, 1044028800000L, 1074700800000L,
            1107878400000L, 1138464000000L, 1171728000000L, 1202313600000L, 1232899200000L,
            1266076800000L, 1296662400000L, 1327248000000L, 1360425600000L, 1391097600000L,
            1424275200000L, 1454860800000L, 1485532800000L, 1518710400000L, 1549296000000L,
            1579881600000L, 1613059200000L, 1643644800000L, 1674316800000L, 1707494400000L,
            1738080000000L, 1771257600000L, 1801843200000L, 1832428800000L, 1865606400000L,
            1896278400000L, 1926864000000L, 1960041600000L, 1990713600000L, 2023891200000L,
            2054476800000L, 2085062400000L, 2118240000000L, 2148825600000L, 2179411200000L,
            2212588800000L, 2243260800000L, 2273932800000L, 2307110400000L, 2337696000000L,
            2370873600000L, 2401459200000L, 2432044800000L, 2465222400000L, 2495808000000L,
            2526480000000L, 2559657600000L, 2590329600000L, 2623507200000L, 2654092800000L,
            2684678400000L, 2717769600000L, 2748441600000L, 2779027200000L, 2812204800000L,
            2842876800000L, 2873462400000L, 2906640000000L, 2937225600000L, 2970403200000L,
            3000988800000L, 3031660800000L, 3064838400000L, 3095424000000L, 3126096000000L,
            3159273600000L, 3189859200000L, 3223036800000L, 3253622400000L, 3284208000000L,
            3317385600000L, 3348057600000L, 3378643200000L, 3411820800000L, 3442492800000L,
            3473078400000L, 3506256000000L, 3536841600000L, 3570019200000L, 3600604800000L,
            3631276800000L, 3664454400000L, 3695040000000L, 3725712000000L, 3758803200000L,
            3789388800000L, 3822566400000L, 3853152000000L, 3883824000000L, 3917001600000L,
            3947673600000L, 3978259200000L, 4011436800000L, 4042022400000L, 4072608000000L,
            4105785600000L
    };

    static final int[] LUNAR_MONTH_DAYS = {
            1887, 0x1694, 0x16aa, 0x4ad5,
            0xab6, 0xc4b7, 0x4ae, 0xa56, 0xb52a, 0x1d2a, 0xd54, 0x75aa, 0x156a,
            0x1096d, 0x95c, 0x14ae, 0xaa4d, 0x1a4c, 0x1b2a, 0x8d55, 0xad4,
            0x135a, 0x495d, 0x95c, 0xd49b, 0x149a, 0x1a4a, 0xbaa5, 0x16a8,
            0x1ad4, 0x52da, 0x12b6, 0xe937, 0x92e, 0x1496, 0xb64b, 0xd4a,
            0xda8, 0x95b5, 0x56c, 0x12ae, 0x492f, 0x92e, 0xcc96, 0x1a94,
            0x1d4a, 0xada9, 0xb5a, 0x56c, 0x726e, 0x125c, 0xf92d, 0x192a,
            0x1a94, 0xdb4a, 0x16aa, 0xad4, 0x955b, 0x4ba, 0x125a, 0x592b,
            0x152a, 0xf695, 0xd94, 0x16aa, 0xaab5, 0x9b4, 0x14b6, 0x6a57,
            0xa56, 0x1152a, 0x1d2a, 0xd54, 0xd5aa, 0x156a, 0x96c, 0x94ae,
            0x14ae, 0xa4c, 0x7d26, 0x1b2a, 0xeb55, 0xad4, 0x12da, 0xa95d,
            0x95a, 0x149a, 0x9a4d, 0x1a4a, 0x11aa5, 0x16a8, 0x16d4, 0xd2da,
            0x12b6, 0x936, 0x9497, 0x1496, 0x1564b, 0xd4a, 0xda8, 0xd5b4,
            0x156c, 0x12ae, 0xa92f, 0x92e, 0xc96, 0x6d4a, 0x1d4a, 0x10d65,
            0xb58, 0x156c, 0xb26d, 0x125c, 0x192c, 0x9a95, 0x1a94, 0x1b4a,
            0x4b55, 0xad4, 0xf55b, 0x4ba, 0x125a, 0xb92b, 0x152a, 0x1694,
            0x96aa, 0x15aa, 0x12ab5, 0x974, 0x14b6, 0xca57, 0xa56, 0x1526,
            0x8e95, 0xd54, 0x15aa, 0x49b5, 0x96c, 0xd4ae, 0x149c, 0x1a4c,
            0xbd26, 0x1aa6, 0xb54, 0x6d6a, 0x12da, 0x1695d, 0x95a, 0x149a,
            0xda4b, 0x1a4a, 0x1aa4, 0xbb54, 0x16b4, 0xada, 0x495b, 0x936,
            0xf497, 0x1496, 0x154a, 0xb6a5, 0xda4, 0x15b4, 0x6ab6, 0x126e,
            0x1092f, 0x92e, 0xc96, 0xcd4a, 0x1d4a, 0xd64, 0x956c, 0x155c,
            0x125c, 0x792e, 0x192c, 0xfa95, 0x1a94, 0x1b4a, 0xab55, 0xad4,
            0x14da, 0x8a5d, 0xa5a, 0x1152b, 0x152a, 0x1694, 0xd6aa, 0x15aa,
            0xab4, 0x94ba, 0x14b6, 0xa56, 0x7527, 0xd26, 0xee53, 0xd54, 0x15aa,
            0xa9b5, 0x96c, 0x14ae, 0x8a4e, 0x1a4c, 0x11d26, 0x1aa4, 0x1b54,
            0xcd6a, 0xada, 0x95c, 0x949d, 0x149a, 0x1a2a, 0x5b25, 0x1aa4,
            0xfb52, 0x16b4, 0xaba, 0xa95b, 0x936, 0x1496, 0x9a4b, 0x154a,
            0x136a5, 0xda4, 0x15ac
    };

    /**
     * 阳历每月1号时一年中已过去的天数
     */
    private static final int[] DAYS_BEFORE_MONTH =
            {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};

    /**
     * 天干
     */
    static final String[] TIANGAN_INFO = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};

    /**
     * 地支
     */
    static final String[] DIZHI_INFO = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

    /**
     * 生肖
     */
    static final String[] ZODIAC_INFO = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};

    /**
     * 24节气
     */
    static final long[] SOLAR_TERM_INFO = new long[]{
            0, 21208, 42467, 63836, 85337, 107014, 128867, 150921,
            173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033,
            353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758
    };

}
