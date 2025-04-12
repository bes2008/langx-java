package com.jn.langx.util.net.uri.component;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.function.Operator;
import com.jn.langx.util.net.uri.UriTemplateVariableResolver;

import java.util.List;

import static com.jn.langx.util.net.uri.component.UriComponents.replaceUriComponent;

/**
 * Represents a path backed by a String.
 */
final class FullPathComponent implements PathComponent {

    private final String path;

    public FullPathComponent(@Nullable String path) {
        this.path = (path != null ? path : "");
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public List<String> getPathSegments() {
        String[] segments = Strings.split(getPath(), "/");
        return Lists.asList(segments);
    }

    @Override
    public PathComponent encode(Function2<String, UriComponentType, String> encoder) {
        String encodedPath = encoder.apply(getPath(), UriComponentType.PATH);
        return new FullPathComponent(encodedPath);
    }

    @Override
    public void verify() {
        HierarchicalUriComponents.verifyUriComponent(getPath(), UriComponentType.PATH);
    }

    @Override
    public PathComponent expand(UriTemplateVariableResolver uriVariables, @Nullable Operator<String> encoder) {
        String expandedPath = replaceUriComponent(getPath(), uriVariables, encoder);
        return new FullPathComponent(expandedPath);
    }

    @Override
    public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
        builder.path(getPath());
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof FullPathComponent) {
            FullPathComponent fullPathComponent = (FullPathComponent) other;
            return Objs.equals(getPath(), fullPathComponent.getPath());
        }

        return false;

    }

    @Override
    public int hashCode() {
        return getPath().hashCode();
    }
}
