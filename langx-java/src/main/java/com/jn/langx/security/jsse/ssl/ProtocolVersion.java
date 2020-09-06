package com.jn.langx.security.jsse.ssl;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum ProtocolVersion implements CommonEnum {
    NONE(-1,"NONE","NONE"),
    SSLv2Hello(2,"SSLv2Hello","SSLv2Hello"),
    SSLv30(768,"SSLv3","SSLv3"),
    TLSv10(769,"TLSv1","TLSv1"),
    TLSv11(770,"TLSv1.1","TLSv1.1"),
    TLSv12(771,"TLSv1.2","TLSv1.2"),
    TLSv13(772,"TLSv1.3","TLSv1.3")
    ;
    private EnumDelegate delegate;

    private ProtocolVersion(int code, String name ,String displayText){
        delegate = new EnumDelegate(code, name, displayText);
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
}
