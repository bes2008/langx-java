package com.jn.langx.text.grok;

import com.jn.langx.configuration.AbstractConfigurationRepository;
import com.jn.langx.configuration.ConfigurationLoader;
import com.jn.langx.configuration.ConfigurationWriter;

/**
 * @since 4.5.0
 */
public class PatternDefinitionRepository<Loader extends ConfigurationLoader<PatternDefinition>, Writer extends ConfigurationWriter<PatternDefinition>> extends AbstractConfigurationRepository<PatternDefinition, Loader, Writer> {

}
