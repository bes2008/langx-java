package com.jn.langx.util.net.uri.component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

import java.util.Map;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.io.Charsets;

/**
 * Utility methods for URI encoding and decoding based on RFC 3986.
 *
 * <p>There are two types of encode methods:
 * <ul>
 * <li>{@code "encodeXyz"} -- these encode a specific URI component (e.g. path,
 * query) by percent encoding illegal characters, which includes non-US-ASCII
 * characters, and also characters that are otherwise illegal within the given
 * URI component type, as defined in RFC 3986. The effect of this method, with
 * regards to encoding, is comparable to using the multi-argument constructor
 * of {@link URI}.
 * <li>{@code "encode"} and {@code "encodeUriVariables"} -- these can be used
 * to encode URI variable values by percent encoding all characters that are
 * either illegal, or have any reserved meaning, anywhere within a URI.
 * </ul>
 *
 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>
 * @since 5.4.7
 */
public abstract class UriComponentUtils {

    /**
     * Encode the given URI scheme with the given encoding.
     *
     * @param scheme   the scheme to be encoded
     * @param encoding the character encoding to encode to
     * @return the encoded scheme
     */
    public static String encodeScheme(String scheme, String encoding) {
        return encodeUriComponent(scheme, encoding, UriComponentType.SCHEME);
    }

    /**
     * Encode the given URI scheme with the given encoding.
     *
     * @param scheme  the scheme to be encoded
     * @param charset the character encoding to encode to
     * @return the encoded scheme
     */
    public static String encodeScheme(String scheme, Charset charset) {
        return encodeUriComponent(scheme, charset, UriComponentType.SCHEME);
    }

    /**
     * Encode the given URI authority with the given encoding.
     *
     * @param authority the authority to be encoded
     * @param encoding  the character encoding to encode to
     * @return the encoded authority
     */
    public static String encodeAuthority(String authority, String encoding) {
        return encodeUriComponent(authority, encoding, UriComponentType.AUTHORITY);
    }

    /**
     * Encode the given URI authority with the given encoding.
     *
     * @param authority the authority to be encoded
     * @param charset   the character encoding to encode to
     * @return the encoded authority
     */
    public static String encodeAuthority(String authority, Charset charset) {
        return encodeUriComponent(authority, charset, UriComponentType.AUTHORITY);
    }

    /**
     * Encode the given URI user info with the given encoding.
     *
     * @param userInfo the user info to be encoded
     * @param encoding the character encoding to encode to
     * @return the encoded user info
     */
    public static String encodeUserInfo(String userInfo, String encoding) {
        return encodeUriComponent(userInfo, encoding, UriComponentType.USER_INFO);
    }

    /**
     * Encode the given URI user info with the given encoding.
     *
     * @param userInfo the user info to be encoded
     * @param charset  the character encoding to encode to
     * @return the encoded user info
     */
    public static String encodeUserInfo(String userInfo, Charset charset) {
        return encodeUriComponent(userInfo, charset, UriComponentType.USER_INFO);
    }

    /**
     * Encode the given URI host with the given encoding.
     *
     * @param host     the host to be encoded
     * @param encoding the character encoding to encode to
     * @return the encoded host
     */
    public static String encodeHost(String host, String encoding) {
        return encodeUriComponent(host, encoding, UriComponentType.HOST_IPV4);
    }

    /**
     * Encode the given URI host with the given encoding.
     *
     * @param host    the host to be encoded
     * @param charset the character encoding to encode to
     * @return the encoded host
     */
    public static String encodeHost(String host, Charset charset) {
        return encodeUriComponent(host, charset, UriComponentType.HOST_IPV4);
    }

    /**
     * Encode the given URI port with the given encoding.
     *
     * @param port     the port to be encoded
     * @param encoding the character encoding to encode to
     * @return the encoded port
     */
    public static String encodePort(String port, String encoding) {
        return encodeUriComponent(port, encoding, UriComponentType.PORT);
    }

    /**
     * Encode the given URI port with the given encoding.
     *
     * @param port    the port to be encoded
     * @param charset the character encoding to encode to
     * @return the encoded port
     */
    public static String encodePort(String port, Charset charset) {
        return encodeUriComponent(port, charset, UriComponentType.PORT);
    }

    /**
     * Encode the given URI path with the given encoding.
     *
     * @param path     the path to be encoded
     * @param encoding the character encoding to encode to
     * @return the encoded path
     */
    public static String encodePath(String path, String encoding) {
        return encodeUriComponent(path, encoding, UriComponentType.PATH);
    }

    /**
     * Encode the given URI path with the given encoding.
     *
     * @param path    the path to be encoded
     * @param charset the character encoding to encode to
     * @return the encoded path
     */
    public static String encodePath(String path, Charset charset) {
        return encodeUriComponent(path, charset, UriComponentType.PATH);
    }

