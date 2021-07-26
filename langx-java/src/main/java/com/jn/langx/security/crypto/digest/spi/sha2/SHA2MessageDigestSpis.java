package com.jn.langx.security.crypto.digest.spi.sha2;

import com.jn.langx.security.crypto.digest.LangxMessageDigest;
import com.jn.langx.security.crypto.digest.internal.impl.*;

public class SHA2MessageDigestSpis {

    public static class SHA224MessageDigest extends LangxMessageDigest {
        public SHA224MessageDigest(){
            super(new _SHA224Digest());
        }
    }

    public static class SHA256MessageDigest extends LangxMessageDigest {
        public SHA256MessageDigest(){
            super(new _SHA256Digest());
        }
    }

    public static class SHA384MessageDigest extends LangxMessageDigest {
        public SHA384MessageDigest(){
            super(new _SHA384Digest());
        }
    }

    public static class SHA512MessageDigest extends LangxMessageDigest {
        public SHA512MessageDigest(){
            super(new _SHA512Digest());
        }
    }
    public static class SHA512TMessageDigest extends LangxMessageDigest {
        public SHA512TMessageDigest(int bitLength) {
            super(new _SHA512tDigest(bitLength));
        }
    }


    public static class SHA512T224MessageDigest extends SHA512TMessageDigest {
        public SHA512T224MessageDigest() {
            super(224);
        }
    }


    public static class SHA512T256MessageDigest extends SHA512TMessageDigest{
        public SHA512T256MessageDigest(){
            super(256);
        }
    }

}
