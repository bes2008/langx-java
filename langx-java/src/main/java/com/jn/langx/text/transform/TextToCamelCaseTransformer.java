package com.jn.langx.text.transform;

import com.jn.langx.text.StrTokenizer;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.struct.Holder;

import java.util.List;

public class TextToCamelCaseTransformer implements TextCaseTransformer {
    private boolean firstLetterUpperCase;
    private String[] delimiters = new String[]{"\t"," ","\r","\n","-","_"};
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
        StrTokenizer tokenizer = new StrTokenizer(text,false, delimiters);
        List<String> tokens = tokenizer.tokenize();
        final Holder<Integer> indexHolder = new Holder<Integer>(0);
        String result = Strings.join("", null, null, tokens, new Function<String, String>() {
            @Override
            public String apply(String token) {
                if (indexHolder.get() == 0) {
                    if (firstLetterUpperCase) {
                        return Strings.upperCaseFirstChar(token);
                    } else {
                        return Strings.lowerCase(token);
                    }
                } else {
                    return Strings.upperCaseFirstChar(token);
                }
            }
        }, null);
        return result;
    }
}
