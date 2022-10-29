package com.jn.langx.text.pinyin;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.concurrent.clhm.ConcurrentLinkedHashMap;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;

import java.util.List;
import java.util.Set;

public class Pinyins {
    private static final GenericRegistry<PinyinDirectory> dictRegistry = new GenericRegistry<PinyinDirectory>(new ConcurrentLinkedHashMap.Builder().initialCapacity(100).maximumWeightedCapacity(1000000).build());

    public static String getPinyin(String text) {
        return getPinyin(text, null);
    }

    public static String getPinyin(String text, OutputStyle theOutputStyle) {
        return getPinyin(null, text, theOutputStyle);
    }

    public static String getPinyin(List<PinyinDirectory> dicts, String text, OutputStyle theOutputStyle) {
        return getPinyin(dicts, text, 5, theOutputStyle);
    }

    public static String getPersonName(String name, OutputStyle theOutputStyle) {
        List<PinyinDirectory> dicts = Pipeline.of("multiple_yin_phrase", "chinese_surname", "hanzi")
                .map(new Function<String, PinyinDirectory>() {
                    @Override
                    public PinyinDirectory apply(String dictName) {
                        return dictRegistry.get(dictName);
                    }
                }).asList();

        return getPinyin(dicts, name, theOutputStyle);
    }

    /**
     * 在指定的字典下检索
     *
     * @see PinyinDirectoryItemToken
     * @see StringToken
     */
    public static String getPinyin(List<PinyinDirectory> dicts, String text, final int tokenMaxWord, OutputStyle theOutputStyle) {
        List<Token> tokens = analyze(dicts, text, tokenMaxWord);
        final OutputStyle outputStyle = theOutputStyle == null ? OutputStyle.DEFAULT_INSTANCE : theOutputStyle;

        final String separator = Objs.useValueIfNull(outputStyle.getSeparator(), "");

        List<String> buffer = Pipeline.of(tokens).map(new Function<Token, String>() {
            @Override
            public String apply(Token token) {
                if (token instanceof StringToken) {
                    if (!outputStyle.isIgnoreNonChinese()) {
                        return ((StringToken) token).getBody();
                    } else {
                        return null;
                    }
                }
                PinyinDirectoryItemToken pinyinToken = (PinyinDirectoryItemToken) token;
                PinyinDirectoryItem item = pinyinToken.getBody();
                if (item.isPunctuationSymbol()) {
                    return item.getMapping();
                }
                if (outputStyle.isWithTone()) {
                    return Strings.join(separator, Strings.split(item.getPinyinWithTone(), " "));
                } else {
                    return Strings.join(separator, Strings.split(item.getPinyinWithoutTone(), " "));
                }
            }
        }).clearNulls().asList();
        String result = Strings.join(separator, buffer);
        return result;
    }

    private static List<Token> analyze(List<PinyinDirectory> dicts, String text, int tokenMaxWord) {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        analyzer.setDicts(dicts != null ? dicts : dictRegistry.instances());
        if (tokenMaxWord < 1) {
            tokenMaxWord = 1;
        }
        if (tokenMaxWord > 5) {
            tokenMaxWord = 5;
        }
        analyzer.setTokenMaxChar(tokenMaxWord);
        List<Token> tokens = analyzer.analyze(text);
        return tokens;
    }


    private static final List<String> ENGLISH_PUNCTUATION_SYMBOLS = Collects.newArrayList(
            ".", "?", "!", ",", ":", "...", ";", "-", "_", "(", ")", "[", "]", "{", "}", "'", "\""
    );

    protected static final PinyinDirectory CHINESE_PUNCTUATION_SYMBOLS_DICT = new PinyinDirectoryLoader().load("chinese_punctuation_symbol", Resources.loadClassPathResource("dict/chinese_punctuation_symbol.dict", Pinyins.class));

    static boolean isEnglishPunctuationSymbol(String c) {
        return ENGLISH_PUNCTUATION_SYMBOLS.contains(c);
    }

    static boolean isChinesePunctuationSymbol(String c) {
        return CHINESE_PUNCTUATION_SYMBOLS_DICT.getItem(c) != null;
    }

    public static final Set<String> CHINESE_PUNCTUATION_SYMBOL_SET = CHINESE_PUNCTUATION_SYMBOLS_DICT.keys();

    static {

        PinyinDirectoryLoader loader = new PinyinDirectoryLoader();

        // 易错词语 (主要是 一些容易出错的人名，地名，常用词语等)
        PinyinDirectory MULTIPLE_YIN_PHRASE = loader.load("multiple_yin_phrase", Resources.loadClassPathResource("dict/multiple_yin_phrase.dict", Pinyins.class));
        dictRegistry.register(MULTIPLE_YIN_PHRASE);

        // 单字大全
        PinyinDirectory HAN_ZI_DICT = loader.load("hanzi", Resources.loadClassPathResource("dict/hanzi.dict", Pinyins.class));
        dictRegistry.register(HAN_ZI_DICT);

        // 成语大全
        PinyinDirectory IDIOM_DICT = loader.load("idiom", Resources.loadClassPathResource("dict/idiom.dict", Pinyins.class));
        dictRegistry.register(IDIOM_DICT);

        // 姓氏大全
        PinyinDirectory CHINESE_SURNAME_DICT = loader.load("chinese_surname", Resources.loadClassPathResource("dict/chinese_surname.dict", Pinyins.class));
        dictRegistry.register(CHINESE_SURNAME_DICT);


        // 标点符号大全
        dictRegistry.register(CHINESE_PUNCTUATION_SYMBOLS_DICT);
    }

    public static void addDict(String name, Resource resource) {
        if (!dictRegistry.contains(name)) {
            PinyinDirectoryLoader loader = new PinyinDirectoryLoader();
            PinyinDirectory directory = loader.load(name, resource);
            dictRegistry.register(directory);
        }
    }

    public static List<PinyinDirectory> findDicts(Predicate<PinyinDirectory> predicate) {
        return Pipeline.of(dictRegistry.instances())
                .filter(predicate)
                .asList();
    }
}
