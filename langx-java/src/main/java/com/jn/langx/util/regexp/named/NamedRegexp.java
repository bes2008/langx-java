package com.jn.langx.util.regexp.named;


import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import static com.jn.langx.util.regexp.named.Groups.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * A compiled representation of a regular expression. This is a wrapper
 * for the java.util.regex.Pattern with support for named capturing
 * groups. The named groups are specified with "(?&lt;name&gt;exp)", which
 * is identical to Java 7 named groups.
 *
 * @since 4.5.0
 */
public class NamedRegexp implements Regexp, Serializable {

    /**
     * Determines if a de-serialized file is compatible with this class.
     *
     * Maintainers must change this value if and only if the new version
     * of this class is not compatible with old versions. See Sun docs
     * for <a href=http://java.sun.com/products/jdk/1.1/docs/guide
     * /serialization/spec/version.doc.html> details. </a>
     *
     * Not necessary to include in first version of the class, but
     * included here as a reminder of its importance.
     */
    private static final long serialVersionUID = 1L;





    /** {@link java.util.regex.Pattern#UNIX_LINES} */
    public static final int UNIX_LINES = java.util.regex.Pattern.UNIX_LINES;

    /** {@link java.util.regex.Pattern#CASE_INSENSITIVE} */
    public static final int CASE_INSENSITIVE = java.util.regex.Pattern.CASE_INSENSITIVE;

    /** {@link java.util.regex.Pattern#COMMENTS} */
    public static final int COMMENTS = java.util.regex.Pattern.COMMENTS;

    /** {@link java.util.regex.Pattern#MULTILINE} */
    public static final int MULTILINE = java.util.regex.Pattern.MULTILINE;

    /** {@link java.util.regex.Pattern#LITERAL} */
    public static final int LITERAL = java.util.regex.Pattern.LITERAL;

    /** {@link java.util.regex.Pattern#DOTALL} */
    public static final int DOTALL = java.util.regex.Pattern.DOTALL;

    /** {@link java.util.regex.Pattern#UNICODE_CASE} */
    public static final int UNICODE_CASE = java.util.regex.Pattern.UNICODE_CASE;

    /** {@link java.util.regex.Pattern#CANON_EQ} */
    public static final int CANON_EQ = java.util.regex.Pattern.CANON_EQ;

    private java.util.regex.Pattern pattern;
    private String namedPattern;
    private List<String> groupNames;
    private Map<String,List<GroupCoordinate> > groupInfo;
    private Option option;

    /**
     * Constructs a named pattern with the given regular expression and flags
     *
     * @param regex the expression to be compiled
     * @param flags Match flags, a bit mask that may include:
     * <ul>
     *   <li>{@link java.util.regex.Pattern#CASE_INSENSITIVE}</li>
     *   <li>{@link java.util.regex.Pattern#MULTILINE}</li>
     *   <li>{@link java.util.regex.Pattern#DOTALL}</li>
     *   <li>{@link java.util.regex.Pattern#UNICODE_CASE}</li>
     *   <li>{@link java.util.regex.Pattern#CANON_EQ}</li>
     *   <li>{@link java.util.regex.Pattern#UNIX_LINES}</li>
     *   <li>{@link java.util.regex.Pattern#LITERAL}</li>
     *   <li>{@link java.util.regex.Pattern#COMMENTS}</li>
     * </ul>
     */
    public NamedRegexp(String regex, int flags) {
        namedPattern = regex;

        // group info must be parsed before building the standard pattern
        // because the pattern relies on group info to determine the indexes
        // of named back-references
        groupInfo = Groups.extractGroupInfo(regex);
        pattern = buildStandardPattern(regex, flags);
        this.option = Option.buildOption(flags);
    }

    public Option getOption(){
        return this.option;
    }

    /**
     * Compiles the given regular expression into a pattern
     *
     * @param regex the expression to be compiled
     * @return the pattern
     */
    public static NamedRegexp compile(String regex) {
        return new NamedRegexp(regex, 0);
    }

    public NamedRegexp(Pattern pattern){
        this(pattern.pattern(), pattern.flags());
    }

    public NamedRegexp(String pattern) {
        this(pattern, 0);
    }

    /**
     * Compiles the given regular expression into a pattern with the given flags
     *
     * @param regex the expression to be compiled
     * @param flags Match flags, a bit mask that may include:
     * <ul>
     *   <li>{@link java.util.regex.Pattern#CASE_INSENSITIVE}</li>
     *   <li>{@link java.util.regex.Pattern#MULTILINE}</li>
     *   <li>{@link java.util.regex.Pattern#DOTALL}</li>
     *   <li>{@link java.util.regex.Pattern#UNICODE_CASE}</li>
     *   <li>{@link java.util.regex.Pattern#CANON_EQ}</li>
     *   <li>{@link java.util.regex.Pattern#UNIX_LINES}</li>
     *   <li>{@link java.util.regex.Pattern#LITERAL}</li>
     *   <li>{@link java.util.regex.Pattern#COMMENTS}</li>
     * </ul>
     * @return the pattern
     */
    public static NamedRegexp compile(String regex, int flags) {
        return new NamedRegexp(regex, flags);
    }


