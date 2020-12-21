package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.pattern.AbstractPatternMatcher;

public abstract class AbstractPatternSetMatcher<PatternEntry extends Named> extends AbstractPatternMatcher {
    protected PatternSet<PatternEntry> defaultPatternSet;
    private PatternSetExpressionParser<PatternEntry> expressionParser;

    public AbstractPatternSetMatcher(@NonNull PatternSetExpressionParser<PatternEntry> expressionParser, @NonNull String defaultPatternExpression) {
        setExpressionParser(expressionParser);
        setDefaultExpression(defaultPatternExpression);
    }

    public AbstractPatternSetMatcher(@NonNull PatternSetExpressionParser<PatternEntry> expressionParser, @NonNull PatternSet<PatternEntry> defaultPatternSet) {
        setExpressionParser(expressionParser);
        setDefaultPatternSet(defaultPatternSet);
    }

    public void setExpressionParser(PatternSetExpressionParser<PatternEntry> expressionParser) {
        Preconditions.checkNotNull(expressionParser);
        this.expressionParser = expressionParser;
    }

    public PatternSet<PatternEntry> getDefaultPatternSet() {
        return defaultPatternSet;
    }

    public void setDefaultPatternSet(PatternSet<PatternEntry> defaultPatternSet) {
        this.defaultPatternSet = defaultPatternSet;
    }

    public void setDefaultExpression(String defaultExpression) {
        if (Strings.isNotEmpty(defaultExpression)) {
            this.defaultPatternSet = expressionParser.parse(defaultExpression);
        }
    }

}
