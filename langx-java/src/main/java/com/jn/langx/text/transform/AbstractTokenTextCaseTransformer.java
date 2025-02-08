package com.jn.langx.text.transform;

import com.jn.langx.Transformer;
import com.jn.langx.text.StrTokenizer;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Function;

import java.util.List;

/**
 * 抽象的令牌文本大小写转换器类，用于转换字符串的大小写格式
 * 该类实现了Transformer接口，专门用于处理字符串类型的输入和输出
 */
public abstract class AbstractTokenTextCaseTransformer implements Transformer<String, String> {

    // 默认的分隔符数组，用于分割字符串
    static final String[] default_delimiters = new String[]{" ", "-", "_"};
    // 可以被设置的分隔符，默认使用default_delimiters
    private String[] delimiters = default_delimiters;

    // 是否清理无效字符，默认为true
    private boolean cleanInvalidChars = true;

    // 令牌（单词）的大小写格式
    private LetterCase tokenCase;

    // 令牌（单词）首字母的大小写格式
    private LetterCase tokenFirstLetterCase;

    // 整个字符串首字母的大小写格式
    private LetterCase firstLetterCase;

    // 用于连接处理后的令牌（单词）的分隔符
    private String joinSeparator;

    /**
     * 构造方法，初始化配置
     *
     * @param joinSeparator 用于连接处理后的令牌的分隔符
     * @param tokenCase 令牌的大小写格式
     * @param tokenFirstLetterCase 令牌首字母的大小写格式
     * @param firstLetterCase 整个字符串首字母的大小写格式
     */
    protected AbstractTokenTextCaseTransformer(String joinSeparator, LetterCase tokenCase, LetterCase tokenFirstLetterCase, LetterCase firstLetterCase) {
        this.joinSeparator = joinSeparator;
        this.tokenCase = Objs.useValueIfNull(tokenCase, LetterCase.NOOP);
        this.tokenFirstLetterCase = Objs.useValueIfNull(tokenFirstLetterCase, LetterCase.NOOP);
        this.firstLetterCase = Objs.useValueIfNull(firstLetterCase, LetterCase.NOOP);
    }

    /**
     * 设置分隔符
     *
     * @param delimiters 新的分隔符数组
     */
    public void setDelimiters(String[] delimiters) {
        if (Objs.isNotEmpty(delimiters)) {
            this.delimiters = delimiters;
        }
    }

    /**
     * 转换输入的字符串
     *
     * @param text 待转换的原始字符串
     * @return 转换后的字符串
     */
    @Override
    public String transform(String text) {
        // 前置处理
        StringBuilder newText = new StringBuilder();
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ((Character.isDigit(c) && count > 0) || Character.isLetter(c)) {
                newText.append(c);
                count++;
            } else if (count > 0) {
                if (Collects.contains(delimiters, "" + c)) {
                    newText.append(c);
                } else if(cleanInvalidChars){
                    newText.append(delimiters[0]);
                }
            }
        }
        return transformInternal(newText.toString());
    }

    /**
     * 内部转换方法，处理字符串的大小写和分隔符
     *
     * @param text 预处理后的字符串
     * @return 最终转换后的字符串
     */
    protected String transformInternal(String text) {
        StrTokenizer tokenizer = new StrTokenizer(text, false, delimiters);
        List<String> tokens = tokenizer.tokenize();

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

