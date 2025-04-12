package com.jn.langx.util.net.uri.component;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringJoiner;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.function.Operator;
import com.jn.langx.util.net.uri.UriTemplateVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a path backed by a String list (i.e. path segments).
 */
final class PathSegmentComponent implements PathComponent {

    private final List<String> pathSegments;

    PathSegmentComponent(List<String> pathSegments) {
        Preconditions.checkNotNull(pathSegments, "List must not be null");
        this.pathSegments = Lists.newArrayList(pathSegments);
    }

    @Override
    public String getPath() {
        String delimiter = "/";
        StringJoiner pathBuilder = new StringJoiner(delimiter, delimiter, "");
        for (String pathSegment : this.pathSegments) {
            pathBuilder.add(pathSegment);
        }
        return pathBuilder.toString();
    }

    @Override
    public List<String> getPathSegments() {
        return this.pathSegments;
    }

    @Override
    public PathComponent encode(Function2<String, UriComponentType, String> encoder) {
        List<String> pathSegments = getPathSegments();
        List<String> encodedPathSegments = new ArrayList<String>(pathSegments.size());
        for (String pathSegment : pathSegments) {
            String encodedPathSegment = encoder.apply(pathSegment, UriComponentType.PATH_SEGMENT);
            encodedPathSegments.add(encodedPathSegment);
        }
        return new PathSegmentComponent(encodedPathSegments);
    }

    @Override
    public void verify() {
        for (String pathSegment : getPathSegments()) {
            HierarchicalUriComponents.verifyUriComponent(pathSegment, UriComponentType.PATH_SEGMENT);
        }
    }

    @Override
    public PathComponent expand(UriTemplateVariables uriVariables, @Nullable Operator<String> encoder) {
        List<String> pathSegments = getPathSegments();
        List<String> expandedPathSegments = new ArrayList<String>(pathSegments.size());
        for (String pathSegment : pathSegments) {
            String expandedPathSegment = UriComponents.expandUriComponent(pathSegment, uriVariables, encoder);
            expandedPathSegments.add(expandedPathSegment);
        }
        return new PathSegmentComponent(expandedPathSegments);
    }

    @Override
    public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
        builder.pathSegment(Collects.toArray(getPathSegments(), String[].class));
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (other instanceof PathSegmentComponent) {
            PathSegmentComponent pathSegmentComponent = (PathSegmentComponent) other;
            return getPathSegments().equals(pathSegmentComponent.getPathSegments());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getPathSegments().hashCode();
    }
}
