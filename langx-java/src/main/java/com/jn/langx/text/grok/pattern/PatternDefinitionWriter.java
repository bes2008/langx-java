package com.jn.langx.text.grok.pattern;

import com.jn.langx.configuration.ConfigurationWriter;
/**
 * @since 4.5.1
 */
public interface PatternDefinitionWriter extends ConfigurationWriter<PatternDefinition> {
    @Override
    void write(PatternDefinition pd);

    @Override
    void rewrite(PatternDefinition pd);
}
