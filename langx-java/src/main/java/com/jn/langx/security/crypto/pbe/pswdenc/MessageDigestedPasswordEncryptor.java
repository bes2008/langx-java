package com.jn.langx.security.crypto.pbe.pswdenc;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.StringifyFormat;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.util.io.Charsets;

/**
 * @since 5.3.9
 */
public class MessageDigestedPasswordEncryptor extends CheckByEncryptEncryptor {

    private String algorithm;
    private StringifyFormat format;
    @Nullable
    private byte[] salt;

    private int iterations;

    public MessageDigestedPasswordEncryptor(){
        this(JCAEStandardName.SHA_256.getName());
    }

    public MessageDigestedPasswordEncryptor(String messageDigestAlgorithm){
        this(messageDigestAlgorithm, StringifyFormat.BASE64);
    }
    public MessageDigestedPasswordEncryptor(String messageDigestAlgorithm, StringifyFormat format){
        this(messageDigestAlgorithm, format, null);
    }

    public MessageDigestedPasswordEncryptor(String messageDigestAlgorithm, StringifyFormat format, @Nullable byte[] salt){
        this(messageDigestAlgorithm, format, salt,1);
    }

    public MessageDigestedPasswordEncryptor(String messageDigestAlgorithm, StringifyFormat format, @Nullable byte[] salt, int iterations) {
        this.algorithm = messageDigestAlgorithm;
        this.format = format;
        this.salt = salt;
        this.iterations = iterations;
    }

    @Override
    public String encrypt(String password) {
        return MessageDigests.digestToString(algorithm, password.getBytes(Charsets.UTF_8), salt, iterations, format);
    }

}
