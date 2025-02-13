package com.jn.langx.util.datetime;

import java.util.Locale;
import java.util.TimeZone;
/**
 * 日期时间解析结果接口
 *
 * 此接口定义了如何获取日期时间的解析结果，包括时间戳、时区、语言环境、格式和原始文本
 * 它主要用于日期时间解析功能，以提供统一的解析结果访问方式
 *
 * @since 4.5.2
 */
public interface DateTimeParsedResult {
    /**
     * 获取时间戳
     *
     * 该方法返回代表年、月、日、时、分、秒和毫秒的时间戳
     * 时间戳采用的是UTC时间
     *
     * @return 时间戳（UTC时间）
     */
    long getTimestamp();

    /**
     * 获取时区
     *
     * 该方法返回解析结果对应的时区
     *
     * @return 时区对象
     */
    TimeZone getTimeZone();

    /**
     * 获取语言环境
     *
     * 该方法返回解析结果对应的语言环境
     *
     * @return 语言环境对象
     */
    Locale getLocale();

    /**
     * 获取格式
     *
     * 该方法返回解析结果对应的日期时间格式
     *
     * @return 日期时间格式字符串
     */
    String getPattern();

    /**
     * 获取原始文本
     *
     * 该方法返回解析结果对应的原始文本
     *
     * @return 原始文本字符串
     */
    String getOriginText();
}
