package com.jn.langx.security.hash;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.hex.Hex;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.MessageDigests;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Maths;
import com.jn.langx.util.collection.PrimitiveArrays;

import java.security.MessageDigest;

public class MessageDigestHasher {
    private static final int DEFAULT_ITERATIONS = 1;

    /**
     * 加盐值
     */
    private byte[] salt = null;
    /**
     * hash 计算 迭代次数
     */
    private int iterations = DEFAULT_ITERATIONS;
    /**
     * hash 算法名称
     */
    private String algorithmName;

    public byte[] getSalt() {
        return PrimitiveArrays.copy(salt);
    }

    public void setSalt(byte[] salt) {
        this.salt = PrimitiveArrays.copy(salt);
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = Maths.max(DEFAULT_ITERATIONS, iterations);
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        if (Emptys.isNotEmpty(algorithmName)) {
            this.algorithmName = algorithmName;
        }
    }


    public MessageDigestHasher(@NonNull String algorithmName) {
        this(algorithmName, null);
    }

    public MessageDigestHasher(@NonNull String algorithmName, byte[] salt) {
        this(algorithmName, salt, DEFAULT_ITERATIONS);
    }

    /**
     * Creates an {@code algorithmName}-specific hash of the specified {@code source} using the given
     * {@code salt} a total of {@code hashIterations} times.
     * <p/>
     *
     * @param algorithmName  the {@link java.security.MessageDigest MessageDigest} algorithm name to use when
     *                       performing the hash.
     * @param salt           the salt to use for the hash
     * @param hashIterations the number of times the {@code source} argument hashed for attack resiliency.
     */
    public MessageDigestHasher(@NonNull String algorithmName, @Nullable byte[] salt, int hashIterations) {
        setAlgorithmName(algorithmName);
        setIterations(iterations);
        setSalt(salt);
    }

    public byte[] hash(byte[] source) {
        return doHash(source, salt, iterations);
    }

    public String hashToHexString(byte[] source, boolean lowerCase) {
        byte[] hashed = hash(source);
        return Hex.encodeHexString(hashed, lowerCase);
    }

    public String hashToBase64String(byte[] source) {
        byte[] hashed = hash(source);
        return Base64.encodeBase64String(hashed);
    }


    /**
     * Hashes the specified byte array using the given {@code salt} for the specified number of iterations.
     *
     * @param bytes          the bytes to hash
     * @param salt           the salt to use for the initial hash
     * @param hashIterations the number of times the the {@code bytes} will be hashed (for attack resiliency).
     * @return the hashed bytes.
     */
    protected byte[] doHash(byte[] bytes, byte[] salt, int hashIterations) {
        MessageDigest digest = MessageDigests.newDigest(algorithmName);
        if (Emptys.isNotEmpty(salt)) {
            digest.reset();
            digest.update(salt);
        }
        byte[] hashed = digest.digest(bytes);
        int iterations = hashIterations - 1; //already hashed once above
        //iterate remaining number:
        for (int i = 0; i < iterations; i++) {
            digest.reset();
            hashed = digest.digest(hashed);
        }
        return hashed;
    }

}
