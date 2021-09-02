package com.jn.langx.http;

import com.jn.langx.util.Strings;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.net.Nets;
import com.jn.langx.util.struct.pair.StringNameValuePair;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

public class HttpUrlBuilder {

    private String scheme;
    private String encodedSchemeSpecificPart;
    private String encodedAuthority;
    private String userInfo;
    private String encodedUserInfo;
    private String host;
    private int port;
    private String encodedPath;
    private List<String> pathSegments;
    private String encodedQuery;
    private List<StringNameValuePair> queryParams;
    private String query;
    private Charset charset;
    private String fragment;
    private String encodedFragment;

    /**
     * Constructs an empty instance.
     */
    public HttpUrlBuilder() {
        super();
        this.port = -1;
    }

    /**
     * Construct an instance from the string which must be a valid URI.
     *
     * @param url a valid URI in string form
     * @throws URISyntaxException if the input is not a valid URI
     */
    public HttpUrlBuilder(final String url) throws URISyntaxException {
        this(new URI(url), null);
    }

    /**
     * Construct an instance from the provided URI.
     * @param uri
     */
    public HttpUrlBuilder(final URI uri) {
        this(uri, null);
    }

    /**
     * Construct an instance from the string which must be a valid URI.
     *
     * @param url a valid URI in string form
     * @throws URISyntaxException if the input is not a valid URI
     */
    public HttpUrlBuilder(final String url, final Charset charset) throws URISyntaxException {
        this(new URI(url), charset);
    }

    /**
     * Construct an instance from the provided URI.
     * @param uri
     */
    public HttpUrlBuilder(final URI uri, final Charset charset) {
        super();
        setCharset(charset);
        digestURI(uri);
    }

    /**
     * @since 2.8.7
     */
    public HttpUrlBuilder setCharset(final Charset charset) {
        this.charset = charset;
        return this;
    }

    /**
     * @since 2.8.7
     */
    public Charset getCharset() {
        return charset;
    }

    private List <StringNameValuePair> parseQuery(final String query, final Charset charset) {
        if (query != null && !query.isEmpty()) {
            return HttpUrlEncodes.parse(query, charset);
        }
        return null;
    }

    private List <String> parsePath(final String path, final Charset charset) {
        if (path != null && !path.isEmpty()) {
            return HttpUrlEncodes.parsePathSegments(path, charset);
        }
        return null;
    }

    /**
     * Builds a {@link URI} instance.
     */
    public URI build() throws URISyntaxException {
        return new URI(buildString());
    }

    private String buildString() {
        final StringBuilder sb = new StringBuilder();
        if (this.scheme != null) {
            sb.append(this.scheme).append(':');
        }
        if (this.encodedSchemeSpecificPart != null) {
            sb.append(this.encodedSchemeSpecificPart);
        } else {
            if (this.encodedAuthority != null) {
                sb.append("//").append(this.encodedAuthority);
            } else if (this.host != null) {
                sb.append("//");
                if (this.encodedUserInfo != null) {
                    sb.append(this.encodedUserInfo).append("@");
                } else if (this.userInfo != null) {
                    sb.append(encodeUserInfo(this.userInfo)).append("@");
                }
                if (Nets.isValidIpV6Address(this.host)) {
                    sb.append("[").append(this.host).append("]");
                } else {
                    sb.append(this.host);
                }
                if (this.port >= 0) {
                    sb.append(":").append(this.port);
                }
            }
            if (this.encodedPath != null) {
                sb.append(normalizePath(this.encodedPath, sb.length() == 0));
            } else if (this.pathSegments != null) {
                sb.append(encodePath(this.pathSegments));
            }
            if (this.encodedQuery != null) {
                sb.append("?").append(this.encodedQuery);
            } else if (this.queryParams != null && !this.queryParams.isEmpty()) {
                sb.append("?").append(encodeUrlForm(this.queryParams));
            } else if (this.query != null) {
                sb.append("?").append(encodeUric(this.query));
            }
        }
        if (this.encodedFragment != null) {
            sb.append("#").append(this.encodedFragment);
        } else if (this.fragment != null) {
            sb.append("#").append(encodeUric(this.fragment));
        }
        return sb.toString();
    }

