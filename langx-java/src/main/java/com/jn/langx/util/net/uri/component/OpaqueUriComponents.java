package com.jn.langx.util.net.uri.component;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.net.uri.UriTemplateVariables;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;


/**
 * Extension of {@link UriComponents} for opaque URIs.
 *
 * @author Arjen Poutsma
 * @author Phillip Webb
 * @see <a href="https://tools.ietf.org/html/rfc3986#section-1.2.3">Hierarchical vs Opaque URIs</a>
 */
final class OpaqueUriComponents extends UriComponents {

    private static final MultiValueMap<String, String> QUERY_PARAMS_NONE = new LinkedMultiValueMap<String, String>();

    @Nullable
    private final String ssp;


    OpaqueUriComponents(@Nullable String scheme, @Nullable String schemeSpecificPart, @Nullable String fragment) {
        super(scheme, fragment);
        this.ssp = schemeSpecificPart;
    }


    @Override
    @Nullable
    public String getSchemeSpecificPart() {
        return this.ssp;
    }

    @Override
    @Nullable
    public String getUserInfo() {
        return null;
    }

    @Override
    @Nullable
    public String getHost() {
        return null;
    }

    @Override
    public int getPort() {
        return -1;
    }

    @Override
    @Nullable
    public String getPath() {
        return null;
    }

    @Override
    public List<String> getPathSegments() {
        return Collections.emptyList();
    }

    @Override
    @Nullable
    public String getQuery() {
        return null;
    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        return QUERY_PARAMS_NONE;
    }

    @Override
    public UriComponents encode(Charset charset) {
        return this;
    }

    @Override
    protected UriComponents expandInternal(UriTemplateVariables uriVariables) {
        String expandedScheme = expandUriComponent(getScheme(), uriVariables);
        String expandedSsp = expandUriComponent(getSchemeSpecificPart(), uriVariables);
        String expandedFragment = expandUriComponent(getFragment(), uriVariables);
        return new OpaqueUriComponents(expandedScheme, expandedSsp, expandedFragment);
    }

    @Override
    public UriComponents normalize() {
        return this;
    }

    @Override
    public String toUriString() {
        StringBuilder uriBuilder = new StringBuilder();

        if (getScheme() != null) {
            uriBuilder.append(getScheme());
            uriBuilder.append(':');
        }
        if (this.ssp != null) {
            uriBuilder.append(this.ssp);
        }
        if (getFragment() != null) {
            uriBuilder.append('#');
            uriBuilder.append(getFragment());
        }

        return uriBuilder.toString();
    }

    @Override
    public URI toUri() {
        try {
            return new URI(getScheme(), this.ssp, getFragment());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
        if (getScheme() != null) {
            builder.scheme(getScheme());
        }
        if (getSchemeSpecificPart() != null) {
            builder.schemeSpecificPart(getSchemeSpecificPart());
        }
        if (getFragment() != null) {
            builder.fragment(getFragment());
        }
    }


    @Override
    public boolean equals(@Nullable Object other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (!(other instanceof UriComponents)) {
            return false;
        }
        OpaqueUriComponents that = (OpaqueUriComponents) other;
        if (!Objs.equals(getScheme(), that.getScheme())) {
            return false;
        }
        if (!Objs.equals(this.ssp, that.ssp)) {
            return false;
        }
        return Objs.equals(getFragment(), that.getFragment());
    }

    @Override
    public int hashCode() {
        return Objs.hash(getScheme(), this.ssp, getFragment());
    }

}
