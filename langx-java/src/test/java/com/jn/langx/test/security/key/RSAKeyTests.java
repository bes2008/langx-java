package com.jn.langx.test.security.key;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.PKIs;
import com.jn.langx.security.SecureRandoms;
import com.jn.langx.security.crypto.signer.Signatures;
import org.junit.Test;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class RSAKeyTests {

    private static int bitLength(int[] val, int len) {
        if (len == 0)
            return 0;
        return ((len - 1) << 5) + bitLengthForInt(val[0]);
    }

    static int bitLengthForInt(int n) {
        return 32 - Integer.numberOfLeadingZeros(n);
    }

    @Test
    public void t() throws Throwable {
        byte[] message = Base64.encodeBase64("你好，欢迎欢迎".getBytes());
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        {
            // 使用 KeyPairGenerator 生成 PublicKey, PrivateKey
            KeyPair pair = PKIs.createKeyPair(JCAEStandardName.RSA.getName(), null, 512, SecureRandoms.getSecureRandom(null));
            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();


            Signature signer = Signatures.createSignature(JCAEStandardName.NONE_RSA.getName(), null, privateKey, null);
            signer.update(message);
            byte[] signed = signer.sign();

            Signature verifier = Signatures.createSignature(JCAEStandardName.NONE_RSA.getName(), null, publicKey);
            verifier.update(message);
            boolean verifyResult = verifier.verify(signed);
            System.out.println(verifyResult);
        }
        {

            // 利用之前生成的 PublicKey, PrivateKey 来重建 PublicKey, PrivateKey
            // 可以认为这样模拟的是存储后重构
            RSAPublicKeyImpl publicKey0 = (RSAPublicKeyImpl) publicKey;
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(publicKey0.getModulus(), publicKey0.getPublicExponent());


            RSAPrivateCrtKeyImpl privateKey0 = (RSAPrivateCrtKeyImpl) privateKey;

            BigInteger modulus = privateKey0.getModulus();
            BigInteger publicExponent = privateKey0.getPublicExponent();
            BigInteger privateExponent = privateKey0.getPrivateExponent();
            BigInteger primeP = privateKey0.getPrimeP();
            BigInteger primeQ = privateKey0.getPrimeQ();
            BigInteger primeExponentP = privateKey0.getPrimeExponentP();
            BigInteger primeExponentQ = privateKey0.getPrimeExponentQ();
            BigInteger crtCoefficient = privateKey0.getCrtCoefficient();
            RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec = new RSAPrivateCrtKeySpec(
                    modulus,
                    publicExponent,
                    privateExponent,
                    primeP,
                    primeQ,
                    primeExponentP,
                    primeExponentQ,
                    crtCoefficient
            );

            publicKey = PKIs.createPublicKey(JCAEStandardName.RSA.getName(), null, publicKeySpec);
            privateKey = PKIs.createPrivateKey(JCAEStandardName.RSA.getName(), null, rsaPrivateCrtKeySpec);

            //    privateKey = rsaPrivateKey(m, e);
            Signature signer2 = Signatures.createSignature(JCAEStandardName.NONE_RSA.getName(), null, privateKey, null);
            signer2.update(message);
            byte[] signed2 = signer2.sign();

            Signature verifier2 = Signatures.createSignature(JCAEStandardName.NONE_RSA.getName(), null, publicKey);
            verifier2.update(message);
            boolean verifyResult2 = verifier2.verify(signed2);
            System.out.println(verifyResult2);
        }
    }

    @Test
    public void createKeyPairs() throws Exception {
        // create the keys
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(512, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        PublicKey pubKey = pair.getPublic();
        PrivateKey privKey = pair.getPrivate();
        byte[] pk = pubKey.getEncoded();
        byte[] privk = privKey.getEncoded();
        String strpk = new String(Base64.encodeBase64(pk));
        String strprivk = new String(Base64.encodeBase64(privk));

        System.out.println("公钥:" + Arrays.toString(pk));
        System.out.println("私钥:" + Arrays.toString(privk));
        System.out.println("公钥Base64编码:" + strpk);
        System.out.println("私钥Base64编码:" + strprivk);

        X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(strpk.getBytes()));
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(strprivk.getBytes()));

        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PublicKey pubkey2 = keyf.generatePublic(pubX509);
        PrivateKey privkey2 = keyf.generatePrivate(priPKCS8);

        System.out.println(pubKey.equals(pubkey2));
        System.out.println(privKey.equals(privkey2));
    }
}
