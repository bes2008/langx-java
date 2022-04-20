package com.jn.langx.util.datetime.grok;

import com.jn.langx.text.grok.TemplatizedPattern;

public class DateTimeGrokPattern extends TemplatizedPattern {
    private String format;

    public DateTimeGrokPattern(String format, String grokExp){

    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
