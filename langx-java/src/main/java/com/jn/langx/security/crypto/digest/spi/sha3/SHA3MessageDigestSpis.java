package com.jn.langx.security.crypto.digest.spi.sha3;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.Digest;
import com.jn.langx.security.crypto.digest.internal.Xof;
import com.jn.langx.security.crypto.digest.internal.impl._SHA3Digest;

public class SHA3MessageDigestSpis {

    public static class SHA3MessageDigest extends LangxMessageDigest {

        public SHA3MessageDigest(){
            this(256);
        }

        public SHA3MessageDigest(int length){
            this(new _SHA3Digest(length));
        }


        public SHA3MessageDigest(Digest digest) {
            super(digest);
        }

        public SHA3MessageDigest(Xof digest, int outputSize) {
            super(digest, outputSize);
        }
    }


    public static class SHA3_224MessageDigest extends com.jn.langx.security.crypto.digest.spi.sha3.SHA3MessageDigest {
        public SHA3_224MessageDigest(){
            super(224);
        }
    }

    public static class SHA3_256MessageDigest extends com.jn.langx.security.crypto.digest.spi.sha3.SHA3MessageDigest {
        public SHA3_256MessageDigest(){
            super(256);
        }
    }
    public static class SHA3_384MessageDigest extends com.jn.langx.security.crypto.digest.spi.sha3.SHA3MessageDigest {
        public SHA3_384MessageDigest(){
            super(384);
        }
    }

    public static class SHA3_512MessageDigest extends com.jn.langx.security.crypto.digest.spi.sha3.SHA3MessageDigest {
        public SHA3_512MessageDigest(){
            super(512);
        }
    }



}
