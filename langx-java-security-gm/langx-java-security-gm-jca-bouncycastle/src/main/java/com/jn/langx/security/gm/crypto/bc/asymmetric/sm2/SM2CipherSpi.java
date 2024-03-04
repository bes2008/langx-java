package com.jn.langx.security.gm.crypto.bc.asymmetric.sm2;

import com.jn.langx.util.Throwables;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseCipherSpi;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

public class SM2CipherSpi extends BaseCipherSpi {
    private SM2Engine engine;

    public SM2CipherSpi() {
        super();
        this.engine = new SM2Engine();
    }

    @Override
    protected int engineGetBlockSize() {
        throw new UnsupportedOperationException("engineGetBlockSize");
    }

    @Override
    protected byte[] engineGetIV() {
        throw new UnsupportedOperationException("engineGetIV");
    }

    @Override
    protected int engineGetKeySize(Key key) {
        return super.engineGetKeySize(key);
    }

    @Override
    protected int engineGetOutputSize(int inputLen) {
        throw new UnsupportedOperationException("engineGetOutputSize");
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        throw new UnsupportedOperationException("engineGetParameters");
    }

    @Override
    protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
        throw new UnsupportedOperationException("engineSetMode");
    }

    @Override
    protected void engineSetPadding(String padding) throws NoSuchPaddingException {
        throw new UnsupportedOperationException("engineSetPadding");
    }

    @Override
    protected int engineUpdate(ByteBuffer byteBuffer, ByteBuffer byteBuffer1) throws ShortBufferException {
        throw new UnsupportedOperationException();
    }

    protected void engineUpdateAAD(byte[] bytes, int i, int i1) {
        throw new UnsupportedOperationException();
    }

    protected void engineUpdateAAD(ByteBuffer byteBuffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void engineInit(int opmode, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(opmode, key, (AlgorithmParameterSpec) null, secureRandom);
        } catch (Exception ex) {
            throw new InvalidKeyException(ex);
        }
    }

    @Override
    protected void engineInit(int opmode, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (opmode == 1 && !(key instanceof ECPublicKey)) {
            throw new InvalidKeyException("SM2 encryption can only public only");
        }
        if (opmode == 2 && !(key instanceof ECPrivateKey)) {
            throw new InvalidKeyException("SM2 decryption can use ec private only");
        }
        if (opmode == 1) {
            this.engine.init(true, new ParametersWithRandom( ECUtil.generatePublicKeyParameter((ECPublicKey) key),secureRandom));
        } else {
            this.engine.init(false, ECUtil.generatePrivateKeyParameter((ECPrivateKey) key));
        }

    }

    @Override
    protected void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected byte[] engineUpdate(byte[] bytes, int i, int i1) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected int engineUpdate(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected byte[] engineDoFinal(byte[] bytes, int offset, int length) throws IllegalBlockSizeException, BadPaddingException {
        try {
            byte[] out = engine.processBlock(bytes, offset, length);
            return out;
        } catch (Exception ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    @Override
    protected int engineDoFinal(byte[] in, int inOffset, int length, byte[] out, int outOffset) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        byte[] actualOut = this.engineDoFinal(in, inOffset, length);
        System.arraycopy(actualOut, 0, out, outOffset, actualOut.length);
        return actualOut.length;
    }

    @Override
    protected int engineDoFinal(ByteBuffer byteBuffer, ByteBuffer byteBuffer1) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return super.engineDoFinal(byteBuffer, byteBuffer1);
    }

    @Override
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        return super.engineWrap(key);
    }

    @Override
    protected Key engineUnwrap(byte[] wrappedKey, String wrappedKeyAlgorithm, int wrappedKeyType) throws InvalidKeyException {
        return super.engineUnwrap(wrappedKey, wrappedKeyAlgorithm, wrappedKeyType);
    }
}
