package com.jn.langx.util.net.uri.component;


import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.function.Operator;
import com.jn.langx.util.net.uri.QueryUriTemplateVariableResolver;
import com.jn.langx.util.net.uri.UriTemplateVariableResolver;

/**
 * Extension of {@link UriComponents} for hierarchical URIs.
 *
 * @see <a href="https://tools.ietf.org/html/rfc3986#section-1.2.3">Hierarchical URIs</a>
 */
final class HierarchicalUriComponents extends UriComponents {

    private static final char PATH_DELIMITER = '/';

    private static final MultiValueMap<String, String> EMPTY_QUERY_PARAMS = new LinkedMultiValueMap<String, String>();


    @Nullable
    private final String userInfo;

    @Nullable
    private final String host;

    @Nullable
    private final String port;

    private final PathComponent path;

    private final MultiValueMap<String, String> queryParams;

    private final EncodeState encodeState;

    @Nullable
    private Operator<String> variableEncoder;


    /**
     * Package-private constructor. All arguments are optional, and can be {@code null}.
     *
     * @param scheme   the scheme
     * @param userInfo the user info
     * @param host     the host
     * @param port     the port
     * @param path     the path
     * @param query    the query parameters
     * @param fragment the fragment
     * @param encoded  whether the components are already encoded
     */
    HierarchicalUriComponents(@Nullable String scheme, @Nullable String fragment, @Nullable String userInfo,
                              @Nullable String host, @Nullable String port, @Nullable PathComponent path,
                              @Nullable MultiValueMap<String, String> query, boolean encoded) {

        super(scheme, fragment);

        this.userInfo = userInfo;
        this.host = host;
        this.port = port;
        this.path = path != null ? path : PathComponent.NULL_PATH_COMPONENT;
        this.queryParams = query != null ? query : EMPTY_QUERY_PARAMS;
        this.encodeState = encoded ? EncodeState.FULLY_ENCODED : EncodeState.RAW;

        // Check for illegal characters..
        if (encoded) {
            verify();
        }
    }

    private HierarchicalUriComponents(@Nullable String scheme, @Nullable String fragment,
                                      @Nullable String userInfo, @Nullable String host, @Nullable String port,
                                      PathComponent path, MultiValueMap<String, String> queryParams,
                                      EncodeState encodeState, @Nullable Operator<String> variableEncoder) {

        super(scheme, fragment);

        this.userInfo = userInfo;
        this.host = host;
        this.port = port;
        this.path = path;
        this.queryParams = queryParams;
        this.encodeState = encodeState;
        this.variableEncoder = variableEncoder;
    }


    // Component getters

    @Override
    @Nullable
    public String getSchemeSpecificPart() {
        return null;
    }

    @Override
    @Nullable
    public String getUserInfo() {
        return this.userInfo;
    }

    @Override
    @Nullable
    public String getHost() {
        return this.host;
    }

    @Override
    public int getPort() {
        if (this.port == null) {
            return -1;
        } else if (this.port.contains("{")) {
            throw new IllegalStateException(
                    "The port contains a URI variable but has not been expanded yet: " + this.port);
        }
        try {
            return Integer.parseInt(this.port);
        } catch (NumberFormatException ex) {
            throw new IllegalStateException("The port must be an integer: " + this.port);
        }
    }

    @Override
    @NonNull
    public String getPath() {
        return this.path.getPath();
    }

    @Override
    public List<String> getPathSegments() {
        return this.path.getPathSegments();
    }

    @Override
    @Nullable
    public String getQuery() {
        if (!this.queryParams.isEmpty()) {
            final StringBuilder queryBuilder = new StringBuilder();
            Collects.forEach(this.queryParams, new Consumer2<String, Collection<String>>() {
                @Override
                public void accept(String name, Collection<String> values) {
                    if (Objs.isEmpty(values)) {
                        if (queryBuilder.length() != 0) {
                            queryBuilder.append('&');
                        }
                        queryBuilder.append(name);
                    } else {
                        for (Object value : values) {
                            if (queryBuilder.length() != 0) {
                                queryBuilder.append('&');
                            }
                            queryBuilder.append(name);
                            if (value != null) {
                                queryBuilder.append('=').append(value.toString());
                            }
                        }
                    }
                }
            });
            return queryBuilder.toString();
        } else {
            return null;
        }
    }

    /**
     * Return the map of query parameters. Empty if no query has been set.
     */
    @Override
    public MultiValueMap<String, String> getQueryParams() {
        return this.queryParams;
    }


    // Encoding