    private static String normalizePath(final String path, final boolean relative) {
        String s = path;
        if (Strings.isBlank(s)) {
            return "";
        }
        if (!relative && !s.startsWith("/")) {
            s = "/" + s;
        }
        return s;
    }

    private void digestURI(final URI uri) {
        this.scheme = uri.getScheme();
        this.encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
        this.encodedAuthority = uri.getRawAuthority();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.encodedUserInfo = uri.getRawUserInfo();
        this.userInfo = uri.getUserInfo();
        this.encodedPath = uri.getRawPath();
        this.pathSegments = parsePath(uri.getRawPath(), this.charset != null ? this.charset : Charsets.UTF_8);
        this.encodedQuery = uri.getRawQuery();
        this.queryParams = parseQuery(uri.getRawQuery(), this.charset != null ? this.charset : Charsets.UTF_8);
        this.encodedFragment = uri.getRawFragment();
        this.fragment = uri.getFragment();
    }

    private String encodeUserInfo(final String userInfo) {
        return HttpUrlEncodes.encUserInfo(userInfo, this.charset != null ? this.charset : Charsets.UTF_8);
    }

    private String encodePath(final List<String> pathSegments) {
        return HttpUrlEncodes.formatSegments(pathSegments, this.charset != null ? this.charset : Charsets.UTF_8);
    }

    private String encodeUrlForm(final List<StringNameValuePair> params) {
        return HttpUrlEncodes.format(params, this.charset != null ? this.charset : Charsets.UTF_8);
    }

    private String encodeUric(final String fragment) {
        return HttpUrlEncodes.encUric(fragment, this.charset != null ? this.charset : Charsets.UTF_8);
    }

