package com.jn.langx.security.crypto.digest.spi.sha3;

import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;
import com.jn.langx.security.crypto.digest.internal.Digest;
import com.jn.langx.security.crypto.digest.internal.Xof;
import com.jn.langx.security.crypto.digest.internal.impl._SHA3Digest;
import com.jn.langx.security.crypto.key.spi.BaseKeyGeneratorSpi;
import com.jn.langx.security.crypto.mac.HmacCoreSpi;

public class SHA3MessageDigestSpis {

    public static class SHA3MessageDigest extends LangxMessageDigestSpi {

        public SHA3MessageDigest() {
            this(256);
        }

        public SHA3MessageDigest(int length) {
            this(new _SHA3Digest(length));
        }


        public SHA3MessageDigest(Digest digest) {
            super(digest);
        }

        public SHA3MessageDigest(Xof digest, int outputSize) {
            super(digest, outputSize);
        }
    }


    public static class SHA3_224MessageDigestSpi extends SHA3MessageDigest {
        public SHA3_224MessageDigestSpi() {
            super(224);
        }
    }

    public static class SHA3_256MessageDigestSpi extends SHA3MessageDigest {
        public SHA3_256MessageDigestSpi() {
            super(256);
        }
    }

    public static class SHA3_384MessageDigestSpi extends SHA3MessageDigest {
        public SHA3_384MessageDigestSpi() {
            super(384);
        }
    }

    public static class SHA3_512MessageDigestSpi extends SHA3MessageDigest {
        public SHA3_512MessageDigestSpi() {
            super(512);
        }
    }

    public static class HMacSHA3_224Spi extends HmacCoreSpi {
        public HMacSHA3_224Spi() {
            super("SHA3-224");
        }
    }

    public static class HMacSHA3_256Spi extends HmacCoreSpi {
        public HMacSHA3_256Spi() {
            super("SHA3-256");
        }
    }

    public static class HMacSHA3_384Spi extends HmacCoreSpi {
        public HMacSHA3_384Spi() {
            super("SHA3-384");
        }
    }

    public static class HMacSHA3_512Spi extends HmacCoreSpi {
        public HMacSHA3_512Spi() {
            super("SHA3-512");
        }
    }


    public static class HMacSHA3KeyGeneratorSpi extends BaseKeyGeneratorSpi {
        public HMacSHA3KeyGeneratorSpi(int size) {
            super("HMACSHA3-" + size, size);
        }
    }

    public static class HMacSHA3_224KeyGeneratorSpi extends HMacSHA3KeyGeneratorSpi {
        public HMacSHA3_224KeyGeneratorSpi() {
            super(224);
        }
    }

    public static class HMacSHA3_256KeyGeneratorSpi extends HMacSHA3KeyGeneratorSpi {
        public HMacSHA3_256KeyGeneratorSpi(int size) {
            super(256);
        }
    }

    public static class HMacSHA3_384KeyGeneratorSpi extends HMacSHA3KeyGeneratorSpi {
        public HMacSHA3_384KeyGeneratorSpi(int size) {
            super(384);
        }
    }

    public static class HMacSHA3_512KeyGeneratorSpi extends HMacSHA3KeyGeneratorSpi {
        public HMacSHA3_512KeyGeneratorSpi(int size) {
            super(512);
        }
    }


}
