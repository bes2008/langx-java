package com.jn.langx.test.security;

import com.jn.langx.security.crypto.key.spec.pem.PEMs;
import org.junit.Test;

public class PemTests {
    @Test
    public void test (){
        PEMs.getDefaultPemStyleRegistry();
    }

}
