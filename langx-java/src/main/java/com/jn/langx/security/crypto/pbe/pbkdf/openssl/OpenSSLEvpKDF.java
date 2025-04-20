package com.jn.langx.security.crypto.pbe.pbkdf.openssl;

import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFEngine;

/**
 * @since 5.3.9
 */
class OpenSSLEvpKDF extends PBKDFEngine {

    public OpenSSLEvpKDF() {
        super(new OpenSSLEvpKeyGeneratorFactory(), false, true);
    }
}