    /**
     * Sets URI scheme.
     */
    public HttpUrlBuilder setScheme(final String scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * Sets URI user info. The value is expected to be unescaped and may contain non ASCII
     * characters.
     */
    public HttpUrlBuilder setUserInfo(final String userInfo) {
        this.userInfo = userInfo;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        this.encodedUserInfo = null;
        return this;
    }

    /**
     * Sets URI user info as a combination of username and password. These values are expected to
     * be unescaped and may contain non ASCII characters.
     */
    public HttpUrlBuilder setUserInfo(final String username, final String password) {
        return setUserInfo(username + ':' + password);
    }

    /**
     * Sets URI host.
     */
    public HttpUrlBuilder setHost(final String host) {
        this.host = host;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        return this;
    }

    /**
     * Sets URI port.
     */
    public HttpUrlBuilder setPort(final int port) {
        this.port = port < 0 ? -1 : port;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        return this;
    }

    /**
     * Sets URI path. The value is expected to be unescaped and may contain non ASCII characters.
     *
     * @return this.
     */
    public HttpUrlBuilder setPath(final String path) {
        return setPathSegments(path != null ? HttpUrlEncodes.splitPathSegments(path) : null);
    }

    /**
     * Sets URI path. The value is expected to be unescaped and may contain non ASCII characters.
     *
     * @return this.
     *
     * @since 2.8.7
     */
    public HttpUrlBuilder setPathSegments(final String... pathSegments) {
        this.pathSegments = pathSegments.length > 0 ? Arrays.asList(pathSegments) : null;
        this.encodedSchemeSpecificPart = null;
        this.encodedPath = null;
        return this;
    }

    /**
     * Sets URI path. The value is expected to be unescaped and may contain non ASCII characters.
     *
     * @return this.
     *
     * @since 2.8.7
     */
    public HttpUrlBuilder setPathSegments(final List<String> pathSegments) {
        this.pathSegments = pathSegments != null && pathSegments.size() > 0 ? new ArrayList<String>(pathSegments) : null;
        this.encodedSchemeSpecificPart = null;
        this.encodedPath = null;
        return this;
    }

    /**
     * Removes URI query.
     */
    public HttpUrlBuilder removeQuery() {
        this.queryParams = null;
        this.query = null;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    /**
     * Sets URI query parameters. The parameter name / values are expected to be unescaped
     * and may contain non ASCII characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method
     * will remove custom query if present.
     * </p>
     *
     * @since 2.8.7
     */
    public HttpUrlBuilder setParameters(final List <StringNameValuePair> nvps) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<StringNameValuePair>();
        } else {
            this.queryParams.clear();
        }
        this.queryParams.addAll(nvps);
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Adds URI query parameters. The parameter name / values are expected to be unescaped
     * and may contain non ASCII characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method
     * will remove custom query if present.
     * </p>
     *
     * @since 2.8.7
     */
    public HttpUrlBuilder addParameters(final List <StringNameValuePair> nvps) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<StringNameValuePair>();
        }
        this.queryParams.addAll(nvps);
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Sets URI query parameters. The parameter name / values are expected to be unescaped
     * and may contain non ASCII characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method
     * will remove custom query if present.
     * </p>
     *
     * @since 2.8.7
     */
    public HttpUrlBuilder setParameters(final StringNameValuePair... nvps) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<StringNameValuePair>();
        } else {
            this.queryParams.clear();
        }
        for (final StringNameValuePair nvp: nvps) {
            this.queryParams.add(nvp);
        }
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Adds parameter to URI query. The parameter name and value are expected to be unescaped
     * and may contain non ASCII characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method
     * will remove custom query if present.
     * </p>
     */
    public HttpUrlBuilder addParameter(final String param, final String value) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<StringNameValuePair>();
        }
        this.queryParams.add(new StringNameValuePair(param, value));
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Sets parameter of URI query overriding existing value if set. The parameter name and value
     * are expected to be unescaped and may contain non ASCII characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method
     * will remove custom query if present.
     * </p>
     */
    public HttpUrlBuilder setParameter(final String param, final String value) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<StringNameValuePair>();
        }
        if (!this.queryParams.isEmpty()) {
            for (final Iterator<StringNameValuePair> it = this.queryParams.iterator(); it.hasNext(); ) {
                final StringNameValuePair nvp = it.next();
                if (nvp.getName().equals(param)) {
                    it.remove();
                }
            }
        }
        this.queryParams.add(new StringNameValuePair(param, value));
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.query = null;
        return this;
    }

    /**
     * Clears URI query parameters.
     *
     * @since 2.8.7
     */
    public HttpUrlBuilder clearParameters() {
        this.queryParams = null;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    /**
     * Sets custom URI query. The value is expected to be unescaped and may contain non ASCII
     * characters.
     * <p>
     * Please note query parameters and custom query component are mutually exclusive. This method
     * will remove query parameters if present.
     * </p>
     *
     * @since 2.8.7
     */
    public HttpUrlBuilder setCustomQuery(final String query) {
        this.query = query;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        this.queryParams = null;
        return this;
    }

    /**
     * Sets URI fragment. The value is expected to be unescaped and may contain non ASCII
     * characters.
     */
    public HttpUrlBuilder setFragment(final String fragment) {
        this.fragment = fragment;
        this.encodedFragment = null;
        return this;
    }

    /**
     * @since 2.8.7
     */
    public boolean isAbsolute() {
        return this.scheme != null;
    }

    /**
     * @since 2.8.7
     */
    public boolean isOpaque() {
        return this.pathSegments == null && this.encodedPath == null;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getUserInfo() {
        return this.userInfo;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    /**
     * @since 2.8.7
     */
    public boolean isPathEmpty() {
        return (this.pathSegments == null || this.pathSegments.isEmpty()) &&
                (this.encodedPath == null || this.encodedPath.isEmpty());
    }

    /**
     * @since 2.8.7
     */
    public List<String> getPathSegments() {
        return this.pathSegments != null ? new ArrayList<String>(this.pathSegments) : Collections.<String>emptyList();
    }

    public String getPath() {
        if (this.pathSegments == null) {
            return null;
        }
        final StringBuilder result = new StringBuilder();
        for (final String segment : this.pathSegments) {
            result.append('/').append(segment);
        }
        return result.toString();
    }

    /**
     * @since 2.8.7
     */
    public boolean isQueryEmpty() {
        return (this.queryParams == null || this.queryParams.isEmpty()) && this.encodedQuery == null;
    }

    public List<StringNameValuePair> getQueryParams() {
        return this.queryParams != null ? new ArrayList<StringNameValuePair>(this.queryParams) : Collections.<StringNameValuePair>emptyList();
    }

    public String getFragment() {
        return this.fragment;
    }

    @Override
    public String toString() {
        return buildString();
    }

}
