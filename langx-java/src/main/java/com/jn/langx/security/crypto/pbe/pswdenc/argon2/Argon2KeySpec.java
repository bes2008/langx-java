package com.jn.langx.security.crypto.pbe.pswdenc.argon2;

import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeySpec;

public class Argon2KeySpec extends PBKDFKeySpec {
    private Argon2Parameters parameters;

    public Argon2KeySpec(char[] password, byte[] salt, int keyBitSize, int iterationCount) {
        super(password, salt, keyBitSize, 0, iterationCount);
    }

    public Argon2Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Argon2Parameters parameters) {
        this.parameters = parameters;
    }
}
