package com.jn.langx.util.datetime.grok;

import com.jn.langx.text.grok.pattern.PatternDefinition;

public class DateTimePatternDefinition extends PatternDefinition {
    private String format;

    public DateTimePatternDefinition(String expression, String format) {
        super(format, expression);
        setFormat(format);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
