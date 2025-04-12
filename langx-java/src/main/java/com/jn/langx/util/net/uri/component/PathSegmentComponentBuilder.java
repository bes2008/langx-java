package com.jn.langx.util.net.uri.component;

import com.jn.langx.util.Strings;

import java.util.ArrayList;
import java.util.List;

class PathSegmentComponentBuilder implements PathComponentBuilder {

    private final List<String> pathSegments = new ArrayList<String>();

    public void append(String... pathSegments) {
        for (String pathSegment : pathSegments) {
            if (Strings.isNotBlank(pathSegment)) {
                this.pathSegments.add(pathSegment);
            }
        }
    }

    @Override
    public PathComponent build() {
        return (this.pathSegments.isEmpty() ? null : new PathSegmentComponent(this.pathSegments));
    }

    @Override
    public PathSegmentComponentBuilder cloneBuilder() {
        PathSegmentComponentBuilder builder = new PathSegmentComponentBuilder();
        builder.pathSegments.addAll(this.pathSegments);
        return builder;
    }
}

