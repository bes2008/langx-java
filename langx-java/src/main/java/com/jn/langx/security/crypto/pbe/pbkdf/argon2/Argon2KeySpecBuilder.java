package com.jn.langx.security.crypto.pbe.pbkdf.argon2;

import com.jn.langx.Builder;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.PrimitiveArrays;

import static com.jn.langx.security.crypto.pbe.pbkdf.argon2.Argon2Constants.*;

/**
 * @since 5.5.0
 */
public class Argon2KeySpecBuilder implements Builder<Argon2KeySpec> {


    private byte[] salt;
    private byte[] secret;
    private byte[] additional;

    private int iterations;
    private int memory;
    private int lanes;

    private int version;
    private final int type;

    /**
     * 这两个是额外加的参数
     */
    private int keyBitSize;
    private char[] password;

    public Argon2KeySpecBuilder() {
        this(Argon2Constants.DEFAULT_TYPE);
    }

    public Argon2KeySpecBuilder(int type) {
        this.type = type;
        this.lanes = DEFAULT_LANES;
        this.memory = 1 << DEFAULT_MEMORY_COST;
        this.iterations = DEFAULT_ITERATIONS;
        this.version = DEFAULT_VERSION;
    }

    public Argon2KeySpecBuilder withKeyBitSize(int keyBitSize) {
        this.keyBitSize = keyBitSize;
        return this;
    }

    public Argon2KeySpecBuilder withParallelism(int parallelism) {
        this.lanes = parallelism;
        return this;
    }

    public Argon2KeySpecBuilder withSalt(byte[] salt) {
        this.salt = PrimitiveArrays.clone(salt);
        return this;
    }

    public Argon2KeySpecBuilder withSecret(byte[] secret) {
        this.secret = PrimitiveArrays.clone(secret);
        return this;
    }

    public Argon2KeySpecBuilder withAdditional(byte[] additional) {
        this.additional = PrimitiveArrays.clone(additional);
        return this;
    }

    public Argon2KeySpecBuilder withPassword(char[] password) {
        this.password = password;
        return this;
    }

    public Argon2KeySpecBuilder withIterations(int iterations) {
        this.iterations = iterations;
        return this;
    }


    public Argon2KeySpecBuilder withMemoryAsKB(int memory) {
        this.memory = memory;
        return this;
    }


    public Argon2KeySpecBuilder withMemoryPowOfTwo(int memory) {
        this.memory = 1 << memory;
        return this;
    }

    public Argon2KeySpecBuilder withVersion(int version) {
        this.version = version;
        return this;
    }


    public Argon2KeySpec build() {
        Preconditions.checkNotNull(password, "password is null");
        Preconditions.checkArgument(keyBitSize > 112, "the key bit size is not set ");
        Argon2Parameters parameters = new Argon2Parameters(type, salt, secret, additional, iterations, memory, lanes, version);
        Argon2KeySpec keySpec = new Argon2KeySpec(this.password, this.keyBitSize, parameters);
        return keySpec;
    }

    public void clear() {
        PrimitiveArrays.clear(salt);
        PrimitiveArrays.clear(secret);
        PrimitiveArrays.clear(additional);
    }
}