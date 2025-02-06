package com.jn.langx.util.datetime.time;

import com.jn.langx.Parser;
/**
 * TimeParser接口继承了Parser接口，专门用于解析时间字符串。
 * 它定义了一个解析方法，将时间字符串解析为TimeParsedResult对象。
 * 此接口自版本4.5.2起可用。
 */
public interface TimeParser extends Parser<String, TimeParsedResult> {
    /**
     * 解析给定的时间字符串。
     *
     * @param time 时间字符串，表示需要解析的时间。
     * @return TimeParsedResult 解析后的结果对象，包含解析的时间信息。
     */
    @Override
    TimeParsedResult parse(String time);
}
