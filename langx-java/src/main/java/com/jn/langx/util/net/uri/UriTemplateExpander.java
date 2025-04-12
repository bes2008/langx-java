package com.jn.langx.util.net.uri;

import java.net.URI;
import java.util.Map;

/**
 * Defines methods for expanding a URI template with variables.
 *
 * @since 5.4.7
 */
public interface UriTemplateExpander {

    /**
     * Expand the given URI template with a map of URI variables.
     *
     * @param uriTemplate  the URI template
     * @param uriVariables variable values
     * @return the created URI instance
     */
    URI expand(String uriTemplate, Map<String, ?> uriVariables);

    /**
     * Expand the given URI template with an array of URI variables.
     *
     * @param uriTemplate  the URI template
     * @param uriVariables variable values
     * @return the created URI instance
     */
    URI expand(String uriTemplate, Object... uriVariables);

}
