package com.jn.langx.test.util.enums;

import com.jn.langx.DelegateHolder;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum Period implements DelegateHolder<EnumDelegate>, CommonEnum {
    MINUTES(0, "minutes", "minutes"),
    HOURS(1, "hours", "hours"),
    DAY(2, "day", "day"),
    MONTH(3, "month", "month");

    public static final long serialVersionUID = 1L;

    private EnumDelegate delegate;

    Period(int code, String name, String displayText) {
        this.delegate = new EnumDelegate(code, name, displayText);
    }

    public int getCode() {
        return delegate.getCode();
    }

    public String getName() {
        return delegate.getName();
    }

    public String getDisplayText() {
        return delegate.getDisplayText();
    }


    public EnumDelegate getDelegate() {
        return delegate;
    }
}