package com.jn.langx.test.security.pbe;

import com.jn.langx.security.crypto.pbe.pswdenc.DjangoCsrfTokenEncryptor;
import org.junit.Assert;
import org.junit.Test;

public class DjangoCsrfTokenTests {
    @Test
    public void test(){
       Assert.assertTrue(DjangoCsrfTokenEncryptor.checkToken("EaOKub9Cbshwpsi8OwWFaWCRumqDuh2bU7NrCJqUPc60psgBi9fbuev6s4ZyY2EN",
               "LGilu6JUAYPsB3KZw1KwODQIehJcoH5M1Dh2CE0ceIEWB3Is0E328VJXcZi7SsHo"));
    }
}
