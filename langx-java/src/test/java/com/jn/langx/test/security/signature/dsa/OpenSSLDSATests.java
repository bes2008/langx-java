package com.jn.langx.test.security.signature.dsa;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.security.KeyFileIOs;

public class OpenSSLDSATests {

    public void test() {
        Resource privateResource = Resources.loadClassPathResource("/com/jn/langx/test/security/signature/dsa/data/openssl/mydsa_pri_pkcs8.pem");
        byte[] privateKey = KeyFileIOs.readKeyFileAndBase64Decode(privateResource);
        
    }

}
