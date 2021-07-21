package com.jn.langx.security.gm.crypto.sm3;

import org.bouncycastle.crypto.macs.HMac;

public class HMacWithSM3 extends JCEMac
{
    public HMacWithSM3() {
        super(new HMac(new SM3DigestImpl()));
    }
}
