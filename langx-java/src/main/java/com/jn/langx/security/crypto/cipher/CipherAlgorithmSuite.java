package com.jn.langx.security.crypto.cipher;

import com.jn.langx.Named;
import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;

/**
 * @since 4.2.7
 */
public class CipherAlgorithmSuite implements Named {
    /**
     * 算法名称
     */
    @NotEmpty
    private String algorithm;
    /**
     * 默认的 transformation
     */
    @NotEmpty
    private String transformation;

    @Nullable
    private Integer keySize;

    @Nullable
    private AlgorithmParameterSupplier parameterSupplier;

    public CipherAlgorithmSuite() {

    }

    public CipherAlgorithmSuite(String algorithm, String transformation) {
        this(algorithm, transformation, null);
    }

    public CipherAlgorithmSuite(String algorithm, String transformation, AlgorithmParameterSupplier parameterSupplier){
        this(algorithm, transformation, parameterSupplier, null);
    }
    public CipherAlgorithmSuite(String algorithm, String transformation, AlgorithmParameterSupplier parameterSupplier, Integer keySize) {
        setName(algorithm);
        setTransformation(transformation);
        setParameterSupplier(parameterSupplier);
        setKeySize(keySize);
    }

    public void setKeySize(Integer keySize) {
        this.keySize = keySize;
    }

    public Integer getKeySize() {
        return keySize;
    }

    public String getName() {
        return algorithm;
    }

    public void setName(String algorithm) {
        Preconditions.checkNotEmpty(algorithm);
        this.algorithm = algorithm;
    }

    public String getTransformation() {
        return transformation;
    }

    public void setTransformation(String transformation) {
        Preconditions.checkNotEmpty(transformation);
        this.transformation = transformation;
    }

    public AlgorithmParameterSupplier getParameterSupplier() {
        return parameterSupplier;
    }

    public void setParameterSupplier(AlgorithmParameterSupplier parameterSupplier) {
        this.parameterSupplier = parameterSupplier;
    }
}
