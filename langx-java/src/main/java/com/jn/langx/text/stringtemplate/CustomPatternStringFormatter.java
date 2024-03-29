package com.jn.langx.text.stringtemplate;

import com.jn.langx.util.function.Function2;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;



public class CustomPatternStringFormatter implements StringTemplateFormatter {
    private Regexp variableRegexp;
    private Function2<String, Object[], String> valueGetter;

    public CustomPatternStringFormatter() {
    }

    public CustomPatternStringFormatter(Regexp variableRegexp, Function2<String, Object[], String> valueGetter) {
        setVariablePattern(variableRegexp);
        setValueGetter(valueGetter);
    }

    public CustomPatternStringFormatter(String pattern, Function2<String, Object[], String> valueGetter) {
        this(Regexps.compile(pattern), valueGetter);
    }


    @Override
    public String format(String template, Object... args) {
        return new StringTemplate().variablePattern(variableRegexp).using(template).with(valueGetter).format(args);
    }

    public Regexp getVariablePattern() {
        return variableRegexp;
    }


    public void setVariablePattern(Regexp variablePattern) {
        this.variableRegexp = variablePattern;
    }

    public Function2<String, Object[], String> getValueGetter() {
        return valueGetter;
    }

    public void setValueGetter(Function2<String, Object[], String> valueGetter) {
        this.valueGetter = valueGetter;
    }
}
