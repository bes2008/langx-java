package com.jn.langx.test.security.pbe;

import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.CipherAlgorithmPadding;
import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.pbe.PBEs;
import com.jn.langx.security.ext.js.cryptojs.CryptoJS;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Lists;
import org.junit.Test;

import javax.crypto.spec.IvParameterSpec;
import java.util.List;

public class CryptoJsMockTests {
    @Test
    public void mockOPENSSL_EVP_AES() {
        String message = "test@123";
        String passphrase = "NsFoCus$#";
        String encryptedText = CryptoJS.AES.encrypt(message, passphrase, null);
        System.out.println(encryptedText);

        String decryptedText = CryptoJS.AES.decrypt(encryptedText, passphrase, null);
        System.out.println(decryptedText);
        System.out.println(Objs.equals(message, decryptedText));

        System.out.println("==========接下来的测试是用 node.js 使用 crypto-js 生成的加密数据 ==========");
        List<String> encryptedTexts = Lists.newArrayList(
                "U2FsdGVkX18vE/xKFEjExOkwa5LGi23wj5ZXXR5zNJs=",
                "U2FsdGVkX1+EkiwP7zttFvziuAOM0MDoPYvKcIxvz3w="
        );
        for (String cipherText : encryptedTexts) {
            decryptedText = CryptoJS.AES.decrypt(cipherText, passphrase, null);
            System.out.println(decryptedText);
            System.out.println(Objs.equals(message, decryptedText));
        }

        System.out.println("==========接下来的测试是用 linux openssl 生成的加密数据 ==========");
        encryptedTexts = Lists.newArrayList(
                "U2FsdGVkX19aeyNUsfL0+mpdW0cdWrU/lOwZa2jOf60=",
                "U2FsdGVkX1/F8ZZaOsRHo9buLUkyHrHxI5MrjaPB9X4="

        );
        for (String encryptedText2 : encryptedTexts) {
            decryptedText = CryptoJS.AES.decrypt(encryptedText2, passphrase, null);
            System.out.println(decryptedText);
            System.out.println(Objs.equals(message, decryptedText));
        }


    }

    @Test
    public void mock_PBKDF2WithSha256_AES() {
        String message = "test@123";
        String passphrase = "NsFoCus$#";

        IvParameterSpec ivParameterSpec = Ciphers.createIvParameterSpec(128);
        CryptoJS.AESConfig cfg = new CryptoJS.AESConfig(256, 128, 1, Symmetrics.MODE.CBC, CipherAlgorithmPadding.PKCS5Padding, JCAEStandardName.SHA_256.getName(), PBEs.PBKDF2WithHmacSHA256, ivParameterSpec.getIV());

        String encryptedText = CryptoJS.AES.encrypt(message, passphrase, cfg);
        System.out.println(encryptedText);


        String decryptedText = CryptoJS.AES.decrypt(encryptedText, passphrase, cfg);
        System.out.println(decryptedText);
        System.out.println(Objs.equals(message, decryptedText));
    }

    @Test
    public void mock_PBKDF2WithSha224_AES() {
        String message = "test@123";
        String passphrase = "NsFoCus$#";

        IvParameterSpec ivParameterSpec = Ciphers.createIvParameterSpec(128);
        CryptoJS.AESConfig cfg = new CryptoJS.AESConfig(256, 128, 1, Symmetrics.MODE.CBC, CipherAlgorithmPadding.PKCS5Padding, JCAEStandardName.SHA_224.getName(), PBEs.PBKDF2WithHmacSHA224, ivParameterSpec.getIV());

        String encryptedText = CryptoJS.AES.encrypt(message, passphrase, cfg);
        System.out.println(encryptedText);

        String decryptedText = CryptoJS.AES.decrypt(encryptedText, passphrase, cfg);
        System.out.println(decryptedText);
        System.out.println(Objs.equals(message, decryptedText));

    }

    @Test
    public void mock_PBKDF2WithSha384_AES() {
        String message = "test@123";
        String passphrase = "NsFoCus$#";

        IvParameterSpec ivParameterSpec = Ciphers.createIvParameterSpec(128);
        CryptoJS.AESConfig cfg = new CryptoJS.AESConfig(256, 128, 1, Symmetrics.MODE.CBC, CipherAlgorithmPadding.PKCS5Padding, JCAEStandardName.SHA_384.getName(), PBEs.PBKDF2WithHmacSHA384, ivParameterSpec.getIV());

        String encryptedText = CryptoJS.AES.encrypt(message, passphrase, cfg);
        System.out.println(encryptedText);

        String decryptedText = CryptoJS.AES.decrypt(encryptedText, passphrase, cfg);
        System.out.println(decryptedText);
        System.out.println(Objs.equals(message, decryptedText));

    }

    @Test
    public void mock_PBKDF2WithSha512_AES() {
        String message = "test@123";
        String passphrase = "NsFoCus$#";

        IvParameterSpec ivParameterSpec = Ciphers.createIvParameterSpec(128);
        CryptoJS.AESConfig cfg = new CryptoJS.AESConfig(256, 128, 1, Symmetrics.MODE.CBC, CipherAlgorithmPadding.PKCS5Padding, JCAEStandardName.SHA_512.getName(), PBEs.PBKDF2WithHmacSHA512, ivParameterSpec.getIV());

        String encryptedText = CryptoJS.AES.encrypt(message, passphrase, cfg);
        System.out.println(encryptedText);

        String decryptedText = CryptoJS.AES.decrypt(encryptedText, passphrase, cfg);
        System.out.println(decryptedText);
        System.out.println(Objs.equals(message, decryptedText));

    }
}
