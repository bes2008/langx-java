package com.jn.langx.text;

import com.jn.langx.Accessor;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.reflect.FieldAccessor;

import java.util.regex.Pattern;

public class BeanBasedStyleStringFormatter extends CustomPatternStringFormatter {
    static final Pattern BEAN_VARIABLE_PATTERN = Pattern.compile("\\$\\{[a-zA-Z_]\\w*}");


    public BeanBasedStyleStringFormatter() {
        super(BEAN_VARIABLE_PATTERN, new FieldValueGetter());
    }

    private static class FieldValueGetter implements Function2<String, Object[], String> {
        @Override
        public String apply(String matched, Object[] args) {
            Preconditions.checkNotNull(args);
            if(matched.startsWith("${") && matched.endsWith("}")) {
                matched = matched.substring(2, matched.length() - 1);
            }
            Accessor<String, ?> accessor = new FieldAccessor(args[0]);
            return accessor.getString(matched);
        }
    }
}
