package com.jn.langx.util.timing;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

import java.util.concurrent.TimeUnit;


public enum TimeUnit2 implements CommonEnum {
    DAYS(TimeUnit.DAYS.ordinal(), "d", "days", "日", TimeUnit.DAYS),
    HOURS(TimeUnit.HOURS.ordinal(), "h", "hours", "时", TimeUnit.HOURS),
    MINUTES(TimeUnit.MINUTES.ordinal(), "m", "minutes", "分", TimeUnit.MINUTES),
    SECONDS(TimeUnit.SECONDS.ordinal(), "s", "seconds", "秒", TimeUnit.SECONDS),
    MiLLS(TimeUnit.MILLISECONDS.ordinal(), "ms", "milliseconds", "毫秒", TimeUnit.MILLISECONDS),
    MICROS(TimeUnit.MICROSECONDS.ordinal(), "micros", "microseconds", "微秒", TimeUnit.MICROSECONDS),
    NANOS(TimeUnit.MICROSECONDS.ordinal(), "nanos", "nanoseconds", "纳秒", TimeUnit.MICROSECONDS);

    private EnumDelegate delegate;
    private String simpleName;
    private java.util.concurrent.TimeUnit unit;

    TimeUnit2(int code, String simpleName, String name, String displayText, TimeUnit unit) {
        this.delegate = new EnumDelegate(code, name, displayText);
        this.simpleName = simpleName;
        this.unit = unit;
    }


    @Override
    public int getCode() {
        return delegate.getCode();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getDisplayText() {
        return delegate.getDisplayText();
    }

    public String getSimpleName() {
        return simpleName;
    }

    public TimeUnit toTimeUtil() {
        return unit;
    }
}
