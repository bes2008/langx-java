package com.jn.langx.text.pinyin;

import java.util.Set;

public interface PinyinDict {
    Set<String> words();

    String[] toPinyin(String paramString);
}