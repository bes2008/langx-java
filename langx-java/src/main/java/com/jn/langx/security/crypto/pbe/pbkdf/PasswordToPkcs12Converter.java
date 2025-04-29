package com.jn.langx.security.crypto.pbe.pbkdf;

/**
 * converts a password to a byte array according to the scheme in
 * PKCS12 (unicode, big endian, 2 zero pad bytes at the end).
 *
 * @since 5.5.0
 */
public class PasswordToPkcs12Converter extends PasswordConverter {

    /**
     * @param password a character array representing the password.
     * @return a byte array representing the password.
     */
    @Override
    public byte[] apply(char[] password) {
        if (password != null && password.length > 0) {
            // +1 for extra 2 pad bytes.
            byte[] bytes = new byte[(password.length + 1) * 2];

            for (int i = 0; i != password.length; i++) {
                bytes[i * 2] = (byte) (password[i] >>> 8);
                bytes[i * 2 + 1] = (byte) password[i];
            }

            return bytes;
        } else {
            return new byte[0];
        }
    }
}
