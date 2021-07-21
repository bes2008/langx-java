package com.jn.langx.security.gm.crypto.sm2;

import com.jn.langx.security.gm.crypto.sm2.impl.SM2Encryption;
import com.jn.langx.security.gm.crypto.skf.SKF_PrivateKey;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @see // cn.gmssl.crypto.SM2DerJce
 */
public class Sm2Cipher extends CipherSpi
{
    private boolean skf;
    private SKF_PrivateKey skfPri;
    private int opmode;
    private Key key;
    private SecureRandom random;

    public Sm2Cipher() {
        this.skf = false;
        this.skfPri = null;
        this.opmode = -1;
        this.key = null;
        this.random = null;
    }

    @Override
    protected void engineSetMode(final String s) throws NoSuchAlgorithmException {
        throw new UnsupportedOperationException("engineSetMode");
    }

    @Override
    protected void engineSetPadding(final String s) throws NoSuchPaddingException {
        throw new UnsupportedOperationException("engineSetPadding");
    }

    @Override
    protected int engineGetBlockSize() {
        throw new UnsupportedOperationException("engineGetBlockSize");
    }

    @Override
    protected int engineGetOutputSize(final int n) {
        throw new UnsupportedOperationException("engineGetOutputSize");
    }

    @Override
    protected byte[] engineGetIV() {
        throw new UnsupportedOperationException("engineGetIV");
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        throw new UnsupportedOperationException("engineGetParameters");
    }

    @Override
    protected int engineGetKeySize(final Key key) throws InvalidKeyException {
        return super.engineGetKeySize(key);
    }

    @Override
    protected void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (Exception ex) {
            throw new InvalidKeyException(ex);
        }
    }

    @Override
    protected void engineInit(final int opmode, final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (opmode == 1 && !(key instanceof ECPublicKey)) {
            throw new InvalidKeyException("sm2 encryption can only public only");
        }
        if (opmode == 2 && !(key instanceof ECPrivateKey)) {
            throw new InvalidKeyException("sm2 decryption can use ec private only");
        }
        this.opmode = opmode;
        this.key = key;
        this.random = random;
    }

    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameters algorithmParameters, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected byte[] engineUpdate(final byte[] array, final int n, final int n2) {
        throw new UnsupportedOperationException("engineUpdate");
    }

    @Override
    protected int engineUpdate(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws ShortBufferException {
        throw new UnsupportedOperationException("engineUpdate");
    }

    @Override
    protected byte[] engineDoFinal(final byte[] array, final int n, final int n2) throws IllegalBlockSizeException, BadPaddingException {
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, n, array2, 0, n2);
        try {
            byte[] array3;
            if (this.opmode == 1) {
                array3 = SM2Encryption.encrypt_der((ECPublicKey)this.key, array2, this.random);
            }
            else {
                if (this.opmode != 2) {
                    throw new RuntimeException("unsupported mode in sm2 : " + this.opmode);
                }
                array3 = SM2Encryption.decrypt_der((ECPrivateKey)this.key, array2);
            }
            return array3;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected int engineDoFinal(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return 0;
    }

    @Override
    protected byte[] engineWrap(final Key key) throws IllegalBlockSizeException, InvalidKeyException {
        final ECPublicKey ecPublicKey = (ECPublicKey)this.key;
        try {
            return SM2Encryption.encrypt_der(ecPublicKey, key.getEncoded(), this.random);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Key engineUnwrap(final byte[] array, final String s, final int n) throws InvalidKeyException, NoSuchAlgorithmException {
        final ECPrivateKey ecPrivateKey = (ECPrivateKey)this.key;
        byte[] decrypt_der;
        try {
            decrypt_der = SM2Encryption.decrypt_der(ecPrivateKey, array);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return new SecretKeySpec(decrypt_der, "TlsEccPremasterSecret");
    }
}