package com.jn.langx.util.regexp;

import java.util.regex.MatchResult;

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
