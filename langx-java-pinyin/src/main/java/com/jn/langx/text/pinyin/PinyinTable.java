package com.jn.langx.text.pinyin;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.multivalue.CommonMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;

import java.util.List;

/**
 * @since 5.1.0
 */
class PinyinTable {
    /**
     * 声母
     */
    static List<String> INITIALS = Collects.newArrayList(
            "b", "p", "m", "f", "d", "t", "n", "l", "g", "k", "h", "j", "q", "x", "zh", "ch", "sh", "r", "z", "c", "s", "y", "w"
    );

    /**
     * 单韵母
     */
    static List<String> SINGLE_FINALS = Collects.newArrayList(
            "a", "o", "e", "i", "u", "ü"
    );
    /**
     * 复韵母
     */
    static List<String> MULTIPLE_FINALS = Collects.newArrayList(
            "ai", "ei", "ui", "ao", "ou", "iu", "ie", "üe", "er",
            "an", "en", "in", "un", "ün", "ang", "eng", "ing", "ong"
    );

    /**
     * 所有的基础韵母
     */
    static List<String> BASIC_FINALS = Pipeline.of(Collects.newArrayList(SINGLE_FINALS)).addAll(MULTIPLE_FINALS).asList();

    /**
     * 声调
     */
    static final MultiValueMap<String, String> toneMap = new CommonMultiValueMap<String, String>();
    private static String TONES = "aāáǎàoōóǒòeēéěèiīíǐìuūúǔùüǖǘǚǜ";

    static {
        // https://unicode-table.com/en/sets/fancy-letters/

        String key = null;
        for (int i = 0; i < TONES.length(); i++) {
            String value = TONES.charAt(i) + "";
            if (i % 5 == 0) {
                key = TONES.charAt(i) + "";
            }
            toneMap.add(key, value);
        }
        // 上面的代码等价于下面的
        /*
        toneMap.put("a", Collects.newArrayList("a", "ā", "á", "ǎ", "à"));
        toneMap.put("o", Collects.newArrayList("o", "ō", "ó", "ǒ", "ò"));
        toneMap.put("e", Collects.newArrayList("e", "ē", "é", "ě", "è"));
        toneMap.put("i", Collects.newArrayList("i", "ī", "í", "ǐ", "ì"));
        toneMap.put("u", Collects.newArrayList("u", "ū", "ú", "ǔ", "ù"));
        toneMap.put("ü", Collects.newArrayList("ü", "ǖ", "ǘ", "ǚ", "ǜ"));
         */
    }
    private PinyinTable(){

    }

    public static String replaceTone(String pinyin) {
        final StringBuilder builder = new StringBuilder(pinyin.length());
        for (int i = 0; i < pinyin.length(); i++) {
            final String c = pinyin.charAt(i) + "";
            int index = TONES.indexOf(c);
            if (index >= 0) {
                index = index / 5 * 5;
                builder.append(TONES.charAt(index));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

}
