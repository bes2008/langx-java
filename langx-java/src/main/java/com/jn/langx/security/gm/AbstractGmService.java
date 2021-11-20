package com.jn.langx.security.gm;

import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.util.enums.Enums;

public abstract class AbstractGmService implements GmService {
    @Deprecated
    @Override
    public final byte[] sm4Encrypt(byte[] data, String mode, byte[] secretKey) {
        Symmetrics.MODE m = Enums.ofName(Symmetrics.MODE.class, mode);
        return this.sm4Encrypt(data, m, secretKey);
    }

    @Deprecated
    @Override
    public final byte[] sm4Encrypt(byte[] data, String mode, byte[] secretKey, byte[] iv) {
        Symmetrics.MODE m = Enums.ofName(Symmetrics.MODE.class, mode);
        return this.sm4Encrypt(data, m, secretKey, iv);
    }

    @Deprecated
    @Override
    public final byte[] sm4Decrypt(byte[] encryptedBytes, String mode, byte[] secretKey) {
        Symmetrics.MODE m = Enums.ofName(Symmetrics.MODE.class, mode);
        return this.sm4Decrypt(encryptedBytes, m, secretKey);
    }

    @Deprecated
    @Override
    public final byte[] sm4Decrypt(byte[] encryptedBytes, String mode, byte[] secretKey, byte[] iv) {
        Symmetrics.MODE m = Enums.ofName(Symmetrics.MODE.class, mode);
        return this.sm4Decrypt(encryptedBytes, m, secretKey, iv);
    }
}
