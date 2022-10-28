package com.jn.langx.text.pinyin;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class Pinyins {
    private static final GenericRegistry<PinyinDirectory> dictRegistry = new GenericRegistry<PinyinDirectory>();

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


    static {
        // 标点符号大全
        dictRegistry.register(CHINESE_PUNCTUATION_SYMBOLS);
        PinyinDirectoryLoader loader = new PinyinDirectoryLoader();
        // 姓氏大全
        PinyinDirectory CHINESE_SURNAME_DICT = loader.load("chinese_surname", Resources.loadClassPathResource("chinese_surname.dict"));
        dictRegistry.register(CHINESE_SURNAME_DICT);

        // 单字大全
        PinyinDirectory HAN_ZI_DICT = loader.load("hanzi", Resources.loadClassPathResource("hanzi.dict"));
        dictRegistry.register(HAN_ZI_DICT);

        // 成语大全
        PinyinDirectory IDIOM_DICT = loader.load("idiom", Resources.loadClassPathResource("idiom.dict"));
        dictRegistry.register(IDIOM_DICT);
    }

    public static void addDirectory(String name, Resource resource) {
        if (!dictRegistry.contains(name)) {
            PinyinDirectoryLoader loader = new PinyinDirectoryLoader();
            PinyinDirectory directory = loader.load(name, resource);
            dictRegistry.register(directory);
        }
    }
}
