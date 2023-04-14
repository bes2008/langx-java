package com.jn.langx.util.datetime.calendarist.core.convert;

import com.luna.common.calendarist.pojo.LunarDate;

public interface ILunarConvert {

    /**
     * 将某日期转为阴历日期
     *
     * @return {@link LunarDate}
     */
    LunarDate toLunar();

}
