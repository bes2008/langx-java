package com.jn.langx.util.time.parser;

import com.jn.langx.util.time.DateTimeParsedResult;

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
