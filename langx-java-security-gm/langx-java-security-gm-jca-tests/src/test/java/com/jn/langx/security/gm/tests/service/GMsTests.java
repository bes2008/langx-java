package com.jn.langx.security.gm.tests.service;

import com.jn.langx.codec.hex.Hex;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.gm.GMs;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.security.gm.crypto.key.SM2KeyGenerator;
import com.jn.langx.security.gm.crypto.key.SM4KeyGenerator;
import com.jn.langx.security.gm.crypto.bc.BcGmService;
import com.jn.langx.security.gm.crypto.gmsslorg.GmsslorgGmService;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import org.junit.Test;

import java.security.KeyPair;

public class GMsTests {

    private static GMs gms = GMs.getGMs();


    // @Test
    @Test
    public void testSM2_cipher() {
        String str = "hello_234";
        KeyPair keyPair = new SM2KeyGenerator().genKeyPair();

        // 测试1：使用 BC SM2 进行 加解密
        GmService gmService = gms.getGmService(BcGmService.NAME);
        byte[] encrypted = gmService.sm2Encrypt(str.getBytes(), keyPair.getPublic().getEncoded());
        byte[] bytes = gmService.sm2Decrypt(encrypted, keyPair.getPrivate().getEncoded());
        System.out.println(new String(bytes));

        // 测试2：使用 GmSSL  sm2encrypt-with-sha1 进行加解密
        GmService gmService2 = gms.getGmService(GmsslorgGmService.NAME);
        byte[] encrypted2 = gmService2.sm2Encrypt(str.getBytes(), keyPair.getPublic().getEncoded());
        byte[] bytes2 = gmService2.sm2Decrypt(encrypted2, keyPair.getPrivate().getEncoded());
        System.out.println(new String(bytes2));


        // 测试3：先执行 sm3 digest ， 在用 bc  sm2 进行加密，最后用 gmssl sm2encrypt-with-sm3 解密
        byte[] digest = MessageDigests.digest("SM3", str.getBytes());
        byte[] encrypted3 = gmService.sm2Encrypt(digest, keyPair.getPublic().getEncoded());
        byte[] bytes3 = gmService2.sm2Decrypt(encrypted3, keyPair.getPrivate().getEncoded());

        System.out.println(new String(bytes3));

        // 测试4：
        byte[] bytes4 = gmService2.sm2Decrypt(encrypted, keyPair.getPrivate().getEncoded());
        System.out.println(new String(bytes4));

    }

    // @Test
    @Test
    public void testSM2_sign() {
        String str = "hello_234";
        KeyPair keyPair = new SM2KeyGenerator().genKeyPair();
        // 测试 1： 使用BC sm3withsm2 签名
        GmService gmService = gms.getGmService(BcGmService.NAME);
        byte[] signature = gmService.sm2Sign(str.getBytes(), keyPair.getPrivate().getEncoded());
        boolean verified = gmService.sm2Verify(str.getBytes(), keyPair.getPublic().getEncoded(), signature);
        System.out.println(Hex.encodeHexString(signature));
        System.out.println(verified);

        // 测试2：使用 GmSSL  sm2sign 进行签名、验证
        GmService gmService2 = gms.getGmService(GmsslorgGmService.NAME);
        byte[] signature2 = gmService2.sm2Sign(str.getBytes(), keyPair.getPrivate().getEncoded());
        boolean verified2 = gmService2.sm2Verify(str.getBytes(), keyPair.getPublic().getEncoded(), signature2);
        System.out.println(Hex.encodeHexString(signature2));
        System.out.println(verified2);

        // 测试3：使用 gmssl api来 验证 bc 的前面
        boolean verified3 = gmService2.sm2Verify(str.getBytes(), keyPair.getPublic().getEncoded(), signature);
        System.out.println(verified3);
    }

    /**
     * 测试后证明，使用BC SM3，GmSSL SM3 结果一致，可以互通
     */
    //@Test
    @Test
    public void testSM3() {
        String str = "hello_234";
        byte[] bytes;
        GmService gmService = gms.getGmService(BcGmService.NAME);

        bytes = gmService.sm3(str.getBytes());
        showSM3(gmService, null, 1, bytes);

        bytes = gmService.sm3(str.getBytes(), "hello".getBytes(), 1);
        showSM3(gmService, "hello", 1, bytes);

        bytes = gmService.sm3(str.getBytes(), 2);
        showSM3(gmService, null, 2, bytes);

        gmService = gms.getGmService(GmsslorgGmService.NAME);

        bytes = gmService.sm3(str.getBytes());
        showSM3(gmService, null, 1, bytes);

        bytes = gmService.sm3(str.getBytes(), "hello".getBytes(), 1);
        showSM3(gmService, "hello", 1, bytes);

        bytes = gmService.sm3(str.getBytes(), 2);
        showSM3(gmService, null, 2, bytes);
    }