    /**
     * Encode the given URI path segment with the given encoding.
     *
     * @param segment  the segment to be encoded
     * @param encoding the character encoding to encode to
     * @return the encoded segment
     */
    public static String encodePathSegment(String segment, String encoding) {
        return encodeUriComponent(segment, encoding, UriComponentType.PATH_SEGMENT);
    }

    /**
     * Encode the given URI path segment with the given encoding.
     *
     * @param segment the segment to be encoded
     * @param charset the character encoding to encode to
     * @return the encoded segment
     */
    public static String encodePathSegment(String segment, Charset charset) {
        return encodeUriComponent(segment, charset, UriComponentType.PATH_SEGMENT);
    }

    /**
     * Encode the given URI query with the given encoding.
     *
     * @param query    the query to be encoded
     * @param encoding the character encoding to encode to
     * @return the encoded query
     */
    public static String encodeQuery(String query, String encoding) {
        return encodeUriComponent(query, encoding, UriComponentType.QUERY);
    }

    /**
     * Encode the given URI query with the given encoding.
     *
     * @param query   the query to be encoded
     * @param charset the character encoding to encode to
     * @return the encoded query
     */
    public static String encodeQuery(String query, Charset charset) {
        return encodeUriComponent(query, charset, UriComponentType.QUERY);
    }

    /**
     * Encode the given URI query parameter with the given encoding.
     *
     * @param queryParam the query parameter to be encoded
     * @param encoding   the character encoding to encode to
     * @return the encoded query parameter
     */
    public static String encodeQueryParam(String queryParam, String encoding) {
        return encodeUriComponent(queryParam, encoding, UriComponentType.QUERY_PARAM);
    }

    /**
     * Encode the given URI query parameter with the given encoding.
     *
     * @param queryParam the query parameter to be encoded
     * @param charset    the character encoding to encode to
     * @return the encoded query parameter
     */
    public static String encodeQueryParam(String queryParam, Charset charset) {
        return encodeUriComponent(queryParam, charset, UriComponentType.QUERY_PARAM);
    }

    /**
     * Encode the query parameters from the given {@code MultiValueMap} with UTF-8.
     * <p>This can be used with `UriComponentsBuilder#queryParams(MultiValueMap)`
     * when building a URI from an already encoded template.
     * <pre class="code">{@code
     * MultiValueMap<String, String> params = new LinkedMultiValueMap<>(2);
     * // add to params...
     *
     * ServletUriComponentsBuilder.fromCurrentRequest()
     *         .queryParams(UriUtils.encodeQueryParams(params))
     *         .build(true)
     *         .toUriString();
     * }</pre>
     *
     * @param params the parameters to encode
     * @return a new {@code MultiValueMap} with the encoded names and values
     */
    public static MultiValueMap<String, String> encodeQueryParams(MultiValueMap<String, String> params) {
        Charset charset = Charsets.UTF_8;
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>(params.size());
        for (Map.Entry<String, Collection<String>> entry : params.entrySet()) {
            for (String value : entry.getValue()) {
                result.add(encodeQueryParam(entry.getKey(), charset), encodeQueryParam(value, charset));
            }
        }
        return result;
    }

    /**
     * Encode the given URI fragment with the given encoding.
     *
     * @param fragment the fragment to be encoded
     * @param encoding the character encoding to encode to
     * @return the encoded fragment
     */
    public static String encodeFragment(String fragment, String encoding) {
        return encodeUriComponent(fragment, encoding, UriComponentType.FRAGMENT);
    }

    /**
     * Encode the given URI fragment with the given encoding.
     *
     * @param fragment the fragment to be encoded
     * @param charset  the character encoding to encode to
     * @return the encoded fragment
     */
    public static String encodeFragment(String fragment, Charset charset) {
        return encodeUriComponent(fragment, charset, UriComponentType.FRAGMENT);
    }


    /**
     * Variant of {@link #encode(String, Charset)} with a String charset.
     *
     * @param source   the String to be encoded
     * @param encoding the character encoding to encode to
     * @return the encoded String
     */
    public static String encode(String source, String encoding) {
        return encodeUriComponent(source, encoding, UriComponentType.URI);
    }

    /**
     * Encode all characters that are either illegal, or have any reserved
     * meaning, anywhere within a URI, as defined in
     * <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a>.
     * This is useful to ensure that the given String will be preserved as-is
     * and will not have any impact on the structure or meaning of the URI.
     *
     * @param source  the String to be encoded
     * @param charset the character encoding to encode to
     * @return the encoded String
     */
    public static String encode(String source, Charset charset) {
        return encodeUriComponent(source, charset, UriComponentType.URI);
    }

    /**
     * Convenience method to apply {@link #encode(String, Charset)} to all
     * given URI variable values.
     *
     * @param uriVariables the URI variable values to be encoded
     * @return the encoded String
     */
    public static Map<String, String> encodeUriVariables(Map<String, ?> uriVariables) {
        Map<String, String> result = Maps.newLinkedHashMapWithExpectedSize(uriVariables.size());
        for (Map.Entry<String, ?> entry : uriVariables.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String stringValue = (value != null ? value.toString() : "");
            result.put(key, encode(stringValue, Charsets.UTF_8));
        }
        return result;
    }

