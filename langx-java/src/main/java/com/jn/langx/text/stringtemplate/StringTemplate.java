package com.jn.langx.text.stringtemplate;

import com.jn.langx.util.*;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;
import org.slf4j.Logger;

import java.util.regex.Pattern;

import static java.util.regex.Matcher.quoteReplacement;

public class StringTemplate {
    /**
     * index pattern
     */
    public final static Regexp defaultPattern = Regexps.createRegexp("\\{\\d+}");
    private final static Function2<String, Object[], String> defaultValueGetter = new IndexBasedValueGetter();

    private Regexp variableRegexp = defaultPattern;
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
            this.variableRegexp = defaultPattern;
        }
        return this;
    }
    public StringTemplate variablePattern(Regexp regexp){
        if (Emptys.isNotNull(regexp)) {
            this.variableRegexp = defaultPattern;
        }
        return this;
    }


    public StringTemplate using(String template) {
        Preconditions.checkNotNull(template);
        this.template = template;
        return this;
    }

    /**
     * set a value getter
     *
     * @param valueGetter apply(String matched, Object[] args)
     */
    public StringTemplate with(Function2<String, Object[], String> valueGetter) {
        if (valueGetter != null) {
            this.valueGetter = valueGetter;
        }
        if (variableRegexp == defaultPattern) {
            this.valueGetter = defaultValueGetter;
        }
        return this;
    }

    public String format(Object[] args) {
        if (Emptys.isNull(args)) {
            args = new Object[0];
        }

        RegexpMatcher matcher = variableRegexp.matcher(this.template);
        StringBuffer b = new StringBuffer();
        Logger logger = Loggers.getLogger(getClass());
        while (matcher.find()) {
            final String matched = matcher.group();
            String value = Throwables.ignoreThrowable(logger, matched, new ThrowableFunction<Object[], String>() {
                @Override
                public String doFun(Object[] args) throws Throwable {
                    return valueGetter.apply(matched, args);
                }
            }, args);
            value = Objs.isNull(value) ? matched : value;
            matcher.appendReplacement(b, quoteReplacement(value));
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
            String indexString = matched;
            if(matched.startsWith("{") && matched.endsWith("}")) {
                indexString = matched.substring(1, matched.length() - 1);
            }
            int index = Integer.parseInt(indexString);
            if (index < 0) {
                index = 0;
            }
            return index;
        }
    }
}
