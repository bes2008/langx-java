package com.jn.langx.util;

public class TimeReverses {
    public static long reverseTimeMillis(long time) {
        return Long.MAX_VALUE - time;
    }

    public static long reverseCurrentTimeMillis() {
        return reverseTimeMillis(System.currentTimeMillis());
    }

    public static long recoveryTimeMillis(long reversedTime) {
        return reverseTimeMillis(reversedTime);
    }
}
