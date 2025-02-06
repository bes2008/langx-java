package com.jn.langx.util.datetime;


import java.util.List;
import java.util.Locale;

/**
 * DateTimeFormatter接口用于定义日期时间的格式化规范
 * 它扩展了DateTimeFormatterFactory接口，提供了一种机制来创建和操作日期时间格式化器
 *
 * @param <DATE_TIME> 泛型参数，表示所操作的日期时间类型
 * @since 4.5.2
 */
public interface DateTimeFormatter<DATE_TIME> extends DateTimeFormatterFactory<DATE_TIME>{
    /**
     * 获取当前日期时间格式化器的模式
     *
     * @return 字符串表示的当前日期时间格式化模式
     */
    String getPattern();

    /**
     * 设置日期时间格式化器的模式
     *
     * @param pattern 字符串表示的新的日期时间格式化模式
     */
    void setPattern(String pattern);

    /**
     * 设置格式化器的区域设置信息
     *
     * @param locale 区域设置对象，用于定义格式化规则的地域性
     */
    void setLocal(Locale locale);

    /**
     * 获取当前格式化器的区域设置信息
     *
     * @return 当前格式化器使用的区域设置对象
     */
    Locale getLocale();

    /**
     * 格式化给定的日期时间对象为字符串
     *
     * @param dateTime 日期时间对象，将根据格式化器的设置进行格式化
     * @return 格式化后的日期时间字符串
     */
    String format(DATE_TIME dateTime);

    /**
     * 获取此格式化器支持的日期时间类型列表
     *
     * @return 包含支持的日期时间类型的Class对象列表
     */
    List<Class> supported();

    /**
     * 获取一个新的日期时间格式化器实例
     *
     * @return 新的DateTimeFormatter实例，用于日期时间的格式化和解析
     */
    DateTimeFormatter<DATE_TIME> get();
}
