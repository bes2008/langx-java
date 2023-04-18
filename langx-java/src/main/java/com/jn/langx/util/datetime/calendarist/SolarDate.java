package com.jn.langx.util.datetime.calendarist;


import java.util.Calendar;

/**
 * 节气日期
 *
 * @since 5.2.0
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

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public int getMonth() {
        return month;
    }

    @Override
    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public int getDay() {
        return day;
    }

    @Override
    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public int getHour() {
        return hour;
    }

    @Override
    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public int getMinute() {
        return minute;
    }

    @Override
    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public int getSecond() {
        return second;
    }

    @Override
    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public int getMillis() {
        return millis;
    }

    @Override
    public void setMillis(int millis) {
        this.millis = millis;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
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