    /**
     * Identical to {@link #encode()} but skipping over URI variable placeholders.
     * Also {@link #variableEncoder} is initialized with the given charset for
     * use later when URI variables are expanded.
     */
    HierarchicalUriComponents encodeTemplate(final Charset charset) {
        if (this.encodeState.isEncoded()) {
            return this;
        }

        // Remember the charset to encode URI variables later..
        this.variableEncoder = new Operator<String>() {
            @Override
            public String apply(String value) {
                return UriComponentUtils.encodeUriComponent(value, charset, UriComponentType.URI);
            }
        };
        DefaultUriTemplateEncoder encoder = new DefaultUriTemplateEncoder(charset);
        String schemeTo = (getScheme() != null ? encoder.apply(getScheme(), UriComponentType.SCHEME) : null);
        String fragmentTo = (getFragment() != null ? encoder.apply(getFragment(), UriComponentType.FRAGMENT) : null);
        String userInfoTo = (getUserInfo() != null ? encoder.apply(getUserInfo(), UriComponentType.USER_INFO) : null);
        String hostTo = (getHost() != null ? encoder.apply(getHost(), getHostType()) : null);
        PathComponent pathTo = this.path.encode(encoder);
        MultiValueMap<String, String> queryParamsTo = encodeQueryParams(encoder);

        return new HierarchicalUriComponents(schemeTo, fragmentTo, userInfoTo,
                hostTo, this.port, pathTo, queryParamsTo, EncodeState.TEMPLATE_ENCODED, this.variableEncoder);
    }

    @Override
    public HierarchicalUriComponents encode(final Charset charset) {
        if (this.encodeState.isEncoded()) {
            return this;
        }
        String scheme = getScheme();
        String fragment = getFragment();
        String schemeTo = (scheme != null ? UriComponentUtils.encodeUriComponent(scheme, charset, UriComponentType.SCHEME) : null);
        String fragmentTo = (fragment != null ? UriComponentUtils.encodeUriComponent(fragment, charset, UriComponentType.FRAGMENT) : null);
        String userInfoTo = (this.userInfo != null ? UriComponentUtils.encodeUriComponent(this.userInfo, charset, UriComponentType.USER_INFO) : null);
        String hostTo = (this.host != null ? UriComponentUtils.encodeUriComponent(this.host, charset, getHostType()) : null);
        UriComponentEncoder encoder = new UriComponentEncoder() {
            @Override
            public String apply(String s, UriComponentType type) {
                return UriComponentUtils.encodeUriComponent(s, charset, type);
            }
        };
        PathComponent pathTo = this.path.encode(encoder);
        MultiValueMap<String, String> queryParamsTo = encodeQueryParams(encoder);

        return new HierarchicalUriComponents(schemeTo, fragmentTo, userInfoTo,
                hostTo, this.port, pathTo, queryParamsTo, EncodeState.FULLY_ENCODED, null);
    }

    private MultiValueMap<String, String> encodeQueryParams(final Function2<String, UriComponentType, String> encoder) {
        int size = this.queryParams.size();
        final MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>(size);

        Collects.forEach(this.queryParams, new Consumer2<String, Collection<String>>() {
            @Override
            public void accept(String key, Collection<String> values) {
                String name = encoder.apply(key, UriComponentType.QUERY_PARAM);
                List<String> encodedValues = new ArrayList<String>(values.size());
                for (String value : values) {
                    encodedValues.add(value != null ? encoder.apply(value, UriComponentType.QUERY_PARAM) : null);
                }
                result.put(name, encodedValues);
            }
        });
        return result;
    }


    private UriComponentType getHostType() {
        return (this.host != null && this.host.startsWith("[") ? UriComponentType.HOST_IPV6 : UriComponentType.HOST_IPV4);
    }

    // Verifying

    /**
     * Check if any of the URI components contain any illegal characters.
     *
     * @throws IllegalArgumentException if any component has illegal characters
     */
    private void verify() {
        verifyUriComponent(getScheme(), UriComponentType.SCHEME);
        verifyUriComponent(this.userInfo, UriComponentType.USER_INFO);
        verifyUriComponent(this.host, getHostType());
        this.path.verify();
        Collects.forEach(this.queryParams, new Consumer2<String, Collection<String>>() {
            @Override
            public void accept(String key, Collection<String> values) {
                verifyUriComponent(key, UriComponentType.QUERY_PARAM);
                for (String value : values) {
                    verifyUriComponent(value, UriComponentType.QUERY_PARAM);
                }
            }
        });
        verifyUriComponent(getFragment(), UriComponentType.FRAGMENT);
    }

