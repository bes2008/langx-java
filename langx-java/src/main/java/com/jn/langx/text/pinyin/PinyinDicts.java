package com.jn.langx.text.pinyin;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;

import java.util.LinkedHashMap;
import java.util.List;

class PinyinDicts {
    private static final GenericRegistry<PinyinDict> dictRegistry = new GenericRegistry<PinyinDict>(new LinkedHashMap<String, PinyinDict>());

    public static final String DN_HAN_ZI = "hanzi";
    public static final String DN_MULTI_YIN_PHRASE = "multiple_yin_phrase";
    public static final String DN_IDIOM="idiom";
    public static final String DN_SURNAME="chinese_surname";
    public static final String DN_PUNCTUATION_SYMBOL="chinese_punctuation_symbol";

    public static void registerDict(PinyinDict dict) {
        if (dict != null) {
            dictRegistry.register(dict);
        }
    }

    public static void addDict(String name, Resource resource) {
        if (!dictRegistry.contains(name)) {
            PinyinDictLoader loader = new PinyinDictLoader();
            PinyinDict directory = loader.load(name, resource);
            registerDict(directory);
        }
    }

    static void addBuiltinDict(String name) {
        addDict(name, Resources.loadClassPathResource(dictPath(name), Pinyins.class));
    }

    public static List<PinyinDict> findDicts(Predicate<PinyinDict> predicate) {
        return Pipeline.of(dictRegistry.instances())
                .filter(predicate)
                .asList();
    }

    public static List<PinyinDict> findDicts(String... dictNames) {
        final List<String> names = Collects.asList(dictNames);
        if (names.isEmpty()) {
            return Collects.immutableList();
        }
        return Pipeline.of(dictRegistry.instances())
                .filter(new Predicate<PinyinDict>() {
                    @Override
                    public boolean test(PinyinDict dict) {
                        return names.contains(dict.getName());
                    }
                })
                .asList();
    }

    public static List<PinyinDict> allDicts() {
        return dictRegistry.instances();
    }

    public static PinyinDict getDict(String dictName) {
        return dictRegistry.get(dictName);
    }

    public static String dictPath(String dictName) {
        return StringTemplates.formatWithPlaceholder("dict/{}.dict", dictName);
    }

    static {


        // 易错词语 (主要是 一些容易出错的人名，地名，常用词语等)
        addBuiltinDict(DN_MULTI_YIN_PHRASE);

        // 单字大全
        addBuiltinDict(DN_HAN_ZI);

        // 成语大全
        addBuiltinDict(DN_IDIOM);

        // 姓氏大全
        addBuiltinDict(DN_SURNAME);

        // 标点符号大全
        addBuiltinDict(DN_PUNCTUATION_SYMBOL);
    }

}
