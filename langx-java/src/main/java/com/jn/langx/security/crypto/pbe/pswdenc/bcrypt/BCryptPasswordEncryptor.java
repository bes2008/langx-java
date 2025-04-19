package com.jn.langx.security.crypto.pbe.pswdenc.bcrypt;

import com.jn.langx.security.crypto.pbe.pswdenc.PasswordEncryptor;

import java.security.SecureRandom;

/**
 * @since 5.3.9
 */
public class BCryptPasswordEncryptor implements PasswordEncryptor {
    private BlowfishSaltGenerator saltGenerator;

    public BCryptPasswordEncryptor() {
        this(10);
    }

    public BCryptPasswordEncryptor(int log_round) {
        this(log_round, null);
    }

    public BCryptPasswordEncryptor(int log_round, SecureRandom secureRandom) {
        this.saltGenerator = new BlowfishSaltGenerator(log_round, secureRandom);
    }

    @Override
    public String encrypt(String password) {
        String salt = saltGenerator.get(16);
        return BCrypt.hashpw(password, salt);
    }

    @Override
    public boolean check(String plainPassword, String encryptedPassword) {
        return BCrypt.checkpw(plainPassword, encryptedPassword);
    }
}
