package com.jn.langx.text.stringtemplate;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

/**
 * placeholder: {}
 */
public class PlaceholderStringFormatter extends CustomPatternStringFormatter {
    private static final Regexp orderPattern = Regexps.compile("\\{}");

    public PlaceholderStringFormatter() {
        setValueGetter(null);
        setVariablePattern(orderPattern);
    }

    @Override
    public void setValueGetter(Function2<String, Object[], String> valueGetter) {
        super.setValueGetter(new Function2<String, Object[], String>() {
            int index = -1;

            @Override
            public String apply(String matched, Object[] args) {
                index++;
                Object value = args[index];
                return Emptys.isNull(value) ? "" : value.toString();
            }
        });
    }

    @Override
    public void setVariablePattern(Regexp variablePattern) {
        super.setVariablePattern(orderPattern);
    }
}