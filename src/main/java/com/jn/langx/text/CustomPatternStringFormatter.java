package com.jn.langx.text;

import com.jn.langx.util.function.Function2;

import java.util.regex.Pattern;

public class CustomPatternStringFormatter extends AbstractStringTemplateFormatter {
    private Pattern variablePattern;
    private Function2<String, Object[], String> valueGetter;

    public CustomPatternStringFormatter() {
    }

    public CustomPatternStringFormatter(Pattern pattern, Function2<String, Object[], String> valueGetter) {
        setVariablePattern(pattern);
        setValueGetter(valueGetter);
    }

    public CustomPatternStringFormatter(String pattern, Function2<String, Object[], String> valueGetter) {
        this(Pattern.compile(pattern), valueGetter);
    }


    @Override
    public String format(String template, Object... args) {
        return new StringTemplate().variablePattern(variablePattern).using(template).with(valueGetter).format(args);
    }

    public Pattern getVariablePattern() {
        return variablePattern;
    }

    public void setVariablePattern(Pattern variablePattern) {
        this.variablePattern = variablePattern;
    }

    public Function2<String, Object[], String> getValueGetter() {
        return valueGetter;
    }

    public void setValueGetter(Function2<String, Object[], String> valueGetter) {
        this.valueGetter = valueGetter;
    }
}
