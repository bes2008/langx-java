package com.jn.langx.test.security;

import com.jn.langx.security.crypto.JCAEStandardName;
import org.junit.Test;

import static com.jn.langx.security.crypto.JCAEStandardName.MD5_RSA;

public class JCAEngineInstanceNameTest {
    @Test
    public void test1(){
        System.out.println(JCAEStandardName.X509.getName());
        System.out.println(JCAEStandardName.JKS.getName());
        System.out.println(MD5_RSA.getName());
    }
}