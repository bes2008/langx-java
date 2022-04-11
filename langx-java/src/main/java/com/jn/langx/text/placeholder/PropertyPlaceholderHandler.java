package com.jn.langx.text.placeholder;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.text.properties.PropertiesPlaceholderParser;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.struct.Holder;
import org.slf4j.Logger;

import java.util.*;


/**
 * Utility class for working with Strings that have placeholder values in them. A placeholder takes the form
 * {@code ${name}}. Using {@code PropertyPlaceholderHelper} these placeholders can be substituted for
 * user-supplied values. <p> Values for substitution can be supplied using a {@link Properties} instance or
 * using a {@link PlaceholderParser}.
 * <p>
 * 假定 : 是 表达式分隔符 expressionSeparator
 * {variable:expression}, {variable}
 * ${variable:expression}, ${variable}
 * %{variable:expression}, %{variable}
 */
public class PropertyPlaceholderHandler {

    private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);

    static {
        wellKnownSimplePrefixes.put("}", "{");
    }


    private final String placeholderPrefix;

    private final String placeholderSuffix;

    private final String simplePrefix;

    @Nullable
    private final String expressionSeparator;
    private PlaceholderSubExpressionHandler subExpressionHandler = new DefaultValueSubExpressionHandler();

    private final boolean ignoreUnresolvablePlaceholders;
    private Logger logger = Loggers.getLogger(getClass());

    private static class DefaultValueSubExpressionHandler implements PlaceholderSubExpressionHandler {
        @Override
        public void accept(String variable, String expression, Holder<String> variableValueHolder) {
            if (Strings.isEmpty(variableValueHolder.get())) {
                variableValueHolder.set(expression);
            }
        }
    }

    public void setSubExpressionHandler(PlaceholderSubExpressionHandler subExpressionHandler) {
        this.subExpressionHandler = subExpressionHandler;
    }

    /**
     * Creates a new {@code PropertyPlaceholderHelper} that uses the supplied prefix and suffix.
     * Unresolvable placeholders are ignored.
     *
     * @param placeholderPrefix the prefix that denotes the start of a placeholder
     * @param placeholderSuffix the suffix that denotes the end of a placeholder
     */
    public PropertyPlaceholderHandler(String placeholderPrefix, String placeholderSuffix) {
        this(placeholderPrefix, placeholderSuffix, null, true);
    }

    /**
     * Creates a new {@code PropertyPlaceholderHelper} that uses the supplied prefix and suffix.
     *
     * @param placeholderPrefix              the prefix that denotes the start of a placeholder
     * @param placeholderSuffix              the suffix that denotes the end of a placeholder
     * @param expressionSeparator            the separating character between the placeholder variable
     *                                       and the associated default value, if any
     * @param ignoreUnresolvablePlaceholders indicates whether unresolvable placeholders should
     *                                       be ignored ({@code true}) or cause an exception ({@code false})
     */
    public PropertyPlaceholderHandler(String placeholderPrefix, String placeholderSuffix,
                                      @Nullable String expressionSeparator, boolean ignoreUnresolvablePlaceholders) {

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
        this.expressionSeparator = expressionSeparator;
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
    }


    /**
     * Replaces all placeholders of format {@code ${name}} with the corresponding
     * property from the supplied {@link Properties}.
     *
     * @param template   the value containing the placeholders to be replaced
     * @param properties the {@code Properties} to use for replacement
     * @return the supplied value with placeholders replaced inline
     */
    public String replacePlaceholders(String template, final Properties properties) {
        Preconditions.checkNotNull(properties, "'properties' must not be null");
        return replacePlaceholders(template, new PropertiesPlaceholderParser(properties));
    }

    /**
     * Replaces all placeholders of format {@code ${name}} with the value returned
     * from the supplied {@link PlaceholderParser}.
     *
     * @param template            the value containing the placeholders to be replaced
     * @param placeholderResolver the {@code PlaceholderResolver} to use for replacement
     * @return the supplied value with placeholders replaced inline
     */
    public String replacePlaceholders(String template, PlaceholderParser placeholderResolver) {
        Preconditions.checkNotNull(template, "'value' must not be null");
        String ret = parseStringValue(template, placeholderResolver, null);
        if (logger.isDebugEnabled()) {
            logger.debug(ret);
        }
        return ret;
    }

    protected String parseStringValue(String template, PlaceholderParser placeholderResolver, @Nullable Set<String> visitedPlaceholders) {

        int startIndex = template.indexOf(this.placeholderPrefix);
        if (startIndex == -1) {
            return template;
        }

        StringBuilder result = new StringBuilder(template);
        while (startIndex != -1) {
            int endIndex = findPlaceholderEndIndex(result, startIndex);
            if (endIndex != -1) {
                String placeholder = result.substring(startIndex + this.placeholderPrefix.length(), endIndex);
                String originalPlaceholder = placeholder;
                if (visitedPlaceholders == null) {
                    visitedPlaceholders = new HashSet<String>(4);
                }
                if (!visitedPlaceholders.add(originalPlaceholder)) {
                    throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("Circular placeholder reference '{}' in property definitions", originalPlaceholder));
                }
                // 此时的 placeholder 格式可能为： variable, variable:defaultValue, variable:a_b_${xxx}, variable_${xx}:a_b_${xyz}， 也就是可能存在表达式，且可能表达式套表达式
                // 找出variable, expression

                String variable = null;
                String expression = null;
                if (this.expressionSeparator != null) {

                    if (placeholder.contains(expressionSeparator)) {
                        StringTokenizer tokenizer = new StringTokenizer(placeholder, this.expressionSeparator, true);
                        StringBuilder variableBuilder = new StringBuilder();
                        StringBuilder expressionBuilder = new StringBuilder();

                        int placeholderPrefixSuffixSum = 0;
                        while (tokenizer.hasMoreTokens()) {
                            String token = tokenizer.nextToken();
                            if (Strings.equals(this.expressionSeparator, token) && placeholderPrefixSuffixSum % 2 == 0) {
                                while (tokenizer.hasMoreTokens()) {
                                    expressionBuilder.append(tokenizer.nextToken());
                                }
                            } else {
                                variableBuilder.append(token);
                            }
                            placeholderPrefixSuffixSum += Strings.countOccurrencesOf(token, this.placeholderPrefix);
                            placeholderPrefixSuffixSum += Strings.countOccurrencesOf(token, this.placeholderSuffix);

                        }
                        variable = variableBuilder.toString();
                        expression = expressionBuilder.toString();
                    } else {
                        variable = placeholder;
                    }
                }
                // 对 variable, expression 都进行 递归式变量解析
                variable = parseStringValue(variable, placeholderResolver, visitedPlaceholders);
                if (Strings.isNotEmpty(expression)) {
                    expression = parseStringValue(expression, placeholderResolver, visitedPlaceholders);
                }
                String variableValue = placeholderResolver.parse(variable);
                if (variableValue != null) {
                    // Recursive invocation, parsing placeholders contained in the
                    // previously resolved placeholder value.
                    variableValue = parseStringValue(variableValue, placeholderResolver, visitedPlaceholders);
                }
                // 对变量以及变量值进行处理
                if (Strings.isNotEmpty(expression) && this.subExpressionHandler != null) {
                    Holder<String> propValueHolder = new Holder<String>(variableValue);
                    this.subExpressionHandler.accept(variable, expression, propValueHolder);
                    variableValue = propValueHolder.get();
                }

                if (variableValue != null) {
                    result.replace(startIndex, endIndex + this.placeholderSuffix.length(), variableValue);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Resolved placeholder '{}'", placeholder);
                    }
                    startIndex = result.indexOf(this.placeholderPrefix, startIndex + variableValue.length());
                } else if (this.ignoreUnresolvablePlaceholders) {
                    // Proceed with unprocessed value.
                    startIndex = result.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
                } else {
                    throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("Could not resolve placeholder '{}' in template: {}", placeholder, template));
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