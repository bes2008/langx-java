package com.jn.langx.util.regexp.named;


import com.jn.langx.util.regexp.RegexpMatchResult;
import com.jn.langx.util.regexp.RegexpMatcher;

import java.util.List;
import java.util.Map;

/**
 * The result of a match operation.
 *
 * <p>This interface contains query methods used to determine the results of
 * a match against a regular expression. The match boundaries, groups and
 * group boundaries can be seen but not modified through a MatchResult.</p>
 *
 * @since 4.5.0
 */
interface _MResult extends RegexpMatcher {

    /**
     * Returns the named capture groups in order
     *
     * @return the named capture groups
     */
    public List<String> orderedGroups();

    /**
     * Returns the named capture groups
     *
     * @return the named capture groups
     */
    public List<Map<String, String>> namedGroups();



    /**
     * Returns the start index of the subsequence captured by the given group
     * during this match.
     *
     * @param groupName name of capture group
     * @return the index
     */
    public int start(String groupName);

    /**
     * Returns the offset after the last character of the subsequence captured
     * by the given group during this match.
     *
     * @param groupName name of capture group
     * @return the offset
     */
    public int end(String groupName);

}
