package com.jn.langx.text.pinyin;

import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;

import java.util.Comparator;
import java.util.List;

public class Pinyins {

    public static String getPersonName(String name, OutputStyle theOutputStyle) {
        return getPinyin(name, 4, true, theOutputStyle);
    }

    public static String getPinyin(String text) {
        return getPinyin(text, null);
    }


    public static String getPinyin(String text, OutputStyle theOutputStyle, String... dictNames) {
        return getPinyin(text, 5, false, theOutputStyle, dictNames);
    }

    public static String getPinyin(String text, int tokenMaxWord, OutputStyle theOutputStyle, String... dictNames) {
        return getPinyin(text, tokenMaxWord, false, theOutputStyle, dictNames);
    }

    /**
     * 在指定的字典下检索
     *
     * @param text           要解析的文本
     * @param tokenMaxWord   解析时最大的解析长度
     * @param surnameFirst   是否优先按照人名姓氏进行解析
     * @param theOutputStyle 解析完毕后，输出的风格
     * @param dictNames      要用的词典
     * @see PinyinDictItemToken
     * @see StringToken
     */
    public static String getPinyin(String text, int tokenMaxWord, boolean surnameFirst, OutputStyle theOutputStyle, String... dictNames) {
        List<Token> tokens = analyze(PinyinDicts.findDicts(dictNames), text, tokenMaxWord, surnameFirst);
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
                PinyinDictItemToken pinyinToken = (PinyinDictItemToken) token;
                PinyinDictItem item = pinyinToken.getBody();
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

    private static List<Token> analyze(List<PinyinDict> dicts, String text, int tokenMaxWord, boolean surnameFirst) {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        analyzer.setDicts(dicts != null ? dicts : PinyinDicts.allDicts());

        int maxKeyLength = Pipeline.of(dicts)
                .max(new Comparator<PinyinDict>() {
                    @Override
                    public int compare(PinyinDict o1, PinyinDict o2) {
                        return o1.getMaxKeyLength() - o2.getMaxKeyLength();
                    }
                }).getMaxKeyLength();

        if (tokenMaxWord > maxKeyLength) {
            tokenMaxWord = maxKeyLength;
        }
        if (tokenMaxWord < 1) {
            tokenMaxWord = 1;
        }
        if (surnameFirst) {
            tokenMaxWord = Maths.min(Maths.max(tokenMaxWord, 2), 4);
        }
        analyzer.setTokenMaxChar(tokenMaxWord);
        List<Token> tokens = analyzer.analyze(text);
        return tokens;
    }


    private static final List<String> ENGLISH_PUNCTUATION_SYMBOLS = Collects.newArrayList(
            ".", "?", "!", ",", ":", "...", ";", "-", "_", "(", ")", "[", "]", "{", "}", "'", "\""
    );


    static boolean isEnglishPunctuationSymbol(String c) {
        return ENGLISH_PUNCTUATION_SYMBOLS.contains(c);
    }

    static boolean isChinesePunctuationSymbol(String c) {
        return PinyinDicts.CHINESE_PUNCTUATION_SYMBOLS_DICT.getItem(c) != null;
    }

}
