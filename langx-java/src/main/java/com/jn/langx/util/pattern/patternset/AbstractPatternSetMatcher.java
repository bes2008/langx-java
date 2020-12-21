package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;
import com.jn.langx.util.pattern.PatternMatcher;

public abstract class AbstractPatternSetMatcher<PatternEntry extends Named> implements PatternMatcher {
    private PatternSetExpressionParser<PatternEntry> parser;

    public void setParser(PatternSetExpressionParser<PatternEntry> parser) {
        this.parser = parser;
    }

}
