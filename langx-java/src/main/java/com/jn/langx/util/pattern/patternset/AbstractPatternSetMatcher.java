package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;
import com.jn.langx.util.pattern.AbstractPatternMatcher;

public abstract class AbstractPatternSetMatcher<PatternEntry extends Named> extends AbstractPatternMatcher {
    protected PatternSet<PatternEntry> patternSet;

    public PatternSet<PatternEntry> getPatternSet() {
        return patternSet;
    }

    public void setPatternSet(PatternSet<PatternEntry> patternSet) {
        this.patternSet = patternSet;
    }
}
