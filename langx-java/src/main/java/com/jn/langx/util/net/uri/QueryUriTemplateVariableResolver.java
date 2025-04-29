package com.jn.langx.util.net.uri;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Arrs;

public class QueryUriTemplateVariableResolver implements UriTemplateVariableResolver {

    private final UriTemplateVariableResolver delegate;

    public QueryUriTemplateVariableResolver(UriTemplateVariableResolver delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object getValue(@Nullable String name) {
        Object value = this.delegate.getValue(name);
        if (Arrs.isArray(value)) {
            value = Strings.join(",", value);
        }
        return value;
    }
}