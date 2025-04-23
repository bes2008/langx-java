package com.jn.langx.security.crypto.pbe.pbkdf;

public interface DerivedKeyFormatter {

    String format(DerivedPBEKey key);

    DerivedPBEKey extract(String rawPassword, String encryptedKey);
}
