package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;
import com.jn.langx.annotation.NonNull;

/**
 * <pre>
 * ?	匹配任何单字符
 * *	匹配0或者任意数量的字符
 * **	匹配0或者更多的目录
 * </pre>
 */
public class AntStyleStringMatcher<PatternEntry extends Named> extends AbstractPatternSetMatcher<PatternEntry> {

    public AntStyleStringMatcher(@NonNull PatternSetExpressionParser<PatternEntry> expressionParser, @NonNull String defaultPatternExpression) {
        super(expressionParser, defaultPatternExpression);
    }

    @Override
    public boolean match(String string) {
        return false;
    }
}
