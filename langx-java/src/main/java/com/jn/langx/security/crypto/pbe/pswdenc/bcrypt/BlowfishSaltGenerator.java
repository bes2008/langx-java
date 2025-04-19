package com.jn.langx.security.crypto.pbe.pswdenc.bcrypt;

import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.salt.StringSaltGenerator;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;

import java.security.SecureRandom;

public class BlowfishSaltGenerator implements StringSaltGenerator {
    private SecureRandom secureRandom;
    private int logRound;

    public BlowfishSaltGenerator(int log_round, SecureRandom secureRandom) {
        this.logRound = Maths.max(1, log_round);
        this.secureRandom = Objs.useValueIfEmpty(secureRandom, Securitys.getSecureRandom());
    }

    @Override
    public String get(Integer bytesLength) {
        String salt = BCrypt.gensalt(this.logRound, this.secureRandom);
        return salt;
    }
}
