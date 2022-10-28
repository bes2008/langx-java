package com.jn.langx.text.pinyin;

import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class Pinyins {

    /**
     * 在指定的字典下检索
     *
     * @see PinyinDirectoryItem, String
     */
    public static List getPinyin(List<PinyinDirectory> directoryList, String text) {
        if (Strings.isEmpty(text)) {
            return Collects.emptyArrayList();
        }


        return null;
    }

    private static final List<String> ENGLISH_PUNCTUATION_SYMBOLS = Collects.newArrayList(
            ".", "?", "!", ",", ":", "...", ";", "-", "_", "(", ")", "[", "]", "{", "}", "'", "\""
    );

    private static final PinyinDirectory CHINESE_PUNCTUATION_SYMBOLS = new PinyinDirectoryLoader().load("chinese_punctuation_symbol", Resources.loadClassPathResource("chinese_punctuation_symbol.dict"));

    public static boolean isEnglishPunctuationSymbol(String c) {
        return ENGLISH_PUNCTUATION_SYMBOLS.contains(c);
    }

    public static boolean isChinesePunctuationSymbol(String c) {
        return CHINESE_PUNCTUATION_SYMBOLS.getItem(c) != null;
    }
}
