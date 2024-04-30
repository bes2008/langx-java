package com.jn.langx.security.gm;

import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.key.PKIs;

public abstract class AbstractGmService implements GmService {
    @Override
    public byte[] createSM4Key(int bitLength) {
        return PKIs.createSecretKey("SM4","BC",bitLength,null).getEncoded();
    }

    @Override
    public byte[] createSM4Key() {
        return PKIs.createSecretKeyBytes(128);
    }

    @Override
    public byte[] createSM4IV(int ivBitLength) {
        return Ciphers.createIvParameterSpec(128).getIV();
    }

    @Override
    public byte[] createSM4IV() {
        return createSM4IV(128);
    }
}
