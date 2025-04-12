package com.jn.langx.util.net.uri.component;


import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.function.Operator;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.net.uri.MapTemplateVariableResolver;
import com.jn.langx.util.net.uri.UriTemplateVariableResolver;
import com.jn.langx.util.net.uri.VarArgsTemplateVariableResolver;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;

import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


/**
 * Represents an immutable collection of URI components, mapping component type to
 * String values. Contains convenience getters for all components. Effectively similar
 * to {@link java.net.URI}, but with more powerful encoding options and support for
 * URI template variables.
 *
 * @see UriComponentsBuilder
 */
public abstract class UriComponents implements Serializable {

    /**
     * Captures URI template variable names.
     */
    private static final Regexp NAMES_PATTERN = Regexps.compile("\\{([^/]+?)\\}");


    @Nullable
    private final String scheme;

    @Nullable
    private final String fragment;


    protected UriComponents(@Nullable String scheme, @Nullable String fragment) {
        this.scheme = scheme;
        this.fragment = fragment;
    }


    // Component getters

    /**
     * Return the scheme. Can be {@code null}.
     */
    @Nullable
    public final String getScheme() {
        return this.scheme;
    }

    /**
     * Return the fragment. Can be {@code null}.
     */
    @Nullable
    public final String getFragment() {
        return this.fragment;
    }

    /**
     * Return the scheme specific part. Can be {@code null}.
     */
    @Nullable
    public abstract String getSchemeSpecificPart();

    /**
     * Return the user info. Can be {@code null}.
     */
    @Nullable
    public abstract String getUserInfo();

    /**
     * Return the host. Can be {@code null}.
     */
    @Nullable
    public abstract String getHost();

    /**
     * Return the port. {@code -1} if no port has been set.
     */
    public abstract int getPort();

    /**
     * Return the path. Can be {@code null}.
     */
    @Nullable
    public abstract String getPath();

    /**
     * Return the list of path segments. Empty if no path has been set.
     */
    public abstract List<String> getPathSegments();

    /**
     * Return the query. Can be {@code null}.
     */
    @Nullable
    public abstract String getQuery();

    /**
     * Return the map of query parameters. Empty if no query has been set.
     */
    public abstract MultiValueMap<String, String> getQueryParams();


    /**
     * Invoke this <em>after</em> expanding URI variables to encode the
     * resulting URI component values.
     * <p>In comparison to {@link UriComponentsBuilder#encode()}, this method
     * <em>only</em> replaces non-ASCII and illegal (within a given URI
     * component type) characters, but not characters with reserved meaning.
     * For most cases, {@link UriComponentsBuilder#encode()} is more likely
     * to give the expected result.
     *
     * @see UriComponentsBuilder#encode()
     */
    public final UriComponents encode() {
        return encode(Charsets.UTF_8);
    }

    /**
     * A variant of {@link #encode()} with a charset other than "UTF-8".
     *
     * @param charset the charset to use for encoding
     * @see UriComponentsBuilder#encode(Charset)
     */
    public abstract UriComponents encode(Charset charset);

    /**
     * Replace all URI template variables with the values from a given map.
     * <p>The given map keys represent variable names; the corresponding values
     * represent variable values. The order of variables is not significant.
     *
     * @param uriVariables the map of URI variables
     * @return the expanded URI components
     */
    public final UriComponents expand(Map<String, ?> uriVariables) {
        Preconditions.checkNotNull(uriVariables, "'uriVariables' must not be null");
        return replaceVariablesInternal(new MapTemplateVariableResolver(uriVariables));
    }

    /**
     * Replace all URI template variables with the values from a given array.
     * <p>The given array represents variable values. The order of variables is significant.
     *
     * @param uriVariableValues the URI variable values
     * @return the expanded URI components
     */
    public final UriComponents replaceVariables(Object... uriVariableValues) {
        Preconditions.checkNotNull(uriVariableValues, "'uriVariableValues' must not be null");
        return replaceVariablesInternal(new VarArgsTemplateVariableResolver(uriVariableValues));
    }

