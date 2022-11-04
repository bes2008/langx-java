package com.jn.langx.text.pinyin;

import com.jn.langx.Formatter;

import java.util.List;

/**
 * @since 5.1.0
 */
public abstract class OutputFormatter implements Formatter<List<RegionToken>, String> {
    @Override
    public abstract String format(List<RegionToken> tokens, Object... args);

    private OutputStyle outputStyle = OutputStyle.DEFAULT_INSTANCE;

    public OutputStyle getOutputStyle() {
        return outputStyle;
    }

    public void setOutputStyle(OutputStyle outputStyle) {
        if (outputStyle != null) {
            this.outputStyle = outputStyle;
        }
    }
}