    /**
     * Returns this pattern's match flags
     *
     * @return The match flags specified when this pattern was compiled
     */
    public int flags() {
        return pattern.flags();
    }

    /**
     * Creates a matcher that will match the given input against this pattern.
     *
     * @param input The character sequence to be matched
     * @return A new matcher for this pattern
     */
    public RegexpMatcher matcher(CharSequence input) {
        return new NamedMatcher(this, input);
    }

    @Override
    public String getPattern() {
        return this.namedPattern;
    }

    /**
     * Returns the wrapped {@link java.util.regex.Pattern}
     * @return the pattern
     */
    public java.util.regex.Pattern pattern() {
        return pattern;
    }

    public static String quote(String s){
        return java.util.regex.Pattern.quote(s);
    }


    /**
     * Gets the names of all capture groups
     *
     * @return the list of names
     */
    public List<String> getNamedGroups() {
        if (groupNames == null) {
            groupNames = new ArrayList<String>(groupInfo.keySet());
        }
        return Collections.unmodifiableList(groupNames);
    }

    Map<String, List<GroupCoordinate>> getGroupInfo() {
        return groupInfo;
    }

    /**
     * Replaces group-name properties (e.g., <b><code>${named}</code></b>) in
     * a replacement pattern with the equivalent reference that uses the
     * corresponding group index (e.g., <b><code>$2</code></b>). If the string
     * contains literal "$", it must be escaped with slash or else this call
     * will attempt to parse it as a group-name property.
     *
     * This is meant to be used to transform the parameter for:
     *  <ul>
     *  <li>{@link NamedMatcher#replaceAll(String)}</li>
     *  <li>{@link NamedMatcher#replaceFirst(String)}</li>
     *  <li>{@link NamedMatcher#appendReplacement(StringBuffer, String)}</li>
     *  </ul>
     * @param replacementPattern the input string to be evaluated
     * @return the modified string
     * @throws PatternSyntaxException group name was not found
     */
    String replaceProperties(String replacementPattern) {
        return replaceGroupNameWithIndex(
                new StringBuilder(replacementPattern),
                PROPERTY_PATTERN,
                "$"
        ).toString();
    }

    /**
     * Splits the given input sequence around matches of this pattern.
     *
     * <p>The array returned by this method contains each substring of the
     * input sequence that is terminated by another subsequence that matches
     * this pattern or is terminated by the end of the input sequence. The
     * substrings in the array are in the order in which they occur in the
     * input. If this pattern does not match any subsequence of the input
     * then the resulting array has just one element, namely the input
     * sequence in string form.</p>
     *
     * <p>The limit parameter controls the number of times the pattern is
     * applied and therefore affects the length of the resulting array. If
     * the limit n is greater than zero then the pattern will be applied
     * at most n - 1 times, the array's length will be no greater than n,
     * and the array's last entry will contain all input beyond the last
     * matched delimiter. If n is non-positive then the pattern will be
     * applied as many times as possible and the array can have any length.
     * If n is zero then the pattern will be applied as many times as
     * possible, the array can have any length, and trailing empty strings
     * will be discarded.</p>
     *
     * @param input The character sequence to be split
     * @param limit The result threshold, as described above
     * @return The array of strings computed by splitting the input around
     * matches of this pattern
     */
    public String[] split(CharSequence input, int limit) {
        return pattern.split(input, limit);
    }

    /**
     * Splits the given input sequence around matches of this pattern.
     *
     * @param input The character sequence to be split
     * @return The array of strings computed by splitting the input around
     * matches of this pattern
     */
    public String[] split(CharSequence input) {
        return pattern.split(input);
    }

    /**
     * Returns a string representation of this pattern
     *
     * @return the string
     */
    public String toString() {
        return namedPattern;
    }


    /**
     * Replaces strings matching a pattern with another string. If the string
     * to be replaced is escaped with a slash, it is skipped.
     *
     * @param input the string to evaluate
     * @param pattern the pattern that matches the string to be replaced
     * @param replacement the string to replace the target
     * @return the modified string (original instance of {@code input})
     */
    private static StringBuilder replace(StringBuilder input, java.util.regex.Pattern pattern, String replacement) {
        java.util.regex.Matcher m = pattern.matcher(input);
        while (m.find()) {
            if (isEscapedChar(input.toString(), m.start())) {
                continue;
            }

            // since we're replacing the original string being matched,
            // we have to reset the matcher so that it searches the new
            // string
            input.replace(m.start(), m.end(), replacement);
            m.reset(input);
        }
        return input;
    }

