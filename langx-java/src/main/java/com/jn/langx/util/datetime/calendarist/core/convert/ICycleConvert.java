package com.jn.langx.util.datetime.calendarist.core.convert;


import com.jn.langx.util.datetime.calendarist.pojo.CycleDate;

public interface ICycleConvert {

    /**
     * 将某日期转为干支日期
     *
     * @return {@link CycleDate}
     */
    CycleDate toCycle();

}
