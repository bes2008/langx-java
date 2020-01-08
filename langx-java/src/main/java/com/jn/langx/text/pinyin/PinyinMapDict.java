package com.jn.langx.text.pinyin;

import java.util.Map;
import java.util.Set;


public abstract class PinyinMapDict
        implements PinyinDict {
    public abstract Map<String, String[]> mapping();

    public Set<String> words() {
        return (mapping() != null) ? mapping().keySet() : null;
    }


    public String[] toPinyin(String word) {
        return (mapping() != null) ? (String[]) mapping().get(word) : null;
    }
}