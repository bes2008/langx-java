package com.jn.langx.http;


import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.http.mime.MediaType;
import com.jn.langx.util.StringJoiner;
import com.jn.langx.util.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.LinkedCaseInsensitiveMap;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMapAdapter;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.net.NetworkAddress;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jn.langx.util.io.Charsets.ISO_8859_1;


public class HttpHeaders implements MultiValueMap<String, String>, Serializable {

    /**
     * The HTTP {@code Accept} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.2">Section 5.3.2 of RFC 7231</a>
     */
    public static final String ACCEPT = "Accept";
    /**
     * The HTTP {@code Accept-Charset} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.3">Section 5.3.3 of RFC 7231</a>
     */
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    /**
     * The HTTP {@code Accept-Encoding} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.4">Section 5.3.4 of RFC 7231</a>
     */
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    /**
     * The HTTP {@code Accept-Language} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.5">Section 5.3.5 of RFC 7231</a>
     */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    /**
     * The HTTP {@code Accept-Ranges} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-2.3">Section 5.3.5 of RFC 7233</a>
     */
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    /**
     * The CORS {@code Access-Control-Allow-Credentials} response header field name.
     *
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    /**
     * The CORS {@code Access-Control-Allow-Headers} response header field name.
     *
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    /**
     * The CORS {@code Access-Control-Allow-Methods} response header field name.
     *
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    /**
     * The CORS {@code Access-Control-Allow-Origin} response header field name.
     *
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    /**
     * The CORS {@code Access-Control-Expose-Headers} response header field name.
     *
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    /**
     * The CORS {@code Access-Control-Max-Age} response header field name.
     *
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    /**
     * The CORS {@code Access-Control-Request-Headers} request header field name.
     *
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    /**
     * The CORS {@code Access-Control-Request-Method} request header field name.
     *
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    /**
     * The HTTP {@code Age} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.1">Section 5.1 of RFC 7234</a>
     */
    public static final String AGE = "Age";
    /**
     * The HTTP {@code Allow} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.4.1">Section 7.4.1 of RFC 7231</a>
     */
    public static final String ALLOW = "Allow";
    /**
     * The HTTP {@code Authorization} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-4.2">Section 4.2 of RFC 7235</a>
     */
    public static final String AUTHORIZATION = "Authorization";
    /**
     * The HTTP {@code Cache-Control} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2">Section 5.2 of RFC 7234</a>
     */
    public static final String CACHE_CONTROL = "Cache-Control";
    /**
     * The HTTP {@code Connection} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-6.1">Section 6.1 of RFC 7230</a>
     */
    public static final String CONNECTION = "Connection";
    /**
     * The HTTP {@code Content-Encoding} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.2.2">Section 3.1.2.2 of RFC 7231</a>
     */
    public static final String CONTENT_ENCODING = "Content-Encoding";
    /**
     * The HTTP {@code Content-Disposition} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc6266">RFC 6266</a>
     */
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    /**
     * The HTTP {@code Content-Language} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.3.2">Section 3.1.3.2 of RFC 7231</a>
     */
    public static final String CONTENT_LANGUAGE = "Content-Language";
    /**
     * The HTTP {@code Content-Length} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-3.3.2">Section 3.3.2 of RFC 7230</a>
     */
    public static final String CONTENT_LENGTH = "Content-Length";
    /**
     * The HTTP {@code Content-Location} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.4.2">Section 3.1.4.2 of RFC 7231</a>
     */
    public static final String CONTENT_LOCATION = "Content-Location";
    /**
     * The HTTP {@code Content-Range} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-4.2">Section 4.2 of RFC 7233</a>
     */
    public static final String CONTENT_RANGE = "Content-Range";
    /**
     * The HTTP {@code Content-Type} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.1.5">Section 3.1.1.5 of RFC 7231</a>
     */
    public static final String CONTENT_TYPE = "Content-Type";
    /**
     * The HTTP {@code Cookie} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc2109#section-4.3.4">Section 4.3.4 of RFC 2109</a>
     */
    public static final String COOKIE = "Cookie";
    /**
     * The HTTP {@code Date} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.1.2">Section 7.1.1.2 of RFC 7231</a>
     */
    public static final String DATE = "Date";
    /**
     * The HTTP {@code ETag} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.3">Section 2.3 of RFC 7232</a>
     */
    public static final String ETAG = "ETag";
    /**
     * The HTTP {@code Expect} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.1.1">Section 5.1.1 of RFC 7231</a>
     */
    public static final String EXPECT = "Expect";
    /**
     * The HTTP {@code Expires} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.3">Section 5.3 of RFC 7234</a>
     */
    public static final String EXPIRES = "Expires";
    /**
     * The HTTP {@code From} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.1">Section 5.5.1 of RFC 7231</a>
     */
    public static final String FROM = "From";
    /**
     * The HTTP {@code Host} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-5.4">Section 5.4 of RFC 7230</a>
     */
    public static final String HOST = "Host";
    /**
     * The HTTP {@code If-Match} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.1">Section 3.1 of RFC 7232</a>
     */
    public static final String IF_MATCH = "If-Match";
    /**
     * The HTTP {@code If-Modified-Since} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.3">Section 3.3 of RFC 7232</a>
     */
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    /**
     * The HTTP {@code If-None-Match} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.2">Section 3.2 of RFC 7232</a>
     */
    public static final String IF_NONE_MATCH = "If-None-Match";
    /**
     * The HTTP {@code If-Range} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-3.2">Section 3.2 of RFC 7233</a>
     */
    public static final String IF_RANGE = "If-Range";
    /**
     * The HTTP {@code If-Unmodified-Since} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.4">Section 3.4 of RFC 7232</a>
     */
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    /**
     * The HTTP {@code Last-Modified} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.2">Section 2.2 of RFC 7232</a>
     */
    public static final String LAST_MODIFIED = "Last-Modified";
    /**
     * The HTTP {@code Link} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc5988">RFC 5988</a>
     */
    public static final String LINK = "Link";
    /**
     * The HTTP {@code Location} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.2">Section 7.1.2 of RFC 7231</a>
     */
    public static final String LOCATION = "Location";
    /**
     * The HTTP {@code Max-Forwards} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.1.2">Section 5.1.2 of RFC 7231</a>
     */
    public static final String MAX_FORWARDS = "Max-Forwards";
    /**
     * The HTTP {@code Origin} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc6454">RFC 6454</a>
     */
    public static final String ORIGIN = "Origin";
    /**
     * The HTTP {@code Pragma} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.4">Section 5.4 of RFC 7234</a>
     */
    public static final String PRAGMA = "Pragma";
    /**
     * The HTTP {@code Proxy-Authenticate} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-4.3">Section 4.3 of RFC 7235</a>
     */
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    /**
     * The HTTP {@code Proxy-Authorization} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-4.4">Section 4.4 of RFC 7235</a>
     */
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    /**
     * The HTTP {@code Range} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-3.1">Section 3.1 of RFC 7233</a>
     */
    public static final String RANGE = "Range";
    /**
     * The HTTP {@code Referer} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.2">Section 5.5.2 of RFC 7231</a>
     */
    public static final String REFERER = "Referer";
    /**
     * The HTTP {@code Retry-After} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.3">Section 7.1.3 of RFC 7231</a>
     */
    public static final String RETRY_AFTER = "Retry-After";
    /**
     * The HTTP {@code Server} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.4.2">Section 7.4.2 of RFC 7231</a>
     */
    public static final String SERVER = "Server";
    /**
     * The HTTP {@code Set-Cookie} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc2109#section-4.2.2">Section 4.2.2 of RFC 2109</a>
     */
    public static final String SET_COOKIE = "Set-Cookie";
    /**
     * The HTTP {@code Set-Cookie2} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc2965">RFC 2965</a>
     */
    public static final String SET_COOKIE2 = "Set-Cookie2";
    /**
     * The HTTP {@code TE} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-4.3">Section 4.3 of RFC 7230</a>
     */
    public static final String TE = "TE";
    /**
     * The HTTP {@code Trailer} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-4.4">Section 4.4 of RFC 7230</a>
     */
    public static final String TRAILER = "Trailer";
    /**
     * The HTTP {@code Transfer-Encoding} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-3.3.1">Section 3.3.1 of RFC 7230</a>
     */
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    /**
     * The HTTP {@code Upgrade} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-6.7">Section 6.7 of RFC 7230</a>
     */
    public static final String UPGRADE = "Upgrade";
    /**
     * The HTTP {@code User-Agent} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.3">Section 5.5.3 of RFC 7231</a>
     */
    public static final String USER_AGENT = "User-Agent";
    /**
     * The HTTP {@code Vary} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.4">Section 7.1.4 of RFC 7231</a>
     */
    public static final String VARY = "Vary";
    /**
     * The HTTP {@code Via} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-5.7.1">Section 5.7.1 of RFC 7230</a>
     */
    public static final String VIA = "Via";
    /**
     * The HTTP {@code Warning} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.5">Section 5.5 of RFC 7234</a>
     */
    public static final String WARNING = "Warning";
    /**
     * The HTTP {@code WWW-Authenticate} header field name.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-4.1">Section 4.1 of RFC 7235</a>
     */
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    /**
     * An empty {@code HttpHeaders} instance (immutable).
     */
    public static final HttpHeaders EMPTY = new HttpHeaders(new LinkedMultiValueMap(0));
    private static final long serialVersionUID = -8578554704772377436L;
    /**
     * Pattern matching ETag multiple field values in headers such as "If-Match", "If-None-Match".
     *
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.3">Section 2.3 of RFC 7232</a>
     */
    private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");

