package com.jn.langx.text.pinyin;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.concurrent.clhm.ConcurrentLinkedHashMap;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;

import java.util.List;

public class Pinyins {
    private static final GenericRegistry<PinyinDirectory> dictRegistry = new GenericRegistry<PinyinDirectory>(new ConcurrentLinkedHashMap.Builder().initialCapacity(100).maximumWeightedCapacity(1000000).build());

    /**
     * 在指定的字典下检索
     *
     * @see PinyinDirectoryItemToken
     * @see StringToken
     */
    public static String getPinyin(List<PinyinDirectory> dicts, String text, final int tokenMaxWord) {
        List<Token> tokens = analyze(dicts, text, tokenMaxWord);
        List<String> buffer = Pipeline.of(tokens).map(new Function<Token, String>() {
            @Override
            public String apply(Token token) {
                if (token instanceof StringToken) {
                    return ((StringToken) token).getBody();
                }
                PinyinDirectoryItemToken pinyinToken = (PinyinDirectoryItemToken) token;
                PinyinDirectoryItem item = pinyinToken.getBody();
                return item.getMapping();
            }
        }).asList();

        String result = Strings.join(" ", buffer);
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

    private static final PinyinDirectory CHINESE_PUNCTUATION_SYMBOLS = new PinyinDirectoryLoader().load("chinese_punctuation_symbol", Resources.loadClassPathResource("chinese_punctuation_symbol.dict", Pinyins.class));

    public static boolean isEnglishPunctuationSymbol(String c) {
        return ENGLISH_PUNCTUATION_SYMBOLS.contains(c);
    }

    public static boolean isChinesePunctuationSymbol(String c) {
        return CHINESE_PUNCTUATION_SYMBOLS.getItem(c) != null;
    }

    static {

        PinyinDirectoryLoader loader = new PinyinDirectoryLoader();
        // 姓氏大全
        PinyinDirectory CHINESE_SURNAME_DICT = loader.load("chinese_surname", Resources.loadClassPathResource("chinese_surname.dict", Pinyins.class));
        dictRegistry.register(CHINESE_SURNAME_DICT);

        // 单字大全
        PinyinDirectory HAN_ZI_DICT = loader.load("hanzi", Resources.loadClassPathResource("hanzi.dict", Pinyins.class));
        dictRegistry.register(HAN_ZI_DICT);

        // 成语大全
        PinyinDirectory IDIOM_DICT = loader.load("idiom", Resources.loadClassPathResource("idiom.dict", Pinyins.class));
        dictRegistry.register(IDIOM_DICT);

        // 标点符号大全
        dictRegistry.register(CHINESE_PUNCTUATION_SYMBOLS);
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
