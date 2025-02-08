package com.jn.langx.text.transform;

import com.jn.langx.text.StrTokenizer;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
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
        // 前置处理
        StringBuilder newText = new StringBuilder();
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ((Character.isDigit(c)&& count>0) || Character.isLetter(c) || Collects.contains(default_delimiters,c+"")) {
                newText.append(c);
                count++;
            }
        }

        // 转换
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
