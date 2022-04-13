package com.jn.langx.util.regexp;

/**
 * @since 4.5.0
 */
public interface RegexpMatcher extends NamedGroupMatchResult {
    /**
     * @return 是否匹配
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
     * @return  this matcher
     */
    RegexpMatcher reset();

    /**
     * 找到下一个匹配的子串
     *
     * @return 是否找到要匹配的内容
     */
    boolean find();

    boolean find(int start);

    /**
     * 对匹配到的部分，进行文本替换
     *
     * @param b           替换后存入的buffer
     * @param replacement 替换物
     * @return 匹配器
     */
    RegexpMatcher appendReplacement(StringBuffer b, String replacement);
    /**
     * 把原始文本中 剩余部分添加的 StringBuffer中
     *
     * @param b
     */
    void appendTail(StringBuffer b);
}
