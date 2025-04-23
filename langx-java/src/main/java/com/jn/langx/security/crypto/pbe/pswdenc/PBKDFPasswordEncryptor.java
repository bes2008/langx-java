package com.jn.langx.security.crypto.pbe.pswdenc;

import com.jn.langx.security.SecurityException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.pbe.PBEs;
import com.jn.langx.security.crypto.pbe.pbkdf.DerivedKeyFormatter;
import com.jn.langx.security.crypto.pbe.pbkdf.DerivedPBEKey;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeySpec;
import com.jn.langx.security.crypto.salt.BytesSaltGenerator;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.struct.Pair;

import javax.crypto.SecretKeyFactory;

public class PBKDFPasswordEncryptor implements PasswordEncryptor {
    private int saltBitLength;
    /**
     * 生成的 hash 的 长度
     */
    private int keyBitLength;
    private int ivBitLength;

    private String pbkdfAlgorithm;
    private String hashAlgorithm;
    private int iterations;

    private BytesSaltGenerator saltGenerator;
    private DerivedKeyFormatter derivedKeyFormatter;

    public PBKDFPasswordEncryptor() {
    }

    public PBKDFPasswordEncryptor(String pbkdfAlgorithm, String hashAlgorithm, int keyBitLength, int saltBitLength, int ivBitLength, int iterations) {
        this.pbkdfAlgorithm = pbkdfAlgorithm;
        this.hashAlgorithm = hashAlgorithm;
        this.keyBitLength = keyBitLength;
        this.saltBitLength = saltBitLength;
        this.iterations = iterations;
        this.ivBitLength = ivBitLength;
    }

    public void setSaltGenerator(BytesSaltGenerator saltGenerator) {
        this.saltGenerator = saltGenerator;
    }

    public void setDerivedKeyFormatter(DerivedKeyFormatter derivedKeyFormatter) {
        this.derivedKeyFormatter = derivedKeyFormatter;
    }

    protected PBKDFKeySpec buildParams(char[] password) {
        byte[] salt = saltGenerator.get(Securitys.getBytesLength(this.saltBitLength));
        return new PBKDFKeySpec(password, salt, keyBitLength, ivBitLength, iterations, hashAlgorithm);
    }
    @Override
    public String encrypt(String password) {
        Preconditions.checkNotNull(password, "password is null");
        try {
            PBKDFKeySpec keySpec = buildParams(password.toCharArray());
            SecretKeyFactory secretKeyFactory = PBEs.getLangxPBEKeyFactory(this.pbkdfAlgorithm);
            DerivedPBEKey derivedPBEKey = (DerivedPBEKey) secretKeyFactory.generateSecret(keySpec);
            return this.derivedKeyFormatter.format(derivedPBEKey);
        } catch (Throwable e) {
            throw new SecurityException(e);
        }
    }

    protected String stringify(DerivedPBEKey derivedPBEKey) {
        return this.derivedKeyFormatter.format(derivedPBEKey);
    }

    protected Pair<byte[], ? extends DerivedPBEKey> extract(String plainPassword, String encryptedPassword) {
        return this.derivedKeyFormatter.extract(plainPassword, encryptedPassword);
    }

    @Override
    public boolean check(String plainPassword, String encryptedPassword) {
        try {
            Pair<byte[], ? extends DerivedPBEKey> pair = extract(plainPassword, encryptedPassword);
            byte[] actualDerivedKey = pair.getKey();
            DerivedPBEKey paramsHolder = pair.getValue();
            PBKDFKeySpec keySpec = paramsHolder.getKeySpec();
            SecretKeyFactory secretKeyFactory = PBEs.getLangxPBEKeyFactory(this.pbkdfAlgorithm);
            DerivedPBEKey expectedDerivedPBEKey = (DerivedPBEKey) secretKeyFactory.generateSecret(keySpec);
            byte[] expectedDerivedKey = expectedDerivedPBEKey.getEncoded();
            return Objs.equals(actualDerivedKey, expectedDerivedKey);
        } catch (Throwable e) {
            throw new SecurityException(e);
        }
    }
}
