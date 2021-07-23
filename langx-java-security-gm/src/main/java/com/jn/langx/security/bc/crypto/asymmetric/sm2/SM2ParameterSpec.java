package com.jn.langx.security.bc.crypto.asymmetric.sm2;

import com.jn.langx.util.io.Charsets;
import org.bouncycastle.util.Arrays;

import java.security.spec.AlgorithmParameterSpec;

public class SM2ParameterSpec implements AlgorithmParameterSpec, IDGetter{
    private static String DEFAULT_ID = "1234567812345678";
    private byte[] id = DEFAULT_ID.getBytes(Charsets.UTF_8);

    public SM2ParameterSpec(byte[] var1) {
        if (var1 != null) {
            this.id = Arrays.clone(var1);
        }
    }

    public byte[] getID() {
        return Arrays.clone(this.id);
    }
}
