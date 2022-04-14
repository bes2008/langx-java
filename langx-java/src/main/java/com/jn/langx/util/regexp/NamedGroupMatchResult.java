package com.jn.langx.util.regexp;

import java.util.regex.MatchResult;

/**
 * @since 4.5.0
 */
public interface NamedGroupMatchResult extends MatchResult {

    /**
     * Returns the input subsequence captured by the given
     * <a href="Pattern.html#groupname">named-capturing group</a> during the previous
     * match operation.
     *
     * <p> If the match was successful but the group specified failed to match
     * any part of the input sequence, then <tt>null</tt> is returned. Note
     * that some groups, for example <tt>(a*)</tt>, match the empty string.
     * This method will return the empty string when such a group successfully
     * matches the empty string in the input.  </p>
     *
     * @param groupName The name of a named-capturing group in this matcher's pattern
     * @return The (possibly empty) subsequence captured by the named group
     * during the previous match, or <tt>null</tt> if the group
     * failed to match part of the input
     * @throws IllegalStateException    If no match has yet been attempted,
     *                                  or if the previous match operation failed
     * @throws IllegalArgumentException If there is no capturing group in the pattern
     *                                  with the given name
     */
    String group(String groupName);


    /**
     * Returns the start index of the subsequence captured by the given
     * <a href="Pattern.html#groupname">named-capturing group</a> during the
     * previous match operation.
     *
     * @param groupName The name of a named-capturing group in this matcher's pattern
     * @return The index of the first character captured by the group,
     * or {@code -1} if the match was successful but the group
     * itself did not match anything
     * @throws IllegalStateException    If no match has yet been attempted,
     *                                  or if the previous match operation failed
     * @throws IllegalArgumentException If there is no capturing group in the pattern
     *                                  with the given name
     */
    int start(String groupName);

    /**
     * Returns the offset after the last character of the subsequence
     * captured by the given <a href="Pattern.html#groupname">named-capturing
     * group</a> during the previous match operation.
     *
     * @param groupName The name of a named-capturing group in this matcher's pattern
     * @return The offset after the last character captured by the group,
     * or {@code -1} if the match was successful
     * but the group itself did not match anything
     * @throws IllegalStateException    If no match has yet been attempted,
     *                                  or if the previous match operation failed
     * @throws IllegalArgumentException If there is no capturing group in the pattern
     *                                  with the given name
     */
    int end(String groupName);
}
