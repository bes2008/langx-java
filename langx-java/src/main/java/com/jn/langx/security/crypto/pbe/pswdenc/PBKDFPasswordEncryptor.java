package com.jn.langx.security.crypto.pbe.pswdenc;

public class PBKDFPasswordEncryptor implements PasswordEncryptor {
    @Override
    public String encrypt(String password) {
        return "";
    }

    @Override
    public boolean check(String plainPassword, String encryptedPassword) {
        return false;
    }
}
