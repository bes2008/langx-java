package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;

/**
 * <pre>
 * ?	匹配任何单字符
 * *	匹配0或者任意数量的字符
 * **	匹配0或者更多的目录
 * </pre>
 */
public abstract class AntStyleMatcher<PatternEntry extends Named> extends AbstractPatternSetMatcher<PatternEntry> {

}
