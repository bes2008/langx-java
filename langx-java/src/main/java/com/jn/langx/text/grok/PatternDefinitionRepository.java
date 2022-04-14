package com.jn.langx.text.grok;

import com.jn.langx.configuration.ConfigurationLoader;
import com.jn.langx.configuration.ConfigurationRepository;
import com.jn.langx.configuration.ConfigurationWriter;

public interface PatternDefinitionRepository<Loader extends ConfigurationLoader<PatternDefinition>, Writer extends ConfigurationWriter<PatternDefinition>> extends ConfigurationRepository<PatternDefinition, Loader, Writer> {

}
