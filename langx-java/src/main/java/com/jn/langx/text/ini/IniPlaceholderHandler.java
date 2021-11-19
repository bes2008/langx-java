package com.jn.langx.text.ini;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.placeholder.PlaceholderParser;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.util.*;


/**
 * Utility class for working with Strings that have placeholder values in them. A placeholder takes the form
 * {@code ${name}}. Using {@code PropertyPlaceholderHelper} these placeholders can be substituted for
 * user-supplied values. <p> Values for substitution can be supplied using a {@link Properties} instance or
 * using a {@link PlaceholderParser}.
 *
 */
public class IniPlaceholderHandler {

    private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);

    static {
        wellKnownSimplePrefixes.put("}", "{");
    }


    private final String placeholderPrefix;

    private final String placeholderSuffix;

    private final String simplePrefix;

    @Nullable
    private final String valueSeparator;

    private final boolean ignoreUnresolvablePlaceholders;


    /**
     * Creates a new {@code PropertyPlaceholderHelper} that uses the supplied prefix and suffix.
     * Unresolvable placeholders are ignored.
     *
     * @param placeholderPrefix the prefix that denotes the start of a placeholder
     * @param placeholderSuffix the suffix that denotes the end of a placeholder
     */
    public IniPlaceholderHandler(String placeholderPrefix, String placeholderSuffix) {
        this(placeholderPrefix, placeholderSuffix, null, true);
    }

    /**
     * Creates a new {@code PropertyPlaceholderHelper} that uses the supplied prefix and suffix.
     *
     * @param placeholderPrefix              the prefix that denotes the start of a placeholder
     * @param placeholderSuffix              the suffix that denotes the end of a placeholder
     * @param valueSeparator                 the separating character between the placeholder variable
     *                                       and the associated default value, if any
     * @param ignoreUnresolvablePlaceholders indicates whether unresolvable placeholders should
     *                                       be ignored ({@code true}) or cause an exception ({@code false})
     */
    public IniPlaceholderHandler(String placeholderPrefix, String placeholderSuffix,
                                 @Nullable String valueSeparator, boolean ignoreUnresolvablePlaceholders) {

        Preconditions.checkNotNull(placeholderPrefix, "'placeholderPrefix' must not be null");
        Preconditions.checkNotNull(placeholderSuffix, "'placeholderSuffix' must not be null");
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
        String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
        if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
            this.simplePrefix = simplePrefixForSuffix;
        } else {
            this.simplePrefix = this.placeholderPrefix;
        }
        this.valueSeparator = valueSeparator;
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
    }


    /**
     * Replaces all placeholders of format {@code ${name}} with the corresponding
     * property from the supplied {@link Properties}.
     *
     * @param value      the value containing the placeholders to be replaced
     * @param ini the {@code Properties} to use for replacement
     * @return the supplied value with placeholders replaced inline
     */
    public String replacePlaceholders(String value, String defaultSection, final Ini ini) {
        Preconditions.checkNotNull(ini, "'ini' must not be null");
        return replacePlaceholders(value, defaultSection, new IniPlaceholderParser(ini));
    }

    /**
     * Replaces all placeholders of format {@code ${name}} with the value returned
     * from the supplied {@link PlaceholderParser}.
     *
     * @param value               the value containing the placeholders to be replaced
     * @param placeholderResolver the {@code PlaceholderResolver} to use for replacement
     * @return the supplied value with placeholders replaced inline
     */
    public String replacePlaceholders(String value,String defaultSection, PlaceholderParser placeholderResolver) {
        Preconditions.checkNotNull(value, "'value' must not be null");
        return parseStringValue(value, defaultSection, placeholderResolver, null);
    }

    protected String parseStringValue( String value, String defaultSection,PlaceholderParser placeholderParser, @Nullable Set<String> visitedPlaceholders) {

        int startIndex = value.indexOf(this.placeholderPrefix);
        if (startIndex == -1) {
            return value;
        }

        StringBuilder result = new StringBuilder(value);
        while (startIndex != -1) {
            int endIndex = findPlaceholderEndIndex(result, startIndex);
            if (endIndex != -1) {
                String placeholder = result.substring(startIndex + this.placeholderPrefix.length(), endIndex);
                String originalPlaceholder = placeholder;
                if (visitedPlaceholders == null) {
                    visitedPlaceholders = new HashSet<String>(4);
                }
                if (!visitedPlaceholders.add(originalPlaceholder)) {
                    throw new IllegalArgumentException(
                            "Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
                }
                if(!placeholder.startsWith("[")){
                    placeholder = "["+defaultSection+"]."+placeholder;
                }
                // Recursive invocation, parsing placeholders contained in the placeholder key.
                placeholder = parseStringValue(placeholder, defaultSection, placeholderParser, visitedPlaceholders);
                // Now obtain the value for the fully resolved key...
                String propVal = placeholderParser.parse(placeholder);
                if (propVal == null && this.valueSeparator != null) {
                    int separatorIndex = placeholder.indexOf(this.valueSeparator);
                    if (separatorIndex != -1) {
                        String actualPlaceholder = placeholder.substring(0, separatorIndex);
                        String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
                        propVal = placeholderParser.parse(actualPlaceholder);
                        if (propVal == null) {
                            propVal = defaultValue;
                        }
                    }
                }
                if (propVal != null) {
                    // Recursive invocation, parsing placeholders contained in the
                    // previously resolved placeholder value.
                    propVal = parseStringValue(propVal, defaultSection, placeholderParser, visitedPlaceholders);
                    if(placeholder.equals(propVal)){
                        propVal = originalPlaceholder;
                    }
                    result.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
                    Logger logger = Loggers.getLogger(getClass());
                    if (logger.isTraceEnabled()) {
                        logger.trace("Resolved placeholder '" + placeholder + "'");
                    }
                    startIndex = result.indexOf(this.placeholderPrefix, startIndex + propVal.length());
                } else if (this.ignoreUnresolvablePlaceholders) {
                    // Proceed with unprocessed value.
                    startIndex = result.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
                } else {
                    throw new IllegalArgumentException("Could not resolve placeholder '" +
                            placeholder + "'" + " in value \"" + value + "\"");
                }
                visitedPlaceholders.remove(originalPlaceholder);
            } else {
                startIndex = -1;
            }
        }
        return result.toString();
    }

    private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + this.placeholderPrefix.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (Strings.substringMatch(buf, index, this.placeholderSuffix)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + this.placeholderSuffix.length();
                } else {
                    return index;
                }
            } else if (Strings.substringMatch(buf, index, this.simplePrefix)) {
                withinNestedPlaceholder++;
                index = index + this.simplePrefix.length();
            } else {
                index++;
            }
        }
        return -1;
    }

}