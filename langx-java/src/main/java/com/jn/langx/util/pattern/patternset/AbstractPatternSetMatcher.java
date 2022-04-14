package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.pattern.AbstractPatternMatcher;

public abstract class AbstractPatternSetMatcher<PatternEntry extends Named> extends AbstractPatternMatcher {
    @Nullable
    private PatternSet<PatternEntry> defaultPatternSet;
    /**
     * 只在使用 #setDefaultExpression, #setPatternExpression 时，要求该字段不能为null
     */
    @Nullable
    private PatternSetExpressionParser<PatternEntry> expressionParser;
    @Nullable
    private PatternSet<PatternEntry> patternSet;

    public AbstractPatternSetMatcher(@Nullable PatternSetExpressionParser<PatternEntry> expressionParser, @Nullable String defaultPatternExpression) {
        setExpressionParser(expressionParser);
        setDefaultExpression(defaultPatternExpression);
    }

    public AbstractPatternSetMatcher(@NonNull PatternSetExpressionParser<PatternEntry> expressionParser, @Nullable PatternSet<PatternEntry> defaultPatternSet) {
        setExpressionParser(expressionParser);
        setDefaultPatternSet(defaultPatternSet);
    }

    public AbstractPatternSetMatcher(@NonNull PatternSetExpressionParser<PatternEntry> expressionParser, @Nullable PatternSet<PatternEntry> defaultPatternSet, PatternSet<PatternEntry> patternSet) {
        setExpressionParser(expressionParser);
        setDefaultPatternSet(defaultPatternSet);
        setPatternSet(patternSet);
    }

    public AbstractPatternSetMatcher(@NonNull PatternSetExpressionParser<PatternEntry> expressionParser, @Nullable String defaultPatternSet, String patternSetExpression) {
        setExpressionParser(expressionParser);
        setDefaultExpression(defaultPatternSet);
        setPatternExpression(patternSetExpression);
    }

    public void setExpressionParser(PatternSetExpressionParser<PatternEntry> expressionParser) {
        Preconditions.checkNotNull(expressionParser);
        this.expressionParser = expressionParser;
    }

    public PatternSet<PatternEntry> getDefaultPatternSet() {
        return defaultPatternSet;
    }

    public void setDefaultPatternSet(@Nullable PatternSet<PatternEntry> defaultPatternSet) {
        this.defaultPatternSet = defaultPatternSet;
    }

    public void setDefaultExpression(@Nullable String defaultExpression) {
        if (Strings.isNotEmpty(defaultExpression)) {
            Preconditions.checkNotNull(expressionParser, "the expression parser is null");
            setDefaultPatternSet(expressionParser.parse(defaultExpression));
        }
    }

    public void setPatternSet(PatternSet<PatternEntry> patternSet) {
        this.patternSet = patternSet;
    }

    public void setPatternExpression(@Nullable String expression) {
        if (Strings.isNotEmpty(expression)) {
            Preconditions.checkNotNull(expressionParser, "the expression parser is null");
            setPatternSet(expressionParser.parse(expression));
        }
    }

    @Override
    public boolean match(final String string) {
        PatternSet patternSet = defaultPatternSet;
        if (Emptys.isNotEmpty(this.patternSet)) {
            patternSet = this.patternSet;
        }

        if (Emptys.isEmpty(patternSet)) {
            throw new IllegalStateException("has no any pattern");
        }

        boolean matched = Collects.anyMatch(this.patternSet.getIncludes(), new Predicate<PatternEntry>() {
            @Override
            public boolean test(PatternEntry patternEntry) {
                return doMatch(patternEntry.getName(), string, option.isGlobal());
            }
        });

        if (matched) {
            matched = Collects.noneMatch(this.patternSet.getExcludes(), new Predicate<PatternEntry>() {
                @Override
                public boolean test(PatternEntry patternEntry) {
                    return doMatch(patternEntry.getName(), string, option.isGlobal());
                }
            });
        }

        return matched;
    }

    protected abstract boolean doMatch(String pattern, String string, boolean fullMatch);
}
