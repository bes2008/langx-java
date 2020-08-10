package com.jn.langx.security;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Throwables;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAs {
    private static final String algorithmTransformation = "RSA/ECB/PKCS1Padding";

    public static byte[] encrypt(byte[] bytes, byte[] pubkey) {
        return encrypt(bytes, pubkey, algorithmTransformation, null, null);
    }

    public static byte[] encrypt(byte[] bytes, byte[] pubkey, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        Preconditions.checkNotEmpty(pubkey, "RSA public key is empty");
        try {
            PublicKey publicKey = PKIs.createPublicKey("RSA", provider == null ? null : provider.getName(), new X509EncodedKeySpec(pubkey));
            Cipher cipher = Ciphers.createCipher(algorithmTransformation, provider, Cipher.ENCRYPT_MODE, publicKey, secureRandom);
            return Ciphers.encrypt(cipher, bytes);
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static byte[] decrypt(byte[] bytes, byte[] priKey) {
        return decrypt(bytes, priKey, algorithmTransformation, null, null);
    }

    public static byte[] decrypt(byte[] bytes, byte[] priKey, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        Preconditions.checkNotEmpty(priKey, "RSA private key is empty");
        try {
            PrivateKey privateKey = PKIs.createPrivateKey("RSA", provider == null ? null : provider.getName(), new PKCS8EncodedKeySpec(priKey));
            Cipher cipher = Ciphers.createCipher(algorithmTransformation, provider, Cipher.DECRYPT_MODE, privateKey, secureRandom);
            return Ciphers.decrypt(cipher, bytes);
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }
}
