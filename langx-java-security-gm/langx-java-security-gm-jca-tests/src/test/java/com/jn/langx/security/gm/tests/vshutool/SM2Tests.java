package com.jn.langx.security.gm.tests.vshutool;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.Asymmetrics;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.Objs;
import com.jn.langx.util.io.Charsets;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.junit.Test;

import java.security.KeyPair;

public class SM2Tests {

    @Test
    public void encryptTest(){
        String content ="hello, 对比 langx-java 的 sm2 加密, 与 hutool 的SM2结果是否一致";
        byte[] contentBytes = content.getBytes(Charsets.UTF_8);
        KeyPair keyPair = PKIs.createKeyPair(JCAEStandardName.SM2.getName());


        // 使用 hutool 加密
        // 默认使用的是 C1C3C2模式
        SM2 hutoolSm2 = SmUtil.sm2(keyPair.getPrivate(),keyPair.getPublic());

        String cipherTextByHutool = Base64.encodeBase64String(SmUtil.changeC1C3C2ToC1C2C3(hutoolSm2.encrypt(contentBytes), null));
        System.out.println(cipherTextByHutool);

        // 使用hutool解密
        String decryptedTextByHutool= new String( hutoolSm2.decrypt(Base64.decodeBase64(cipherTextByHutool)),Charsets.UTF_8);
        System.out.println(Objs.equals(decryptedTextByHutool, content));


        // 默认使用的是 C1C2C3模式
        String cipherTextByLangx = Base64.encodeBase64String(Asymmetrics.encrypt(contentBytes, keyPair.getPublic().getEncoded(), JCAEStandardName.SM2.getName(),null ));
        System.out.println(cipherTextByLangx);
        System.out.println(Objs.equals(cipherTextByHutool, cipherTextByLangx));
    }


}
