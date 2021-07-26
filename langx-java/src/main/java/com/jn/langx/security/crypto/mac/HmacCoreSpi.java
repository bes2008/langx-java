package com.jn.langx.security.crypto.mac;

import com.jn.langx.security.crypto.digest.BufferSizeAware;
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
    private byte[] inputPad;
    private byte[] outPad;
    private boolean first;
    private final int blockLength;

    public HmacCoreSpi(MessageDigest digest, int blockLength) {
        this.messageDigest = digest;

        if (blockLength <= 8) {
            if (this.messageDigest instanceof BufferSizeAware) {
                blockLength = ((BufferSizeAware) this.messageDigest).getBufferSize();
            }
            if (blockLength <= 8) {
                blockLength = 64;
            }
        }
        this.blockLength = blockLength;
        this.inputPad = new byte[this.blockLength];
        this.outPad = new byte[this.blockLength];
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
                if (keyBytes.length > this.blockLength) {
                    byte[] digest = this.messageDigest.digest(keyBytes);
                    Arrays.fill(keyBytes, (byte) 0);
                    keyBytes = digest;
                }

                for (int i = 0; i < this.blockLength; ++i) {
                    byte b = i < keyBytes.length ? keyBytes[i] : 0;
                    this.inputPad[i] = (byte) (b ^ 54);
                    this.outPad[i] = (byte) (b ^ 92);
                }

                Arrays.fill(keyBytes, (byte) 0);
                this.engineReset();
            }
        }
    }

    protected void engineUpdate(byte b) {
        if (this.first) {
            this.messageDigest.update(this.inputPad);
            this.first = false;
        }

        this.messageDigest.update(b);
    }

    protected void engineUpdate(byte[] bytes, int off, int len) {
        if (this.first) {
            this.messageDigest.update(this.inputPad);
            this.first = false;
        }

        this.messageDigest.update(bytes, off, len);
    }

    protected void engineUpdate(ByteBuffer buffer) {
        if (this.first) {
            this.messageDigest.update(this.inputPad);
            this.first = false;
        }

        this.messageDigest.update(buffer);
    }

    protected byte[] engineDoFinal() {
        if (this.first) {
            this.messageDigest.update(this.inputPad);
        } else {
            this.first = true;
        }

        try {
            byte[] digest = this.messageDigest.digest();
            this.messageDigest.update(this.outPad);
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
        var1.inputPad = Arrs.copy(this.inputPad);
        var1.outPad = Arrs.copy(this.outPad);
        return var1;
    }
}