package com.jn.langx.util.datetime;

import com.jn.langx.Parser;
/**
 * 通过解析，得到一个时间 & 格式
 *
 * @since 4.5.2
 */
public interface DateTimeParser extends Parser<String, DateTimeParsedResult> {
    // 此接口继承了Parser接口，专门用于解析字符串类型的输入，输出为DateTimeParsedResult类型的结果
}
