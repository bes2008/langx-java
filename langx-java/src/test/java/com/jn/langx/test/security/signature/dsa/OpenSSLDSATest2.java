package com.jn.langx.test.security.signature.dsa;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class OpenSSLDSATest2 {


    // 公钥
    private static final String PUBLIC_KEY = "DSAPublicKey";

    // 私钥
    private static final String PRIVATE_KEY = "DSAPrivateKey";

    private static PrivateKey privateKey;

    private static PublicKey publicKey;


    private static Map<String, Object> keyMap = new HashMap<String, Object>();


    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() {
        initPrivateKey();
        System.out.println("======================================");
        initPublicKey();
        // 将密钥存储在map中
        keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;

    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    /**
     * 读取私钥
     */
    private static void initPrivateKey() {
        InputStream in = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
        try {
            in = Resources.loadClassPathResource("/security/dsa/data/openssl/dsa_private_key_pkcs8.pem").getInputStream();
            ir = new InputStreamReader(in, Charsets.UTF_8);
            br = new BufferedReader(ir);
            String s = br.readLine();
            StringBuilder privatekey = new StringBuilder();
            s = br.readLine();
            while (s.charAt(0) != '-') {
                privatekey.append(s).append("\r");
                System.out.println(s);
                s = br.readLine();
            }
            byte[] keybyte = Base64.decodeBase64(privatekey.toString());

            KeyFactory kf = KeyFactory.getInstance("DSA");

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keybyte);

            privateKey = kf.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOs.close(br);
            IOs.close(ir);
            IOs.close(in);
        }
    }

    /**
     * 读取公钥
     */
    private static void initPublicKey() {
        InputStream inputStream = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
        try {
            inputStream = Resources.loadClassPathResource("/security/dsa/data/openssl/dsa_public_key.pem").getInputStream();
            ir = new InputStreamReader(inputStream, Charsets.UTF_8);
            br = new BufferedReader(ir);
            String s = br.readLine();
            StringBuilder publickey = new StringBuilder();
            s = br.readLine();
            while (s.charAt(0) != '-') {
                publickey.append(s).append("\r");
                System.out.println(s);
                s = br.readLine();

            }

            byte[] keybyte = Base64.decodeBase64(publickey.toString());

            KeyFactory kf = KeyFactory.getInstance("DSA");

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keybyte);

            publicKey = kf.generatePublic(keySpec);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            IOs.close(br);
            IOs.close(ir);
            IOs.close(inputStream);
        }
    }

    /**
     * 签名
     *
     * @param content 待签名字符串
     */
    public static String sign(String content) {
        try {
            Signature signalg = Signature.getInstance("DSA");
            signalg.initSign(privateKey);
            signalg.update(content.getBytes(Charsets.UTF_8.name()));

            byte[] signature = signalg.sign();

            String sign = Base64.encodeBase64String(signature);
            return sign;
        } catch (Exception e) {
        }
        return "";

    }

    /**
     * 数字校验签名
     *
     * @param signature 签名串
     * @param contecnt  待校验内容
     */
    public static boolean verify(String signature, String contecnt) {
        try {
            Signature verifyalg = Signature.getInstance("DSA");
            verifyalg.initVerify(publicKey);

            verifyalg.update(contecnt.getBytes(Charsets.UTF_8.name()));
            byte[] signbyte = Base64.decodeBase64(signature);
            return verifyalg.verify(signbyte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Test
    public void test() {
        // 初始化密钥
        // 生成密钥对
        Map<String, Object> keyMap;
        try {
            keyMap = initKey();
            // 公钥
            byte[] publicKey = getPublicKey(keyMap);
            // 私钥
            byte[] privateKey = getPrivateKey(keyMap);

            System.out.println("公钥：" + Base64.encodeBase64String(publicKey));
            System.out.println("私钥：" + Base64.encodeBase64String(privateKey));
        } catch (Exception e) {
            System.out.println(e);
        }


        String str = "<test></test>";
        System.out.println("原文:" + str);

        // 甲方进行数据的加密
        String sign = sign(str);
        System.out.println("产生签名：" + sign);
        // 验证签名
        boolean status = verify(sign, str);
        System.out.println("状态：" + status);
    }

}
