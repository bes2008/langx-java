package com.jn.langx.security.gm.crypto.bc.crypto.asymmetric.sm2;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.io.Charsets;
import org.bouncycastle.util.Arrays;

import java.security.spec.AlgorithmParameterSpec;

public class SM2ParameterSpec implements AlgorithmParameterSpec, IDGetter {
    private static String DEFAULT_ID = "1234567812345678";
    private byte[] id = DEFAULT_ID.getBytes(Charsets.UTF_8);

    public SM2ParameterSpec() {
        this((String) null);
    }

    public SM2ParameterSpec(String id) {
        this(Strings.isEmpty(id) ? (byte[]) null : id.getBytes(Charsets.UTF_8));
    }

    public SM2ParameterSpec(byte[] id) {
        if (id != null) {
            this.id = PrimitiveArrays.copy(id);
        }
    }

    public byte[] getID() {
        return Arrays.clone(this.id);
    }
}
