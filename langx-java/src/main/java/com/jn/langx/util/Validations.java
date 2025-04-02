package com.jn.langx.util;

import com.jn.langx.util.net.Nets;
import com.jn.langx.util.ranges.IntRange;

public class Validations {
    private Validations() {
    }

    public static boolean isValidRFC1123Hostname(String hostname) {
        return Nets.isValidRFC1123Domain(hostname);
    }

    public static boolean lengthInRange(Object value, int start, int end) {
        int length = Objs.length(value);
        return inRange(length, start, end);
    }

    public static boolean isValidPort(int port) {
        return inRange(port, 0, 65536);
    }

    public static boolean inRange(int num, int start, int end) {
        return new IntRange(start, end).contains(num);
    }
}
