package com.jn.langx.test.security.pbe;

import com.jn.langx.security.crypto.pbe.pswdenc.argon2.Argon2PasswordEncryptor;
import org.junit.Test;

public class Argon2PasswordEncryptorTests {
    @Test
    public void test() {
        String password = "hello123";
        Argon2PasswordEncryptor encryptor = new Argon2PasswordEncryptor();
        String encryptedText = encryptor.encrypt(password);
        System.out.println(encryptedText);

        boolean checked = encryptor.check(password, encryptedText);
        System.out.println(checked);
    }
}
