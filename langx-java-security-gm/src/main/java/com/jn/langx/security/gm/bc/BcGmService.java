package com.jn.langx.security.gm.bc;

import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.gm.GmService;

public class BcGmService implements GmService {
    @Override
    public String getName() {
        return "BC-GmService";
    }

    @Override
    public byte[] sm3(byte[] data) {
        return sm3(data, 1);
    }

    @Override
    public byte[] sm3(byte[] data, int iterations) {
        return sm3(data, null, iterations);
    }

    @Override
    public byte[] sm3(byte[] data, byte[] salt, int iterations) {
        return MessageDigests.digest("SM3", data, salt, iterations);
    }
}
