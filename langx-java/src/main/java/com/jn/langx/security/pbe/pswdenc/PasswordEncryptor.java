package com.jn.langx.security.pbe.pswdenc;

public interface PasswordEncryptor {
    /**
     * Encrypts (digests) a password.
     *
     * @param password the password to be encrypted.
     * @return the resulting digest.
     */
    public String encrypt(String password);


    /**
     * Checks an unencrypted (plain) password against an encrypted one
     * (a digest) to see if they match.
     *
     * @param plainPassword the plain password to check.
     * @param encryptedPassword the digest against which to check the password.
     * @return true if passwords match, false if not.
     */
    public boolean check(String plainPassword, String encryptedPassword);
}
