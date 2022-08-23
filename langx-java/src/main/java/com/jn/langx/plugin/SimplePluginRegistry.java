package com.jn.langx.plugin;

import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

import java.util.List;

public class SimplePluginRegistry extends GenericRegistry<Plugin> implements PluginRegistry {
    @Override
    public List<Plugin> plugins() {
        return this.instances();
    }

    @Override
    public <P extends Plugin> List<P> find(Class<P> itfc) {
        return find(Functions.isInstancePredicate(itfc));
    }

    @Override
    public final <P extends Plugin> List<P> find(Predicate<P> predicate) {
        return Pipeline.<P>of(this.plugins()).filter(predicate).asList();
    }

    @Override
    public final <P extends Plugin> List<P> find(Class<P> itfc, Predicate<P> predicate) {
        return Pipeline.of(find(itfc)).filter(predicate).asList();
    }

    @Override
    public final <P extends Plugin> P findOne(Class<P> itfc, Predicate<P> predicate) {
        return Pipeline.of(find(itfc, predicate)).findFirst();
    }
}
