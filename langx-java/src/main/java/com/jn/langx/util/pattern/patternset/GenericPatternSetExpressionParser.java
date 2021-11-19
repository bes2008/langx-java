package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.exception.ExpressionParseException;
import com.jn.langx.Factory;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.List;

public class GenericPatternSetExpressionParser<PatternEntry extends Named> implements PatternSetExpressionParser<PatternEntry> {

    @NonNull
    private Factory<String, PatternEntry> patternFactory;

    public GenericPatternSetExpressionParser(Factory<String, PatternEntry> patternFactory) {
        setPatternFactory(patternFactory);
    }

    @Override
    public PatternSet<PatternEntry> parse(String expression) {
        Preconditions.checkNotEmpty(expression, "the expression is empty or null");
        Preconditions.checkNotEmpty(getSeparator(), "the separator is empty or null");
        Preconditions.checkNotNull(patternFactory, "the pattern factory is null");

        List<String> segments = Collects.asList(Strings.split(expression, getSeparator(), false));

        PatternSet<PatternEntry> patternSet = internalParse(segments);

        patternSet.setExcludeFlag(getExcludeFlag());
        patternSet.setSeparator(getSeparator());
        patternSet.setExpression(expression);

        return patternSet;
    }

    protected PatternSet<PatternEntry> internalParse(List<String> segments) throws ExpressionParseException {
        final PatternSet<PatternEntry> patternSet = new PatternSet<PatternEntry>();
        Collects.forEach(segments, new Consumer<String>() {
            @Override
            public void accept(String segment) {
                if (Strings.startsWith(segment, getExcludeFlag())) {
                    patternSet.addExclude(patternFactory.get(segment));
                } else {
                    patternSet.addInclude(patternFactory.get(segment));
                }
            }
        });
        return patternSet;
    }

    @Override
    public String getSeparator() {
        return ";";
    }

    @Override
    public String getExcludeFlag() {
        return "!";
    }

    public Factory<String, PatternEntry> getPatternFactory() {
        return patternFactory;
    }

    public void setPatternFactory(Factory<String, PatternEntry> patternFactory) {
        this.patternFactory = patternFactory;
    }
}
