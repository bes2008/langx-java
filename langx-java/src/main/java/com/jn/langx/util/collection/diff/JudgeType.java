package com.jn.langx.util.collection.diff;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum JudgeType implements CommonEnum {
    ADDED(1, "added"), REMOVED(2, "removed"),UPDATED(4,"updated"), EQUALED(8, "equaled");

    private EnumDelegate enumDelegate;

    JudgeType(int code, String name) {
        this.enumDelegate = new EnumDelegate(code, name, name);
    }

    @Override
    public String getName() {
        return this.enumDelegate.getName();
    }

    @Override
    public int getCode() {
        return this.enumDelegate.getCode();
    }

    @Override
    public String getDisplayText() {
        return this.enumDelegate.getDisplayText();
    }
}
