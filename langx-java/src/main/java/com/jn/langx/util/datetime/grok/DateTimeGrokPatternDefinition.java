package com.jn.langx.util.datetime.grok;

import com.jn.langx.text.grok.pattern.PatternDefinition;

class DateTimeGrokPatternDefinition extends PatternDefinition {
    private String format;

    public DateTimeGrokPatternDefinition(String expression, String format) {
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
