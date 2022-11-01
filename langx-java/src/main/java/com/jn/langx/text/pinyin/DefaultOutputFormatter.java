package com.jn.langx.text.pinyin;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;

import java.util.List;

/**
 * @since 5.1.0
 */
class DefaultOutputFormatter extends OutputFormatter {
    public DefaultOutputFormatter() {
    }

    @Override
    public String format(List<SegmentToken> segmentTokens, Object... args) {
        final OutputStyle outputStyle = getOutputStyle() == null ? OutputStyle.DEFAULT_INSTANCE : getOutputStyle();


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
                List<Token> chineseTokens = chineseSequenceToken.getBody();
                List<String> chineseTokenPinyins = Pipeline.of(chineseTokens)
                        .map(new Function<Token, String>() {
                            @Override
                            public String apply(Token pinyinToken) {
                                if (pinyinToken instanceof StringToken) {
                                    // 汉字词典里找不到的情况
                                    return ((StringToken) pinyinToken).getBody();
                                } else {
                                    PinyinDictItem item = ((PinyinDictItemToken) pinyinToken).getBody();
                                    if (outputStyle.isWithTone()) {
                                        return Strings.join(chineseCharSeparator, Strings.split(item.getPinyinWithTone(), " "));
                                    } else {
                                        return Strings.join(chineseCharSeparator, Strings.split(item.getPinyinWithoutTone(), " "));
                                    }
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
}
