package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;
import com.jn.langx.Parser;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

/**
 * PatternSetExpressionParser是一个泛型接口，用于解析表示模式集的字符串表达式。
 * 它继承自Parser接口，专门用于解析String类型的数据，返回一个PatternSet对象。
 * 该接口的主要用途是定义解析规则，以处理包含一系列模式的表达式，这些模式可以是包含或排除特定项的集合。
 *
 * @param <PatternEntry> 表示模式集中的条目类型，该类型必须继承自Named接口，以确保每个条目都有一个名称。
 */
public interface PatternSetExpressionParser<PatternEntry extends Named> extends Parser<String, PatternSet<PatternEntry>> {

    /**
     * 解析给定的表达式字符串，将其转换为一个PatternSet对象。
     * 表达式字符串是由一系列模式条目组成的，这些条目使用特定的分隔符分隔，并可能包含排除标志来指示哪些条目应从集合中排除。
     *
     * @param expression 待解析的表达式字符串，包含一系列模式条目。
     * @return PatternSet<PatternEntry> 解析后的模式集对象，包含解析出的模式条目。
     */
    PatternSet<PatternEntry> parse(@NonNull String expression);

    /**
     * 获取用于分隔表达式中模式条目的分隔符。
     * 分隔符用于解析表达式字符串，以确定各个模式条目的边界。
     *
     * @return String 用于分隔模式条目的分隔符字符串。
     */
    @NonNull
    String getSeparator();

    /**
     * 获取用于指示表达式中模式条目应被排除的排除标志。
     * 排除标志是可选的，如果提供，则解析过程中会识别并处理带有此标志的条目，将其从模式集中排除。
     *
     * @return String 如果解析器支持排除标志，则返回该标志的字符串表示；如果不支持，则返回null。
     */
    @Nullable
    String getExcludeFlag();
}
