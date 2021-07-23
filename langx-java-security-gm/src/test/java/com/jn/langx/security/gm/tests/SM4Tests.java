package com.jn.langx.security.gm.tests;

import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.gm.crypto.SM4s;
import com.jn.langx.security.gm.crypto._sm4;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.Charsets;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.SecretKey;


public class SM4Tests {
    @Test
    public void test() throws Throwable {
        //Securitys.addProvider(new GmJceProvider());

        String string = "abcde_12345";
        final BouncyCastleProvider provider = new BouncyCastleProvider();
        Securitys.addProvider(provider);

        Pipeline.of(provider.keySet()).filter(new Predicate<Object>() {
            @Override
            public boolean test(Object key) {
                return key.toString().contains("SM4");//&& !Strings.contains(key.toString(),"mac", true) && !Strings.contains(key.toString(),"poly", true);
            }
        }).forEach(new Consumer<Object>() {
            @Override
            public void accept(Object key) {
                System.out.println(StringTemplates.formatWithPlaceholder("{} : {}", key, provider.getProperty(key.toString())));
            }
        });

        String encryptedString = _sm4.encryptCbcDataTimes(string, "2", 1);
        String decryptedString = _sm4.decryptCbcDataTimes(encryptedString, "2", 1);
        System.out.println(decryptedString);

        SecretKey sm4key = PKIs.createSecretKey("SM4", (String) null, 128, null);

        byte[] encryptedBytes = SM4s.encrypt(string.getBytes(Charsets.UTF_8),
                sm4key.getEncoded(),
                "SM4/ECB/PKCS5Padding",
                null,null
        );

        byte[] decryptedBytes = SM4s.decrypt(encryptedBytes,
                sm4key.getEncoded(),
                "SM4/ECB/PKCS5Padding",
                null,null
        );
        System.out.println(new String(decryptedBytes));
    }
}
