package com.jn.langx.util.regexp;

/**
 * @since 4.5.0
 */
public interface RegexpMatcher extends NamedGroupMatchResult {

    /**
     * Attempts to match the entire region against the pattern.
     *
     * <p> If the match succeeds then more information can be obtained via the
     * <tt>start</tt>, <tt>end</tt>, and <tt>group</tt> methods.  </p>
     *
     * @return  <tt>true</tt> if, and only if, the entire region sequence
     *          matches this matcher's pattern
     */
    boolean matches();

    /**
     * Resets this matcher.
     *
     * <p> Resetting a matcher discards all of its explicit state information
     * and sets its append position to zero. The matcher's region is set to the
     * default region, which is its entire character sequence. The anchoring
     * and transparency of this matcher's region boundaries are unaffected.
     *
     * @return  This matcher
     */
    RegexpMatcher reset();

    RegexpMatcher reset(CharSequence content);

    /**
     * Attempts to find the next subsequence of the input sequence that matches
     * the pattern.
     * 找到下一个匹配的子串
     * <p> This method starts at the beginning of this matcher's region, or, if
     * a previous invocation of the method was successful and the matcher has
     * not since been reset, at the first character not matched by the previous
     * match.
     *
     * <p> If the match succeeds then more information can be obtained via the
     * <tt>start</tt>, <tt>end</tt>, and <tt>group</tt> methods.  </p>
     *
     * @return  <tt>true</tt> if, and only if, a subsequence of the input
     *          sequence matches this matcher's pattern
     */
    boolean find();

    /**
     * Resets this matcher and then attempts to find the next subsequence of
     * the input sequence that matches the pattern, starting at the specified
     * index.
     *
     * <p> If the match succeeds then more information can be obtained via the
     * <tt>start</tt>, <tt>end</tt>, and <tt>group</tt> methods, and subsequent
     * invocations of the {@link #find()} method will start at the first
     * character not matched by this match.  </p>
     *
     * @param start the index to start searching for a match
     * @throws  IndexOutOfBoundsException
     *          If start is less than zero or if start is greater than the
     *          length of the input sequence.
     *
     * @return  <tt>true</tt> if, and only if, a subsequence of the input
     *          sequence starting at the given index matches this matcher's
     *          pattern
     */
    boolean find(int start);

    /**
     * 对匹配到的部分，进行文本替换
     *
     * @param b           替换后存入的buffer
     * @param replacement 替换物
     * @return 匹配器
     */
    RegexpMatcher appendReplacement(StringBuilder b, String replacement);
    /**
     * 把原始文本中 剩余部分添加的 StringBuffer中
     *
     * @param b
     */
    void appendTail(StringBuilder b);

    void interrupt();
}
