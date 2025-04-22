package com.jn.langx.security.crypto.pbe.pbkdf.argon2;

import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeySpec;

/**
 * @since 5.5.0
 */
public class Argon2KeySpec extends PBKDFKeySpec {
    private Argon2Parameters parameters;

    private Argon2KeySpec(char[] password, byte[] salt, int keyBitSize, int iterationCount) {
        super(password, salt, keyBitSize, 0, iterationCount);
    }

    public Argon2KeySpec(char[] password, int keyBitSize, Argon2Parameters parameters) {
        this(password, parameters.getSalt(), keyBitSize, parameters.getIterations());
        this.parameters = parameters;
    }

    public Argon2Parameters getParameters() {
        return parameters;
    }


    public int getType() {
        return parameters.getType();
    }

    public int getMemory() {
        return parameters.getMemory();
    }

    public int getVersion() {
        return parameters.getVersion();
    }

    public int getLanes() {
        return parameters.getLanes();
    }
}
