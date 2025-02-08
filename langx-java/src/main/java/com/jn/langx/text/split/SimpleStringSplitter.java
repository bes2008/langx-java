package com.jn.langx.text.split;

import com.jn.langx.text.StrTokenizer;
import com.jn.langx.util.Preconditions;
import java.util.List;

public class SimpleStringSplitter extends AbstractStringSplitter {
    protected String[] delimiters;
    private boolean returnDelimiter;
    public SimpleStringSplitter(boolean returnDelimiter, String... delimiters){
        this.returnDelimiter=returnDelimiter;
        this.delimiters = delimiters;
    }

    protected String beforeSplit(String str){
        Preconditions.checkNotEmpty(delimiters);
        return str;
    }

    @Override
    protected List<String> doSplit(String str) {
        StrTokenizer tokenizer = new StrTokenizer(str, this.returnDelimiter, delimiters);
        List<String> tokens = tokenizer.tokenize();
        return tokens;
    }

}
