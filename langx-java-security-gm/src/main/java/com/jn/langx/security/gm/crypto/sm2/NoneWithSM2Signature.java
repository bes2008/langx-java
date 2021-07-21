package com.jn.langx.security.gm.crypto.sm2;

import com.jn.langx.security.gm.crypto.sm2.internal.BCSignatureSpi;
import com.jn.langx.security.gm.crypto.sm2.internal.NoneDigest;
import com.jn.langx.security.gm.crypto.sm2.internal.SM2Signer;
import com.jn.langx.security.gm.crypto.sm2.internal.StdDSAEncoder;

public class NoneWithSM2Signature extends BCSignatureSpi {
    public NoneWithSM2Signature() {
        super(new NoneDigest(), new SM2Signer(), new StdDSAEncoder());
    }
}
