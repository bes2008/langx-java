package com.jn.langx.configuration.resource;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.configuration.AbstractConfigurationLoader;
import com.jn.langx.configuration.Configuration;
import com.jn.langx.configuration.InputStreamConfigurationParser;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.ResourceLocationProvider;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class ResourceConfigurationLoader<T extends Configuration> extends AbstractConfigurationLoader<T> {

    private InputStreamConfigurationParser<T> parser;
    private ResourceLocationProvider<String> resourceLocationProvider;


    public InputStreamConfigurationParser<T> getParser() {
        return parser;
    }

    public void setParser(InputStreamConfigurationParser<T> parser) {
        this.parser = parser;
    }

    public ResourceLocationProvider<String> getResourceLocationProvider() {
        return resourceLocationProvider;
    }

    public void setResourceLocationProvider(ResourceLocationProvider<String> resourceLocationProvider) {
        this.resourceLocationProvider = resourceLocationProvider;
    }

    @Override
    public T load(@NonNull String configurationId) {
        Preconditions.checkNotEmpty(configurationId, "the configuration id is null or empty");
        Location location = resourceLocationProvider.get(configurationId);
        Preconditions.checkNotNull(location, "Can't find the location for configuration : {}", configurationId);
        T configuration = null;
        Resource resource = Resources.loadResource(location);
        Logger logger = Loggers.getLogger(getClass());
        if (resource != null && resource.exists()) {
            InputStream inputStream = null;
            try {
                inputStream = resource.getInputStream();
                configuration = parser.parse(inputStream);
                if (configuration != null) {
                    configuration.setId(configurationId);
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        } else {
            logger.error("Location {} is not exists", location);
        }
        return null;
    }
}
