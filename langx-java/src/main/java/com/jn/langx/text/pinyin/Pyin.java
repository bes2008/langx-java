package com.jn.langx.text.pinyin;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.multivalue.CommonMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;

import java.util.List;

 class Pyin {
    /**
     * 声母
     */
    List<String> INITIALS = Collects.newArrayList(
            "b", "p", "m", "f", "d", "t", "n", "l", "g", "k", "h", "j", "q", "x", "zh", "ch", "sh", "r", "z", "c", "s", "y", "w"
    );

    /**
     * 单韵母
     */
    List<String> SINGLE_FINALS = Collects.newArrayList(
            "a", "o", "e", "i", "u", "ü"
    );
    /**
     * 复韵母
     */
    List<String> MULTIPLE_FINALS = Collects.newArrayList(
            "ai", "ei", "ui", "ao", "ou", "iu", "ie", "üe", "er",
            "an", "en", "in", "un", "ün", "ang", "eng", "ing", "ong"
    );

    /**
     * 所有的基础韵母
     */
    List<String> BASIC_FINALS = Pipeline.of(Collects.newArrayList(SINGLE_FINALS)).addAll(MULTIPLE_FINALS).asList();

    /**
     * 声调
     */
    static final MultiValueMap<String, String> toneMap = new CommonMultiValueMap<String, String>();

    static {
        // https://unicode-table.com/en/sets/fancy-letters/
        toneMap.put("a", Collects.newArrayList("a", "ā", "á", "ǎ", "à"));
        toneMap.put("o", Collects.newArrayList("o", "ō", "ó", "ǒ", "ò"));
        toneMap.put("e", Collects.newArrayList("e", "ē", "é", "ě", "è"));
        toneMap.put("i", Collects.newArrayList("i", "ī", "í", "ǐ", "ì"));
        toneMap.put("u", Collects.newArrayList("u", "ū", "ú", "ǔ", "ù"));
        toneMap.put("ü", Collects.newArrayList("ü", "ǖ", "ǘ", "ǚ", "ǜ"));
    }

}
