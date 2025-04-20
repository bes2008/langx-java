package com.jn.langx.security.crypto.pbe.pswdenc.scrypt;


import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeySpec;

public class ScryptPBKDFKeySpec extends PBKDFKeySpec {
    /**
     * CPU/Memory cost parameter. Must be larger than 1, a power of 2 and less than 2^(128 * memoryCost / 8).
     */
    int cpuCost;

    /**
     * the block size, must be >= 1.
     */
    int memoryCost;

    /**
     * Parallelization parameter. Must be a positive integer less than or equal to Integer.MAX_VALUE / (128 * memoryCost * 8).
     */
    int parallel;

    public int getCpuCost() {
        return cpuCost;
    }

    public void setCpuCost(int cpuCost) {
        this.cpuCost = cpuCost;
    }

    public int getMemoryCost() {
        return memoryCost;
    }

    public void setMemoryCost(int memoryCost) {
        this.memoryCost = memoryCost;
    }

    public int getParallel() {
        return parallel;
    }

    public void setParallel(int parallel) {
        this.parallel = parallel;
    }

    public ScryptPBKDFKeySpec(char[] password, byte[] salt, int keyBitSize) {
        super(password, salt, keyBitSize);
    }

    public ScryptPBKDFKeySpec(char[] password, byte[] salt, int keyBitSize, int iterationCount) {
        super(password, salt, keyBitSize, iterationCount);
    }

    public ScryptPBKDFKeySpec(char[] password, byte[] salt, int keySize, int ivBitSize, int iterationCount) {
        super(password, salt, keySize, ivBitSize, iterationCount);
    }

    public ScryptPBKDFKeySpec(char[] password, byte[] salt, int keySize, int ivBitSize, int iterationCount, String hashAlgorithm) {
        super(password, salt, keySize, ivBitSize, iterationCount, hashAlgorithm);
    }
}
