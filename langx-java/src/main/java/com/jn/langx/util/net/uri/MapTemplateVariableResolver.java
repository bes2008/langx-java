package com.jn.langx.util.net.uri;

import com.jn.langx.annotation.Nullable;

import java.util.Map;

/**
 * URI template variables backed by a map.
 */
public class MapTemplateVariableResolver implements UriTemplateVariableResolver {

    private final Map<String, ?> uriVariables;

    public MapTemplateVariableResolver(Map<String, ?> uriVariables) {
        this.uriVariables = uriVariables;
    }

    @Override
    @Nullable
    public Object getValue(@Nullable String name) {
        if (!this.uriVariables.containsKey(name)) {
            throw new IllegalArgumentException("Map has no value for '" + name + "'");
        }
        return this.uriVariables.get(name);
    }
}
