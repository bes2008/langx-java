package com.jn.langx.text.transform;

import com.jn.langx.text.split.SimpleStringSplitter;
import com.jn.langx.util.collection.Collects;

public class TokenCaseStringSplitter extends SimpleStringSplitter {
    private boolean cleanInvalidChars;
    public TokenCaseStringSplitter( String... delimiters){
        this(true, false, delimiters);
    }
    public TokenCaseStringSplitter(boolean returnDelimiter, String... delimiters){
        this(true, returnDelimiter, delimiters);
    }

    public TokenCaseStringSplitter(boolean cleanInvalidChars,boolean returnDelimiter, String... delimiters){
        super(returnDelimiter, delimiters);
        this.cleanInvalidChars=cleanInvalidChars;
    }

    @Override
    protected String beforeSplit(String text) {
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
        return newText.toString();
    }
}
