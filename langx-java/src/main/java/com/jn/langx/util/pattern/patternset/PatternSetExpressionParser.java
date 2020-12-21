package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;
import com.jn.langx.Parser;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

public interface PatternSetExpressionParser<PatternEntry extends Named> extends Parser<String, PatternSet<PatternEntry>> {
    PatternSet<PatternEntry> parse(@NonNull String expression);

    @NonNull
    String getSeparator();

    @Nullable
    String getExcludeFlag();
}
