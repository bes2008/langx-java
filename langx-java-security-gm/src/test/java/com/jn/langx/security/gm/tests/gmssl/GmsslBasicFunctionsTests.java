package com.jn.langx.security.gm.tests.gmssl;

import com.jn.langx.util.Strings;
import org.gmssl.GmSSL;
import org.junit.Test;

public class GmsslBasicFunctionsTests {
    private GmSSL gmssl = new GmSSL();

    @Test
    public void showDigests() {
        System.out.println("digests: ");
        System.out.println("\t" + Strings.join(",", gmssl.getDigests()));
    }

    @Test
    public void showMacs(){
        System.out.println("macs: ");
        System.out.println("\t" + Strings.join(",", gmssl.getMacs()));
    }

    @Test
    public void showCiphers(){
        System.out.println("ciphers: ");
        System.out.println("\t" + Strings.join(",", gmssl.getCiphers()));
    }

    @Test
    public void showVersion(){
        System.out.println("versions: ");
        System.out.println("\t" + Strings.join(",", gmssl.getVersions()));
    }

    @Test
    public void showSignAlgorithms(){
        System.out.println("sign-algorithms: ");
        System.out.println("\t" + Strings.join(",", gmssl.getSignAlgorithms()));
    }

    @Test
    public void showPublicKeyEncryptions(){
        System.out.println("public-key cipher: ");
        System.out.println("\t" + Strings.join(",", gmssl.getPublicKeyEncryptions()));
    }

}
