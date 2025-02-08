package com.jn.langx.text.transform;

import com.jn.langx.Transformer;
import com.jn.langx.text.StrTokenizer;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Function;

import java.util.List;

/**
 * TextCaseTransformer接口继承自Transformer接口，专门用于字符串到字符串的转换操作
 * 它定义了一个转换字符串的方法，允许实现者提供自定义的字符串转换逻辑
 */
public abstract class AbstractTokenTextCaseTransformer implements Transformer<String, String> {

    static final String[] default_delimiters = new String[]{" ", "-", "_"};
    private String[] delimiters = default_delimiters;

    private boolean cleanInvalidChars = true;

    private LetterCase tokenCase;

    private LetterCase tokenFirstLetterCase;

    private LetterCase firstLetterCase;

    private String joinSeparator;

    protected AbstractTokenTextCaseTransformer(String joinSeparator, LetterCase tokenCase, LetterCase tokenFirstLetterCase, LetterCase firstLetterCase) {
        this.joinSeparator = joinSeparator;
        this.tokenCase = Objs.useValueIfNull(tokenCase, LetterCase.NOOP);
        this.tokenFirstLetterCase = Objs.useValueIfNull(tokenFirstLetterCase, LetterCase.NOOP);
        this.firstLetterCase = Objs.useValueIfNull(firstLetterCase, LetterCase.NOOP);
    }

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
