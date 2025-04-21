package com.jn.langx.security.crypto.pbe.pswdenc.argon2;


import com.jn.langx.security.crypto.pbe.pbkdf.PasswordConverter;
import com.jn.langx.security.crypto.pbe.pbkdf.PasswordToPkcs5Utf8Converter;
import com.jn.langx.util.collection.PrimitiveArrays;

public class Argon2Parameters {
    public static final int ARGON2_d = 0x00;
    public static final int ARGON2_i = 0x01;
    public static final int ARGON2_id = 0x02;

    public static final int ARGON2_VERSION_10 = 0x10;
    public static final int ARGON2_VERSION_13 = 0x13;

    private static final int DEFAULT_ITERATIONS = 3;
    private static final int DEFAULT_MEMORY_COST = 12;
    private static final int DEFAULT_LANES = 1;
    private static final int DEFAULT_TYPE = ARGON2_i;
    private static final int DEFAULT_VERSION = ARGON2_VERSION_13;

    public static class Builder {
        private byte[] salt;
        private byte[] secret;
        private byte[] additional;

        private int iterations;
        private int memory;
        private int lanes;

        private int version;
        private final int type;

        private PasswordConverter converter = new PasswordToPkcs5Utf8Converter();

        public Builder() {
            this(DEFAULT_TYPE);
        }

        public Builder(int type) {
            this.type = type;
            this.lanes = DEFAULT_LANES;
            this.memory = 1 << DEFAULT_MEMORY_COST;
            this.iterations = DEFAULT_ITERATIONS;
            this.version = DEFAULT_VERSION;
        }

        public Builder withParallelism(int parallelism) {
            this.lanes = parallelism;
            return this;
        }

        public Builder withSalt(byte[] salt) {
            this.salt = PrimitiveArrays.clone(salt);
            return this;
        }

        public Builder withSecret(byte[] secret) {
            this.secret = PrimitiveArrays.clone(secret);
            return this;
        }

        public Builder withAdditional(byte[] additional) {
            this.additional = PrimitiveArrays.clone(additional);
            return this;
        }

        public Builder withIterations(int iterations) {
            this.iterations = iterations;
            return this;
        }


        public Builder withMemoryAsKB(int memory) {
            this.memory = memory;
            return this;
        }


        public Builder withMemoryPowOfTwo(int memory) {
            this.memory = 1 << memory;
            return this;
        }

        public Builder withVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder withPasswordConverter(PasswordConverter converter) {
            this.converter = converter;
            return this;
        }

        public Argon2Parameters build() {
            return new Argon2Parameters(type, salt, secret, additional, iterations, memory, lanes, version, converter);
        }

        public void clear() {
            PrimitiveArrays.clear(salt);
            PrimitiveArrays.clear(secret);
            PrimitiveArrays.clear(additional);
        }
    }

    private final byte[] salt;
    private final byte[] secret;
    private final byte[] additional;

    private final int iterations;
    private final int memory;
    private final int lanes;

    private final int version;
    private final int type;
    private final PasswordConverter converter;

    private Argon2Parameters(
            int type,
            byte[] salt,
            byte[] secret,
            byte[] additional,
            int iterations,
            int memory,
            int lanes,
            int version,
            PasswordConverter converter) {

        this.salt = PrimitiveArrays.clone(salt);
        this.secret = PrimitiveArrays.clone(secret);
        this.additional = PrimitiveArrays.clone(additional);
        this.iterations = iterations;
        this.memory = memory;
        this.lanes = lanes;
        this.version = version;
        this.type = type;
        this.converter = converter;
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

    public PasswordConverter getPasswordConverter() {
        return converter;
    }

    public void clear() {
        PrimitiveArrays.clear(salt);
        PrimitiveArrays.clear(secret);
        PrimitiveArrays.clear(additional);
    }
}
