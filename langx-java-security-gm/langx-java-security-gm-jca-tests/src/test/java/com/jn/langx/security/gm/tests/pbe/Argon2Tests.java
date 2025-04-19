package com.jn.langx.security.gm.tests.pbe;

import com.jn.langx.security.crypto.pbe.pswdenc.argon2.Argon2PasswordEncryptor;
import org.junit.Assert;
import org.junit.Test;

public class Argon2Tests {
    @Test
    public void testPasswordEncrypt() {
        Argon2PasswordEncryptor encryptor = new Argon2PasswordEncryptor();
        String pswd = "test@123";
        String encryptedPswd = encryptor.encrypt(pswd);
        Assert.assertTrue(encryptor.check(pswd, encryptedPswd));
    }

}
