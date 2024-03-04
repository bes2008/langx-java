package com.jn.langx.security.gm.tests.vshutool;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.Asymmetrics;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.Objs;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

import java.security.KeyPair;

public class SM2Tests {

    @Test
    public void encryptTest(){
        encryptTest(true);
    }
    public void encryptTest(boolean c1c3c2Enabled){
        if(c1c3c2Enabled){
            SystemPropertys.setProperty("langx.security.gm.SM2.defaultMode.c1c3c2.enabled","true");
        }
        String content ="hello, 对比 langx-java 的 sm2 加密, 与 hutool 的SM2结果是否一致";
        byte[] contentBytes = content.getBytes(Charsets.UTF_8);
        KeyPair keyPair = PKIs.createKeyPair(JCAEStandardName.SM2.getName());

        System.out.println("private key:");
        System.out.println(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));

        System.out.println("public key:");
        System.out.println(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));

        System.out.println("================test-hutool-01===================");
        // 使用 hutool 加密
        // 默认使用的是 C1C3C2模式
        SM2 hutoolSm2 = SmUtil.sm2(keyPair.getPrivate(),keyPair.getPublic());

        String cipherTextByHutool = Base64.encodeBase64String(hutoolSm2.encrypt(contentBytes));
        System.out.println(cipherTextByHutool);

        // 使用hutool解密
        String decryptedTextByHutool= new String( hutoolSm2.decrypt(Base64.decodeBase64(cipherTextByHutool)),Charsets.UTF_8);
        System.out.println(Objs.equals(decryptedTextByHutool, content));

        System.out.println("================test-langx-01===================");
        // 使用 langx-java 加密
        String cipherTextByLangx = Base64.encodeBase64String(Asymmetrics.encrypt(contentBytes, keyPair.getPublic().getEncoded(), JCAEStandardName.SM2.getName(),null ));
        System.out.println(cipherTextByLangx);
        System.out.println(Objs.equals(cipherTextByHutool, cipherTextByLangx));

        // 使用 langx-java 解密
        String decryptedTextByLangx= new String( Asymmetrics.decrypt(Base64.decodeBase64(cipherTextByLangx), keyPair.getPrivate().getEncoded(), JCAEStandardName.SM2.getName(), null ),Charsets.UTF_8);
        System.out.println(decryptedTextByLangx);
        System.out.println(Objs.equals(decryptedTextByLangx, content));


        System.out.println("================test-langx-02===================");

        String cipherTextByLangx2 = Base64.encodeBase64String(Asymmetrics.encrypt(contentBytes, keyPair.getPublic().getEncoded(), JCAEStandardName.SM2.getName(),null ));
        System.out.println(cipherTextByLangx2);
        System.out.println(Objs.equals(cipherTextByLangx2, cipherTextByLangx));

        // 使用 langx-java 解密
        String decryptedTextByLangx2= new String( Asymmetrics.decrypt(Base64.decodeBase64(cipherTextByLangx2), keyPair.getPrivate().getEncoded(), JCAEStandardName.SM2.getName(), null ),Charsets.UTF_8);
        System.out.println(decryptedTextByLangx2);
        System.out.println(Objs.equals(decryptedTextByLangx2, content));

        // 从验证的结果来看：
        // 1) Hutool, langx-java各自加密、并解密是可以成功的。
        // 2）同样的keypair, 同样的内容，加密出来的结果 是不一样的。
        // 3）即便是使用同样的库，同样的 keypair,对同样的内容加密，每一次加密结果也是不一样的，因为在加密过程中使用的随机数每次都是不一样的。

    }


    @Test
    public void test2(){

        // 使用  langx加密、hutool 来解密

        boolean c1c3c2Enabled = true;
        if(c1c3c2Enabled){
            SystemPropertys.setProperty("langx.security.gm.SM2.defaultMode.c1c3c2.enabled","true");
        }
        String content ="hello, 对比 langx-java 的 sm2 加密, 与 hutool 的SM2结果是否一致";
        byte[] contentBytes = content.getBytes(Charsets.UTF_8);
        KeyPair keyPair = PKIs.createKeyPair(JCAEStandardName.SM2.getName());

        System.out.println("private key:");
        System.out.println(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));

        System.out.println("public key:");
        System.out.println(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));

        System.out.println("================test-hutool-01===================");

        SM2 hutoolSm2 = SmUtil.sm2(keyPair.getPrivate(),keyPair.getPublic());


        System.out.println("================test-01 langx加密、hutool解密===================");
        // 使用 langx-java 加密
        //String cipherTextByLangx = Base64.encodeBase64String(Asymmetrics.encrypt(contentBytes, keyPair.getPublic().getEncoded(), JCAEStandardName.SM2.getName(),null ));
        String cipherTextByLangx = Base64.encodeBase64String(Asymmetrics.encrypt(contentBytes, keyPair.getPublic().getEncoded(), JCAEStandardName.SM2.getName(),null ));
        System.out.println(cipherTextByLangx);
        // 使用hutool解密
        String decryptedTextByHutool= new String( hutoolSm2.decrypt(Base64.decodeBase64(cipherTextByLangx)),Charsets.UTF_8);
        System.out.println(decryptedTextByHutool);
        System.out.println(Objs.equals(decryptedTextByHutool, content));


        System.out.println("================test-02 hutool 加密、langx解密===================");

        hutoolSm2 = SmUtil.sm2(keyPair.getPrivate(),keyPair.getPublic());

        String cipherTextByHutool = Base64.encodeBase64String(hutoolSm2.encrypt(contentBytes));
        System.out.println(cipherTextByHutool);

        // 使用 langx-java 解密
        String decryptedTextByLangx2= new String( Asymmetrics.decrypt(Base64.decodeBase64(cipherTextByHutool), keyPair.getPrivate().getEncoded(), JCAEStandardName.SM2.getName(), null ),Charsets.UTF_8);
        System.out.println(decryptedTextByLangx2);
        System.out.println(Objs.equals(decryptedTextByLangx2, content));

        // 从验证的结果来看：
        // 1) Hutool, langx-java各自加密、并解密是可以成功的。
        // 2）hutool 加密后，由langx-java解密可以成功
        // 3）langx-java加密后，由hutool解密是可以成功

    }

}
