package com.jn.langx.security.gm.tests;

import com.jn.langx.codec.hex.Hex;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Charsets;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

public class HmacTests {

    @Test
    public void test() throws Throwable {
        Securitys.setup();

        String str = "hello-123: hmac";

        String[] algorithms = new String[]{
                "hmacmd2", "hmacmd4", "hmacmd5",
                "hmacsm3",
                "hmacsha1",
                "hmacsha224","hmacsha256","hmacsha384","hmacsha512"
        };
        Collects.forEach(algorithms, new Consumer<String>() {
            @Override
            public void accept(String algorithm) {
                try {
                    doTest(algorithm);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    private void doTest(String algorithm) throws Throwable {
        String str = "hello-123: hmac";

        KeyGenerator keyGenerator = PKIs.getKeyGenerator(algorithm, null);

        System.out.println(StringTemplates.formatWithPlaceholder("key generator: {}", keyGenerator.getClass()));
        SecretKey secretKey = keyGenerator.generateKey();
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKey);
        byte[] bytes = mac.doFinal(str.getBytes(Charsets.UTF_8));
        String hex1 = Hex.encodeHexString(bytes);
        System.out.println(StringTemplates.formatWithPlaceholder("{}: {}", algorithm, hex1));

        Mac mac2 = Mac.getInstance(algorithm);
        SecretKey key2 = PKIs.createSecretKey(algorithm, secretKey.getEncoded());
        mac2.init(key2);
        byte[] bytes2 = mac.doFinal(str.getBytes(Charsets.UTF_8));
        String hex2 = Hex.encodeHexString(bytes2);
        System.out.println(StringTemplates.formatWithPlaceholder("{}: {}", algorithm, hex2));

        Assert.assertEquals(hex1, hex2);
        System.out.println("===================");
    }

}
