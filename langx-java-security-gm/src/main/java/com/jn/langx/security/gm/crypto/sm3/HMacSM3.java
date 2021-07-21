package com.jn.langx.security.gm.crypto.sm3;

import org.bouncycastle.crypto.macs.HMac;

public class HMacSM3 extends JCEMac
{
    public HMacSM3() {
        super(new HMac(new SM3()));
    }
}
