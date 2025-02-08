package com.jn.langx.text.transform;

import com.jn.langx.text.StrTokenizer;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Function;

import java.util.List;

public class TextToHyphenCaseTransformer implements TextCaseTransformer {
    private String[] delimiters = new String[]{"\t"," ","\r","\n"};

    private boolean lowerCase;

    public TextToHyphenCaseTransformer(){
        this(false);
    }

    public TextToHyphenCaseTransformer(boolean lowerCase){
        this.lowerCase = lowerCase;
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
        String result = Strings.join("-", null, null, tokens, new Function<String, String>() {
            @Override
            public String apply(String token) {
                return lowerCase ? Strings.lowerCase(token) :token ;
            }
        }, null);
        return result;
    }
}
