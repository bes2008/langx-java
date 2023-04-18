package com.jn.langx.util.datetime;

import com.jn.langx.util.enums.base.CommonEnum;

import java.util.Calendar;

/**
 * @since 4.5.2
 */
public enum WeekDay implements CommonEnum {
    SUN(Calendar.SUNDAY, "sunday", "日"),
    MON(Calendar.MONDAY, "monday", "一"),
    TUE(Calendar.TUESDAY, "tuesday", "二"),
    WED(Calendar.WEDNESDAY, "wednesday", "三"),
    THU(Calendar.THURSDAY, "thursday", "四"),
    FRI(Calendar.FRIDAY, "friday", "五"),
    SAT(Calendar.SATURDAY, "saturday", "六");
    private int code;
    private String fullname;
    private String chinese;

    WeekDay(int code, String fullname, String chinese) {
        this.code = code;
        this.fullname = fullname;
        this.chinese = chinese;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public String getFullname() {
        return fullname;
    }

    @Override
    public String getDisplayText() {
        return this.fullname;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public String getChinese() {
        return chinese;
    }

    /**
     *
     * @param prefix 可以是：周、星期、礼拜
     */
    public String getChinese(String prefix) {
        return prefix + chinese;
    }
}
