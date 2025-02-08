package com.jn.langx.text.split;

import com.jn.langx.text.StrTokenizer;
import com.jn.langx.text.StringSplitter;
import com.jn.langx.util.Preconditions;

import java.util.List;

public class SimpleStringSplitter implements StringSplitter {
    protected String[] delimiters;
    private boolean returnDelimiter;
    public SimpleStringSplitter(boolean returnDelimiter, String... delimiters){
        this.returnDelimiter=returnDelimiter;
        this.delimiters = delimiters;
    }

    @Override
    public List<String> split(String str) {
        Preconditions.checkNotNull(str);
        Preconditions.checkNotEmpty(delimiters);
        str = beforeSplit(str);
        StrTokenizer tokenizer = new StrTokenizer(str, this.returnDelimiter, delimiters);
        List<String> tokens = tokenizer.tokenize();
        return tokens;
    }

    protected String beforeSplit(String str){
        return str;
    }
}
