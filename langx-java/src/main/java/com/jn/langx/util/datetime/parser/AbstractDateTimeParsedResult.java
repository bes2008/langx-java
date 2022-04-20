package com.jn.langx.util.datetime.parser;

import com.jn.langx.util.datetime.DateTimeParsedResult;

public abstract class AbstractDateTimeParsedResult implements DateTimeParsedResult {
    private String originText;
    private String formatPattern;

    @Override
    public String getOriginText() {
        return originText;
    }

    @Override
    public void setOriginText(String originText) {
        this.originText = originText;
    }

    @Override
    public String getFormatPattern() {
        return formatPattern;
    }

    @Override
    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }
}
