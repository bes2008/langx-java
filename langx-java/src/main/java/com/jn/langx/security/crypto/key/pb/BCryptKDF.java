package com.jn.langx.security.crypto.key.pb;

import com.jn.langx.util.io.Charsets;

import java.security.NoSuchAlgorithmException;

/**
 * BCrypt 是一种 对密码进行Hash的算法，通过算法生成的是 passphrase 的 hash 值，这个hash值只是为也比较 传入的passphrase是否匹配，不能直接用作加密。
 * 所以不能与 PBKDFCipher结合使用.
 *
 * 所以可以将它看作是一种用于替代 md5, sha1 等的一种hash算法即可
 */
public class BCryptKDF implements KDF{
    @Override
    public byte[] genSalt(int saltBitSize, int round) {
        return new byte[0];
    }

    @Override
    public DerivedKey generate(String passphrase, byte[] saltBytes, int keyBitSize, int ivBitSize, int iterations, String hashAlgorithm) throws NoSuchAlgorithmException {
        String hashedPswd = BCrypt.hashpw(passphrase, new String(saltBytes, Charsets.UTF_8));
        byte[] key=hashedPswd.getBytes(Charsets.UTF_8);
        return new DerivedKey(saltBytes, key);
    }
}
