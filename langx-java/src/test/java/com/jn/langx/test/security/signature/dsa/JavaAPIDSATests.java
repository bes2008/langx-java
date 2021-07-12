package com.jn.langx.test.security.signature.dsa;

import com.jn.langx.codec.hex.Hex;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.security.DSAs;
import com.jn.langx.security.keyspec.pem.PemFileIOs;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

import java.io.IOException;
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
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            DSAPublicKey dsaPublicKey = (DSAPublicKey) keyPair.getPublic();
            DSAPrivateKey dsaPrivateKey = (DSAPrivateKey) keyPair.getPrivate();
// 签名：

            printContent("-----BEGIN DSA PRIVATE KEY-----", "-----END DSA PRIVATE KEY-----", dsaPrivateKey.getEncoded());
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(dsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte[] arr = signature.sign();
            System.out.println("jdk dsa sign:" + Hex.encodeHex(arr));
// 验证签名

            printContent("-----BEGIN DSA PUBLIC KEY-----", "-----END DSA PUBLIC KEY-----", dsaPublicKey.getEncoded());
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

    private void printContent(String startLine, String endLine, byte[] bytes) throws IOException {
        PemFileIOs.writeKey(bytes, System.out, null, startLine, endLine);
    }

    @Test
    public void jdkDSA2() {
        try {

            String src = "你好呀";
// 签名：
            Resource privateResource = Resources.loadClassPathResource("/security/dsa/data/javaapi/dsa_private_key_pkcs8.pem");
            byte[] privateKeyBytes = PemFileIOs.readKey(privateResource);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte[] arr = signature.sign();
            System.out.println("jdk dsa sign:" + Hex.encodeHex(arr));
// 验证签名
            Resource publicResource = Resources.loadClassPathResource("/security/dsa/data/javaapi/dsa_public_key.pem");
            byte[] publicKeyBytes = PemFileIOs.readKey(publicResource);

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


    @Test
    public void test() {
        Resource privateResource = Resources.loadClassPathResource("/security/dsa/data/javaapi/dsa_private_key_pkcs8.pem");
        byte[] privateKey = PemFileIOs.readKey(privateResource);
        String content = "你好，JAVAAPI 生成的 DSA";
        byte[] data = content.getBytes(Charsets.UTF_8);
        byte[] signature = DSAs.sign(privateKey, data);
        Resource publicResource = Resources.loadClassPathResource("/security/dsa/data/javaapi/dsa_public_key.pem");
        byte[] publicKey = PemFileIOs.readKey(publicResource);
        if (DSAs.verify(publicKey, data, signature)) {
            System.out.println("验证通过");
        }
    }

}
