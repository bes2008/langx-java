package com.jn.langx.util.datetime.calendarist.core.convert;


import com.jn.langx.util.datetime.calendarist.pojo.SolarDate;

public interface ISolarConvert {

    /**
     * 将某日期转为阳历日期
     *
     * @return {@link SolarDate}
     */
    SolarDate toSolar();

}
