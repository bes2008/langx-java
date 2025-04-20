package com.jn.langx.security.crypto.pbe.pbkdf;


/**
 * super class for all Password Based Encryption (PBE) parameter generator classes.
 */
public abstract class DerivedKeyGenerator {
    protected byte[] password;
    protected byte[] salt;
    protected int iterationCount;

    /**
     * base constructor.
     */
    protected DerivedKeyGenerator() {
    }

    /**
     * initialise the PBE generator.
     *
     * @param password       the password converted into bytes (see below).
     * @param salt           the salt to be mixed with the password.
     * @param iterationCount the number of iterations the "mixing" function
     *                       is to be applied for.
     */
    public void init(byte[] password, byte[] salt, int iterationCount) {
        this.password = password;
        this.salt = salt;
        this.iterationCount = iterationCount;
    }

    /**
     * return the password byte array.
     *
     * @return the password byte array.
     */
    public byte[] getPassword() {
        return password;
    }

    /**
     * return the salt byte array.
     *
     * @return the salt byte array.
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * return the iteration count.
     *
     * @return the iteration count.
     */
    public int getIterationCount() {
        return iterationCount;
    }

    /**
     * generate derived parameters for a key of length keySize.
     *
     * @param keyBitSize the length, in bits, of the key required.
     * @return a parameters object representing a key.
     */
    public abstract SimpleDerivedKey generateDerivedKey(int keyBitSize);

    /**
     * generate derived parameters for a key of length keySize, and
     * an initialisation vector (IV) of length ivSize.
     *
     * @param keyBitSize the length, in bits, of the key required.
     * @param ivSize     the length, in bits, of the iv required.
     * @return a parameters object representing a key and an IV.
     */
    public abstract SimpleDerivedKey generateDerivedKeyWithIV(int keyBitSize, int ivBitSize);

    /**
     * generate derived parameters for a key of length keySize, specifically
     * for use with a MAC.
     *
     * @param keyBitSize the length, in bits, of the key required.
     * @return a parameters object representing a key.
     */
    public abstract SimpleDerivedKey generateDerivedKeyUseHMac(int keyBitSize);

}