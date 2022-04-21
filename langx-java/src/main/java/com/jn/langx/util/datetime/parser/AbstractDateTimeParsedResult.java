package com.jn.langx.util.datetime.parser;

import com.jn.langx.util.datetime.DateTimeParsedResult;

public abstract class AbstractDateTimeParsedResult implements DateTimeParsedResult {
    private String originText;
    private String pattern;

    @Override
    public String getOriginText() {
        return originText;
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String formatPattern) {
        this.pattern = formatPattern;
    }
}
