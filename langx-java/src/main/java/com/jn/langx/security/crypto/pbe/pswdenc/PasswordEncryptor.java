package com.jn.langx.security.crypto.pbe.pswdenc;

/**
 * @since 5.3.9
 */
public interface PasswordEncryptor {
    /**
     * Encrypts (digests) a password.
     *
     * @param password the password to be encrypted.
     * @return the resulting digest.
     */
    String encrypt(String password);


    /**
     * Checks an unencrypted (plain) password against an encrypted one
     * (a digest) to see if they match.
     *
     * @param plainPassword the plain password to check.
     * @param encryptedPassword the digest against which to check the password.
     * @return true if passwords match, false if not.
     */
    boolean check(String plainPassword, String encryptedPassword);
}