    /**
     * Replace all URI template variables with the values from the given
     * {@link UriTemplateVariableResolver}.
     *
     * @param uriVariables the URI template values
     * @return the expanded URI components
     */
    public final UriComponents replaceVariables(UriTemplateVariableResolver uriVariables) {
        Preconditions.checkNotNull(uriVariables, "'uriVariables' must not be null");
        return replaceVariablesInternal(uriVariables);
    }

    /**
     * Replace all URI template variables with the values from the given {@link
     * UriTemplateVariableResolver}.
     *
     * @param uriVariables the URI template values
     * @return the expanded URI components
     */
    abstract UriComponents replaceVariablesInternal(UriTemplateVariableResolver uriVariables);

    /**
     * Normalize the path removing sequences like "path/..". Note that
     * normalization is applied to the full path, and not to individual path
     * segments.
     */
    public abstract UriComponents normalize();

    /**
     * Concatenate all URI components to return the fully formed URI String.
     * <p>This method amounts to simple String concatenation of the current
     * URI component values and as such the result may contain illegal URI
     * characters, for example if URI variables have not been expanded or if
     * encoding has not been applied via {@link UriComponentsBuilder#encode()}
     * or {@link #encode()}.
     */
    public abstract String toUriString();

    /**
     * Create a {@link URI} from this instance as follows:
     * <p>If the current instance is {@link #encode() encoded}, form the full
     * URI String via {@link #toUriString()}, and then pass it to the single
     * argument {@link URI} constructor which preserves percent encoding.
     * <p>If not yet encoded, pass individual URI component values to the
     * multi-argument {@link URI} constructor which quotes illegal characters
     * that cannot appear in their respective URI component.
     */
    public abstract URI toUri();

    /**
     * A simple pass-through to {@link #toUriString()}.
     */
    @Override
    public final String toString() {
        return toUriString();
    }

    /**
     * Set all components of the given UriComponentsBuilder.
     */
    protected abstract void copyToUriComponentsBuilder(UriComponentsBuilder builder);


    // Static expansion helpers

    @Nullable
    public static String replaceUriComponent(@Nullable String source, UriTemplateVariableResolver uriVariables) {
        return replaceUriComponent(source, uriVariables, null);
    }

    @Nullable
    public static String replaceUriComponent(@Nullable String source, UriTemplateVariableResolver uriVariables,
                                             @Nullable Operator<String> encoder) {

        if (source == null) {
            return null;
        }
        if (source.indexOf('{') == -1) {
            return source;
        }
        if (source.indexOf(':') != -1) {
            source = sanitizeSource(source);
        }
        RegexpMatcher matcher = NAMES_PATTERN.matcher(source);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String match = matcher.group(1);
            String varName = getVariableName(match);
            Object varValue = uriVariables.getValue(varName);
            if (UriTemplateVariableResolver.SKIP_VALUE.equals(varValue)) {
                continue;
            }
            String formatted = getVariableValueAsString(varValue);
            formatted = encoder != null ? encoder.apply(formatted) : Matcher.quoteReplacement(formatted);
            matcher.appendReplacement(sb, formatted);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Remove nested "{}" such as in URI vars with regular expressions.
     */
    private static String sanitizeSource(String source) {
        int level = 0;
        int lastCharIndex = 0;
        char[] chars = new char[source.length()];
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (c == '{') {
                level++;
            }
            if (c == '}') {
                level--;
            }
            if (level > 1 || (level == 1 && c == '}')) {
                continue;
            }
            chars[lastCharIndex++] = c;
        }
        return new String(chars, 0, lastCharIndex);
    }

    private static String getVariableName(String match) {
        int colonIdx = match.indexOf(':');
        return (colonIdx != -1 ? match.substring(0, colonIdx) : match);
    }

    private static String getVariableValueAsString(@Nullable Object variableValue) {
        return (variableValue != null ? variableValue.toString() : "");
    }


}
