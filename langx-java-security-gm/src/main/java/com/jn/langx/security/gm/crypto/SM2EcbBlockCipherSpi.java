package com.jn.langx.security.gm.crypto;

import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

public class SM2EcbBlockCipherSpi extends BaseBlockCipher {
    public SM2EcbBlockCipherSpi() {
        super(new SM2BouncyCastleBlockCipher());
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        return super.engineGetParameters();
    }

    @Override
    protected int engineGetBlockSize() {
        return super.engineGetBlockSize();
    }

    @Override
    protected byte[] engineGetIV() {
        return super.engineGetIV();
    }

    @Override
    protected int engineGetKeySize(Key key) {
        return super.engineGetKeySize(key);
    }

    @Override
    protected int engineGetOutputSize(int inputLen) {
        return super.engineGetOutputSize(inputLen);
    }

    @Override
    protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
        super.engineSetMode(mode);
    }

    @Override
    protected void engineSetPadding(String padding) throws NoSuchPaddingException {
        super.engineSetPadding(padding);
    }

    @Override
    protected void engineInit(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        super.engineInit(opmode, key, params, random);
    }

    @Override
    protected void engineInit(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        super.engineInit(opmode, key, params, random);
    }

    @Override
    protected void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
        super.engineInit(opmode, key, random);
    }

    @Override
    protected void engineUpdateAAD(byte[] input, int offset, int length) {
        super.engineUpdateAAD(input, offset, length);
    }

    @Override
    protected void engineUpdateAAD(ByteBuffer bytebuffer) {
        super.engineUpdateAAD(bytebuffer);
    }

    @Override
    protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
        return super.engineUpdate(input, inputOffset, inputLen);
    }

    @Override
    protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException {
        return super.engineUpdate(input, inputOffset, inputLen, output, outputOffset);
    }

    @Override
    protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
        return super.engineDoFinal(input, inputOffset, inputLen);
    }

    @Override
    protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        return super.engineDoFinal(input, inputOffset, inputLen, output, outputOffset);
    }
}