    /**
     * Replaces referenced group names with the reference to the corresponding group
     * index (e.g., <b><code>\k&lt;named></code></b>} to <b><code>\k2</code></b>};
     * <b><code>${named}</code></b> to <b><code>$2</code></b>}).
     * This assumes the group names have already been parsed from the pattern.
     *
     * @param input the string to evaluate
     * @param pattern the pattern that matches the string to be replaced
     * @param prefix string to prefix to the replacement (e.g., "$" or "\\")
     * @return the modified string (original instance of {@code input})
     * @throws PatternSyntaxException group name was not found
     */
    private StringBuilder replaceGroupNameWithIndex(StringBuilder input, java.util.regex.Pattern pattern, String prefix) {
        java.util.regex.Matcher m = pattern.matcher(input);
        while (m.find()) {
            if (Groups.isEscapedChar(input.toString(), m.start())) {
                continue;
            }

            int index = Groups.indexOf(groupInfo,m.group(INDEX_GROUP_NAME));
            if (index >= 0) {
                index++;
            } else {
                throw new PatternSyntaxException("unknown group name", input.toString(), m.start(INDEX_GROUP_NAME));
            }

            // since we're replacing the original string being matched,
            // we have to reset the matcher so that it searches the new
            // string
            input.replace(m.start(), m.end(), prefix + index);
            m.reset(input);
        }
        return input;
    }

    /**
     * Builds a {@code java.util.regex.Pattern} from a given regular expression
     * pattern (which may contain named groups) and flags
     *
     * @param namedPattern the expression to be compiled
     * @param flags Match flags, a bit mask that may include:
     * <ul>
     *   <li>{@link java.util.regex.Pattern#CASE_INSENSITIVE}</li>
     *   <li>{@link java.util.regex.Pattern#MULTILINE}</li>
     *   <li>{@link java.util.regex.Pattern#DOTALL}</li>
     *   <li>{@link java.util.regex.Pattern#UNICODE_CASE}</li>
     *   <li>{@link java.util.regex.Pattern#CANON_EQ}</li>
     *   <li>{@link java.util.regex.Pattern#UNIX_LINES}</li>
     *   <li>{@link java.util.regex.Pattern#LITERAL}</li>
     *   <li>{@link java.util.regex.Pattern#COMMENTS}</li>
     * </ul>
     * @return the standard {@code java.util.regex.Pattern}
     */
    private java.util.regex.Pattern buildStandardPattern(String namedPattern, Integer flags) {
        // replace the named-group construct with left-paren but
        // make sure we're actually looking at the construct (ignore escapes)
        StringBuilder s = new StringBuilder(namedPattern);
        s = replace(s, NAMED_GROUP_PATTERN, "(");
        s = replaceGroupNameWithIndex(s, BACKREF_NAMED_GROUP_PATTERN, "\\");
        return java.util.regex.Pattern.compile(s.toString(), flags);
    }

    /**
     * Compares the keys and values of two group-info maps
     *
     * @param a the first map to compare
     * @param b the other map to compare
     * @return {@code true} if the first map contains all of the other map's keys and values; {@code false} otherwise
     */
    private boolean groupInfoMatches(Map<String, List<GroupCoordinate>> a, Map<String, List<GroupCoordinate>> b) {
        if (a == null && b == null) {
            return true;
        }

        boolean isMatch = false;
        if (a != null && b != null) {
            if (a.isEmpty() && b.isEmpty()) {
                isMatch = true;
            } else if (a.size() == b.size()) {
                for (Map.Entry<String, List<GroupCoordinate>> entry : a.entrySet()) {
                    List<GroupCoordinate> otherList = b.get(entry.getKey());
                    isMatch = (otherList != null);
                    if (!isMatch) {
                        break;
                    }

                    List<GroupCoordinate> thisList = entry.getValue();
                    isMatch = otherList.containsAll(thisList) && thisList.containsAll(otherList);
                    if (!isMatch) {
                        break;
                    }
                }
            }
        }
        return isMatch;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NamedRegexp)) {
            return false;
        }
        NamedRegexp other = (NamedRegexp)obj;

        boolean groupNamesMatch = (groupNames == null && other.groupNames == null) ||
                (groupNames != null && !Collections.disjoint(groupNames, other.groupNames));
        boolean groupInfoMatch = groupNamesMatch && groupInfoMatches(groupInfo, other.groupInfo);

        return groupNamesMatch
                && groupInfoMatch
                && namedPattern.equals(other.namedPattern)
                && pattern.flags() == other.pattern.flags()
                ;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = namedPattern.hashCode() ^ pattern.hashCode();
        if (groupInfo != null) {
            hash ^= groupInfo.hashCode();
        }
        if (groupNames != null) {
            hash ^= groupNames.hashCode();
        }
        return hash;
    }

}

