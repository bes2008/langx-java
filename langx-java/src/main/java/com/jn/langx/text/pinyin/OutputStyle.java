package com.jn.langx.text.pinyin;

public class OutputStyle {

    /**
     * 非中文字符是否忽略掉
     */
    private boolean ignoreNonChinese = false;

    /**
     * 输出的拼音 是否带声调
     */
    private boolean withTone = true;

    /**
     * token 之间的分隔符
     */
    private String separator = "|";

    /**
     * 中文字符分隔符
     */
    private String chineseCharSeparator =" ";

    private boolean retainWhitespace = false;

    public boolean isRetainWhitespace() {
        return retainWhitespace;
    }

    public void setRetainWhitespace(boolean retainWhitespace) {
        this.retainWhitespace = retainWhitespace;
    }

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

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getChineseCharSeparator() {
        return chineseCharSeparator;
    }

    public void setChineseCharSeparator(String chineseCharSeparator) {
        this.chineseCharSeparator = chineseCharSeparator;
    }
}
