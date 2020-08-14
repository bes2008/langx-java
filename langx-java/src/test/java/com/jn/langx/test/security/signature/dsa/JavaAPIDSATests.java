package com.jn.langx.test.security.signature.dsa;

import com.jn.langx.codec.Hex;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.security.KeyFileIOs;
import com.jn.langx.security.PKIs;
import org.junit.Test;

import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class JavaAPIDSATests {
    @Test
    public void jdkDSA() {
        try {

            String src = "你好呀";
// 初始化：
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            DSAPublicKey dsaPublicKey = (DSAPublicKey) keyPair.getPublic();
            DSAPrivateKey dsaPrivateKey = (DSAPrivateKey) keyPair.getPrivate();
// 签名：
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(dsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte arr[] = signature.sign();
            System.out.println("jdk dsa sign:" + Hex.encodeHex(arr));
// 验证签名
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(dsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("DSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            signature = Signature.getInstance("SHA1withDSA");
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean bool = signature.verify(arr);
            System.out.println("jdk dsa verify:" + bool);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void jdkDSA2() {
        try {

            String src = "你好呀";
// 签名：
            Resource privateResource = Resources.loadClassPathResource("/security/dsa/data/openssl/dsa_private_key_pkcs8.pem");
            byte[] privateKeyBytes = KeyFileIOs.readKeyFileAndBase64Decode(privateResource);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte arr[] = signature.sign();
            System.out.println("jdk dsa sign:" + Hex.encodeHex(arr));
// 验证签名
            Resource publicResource = Resources.loadClassPathResource("/security/dsa/data/openssl/dsa_public_key.pem");
            byte[] publicKeyBytes = KeyFileIOs.readKeyFileAndBase64Decode(publicResource);

            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            keyFactory = KeyFactory.getInstance("DSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            // PublicKey publicKey =  PKIs.createPublicKey("DSA", null, new X509EncodedKeySpec(publicKeyBytes));
            signature = Signature.getInstance("SHA1withDSA");
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean bool = signature.verify(arr);
            System.out.println("jdk dsa verify:" + bool);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
