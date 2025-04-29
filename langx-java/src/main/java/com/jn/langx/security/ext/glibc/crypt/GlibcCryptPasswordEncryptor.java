package com.jn.langx.security.ext.glibc.crypt;

import com.jn.langx.codec.StringifyFormat;
import com.jn.langx.codec.Stringifys;
import com.jn.langx.security.crypto.pbe.pswdenc.PasswordEncryptor;
import com.jn.langx.security.crypto.salt.RandomBytesSaltGenerator;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Strings;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

public class GlibcCryptPasswordEncryptor implements PasswordEncryptor {
    private static final int SALT_LENGTH = 8;
    private static final Logger LOGGER = Loggers.getLogger(GlibcCryptPasswordEncryptor.class);
    private final String encodingAlgorithm;

    private final int strength;

    private String secret;

    public GlibcCryptPasswordEncryptor(final String encodingAlgorithm, final int strength, String secret) {
        this.encodingAlgorithm = encodingAlgorithm;
        this.strength = strength;
        this.secret = secret;
    }

    @Override
    public String encrypt(final String password) {
        if (password == null) {
            return null;
        }

        if (Strings.isBlank(this.encodingAlgorithm)) {
            LOGGER.warn("No encoding algorithm is defined. Password cannot be encoded;");
            return null;
        }

        return GlibcCrypt.crypt(password.toString(), generateCryptSalt());
    }

    /**
     * Special note on DES UnixCrypt:
     * In DES UnixCrypt, so first two characters of the encoded password are the salt.
     * <p>
     * When you change your password, the {@code /bin/passwd} program selects a salt based on the time of day.
     * The salt is converted into a two-character string and is stored in the {@code /etc/passwd} file along with the
     * encrypted {@code "password."[10]} In this manner, when you type your password at login time, the same salt is used again.
     * UNIX stores the salt as the first two characters of the encrypted password.
     *
     * @param rawPassword       the raw password as it was provided
     * @param encryptedPassword the encoded password.
     * @return true/false
     */
    @Override
    public boolean check(final String rawPassword, final String encryptedPassword) {
        if (Strings.isBlank(encryptedPassword)) {
            LOGGER.warn("The encoded password provided for matching is null. Returning false");
            return false;
        }
        String providedSalt;
        int lastDollarIndex = encryptedPassword.lastIndexOf('$');
        if (lastDollarIndex == -1) {
            providedSalt = encryptedPassword.substring(0, 2);
            LOGGER.debug("Assuming DES UnixCrypt as no delimiter could be found in the encoded password provided");
        } else {
            providedSalt = encryptedPassword.substring(0, lastDollarIndex);
            LOGGER.debug("Encoded password uses algorithm [{}]", providedSalt.charAt(1));
        }
        String encodedRawPassword = GlibcCrypt.crypt(rawPassword, providedSalt);
        boolean matched = Strings.equals(encodedRawPassword, encryptedPassword);
        String msg = String.format("Provided password does %smatch the encoded password", (matched ? Emptys.EMPTY_STRING : "not "));
        LOGGER.debug(msg);
        return matched;
    }

    private String generateCryptSalt() {
        StringBuilder cryptSalt = new StringBuilder();
        if ("1".equals(this.encodingAlgorithm) || "MD5".equalsIgnoreCase(this.encodingAlgorithm)) {
            cryptSalt.append("$1$");
            LOGGER.debug("Encoding with MD5 algorithm");
        } else if ("5".equals(this.encodingAlgorithm) || "SHA-256".equalsIgnoreCase(this.encodingAlgorithm)) {
            cryptSalt.append("$5$rounds=").append(this.strength).append('$');
            LOGGER.debug("Encoding with SHA-256 algorithm and [{}] rounds", this.strength);
        } else if ("6".equals(this.encodingAlgorithm) || "SHA-512".equalsIgnoreCase(this.encodingAlgorithm)) {
            cryptSalt.append("$6$rounds=").append(this.strength).append('$');
            LOGGER.debug("Encoding with SHA-512 algorithm and [{}] rounds", this.strength);
        } else {
            cryptSalt.append(this.encodingAlgorithm);
            LOGGER.debug("Encoding with DES UnixCrypt algorithm as no indicator for another algorithm was found.");
        }

        if (Strings.isBlank(this.secret)) {
            LOGGER.debug("No secret was found. Generating a salt with length [{}]", SALT_LENGTH);
            this.secret = Stringifys.stringify(new RandomBytesSaltGenerator().get(SALT_LENGTH), StringifyFormat.HEX);
        } else {
            LOGGER.trace("The provided secret is used as a salt");
        }
        cryptSalt.append(this.secret);
        return cryptSalt.toString();
    }
}
