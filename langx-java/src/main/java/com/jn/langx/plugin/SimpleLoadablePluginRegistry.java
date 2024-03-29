package com.jn.langx.plugin;


import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;

import java.util.Iterator;
import java.util.List;

public class SimpleLoadablePluginRegistry extends SimplePluginRegistry {
    private PluginLoader pluginLoader;

    public void setPluginLoader(PluginLoader pluginLoader) {
        this.pluginLoader = pluginLoader;
    }

    @Override
    public <P extends Plugin> List<P> find(Class<P> itfc) {
        Iterator<P> plugins = this.pluginLoader.load(itfc);
        Pipeline.<P>of(plugins).filter(new Predicate<P>() {
            @Override
            public boolean test(P plugin) {
                return !contains(plugin.getName());
            }
        }).forEach(new Consumer<P>() {
            @Override
            public void accept(P p) {
                register(p);
            }
        });
        return super.find(itfc);
    }
}
