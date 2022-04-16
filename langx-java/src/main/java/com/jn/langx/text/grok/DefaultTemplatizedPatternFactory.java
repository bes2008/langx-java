package com.jn.langx.text.grok;


import com.jn.langx.text.grok.pattern.PatternDefinition;

public class DefaultTemplatizedPatternFactory implements TemplatizedPatternFactory {
    @Override
    public TemplatizedPattern get(PatternDefinition patternDefinition) {
        TemplatizedPattern pattern = new TemplatizedPattern();
        return pattern;
    }
}
