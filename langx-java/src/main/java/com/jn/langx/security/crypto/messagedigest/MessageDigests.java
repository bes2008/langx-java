package com.jn.langx.security.crypto.messagedigest;

import com.jn.langx.codec.hex.Hex;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.io.Charsets;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MessageDigests {

    public static String md5(InputStream inputStream) {
        return getDigestHexString(JCAEStandardName.MD5.getName(), inputStream);
    }

    public static String md5(String source) {
        return getDigestHexString(JCAEStandardName.MD5.getName(), source);
    }

    public static String md5(byte[] source) {
        return getDigestHexString(JCAEStandardName.MD5.getName(), source);
    }

    public static String md5(byte[] source, int offset, int length) {
        return getDigestHexString(JCAEStandardName.MD5.getName(), source, offset, length);
    }

    public static String getDigestHexString(String algorithm, InputStream inputStream) {
        return Hex.encodeHexString(digest(algorithm, inputStream));
    }

    public static String getDigestHexString(String algorithm, String source) {
        return Hex.encodeHexString(digest(algorithm, source));
    }

    public static String getDigestHexString(String algorithm, byte[] source) {
        return Hex.encodeHexString(digest(algorithm, source));
    }

    public static String getDigestHexString(String algorithm, byte[] source, int offset, int length) {
        return Hex.encodeHexString(digest(algorithm, source, offset, length));
    }

    public static byte[] digest(String algorithm, InputStream inputStream) {
        MessageDigest messageDigest = MessageDigests.newDigest(algorithm);
        BufferedInputStream bi = new BufferedInputStream(inputStream);
        byte[] bytes = new byte[8192];
        int length = 0;
        try {
            while ((length = bi.read(bytes, 0, 8192)) != -1) {
                messageDigest.update(bytes, 0, length);
            }
            return messageDigest.digest();
        } catch (IOException ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static byte[] digest(String algorithm, String source) {
        return digest(algorithm, source.getBytes(Charsets.UTF_8));
    }

    public static byte[] digest(String algorithm, byte[] source) {
        return digest(algorithm, source, null, 1);
    }


    public static byte[] digest(String algorithm, byte[] source, byte[] salt) {
        return digest(algorithm, source, salt, 1);
    }

    public static byte[] digest(String algorithm, byte[] source, int iterations) {
        return digest(algorithm, source, null, 1);
    }

    public static byte[] digest(String algorithm, byte[] source, byte[] salt, int iterations) {
        return new MessageDigestHasher(algorithm, salt, iterations).hash(source);
    }

    public static byte[] digest(String algorithm, byte[] source, int offset, int length) {
        MessageDigest messageDigest = newDigest(algorithm);
        Preconditions.checkNotNull(messageDigest);
        messageDigest.update(source, offset, length);
        return messageDigest.digest();
    }

    public static MessageDigest newDigest(String algorithm) {
        try {
            if (algorithm == null) {
                algorithm = "MD5";
            }
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean isKnownMessageDigitAlgorithm(String algorithm) {
        List<JCAEStandardName> names = JCAEStandardName.findAlgorithms(MessageDigest.class);
        return Pipeline.of(names)
                .map(new Function<JCAEStandardName, String>() {
                    @Override
                    public String apply(JCAEStandardName n) {
                        return n.getName().toUpperCase();
                    }
                })
                .contains(algorithm.toUpperCase());
    }

}
