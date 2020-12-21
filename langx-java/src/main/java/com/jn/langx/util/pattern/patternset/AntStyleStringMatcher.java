package com.jn.langx.util.pattern.patternset;

import com.jn.langx.annotation.NonNull;

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

    public AntStyleStringMatcher(PatternSetExpressionParser<StringPatternEntry> parser, @NonNull String defaultPatternExpression){
        super(parser, defaultPatternExpression);
    }

    @Override
    public boolean match(String string) {
        return false;
    }
}
