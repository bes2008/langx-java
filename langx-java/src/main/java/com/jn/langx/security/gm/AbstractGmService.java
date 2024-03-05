package com.jn.langx.security.gm;

import com.jn.langx.security.crypto.cipher.Ciphers;

public abstract class AbstractGmService implements GmService {

    @Override
    public byte[] createSM4IV(int ivBitLength) {
        return Ciphers.createIvParameterSpec(128).getIV();
    }

    @Override
    public byte[] createSM4IV() {
        return createSM4IV(128);
    }
}
