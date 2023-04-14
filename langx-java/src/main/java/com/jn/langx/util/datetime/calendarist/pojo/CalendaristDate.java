package com.jn.langx.util.datetime.calendarist.pojo;

import java.io.Serializable;

public class CalendaristDate implements Serializable {

    // 阳历 OR 农历的年、月、日
    protected int  year;
    protected int  month;
    protected int  day;

    // 阳历 OR 农历的时、分、秒
    protected int  hour;
    protected int  minute;
    protected int  second;

    // 阳历 OR 农历的毫秒
    protected int  millis;

    // 阳历 OR 农历时间戳
    protected long timestamp;

    public CalendaristDate() {}

    public CalendaristDate(int year, int month, int day) {
        this(year, month, day, 0, 0, 0, 0);
    }

    public CalendaristDate(int year, int month, int day, int hour, int minute, int second, int millis) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millis = millis;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMillis() {
        return millis;
    }

    public void setMillis(int millis) {
        this.millis = millis;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CalendaristDate{");
        sb.append("year=").append(year);
        sb.append(", month=").append(month);
        sb.append(", day=").append(day);
        sb.append(", hour=").append(hour);
        sb.append(", minute=").append(minute);
        sb.append(", second=").append(second);
        sb.append(", millis=").append(millis);
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
