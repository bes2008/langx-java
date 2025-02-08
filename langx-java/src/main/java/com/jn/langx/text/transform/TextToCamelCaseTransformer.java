package com.jn.langx.text.transform;

import com.jn.langx.text.StrTokenizer;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.struct.Holder;

import java.util.List;

public class TextToCamelCaseTransformer extends AbstractTextCaseTransformer {


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
    protected String transformInternal(String text) {
        // 转为驼峰
        StrTokenizer tokenizer = new StrTokenizer(text,false, delimiters);
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
