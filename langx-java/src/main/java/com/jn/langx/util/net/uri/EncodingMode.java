package com.jn.langx.util.net.uri;

import com.jn.langx.util.net.uri.component.UriComponentUtils;
import com.jn.langx.util.net.uri.component.UriComponents;
import com.jn.langx.util.net.uri.component.UriComponentsBuilder;

import java.util.Map;


/**
 * Enum to represent URI encoding strategies. The following are
 * available:
 * <ul>
 * <li>{@link #TEMPLATE_AND_VALUES}
 * <li>{@link #VALUES_ONLY}
 * <li>{@link #URI_COMPONENT}
 * <li>{@link #NONE}
 * </ul>
 *
 * @since 5.5.0
 */
public enum EncodingMode {

    /**
     * Pre-encode the URI template first, then strictly encode URI variables
     * when replaced, with the following rules:
     * <ul>
     * <li>For the URI template replace <em>only</em> non-ASCII and illegal
     * (within a given URI component type) characters with escaped octets.
     * <li>For URI variables do the same and also replace characters with
     * reserved meaning.
     * </ul>
     * <p>For most cases, this mode is most likely to give the expected
     * result because in treats URI variables as opaque data to be fully
     * encoded, while {@link #URI_COMPONENT} by comparison is useful only
     * if intentionally expanding URI variables with reserved characters.
     *
     * @see UriComponentsBuilder#enableEncode() ()
     */
    TEMPLATE_AND_VALUES,

    /**
     * Does not encode the URI template and instead applies strict encoding
     * to URI variables via {@link UriComponentUtils#encodeUriVariables} prior to
     * expanding them into the template.
     *
     * @see UriComponentUtils#encodeUriVariables(Object...)
     * @see UriComponentUtils#encodeUriVariables(Map)
     */
    VALUES_ONLY,

    /**
     * Replace URI variables first, and then encode the resulting URI
     * component values, replacing <em>only</em> non-ASCII and illegal
     * (within a given URI component type) characters, but not characters
     * with reserved meaning.
     *
     * @see UriComponents#encode()
     */
    URI_COMPONENT,

    /**
     * No encoding should be applied.
     */
    NONE
}