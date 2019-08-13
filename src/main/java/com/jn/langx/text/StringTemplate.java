package com.jn.langx.text;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTemplate {
    protected final static Pattern defaultPattern = Pattern.compile("\\{\\d+\\}");

    private Pattern variablePattern = defaultPattern;
    private String template;

    public StringTemplate variablePattern(String pattern) {
        if (Emptys.isNotEmpty(pattern)) {
            return variablePattern(Pattern.compile(pattern));
        }
        return this;
    }

    public StringTemplate variablePattern(Pattern pattern) {
        if (Emptys.isNotNull(pattern)) {
            this.variablePattern = pattern;
        }
        return this;
    }

    public StringTemplate using(String template) {
        Preconditions.checkNotNull(template);
        this.template = template;
        return this;
    }

    public String with(Object[] args) {
        if (Emptys.isNull(args)) {
            args = new Object[0];
        }

        Matcher matcher = variablePattern.matcher(this.template);
        StringBuffer b = new StringBuffer();
        int i = 0;
        while (matcher.find()) {
            matcher.appendReplacement(b, args[i++].toString());
        }
        matcher.appendTail(b);
        return b.toString();
    }

}
