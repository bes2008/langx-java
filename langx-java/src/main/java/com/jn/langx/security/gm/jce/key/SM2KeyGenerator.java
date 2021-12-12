package com.jn.langx.security.gm.jce.key;

import com.jn.langx.security.crypto.key.PKIs;

import java.security.KeyPair;

public class SM2KeyGenerator {
    public KeyPair genKeyPair() {
        return PKIs.createKeyPair("EC", null, 256, null);
    }
}
