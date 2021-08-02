package com.jn.langx.security.gm.tests.service;

import com.jn.langx.codec.hex.Hex;
import com.jn.langx.security.gm.GMs;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.security.gm.SM4KeyGenerator;
import com.jn.langx.security.gm.bc.BcGmService;
import com.jn.langx.security.gm.gmssl.GmsslGmService;
import com.jn.langx.text.StringTemplates;
import org.junit.Test;

public class GMsTests {

    private static GMs gms = GMs.getGMs();

    @Test
    public void testSM3() {
        String str = "hello_234";
        byte[] bytes = null;
        GmService gmService = gms.getGmService(BcGmService.NAME);

        bytes = gmService.sm3(str.getBytes());
        showSM3(gmService, null, 1, bytes);

        bytes = gmService.sm3(str.getBytes(), "hello".getBytes(), 1);
        showSM3(gmService, "hello", 1, bytes);

        bytes = gmService.sm3(str.getBytes(), 2);
        showSM3(gmService, null, 2, bytes);

        gmService = gms.getGmService(GmsslGmService.NAME);

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


    @Test
    public void testSM4() {
        String str = "hello_234";

        byte[] sm4Key = new SM4KeyGenerator().genSecretKey();

        GmService gmService = gms.getGmService(BcGmService.NAME);
        byte[] encryptedBytes = gmService.sm4Encrypt(str.getBytes(), "CBC", sm4Key, GmService.SM4_IV_DEFAULT);
        showSM4(gmService, "SM-CBC", sm4Key, null, encryptedBytes);

        GmService gmService2 = gms.getGmService(GmsslGmService.NAME);
        byte[] encryptedBytes2 = gmService2.sm4Encrypt(str.getBytes(), "CBC", sm4Key, GmService.SM4_IV_DEFAULT);
        showSM4(gmService2, "SM-CBC", sm4Key, null, encryptedBytes2);

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
