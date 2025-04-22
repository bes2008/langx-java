package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.util.struct.Pair;

public interface DerivedKeyFormatter {

    String format(DerivedPBEKey key);

    Pair<byte[], DerivedPBEKey> extract(String rawPassword, String encryptedKey);
}
