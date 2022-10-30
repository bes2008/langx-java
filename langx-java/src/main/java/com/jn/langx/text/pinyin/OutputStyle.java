package com.jn.langx.text.pinyin;

public class OutputStyle {

    /**
     * 非中文字符是否忽略掉
     */
    private boolean retainNonChineseChars = false;

    /**
     * 输出的拼音 是否带声调
     */
    private boolean withTone = true;

    /**
     * segment 之间的分隔符
     */
    private String segmentSeparator = "|";

    /**
     * 中文segment内token之间的分隔符
     */
    private String chineseTokenSeparator = " ";
    /**
     * 中文token 内的字符分隔符
     */
    private String chineseCharSeparator = " ";

    private boolean retainWhitespace = false;

    private boolean retainPunctuationSymbol = true;

    public boolean isRetainWhitespace() {
        return retainWhitespace;
    }

    public void setRetainWhitespace(boolean retainWhitespace) {
        this.retainWhitespace = retainWhitespace;
    }

    public boolean isRetainNonChineseChars() {
        return retainNonChineseChars;
    }

    public void setRetainNonChineseChars(boolean retainNonChineseChars) {
        this.retainNonChineseChars = retainNonChineseChars;
    }

    public boolean isWithTone() {
        return withTone;
    }

    public void setWithTone(boolean withTone) {
        this.withTone = withTone;
    }

    public static final OutputStyle DEFAULT_INSTANCE = new OutputStyle();

    public String getSegmentSeparator() {
        return segmentSeparator;
    }

    public void setSegmentSeparator(String segmentSeparator) {
        this.segmentSeparator = segmentSeparator;
    }

    public String getChineseCharSeparator() {
        return chineseCharSeparator;
    }

    public void setChineseCharSeparator(String chineseCharSeparator) {
        this.chineseCharSeparator = chineseCharSeparator;
    }

    public boolean isRetainPunctuationSymbol() {
        return retainPunctuationSymbol;
    }

    public void setRetainPunctuationSymbol(boolean retainPunctuationSymbol) {
        this.retainPunctuationSymbol = retainPunctuationSymbol;
    }

    public String getChineseTokenSeparator() {
        return chineseTokenSeparator;
    }

    public void setChineseTokenSeparator(String chineseTokenSeparator) {
        this.chineseTokenSeparator = chineseTokenSeparator;
    }
}
