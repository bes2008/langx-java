package com.jn.langx.util;

import java.util.Locale;

public class Platform {
    public static boolean isWindows = isWindows0();

    private static boolean isWindows0() {
        return System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
    }
}
