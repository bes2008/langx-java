package com.jn.langx.util.datetime;

import java.util.TimeZone;

/**
 * 时区感知接口，用于获取和设置时区
 * 主要用于那些需要显示时间信息，并且希望这些信息能够根据不同的时区进行变化的类
 *
 * @since 4.5.2
 */
public interface TimeZoneAware {
    /**
     * 获取当前对象的时区
     *
     * @return 当前对象的时区
     */
    TimeZone getTimeZone();

    /**
     * 设置当前对象的时区
     *
     * @param tz 要设置的时区
     */
    void setTimeZone(TimeZone tz);
}
