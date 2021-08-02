package com.jn.langx.security.gm.gmssl;

import com.jn.langx.security.gm.GmService;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Maths;
import org.gmssl.GmSSL;

public class GmsslGmService implements GmService {
    public static final String NAME = "GmSSL-GmService";
    private GmSSL gmssl = new GmSSL();

    @Override
    public String getName() {
        return NAME;
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
        byte[] bytes = data;
        if (Emptys.isNotEmpty(salt)) {
            bytes = gmssl.digest("SM3", salt);
        }
        iterations = Maths.max(1, iterations);
        for (int i = 0; i < iterations; i++) {
            bytes = gmssl.digest("SM3", bytes);
        }
        return bytes;
    }
}
