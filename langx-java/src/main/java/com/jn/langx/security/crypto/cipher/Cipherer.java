package com.jn.langx.security.crypto.cipher;

import com.jn.langx.security.SecurityException;

public interface Cipherer<ORIGIN, ENCRYPTED> {

    ENCRYPTED encrypt(ORIGIN text);

    /**
     * 解密操作。
     *
     * @param encryptedText
     * @return
     * @throws IllegalArgumentException 当参数 encryptedText 的格式不符合时，会抛出该异常
     * @throws SecurityException 其他情况下抛出该异常
     */
    ORIGIN decrypt(ENCRYPTED encryptedText);

}