    /**
     * Convenience method to apply {@link #encode(String, Charset)} to all
     * given URI variable values.
     *
     * @param uriVariables the URI variable values to be encoded
     * @return the encoded String
     */
    public static Object[] encodeUriVariables(Object... uriVariables) {
        return Pipeline.of(uriVariables)
                .map(new Function<Object, String>() {
                    @Override
                    public String apply(Object value) {
                        String stringValue = (value != null ? value.toString() : "");
                        return encode(stringValue, Charsets.UTF_8);
                    }
                })
                .toArray();
    }

    /**
     * Encode the given source into an encoded String using the rules specified
     * by the given component and with the given options.
     *
     * @param source   the source String
     * @param encoding the encoding of the source String
     * @param type     the URI component for the source
     * @return the encoded URI
     * @throws IllegalArgumentException when the given value is not a valid URI component
     */
    public static String encodeUriComponent(String source, String encoding, UriComponentType type) {
        return encodeUriComponent(source, Charset.forName(encoding), type);
    }

    /**
     * Encode the given source into an encoded String using the rules specified
     * by the given component and with the given options.
     *
     * @param source  the source String
     * @param charset the encoding of the source String
     * @param type    the URI component for the source
     * @return the encoded URI
     * @throws IllegalArgumentException when the given value is not a valid URI component
     */
    public static String encodeUriComponent(String source, Charset charset, UriComponentType type) {
        if (Strings.isEmpty(source)) {
            return source;
        }
        Preconditions.checkNotNull(charset, "Charset must not be null");
        Preconditions.checkNotNull(type, "Type must not be null");

        byte[] bytes = source.getBytes(charset);
        boolean original = true;
        for (byte b : bytes) {
            if (!type.isAllowed(b)) {
                original = false;
                break;
            }
        }
        if (original) {
            return source;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
        for (byte b : bytes) {
            if (type.isAllowed(b)) {
                baos.write(b);
            } else {
                baos.write('%');
                char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
                baos.write(hex1);
                baos.write(hex2);
            }
        }
        try {
            return baos.toString(charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Decode the given encoded URI component.
     */
    public static String decodeUriComponent(String source, String encoding) {
        return decodeUriComponent(source, Charset.forName(encoding));
    }

    /**
     * Decode the given encoded URI component value. Based on the following rules:
     * <ul>
     * <li>Alphanumeric characters {@code "a"} through {@code "z"}, {@code "A"} through {@code "Z"},
     * and {@code "0"} through {@code "9"} stay the same.</li>
     * <li>Special characters {@code "-"}, {@code "_"}, {@code "."}, and {@code "*"} stay the same.</li>
     * <li>A sequence "{@code %<i>xy</i>}" is interpreted as a hexadecimal representation of the character.</li>
     * </ul>
     *
     * @param source  the encoded String
     * @param charset the character set
     * @return the decoded value
     * @throws IllegalArgumentException when the given source contains invalid encoded sequences
     * @see java.net.URLDecoder#decode(String, String)
     */
    public static String decodeUriComponent(String source, Charset charset) {
        int length = source.length();
        if (length == 0) {
            return source;
        }
        Preconditions.checkNotNull(charset, "Charset must not be null");

        ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
        boolean changed = false;
        for (int i = 0; i < length; i++) {
            int ch = source.charAt(i);
            if (ch == '%') {
                if (i + 2 < length) {
                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                    }
                    baos.write((char) ((u << 4) + l));
                    i += 2;
                    changed = true;
                } else {
                    throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                }
            } else {
                baos.write(ch);
            }
        }
        if (changed) {
            try {
                return baos.toString(charset.name());
            } catch (IOException ex) {
                throw new IllegalStateException("Failed to decode URI component", ex);
            }
        }
        return source;
    }

    /**
     * Extract the file extension from the given URI path.
     *
     * @param path the URI path (e.g. "/products/index.html")
     * @return the extracted file extension (e.g. "html")
     */
    @Nullable
    public static String extractFileExtension(String path) {
        int end = path.indexOf('?');
        int fragmentIndex = path.indexOf('#');
        if (fragmentIndex != -1 && (end == -1 || fragmentIndex < end)) {
            end = fragmentIndex;
        }
        if (end == -1) {
            end = path.length();
        }
        int begin = path.lastIndexOf('/', end) + 1;
        int paramIndex = path.indexOf(';', begin);
        end = (paramIndex != -1 && paramIndex < end ? paramIndex : end);
        int extIndex = path.lastIndexOf('.', end);
        if (extIndex != -1 && extIndex >= begin) {
            return path.substring(extIndex + 1, end);
        }
        return null;
    }

}
