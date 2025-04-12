package com.jn.langx.util.net.uri;

import com.jn.langx.annotation.Nullable;

import java.util.Arrays;
import java.util.Iterator;

/**
 * URI template variables backed by a variable argument array.
 *
 * @since 5.4.7
 */
public class VarArgsTemplateVariables implements UriTemplateVariables {

    private final Iterator<Object> valueIterator;

    public VarArgsTemplateVariables(Object... uriVariableValues) {
        this.valueIterator = Arrays.asList(uriVariableValues).iterator();
    }

    @Override
    @Nullable
    public Object getValue(@Nullable String name) {
        if (!this.valueIterator.hasNext()) {
            throw new IllegalArgumentException("Not enough variable values available to expand '" + name + "'");
        }
        return this.valueIterator.next();
    }
}