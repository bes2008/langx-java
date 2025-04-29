package com.jn.langx.util.net.uri;

import com.jn.langx.annotation.Nullable;


/**
 * Defines the contract for URI Template variables.
 *
 * @since 5.5.0
 */
public interface UriTemplateVariableResolver {

    /**
     * Constant for a value that indicates a URI variable name should be
     * ignored and left as is. This is useful for partial expanding of some
     * but not all URI variables.
     */
    Object SKIP_VALUE = UriTemplateVariableResolver.class;

    /**
     * Get the value for the given URI variable name.
     * If the value is {@code null}, an empty String is expanded.
     * If the value is {@link #SKIP_VALUE}, the URI variable is not expanded.
     *
     * @param name the variable name
     * @return the variable value, possibly {@code null} or {@link #SKIP_VALUE}
     */
    @Nullable
    Object getValue(@Nullable String name);
}