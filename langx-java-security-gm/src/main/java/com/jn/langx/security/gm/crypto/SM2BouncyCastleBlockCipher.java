package com.jn.langx.security.gm.crypto;

import com.jn.langx.util.Throwables;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.SM2Engine;

public class SM2BouncyCastleBlockCipher implements BlockCipher {
    private SM2Engine engine;

    public SM2BouncyCastleBlockCipher() {
        engine = new SM2Engine();
    }

    @Override
    public void init(boolean forEncryption, CipherParameters cipherParameters) throws IllegalArgumentException {
        engine.init(forEncryption, cipherParameters);
    }

    @Override
    public String getAlgorithmName() {
        return "SM2";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
        try {
            byte[] bytes = engine.processBlock(in, inOff, in.length - inOff);
            System.arraycopy(bytes, 0, out, outOff, bytes.length);
            return bytes.length;
        }catch (Throwable ex){
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    @Override
    public void reset() {
    }
}
