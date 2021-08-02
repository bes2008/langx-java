package com.jn.langx.security.gm;

import com.jn.langx.Named;

public interface GmService extends Named {
    String getName();

    byte[] sm3(byte[] data);

    byte[] sm3(byte[] data, int iterations);

    byte[] sm3(byte[] data, byte[] salt, int iterations);


}
