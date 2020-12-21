package com.jn.langx.util.pattern.patternset;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

/**
 * <pre>
 * ?	匹配任何单字符
 * *	匹配0或者任意数量的字符
 * **	匹配0或者更多的目录
 * </pre>
 */
public class AntStringMatcher extends AbstractPatternSetMatcher<StringPatternEntry> {

    public AntStringMatcher(@NonNull String defaultPatternExpression) {
        this(new GenericPatternSetExpressionParser<StringPatternEntry>(new StringPatternEntry.Factory()), defaultPatternExpression);
    }

    public AntStringMatcher(String defaultPatternExpression, @NonNull String patternExpression) {
        super(new GenericPatternSetExpressionParser<StringPatternEntry>(new StringPatternEntry.Factory()), defaultPatternExpression, patternExpression);
    }

    public AntStringMatcher(@NonNull PatternSetExpressionParser<StringPatternEntry> expressionParser, @NonNull String defaultPatternExpression) {
        super(expressionParser, defaultPatternExpression);
    }

    public AntStringMatcher(@Nullable PatternSetExpressionParser<StringPatternEntry> expressionParser, @NonNull PatternSet<StringPatternEntry> defaultPatternSet) {
        super(expressionParser, defaultPatternSet);
    }

    public AntStringMatcher(@Nullable PatternSetExpressionParser<StringPatternEntry> expressionParser, @NonNull PatternSet<StringPatternEntry> defaultPatternSet, PatternSet patternSet) {
        super(expressionParser, defaultPatternSet, patternSet);
    }

    public AntStringMatcher(@NonNull PatternSetExpressionParser<StringPatternEntry> expressionParser, @NonNull String defaultPatternExpression, String patternSetExpression) {
        super(expressionParser, defaultPatternExpression, patternSetExpression);
    }

    @Override
    protected boolean doMatch(String pattern, String string, boolean fullMatch) {
        return false;
    }
}
