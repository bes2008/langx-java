package com.jn.langx.security.crypto.pbe.pswdenc;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.security.SecurityException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.pbe.PBEs;
import com.jn.langx.security.crypto.pbe.pbkdf.DerivedKeyFormatter;
import com.jn.langx.security.crypto.pbe.pbkdf.DerivedPBEKey;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeySpec;
import com.jn.langx.security.crypto.salt.BytesSaltGenerator;
import com.jn.langx.security.crypto.salt.RandomBytesSaltGenerator;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.io.bytes.Bytes;

import javax.crypto.SecretKeyFactory;

public class PBKDFPasswordEncryptor implements PasswordEncryptor {
    protected int saltBitLength;
    /**
     * 生成的 hash 的 长度
     */
    protected int hashBitLength;
    protected int ivBitLength = 0;

    protected String pbkdfAlgorithm;
    protected String hashAlgorithm;
    protected int iterations;

    @NonNull
    protected BytesSaltGenerator saltGenerator = new RandomBytesSaltGenerator();
    @Nullable
    protected DerivedKeyFormatter derivedKeyFormatter;

    public PBKDFPasswordEncryptor() {
    }

    public PBKDFPasswordEncryptor(String pbkdfAlgorithm, String hashAlgorithm, int keyBitLength, int saltBitLength, int iterations) {
        this(pbkdfAlgorithm, hashAlgorithm, keyBitLength, saltBitLength, iterations, 0);
    }

    public PBKDFPasswordEncryptor(String pbkdfAlgorithm, String hashAlgorithm, int keyBitLength, int saltBitLength, int iterations, int ivBitLength) {
        this.pbkdfAlgorithm = pbkdfAlgorithm;
        this.hashAlgorithm = hashAlgorithm;
        this.hashBitLength = keyBitLength;
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

    protected PBKDFKeySpec buildParams(char[] password, byte[] salt) {
        return new PBKDFKeySpec(password, salt, hashBitLength, ivBitLength, iterations, hashAlgorithm);
    }

    @Override
    public String encrypt(String password) {
        Preconditions.checkNotNull(password, "password is null");
        Preconditions.checkNotNull(this.pbkdfAlgorithm, "the pbkdf algorithm is required");
        try {
            byte[] salt = saltGenerator.get(Securitys.getBytesLength(this.saltBitLength));
            PBKDFKeySpec keySpec = buildParams(password.toCharArray(), salt);
            SecretKeyFactory secretKeyFactory = PBEs.getLangxPBEKeyFactory(this.pbkdfAlgorithm);
            DerivedPBEKey derivedPBEKey = (DerivedPBEKey) secretKeyFactory.generateSecret(keySpec);
            return stringify(derivedPBEKey);
        } catch (Throwable e) {
            throw new SecurityException(e);
        }
    }

    protected String stringify(DerivedPBEKey derivedPBEKey) {
        return this.derivedKeyFormatter.format(derivedPBEKey);
    }

    protected DerivedPBEKey extract(String plainPassword, String encryptedPassword) {
        return this.derivedKeyFormatter.extract(plainPassword, encryptedPassword);
    }

    @Override
    public boolean check(String plainPassword, String encryptedPassword) {
        if (Strings.isBlank(encryptedPassword)) {
            return false;
        }
        try {
            DerivedPBEKey derivedPBEKey = extract(plainPassword, encryptedPassword);
            byte[] actualDerivedKey = derivedPBEKey.getEncoded();
            PBKDFKeySpec keySpec = derivedPBEKey.getKeySpec();
            SecretKeyFactory secretKeyFactory = PBEs.getLangxPBEKeyFactory(this.pbkdfAlgorithm);
            DerivedPBEKey expectedDerivedPBEKey = (DerivedPBEKey) secretKeyFactory.generateSecret(keySpec);
            byte[] expectedDerivedKey = expectedDerivedPBEKey.getEncoded();
            return Bytes.arrayEquals(actualDerivedKey, expectedDerivedKey);
        } catch (Throwable e) {
            throw new SecurityException(e);
        }
    }
}
