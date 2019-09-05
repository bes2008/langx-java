package com.jn.langx.text;

import com.jn.langx.util.function.Function2;

import java.util.regex.Pattern;

/**
 * pattern: {0},{1},{2},{3}
 * start index: 0
 */
public class IndexStringFormatter extends CustomPatternStringFormatter {

    public IndexStringFormatter() {
        setVariablePattern(null);
        setValueGetter(null);
    }

    @Override
    public void setVariablePattern(Pattern variablePattern) {
        super.setVariablePattern(StringTemplate.defaultPattern);
    }

    @Override
    public void setValueGetter(Function2<String, Object[], String> valueGetter) {
        super.setValueGetter(new StringTemplate.IndexBasedValueGetter());
    }

}
