package com.jn.langx.test.security.signature.dsa;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.security.crypto.DSAs;
import com.jn.langx.security.crypto.keyspec.pem.PemFileIOs;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

public class OpenSSLDSATests {

    @Test
    public void test() {
        Resource privateResource = Resources.loadClassPathResource("/security/dsa/data/openssl/dsa_private_key_pkcs8.pem");
        byte[] privateKey = PemFileIOs.readKey(privateResource);
        String content = "你好，OpenSSL 生成的 DSA";
        byte[] data = content.getBytes(Charsets.UTF_8);
        byte[] signature = DSAs.sign(privateKey, data);
        Resource publicResource = Resources.loadClassPathResource("/security/dsa/data/openssl/dsa_public_key.pem");
        byte[] publicKey = PemFileIOs.readKey(publicResource);
        if(DSAs.verify(publicKey, data, signature)){
            System.out.println("验证通过");
        }
    }

}
