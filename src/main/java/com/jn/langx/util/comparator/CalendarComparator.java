package com.jn.langx.util.comparator;

import java.util.Calendar;
import java.util.Comparator;

public class CalendarComparator implements Comparator<Calendar> {
    public static final CalendarComparator INSTANCE = new CalendarComparator();

    public int compare(Calendar x, Calendar y) {
        if (x.before(y)) {
            return -1;
        }
        if (x.after(y)) {
            return 1;
        }
        return 0;
    }
}
