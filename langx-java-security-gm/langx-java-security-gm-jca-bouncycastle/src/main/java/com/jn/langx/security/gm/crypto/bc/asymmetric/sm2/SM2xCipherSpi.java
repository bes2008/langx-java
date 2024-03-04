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
    static public class SM2withSm3 extends SM2xCipherSpi {
        public SM2withSm3() {
            super(new SM3Digest());
        }
    }

    static public class SM2withBlake2b extends SM2xCipherSpi {
        public SM2withBlake2b() {
            super(new Blake2bDigest(512));
        }
    }

    static public class SM2withBlake2s extends SM2xCipherSpi {
        public SM2withBlake2s() {
            super(new Blake2sDigest(256));
        }
    }

    static public class SM2withWhirlpool extends SM2xCipherSpi {
        public SM2withWhirlpool() {
            super(new WhirlpoolDigest());
        }
    }

    static public class SM2withMD5 extends SM2xCipherSpi {
        public SM2withMD5() {
            super(new MD5Digest());
        }
    }

    static public class SM2withRMD extends SM2xCipherSpi {
        public SM2withRMD() {
            super(new RIPEMD160Digest());
        }
    }

    static public class SM2withSha1 extends SM2xCipherSpi {
        public SM2withSha1() {
            super(new SHA1Digest());
        }
    }

    static public class SM2withSha224 extends SM2xCipherSpi {
        public SM2withSha224() {
            super(new SHA224Digest());
        }
    }

    static public class SM2withSha256 extends SM2xCipherSpi {
        public SM2withSha256() {
            super(new SHA256Digest());
        }
    }

    static public class SM2withSha384 extends SM2xCipherSpi {
        public SM2withSha384() {
            super(new SHA384Digest());
        }
    }

    static public class SM2withSha512 extends SM2xCipherSpi {
        public SM2withSha512() {
            super(new SHA512Digest());
        }
    }

}
