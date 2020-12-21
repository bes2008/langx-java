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
public class AntStyleStringMatcher extends AbstractPatternSetMatcher<StringPatternEntry> {

    public AntStyleStringMatcher(@NonNull String defaultPatternExpression) {
        this(new GenericPatternSetExpressionParser<StringPatternEntry>(new StringPatternEntry.Factory()), defaultPatternExpression);
    }

    public AntStyleStringMatcher(String defaultPatternExpression, @NonNull String patternExpression) {
        super(new GenericPatternSetExpressionParser<StringPatternEntry>(new StringPatternEntry.Factory()), defaultPatternExpression, patternExpression);
    }

    public AntStyleStringMatcher(@NonNull PatternSetExpressionParser<StringPatternEntry> expressionParser, @NonNull String defaultPatternExpression) {
        super(expressionParser, defaultPatternExpression);
    }

    public AntStyleStringMatcher(@Nullable PatternSetExpressionParser<StringPatternEntry> expressionParser, @NonNull PatternSet<StringPatternEntry> defaultPatternSet) {
        super(expressionParser, defaultPatternSet);
    }

    public AntStyleStringMatcher(@Nullable PatternSetExpressionParser<StringPatternEntry> expressionParser, @NonNull PatternSet<StringPatternEntry> defaultPatternSet, PatternSet patternSet) {
        super(expressionParser, defaultPatternSet, patternSet);
    }

    public AntStyleStringMatcher(@NonNull PatternSetExpressionParser<StringPatternEntry> expressionParser, @NonNull String defaultPatternExpression, String patternSetExpression) {
        super(expressionParser, defaultPatternExpression, patternSetExpression);
    }

    @Override
    public boolean match(String string) {
        return false;
    }
}
