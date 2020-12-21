package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.pattern.AbstractPatternMatcher;

public abstract class AbstractPatternSetMatcher<PatternEntry extends Named> extends AbstractPatternMatcher {
    protected PatternSet<PatternEntry> defaultPatternSet;
    /**
     * 只在使用 #setDefaultExpression, #setPatternExpression 时，要求该字段不能为null
     */
    @Nullable
    private PatternSetExpressionParser<PatternEntry> expressionParser;
    protected PatternSet<PatternEntry> patternSet;

    public AbstractPatternSetMatcher(@Nullable PatternSetExpressionParser<PatternEntry> expressionParser, @NonNull String defaultPatternExpression) {
        setExpressionParser(expressionParser);
        setDefaultExpression(defaultPatternExpression);
    }

    public AbstractPatternSetMatcher(@NonNull PatternSetExpressionParser<PatternEntry> expressionParser, @NonNull PatternSet<PatternEntry> defaultPatternSet) {
        setExpressionParser(expressionParser);
        setDefaultPatternSet(defaultPatternSet);
    }

    public AbstractPatternSetMatcher(@NonNull PatternSetExpressionParser<PatternEntry> expressionParser, @NonNull PatternSet<PatternEntry> defaultPatternSet, PatternSet patternSet) {
        setExpressionParser(expressionParser);
        setDefaultPatternSet(defaultPatternSet);
        setPatternSet(patternSet);
    }

    public AbstractPatternSetMatcher(@NonNull PatternSetExpressionParser<PatternEntry> expressionParser, @NonNull PatternSet<PatternEntry> defaultPatternSet, String patternSetExpression) {
        setExpressionParser(expressionParser);
        setDefaultPatternSet(defaultPatternSet);
        setPatternExpression(patternSetExpression);
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
            Preconditions.checkNotNull(expressionParser, "the expression parser is null");
            setDefaultPatternSet(expressionParser.parse(defaultExpression));
        }
    }

    public void setPatternSet(PatternSet<PatternEntry> patternSet) {
        this.patternSet = patternSet;
    }

    public void setPatternExpression(String expression) {
        if (Strings.isNotEmpty(expression)) {
            Preconditions.checkNotNull(expressionParser, "the expression parser is null");
            setPatternSet(patternSet);
        }
    }
}
