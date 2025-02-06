package com.jn.langx.util.datetime;

import com.jn.langx.util.function.Supplier0;

import java.util.List;

/**
 * DateTimeFormatterFactory接口用于创建和提供DateTimeFormatter实例
 * 它是一个泛型接口，允许为不同的日期时间类型创建格式化器
 *
 * @param <DATE_TIME> 泛型参数，表示日期时间类型
 */
public interface DateTimeFormatterFactory<DATE_TIME> extends Supplier0<DateTimeFormatter<DATE_TIME>> {

    /**
     * 获取DateTimeFormatter实例
     *
     * @return DateTimeFormatter实例，用于格式化和解析日期时间
     */
    @Override
    DateTimeFormatter<DATE_TIME> get();

    /**
     * 获取当前格式化器支持的日期时间类型
     *
     * @return 包含支持的日期时间类型的Class对象列表
     */
    List<Class> supported();
}
