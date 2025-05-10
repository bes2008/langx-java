package com.jn.langx.util.net.uri.component;

import com.jn.langx.Builder;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.net.uri.UriTemplateVariableResolver;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.util.struct.Holder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.jn.langx.util.net.uri.component.UriComponentUtils.getQueryParamValue;

/**
 * Builder for {@link UriComponents}.
 *
 * <p>Typical usage involves:
 * <ol>
 * <li>Create a {@code UriComponentsBuilder} with one of the static factory methods
 * (such as {@link #fromPath(String)} or {@link #fromUri(URI)})</li>
 * <li>Set the various URI components through the respective methods ({@link #scheme(String)},
 * {@link #userInfo(String)}, {@link #host(String)}, {@link #port(int)}, {@link #path(String)},
 * {@link #pathSegment(String...)}, {@link #queryParam(String, Object...)}, and
 * {@link #fragment(String)}.</li>
 * <li>Build the {@link UriComponents} instance with the {@link #build()} method.</li>
 * </ol>
 *
 * @see #fromPath(String)
 * @see #fromUri(URI)
 */
public class UriComponentsBuilder implements Builder<UriComponents>, Cloneable {

    private static final Regexp QUERY_PARAM_PATTERN = Regexps.compile("([^&=]+)(=?)([^&]+)?");

    private static final String SCHEME_PATTERN = "([^:/?#]+):";

    private static final String HTTP_PATTERN = "(?i)(http|https):";

    private static final String USERINFO_PATTERN = "([^@\\[/?#]*)";

    private static final String HOST_IPV4_PATTERN = "[^\\[/?#:]*";

    private static final String HOST_IPV6_PATTERN = "\\[[\\p{XDigit}:.]*[%\\p{Alnum}]*]";

    private static final String HOST_PATTERN = "(" + HOST_IPV6_PATTERN + "|" + HOST_IPV4_PATTERN + ")";

    private static final String PORT_PATTERN = "(\\{[^}]+\\}?|[^/?#]*)";

    private static final String PATH_PATTERN = "([^?#]*)";

    private static final String QUERY_PATTERN = "([^#]*)";

    private static final String LAST_PATTERN = "(.*)";

    // Regex patterns that matches URIs. See RFC 3986, appendix B
    private static final Regexp URI_PATTERN = Regexps.compile(
            "^(" + SCHEME_PATTERN + ")?" + "(//(" + USERINFO_PATTERN + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN +
                    ")?" + ")?" + PATH_PATTERN + "(\\?" + QUERY_PATTERN + ")?" + "(#" + LAST_PATTERN + ")?");

    private static final Regexp HTTP_URL_PATTERN = Regexps.compile(
            "^" + HTTP_PATTERN + "(//(" + USERINFO_PATTERN + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN + ")?" + ")?" +
                    PATH_PATTERN + "(\\?" + QUERY_PATTERN + ")?" + "(#" + LAST_PATTERN + ")?");


    private static final Object[] EMPTY_VALUES = new Object[0];


    @Nullable
    private String scheme;

    @Nullable
    private String ssp;

    @Nullable
    private String userInfo;

    @Nullable
    private String host;

    @Nullable
    private String port;

    private CompositePathComponentBuilder pathBuilder;

    private final MultiValueMap<String, Object> queryParams = new LinkedMultiValueMap<String, Object>();

    @Nullable
    private String fragment;

    private final Map<String, Object> uriVariables = new HashMap<String, Object>(4);

    private boolean encodeTemplate;

    private Charset charset = Charsets.UTF_8;


    /**
     * Default constructor. Protected to prevent direct instantiation.
     *
     * @see #fromPath(String)
     * @see #fromUri(URI)
     */
    public UriComponentsBuilder() {
        this.pathBuilder = new CompositePathComponentBuilder();
    }

