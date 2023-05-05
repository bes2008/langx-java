package com.jn.langx.text.grok;

import com.jn.langx.Converter;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;

import java.util.*;


/**
 * {@code Grok} parse arbitrary text and structure it.
 * <br>
 * {@code Grok} is simple API that allows you to easily parse logs
 * and other files (single line). With {@code Grok},
 * you can turn unstructured log and event data into structured data.
 *
 * @since 4.7.2
 */
public class Grok implements GrokTemplate {

    /**
     * Named regex of the originalGrokPattern.
     */
    private final String namedRegex;
    /**
     * Map of the named regex of the originalGrokPattern
     * with id = namedregexid and value = namedregex.
     */
    private final Map<String, String> namedRegexCollection;
    /**
     * Original {@code Grok} pattern (expl: %{IP}).
     */
    private final String originalGrokPattern;
    /**
     * Pattern of the namedRegex.
     */
    private final Regexp compiledNamedRegex;

    /**
     * {@code Grok} patterns definition.
     */
    private final Map<String, String> grokPatternDefinition;

    public final Set<String> namedGroups;

    public final Map<String, Converters.Type> groupTypes;

    public final Map<String, Converter> converters;

    /**
     * {@code Grok} discovery.
     */
    private Discovery disco;

    /**
     * only use in grok discovery.
     */
    private String savedPattern = "";

    public Grok(String pattern,
                String namedRegex,
                Map<String, String> namedRegexCollection,
                Map<String, String> patternDefinitions,
                String defaultTimeZone) {
        this.originalGrokPattern = pattern;
        this.namedRegex = namedRegex;
        this.compiledNamedRegex = Regexps.createRegexp(namedRegex);
        this.namedRegexCollection = namedRegexCollection;
        this.namedGroups = Groks.getNameGroups(namedRegex);
        this.groupTypes = Converters.getGroupTypes(namedRegexCollection.values());
        this.converters = Converters.getConverters(namedRegexCollection.values(), defaultTimeZone);
        this.grokPatternDefinition = patternDefinitions;
    }

    public String getSavedPattern() {
        return savedPattern;
    }

    public void setSavedPattern(String savedpattern) {
        this.savedPattern = savedpattern;
    }

    /**
     * Get the current map of {@code Grok} pattern.
     *
     * @return Patterns (name, regular expression)
     */
    public Map<String, String> getPatterns() {
        return grokPatternDefinition;
    }

    /**
     * Get the named regex from the {@code Grok} pattern. <br>
     *
     * @return named regex
     */
    public String getNamedRegex() {
        return namedRegex;
    }

    /**
     * Original grok pattern used to compile to the named regex.
     *
     * @return String Original Grok pattern
     */
    public String getOriginalGrokPattern() {
        return originalGrokPattern;
    }

    /**
     * Get the named regex from the given id.
     *
     * @param id : named regex id
     * @return String of the named regex
     */
    public String getNamedRegexCollectionById(String id) {
        return namedRegexCollection.get(id);
    }

    /**
     * Get the full collection of the named regex.
     *
     * @return named RegexCollection
     */
    public Map<String, String> getNamedRegexCollection() {
        return namedRegexCollection;
    }

    /**
     * Match the given <tt>log</tt> with the named regex.
     * And return the json representation of the matched element
     *
     * @param log : log to match
     * @return map containing matches
     */
    public Map<String, Object> extract(String log) {
        Match match = match(log);
        return match.capture();
    }

    /**
     * Match the given list of <tt>log</tt> with the named regex
     * and return the list of json representation of the matched elements.
     *
     * @param logs : list of log
     * @return list of maps containing matches
     */
    public List<Map<String, Object>> extract(List<String> logs) {
        final List<Map<String, Object>> matched = Collects.emptyArrayList();
        for (String log : logs) {
            matched.add(extract(log));
        }
        return matched;
    }

    /**
     * Match the given <tt>text</tt> with the named regex
     * {@code Grok} will extract data from the string and get an extence of {@link Match}.
     *
     * @param text : Single line of log
     * @return Grok Match
     */
    Match match(CharSequence text) {
        if (compiledNamedRegex == null || Strings.isBlank(text)) {
            return Match.EMPTY;
        }

        RegexpMatcher matcher = compiledNamedRegex.matcher(text);
        if (matcher.matches()) {
            return new Match(text, this, matcher);
        }
        return Match.EMPTY;
    }

    /**
     * {@code Grok} will try to find the best expression that will match your input.
     * {@link Discovery}
     *
     * @param input : Single line of log
     * @return the Grok pattern
     */
    private String discover(String input) {

        if (disco == null) {
            disco = new Discovery(this);
        }
        return disco.discover(input);
    }

}
