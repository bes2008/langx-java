package com.jn.langx.util.datetime.calendarist.pojo;


import com.jn.langx.util.datetime.calendarist.Lunars;

/**
 * 阴历日期
 */
public class LunarDate extends CalendaristDate {

    // 该年的闰月月份
    private int     leapMonth;

    // 当前月是否是闰月
    private boolean itsLeapMonth;

    public LunarDate() {}

    public LunarDate(int year, int month, int day) {
        this(year, month, day, 0, 0, 0, 0);
    }

    public LunarDate(int year, int month, int day, int hour, int minute, int second, int millis) {
        this(year, month, day, hour, minute, second, millis, false);
    }

    public LunarDate(int year, int month, int day, int hour, int minute, int second, int millis, boolean itsLeapMonth) {
        super(year, month, day, hour, minute, second, millis);

        // 获取该年真实的闰月月份
        this.leapMonth = Lunars.leapMonth(year);

        // 如果设置了当前月是闰月，则要验证真实性
        this.itsLeapMonth = itsLeapMonth && this.leapMonth != 0 && this.leapMonth == month;
    }

    public int getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(int leapMonth) {
        this.leapMonth = leapMonth;
    }

    public boolean isItsLeapMonth() {
        return itsLeapMonth;
    }

    public void setItsLeapMonth(boolean itsLeapMonth) {
        this.itsLeapMonth = itsLeapMonth;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LunarDate{");
        sb.append("year=").append(year);
        sb.append(", month=").append(month);
        sb.append(", day=").append(day);
        sb.append(", hour=").append(hour);
        sb.append(", minute=").append(minute);
        sb.append(", second=").append(second);
        sb.append(", millis=").append(millis);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", itsLeapMonth=").append(itsLeapMonth);
        sb.append(", leapMonth=").append(leapMonth);
        sb.append('}');
        return sb.toString();
    }
}
