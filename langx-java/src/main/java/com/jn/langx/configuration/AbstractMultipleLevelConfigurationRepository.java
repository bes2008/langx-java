package com.jn.langx.configuration;

import java.util.Map;

public abstract class AbstractMultipleLevelConfigurationRepository extends AbstractConfigurationRepository{
    private Map<Integer, ConfigurationRepository> delegates;


}
