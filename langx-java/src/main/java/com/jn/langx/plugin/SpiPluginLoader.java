package com.jn.langx.plugin;

import com.jn.langx.util.spi.CommonServiceProvider;

import java.util.Iterator;

public class SpiPluginLoader implements PluginLoader {
    private CommonServiceProvider serviceProvider;

    public void setServiceProvider(CommonServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public <P extends Plugin> Iterator<P> load(Class<P> pluginClass) {
        if (this.serviceProvider == null) {
            this.serviceProvider = new CommonServiceProvider();
        }
        return serviceProvider.get(pluginClass);
    }
}
