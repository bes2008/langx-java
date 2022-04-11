package com.jn.langx.text.grok;

import com.jn.langx.Converter;
import com.jn.langx.util.Strings;
import com.jn.langx.util.regexp.Regexp;

import java.util.Map;
import java.util.Set;

/**
 * @since 4.5.0
 */
public class TemplatizedPattern {
    /**
     * 原始表达式
     */
    private String expression;

    /**
     * 合并后的pattern
     */
    private Regexp regexp;
    private Set<String> fields;
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

    public Set<String> getFields() {
        return fields;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }

    public Object convert(String field, String originalValue){
        if(expectedConverters!=null){
            Converter converter = expectedConverters.get(field);
            if(converter!=null){
                return converter.apply(originalValue);
            }
        }
        return originalValue;
    }

    @Override
    public String toString() {
        return Strings.replaceChars(expression,"\\","\\\\");
    }
}
