package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.util.Date;

/**
 * https://www.iso.org/obp/ui#iso:std:iso:8601:-1:ed-1:v1:en
 */
public class Dates {
    public static final String YYYY_MM_DD_HH_mm_ss = "YYYY-MM-DD HH:mm:ss";
    public static final String YYYY_MM_DD_HH_mm_ss_SSS = "YYYY-MM-DD HH:mm:ss.SSS";
    public static final String YYYY_MM_DD= "YYYY-MM-DD";
    public static final String DD_MM_YYYY= "DD/MM/YYYY";
    public static final String HH_mm_ss ="HH:mm:ss";

    public String format(@NonNull Date date, @NonNull String pattern) {
        Preconditions.checkNotEmpty(pattern, "pattern is empty");
        Preconditions.checkNotNull(date);
        return InternalThreadLocalMap.getDateFormat(pattern).format(date);
    }

}