    /**
     * Create a deep copy of the given UriComponentsBuilder.
     *
     * @param other the other builder to copy from
     */
    protected UriComponentsBuilder(UriComponentsBuilder other) {
        this.scheme = other.scheme;
        this.ssp = other.ssp;
        this.userInfo = other.userInfo;
        this.host = other.host;
        this.port = other.port;
        this.pathBuilder = other.pathBuilder.cloneBuilder();
        this.uriVariables.putAll(other.uriVariables);
        this.queryParams.addAll(other.queryParams);
        this.fragment = other.fragment;
        this.encodeTemplate = other.encodeTemplate;
        this.charset = other.charset;
    }


    // Encode methods

    /**
     * 对模板预编码
     * <p>
     * Request to have the URI template pre-encoded at build time, and
     * URI variables encoded separately when replaced.
     * <p>In comparison to {@link UriComponents#encode()}, this method has the
     * same effect on the URI template, i.e. each URI component is encoded by
     * replacing non-ASCII and illegal (within the URI component type) characters
     * with escaped octets. However, URI variables are encoded more strictly, by
     * also escaping characters with reserved meaning.
     * <p>For most cases, this method is more likely to give the expected result
     * because in treats URI variables as opaque data to be fully encoded, while
     * {@link UriComponents#encode()} is useful when intentionally expanding URI
     * variables that contain reserved characters.
     * <p>For example ';' is legal in a path but has reserved meaning. This
     * method replaces ";" with "%3B" in URI variables but not in the URI
     * template. By contrast, {@link UriComponents#encode()} never replaces ";"
     * since it is a legal character in a path.
     * <p>When not expanding URI variables at all, prefer use of
     * {@link UriComponents#encode()} since that will also encode anything that
     * incidentally looks like a URI variable.
     */
    public final UriComponentsBuilder enableEncode() {
        return enableEncode(Charsets.UTF_8);
    }

    /**
     * A variant of {@link #enableEncode()} with a charset other than "UTF-8".
     *
     * @param charset the charset to use for encoding
     */
    public UriComponentsBuilder enableEncode(Charset charset) {
        this.encodeTemplate = true;
        this.charset = charset;
        return this;
    }


    // Build methods

    /**
     * Build a {@code UriComponents} instance from the various components contained in this builder.
     *
     * @return the URI components
     */
    public UriComponents build() {
        return build(false);
    }

    /**
     * Variant of {@link #build()} to create a {@link UriComponents} instance
     * when components are already fully encoded. This is useful for example if
     * the builder was created via {@link UriComponentsBuilder#fromUri(URI)}.
     *
     * @param encoded whether the components in this builder are already encoded
     * @return the URI components
     * @throws IllegalArgumentException if any of the components contain illegal
     *                                  characters that should have been encoded.
     */
    public UriComponents build(boolean encoded) {
        return buildInternal(encoded ? EncodingHint.FULLY_ENCODED :
                (this.encodeTemplate ? EncodingHint.ENCODE_TEMPLATE : EncodingHint.NONE));
    }

    private UriComponents buildInternal(EncodingHint hint) {
        UriComponents result;
        if (this.ssp != null) {
            result = new OpaqueUriComponents(this.scheme, this.ssp, this.fragment);
        } else {
            MultiValueMap<String, Object> theQueryParams = new LinkedMultiValueMap<String, Object>(this.queryParams);
            HierarchicalUriComponents uric = new HierarchicalUriComponents(this.scheme, this.fragment,
                    this.userInfo, this.host, this.port, this.pathBuilder.build(), theQueryParams,
                    hint == EncodingHint.FULLY_ENCODED);
            result = (hint == EncodingHint.ENCODE_TEMPLATE ? uric.encodeTemplate(this.charset) : uric);
        }
        if (!this.uriVariables.isEmpty()) {
            result = result.replaceVariables(new UriTemplateVariableResolver() {
                @Override
                public Object getValue(String name) {
                    if (UriComponentsBuilder.this.uriVariables.containsKey(name)) {
                        return UriComponentsBuilder.this.uriVariables.get(name);
                    }
                    return UriTemplateVariableResolver.SKIP_VALUE;
                }
            });

        }
        return result;
    }

