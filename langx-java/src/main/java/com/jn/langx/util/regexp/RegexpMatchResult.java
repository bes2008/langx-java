package com.jn.langx.util.regexp;

import java.util.regex.MatchResult;

/**
 * @since 4.5.0
 */
public interface RegexpMatchResult extends MatchResult {
    /**
     * Returns the input subsequence captured by the given group during the
     * previous match operation.
     *
     * @param groupName name of capture group
     * @return the subsequence
     */
    String group(String groupName);
}
