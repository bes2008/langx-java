package com.jn.langx.text.grok;

import com.jn.langx.Converter;
import com.jn.langx.util.regexp.Regexp;

import java.util.Map;

public class TemplatizedPattern {
    /**
     * 原始表达式
     */
    private String expression;

    /**
     * 合并后的pattern
     */
    private Regexp regexp;

    private Map<String, Converter> expectedConverters;


    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Regexp getRegexp() {
        return regexp;
    }

    public void setRegexp(Regexp regexp) {
        this.regexp = regexp;
    }

    public Map<String, Converter> getExpectedConverters() {
        return expectedConverters;
    }

    public void setExpectedConverters(Map<String, Converter> expectedConverters) {
        this.expectedConverters = expectedConverters;
    }

}
