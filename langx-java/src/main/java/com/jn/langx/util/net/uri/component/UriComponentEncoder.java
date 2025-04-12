package com.jn.langx.util.net.uri.component;

import com.jn.langx.util.function.Function2;

interface UriComponentEncoder extends Function2<String, UriComponentType, String> {
    @Override
    String apply(String component, UriComponentType componentType);
}
