package com.jn.langx.text;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Function2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTemplate {
    private final static Pattern defaultPattern = Pattern.compile("\\{\\d+\\}");
    private final static Function2<String, Object[], String> defaultValueGetter = new IndexBasedValueGetter();

    private Pattern variablePattern = defaultPattern;
    private String template;
    private Function2<String, Object[], String> valueGetter = defaultValueGetter;

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

    public StringTemplate with(Function2<String, Object[], String> valueGetter) {
        if (valueGetter != null) {
            this.valueGetter = valueGetter;
        }
        if (variablePattern == defaultPattern) {
            this.valueGetter = defaultValueGetter;
        }
        return this;
    }

    public String format(Object[] args) {
        if (Emptys.isNull(args)) {
            args = new Object[0];
        }

        Matcher matcher = variablePattern.matcher(this.template);
        StringBuffer b = new StringBuffer();
        while (matcher.find()) {
            String matched = matcher.group();
            matcher.appendReplacement(b, valueGetter.apply(matched, args));
        }
        matcher.appendTail(b);
        return b.toString();
    }


    public static class IndexBasedValueGetter implements Function2<String, Object[], String> {
        @Override
        public String apply(String matched, Object[] args) {
            Object object = args[getIndex(matched)];
            return Emptys.isNull(object) ? "" : object.toString();
        }

        private int getIndex(String matched) {
            String indexString = matched.substring(1, matched.length() - 1);
            int index = Integer.parseInt(indexString);
            if (index < 0) {
                index = 0;
            }
            return index;
        }
    }
}
