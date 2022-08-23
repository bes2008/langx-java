package com.jn.langx.plugin;

import java.util.Iterator;

public interface PluginLoader {
    <P extends Plugin> Iterator<P> load(Class<P> pluginClass);
}
