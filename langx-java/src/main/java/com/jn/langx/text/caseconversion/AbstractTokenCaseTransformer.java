package com.jn.langx.text.caseconversion;

import com.jn.langx.Transformer;
import com.jn.langx.text.split.StringSplitter;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Function;

import java.util.List;

/**
 * 提供基于token的处理方式
 * 该类实现了Transformer接口，专门用于处理字符串类型的输入和输出
 */
public abstract class AbstractTokenCaseTransformer implements Transformer<String, String> {

    private StringSplitter splitter;

    // 令牌（单词）的大小写格式
    private LetterCase tokenCase;

    // 令牌（单词）首字母的大小写格式
    private LetterCase tokenFirstLetterCase;

    // 整个字符串首字母的大小写格式
    private LetterCase firstLetterCase;

    // 连接符
    private String joinSeparator;

    /**
     * 构造方法，初始化配置
     *
     * @param joinSeparator 用于连接处理时连接符
     * @param tokenCase token的大小写格式
     * @param tokenFirstLetterCase token首字母的大小写格式
     * @param firstLetterCase 整个字符串首字母的大小写格式
     */
    protected AbstractTokenCaseTransformer(String joinSeparator, LetterCase tokenCase, LetterCase tokenFirstLetterCase, LetterCase firstLetterCase) {
        this.joinSeparator = joinSeparator;
        this.tokenCase = Objs.useValueIfNull(tokenCase, LetterCase.NOOP);
        this.tokenFirstLetterCase = Objs.useValueIfNull(tokenFirstLetterCase, LetterCase.NOOP);
        this.firstLetterCase = Objs.useValueIfNull(firstLetterCase, LetterCase.NOOP);
        this.splitter = new TokenCaseStringSplitter(false);
    }

    public void setSplitter(StringSplitter splitter) {
        this.splitter = splitter;
    }

    /**
     * 转换输入的字符串
     *
     * @param text 待转换的原始字符串
     * @return 转换后的字符串
     */
    @Override
    public String transform(String text) {
        List<String> tokens = splitter.split(text);
        return transformInternal(tokens);
    }


    protected String transformInternal(List<String> tokens) {

        String result = Strings.join(this.joinSeparator, null, null, tokens, new Function<String, String>() {
            @Override
            public String apply(String token) {
                if (tokenCase != LetterCase.NOOP) {
                    token = tokenCase == LetterCase.LOWER ? Strings.lowerCase(token) : Strings.upperCase(token);
                }
                if (tokenFirstLetterCase != LetterCase.NOOP) {
                    token = tokenFirstLetterCase == LetterCase.LOWER ? Strings.lowerCaseFirstChar(token) : Strings.upperCaseFirstChar(token);
                }
                return token;
            }
        }, null);

        if (firstLetterCase != LetterCase.NOOP) {
            result = firstLetterCase == LetterCase.UPPER ? Strings.upperCaseFirstChar(result) : Strings.lowerCaseFirstChar(result);
        }
        return result;
    }
}

