package com.jn.langx.security.ssl;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

/**
 * SSL1,SSL2, SSL3 都是 Netscape 公司发布的协议规范。其中SSL 1 已废除，不能再用。
 * TSL1.0 是基于SSL3略作改动后发布的。
 */
public enum SSLProtocolVersion implements CommonEnum {
    NONE(-1, "NONE", "NONE"),
    SSLv2Hello(2, "SSLv2Hello", "SSLv2Hello"),
    SSLv30(768, "SSLv3", "SSLv3"),
    TLSv10(769, "TLSv1", "TLSv1"),
    TLSv11(770, "TLSv1.1", "TLSv1.1"),
    TLSv12(771, "TLSv1.2", "TLSv1.2"),
    TLSv13(772, "TLSv1.3", "TLSv1.3"),
    GMSSLv10(770,1,0, "GMSSLv1.0","GMSSLv1.0"),
    GMSSLv11(770,1,1,"GMSSLv1.1","GMSSLv1.1");
    private EnumDelegate delegate;
    private byte major;
    private byte minor;

    private SSLProtocolVersion(int code, String name, String displayText) {
        this(code, (byte) (code >>> 8), (byte) (code & 255), name, displayText);
    }

    private SSLProtocolVersion(int code, int major, int minor, String name, String displayText) {
        this.delegate = new EnumDelegate(code, name, displayText);
        this.major = (byte) major;
        this.minor = (byte) minor;
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
