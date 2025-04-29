package com.jn.langx.security.gm.tests.pbe;

import com.jn.langx.security.crypto.pbe.pswdenc.scrypt.ScryptPasswordEncryptor;
import org.junit.Assert;
import org.junit.Test;

public class SCryptTests {
    public void testPBE() {

    }

    @Test
    public void testPasswordEncrypt() {
        ScryptPasswordEncryptor encryptor = new ScryptPasswordEncryptor();
        String pswd = "test@123";
        String encryptedPswd = encryptor.encrypt(pswd);
        Assert.assertTrue(encryptor.check(pswd, encryptedPswd));
    }
}
