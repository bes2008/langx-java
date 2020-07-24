package com.jn.langx.test.security.key;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.JCAEStandardName;
import com.jn.langx.security.PKIs;
import com.jn.langx.security.SecureRandoms;
import com.jn.langx.security.Signatures;
import org.junit.Test;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

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


}
