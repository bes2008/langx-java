package com.jn.langx.security.crypto.digest.internal;

import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.digest.LangxMessageDigestSpi;
import com.jn.langx.security.crypto.digest.internal.impl.*;
import com.jn.langx.util.Strings;

public class Digests {

    public static Digest getDigest(String digestAlgorithm) {
        if (Strings.equals(digestAlgorithm, JCAEStandardName.MD2.getName(), true)) {
            return new _MD2Digest();
        }
        if (Strings.equals(digestAlgorithm, JCAEStandardName.MD4.getName(), true)) {
            return new _MD4Digest();
        }
        if (Strings.equals(digestAlgorithm, JCAEStandardName.MD5.getName(), true)) {
            return new _MD5Digest();
        }
        if (Strings.equals(digestAlgorithm, JCAEStandardName.SHA_1.getName(), true)) {
            return new _SHA1Digest();
        }
        if (Strings.equals(digestAlgorithm, JCAEStandardName.SHA3.getName(), true)) {
            return new _SHA3Digest();
        }
        if (Strings.equals(digestAlgorithm, JCAEStandardName.SHA_224.getName(), true)) {
            return new _SHA224Digest();
        }
        if (Strings.equals(digestAlgorithm, JCAEStandardName.SHA_256.getName(), true)) {
            return new _SHA256Digest();
        }
        if (Strings.equals(digestAlgorithm, JCAEStandardName.SHA_384.getName(), true)) {
            return new _SHA384Digest();
        }
        if (Strings.equals(digestAlgorithm, JCAEStandardName.SHA_512.getName(), true)) {
            return new _SHA512Digest();
        }
        if (Strings.equals(digestAlgorithm, "WHIRLPOOL", true)) {
            return new _WhirlpoolDigest();
        }

        throw new UnsupportedOperationException("unsupported digest algorithm: " + digestAlgorithm);
    }
}
