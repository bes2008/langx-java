package com.jn.langx.configuration;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum ConfigurationEventType implements CommonEnum{
    ADD(0, "ADD", "add a configuration"),
    REMOVE(1, "REMOVE", "remove a configuration"),
    UPDATE(2, "UPDATE", "update a configuration");

    private EnumDelegate delegate;

    ConfigurationEventType(int code, String name, String displayText) {
        this.delegate = new EnumDelegate(code, name, displayText);
    }


    @Override
    public int getCode() {
        return this.delegate.getCode();
    }

    @Override
    public String getDisplayText() {
        return this.delegate.getDisplayText();
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }
}
