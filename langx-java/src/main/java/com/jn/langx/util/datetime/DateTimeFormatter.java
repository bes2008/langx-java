package com.jn.langx.util.datetime;


import java.util.List;
import java.util.Locale;

public interface DateTimeFormatter<DATE_TIME> {
    String getPattern();
    void setPattern(String pattern);

    void setLocal(Locale locale);
    Locale getLocale();

    String format(DATE_TIME dateTime);

    List<Class> supported();
}
