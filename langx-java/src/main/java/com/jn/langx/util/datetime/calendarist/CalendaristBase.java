package com.jn.langx.util.datetime.calendarist;


public abstract class CalendaristBase {

    public final static int YEAR = 1;

    public final static int MONTH = 2;

    public final static int WEEK_OF_YEAR = 3;

    public final static int WEEK_OF_MONTH = 4;

    public final static int DATE = 5;

    public final static int DAY_OF_MONTH = 5;

    public final static int DAY_OF_YEAR = 6;

    public final static int DAY_OF_WEEK = 7;

    public final static int DAY_OF_WEEK_IN_MONTH = 8;

    public final static int AM_PM = 9;

    public final static int HOUR = 10;

    public final static int HOUR_OF_DAY = 11;

    public final static int MINUTE = 12;

    public final static int SECOND = 13;

    public final static int MILLISECOND = 14;

    public final static int TIMEMILLIS = 15;

    public final static int LEAP_MONTH_OF_CURRENT = 21;

    public final static int LEAP_MONTH = 22;

    /**
     * 从阳历转换 OR 从阴历转换
     * 1: 阳历
     * 2: 阴历
     */
    protected ConvertFromType from;

    protected int[] fields = new int[25];

    public void set(int field, int value) {
        validateSet(field, value);

        fields[field] = value;
    }

    public void setFrom(ConvertFromType from) {
        this.from = from;
    }

    /**
     * 由阴历转换时，设置要转换的月份是否是闰月
     *
     * @param itsLeapMonth 是否是闰月
     */
    public void itsLeapMonth(boolean itsLeapMonth) {
        fields[LEAP_MONTH_OF_CURRENT] = itsLeapMonth ? 1 : 0;
    }

    private void validateSet(int field, int value) {
        if (field == YEAR) {
            if (value < 1900 || value > 2100) {
                throw new IllegalArgumentException("the argument 'year' must between 1900 and 2100");
            }
        }

        if (field == MONTH) {
            if (value < 1 || value > 12) {
                throw new IllegalArgumentException("the argument 'month' must between 1 and 12");
            }
        }

        if (field == DAY_OF_MONTH) {
            if (value < 1 || value > 31) {
                throw new IllegalArgumentException("the argument 'day' must between 1 and 31");
            }
        }

        if (field == HOUR_OF_DAY) {
            if (value < 0 || value > 23) {
                throw new IllegalArgumentException("the argument 'hour' must between 0 and 23");
            }
        }

        if (field == MINUTE) {
            if (value < 0 || value > 59) {
                throw new IllegalArgumentException("the argument 'minute' must between 0 and 59");
            }
        }

        if (field == SECOND) {
            if (value < 0 || value > 59) {
                throw new IllegalArgumentException("the argument 'second' must between 0 and 59");
            }
        }

        if (field == MILLISECOND) {
            if (value < 0 || value > 999) {
                throw new IllegalArgumentException("the argument 'millis' must between 0 and 999");
            }
        }
    }

    protected static void validate(int year, int month, int day, int hour, int minute, int second, int millis) {
        if (year < 1900 || year > 2100) {
            throw new IllegalArgumentException("the argument 'year' must between 1900 and 2100");
        }

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("the argument 'month' must between 1 and 12");
        }

        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("the argument 'day' must between 1 and 31");
        }

        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("the argument 'hour' must between 0 and 23");
        }

        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("the argument 'minute' must between 0 and 59");
        }

        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("the argument 'second' must between 0 and 59");
        }

        if (millis < 0 || millis > 999) {
            throw new IllegalArgumentException("the argument 'millis' must between 0 and 999");
        }
    }
}
