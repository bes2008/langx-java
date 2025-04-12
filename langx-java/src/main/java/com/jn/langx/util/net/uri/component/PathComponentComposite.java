package com.jn.langx.util.net.uri.component;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.function.Operator;
import com.jn.langx.util.net.uri.UriTemplateVariableResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of PathComponents.
 */
final class PathComponentComposite implements PathComponent {

    private final List<PathComponent> pathComponents;

    public PathComponentComposite(List<PathComponent> pathComponents) {
        Preconditions.checkNotNull(pathComponents, "PathComponent List must not be null");
        this.pathComponents = pathComponents;
    }

    @Override
    public String getPath() {
        StringBuilder pathBuilder = new StringBuilder();
        for (PathComponent pathComponent : this.pathComponents) {
            pathBuilder.append(pathComponent.getPath());
        }
        return pathBuilder.toString();
    }

    @Override
    public List<String> getPathSegments() {
        List<String> result = new ArrayList<String>();
        for (PathComponent pathComponent : this.pathComponents) {
            result.addAll(pathComponent.getPathSegments());
        }
        return result;
    }

    @Override
    public PathComponent encode(Function2<String, UriComponentType, String> encoder) {
        List<PathComponent> encodedComponents = new ArrayList<PathComponent>(this.pathComponents.size());
        for (PathComponent pathComponent : this.pathComponents) {
            encodedComponents.add(pathComponent.encode(encoder));
        }
        return new PathComponentComposite(encodedComponents);
    }

    @Override
    public void verify() {
        for (PathComponent pathComponent : this.pathComponents) {
            pathComponent.verify();
        }
    }

    @Override
    public PathComponent expand(UriTemplateVariableResolver uriVariables, @Nullable Operator<String> encoder) {
        List<PathComponent> expandedComponents = new ArrayList<PathComponent>(this.pathComponents.size());
        for (PathComponent pathComponent : this.pathComponents) {
            expandedComponents.add(pathComponent.expand(uriVariables, encoder));
        }
        return new PathComponentComposite(expandedComponents);
    }

    @Override
    public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
        for (PathComponent pathComponent : this.pathComponents) {
            pathComponent.copyToUriComponentsBuilder(builder);
        }
    }
}
