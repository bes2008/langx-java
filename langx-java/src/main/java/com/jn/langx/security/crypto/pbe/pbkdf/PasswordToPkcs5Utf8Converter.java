package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.util.Chars;

/**
 * converts a password to a byte array according to the scheme in
 * PKCS5 (UTF-8, no padding)
 */
public class PasswordToPkcs5Utf8Converter extends PasswordConverter {
    /**
     * @param password a character array representing the password.
     * @return a byte array representing the password.
     */
    @Override
    public byte[] apply(char[] password) {
        if (password != null) {
            return Chars.toUtf8Bytes(password);
        } else {
            return new byte[0];
        }
    }
}
