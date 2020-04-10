package com.jn.langx.util.enums.base;

import com.jn.langx.Delegatable;

/**
 * common enum template
 */
class DelegatableCommonEnum implements CommonEnum, Delegatable<EnumDelegate> {
    private EnumDelegate delegate;

    @Override
    public int getCode() {
        return delegate.getCode();
    }

    @Override
    public void setCode(int code) {
        delegate.setCode(code);
    }

    @Override
    public void setName(String name) {
        delegate.setName(name);
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getDisplayText() {
        return delegate.getDisplayText();
    }

    @Override
    public void setDisplayText(String displayText) {
        delegate.setDisplayText(displayText);
    }

    @Override
    public EnumDelegate getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(EnumDelegate delegate) {
        this.delegate = delegate;
    }
}
