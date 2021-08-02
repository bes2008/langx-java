package com.jn.langx.security.gm.tests.service;

import com.jn.langx.codec.hex.Hex;
import com.jn.langx.security.gm.GMs;
import com.jn.langx.security.gm.GmService;
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
}
