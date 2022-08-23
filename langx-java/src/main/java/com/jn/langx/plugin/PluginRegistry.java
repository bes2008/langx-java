package com.jn.langx.plugin;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.registry.Registry;
import com.jn.langx.util.function.Predicate;

import java.util.List;

public interface PluginRegistry extends Registry<String, Plugin> {
    List<Plugin> plugins();

    <P extends Plugin> List<P> find(@NonNull Class<P> itfc);

    <P extends Plugin> List<P> find(@NonNull Predicate<P> predicate);

    <P extends Plugin> List<P> find(@NonNull Class<P> itfc, @NonNull Predicate<P> predicate);

    <P extends Plugin> P findOne(@NonNull Class<P> itfc, @NonNull Predicate<P> predicate);
}
