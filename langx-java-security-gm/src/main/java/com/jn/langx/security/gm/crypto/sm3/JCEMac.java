package com.jn.langx.security.gm.crypto.sm3;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.digests.*;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.engines.RC2Engine;
import org.bouncycastle.crypto.macs.*;
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;

import javax.crypto.MacSpi;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

public class JCEMac extends MacSpi implements PBE
{
    private Mac macEngine;
    private int pbeType;
    private int pbeHash;
    private int keySize;

    protected JCEMac(final Mac macEngine) {
        this.pbeType = 2;
        this.pbeHash = 1;
        this.keySize = 160;
        this.macEngine = macEngine;
    }

    protected JCEMac(final Mac macEngine, final int pbeType, final int pbeHash, final int keySize) {
        this.pbeType = 2;
        this.pbeHash = 1;
        this.keySize = 160;
        this.macEngine = macEngine;
        this.pbeType = pbeType;
        this.pbeHash = pbeHash;
        this.keySize = keySize;
    }

    @Override
    protected void engineInit(final Key key, final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (key == null) {
            throw new InvalidKeyException("key is null");
        }
        CipherParameters cipherParameters;
        if (key instanceof BCPBEKey) {
            final BCPBEKey bcpbeKey = (BCPBEKey)key;
            if (bcpbeKey.getParam() != null) {
                cipherParameters = bcpbeKey.getParam();
            }
            else {
                if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                    throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                }
                cipherParameters = Util.makePBEMacParameters(bcpbeKey, algorithmParameterSpec);
            }
        }
        else if (algorithmParameterSpec instanceof IvParameterSpec) {
            cipherParameters = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec)algorithmParameterSpec).getIV());
        }
        else {
            if (algorithmParameterSpec != null) {
                throw new InvalidAlgorithmParameterException("unknown parameter type.");
            }
            cipherParameters = new KeyParameter(key.getEncoded());
        }
        this.macEngine.init(cipherParameters);
    }

    @Override
    protected int engineGetMacLength() {
        return this.macEngine.getMacSize();
    }

    @Override
    protected void engineReset() {
        this.macEngine.reset();
    }

    @Override
    protected void engineUpdate(final byte b) {
        this.macEngine.update(b);
    }

    @Override
    protected void engineUpdate(final byte[] array, final int n, final int n2) {
        this.macEngine.update(array, n, n2);
    }

    @Override
    protected byte[] engineDoFinal() {
        final byte[] array = new byte[this.engineGetMacLength()];
        this.macEngine.doFinal(array, 0);
        return array;
    }

    public static class DES extends JCEMac
    {
        public DES() {
            super(new CBCBlockCipherMac(new DESEngine()));
        }
    }

    public static class DES64 extends JCEMac
    {
        public DES64() {
            super(new CBCBlockCipherMac(new DESEngine(), 64));
        }
    }

    public static class DES9797Alg3 extends JCEMac
    {
        public DES9797Alg3() {
            super(new ISO9797Alg3Mac(new DESEngine()));
        }
    }

    public static class DES9797Alg3with7816d4 extends JCEMac
    {
        public DES9797Alg3with7816d4() {
            super(new ISO9797Alg3Mac(new DESEngine(), new ISO7816d4Padding()));
        }
    }

    public static class DESCFB8 extends JCEMac
    {
        public DESCFB8() {
            super(new CFBBlockCipherMac(new DESEngine()));
        }
    }

    public static class MD2 extends JCEMac
    {
        public MD2() {
            super(new HMac(new MD2Digest()));
        }
    }

    public static class MD4 extends JCEMac
    {
        public MD4() {
            super(new HMac(new MD4Digest()));
        }
    }

    public static class MD5 extends JCEMac
    {
        public MD5() {
            super(new HMac(new MD5Digest()));
        }
    }

    public static class OldSHA384 extends JCEMac
    {
        public OldSHA384() {
            super(new OldHMac(new SHA384Digest()));
        }
    }

    public static class OldSHA512 extends JCEMac
    {
        public OldSHA512() {
            super(new OldHMac(new SHA512Digest()));
        }
    }

    public static class PBEWithRIPEMD160 extends JCEMac
    {
        public PBEWithRIPEMD160() {
            super(new HMac(new RIPEMD160Digest()), 2, 2, 160);
        }
    }

    public static class PBEWithSHA extends JCEMac
    {
        public PBEWithSHA() {
            super(new HMac(new SHA1Digest()), 2, 1, 160);
        }
    }

    public static class PBEWithTiger extends JCEMac
    {
        public PBEWithTiger() {
            super(new HMac(new TigerDigest()), 2, 3, 192);
        }
    }

    public static class RC2 extends JCEMac
    {
        public RC2() {
            super(new CBCBlockCipherMac(new RC2Engine()));
        }
    }

    public static class RIPEMD128 extends JCEMac
    {
        public RIPEMD128() {
            super(new HMac(new RIPEMD128Digest()));
        }
    }

    public static class RIPEMD160 extends JCEMac
    {
        public RIPEMD160() {
            super(new HMac(new RIPEMD160Digest()));
        }
    }

    public static class SHA1 extends JCEMac
    {
        public SHA1() {
            super(new HMac(new SHA1Digest()));
        }
    }

    public static class SHA224 extends JCEMac
    {
        public SHA224() {
            super(new HMac(new SHA224Digest()));
        }
    }

    public static class SHA256 extends JCEMac
    {
        public SHA256() {
            super(new HMac(new SHA256Digest()));
        }
    }

    public static class SHA384 extends JCEMac
    {
        public SHA384() {
            super(new HMac(new SHA384Digest()));
        }
    }

    public static class SHA512 extends JCEMac
    {
        public SHA512() {
            super(new HMac(new SHA512Digest()));
        }
    }

    public static class Tiger extends JCEMac
    {
        public Tiger() {
            super(new HMac(new TigerDigest()));
        }
    }
}