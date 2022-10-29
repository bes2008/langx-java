package com.jn.langx.text.pinyin;

public class OutputStyle {

    /**
     * 非中文字符是否忽略掉
     */
    boolean ignoreNonChinese = false;

    /**
     * 输出的拼音 是否带声调
     */
    boolean withTone = true;

    public boolean isIgnoreNonChinese() {
        return ignoreNonChinese;
    }

    public void setIgnoreNonChinese(boolean ignoreNonChinese) {
        this.ignoreNonChinese = ignoreNonChinese;
    }

    public boolean isWithTone() {
        return withTone;
    }

    public void setWithTone(boolean withTone) {
        this.withTone = withTone;
    }

    public static final OutputStyle DEFAULT_INSTANCE = new OutputStyle();
}
