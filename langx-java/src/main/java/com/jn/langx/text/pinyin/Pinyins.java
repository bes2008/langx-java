package com.jn.langx.text.pinyin;

import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;

import java.util.Comparator;
import java.util.List;

public class Pinyins extends PinyinDicts {

    public static String getPersonName(String name, OutputStyle theOutputStyle) {
        return getPinyin(name, 4, true, theOutputStyle);
    }

    public static String getPinyin(String text) {
        return getPinyin(text, null);
    }


    public static String getPinyin(String text, OutputStyle theOutputStyle, String... dictNames) {
        return getPinyin(text, 50, false, theOutputStyle, dictNames);
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
        List<SegmentToken> segmentTokens = analyze(PinyinDicts.findDicts(dictNames), text, tokenMaxWord, surnameFirst);
        final OutputStyle outputStyle = theOutputStyle == null ? OutputStyle.DEFAULT_INSTANCE : theOutputStyle;


        final String chineseCharSeparator = Objs.useValueIfNull(outputStyle.getChineseCharSeparator(), " ");
        final String chineseTokenSeparator = Objs.useValueIfNull(outputStyle.getChineseTokenSeparator(), " ");
        List<String> segments = Pipeline.of(segmentTokens).map(new Function<SegmentToken, String>() {
            @Override
            public String apply(SegmentToken segmentToken) {
                String segment = null;
                // 标点符号
                if (segmentToken.isPunctuationSymbol()) {
                    if (outputStyle.isRetainPunctuationSymbol()) {
                        segment = segmentToken.toString();
                    }
                    return segment;
                }
                // 下面是非标点符号

                // 普通的文本
                if (segmentToken instanceof StringToken) {
                    if (outputStyle.isRetainNonChineseChars()) {
                        segment = ((StringToken) segmentToken).getBody();
                    }
                    return segment;
                }
                ChineseSequenceToken chineseSequenceToken = (ChineseSequenceToken) segmentToken;
                List<PinyinDictItemToken> chineseTokens = chineseSequenceToken.getBody();
                List<String> chineseTokenPinyins = Pipeline.of(chineseTokens)
                        .map(new Function<PinyinDictItemToken, String>() {
                            @Override
                            public String apply(PinyinDictItemToken pinyinToken) {
                                PinyinDictItem item = pinyinToken.getBody();
                                if (outputStyle.isWithTone()) {
                                    return Strings.join(chineseCharSeparator, Strings.split(item.getPinyinWithTone(), " "));
                                } else {
                                    return Strings.join(chineseCharSeparator, Strings.split(item.getPinyinWithoutTone(), " "));
                                }
                            }
                        }).asList();

                segment = Strings.join(chineseTokenSeparator, chineseTokenPinyins);
                return segment;
            }
        }).clearNulls().asList();
        final String segmentSeparator = Objs.useValueIfNull(outputStyle.getSegmentSeparator(), "");
        String result = Strings.join(segmentSeparator, segments);
        return result;
    }

    private static List<SegmentToken> analyze(List<PinyinDict> dicts, String text, int tokenMaxWord, boolean surnameFirst) {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        dicts = Objs.isNotEmpty(dicts) ? dicts : PinyinDicts.allDicts();
        analyzer.setDicts(dicts);

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
        analyzer.setSurnameFirst(surnameFirst);
        List<SegmentToken> tokens = analyzer.analyze(text);
        return tokens;
    }


    private static final List<String> ENGLISH_PUNCTUATION_SYMBOLS = Collects.newArrayList(
            ".", "?", "!", ",", ":", "...", ";", "-", "_", "(", ")", "[", "]", "{", "}", "'", "\""
    );


    static boolean isEnglishPunctuationSymbol(String c) {
        return ENGLISH_PUNCTUATION_SYMBOLS.contains(c);
    }

    static boolean isChinesePunctuationSymbol(String c) {
        return PinyinDicts.getDict(PinyinDicts.DN_PUNCTUATION_SYMBOL).getItem(c) != null;
    }

}
