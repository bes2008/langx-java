package com.jn.langx.text.grok;

import com.jn.langx.Parser;
import com.jn.langx.text.grok.pattern.PatternDefinition;

/**
 * @since 4.5.0
 */
public interface GrokTemplatizedPatternParser extends Parser<PatternDefinition, TemplatizedPattern> {
    @Override
    TemplatizedPattern parse(PatternDefinition template);

    TemplatizedPattern parse(String template);
}