    void showSM3(GmService service, String salt, int iterations, byte[] sm3ed) {
        String text = StringTemplates.formatWithPlaceholder("name: {}, salt:{}, iterations:{} \n hash: {}", service.getName(), salt, iterations, Hex.encodeHexString(sm3ed));
        System.out.println(text);
    }

    private String[] texts = new String[]{
            "abc133%4235&34`gerg",
            "hldf2=45343`   et3t4565gdss36656'fw33'6566yu990[--",
            "23aeq0uqwefew;ttqepet3ouqwevekp",
            "你好呀11111"
    };


    @Test
    public void testSM4_ECB() {
        testSM4_for_mode(Symmetrics.MODE.ECB);
    }

    @Test
    public void testSM4_CBC() {
        testSM4_for_mode(Symmetrics.MODE.CBC);
    }

    @Test
    public void testSM4_CFB() {
        testSM4_for_mode(Symmetrics.MODE.CFB);
    }

    @Test
    public void testSM4_OFB() {
        testSM4_for_mode(Symmetrics.MODE.OFB);
    }

    @Test
    public void testSM4_CTR() {
        testSM4_for_mode(Symmetrics.MODE.CTR);
    }

    private void testSM4_for_mode(final Symmetrics.MODE mode){
        Collects.forEach(texts, new Consumer<String>() {
            @Override
            public void accept(String s) {
                testSM4_for_mode(s, mode);
            }
        });
    }

    /**
     * 测试后证明，使用BC SM4，GmSSL SM4 结果一致，可以互通，可以互相解密
     */
    private void testSM4_for_mode(String str, Symmetrics.MODE mode) {
        byte[] sm4Key = new SM4KeyGenerator().genSecretKey();

        GmService gmService = gms.getGmService(BcGmService.NAME);
        byte[] encryptedBytes = gmService.sm4Encrypt(str.getBytes(), mode, sm4Key, GmService.SM4_IV_DEFAULT);
        showSM4(gmService, "SM4/" + mode.name(), sm4Key, null, encryptedBytes);
        byte[] decryptedBytes1 = gmService.sm4Decrypt(encryptedBytes, mode, sm4Key);
        System.out.println("1：" + new String(decryptedBytes1));

        GmService gmService2 = gms.getGmService(GmsslorgGmService.NAME);
        byte[] encryptedBytes2 = gmService2.sm4Encrypt(str.getBytes(), mode, sm4Key, GmService.SM4_IV_DEFAULT);
        byte[] decryptedBytes2 = null;
        if (encryptedBytes2 != null) {
            showSM4(gmService2, "SM4/" + mode.name(), sm4Key, null, encryptedBytes2);
            decryptedBytes2 = gmService2.sm4Decrypt(encryptedBytes2, mode, sm4Key);
            System.out.println("2：" + new String(decryptedBytes2));
        } else {
            System.out.println("2：Error when encrypt use gmssl SM4-" + mode.name());
        }



        // 接下来 使用  gmssl 来解码 BC 编码后的内容：
        byte[] validBytes = encryptedBytes;
        // 使用BC 编码后的内容长度，要比 使用GMSSL 编码后的长度多 16，所以如果要使用 GMSSL 来解码BC编码后的内容时，需要先截掉 最后16位
        if (mode == Symmetrics.MODE.CFB || mode == Symmetrics.MODE.OFB || mode == Symmetrics.MODE.CTR) {
            // BC 加密后会进行补位，最终保证长度是 16的整数倍 （所有模式都是这样）
            // gmssl 加密后的长度与加密之前的长度保持一致     （限于 CFB,OFB,CTR）
            // gmssl 加密后会进行补位，最终保证长度是 16的整数倍 （限于 CBC）
            // ECB 模式 gmssl 有问题，尚未测试出
            // 该处理只针对 CFB,OFB,CTR
            if(encryptedBytes.length%16==0) {
                validBytes = new byte[encryptedBytes.length - 16]; // 这里是有bug的
                System.arraycopy(encryptedBytes, 0, validBytes, 0, validBytes.length);
            }
        }

        // 使用 gmssl sm4 去 解密 bc 的加密内容
        byte[] decryptedBytes3 = gmService2.sm4Decrypt(validBytes, mode, sm4Key, GmService.SM4_IV_DEFAULT);
        if (decryptedBytes3 != null) {
            System.out.println("3：" + new String(decryptedBytes3));
        } else {
            System.out.println("3：Error when decrypt bc's cipher text use gmssl SM4-" + mode.name());
        }

        System.out.println("\n\n=====================================================\n\n");
    }

    public void showSM4(GmService service, String transformation, byte[] key, byte[] iv, byte[] encrypted) {
        String text = StringTemplates.formatWithPlaceholder("name: {}, transformation:{}, key:{}, iv: {}\n out: {}",
                service.getName(),
                transformation,
                Hex.encodeHexString(key),
                iv == null ? "" : Hex.encodeHexString(iv),
                Hex.encodeHexString(encrypted));
        System.out.println(text);
    }
}
