package com.jn.langx.security.gm.crypto.bc.asymmetric.sm2;

import com.jn.langx.util.Objs;
import com.jn.langx.util.io.Charsets;
import org.bouncycastle.jcajce.spec.SM2ParameterSpec;

public class SM2SignParameterSpec extends SM2ParameterSpec {
    private static byte[] DEFAULT_ID_BYTES = "1234567812345678".getBytes(Charsets.UTF_8);
    public SM2SignParameterSpec() {
        this(null);
    }
    public SM2SignParameterSpec(byte[] userId) {
        super(Objs.isEmpty(userId)? DEFAULT_ID_BYTES:userId );
    }
}
