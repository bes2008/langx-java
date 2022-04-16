package com.jn.langx.util.time;


import java.util.List;

public interface DateTimeFormatter<DATE_TIME> {
    String getFormat();

    void setFormat(String pattern);

    String format(DATE_TIME dateTime);

    List<Class> supported();
}
