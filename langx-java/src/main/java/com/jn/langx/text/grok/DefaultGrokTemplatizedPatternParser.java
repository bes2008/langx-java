package com.jn.langx.text.grok;

import com.jn.langx.util.Preconditions;


/**
 * @since 4.5.0
 */
public class DefaultGrokTemplatizedPatternParser implements GrokTemplatizedPatternParser{

    private PatternDefinitionRepository patternDefinitionRepository;

    @Override
    public TemplatizedPattern parse(String template) {
        Preconditions.checkNotNull(template, "template");

        return null;
    }

}
