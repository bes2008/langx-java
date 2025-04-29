package com.jn.langx.util.net.uri.component;

import com.jn.langx.annotation.Nullable;

interface PathComponentBuilder {

    @Nullable
    PathComponent build();

    PathComponentBuilder cloneBuilder();
}