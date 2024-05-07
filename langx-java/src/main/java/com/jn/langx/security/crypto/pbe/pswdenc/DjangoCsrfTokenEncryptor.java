package com.jn.langx.security.crypto.pbe.pswdenc;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.id.Nanoids;
import com.jn.langx.util.random.Randoms;

/**
 * <pre>
 * 生成token：
 * csrftoken = salt+encrypt(secret,salt_1); 这个 是 cookie 级别的 token
 * csrfmiddlewaretoken=salt+encrypt(secret,salt_2); 这个是请示 级别的 token
 *
 * 验证token：
 * secret_1 = decrypt(csrftoken)
 * secret_2 = decrypt(csrfmiddlewaretoken)
 * Objs.equals(secret_1, secret_2)
 *
 * </pre>
 */
public class DjangoCsrfTokenEncryptor implements PasswordEncryptor {
    private static final String ALPHABET = Nanoids.DEFAULT_ALPHABET;
    private int secretLength = 32;

    /**
     * @param csrfSecret the csrf token to be encrypted. 它是存在于cookie中的
     * @return 生成 csrfmiddlewaretoken 并返回
     */
    @Override
    public String encrypt(String csrfSecret) {
        Preconditions.checkArgument(secretLength == csrfSecret.length(), "invalid django csrf secret");
        String salt = getNewCsrfString();
        return encrypt(csrfSecret, salt);
    }

    private String encrypt(String csrfSecret, String salt) {
        StringBuilder cipherBuilder = new StringBuilder(secretLength * 2);
        for (int i = 0; i < secretLength; i++) {
            int indexOfSecret = Strings.indexOf(ALPHABET, csrfSecret.charAt(i));
            int indexOfSalt = Strings.indexOf(ALPHABET, salt.charAt(i));
            char c = Arrs.<Character>get(PrimitiveArrays.<Character>wrap(ALPHABET.toCharArray()), (indexOfSecret + indexOfSalt) % ALPHABET.length());
            cipherBuilder.append(c);
        }
        String cipherText = cipherBuilder.toString();
        return salt + cipherText;
    }

    private String decrypt(String cipherText, String salt) {
        StringBuilder builder = new StringBuilder(secretLength * 2);
        for (int i = 0; i < secretLength; i++) {
            int indexOfSecret = Strings.indexOf(ALPHABET, cipherText.charAt(i));
            int indexOfSalt = Strings.indexOf(ALPHABET, salt.charAt(i));
            char c = Arrs.<Character>get(PrimitiveArrays.<Character>wrap(ALPHABET.toCharArray()), indexOfSecret - indexOfSalt);
            builder.append(c);
        }
        return builder.toString();
    }

    /**
     * @param expectedSecret      cookie 中 csrftoken 所使用的 secret
     * @param csrfMiddlewareToken 表单中 csrfMiddlewareToken 的值
     * @return 是否是同一个 secret生成的token
     */
    @Override
    public boolean check(String expectedSecret, String csrfMiddlewareToken) {
        String csrfMiddlewareTokenSecret = decrypt(csrfMiddlewareToken);
        return Objs.equals(expectedSecret, csrfMiddlewareTokenSecret);
    }

    /**
     * @param csrfToken           cookie 中 csrftoken的值
     * @param csrfMiddlewareToken 表单中 csrfMiddlewareToken 的值
     * @return 是否是同一个 secret生成的token
     */
    public static boolean checkToken(String csrfToken, String csrfMiddlewareToken) {
        DjangoCsrfTokenEncryptor encryptor = new DjangoCsrfTokenEncryptor();
        String csrfTokenSecret = encryptor.decrypt(csrfToken);
        return encryptor.check(csrfTokenSecret, csrfMiddlewareToken);
    }

    private String decrypt(String saltedCipherText) {
        String salt = Strings.substring(saltedCipherText, 0, secretLength);
        String cipherText = Strings.substring(saltedCipherText, secretLength);
        String secret = decrypt(cipherText, salt);

        return secret;
    }

    private String getNewCsrfString() {
        return Randoms.randomString(ALPHABET, secretLength);
    }

    private String getNewCsrfSecret() {
        return getNewCsrfString();
    }
}
