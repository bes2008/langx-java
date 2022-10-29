package com.jn.langx.text.pinyin;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;

import java.util.LinkedHashMap;
import java.util.List;

public class PinyinDicts {
    private static final GenericRegistry<PinyinDict> dictRegistry = new GenericRegistry<PinyinDict>(new LinkedHashMap<String, PinyinDict>());
    static final PinyinDict CHINESE_PUNCTUATION_SYMBOLS_DICT = new PinyinDictLoader().load("chinese_punctuation_symbol", Resources.loadClassPathResource("dict/chinese_punctuation_symbol.dict", Pinyins.class));

    public static void registerDict(PinyinDict dict) {
        dictRegistry.register(dict);
    }

    public static void addDict(String name, Resource resource) {
        if (!dictRegistry.contains(name)) {
            PinyinDictLoader loader = new PinyinDictLoader();
            PinyinDict directory = loader.load(name, resource);
            registerDict(directory);
        }
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

    public static List<PinyinDict> allDicts(){
        return dictRegistry.instances();
    }

    public static PinyinDict getDict(String dictName) {
        return dictRegistry.get(dictName);
    }

    static {

        PinyinDictLoader loader = new PinyinDictLoader();

        // 易错词语 (主要是 一些容易出错的人名，地名，常用词语等)
        PinyinDict MULTIPLE_YIN_PHRASE = loader.load("multiple_yin_phrase", Resources.loadClassPathResource("dict/multiple_yin_phrase.dict", Pinyins.class));
        PinyinDicts.registerDict(MULTIPLE_YIN_PHRASE);

        // 单字大全
        PinyinDict HAN_ZI_DICT = loader.load("hanzi", Resources.loadClassPathResource("dict/hanzi.dict", Pinyins.class));
        PinyinDicts.registerDict(HAN_ZI_DICT);

        // 成语大全
        PinyinDict IDIOM_DICT = loader.load("idiom", Resources.loadClassPathResource("dict/idiom.dict", Pinyins.class));
        PinyinDicts.registerDict(IDIOM_DICT);

        // 姓氏大全
        PinyinDict CHINESE_SURNAME_DICT = loader.load("chinese_surname", Resources.loadClassPathResource("dict/chinese_surname.dict", Pinyins.class));
        PinyinDicts.registerDict(CHINESE_SURNAME_DICT);


        // 标点符号大全
        PinyinDicts.registerDict(CHINESE_PUNCTUATION_SYMBOLS_DICT);
    }

}
