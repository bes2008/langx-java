package com.jn.langx.test.util.enums;

import com.jn.langx.Delegatable;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum Period implements Delegatable<EnumDelegate> {
    MINUTES(0, "minutes", "minutes"),
    HOURS(1, "hours", "hours"),
    DAY(2, "day", "day"),
    MONTH(3, "month", "month");


    private int code;
    private String name;
    private String displayText;
    private EnumDelegate delegate;

    Period(int code, String name, String displayText) {
        this.code = code;
        this.name = name;
        this.displayText = displayText;

        setDelegate(new EnumDelegate(code, name, displayText));
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDisplayText() {
        return displayText;
    }

    public EnumDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(EnumDelegate delegate) {
        this.delegate = delegate;
    }
}