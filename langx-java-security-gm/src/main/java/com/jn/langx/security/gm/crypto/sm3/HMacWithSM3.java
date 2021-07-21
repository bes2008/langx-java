package com.jn.langx.security.gm.crypto.sm3;

import com.jn.langx.security.gm.crypto.sm3.internal._SM3DigestImpl;
import org.bouncycastle.crypto.macs.HMac;

public class HMacWithSM3 extends JCEMac
{
    public HMacWithSM3() {
        super(new HMac(new _SM3DigestImpl()));
    }
}
