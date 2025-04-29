package com.jn.langx.security.crypto.pbe.pbkdf.argon2;

/**
 * @since 5.5.0
 */
public class Argon2Constants {
    public static final int ARGON2_d = 0x00;
    public static final int ARGON2_i = 0x01;
    public static final int ARGON2_id = 0x02;

    public static final int ARGON2_VERSION_10 = 0x10;
    public static final int ARGON2_VERSION_13 = 0x13;

    public static final int DEFAULT_ITERATIONS = 3;
    public static final int DEFAULT_MEMORY_COST = 12;
    public static final int DEFAULT_LANES = 1;
    public static final int DEFAULT_TYPE = ARGON2_i;
    public static final int DEFAULT_VERSION = ARGON2_VERSION_13;
}
