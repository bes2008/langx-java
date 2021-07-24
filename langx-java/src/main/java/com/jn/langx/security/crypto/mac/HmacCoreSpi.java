package com.jn.langx.security.crypto.mac;

import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.util.collection.Arrs;

import javax.crypto.MacSpi;
import javax.crypto.SecretKey;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

/**
 * ref: sun provider
 */
public class HmacCoreSpi extends MacSpi implements Cloneable {
    private MessageDigest messageDigest;
    private byte[] k_ipad;
    private byte[] k_opad;
    private boolean first;
    private final int blockLen;

    public HmacCoreSpi(MessageDigest digest, int blockLength) {
        this.messageDigest = digest;
        if (blockLength <= 8) {
            blockLength = 64;
        }
        this.blockLen = blockLength;
        this.k_ipad = new byte[this.blockLen];
        this.k_opad = new byte[this.blockLen];
        this.first = true;
    }

    public HmacCoreSpi(String digestAlgorithm) {
        this(digestAlgorithm, HMacs.getBlockLength(digestAlgorithm));
    }

    public HmacCoreSpi(String digestAlgorithm, int blockLength) {
        this(MessageDigests.newDigest(digestAlgorithm), blockLength);
    }

    protected int engineGetMacLength() {
        return this.messageDigest.getDigestLength();
    }

    protected void engineInit(Key key, AlgorithmParameterSpec parameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (parameterSpec != null) {
            throw new InvalidAlgorithmParameterException("HMAC does not use parameters");
        } else if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Secret key expected");
        } else {
            byte[] keyBytes = key.getEncoded();
            if (keyBytes == null) {
                throw new InvalidKeyException("Missing key data");
            } else {
                if (keyBytes.length > this.blockLen) {
                    byte[] digest = this.messageDigest.digest(keyBytes);
                    Arrays.fill(keyBytes, (byte) 0);
                    keyBytes = digest;
                }

                for (int i = 0; i < this.blockLen; ++i) {
                    byte b = i < keyBytes.length ? keyBytes[i] : 0;
                    this.k_ipad[i] = (byte) (b ^ 54);
                    this.k_opad[i] = (byte) (b ^ 92);
                }

                Arrays.fill(keyBytes, (byte) 0);
                this.engineReset();
            }
        }
    }

    protected void engineUpdate(byte b) {
        if (this.first) {
            this.messageDigest.update(this.k_ipad);
            this.first = false;
        }

        this.messageDigest.update(b);
    }

    protected void engineUpdate(byte[] bytes, int off, int len) {
        if (this.first) {
            this.messageDigest.update(this.k_ipad);
            this.first = false;
        }

        this.messageDigest.update(bytes, off, len);
    }

    protected void engineUpdate(ByteBuffer buffer) {
        if (this.first) {
            this.messageDigest.update(this.k_ipad);
            this.first = false;
        }

        this.messageDigest.update(buffer);
    }

    protected byte[] engineDoFinal() {
        if (this.first) {
            this.messageDigest.update(this.k_ipad);
        } else {
            this.first = true;
        }

        try {
            byte[] digest = this.messageDigest.digest();
            this.messageDigest.update(this.k_opad);
            this.messageDigest.update(digest);
            this.messageDigest.digest(digest, 0, digest.length);
            return digest;
        } catch (DigestException var2) {
            throw new ProviderException(var2);
        }
    }

    protected void engineReset() {
        if (!this.first) {
            this.messageDigest.reset();
            this.first = true;
        }

    }

    public Object clone() throws CloneNotSupportedException {
        HmacCoreSpi var1 = (HmacCoreSpi) super.clone();
        var1.messageDigest = (MessageDigest) this.messageDigest.clone();
        var1.k_ipad = Arrs.copy(this.k_ipad);
        var1.k_opad = Arrs.copy(this.k_opad);
        return var1;
    }
}