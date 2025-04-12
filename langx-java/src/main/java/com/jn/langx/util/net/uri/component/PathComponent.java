package com.jn.langx.util.net.uri.component;


import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.function.Operator;
import com.jn.langx.util.net.uri.UriTemplateVariables;

import java.io.Serializable;
import java.util.List;

/**
 * Defines the contract for path (segments).
 *
 * @since 5.4.7
 */
interface PathComponent extends Serializable {

    String getPath();

    List<String> getPathSegments();

    PathComponent encode(Function2<String, UriComponentType, String> encoder);

    void verify();

    PathComponent expand(UriTemplateVariables uriVariables, @Nullable Operator<String> encoder);

    void copyToUriComponentsBuilder(UriComponentsBuilder builder);


    /**
     * Represents an empty path.
     */
    static final PathComponent NULL_PATH_COMPONENT = new PathComponent() {
        @Override
        public String getPath() {
            return "";
        }

        @Override
        public List<String> getPathSegments() {
            return Collects.emptyArrayList();
        }

        public PathComponent encode(Function2<String, UriComponentType, String> encoder) {
            return this;
        }

        @Override
        public void verify() {
        }

        @Override
        public PathComponent expand(UriTemplateVariables uriVariables, @Nullable Operator<String> encoder) {
            return this;
        }

        @Override
        public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return (this == other);
        }

        @Override
        public int hashCode() {
            return getClass().hashCode();
        }
    };
}
