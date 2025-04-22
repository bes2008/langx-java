package com.jn.langx.security.crypto.pbe.pswdenc.argon2;


import com.jn.langx.util.collection.PrimitiveArrays;

/**
 * @since 5.5.0
 */
class Argon2Parameters {
    private final byte[] salt;
    private final byte[] secret;
    private final byte[] additional;

    private final int iterations;
    private final int memory;
    private final int lanes;

    private final int version;
    private final int type;

    Argon2Parameters(
            int type,
            byte[] salt,
            byte[] secret,
            byte[] additional,
            int iterations,
            int memory,
            int lanes,
            int version) {

        this.salt = PrimitiveArrays.clone(salt);
        this.secret = PrimitiveArrays.clone(secret);
        this.additional = PrimitiveArrays.clone(additional);
        this.iterations = iterations;
        this.memory = memory;
        this.lanes = lanes;
        this.version = version;
        this.type = type;
    }

    public byte[] getSalt() {
        return PrimitiveArrays.clone(salt);
    }

    public byte[] getSecret() {
        return PrimitiveArrays.clone(secret);
    }

    public byte[] getAdditional() {
        return PrimitiveArrays.clone(additional);
    }

    public int getIterations() {
        return iterations;
    }

    public int getMemory() {
        return memory;
    }

    public int getLanes() {
        return lanes;
    }

    public int getVersion() {
        return version;
    }

    public int getType() {
        return type;
    }

    public void clear() {
        PrimitiveArrays.clear(salt);
        PrimitiveArrays.clear(secret);
        PrimitiveArrays.clear(additional);
    }
}
