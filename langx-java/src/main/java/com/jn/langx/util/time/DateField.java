package com.jn.langx.util.time;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

import java.util.Calendar;

public enum DateField implements CommonEnum {
    ERA(Calendar.ERA, "era", "世纪"),
    YEAR(Calendar.YEAR, "year", "年"),
    MONTH(Calendar.MONTH, "month", "月"),
    WEEK_OF_YEAR(Calendar.WEEK_OF_YEAR, "week_of_year", "周"),
    WEEK_OF_MONTH(Calendar.WEEK_OF_MONTH, "week_of_month", "周"),
    DAY(Calendar.DAY_OF_MONTH, "day","日"),
    DAY_OF_YEAR(Calendar.DAY_OF_YEAR,"day_of_year", "天"),
    DAY_OF_WEEK(Calendar.DAY_OF_WEEK,"day_of_week", "星期"),
    HOUR(Calendar.HOUR_OF_DAY, "hour", "时"),
    MINUTE(Calendar.MINUTE, "minute","分"),
    SECOND(Calendar.SECOND,"second","秒"),
    MILLIS(Calendar.MILLISECOND,"millisecond","毫秒");

    private EnumDelegate enumDelegate;

    DateField(int code, String name, String displayText) {
        this.enumDelegate = new EnumDelegate(code, name, displayText);
    }

    public int getField() {
        return getCode();
    }

    @Override
    public int getCode() {
        return this.enumDelegate.getCode();
    }

    @Override
    public String getName() {
        return  this.enumDelegate.getName();
    }

    @Override
    public String getDisplayText() {
        return this.enumDelegate.getDisplayText();
    }
}