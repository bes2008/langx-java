package com.jn.langx.text.grok;


import com.jn.langx.Converter;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;

import java.util.*;


/**
 * {@code Match} is a representation in {@code Grok} world of your log.
 *
 * @since 4.7.2
 */
class Match {
    /**
     * Create Empty grok matcher.
     */
    public static final Match EMPTY = new Match("", null, null);
    private final CharSequence subject;
    private final Grok grok;
    private final RegexpMatcher matcher;
    private boolean keepEmptyCaptures = true;
    private Map<String, Object> capture = Collections.emptyMap();

    /**
     * Create a new {@code Match} object.
     */
    public Match(CharSequence subject, Grok grok, RegexpMatcher matcher) {
        this.subject = subject;
        this.grok = grok;
        this.matcher = matcher;
    }

    public RegexpMatcher getMatcher() {
        return matcher;
    }


    public boolean isKeepEmptyCaptures() {
        return this.keepEmptyCaptures;
    }

    /**
     * Ignore empty captures.
     */
    public void setKeepEmptyCaptures(boolean ignore) {
        // clear any cached captures
        if (capture.size() > 0) {
            capture = new HashMap<String, Object>();
        }
        this.keepEmptyCaptures = ignore;
    }

    /**
     * Retrurn the single line of log.
     *
     * @return the single line of log
     */
    public CharSequence getSubject() {
        return subject;
    }

    /**
     * Match to the <tt>subject</tt> the <tt>regex</tt> and save the matched element into a map.
     * <p>
     * Multiple values for the same key are stored as list.
     */
    public Map<String, Object> capture() {
        return capture(false);
    }

    /**
     * Match to the <tt>subject</tt> the <tt>regex</tt> and save the matched element into a map
     * <p>
     * Multiple values to the same key are flattened to one value: the sole non-null value will be captured.
     * Should there be multiple non-null values a RuntimeException is being thrown.
     * <p>
     * This can be used in cases like: (foo (.*:message) bar|bar (.*:message) foo) where the regexp guarantees that only
     * one value will be captured.
     * <p>
     * See also {@link #capture} which returns multiple values of the same key as list.
     *
     * @return the matched elements
     * @throws GrokException if a keys has multiple non-null values.
     */
    public Map<String, Object> captureFlattened() throws GrokException {
        return capture(true);
    }

    /**
     * Private implementation of captureFlattened and capture.
     *
     * @param flattened will it flatten values.
     * @return the matched elements.
     * @throws GrokException if a keys has multiple non-null values, but only if flattened is set to true.
     */
    private Map<String, Object> capture(final boolean flattened) throws GrokException {
        if (matcher == null) {
            return Collections.emptyMap();
        }

        if (!capture.isEmpty()) {
            return capture;
        }

        capture = new LinkedHashMap<String, Object>();

        // _capture.put("LINE", this.line);
        // _capture.put("LENGTH", this.line.length() +"");

        Map<String, String> mappedw = Regexps.namedGroups(this.matcher, this.grok.namedGroups);
        Collects.forEach(mappedw, new Consumer2<String, String>() {
            @Override
            public void accept(String key, String valueString) {
                String id = grok.getNamedRegexCollectionById(key);
                if (id != null && !id.isEmpty()) {
                    key = id;
                }

                if (Strings.equals("UNWANTED", key)) {
                    return;
                }

                Object value = valueString;
                if (valueString != null) {
                    Converter converter = grok.converters.get(key);

                    if (converter != null) {
                        key = Converters.extractKey(key);
                        try {
                            value = converter.apply(valueString);
                        } catch (Exception e) {
                            capture.put(key + "_grokfailure", e.toString());
                        }

                        if (value instanceof String) {
                            value = cleanString((String) value);
                        }
                    } else {
                        value = cleanString(valueString);
                    }
                } else if (!isKeepEmptyCaptures()) {
                    return;
                }

                if (capture.containsKey(key)) {
                    Object currentValue = capture.get(key);

                    if (flattened) {
                        if (currentValue == null && value != null) {
                            capture.put(key, value);
                        }
                        if (currentValue != null && value != null) {
                            throw new GrokException(
                                    StringTemplates.formatWithCStyle(
                                            "key '%s' has multiple non-null values, this is not allowed in flattened mode, values:'%s', '%s'",
                                            key,
                                            currentValue,
                                            value));
                        }
                    } else {
                        if (currentValue instanceof List) {
                            @SuppressWarnings("unchecked")
                            List<Object> cvl = (List<Object>) currentValue;
                            cvl.add(value);
                        } else {
                            List<Object> list = new ArrayList<Object>();
                            list.add(currentValue);
                            list.add(value);
                            capture.put(key, list);
                        }
                    }
                } else {
                    capture.put(key, value);
                }
            }
        });

        capture = Collections.unmodifiableMap(capture);

        return capture;
    }

    /**
     * remove from the string the quote and double quote.
     *
     * @param value string to pure: "my/text"
     * @return unquoted string: my/text
     */
    private String cleanString(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        char firstChar = value.charAt(0);
        char lastChar = value.charAt(value.length() - 1);

        if (firstChar == lastChar
                && (firstChar == '"' || firstChar == '\'')
        ) {
            if (value.length() <= 2) {
                return "";
            } else {
                int found = 0;
                for (int i = 1; i < value.length() - 1; i++) {
                    if (value.charAt(i) == firstChar) {
                        found++;
                    }
                }
                if (found == 0) {
                    return value.substring(1, value.length() - 1);
                }
            }
        }

        return value;
    }

    /**
     * Util fct.
     *
     * @return boolean
     */
    public Boolean isNull() {
        return this.matcher == null;
    }

}
