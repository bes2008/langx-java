package com.jn.langx.util.pattern.patternset;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.util.regex.Pattern;

public class RegexpPatternSetMatcher extends AbstractPatternSetMatcher<StringPatternEntry> {
    public RegexpPatternSetMatcher(@Nullable PatternSetExpressionParser<StringPatternEntry> expressionParser, @Nullable String defaultPatternExpression) {
        super(expressionParser, defaultPatternExpression);
    }

    public RegexpPatternSetMatcher(@NonNull PatternSetExpressionParser<StringPatternEntry> expressionParser, @Nullable PatternSet<StringPatternEntry> defaultPatternSet) {
        super(expressionParser, defaultPatternSet);
    }

    public RegexpPatternSetMatcher(@NonNull PatternSetExpressionParser<StringPatternEntry> expressionParser, @Nullable PatternSet<StringPatternEntry> defaultPatternSet, PatternSet<StringPatternEntry> patternSet) {
        super(expressionParser, defaultPatternSet, patternSet);
    }

    public RegexpPatternSetMatcher(@NonNull PatternSetExpressionParser<StringPatternEntry> expressionParser, @Nullable String defaultPatternSet, String patternSetExpression) {
        super(expressionParser, defaultPatternSet, patternSetExpression);
    }


    @Override
    protected boolean doMatch(String pattern, String string, boolean fullMatch) {
        return Pattern.compile(pattern).matcher(string).matches();
    }
}
