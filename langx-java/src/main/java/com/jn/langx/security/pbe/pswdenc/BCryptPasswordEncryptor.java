package com.jn.langx.security.pbe.pswdenc;

import com.jn.langx.security.Securitys;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;

import java.security.SecureRandom;

public class BCryptPasswordEncryptor implements PasswordEncryptor{
    private SecureRandom secureRandom;
    private int logRound;

    public BCryptPasswordEncryptor(){
        this(10);
    }
    public BCryptPasswordEncryptor(int log_round){
        this(log_round, null);
    }
    public BCryptPasswordEncryptor(int log_round, SecureRandom secureRandom){
        this.logRound=Maths.max(1, log_round);
        this.secureRandom= Objs.useValueIfEmpty(secureRandom, Securitys.getSecureRandom());
    }
    @Override
    public String encrypt(String password) {
        String salt= BCrypt.gensalt(this.logRound, this.secureRandom);
        return BCrypt.hashpw(password,salt);
    }

    @Override
    public boolean check(String plainPassword, String encryptedPassword) {
        return BCrypt.checkpw(plainPassword,encryptedPassword);
    }
}
