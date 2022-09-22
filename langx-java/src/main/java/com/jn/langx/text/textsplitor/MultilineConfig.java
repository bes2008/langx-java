package com.jn.langx.text.textsplitor;

import com.jn.langx.util.Objs;

/**
 * @since 5.0.1
 */
public class MultilineConfig {
    public static enum Match {
        BEFORE,
        AFTER;
    }

    private String pattern;
    private boolean negate;
    private Match match;
    private String concatSeparator = "\n";

    public MultilineConfig() {
    }

    public MultilineConfig(String pattern) {
        this(pattern, true, Match.AFTER);
    }

    public MultilineConfig(String pattern, boolean negate, Match match) {
        this(pattern, negate, match, null);
    }

    public MultilineConfig(String pattern, boolean negate, Match match, String concatSeparator) {
        this.pattern = pattern;
        this.negate = negate;
        this.match = match == null ? Match.AFTER : match;
        this.concatSeparator = Objs.isNull(concatSeparator) ? "\n" : concatSeparator;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getConcatSeparator() {
        return concatSeparator;
    }

    public void setConcatSeparator(String concatSeparator) {
        this.concatSeparator = concatSeparator;
    }
}
