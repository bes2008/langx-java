package com.jn.langx.text.grok;

import com.jn.langx.Factory;
import com.jn.langx.text.grok.pattern.PatternDefinition;

public interface TemplatizedPatternFactory extends Factory<PatternDefinition, TemplatizedPattern> {
    @Override
    TemplatizedPattern get(PatternDefinition definition);
}