    private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.ENGLISH);

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    final MultiValueMap<String, String> headers;
    /**
     * Date formats with time zone as specified in the HTTP RFC to use for formatting.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.1.1">Section 7.1.1.1 of RFC 7231</a>
     */

    private final SimpleDateFormat dateFormatter = Dates.getSimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", GMT, Locale.US);
    /**
     * Date formats with time zone as specified in the HTTP RFC to use for parsing.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.1.1">Section 7.1.1.1 of RFC 7231</a>
     */
    private final SimpleDateFormat[] dateParsers = new SimpleDateFormat[]{
            Dates.getSimpleDateFormat("EEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
            Dates.getSimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", GMT, Locale.US)
    };


    /**
     * Construct a new, empty instance of the {@code HttpHeaders} object.
     * <p>This is the common constructor, using a case-insensitive map structure.
     */
    public HttpHeaders() {
        this(new MultiValueMapAdapter<String, String>(new LinkedCaseInsensitiveMap(8, Locale.ENGLISH)));
    }

    /**
     * Construct a new {@code HttpHeaders} instance backed by an existing map.
     * <p>This constructor is available as an optimization for adapting to existing
     * headers map structures, primarily for internal use within the framework.
     *
     * @param headers the headers map (expected to operate with case-insensitive keys)
     */
    public HttpHeaders(MultiValueMap<String, String> headers) {
        Preconditions.checkNotNull(headers, "MultiValueMap must not be null");
        this.headers = headers;
    }

    /**
     * Return an {@code HttpHeaders} object that can be read and written to.
     */
    public static HttpHeaders writableHttpHeaders(HttpHeaders headers) {
        Preconditions.checkNotNull(headers, "HttpHeaders must not be null");
        if (headers == EMPTY) {
            return new HttpHeaders();
        } else {
            return headers;
        }
    }

    /**
     * Helps to format HTTP header values, as HTTP header values themselves can
     * contain comma-separated values, can become confusing with regular
     * {@link Map} formatting that also uses commas between entries.
     *
     * @param headers the headers to format
     * @return the headers to a String
     */
    public static String formatHeaders(MultiValueMap<String, String> headers) {
        return "[" + Strings.join(", ", Pipeline.of(headers.entrySet())
                .map(new Function<Entry<String, Collection<String>>, String>() {
                    @Override
                    public String apply(Entry<String, Collection<String>> entry) {
                        Collection<String> values = entry.getValue();
                        return entry.getKey() + ":" + (values.size() == 1 ?
                                "\"" + Collects.asList(values).get(0) + "\"" :
                                Strings.join(", ", Pipeline.of(values)
                                        .map(new Function<String, String>() {
                                            @Override
                                            public String apply(String input) {
                                                return "\"" + input + "\"";
                                            }
                                        }).asList()));
                    }
                }).asList()) + "]";
    }

    /**
     * Encode the given username and password into Basic Authentication credentials.
     * <p>The encoded credentials returned by this method can be supplied to
     * {@link #setBasicAuth(String)} to set the Basic Authentication header.
     *
     * @param username the username
     * @param password the password
     * @param charset  the charset to use to convert the credentials into an octet
     *                 sequence. Defaults to {@linkplain com.jn.langx.util.io.Charsets#ISO_8859_1}.
     * @throws IllegalArgumentException if {@code username} or {@code password}
     *                                  contains characters that cannot be encoded to the given charset
     * @see #setBasicAuth(String)
     * @see #setBasicAuth(String, String)
     * @see #setBasicAuth(String, String, Charset)
     * @see <a href="https://tools.ietf.org/html/rfc7617">RFC 7617</a>
     */
    public static String encodeBasicAuth(String username, String password, @Nullable Charset charset) {
        Preconditions.checkNotNull(username, "Username must not be null");
        Preconditions.checkTrue(!username.contains(":"), "Username must not contain a colon");
        Preconditions.checkNotNull(password, "Password must not be null");
        if (charset == null) {
            charset = ISO_8859_1;
        }

        CharsetEncoder encoder = charset.newEncoder();
        if (!encoder.canEncode(username) || !encoder.canEncode(password)) {
            throw new IllegalArgumentException(
                    "Username or password contains characters that cannot be encoded to " + charset.displayName());
        }

        String credentialsString = username + ":" + password;
        byte[] encodedBytes = Base64.encodeBase64(credentialsString.getBytes(charset));
        return new String(encodedBytes, charset);
    }

    /**
     * Get the list of header values for the given header name, if any.
     *
     * @param headerName the header name
     * @return the list of header values, or an empty list
     */
    public List<String> getOrEmpty(Object headerName) {
        Collection<String> values = get(headerName);
        return (values != null ? Collects.asList(values) : Collects.<String>emptyArrayList());
    }

    /**
     * Return the list of acceptable {@linkplain MediaType media types},
     * as specified by the {@code Accept} header.
     * <p>Returns an empty list when the acceptable media types are unspecified.
     */
    public List<MediaType> getAccept() {
        return MediaType.parseMediaTypes(Collects.asList(get(ACCEPT)));
    }

    /**
     * Set the list of acceptable {@linkplain MediaType media types},
     * as specified by the {@code Accept} header.
     */
    public void setAccept(List<MediaType> acceptableMediaTypes) {
        set(ACCEPT, MediaType.toString(acceptableMediaTypes));
    }

    /**
     * Return the value of the {@code Access-Control-Allow-Credentials} response header.
     */
    public boolean getAccessControlAllowCredentials() {
        return Boolean.parseBoolean(getFirst(ACCESS_CONTROL_ALLOW_CREDENTIALS));
    }

    /**
     * Set the (new) value of the {@code Access-Control-Allow-Credentials} response header.
     */
    public void setAccessControlAllowCredentials(boolean allowCredentials) {
        set(ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.toString(allowCredentials));
    }

    /**
     * Return the value of the {@code Access-Control-Allow-Headers} response header.
     */
    public List<String> getAccessControlAllowHeaders() {
        return getValuesAsList(ACCESS_CONTROL_ALLOW_HEADERS);
    }

    /**
     * Set the (new) value of the {@code Access-Control-Allow-Headers} response header.
     */
    public void setAccessControlAllowHeaders(List<String> allowedHeaders) {
        set(ACCESS_CONTROL_ALLOW_HEADERS, toCommaDelimitedString(allowedHeaders));
    }

    /**
     * Return the value of the {@code Access-Control-Allow-Methods} response header.
     */
    public List<HttpMethod> getAccessControlAllowMethods() {
        List<HttpMethod> result = new ArrayList<HttpMethod>();
        String value = getFirst(ACCESS_CONTROL_ALLOW_METHODS);
        if (value != null) {
            String[] tokens = Strings.split(value, ",");
            for (String token : tokens) {
                HttpMethod resolved = HttpMethod.resolve(token);
                if (resolved != null) {
                    result.add(resolved);
                }
            }
        }
        return result;
    }

    /**
     * Set the (new) value of the {@code Access-Control-Allow-Methods} response header.
     */
    public void setAccessControlAllowMethods(List<HttpMethod> allowedMethods) {
        set(ACCESS_CONTROL_ALLOW_METHODS, Strings.join(",", allowedMethods));
    }

    /**
     * Return the value of the {@code Access-Control-Allow-Origin} response header.
     */
    @Nullable
    public String getAccessControlAllowOrigin() {
        return getFieldValues(ACCESS_CONTROL_ALLOW_ORIGIN);
    }

    /**
     * Set the (new) value of the {@code Access-Control-Allow-Origin} response header.
     */
    public void setAccessControlAllowOrigin(@Nullable String allowedOrigin) {
        setOrRemove(ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrigin);
    }

    /**
     * Return the value of the {@code Access-Control-Expose-Headers} response header.
     */
    public List<String> getAccessControlExposeHeaders() {
        return getValuesAsList(ACCESS_CONTROL_EXPOSE_HEADERS);
    }

    /**
     * Set the (new) value of the {@code Access-Control-Expose-Headers} response header.
     */
    public void setAccessControlExposeHeaders(List<String> exposedHeaders) {
        set(ACCESS_CONTROL_EXPOSE_HEADERS, toCommaDelimitedString(exposedHeaders));
    }

    /**
     * Return the value of the {@code Access-Control-Max-Age} response header.
     * <p>Returns -1 when the max age is unknown.
     */
    public long getAccessControlMaxAge() {
        String value = getFirst(ACCESS_CONTROL_MAX_AGE);
        return (value != null ? Long.parseLong(value) : -1);
    }

    /**
     * Set the (new) value of the {@code Access-Control-Max-Age} response header.
     */
    public void setAccessControlMaxAge(long maxAge) {
        set(ACCESS_CONTROL_MAX_AGE, Long.toString(maxAge));
    }

    /**
     * Return the value of the {@code Access-Control-Request-Headers} request header.
     */
    public List<String> getAccessControlRequestHeaders() {
        return getValuesAsList(ACCESS_CONTROL_REQUEST_HEADERS);
    }

    /**
     * Set the (new) value of the {@code Access-Control-Request-Headers} request header.
     */
    public void setAccessControlRequestHeaders(List<String> requestHeaders) {
        set(ACCESS_CONTROL_REQUEST_HEADERS, toCommaDelimitedString(requestHeaders));
    }

    /**
     * Return the value of the {@code Access-Control-Request-Method} request header.
     */
    @Nullable
    public HttpMethod getAccessControlRequestMethod() {
        return HttpMethod.resolve(getFirst(ACCESS_CONTROL_REQUEST_METHOD));
    }

    /**
     * Set the (new) value of the {@code Access-Control-Request-Method} request header.
     */
    public void setAccessControlRequestMethod(@Nullable HttpMethod requestMethod) {
        setOrRemove(ACCESS_CONTROL_REQUEST_METHOD, (requestMethod != null ? requestMethod.name() : null));
    }

    /**
     * Return the list of acceptable {@linkplain Charset charsets},
     * as specified by the {@code Accept-Charset} header.
     */
    public List<Charset> getAcceptCharset() {
        String value = getFirst(ACCEPT_CHARSET);
        if (value != null) {
            String[] tokens = Strings.split(value, ",");
            List<Charset> result = new ArrayList<Charset>(tokens.length);
            for (String token : tokens) {
                int paramIdx = token.indexOf(';');
                String charsetName;
                if (paramIdx == -1) {
                    charsetName = token;
                } else {
                    charsetName = token.substring(0, paramIdx);
                }
                if (!charsetName.equals("*")) {
                    result.add(Charset.forName(charsetName));
                }
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Set the list of acceptable {@linkplain Charset charsets},
     * as specified by the {@code Accept-Charset} header.
     */
    public void setAcceptCharset(List<Charset> acceptableCharsets) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Charset charset : acceptableCharsets) {
            joiner.add(charset.name().toLowerCase(Locale.ENGLISH));
        }
        set(ACCEPT_CHARSET, joiner.toString());
    }

    /**
     * Return the set of allowed {@link HttpMethod HTTP methods},
     * as specified by the {@code Allow} header.
     * <p>Returns an empty set when the allowed methods are unspecified.
     */
    public Set<HttpMethod> getAllow() {
        String value = getFirst(ALLOW);
        if (Strings.isNotEmpty(value)) {
            String[] tokens = Strings.split(value, ",");
            List<HttpMethod> result = new ArrayList<HttpMethod>(tokens.length);
            for (String token : tokens) {
                HttpMethod resolved = HttpMethod.resolve(token);
                if (resolved != null) {
                    result.add(resolved);
                }
            }
            return EnumSet.copyOf(result);
        } else {
            return EnumSet.noneOf(HttpMethod.class);
        }
    }

    /**
     * Set the set of allowed {@link HttpMethod HTTP methods},
     * as specified by the {@code Allow} header.
     */
    public void setAllow(Set<HttpMethod> allowedMethods) {
        set(ALLOW, Strings.join(",", allowedMethods));
    }

    /**
     * Set the value of the {@linkplain #AUTHORIZATION Authorization} header to
     * Basic Authentication based on the given username and password.
     * <p>Note that this method only supports characters in the
     * {@link com.jn.langx.util.io.Charsets#ISO_8859_1 ISO-8859-1} character set.
     *
     * @param username the username
     * @param password the password
     * @throws IllegalArgumentException if either {@code user} or
     *                                  {@code password} contain characters that cannot be encoded to ISO-8859-1
     * @see #setBasicAuth(String)
     * @see #setBasicAuth(String, String, Charset)
     * @see #encodeBasicAuth(String, String, Charset)
     * @see <a href="https://tools.ietf.org/html/rfc7617">RFC 7617</a>
     */
    public void setBasicAuth(String username, String password) {
        setBasicAuth(username, password, null);
    }

    /**
     * Set the value of the {@linkplain #AUTHORIZATION Authorization} header to
     * Basic Authentication based on the given username and password.
     *
     * @param username the username
     * @param password the password
     * @param charset  the charset to use to convert the credentials into an octet
     *                 sequence. Defaults to {@linkplain com.jn.langx.util.io.Charsets#ISO_8859_1 ISO-8859-1}.
     * @throws IllegalArgumentException if {@code username} or {@code password}
     *                                  contains characters that cannot be encoded to the given charset
     * @see #setBasicAuth(String)
     * @see #setBasicAuth(String, String)
     * @see #encodeBasicAuth(String, String, Charset)
     * @see <a href="https://tools.ietf.org/html/rfc7617">RFC 7617</a>
     */
    public void setBasicAuth(String username, String password, @Nullable Charset charset) {
        setBasicAuth(encodeBasicAuth(username, password, charset));
    }

    /**
     * Set the value of the {@linkplain #AUTHORIZATION Authorization} header to
     * Basic Authentication based on the given {@linkplain #encodeBasicAuth
     * encoded credentials}.
     * <p>Favor this method over {@link #setBasicAuth(String, String)} and
     * {@link #setBasicAuth(String, String, Charset)} if you wish to cache the
     * encoded credentials.
     *
     * @param encodedCredentials the encoded credentials
     * @throws IllegalArgumentException if supplied credentials string is
     *                                  {@code null} or blank
     * @see #setBasicAuth(String, String)
     * @see #setBasicAuth(String, String, Charset)
     * @see #encodeBasicAuth(String, String, Charset)
     * @see <a href="https://tools.ietf.org/html/rfc7617">RFC 7617</a>
     */
    public void setBasicAuth(String encodedCredentials) {
        Preconditions.checkNotEmpty(encodedCredentials, "'encodedCredentials' must not be null or blank");
        set(AUTHORIZATION, "Basic " + encodedCredentials);
    }

    /**
     * Set the value of the {@linkplain #AUTHORIZATION Authorization} header to
     * the given Bearer token.
     *
     * @param token the Base64 encoded token
     * @see <a href="https://tools.ietf.org/html/rfc6750">RFC 6750</a>
     */
    public void setBearerAuth(String token) {
        set(AUTHORIZATION, "Bearer " + token);
    }

    /**
     * Return the value of the {@code Cache-Control} header.
     */
    @Nullable
    public String getCacheControl() {
        return getFieldValues(CACHE_CONTROL);
    }

    /**
     * Set the (new) value of the {@code Cache-Control} header.
     */
    public void setCacheControl(@Nullable String cacheControl) {
        setOrRemove(CACHE_CONTROL, cacheControl);
    }

    /**
     * Return the value of the {@code Connection} header.
     */
    public List<String> getConnection() {
        return getValuesAsList(CONNECTION);
    }

    /**
     * Set the (new) value of the {@code Connection} header.
     */
    public void setConnection(String connection) {
        set(CONNECTION, connection);
    }

    /**
     * Set the (new) value of the {@code Connection} header.
     */
    public void setConnection(List<String> connection) {
        set(CONNECTION, toCommaDelimitedString(connection));
    }

    /**
     * Return the first {@link Locale} of the content languages,
     * as specified by the {@literal Content-Language} header.
     * <p>Returns {@code null} when the content language is unknown.
     * <p>Use {@code getValuesAsList(CONTENT_LANGUAGE)} if you need
     * to get multiple content languages.</p>
     */
    @Nullable
    public Locale getContentLanguage() {
        String language = Pipeline.of(getValuesAsList(CONTENT_LANGUAGE))
                .findFirst();
        if (Emptys.isNotEmpty(language)) {
            return new Locale(language);
        }
        return null;
    }

    /**
     * Return the length of the body in bytes, as specified by the
     * {@code Content-Length} header.
     * <p>Returns -1 when the content-length is unknown.
     */
    public long getContentLength() {
        String value = getFirst(CONTENT_LENGTH);
        return (value != null ? Long.parseLong(value) : -1);
    }

    /**
     * Set the length of the body in bytes, as specified by the
     * {@code Content-Length} header.
     */
    public void setContentLength(long contentLength) {
        set(CONTENT_LENGTH, Long.toString(contentLength));
    }

    /**
     * Return the {@linkplain MediaType media type} of the body, as specified
     * by the {@code Content-Type} header.
     * <p>Returns {@code null} when the content-type is unknown.
     */
    @Nullable
    public MediaType getContentType() {
        String value = getFirst(CONTENT_TYPE);
        return (Strings.isNotEmpty(value) ? MediaType.parseMediaType(value) : null);
    }

    /**
     * Set the {@linkplain MediaType media type} of the body,
     * as specified by the {@code Content-Type} header.
     */
    public void setContentType(@Nullable MediaType mediaType) {
        if (mediaType != null) {
            Preconditions.checkTrue(!mediaType.isWildcardType(), "Content-Type cannot contain wildcard type '*'");
            Preconditions.checkTrue(!mediaType.isWildcardSubtype(), "Content-Type cannot contain wildcard subtype '*'");
            set(CONTENT_TYPE, mediaType.toString());
        } else {
            remove(CONTENT_TYPE);
        }
    }

    /**
     * Return the entity tag of the body, as specified by the {@code ETag} header.
     */
    @Nullable
    public String getETag() {
        return getFirst(ETAG);
    }

    /**
     * Set the (new) entity tag of the body, as specified by the {@code ETag} header.
     */
    public void setETag(@Nullable String etag) {
        if (etag != null) {
            Preconditions.checkTrue(etag.startsWith("\"") || etag.startsWith("W/"),
                    "Invalid ETag: does not start with W/ or \"");
            Preconditions.checkTrue(etag.endsWith("\""), "Invalid ETag: does not end with \"");
            set(ETAG, etag);
        } else {
            remove(ETAG);
        }
    }

    /**
     * Return the value of the {@code Host} header, if available.
     * <p>If the header value does not contain a port, the
     * {@linkplain InetSocketAddress#getPort() port} in the returned address will
     * be {@code 0}.
     */
    @Nullable
    public InetSocketAddress getHost() {
        String value = getFirst(HOST);
        if (value == null) {
            return null;
        }

        String host = null;
        int port = 0;
        int separator = (value.startsWith("[") ? value.indexOf(':', value.indexOf(']')) : value.lastIndexOf(':'));
        if (separator != -1) {
            host = value.substring(0, separator);
            String portString = value.substring(separator + 1);
            try {
                port = Integer.parseInt(portString);
            } catch (NumberFormatException ex) {
                // ignore
            }
        }

        if (host == null) {
            host = value;
        }
        return InetSocketAddress.createUnresolved(host, port);
    }

    /**
     * Set the (new) value of the {@code Host} header.
     * <p>If the given {@linkplain InetSocketAddress#getPort() port} is {@code 0},
     * the host header will only contain the
     */
    public void setHost(@Nullable NetworkAddress host) {
        if (host != null) {
            String value = host.toString();
            set(HOST, value);
        } else {
            remove(HOST);
        }
    }

    /**
     * Return the value of the {@code If-Match} header.
     *
     * @throws IllegalArgumentException if parsing fails
     */
    public List<String> getIfMatch() {
        return getETagValuesAsList(IF_MATCH);
    }

    /**
     * Set the (new) value of the {@code If-Match} header.
     */
    public void setIfMatch(String ifMatch) {
        set(IF_MATCH, ifMatch);
    }

    /**
     * Set the (new) value of the {@code If-Match} header.
     */
    public void setIfMatch(List<String> ifMatchList) {
        set(IF_MATCH, toCommaDelimitedString(ifMatchList));
    }

    /**
     * Return the value of the {@code If-None-Match} header.
     *
     * @throws IllegalArgumentException if parsing fails
     */
    public List<String> getIfNoneMatch() {
        return getETagValuesAsList(IF_NONE_MATCH);
    }

    /**
     * Set the (new) value of the {@code If-None-Match} header.
     */
    public void setIfNoneMatch(String ifNoneMatch) {
        set(IF_NONE_MATCH, ifNoneMatch);
    }

    /**
     * Set the (new) values of the {@code If-None-Match} header.
     */
    public void setIfNoneMatch(List<String> ifNoneMatchList) {
        set(IF_NONE_MATCH, toCommaDelimitedString(ifNoneMatchList));
    }

    /**
     * Return the (new) location of a resource
     * as specified by the {@code Location} header.
     * <p>Returns {@code null} when the location is unknown.
     */
    @Nullable
    public URI getLocation() {
        String value = getFirst(LOCATION);
        return (value != null ? URI.create(value) : null);
    }

    /**
     * Set the (new) location of a resource,
     * as specified by the {@code Location} header.
     */
    public void setLocation(@Nullable URI location) {
        setOrRemove(LOCATION, (location != null ? location.toASCIIString() : null));
    }

    /**
     * Return the value of the {@code Origin} header.
     */
    @Nullable
    public String getOrigin() {
        return getFirst(ORIGIN);
    }

    /**
     * Set the (new) value of the {@code Origin} header.
     */
    public void setOrigin(@Nullable String origin) {
        setOrRemove(ORIGIN, origin);
    }

    /**
     * Return the value of the {@code Pragma} header.
     */
    @Nullable
    public String getPragma() {
        return getFirst(PRAGMA);
    }

    /**
     * Set the (new) value of the {@code Pragma} header.
     */
    public void setPragma(@Nullable String pragma) {
        setOrRemove(PRAGMA, pragma);
    }

    /**
     * Return the value of the {@code Range} header.
     * <p>Returns an empty list when the range is unknown.
     */
    public List<HttpRange> getRange() {
        String value = getFirst(RANGE);
        return HttpRange.parseRanges(value);
    }

    /**
     * Sets the (new) value of the {@code Range} header.
     */
    public void setRange(List<HttpRange> ranges) {
        String value = HttpRange.toString(ranges);
        set(RANGE, value);
    }

    public void setExpires(long time) {
        set(EXPIRES, this.dateFormatter.format(new Date(time)));
    }

    public Date getExpires() {
        String value = getFirst(EXPIRES);
        if (Strings.isNotEmpty(value)) {
            try {
                Date date = this.dateFormatter.parse(value);
            } catch (ParseException ex) {

            }
        }
        return null;
    }

    /**
     * Return the value of the {@code Upgrade} header.
     */
    @Nullable
    public String getUpgrade() {
        return getFirst(UPGRADE);
    }

    /**
     * Set the (new) value of the {@code Upgrade} header.
     */
    public void setUpgrade(@Nullable String upgrade) {
        setOrRemove(UPGRADE, upgrade);
    }

    /**
     * Return the request header names subject to content negotiation.
     */
    public List<String> getVary() {
        return getValuesAsList(VARY);
    }

    /**
     * Set the request header names (e.g. "Accept-Language") for which the
     * response is subject to content negotiation and variances based on the
     * value of those request headers.
     *
     * @param requestHeaders the request header names
     */
    public void setVary(List<String> requestHeaders) {
        set(VARY, toCommaDelimitedString(requestHeaders));
    }

    /**
     * Return all values of a given header name,
     * even if this header is set multiple times.
     *
     * @param headerName the header name
     * @return all associated values
     */
    public List<String> getValuesAsList(String headerName) {
        List<String> values = get(headerName);
        if (values != null) {
            List<String> result = new ArrayList<String>();
            for (String value : values) {
                if (value != null) {
                    Collections.addAll(result, Strings.split(value, ","));
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    /**
     * Remove the well-known {@code "Content-*"} HTTP headers.
     * <p>Such headers should be cleared from the response if the intended
     * body can't be written due to errors.
     */
    public void clearContentHeaders() {
        this.headers.remove(HttpHeaders.CONTENT_DISPOSITION);
        this.headers.remove(HttpHeaders.CONTENT_ENCODING);
        this.headers.remove(HttpHeaders.CONTENT_LANGUAGE);
        this.headers.remove(HttpHeaders.CONTENT_LENGTH);
        this.headers.remove(HttpHeaders.CONTENT_LOCATION);
        this.headers.remove(HttpHeaders.CONTENT_RANGE);
        this.headers.remove(HttpHeaders.CONTENT_TYPE);
    }

    /**
     * Retrieve a combined result from the field values of the ETag header.
     *
     * @param headerName the header name
     * @return the combined result
     * @throws IllegalArgumentException if parsing fails
     */
    protected List<String> getETagValuesAsList(String headerName) {
        List<String> values = get(headerName);
        if (values != null) {
            List<String> result = new ArrayList<String>();
            for (String value : values) {
                if (value != null) {
                    Matcher matcher = ETAG_HEADER_VALUE_PATTERN.matcher(value);
                    while (matcher.find()) {
                        if ("*".equals(matcher.group())) {
                            result.add(matcher.group());
                        } else {
                            result.add(matcher.group(1));
                        }
                    }
                    if (result.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Could not parse header '" + headerName + "' with value '" + value + "'");
                    }
                }
            }
            return result;
        }
        return Collections.emptyList();
    }


    // MultiValueMap implementation

    /**
     * Retrieve a combined result from the field values of multi-valued headers.
     *
     * @param headerName the header name
     * @return the combined result
     */
    @Nullable
    protected String getFieldValues(String headerName) {
        List<String> headerValues = get(headerName);
        return (headerValues != null ? toCommaDelimitedString(headerValues) : null);
    }

    /**
     * Turn the given list of header values into a comma-delimited result.
     *
     * @param headerValues the list of header values
     * @return a combined result with comma delimitation
     */
    protected String toCommaDelimitedString(List<String> headerValues) {
        StringJoiner joiner = new StringJoiner(", ");
        for (String val : headerValues) {
            if (val != null) {
                joiner.add(val);
            }
        }
        return joiner.toString();
    }

    /**
     * Set the given header value, or remove the header if {@code null}.
     *
     * @param headerName  the header name
     * @param headerValue the header value, or {@code null} for none
     */
    private void setOrRemove(String headerName, @Nullable String headerValue) {
        if (headerValue != null) {
            set(headerName, headerValue);
        } else {
            remove(headerName);
        }
    }

    /**
     * Return the first header value for the given header name, if any.
     *
     * @param headerName the header name
     * @return the first header value, or {@code null} if none
     */
    @Override
    @Nullable
    public String getFirst(String headerName) {
        return this.headers.getFirst(headerName);
    }

    /**
     * Add the given, single header value under the given name.
     *
     * @param headerName  the header name
     * @param headerValue the header value
     * @throws UnsupportedOperationException if adding headers is not supported
     * @see #set(String, String)
     */
    @Override
    public void add(String headerName, @Nullable String headerValue) {
        this.headers.add(headerName, headerValue);
    }

    @Override
    public void addAll(String key, Collection<? extends String> values) {
        this.headers.addAll(key, values);
    }

    @Override
    public void addAll(MultiValueMap<String, String> values) {
        this.headers.addAll(values);
    }


    // Map implementation

    /**
     * Set the given, single header value under the given name.
     *
     * @param headerName  the header name
     * @param headerValue the header value
     * @throws UnsupportedOperationException if adding headers is not supported
     * @see #add(String, String)
     */
    @Override
    public void set(String headerName, @Nullable String headerValue) {
        this.headers.set(headerName, headerValue);
    }

    @Override
    public void setAll(Map<String, String> values) {
        this.headers.setAll(values);
    }

    @Override
    public Map<String, String> toSingleValueMap() {
        return this.headers.toSingleValueMap();
    }

    @Override
    public int size() {
        return this.headers.size();
    }

    @Override
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    @Override
    @Nullable
    public List<String> get(Object key) {
        Collection<String> vs = this.headers.get(key);
        if (vs == null) {
            return null;
        }
        return Collects.asList(vs);
    }

    @Override
    public Collection<String> put(String key, Collection<String> value) {
        return this.headers.put(key, value);
    }

    @Override
    public Collection<String> remove(Object key) {
        return this.headers.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Collection<String>> map) {
        this.headers.putAll(map);
    }

    @Override
    public void clear() {
        this.headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.headers.keySet();
    }

    @Override
    public Collection<Collection<String>> values() {
        return this.headers.values();
    }

    @Override
    public Set<Map.Entry<String, Collection<String>>> entrySet() {
        return this.headers.entrySet();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HttpHeaders)) {
            return false;
        }
        HttpHeaders otherHeaders = (HttpHeaders) other;
        return this.headers.equals(otherHeaders.headers);
    }

    @Override
    public int hashCode() {
        return this.headers.hashCode();
    }

    @Override
    public String toString() {
        return formatHeaders(this.headers);
    }

    @Override
    public void addIfAbsent(String key, String value) {
        if (!containsKey(key)) {
            add(key, value);
        }
    }


    @Override
    public int total() {
        return headers.total();
    }
}
