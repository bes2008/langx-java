package com.jn.langx.text.i18n;

/**
 * 中国56个民族
 **/
public enum ChinaEthnic {

    HAN("01", "汉族"),
    MONGOL("02", "蒙古族"),
    HUI("03", "回族"),
    TIBETAN("04", "藏族"),
    UYGHUR("05", "维吾尔族"),
    MIAO("06", "苗族"),
    YI("07", "彝族"),
    ZHUANG("08", "壮族"),
    BUYEI("09", "布依族"),
    KOREAN("10", "朝鲜族"),
    MANCHU("11", "满族"),
    DONG("12", "侗族"),
    YAO("13", "瑶族"),
    BAI("14", "白族"),
    TUJIA("15", "土家族"),
    HANI("16", "哈尼族"),
    KAZAK("17", "哈萨克族"),
    DAI("18", "傣族"),
    LI("19", "黎族"),
    LISU("20", "傈僳族"),
    VA("21", "佤族"),
    SHE("22", "畲族"),
    GAOSHAN("23", "高山族"),
    LAHU("24", "拉祜族"),
    SUI("25", "水族"),
    DONGXIANG("26", "东乡族"),
    NAXI("27", "纳西族"),
    JINGPO("28", "景颇族"),
    KIRGIZ("29", "柯尔克孜族"),
    TU("30", "土族"),
    DAUR("31", "达斡尔族"),
    MULAO("32", "仫佬族"),
    QIANG("33", "羌族"),
    BLANG("34", "布朗族"),
    SALAR("35", "撒拉族"),
    MAONAN("36", "毛南族"),
    GELAO("37", "仡佬族"),
    XIBE("38", "锡伯族"),
    ACHANG("39", "阿昌族"),
    PUMI("40", "普米族"),
    TAJIK("41", "塔吉克族"),
    NU("42", "怒族"),
    UZBEK("43", "乌孜别克族"),
    RUSSIANS("44", "俄罗斯族"),
    EWENKI("45", "鄂温克族"),
    DEANG("46", "德昂族"),
    BONAN("47", "保安族"),
    YUGUR("48", "裕固族"),
    GIN("49", "京族"),
    TATAR("50", "塔塔尔族"),
    DERUNG("51", "独龙族"),
    OROQEN("52", "鄂伦春族"),
    HEZHEN("53", "赫哲族"),
    MONBA("54", "门巴族"),
    LHOBA("55", "珞巴族"),
    JINO("56", "基诺族"),
    OTHER("57", "其他"),
    FOREIGN_COUNTRY("58", "外国血统中国籍人士");

    private String code;
    private String desc;

    ChinaEthnic(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
