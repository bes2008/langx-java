package com.jn.langx.security.crypto.pbe.pswdenc;

import com.jn.langx.util.Objs;

/**
 * @since 5.3.9
 */
public abstract class CheckByEncryptEncryptor implements PasswordEncryptor{
    @Override
    public boolean check(String plainPassword, String encryptedPassword) {
        return Objs.equals(encrypt(plainPassword), encryptedPassword);
    }
}
