package com.jn.langx.security.gm.crypto.bc.asymmetric.sm2;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.*;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.jcajce.provider.asymmetric.ec.GMCipherSpi;

public class SM2xCipherSpi extends GMCipherSpi {

    public SM2xCipherSpi(Digest digest) {
        this(new SM2Engine(digest, SM2Engine.Mode.C1C3C2));
    }

    public SM2xCipherSpi(SM2Engine engine) {
        super(engine);
    }

    /**
     * Classes that inherit from us
     */
    public static class SM2withSm3 extends SM2xCipherSpi {
        public SM2withSm3() {
            super(new SM3Digest());
        }

        public SM2withSm3(SM2Engine.Mode mode){
            super(new SM2Engine(new SM3Digest(), mode));
        }
    }

    public static class SM2withBlake2b extends SM2xCipherSpi {
        public SM2withBlake2b() {
            super(new Blake2bDigest(512));
        }

        public SM2withBlake2b(SM2Engine.Mode mode){
            super(new SM2Engine(new Blake2bDigest(512), mode));
        }
    }

    public static class SM2withBlake2s extends SM2xCipherSpi {
        public SM2withBlake2s() {
            super(new Blake2sDigest(256));
        }

        public SM2withBlake2s(SM2Engine.Mode mode){
            super(new SM2Engine(new Blake2sDigest(256), mode));
        }
    }

    public static class SM2withWhirlpool extends SM2xCipherSpi {
        public SM2withWhirlpool() {
            super(new WhirlpoolDigest());
        }

        public SM2withWhirlpool(SM2Engine.Mode mode){
            super(new SM2Engine(new WhirlpoolDigest(), mode));
        }
    }

    public static class SM2withMD5 extends SM2xCipherSpi {
        public SM2withMD5() {
            super(new MD5Digest());
        }

        public SM2withMD5(SM2Engine.Mode mode){
            super(new SM2Engine(new MD5Digest(), mode));
        }
    }

    public static class SM2withRMD extends SM2xCipherSpi {
        public SM2withRMD() {
            super(new RIPEMD160Digest());
        }

        public SM2withRMD(SM2Engine.Mode mode){
            super(new SM2Engine(new RIPEMD160Digest(), mode));
        }
    }

    public static class SM2withSha1 extends SM2xCipherSpi {
        public SM2withSha1() {
            super(new SHA1Digest());
        }

        public SM2withSha1(SM2Engine.Mode mode){
            super(new SM2Engine(new SHA1Digest(), mode));
        }
    }

    public static class SM2withSha224 extends SM2xCipherSpi {
        public SM2withSha224() {
            super(new SHA224Digest());
        }

        public SM2withSha224(SM2Engine.Mode mode){
            super(new SM2Engine(new SHA224Digest(), mode));
        }
    }

    public static class SM2withSha256 extends SM2xCipherSpi {
        public SM2withSha256() {
            super(new SHA256Digest());
        }

        public SM2withSha256(SM2Engine.Mode mode){
            super(new SM2Engine(new SHA256Digest(), mode));
        }
    }

    public static class SM2withSha384 extends SM2xCipherSpi {
        public SM2withSha384() {
            super(new SHA384Digest());
        }

        public SM2withSha384(SM2Engine.Mode mode){
            super(new SM2Engine(new SHA384Digest(), mode));
        }
    }

    public static class SM2withSha512 extends SM2xCipherSpi {
        public SM2withSha512() {
            super(new SHA512Digest());
        }

        public SM2withSha512(SM2Engine.Mode mode){
            super(new SM2Engine(new SHA512Digest(), mode));
        }
    }

}
