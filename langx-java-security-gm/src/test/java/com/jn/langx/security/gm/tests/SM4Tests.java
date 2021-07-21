package com.jn.langx.security.gm.tests;

import com.jn.langx.security.GmJceProvider;
import com.jn.langx.security.Securitys;
import org.junit.Test;


public class SM4Tests {
    @Test
    public void test(){
        Securitys.addProvider(new GmJceProvider());
    }
}
