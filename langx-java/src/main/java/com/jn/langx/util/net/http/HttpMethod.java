package com.jn.langx.util.net.http;


import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Strings;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;


    private static final Map<String, HttpMethod> mappings = new HashMap<String, HttpMethod>(16);

    static {
        for (HttpMethod httpMethod : values()) {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }


    /**
     * Resolve the given method value to an {@code HttpMethod}.
     * @param method the method value as a String
     * @return the corresponding {@code HttpMethod}, or {@code null} if not found
     * 
     */
    @Nullable
    public static HttpMethod resolve(@Nullable String method) {
        return (method != null ? mappings.get(Strings.upperCase(method)) : null);
    }


    /**
     * Determine whether this {@code HttpMethod} matches the given
     * method value.
     * @param method the method value as a String
     * @return {@code true} if it matches, {@code false} otherwise
     * 
     */
    public boolean matches(String method) {
        return (this == resolve(method));
    }

}
