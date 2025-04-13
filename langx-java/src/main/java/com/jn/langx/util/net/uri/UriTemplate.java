package com.jn.langx.util.net.uri;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.net.uri.component.UriComponents;
import com.jn.langx.util.net.uri.component.UriComponentsBuilder;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;

/**
 * Representation of a URI template that can be expanded with URI variables via
 * {@link #expand(Map)}, {@link #expand(Object[])}, or matched to a URL via
 * {@link #match(String)}. This class is designed to be thread-safe and
 * reusable, and allows any number of expand or match calls.
 *
 * <p><strong>Note:</strong> this class uses {@link UriComponentsBuilder}
 * internally to expand URI templates, and is merely a shortcut for already
 * prepared URI templates. For more dynamic preparation and extra flexibility,
 * e.g. around URI encoding, consider using {@code UriComponentsBuilder} or the
 * higher level `DefaultUriBuilderFactory` which adds several encoding
 * modes on top of {@code UriComponentsBuilder}. See the
 * <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-uri-building">reference docs</a>
 * for further details.
 *
 * @since 5.5.0
 */
public class UriTemplate implements Serializable {

    private final String uriTemplate;

    private final UriComponents uriComponents;

    private final List<String> variableNames;

    private final Regexp matchPattern;


    /**
     * Construct a new {@code UriTemplate} with the given URI String.
     *
     * @param uriTemplate the URI template string
     */
    public UriTemplate(String uriTemplate) {
        Preconditions.checkNotEmpty(uriTemplate, "'uriTemplate' must not be null");
        this.uriTemplate = uriTemplate;
        this.uriComponents = UriComponentsBuilder.fromUriString(uriTemplate).build();

        TemplateInfo info = TemplateInfo.parse(uriTemplate);
        this.variableNames = Collections.unmodifiableList(info.getVariableNames());
        this.matchPattern = info.getMatchRegexp();
    }


    /**
     * Return the names of the variables in the template, in order.
     *
     * @return the template variable names
     */
    public List<String> getVariableNames() {
        return this.variableNames;
    }

    /**
     * Given the Map of variables, expands this template into a URI. The Map keys represent variable names,
     * the Map values variable values. The order of variables is not significant.
     * <p>Example:
     * <pre class="code">
     * UriTemplate template = new UriTemplate("https://example.com/hotels/{hotel}/bookings/{booking}");
     * Map&lt;String, String&gt; uriVariables = new HashMap&lt;String, String&gt;();
     * uriVariables.put("booking", "42");
     * uriVariables.put("hotel", "Rest &amp; Relax");
     * System.out.println(template.expand(uriVariables));
     * </pre>
     * will print: <blockquote>{@code https://example.com/hotels/Rest%20%26%20Relax/bookings/42}</blockquote>
     *
     * @param uriVariables the map of URI variables
     * @return the expanded URI
     * @throws IllegalArgumentException if {@code uriVariables} is {@code null};
     *                                  or if it does not contain values for all the variable names
     */
    public URI replaceVariables(Map<String, ?> uriVariables) {
        UriComponents expandedComponents = this.uriComponents.replaceVariables(uriVariables);
        UriComponents encodedComponents = expandedComponents.encode();
        return encodedComponents.toUri();
    }

    /**
     * Given an array of variables, expand this template into a full URI. The array represent variable values.
     * The order of variables is significant.
     * <p>Example:
     * <pre class="code">
     * UriTemplate template = new UriTemplate("https://example.com/hotels/{hotel}/bookings/{booking}");
     * System.out.println(template.expand("Rest &amp; Relax", 42));
     * </pre>
     * will print: <blockquote>{@code https://example.com/hotels/Rest%20%26%20Relax/bookings/42}</blockquote>
     *
     * @param uriVariableValues the array of URI variables
     * @return the expanded URI
     * @throws IllegalArgumentException if {@code uriVariables} is {@code null}
     *                                  or if it does not contain sufficient variables
     */
    public URI replaceVariables(Object... uriVariableValues) {
        UriComponents expandedComponents = this.uriComponents.replaceVariables(uriVariableValues);
        UriComponents encodedComponents = expandedComponents.encode();
        return encodedComponents.toUri();
    }

    /**
     * Indicate whether the given URI matches this template.
     *
     * @param uri the URI to match to
     * @return {@code true} if it matches; {@code false} otherwise
     */
    public boolean matches(@Nullable String uri) {
        if (uri == null) {
            return false;
        }
        RegexpMatcher matcher = this.matchPattern.matcher(uri);
        return matcher.matches();
    }

    /**
     * Match the given URI to a map of variable values. Keys in the returned map are variable names,
     * values are variable values, as occurred in the given URI.
     * <p>Example:
     * <pre class="code">
     * UriTemplate template = new UriTemplate("https://example.com/hotels/{hotel}/bookings/{booking}");
     * System.out.println(template.match("https://example.com/hotels/1/bookings/42"));
     * </pre>
     * will print: <blockquote>{@code {hotel=1, booking=42}}</blockquote>
     *
     * @param uri the URI to match to
     * @return a map of variable values
     */
    public Map<String, String> match(String uri) {
        Preconditions.checkNotNull(uri, "'uri' must not be null");
        Map<String, String> result = Maps.newLinkedHashMapWithExpectedSize(this.variableNames.size());
        RegexpMatcher matcher = this.matchPattern.matcher(uri);
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String name = this.variableNames.get(i - 1);
                String value = matcher.group(i);
                result.put(name, value);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.uriTemplate;
    }


    /**
     * Helper to extract variable names and regex for matching to actual URLs.
     */
    private static final class TemplateInfo {

        private final List<String> variableNames;

        private final Regexp regexp;

        private TemplateInfo(List<String> vars, Regexp pattern) {
            this.variableNames = vars;
            this.regexp = pattern;
        }

        public List<String> getVariableNames() {
            return this.variableNames;
        }

        public Regexp getMatchRegexp() {
            return this.regexp;
        }

        public static TemplateInfo parse(String uriTemplate) {
            int level = 0;
            List<String> variableNames = new ArrayList<String>();
            StringBuilder pattern = new StringBuilder();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < uriTemplate.length(); i++) {
                char c = uriTemplate.charAt(i);
                if (c == '{') {
                    level++;
                    if (level == 1) {
                        // start of URI variable
                        pattern.append(quote(builder));
                        builder = new StringBuilder();
                        continue;
                    }
                } else if (c == '}') {
                    level--;
                    if (level == 0) {
                        // end of URI variable
                        String variable = builder.toString();
                        int idx = variable.indexOf(':');
                        if (idx == -1) {
                            pattern.append("([^/]*)");
                            variableNames.add(variable);
                        } else {
                            if (idx + 1 == variable.length()) {
                                throw new IllegalArgumentException(
                                        "No custom regular expression specified after ':' in \"" + variable + "\"");
                            }
                            String regex = variable.substring(idx + 1);
                            pattern.append('(');
                            pattern.append(regex);
                            pattern.append(')');
                            variableNames.add(variable.substring(0, idx));
                        }
                        builder = new StringBuilder();
                        continue;
                    }
                }
                builder.append(c);
            }
            if (builder.length() > 0) {
                pattern.append(quote(builder));
            }
            return new TemplateInfo(variableNames, Regexps.compile(pattern.toString()));
        }

        private static String quote(StringBuilder builder) {
            return (builder.length() > 0 ? Regexps.quote(builder.toString()) : "");
        }
    }

}