    public URI build(Object... uriVariables) {
        return buildInternal(EncodingHint.ENCODE_TEMPLATE).replaceVariables(uriVariables).toUri();
    }

    public URI build(Map<String, ?> uriVariables) {
        return buildInternal(EncodingHint.ENCODE_TEMPLATE).replaceVariables(uriVariables).toUri();
    }

    /**
     * Build a URI String.
     * <p>Effectively, a shortcut for building, encoding, and returning the
     * String representation:
     * <pre class="code">
     * String uri = builder.build().encode().toUriString()
     * </pre>
     * <p>However if {@link #uriVariables(Map) URI variables} have been provided
     * then the URI template is pre-encoded separately from URI variables (see
     * {@link #enableEncode()} for details), i.e. equivalent to:
     * <pre>
     * String uri = builder.encode().build().toUriString()
     * </pre>
     *
     * @see UriComponents#toUriString()
     */
    public String toUriString() {
        return this.uriVariables.isEmpty()
                ? build().encode().toUriString()
                : buildInternal(EncodingHint.ENCODE_TEMPLATE).toUriString();
    }


    // Instance methods

    /**
     * Initialize components of this builder from components of the given URI.
     *
     * @param uri the URI
     * @return this UriComponentsBuilder
     */
    public UriComponentsBuilder uri(URI uri) {
        Preconditions.checkNotNull(uri, "URI must not be null");
        this.scheme = uri.getScheme();
        if (uri.isOpaque()) {
            this.ssp = uri.getRawSchemeSpecificPart();
            resetHierarchicalComponents();
        } else {
            if (uri.getRawUserInfo() != null) {
                this.userInfo = uri.getRawUserInfo();
            }
            if (uri.getHost() != null) {
                this.host = uri.getHost();
            }
            if (uri.getPort() != -1) {
                this.port = String.valueOf(uri.getPort());
            }
            if (Strings.isNotEmpty(uri.getRawPath())) {
                this.pathBuilder = new CompositePathComponentBuilder();
                this.pathBuilder.addPath(uri.getRawPath());
            }
            if (Strings.isNotEmpty(uri.getRawQuery())) {
                this.queryParams.clear();
                query(uri.getRawQuery());
            }
            resetSchemeSpecificPart();
        }
        if (uri.getRawFragment() != null) {
            this.fragment = uri.getRawFragment();
        }
        return this;
    }

    /**
     * Set or append individual URI components of this builder from the values
     * of the given {@link UriComponents} instance.
     * <p>For the semantics of each component (i.e. set vs append) check the
     * builder methods on this class. For example {@link #host(String)} sets
     * while {@link #path(String)} appends.
     *
     * @param uriComponents the UriComponents to copy from
     * @return this UriComponentsBuilder
     */
    public UriComponentsBuilder uriComponents(UriComponents uriComponents) {
        Preconditions.checkNotNull(uriComponents, "UriComponents must not be null");
        uriComponents.copyToUriComponentsBuilder(this);
        return this;
    }

