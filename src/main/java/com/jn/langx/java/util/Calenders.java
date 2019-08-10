package com.jn.langx.java.util;

public class Calenders {
    private static int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static String[] week = {"日", "一", "二", "三", "四", "五", "六"};

    /**
     * 求每个月的第一天是星期几
     * @return
     */
    private static int getNumberOfFirstDayInMonth(int year, int month) {
        if (month < 3) {
            year--;
            month += 12;
        }

        int y = year % 100;
        int m = month;

        int result = (1 + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400) % 7 + 1;

        // System.out.println(year + "-" + month + "-1  星期" + result);

        return result;
    }

    /**
     * 判断是否为闰年
     *
     * @param year
     * @return
     */
    private static boolean isLeapYear(int year) {
        return year % 400 == 0 ? false : (year % 4 == 0 || year % 100 == 0);
    }

    /**
     * 获取某个月份的天数
     *
     * @return
     */
    public static int getMaxDay(int year, int month) {
        return month == 2 && isLeapYear(year) ? 29 : arr[month - 1];
    }

    public static int getNumberInWeek(int year, int month, int day) {
        return (getNumberOfFirstDayInMonth(year, month) + day - 1) % 7;
    }
}
