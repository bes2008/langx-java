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
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getDisplayText() {
        return delegate.getDisplayText();
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