    public UriComponentsBuilder scheme(@Nullable String scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * Set the URI scheme-specific-part. When invoked, this method overwrites
     * {@linkplain #userInfo(String) user-info}, {@linkplain #host(String) host},
     * {@linkplain #port(int) port}, {@linkplain #path(String) path}, and
     * {@link #query(String) query}.
     *
     * @param ssp the URI scheme-specific-part, may contain URI template parameters
     * @return this UriComponentsBuilder
     */
    public UriComponentsBuilder schemeSpecificPart(String ssp) {
        this.ssp = ssp;
        resetHierarchicalComponents();
        return this;
    }

    public UriComponentsBuilder userInfo(@Nullable String userInfo) {
        this.userInfo = userInfo;
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder host(@Nullable String host) {
        this.host = host;
        if (host != null) {
            resetSchemeSpecificPart();
        }
        return this;
    }

    public UriComponentsBuilder port(int port) {
        Preconditions.checkTrue(port >= -1, "Port must be >= -1");
        this.port = String.valueOf(port);
        if (port > -1) {
            resetSchemeSpecificPart();
        }
        return this;
    }

    public UriComponentsBuilder port(@Nullable String port) {
        this.port = port;
        if (port != null) {
            resetSchemeSpecificPart();
        }
        return this;
    }

    public UriComponentsBuilder path(String path) {
        this.pathBuilder.addPath(path);
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder pathSegment(String... pathSegments) throws IllegalArgumentException {
        this.pathBuilder.addPathSegments(pathSegments);
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder replacePath(@Nullable String path) {
        this.pathBuilder = new CompositePathComponentBuilder();
        if (path != null) {
            this.pathBuilder.addPath(path);
        }
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder query(@Nullable String query) {
        if (query != null) {
            RegexpMatcher matcher = QUERY_PARAM_PATTERN.matcher(query);
            while (matcher.find()) {
                String name = matcher.group(1);
                String eq = matcher.group(2);
                String value = matcher.group(3);
                queryParam(name, (value != null ? value : (Strings.isNotEmpty(eq) ? "" : null)));
            }
            resetSchemeSpecificPart();
        } else {
            this.queryParams.clear();
        }
        return this;
    }

    public UriComponentsBuilder replaceQuery(@Nullable String query) {
        this.queryParams.clear();
        if (query != null) {
            query(query);
            resetSchemeSpecificPart();
        }
        return this;
    }

    /**
     * 在 uri 后面拼接 query param
     */
    public UriComponentsBuilder queryParam(String name, Object... values) {
        Preconditions.checkNotNull(name, "Name must not be null");
        if (!Objs.isEmpty(values)) {
            for (Object value : values) {
                String valueAsString = getQueryParamValue(value);
                this.queryParams.add(name, valueAsString);
            }
        } else {
            this.queryParams.add(name, null);
        }
        resetSchemeSpecificPart();
        return this;
    }



    public UriComponentsBuilder queryParam(String name, @Nullable Collection<?> values) {
        return queryParam(name, (Objs.isEmpty(values) ? EMPTY_VALUES : values.toArray()));
    }

    public UriComponentsBuilder queryParamIfPresent(String name, Holder<?> valueHolder) {
        if (!valueHolder.isNull()) {
            Object v = valueHolder.get();
            if (v instanceof Collection<?>) {
                Collection<?> values = (Collection<?>) v;
                queryParam(name, values);
            } else {
                queryParam(name, v);
            }
        }
        return this;
    }

    public UriComponentsBuilder queryParams(@Nullable MultiValueMap<String, Object> params) {
        if (params != null) {
            this.queryParams.addAll(params);
            resetSchemeSpecificPart();
        }
        return this;
    }

    public UriComponentsBuilder replaceQueryParam(String name, Object... values) {
        Preconditions.checkNotNull(name, "Name must not be null");
        this.queryParams.remove(name);
        if (Objs.isNotEmpty(values)) {
            queryParam(name, values);
        }
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder replaceQueryParam(String name, @Nullable Collection<?> values) {
        return replaceQueryParam(name, (Objs.isEmpty(values) ? EMPTY_VALUES : values.toArray()));
    }

    /**
     * {@inheritDoc}
     */
    public UriComponentsBuilder replaceQueryParams(@Nullable MultiValueMap<String, Object> params) {
        this.queryParams.clear();
        if (params != null) {
            this.queryParams.putAll(params);
        }
        return this;
    }

    public UriComponentsBuilder fragment(@Nullable String fragment) {
        if (fragment != null) {
            Preconditions.checkNotEmpty(fragment, "Fragment must not be empty");
            this.fragment = fragment;
        } else {
            this.fragment = null;
        }
        return this;
    }

    /**
     * Configure URI variables to be expanded at build time.
     * <p>The provided variables may be a subset of all required ones. At build
     * time, the available ones are expanded, while unresolved URI placeholders
     * are left in place and can still be expanded later.
     * <p>In contrast to {@link UriComponents#replaceVariables(Map)}, this method is useful when you need to
     * supply URI variables without building the {@link UriComponents} instance
     * just yet, or perhaps pre-expand some shared default values such as host
     * and port.
     *
     * @param uriVariables the URI variables to use
     * @return this UriComponentsBuilder
     */
    public UriComponentsBuilder uriVariables(Map<String, Object> uriVariables) {
        this.uriVariables.putAll(uriVariables);
        return this;
    }


    private void resetHierarchicalComponents() {
        this.userInfo = null;
        this.host = null;
        this.port = null;
        this.pathBuilder = new CompositePathComponentBuilder();
        this.queryParams.clear();
    }

    private void resetSchemeSpecificPart() {
        this.ssp = null;
    }


    /**
     * Public declaration of Object's {@code clone()} method.
     * Delegates to {@link #cloneBuilder()}.
     */
    @Override
    public Object clone() {
        return cloneBuilder();
    }

    /**
     * Clone this {@code UriComponentsBuilder}.
     *
     * @return the cloned {@code UriComponentsBuilder} object
     */
    public UriComponentsBuilder cloneBuilder() {
        return new UriComponentsBuilder(this);
    }


    private enum EncodingHint {ENCODE_TEMPLATE, FULLY_ENCODED, NONE}


    /**
     * Create a builder that is initialized with the given path.
     *
     * @param path the path to initialize with
     * @return the new {@code UriComponentsBuilder}
     */
    public static UriComponentsBuilder fromPath(String path) {
        UriComponentsBuilder builder = new UriComponentsBuilder();
        builder.path(path);
        return builder;
    }

    /**
     * Create a builder that is initialized from the given {@code URI}.
     * <p><strong>Note:</strong> the components in the resulting builder will be
     * in fully encoded (raw) form and further changes must also supply values
     * that are fully encoded, for example via methods in {@link UriComponentUtils}.
     * In addition please use {@link #build(boolean)} with a value of "true" to
     * build the {@link UriComponents} instance in order to indicate that the
     * components are encoded.
     *
     * @param uri the URI to initialize with
     * @return the new {@code UriComponentsBuilder}
     */
    public static UriComponentsBuilder fromUri(URI uri) {
        UriComponentsBuilder builder = new UriComponentsBuilder();
        builder.uri(uri);
        return builder;
    }

    /**
     * Create a builder that is initialized with the given URI string.
     * <p><strong>Note:</strong> The presence of reserved characters can prevent
     * correct parsing of the URI string. For example if a query parameter
     * contains {@code '='} or {@code '&'} characters, the query string cannot
     * be parsed unambiguously. Such values should be substituted for URI
     * variables to enable correct parsing:
     * <pre class="code">
     * String uriString = &quot;/hotels/42?filter={value}&quot;;
     * UriComponentsBuilder.fromUriString(uriString).buildAndExpand(&quot;hot&amp;cold&quot;);
     * </pre>
     *
     * @param uri the URI string to initialize with
     * @return the new {@code UriComponentsBuilder}
     */
    public static UriComponentsBuilder fromUriString(String uri) {
        Preconditions.checkNotNull(uri, "URI must not be null");
        RegexpMatcher matcher = URI_PATTERN.matcher(uri);
        if (matcher.matches()) {
            UriComponentsBuilder builder = new UriComponentsBuilder();
            String scheme = matcher.group(2);
            String userInfo = matcher.group(5);
            String host = matcher.group(6);
            String port = matcher.group(8);
            String path = matcher.group(9);
            String query = matcher.group(11);
            String fragment = matcher.group(13);
            boolean opaque = false;
            if (Strings.isNotEmpty(scheme)) {
                String rest = uri.substring(scheme.length());
                if (!rest.startsWith(":/")) {
                    opaque = true;
                }
            }
            builder.scheme(scheme);
            if (opaque) {
                String ssp = uri.substring(scheme.length() + 1);
                if (Strings.isNotEmpty(fragment)) {
                    ssp = ssp.substring(0, ssp.length() - (fragment.length() + 1));
                }
                builder.schemeSpecificPart(ssp);
            } else {
                if (Strings.isNotEmpty(scheme) && scheme.startsWith("http") && !Strings.isNotEmpty(host)) {
                    throw new IllegalArgumentException("[" + uri + "] is not a valid HTTP URL");
                }
                builder.userInfo(userInfo);
                builder.host(host);
                if (Strings.isNotEmpty(port)) {
                    builder.port(port);
                }
                builder.path(path);
                builder.query(query);
            }
            if (Strings.isNotBlank(fragment)) {
                builder.fragment(fragment);
            }
            return builder;
        } else {
            throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
        }
    }

    /**
     * Create a URI components builder from the given HTTP URL String.
     * <p><strong>Note:</strong> The presence of reserved characters can prevent
     * correct parsing of the URI string. For example if a query parameter
     * contains {@code '='} or {@code '&'} characters, the query string cannot
     * be parsed unambiguously. Such values should be substituted for URI
     * variables to enable correct parsing:
     * <pre class="code">
     * String urlString = &quot;https://example.com/hotels/42?filter={value}&quot;;
     * UriComponentsBuilder.fromHttpUrl(urlString).buildAndExpand(&quot;hot&amp;cold&quot;);
     * </pre>
     *
     * @param httpUrl the source URI
     * @return the URI components of the URI
     */
    public static UriComponentsBuilder fromHttpUrl(String httpUrl) {
        Preconditions.checkNotNull(httpUrl, "HTTP URL must not be null");
        RegexpMatcher matcher = HTTP_URL_PATTERN.matcher(httpUrl);
        if (matcher.matches()) {
            UriComponentsBuilder builder = new UriComponentsBuilder();
            String scheme = matcher.group(1);
            builder.scheme(scheme != null ? scheme.toLowerCase() : null);
            builder.userInfo(matcher.group(4));
            String host = matcher.group(5);
            if (Strings.isNotEmpty(scheme) && Strings.isEmpty(host)) {
                throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
            }
            builder.host(host);
            String port = matcher.group(7);
            if (Strings.isNotEmpty(port)) {
                builder.port(port);
            }
            builder.path(matcher.group(8));
            builder.query(matcher.group(10));
            String fragment = matcher.group(12);
            if (Strings.isNotBlank(fragment)) {
                builder.fragment(fragment);
            }
            return builder;
        } else {
            throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
        }
    }


    /**
     * Create an instance by parsing the "Origin" header of an HTTP request.
     *
     * @see <a href="https://tools.ietf.org/html/rfc6454">RFC 6454</a>
     */
    public static UriComponentsBuilder fromOriginHeader(String origin) {
        RegexpMatcher matcher = URI_PATTERN.matcher(origin);
        if (matcher.matches()) {
            UriComponentsBuilder builder = new UriComponentsBuilder();
            String scheme = matcher.group(2);
            String host = matcher.group(6);
            String port = matcher.group(8);
            if (Strings.isNotEmpty(scheme)) {
                builder.scheme(scheme);
            }
            builder.host(host);
            if (Strings.isNotEmpty(port)) {
                builder.port(port);
            }
            return builder;
        } else {
            throw new IllegalArgumentException("[" + origin + "] is not a valid \"Origin\" header value");
        }
    }

}
