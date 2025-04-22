package com.jn.langx.security.crypto.pbe.pbkdf;

/**
 * converts a password to a byte array according to the scheme in
 * PKCS5 (ascii, no padding)
 *
 * @since 5.5.0
 */
public class PasswordToPkcs5AsciiConverter extends PasswordConverter {
    /**
     * @param password a character array representing the password.
     * @return a byte array representing the password.
     */
    @Override
    public byte[] apply(char[] password) {
        if (password != null) {
            byte[] bytes = new byte[password.length];

            for (int i = 0; i != bytes.length; i++) {
                bytes[i] = (byte) password[i];
            }

            return bytes;
        } else {
            return new byte[0];
        }
    }
}
