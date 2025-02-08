package com.jn.langx.text.transform;

import com.jn.langx.text.StrTokenizer;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.struct.Holder;

import java.util.List;

public class TextToCamelCaseTransformer implements TextCaseTransformer {

    private static final String[] default_delimiters = new String[]{"\t"," ","\r","\n","-","_"};
    private boolean firstLetterUpperCase;
    private String[] delimiters = default_delimiters;
    public TextToCamelCaseTransformer(){
        this(false);
    }
    public TextToCamelCaseTransformer(boolean firstLetterUpperCase){
        this.firstLetterUpperCase = firstLetterUpperCase;
    }

    public void setDelimiters(String[] delimiters) {
        if(Objs.isNotEmpty(delimiters)) {
            this.delimiters = delimiters;
        }
    }

    @Override
    public String transform(final String text) {
        // 前置处理
        StringBuilder newText = new StringBuilder();
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ((Character.isDigit(c)&& count>0) || Character.isLetter(c) || Collects.contains(delimiters,c+"")) {
                newText.append(c);
                count++;
            }
        }

        // 转为驼峰
        StrTokenizer tokenizer = new StrTokenizer(newText.toString(),false, delimiters);
        List<String> tokens = tokenizer.tokenize();
        final Holder<Integer> indexHolder = new Holder<Integer>(0);
        String result = Strings.join("", null, null, tokens, new Function<String, String>() {
            @Override
            public String apply(String token) {
                token = Strings.lowerCase(token);
                if (indexHolder.get() == 0) {
                    if (firstLetterUpperCase) {
                        return Strings.upperCaseFirstChar(token);
                    } else {
                        return token;
                    }
                } else {
                    return Strings.upperCaseFirstChar(token);
                }
            }
        }, null);
        return result;
    }
}
