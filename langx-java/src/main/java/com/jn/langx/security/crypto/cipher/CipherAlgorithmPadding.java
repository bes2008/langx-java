package com.jn.langx.security.crypto.cipher;

/**
 * @since 5.3.6
 */
public enum CipherAlgorithmPadding {
    NoPadding,
    ZeroPadding,
    AnsiX923Padding,
    ISO97971Padding,
    ISO10126Padding,
    OAEPPadding,
    PKCS1Padding,
    PKCS5Padding,

    PKCS7Padding,
    SSL3Padding;

}
