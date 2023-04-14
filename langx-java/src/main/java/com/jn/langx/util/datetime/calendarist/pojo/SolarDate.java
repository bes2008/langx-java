package com.jn.langx.util.datetime.calendarist.pojo;


import com.jn.langx.util.datetime.calendarist.Lunars;

import java.util.Calendar;

/**
 * 阳历日期
 */
public class SolarDate extends CalendaristDate {

    private int dayOfWeek;
    public SolarDate() {}

    public SolarDate(int year, int month, int day) {
        this(year, month, day, 0, 0, 0, 0);
    }

    public SolarDate(int year, int month, int day, int hour, int minute, int second, int millis) {
        super(year, month, day, hour, minute, second, millis);

        Calendar calendar = Lunars.getCalendarInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, millis);

        this.timestamp = calendar.getTimeInMillis();

        this.dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
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
        return "SolarDate{" +
            "dayOfWeek=" + dayOfWeek +
            ", year=" + year +
            ", month=" + month +
            ", day=" + day +
            ", hour=" + hour +
            ", minute=" + minute +
            ", second=" + second +
            ", millis=" + millis +
            ", timestamp=" + timestamp +
            '}';
    }
}
