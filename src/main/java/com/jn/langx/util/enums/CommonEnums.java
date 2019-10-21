package com.jn.langx.util.enums;

import com.jn.langx.Delegatable;
import com.jn.langx.util.enums.base.EnumDelegate;

import java.util.EnumSet;

public class CommonEnums {
    public static <T extends Enum> T ofCode(final Class<T> tClass, final int code) {
        T value = null;
        if (Delegatable.class.isAssignableFrom((Class<T>) tClass)) {
            EnumSet<? extends Enum> enums = EnumSet.allOf(tClass);
            for (Enum x : enums) {
                Delegatable y = (Delegatable) x;
                Object delegateObj = y.getDelegate();
                if (delegateObj != null && delegateObj instanceof EnumDelegate) {
                    EnumDelegate delegate = (EnumDelegate) delegateObj;
                    if (delegate.getCode() == code) {
                        return (T) x;
                    }
                }
            }
        }
        return value;
    }

    public static <T extends Enum> T ofName(final Class<T> tClass, final String name) {
        T value = null;
        if (Delegatable.class.isAssignableFrom((Class<?>) tClass)) {
            EnumSet<? extends Enum> enums = EnumSet.allOf(tClass);
            for (Enum x : enums) {
                Delegatable y = (Delegatable) x;
                Object delegateObj = y.getDelegate();
                if (delegateObj != null && delegateObj instanceof EnumDelegate) {
                    EnumDelegate delegate = (EnumDelegate) delegateObj;
                    if (delegate.getName().equals(name)) {
                        return (T) x;
                    }
                }
            }
        }
        return value;
    }

    public static <T extends Enum> T ofDisplayText(final Class<T> tClass, final String displayText) {
        T value = null;
        if (Delegatable.class.isAssignableFrom((Class<?>) tClass)) {
            EnumSet<? extends Enum> enums = EnumSet.allOf(tClass);
            for (Enum x : enums) {
                Delegatable y = (Delegatable) x;
                Object delegateObj = y.getDelegate();
                if (delegateObj != null && delegateObj instanceof EnumDelegate) {
                    EnumDelegate delegate = (EnumDelegate) delegateObj;
                    if (delegate.getDisplayText().equals(displayText)) {
                        return (T) x;
                    }
                }
            }
        }
        return value;
    }
}