package com.jn.langx.security.cipher;

public interface Cipherer<ORIGIN, ENCRYPTED> {

    ENCRYPTED encrypt(ORIGIN text);

    ORIGIN decrypt(ENCRYPTED encryptedText);

}
