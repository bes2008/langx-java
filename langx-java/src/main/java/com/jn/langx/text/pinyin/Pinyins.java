package com.jn.langx.text.pinyin;

import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;

import java.util.Comparator;
import java.util.List;

/**
 * @since 5.1.0
 */
public class Pinyins extends PinyinDicts {

    public static String getPersonName(String name, OutputStyle theOutputStyle) {
        return getPinyin(name, 4, true, theOutputStyle, null);
    }

    public static String getPinyin(String text) {
        return getPinyin(text, null);
    }


    public static String getPinyin(String text, OutputStyle theOutputStyle, String... dictNames) {
        return getPinyin(text, 50, false, theOutputStyle, null, dictNames);
    }

    public static String getPinyin(String text, int tokenMaxWord, OutputStyle theOutputStyle, String... dictNames) {
        return getPinyin(text, tokenMaxWord, false, theOutputStyle, null, dictNames);
    }

    public static String getPinyin(String text, int tokenMaxWord, OutputStyle theOutputStyle, OutputFormatter outputFormatter, String... dictNames) {
        return getPinyin(text, tokenMaxWord, false, theOutputStyle, outputFormatter, dictNames);
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
    public static String getPinyin(String text, int tokenMaxWord, boolean surnameFirst, OutputStyle theOutputStyle, OutputFormatter outputFormatter, String... dictNames) {
        List<SegmentToken> segmentTokens = analyze(PinyinDicts.findDicts(dictNames), text, tokenMaxWord, surnameFirst);
        if (outputFormatter == null) {
            outputFormatter = new DefaultOutputFormatter();
        }
        outputFormatter.setOutputStyle(theOutputStyle);
        String result = outputFormatter.format(segmentTokens);
        return result;
    }

    private static List<SegmentToken> analyze(List<PinyinDict> dicts, String text, int tokenMaxWord, boolean surnameFirst) {
        dicts = Objs.isNotEmpty(dicts) ? dicts : PinyinDicts.allDicts();
        PinyinTokenizationConfig config = new PinyinTokenizationConfig();
        config.setDicts(dicts);

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
        config.setTokenMaxChar(tokenMaxWord);
        config.setSurnameFirst(surnameFirst);
       // PinyinTokenizer pinyinTokenizer = new PinyinTokenizer(text, config);
        LTokenizer pinyinTokenizer = new LTokenizer(text,config);
        List<SegmentToken> tokens = pinyinTokenizer.tokenize();
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
