package com.jn.langx.util;

import com.jn.langx.text.tokenizer.CommonTokenizer;
import com.jn.langx.util.Objs;

/**
 * @since 5.1.0
 */
public class StringTokenizer extends CommonTokenizer {
    private String delimiters;

    public StringTokenizer(String str) {
        this(str, " \n\t");
    }

    public StringTokenizer(String str, String delimiters) {
        this(str, delimiters, false);
    }

    public StringTokenizer(String str, String delimiters, boolean returnDelimiter) {
        super(str, returnDelimiter);
        this.delimiters = Objs.useValueIfEmpty(delimiters, " \n\t");
    }

    @Override
    protected String getIfDelimiterStart(long position, char c) {
        String s = c + "";
        if (this.delimiters.contains(s)) {
            return s;
        }
        return null;
    }

    public String next(String delimiters) {
        this.delimiters = delimiters;
        return getNext();
    }

}
