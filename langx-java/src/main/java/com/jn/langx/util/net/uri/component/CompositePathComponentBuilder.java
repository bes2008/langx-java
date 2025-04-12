package com.jn.langx.util.net.uri.component;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

class CompositePathComponentBuilder implements PathComponentBuilder {

    private final Deque<PathComponentBuilder> builders = new ArrayDeque<PathComponentBuilder>();

    public void addPathSegments(String... pathSegments) {
        if (!Objs.isEmpty(pathSegments)) {
            PathSegmentComponentBuilder psBuilder = getLastBuilder(PathSegmentComponentBuilder.class);
            FullPathComponentBuilder fpBuilder = getLastBuilder(FullPathComponentBuilder.class);
            if (psBuilder == null) {
                psBuilder = new PathSegmentComponentBuilder();
                this.builders.add(psBuilder);
                if (fpBuilder != null) {
                    fpBuilder.removeTrailingSlash();
                }
            }
            psBuilder.append(pathSegments);
        }
    }

    public void addPath(String path) {
        if (Strings.isNotBlank(path)) {
            PathSegmentComponentBuilder psBuilder = getLastBuilder(PathSegmentComponentBuilder.class);
            FullPathComponentBuilder fpBuilder = getLastBuilder(FullPathComponentBuilder.class);
            if (psBuilder != null) {
                path = (path.startsWith("/") ? path : "/" + path);
            }
            if (fpBuilder == null) {
                fpBuilder = new FullPathComponentBuilder();
                this.builders.add(fpBuilder);
            }
            fpBuilder.append(path);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <T> T getLastBuilder(Class<T> builderClass) {
        if (!this.builders.isEmpty()) {
            PathComponentBuilder last = this.builders.getLast();
            if (builderClass.isInstance(last)) {
                return (T) last;
            }
        }
        return null;
    }

    @Override
    public PathComponent build() {
        int size = this.builders.size();
        List<PathComponent> components = new ArrayList<PathComponent>(size);
        for (PathComponentBuilder componentBuilder : this.builders) {
            PathComponent pathComponent = componentBuilder.build();
            if (pathComponent != null) {
                components.add(pathComponent);
            }
        }
        if (components.isEmpty()) {
            return PathComponent.NULL_PATH_COMPONENT;
        }
        if (components.size() == 1) {
            return components.get(0);
        }
        return new PathComponentComposite(components);
    }

    @Override
    public CompositePathComponentBuilder cloneBuilder() {
        CompositePathComponentBuilder compositeBuilder = new CompositePathComponentBuilder();
        for (PathComponentBuilder builder : this.builders) {
            compositeBuilder.builders.add(builder.cloneBuilder());
        }
        return compositeBuilder;
    }
}
