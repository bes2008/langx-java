package com.jn.langx.security.cipher;

public interface Cipherer<ORIGIN, ENCRYPTED> {

    ENCRYPTED encrypt(ORIGIN text);

    /**
     * 解密操作。
     *
     * @param encryptedText
     * @return
     * @throws IllegalArgumentException 当参数 encryptedText 的格式不符合时，会抛出该异常
     * @throws com.jn.langx.security.exception.SecurityException 其他情况下抛出该异常
     */
    ORIGIN decrypt(ENCRYPTED encryptedText);

}
