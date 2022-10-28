package com.jn.langx.text.pinyin;

public class PinyinDirectoryItem {
    /**
     * 字或词
     */
    private String word;

    private String pinyinWithTone;

    private String pinyinWithoutTone;

    private String surnamePinyinWithTone;

    private String surnamePinyinWithoutTone;

    public String getPinyinWithTone() {
        return pinyinWithTone;
    }

    public void setPinyinWithTone(String pinyinWithTone) {
        this.pinyinWithTone = pinyinWithTone;
    }

    public String getPinyinWithoutTone() {
        return pinyinWithoutTone;
    }

    public void setPinyinWithoutTone(String pinyinWithoutTone) {
        this.pinyinWithoutTone = pinyinWithoutTone;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSurnamePinyinWithTone() {
        return surnamePinyinWithTone;
    }

    public void setSurnamePinyinWithTone(String surnamePinyinWithTone) {
        this.surnamePinyinWithTone = surnamePinyinWithTone;
    }

    public String getSurnamePinyinWithoutTone() {
        return surnamePinyinWithoutTone;
    }

    public void setSurnamePinyinWithoutTone(String surnamePinyinWithoutTone) {
        this.surnamePinyinWithoutTone = surnamePinyinWithoutTone;
    }
}