    public static void verifyUriComponent(@Nullable String source, UriComponentType type) {
        if (source == null) {
            return;
        }
        int length = source.length();
        for (int i = 0; i < length; i++) {
            char ch = source.charAt(i);
            if (ch == '%') {
                if ((i + 2) < length) {
                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" +
                                source.substring(i) + "\"");
                    }
                    i += 2;
                } else {
                    throw new IllegalArgumentException("Invalid encoded sequence \"" +
                            source.substring(i) + "\"");
                }
            } else if (!type.isAllowed(ch)) {
                throw new IllegalArgumentException("Invalid character '" + ch + "' for " +
                        type.name() + " in \"" + source + "\"");
            }
        }
    }


    // Expanding

    @Override
    protected HierarchicalUriComponents replaceVariablesInternal(UriTemplateVariableResolver uriVariables) {
        Preconditions.checkState(!this.encodeState.equals(EncodeState.FULLY_ENCODED),
                "URI components already encoded, and could not possibly contain '{' or '}'.");

        // Array-based vars rely on the order below...
        String schemeTo = replaceUriComponent(getScheme(), uriVariables, this.variableEncoder);
        String userInfoTo = replaceUriComponent(this.userInfo, uriVariables, this.variableEncoder);
        String hostTo = replaceUriComponent(this.host, uriVariables, this.variableEncoder);
        String portTo = replaceUriComponent(this.port, uriVariables, this.variableEncoder);
        PathComponent pathTo = this.path.replaceVariables(uriVariables, this.variableEncoder);
        MultiValueMap<String, String> queryParamsTo = expandQueryParams(uriVariables);
        String fragmentTo = replaceUriComponent(getFragment(), uriVariables, this.variableEncoder);

        return new HierarchicalUriComponents(schemeTo, fragmentTo, userInfoTo,
                hostTo, portTo, pathTo, queryParamsTo, this.encodeState, this.variableEncoder);
    }

    private MultiValueMap<String, String> expandQueryParams(UriTemplateVariableResolver variables) {
        int size = this.queryParams.size();
        final MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>(size);
        final UriTemplateVariableResolver queryVariables = new QueryUriTemplateVariableResolver(variables);
        Collects.forEach(this.queryParams, new Consumer2<String, Collection<String>>() {
            @Override
            public void accept(String key, Collection<String> values) {
                String name = replaceUriComponent(key, queryVariables, HierarchicalUriComponents.this.variableEncoder);
                List<String> expandedValues = new ArrayList<String>(values.size());
                for (String value : values) {
                    expandedValues.add(replaceUriComponent(value, queryVariables, HierarchicalUriComponents.this.variableEncoder));
                }
                result.put(name, expandedValues);
            }
        });
        return result;
    }

    @Override
    public UriComponents normalize() {
        String normalizedPath = Strings.cleanPath(getPath());
        FullPathComponent path = new FullPathComponent(normalizedPath);
        return new HierarchicalUriComponents(getScheme(), getFragment(), this.userInfo, this.host, this.port,
                path, this.queryParams, this.encodeState, this.variableEncoder);
    }


    // Other functionality

    @Override
    public String toUriString() {
        StringBuilder uriBuilder = new StringBuilder();
        if (getScheme() != null) {
            uriBuilder.append(getScheme()).append(':');
        }
        if (this.userInfo != null || this.host != null) {
            uriBuilder.append("//");
            if (this.userInfo != null) {
                uriBuilder.append(this.userInfo).append('@');
            }
            if (this.host != null) {
                uriBuilder.append(this.host);
            }
            if (getPort() != -1) {
                uriBuilder.append(':').append(this.port);
            }
        }
        String path = getPath();
        if (Strings.isNotEmpty(path)) {
            if (uriBuilder.length() != 0 && path.charAt(0) != PATH_DELIMITER) {
                uriBuilder.append(PATH_DELIMITER);
            }
            uriBuilder.append(path);
        }
        String query = getQuery();
        if (query != null) {
            uriBuilder.append('?').append(query);
        }
        if (getFragment() != null) {
            uriBuilder.append('#').append(getFragment());
        }
        return uriBuilder.toString();
    }

    @Override
    public URI toUri() {
        try {
            if (this.encodeState.isEncoded()) {
                return new URI(toUriString());
            } else {
                String path = getPath();
                if (Strings.isNotEmpty(path) && path.charAt(0) != PATH_DELIMITER) {
                    // Only prefix the path delimiter if something exists before it
                    if (getScheme() != null || getUserInfo() != null || getHost() != null || getPort() != -1) {
                        path = PATH_DELIMITER + path;
                    }
                }
                return new URI(getScheme(), getUserInfo(), getHost(), getPort(), path, getQuery(), getFragment());
            }
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
        if (getScheme() != null) {
            builder.scheme(getScheme());
        }
        if (getUserInfo() != null) {
            builder.userInfo(getUserInfo());
        }
        if (getHost() != null) {
            builder.host(getHost());
        }
        // Avoid parsing the port, may have URI variable.
        if (this.port != null) {
            builder.port(this.port);
        }
        this.path.copyToUriComponentsBuilder(builder);
        if (!getQueryParams().isEmpty()) {
            builder.queryParams(getQueryParams());
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

        HierarchicalUriComponents that = (HierarchicalUriComponents) other;
        if (!Objs.equals(getScheme(), that.getScheme())) {
            return false;
        }
        if (!Objs.equals(this.getUserInfo(), that.getUserInfo())) {
            return false;
        }
        if (!Objs.equals(this.getHost(), that.getHost())) {
            return false;
        }
        if (!Objs.equals(this.getPort(), that.getPort())) {
            return false;
        }

        if (!Objs.equals(this.getPath(), that.getPath())) {
            return false;
        }
        if (!Objs.equals(this.getFragment(), that.getFragment())) {
            return false;
        }
        if (!this.queryParams.equals(that.queryParams)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objs.hash(getScheme(), this.userInfo, this.host, this.port, this.path, this.queryParams, getFragment());
    }
}
