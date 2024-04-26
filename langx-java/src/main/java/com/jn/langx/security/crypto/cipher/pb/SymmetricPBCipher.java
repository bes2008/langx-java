package com.jn.langx.security.crypto.cipher.pb;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.security.crypto.cipher.*;
import com.jn.langx.security.crypto.key.pb.DerivedKey;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedSecretKeySupplier;

import javax.crypto.spec.IvParameterSpec;

public class SymmetricPBCipher extends AbstractPBCipher {
    @NonNull
    private String cryptoAlgorithm;
    @NonNull
    private Symmetrics.MODE mode;
    @NonNull
    private CipherAlgorithmPadding padding;

    public SymmetricPBCipher(
            String password,
            int saltBitLength,
            int keyBitSize,
            int ivBitSize,
            String hashAlgorithm,
            int iterations,
            String cryptoAlgorithm,
            Symmetrics.MODE mode,
            CipherAlgorithmPadding padding
    ) {
        super(password, saltBitLength, keyBitSize, ivBitSize, hashAlgorithm, iterations);
        this.cryptoAlgorithm = cryptoAlgorithm;
        this.mode = mode;
        this.padding = padding;
    }

    @Override
    protected byte[] enc(byte[] message, DerivedKey derivedKey) {
        byte[] encryptedBytes = Symmetrics.encrypt(
                message,
                derivedKey.getSecretKey(),
                this.cryptoAlgorithm,
                Ciphers.createAlgorithmTransformation(this.cryptoAlgorithm, mode.name(), padding),
                null,
                null,
                new BytesBasedSecretKeySupplier(),
                new IvParameterSpec(derivedKey.getIv())
        );
        return encryptedBytes;
    }

    @Override
    protected byte[] dec(byte[] message, DerivedKey derivedKey) {
        byte[] decryptedBytes = Symmetrics.decrypt(
                message,
                derivedKey.getSecretKey(),
                this.cryptoAlgorithm,
                Ciphers.createAlgorithmTransformation(this.cryptoAlgorithm, mode.name(), padding),
                null,
                null,
                new BytesBasedSecretKeySupplier(),
                new IvParameterSpec(derivedKey.getIv())
        );
        return decryptedBytes;
    }
}
