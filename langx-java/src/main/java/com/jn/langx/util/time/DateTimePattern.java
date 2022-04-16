package com.jn.langx.util.time;

import com.jn.langx.text.grok.TemplatizedPattern;

public class DateTimePattern extends TemplatizedPattern {
    private String format;

    public DateTimePattern(String format, String grokExp){

    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
