package com.jn.langx.security.gm.tests;

import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.crypto.key.supplier.bytesbased.ByteBasedSecretKeySupplier;
import com.jn.langx.security.gm.GmJceProvider;
import com.jn.langx.security.gm.crypto.symmetric.sm4.SM4AlgorithmSpecSupplier;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.Provider;
import java.security.SecureRandom;


public class SM4Tests {
    @Test
    public void test() throws Throwable {
        //Securitys.addProvider(new GmJceProvider());

        String string = "abcde_12345";

        new GmJceProvider();
        // final BouncyCastleProvider provider = new BouncyCastleProvider();
        //Securitys.addProvider(provider);

        Pipeline.of(GmJceProvider.bouncyCastleProvider.keySet()).filter(new Predicate<Object>() {
            @Override
            public boolean test(Object key) {
                return key.toString().contains("SM2");//&& !Strings.contains(key.toString(),"mac", true) && !Strings.contains(key.toString(),"poly", true);
            }
        }).forEach(new Consumer<Object>() {
            @Override
            public void accept(Object key) {
                System.out.println(StringTemplates.formatWithPlaceholder("{} : {}", key, GmJceProvider.bouncyCastleProvider.getProperty(key.toString())));
            }
        });


        SecretKey sm4key = PKIs.createSecretKey("SM4", (String) null, 128, null);

        byte[] encryptedBytes = Symmetrics.encrypt(string.getBytes(Charsets.UTF_8),
                sm4key.getEncoded(),
                "SM4",
                "SM4/ECB/PKCS5Padding",
                (Provider) null,
                (SecureRandom) null,
                new ByteBasedSecretKeySupplier(),
                new SM4AlgorithmSpecSupplier()
        );

        byte[] decryptedBytes = Symmetrics.decrypt(encryptedBytes,
                sm4key.getEncoded(),
                "SM4",
                "SM4/ECB/PKCS5Padding",
                null, null,
                new ByteBasedSecretKeySupplier(),
                new SM4AlgorithmSpecSupplier()
        );
        System.out.println(new String(decryptedBytes));
    }
}
