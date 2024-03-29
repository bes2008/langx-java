package com.jn.langx.util.datetime;

import com.jn.langx.util.function.Supplier0;

import java.util.List;

/**
 * @since 4.5.2
 */
public interface DateTimeFormatterFactory<DATE_TIME> extends Supplier0<DateTimeFormatter<DATE_TIME>> {
    @Override
    DateTimeFormatter<DATE_TIME> get();

    List<Class> supported();
}
