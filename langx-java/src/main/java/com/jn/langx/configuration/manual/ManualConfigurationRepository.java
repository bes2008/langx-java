package com.jn.langx.configuration.manual;

import com.jn.langx.configuration.*;

public class ManualConfigurationRepository<T extends Configuration> extends AbstractConfigurationRepository<T, NoopConfigurationLoader<T>, ConfigurationWriter<T>> {

}
