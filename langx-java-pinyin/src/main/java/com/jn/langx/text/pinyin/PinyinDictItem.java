package com.jn.langx.text.pinyin;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;

import java.io.Serializable;

/**
 * @since 5.1.0
 */
class PinyinDictItem implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 汉字 字、词、成语等
     */
    private String word;
    /**
     * 繁体字、简体字、标点符号时，会用到这个。
     *
     * <pre>
     *      1. 如果 traditional = true ，即 word 为 繁体字，那么 mapping 就是对应的简体字。
     *      2. 如果 isPunctuationSymbol = true, 即 word 为标点符号，那么 mapping的就是 英文标点符号
     * </pre>
     */
    private String mapping;

    /**
     * word 是否为繁体字
     */
    private boolean traditional;

    /**
     * word 是否为标点符号
     */
    private boolean isPunctuationSymbol;

    /**
     * 如果是 word是个字，并且是是多音字，只保留最常用的 拼音
     */
    @Nullable
    private String pinyinWithTone;

    @Nullable
    private String pinyinWithoutTone;


    public String getPinyinWithTone() {
        return pinyinWithTone;
    }

    protected void setPinyinWithTone(String pinyinWithTone) {
        this.pinyinWithTone = pinyinWithTone;
    }

    public String getPinyinWithoutTone() {
        return pinyinWithoutTone;
    }

    protected void setPinyinWithoutTone(String pinyinWithoutTone) {
        this.pinyinWithoutTone = pinyinWithoutTone;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public String getMapping() {
        return mapping;
    }

    protected void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public boolean isPunctuationSymbol() {
        return isPunctuationSymbol;
    }

    protected void setPunctuationSymbol(boolean punctuationSymbol) {
        isPunctuationSymbol = punctuationSymbol;
    }

    public boolean isTraditional() {
        return traditional;
    }

    protected void setTraditional(boolean traditional) {
        this.traditional = traditional;
    }

    @Override
    public String toString() {
        if (isPunctuationSymbol) {
            return getMapping();
        }
        if (isTraditional()) {
            return getMapping();
        }
        return Objs.useValueIfEmpty(getPinyinWithTone(), getMapping());
    }
}
