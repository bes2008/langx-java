package com.jn.langx.security.gm.tests.vshutool;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.crypto.signature.Signatures;
import com.jn.langx.security.gm.GMs;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.security.gm.SM2Mode;
import com.jn.langx.security.gm.crypto.bc.asymmetric.sm2.SM2SignParameterSpec;
import com.jn.langx.util.Objs;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

import java.security.KeyPair;

public class SM2Tests {

    @Test
    public void test1_encrypt_vs_hutool(){
        // 因为hutool不支持c1c2c3，所以只比较c1c3c2
        GMs.enableGlobalSM2_C1C3C2();

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

        GmService gmService = GMs.getGMs().getDefault();

        String cipherTextByHutool = Base64.encodeBase64String(hutoolSm2.encrypt(contentBytes));
        System.out.println(cipherTextByHutool);

        // 使用hutool解密
        String decryptedTextByHutool= new String( hutoolSm2.decrypt(Base64.decodeBase64(cipherTextByHutool)),Charsets.UTF_8);
        System.out.println(Objs.equals(decryptedTextByHutool, content));

        System.out.println("================test-langx-01===================");
        // 使用 langx-java 加密
        String cipherTextByLangx = Base64.encodeBase64String(gmService.sm2Encrypt(contentBytes, keyPair.getPublic().getEncoded()));
        System.out.println(cipherTextByLangx);
        System.out.println(Objs.equals(cipherTextByHutool, cipherTextByLangx));

        // 使用 langx-java 解密
        String decryptedTextByLangx= new String( gmService.sm2Decrypt(Base64.decodeBase64(cipherTextByLangx), keyPair.getPrivate().getEncoded() ),Charsets.UTF_8);
        System.out.println(decryptedTextByLangx);
        System.out.println(Objs.equals(decryptedTextByLangx, content));


        System.out.println("================test-langx-02===================");

        String cipherTextByLangx2 = Base64.encodeBase64String(gmService.sm2Encrypt(contentBytes, keyPair.getPublic().getEncoded()));
        System.out.println(cipherTextByLangx2);
        System.out.println(Objs.equals(cipherTextByLangx2, cipherTextByLangx));

        // 使用 langx-java 解密
        String decryptedTextByLangx2= new String( gmService.sm2Decrypt(Base64.decodeBase64(cipherTextByLangx2), keyPair.getPrivate().getEncoded() ),Charsets.UTF_8);
        System.out.println(decryptedTextByLangx2);
        System.out.println(Objs.equals(decryptedTextByLangx2, content));

        // 从验证的结果来看：
        // 1) Hutool, langx-java各自加密、并解密是可以成功的。
        // 2）同样的keypair, 同样的内容，加密出来的结果 是不一样的。
        // 3）即便是使用同样的库，同样的 keypair,对同样的内容加密，每一次加密结果也是不一样的，因为在加密过程中使用的随机数每次都是不一样的。

    }


    @Test
    public void test2_fix_encrypt_decrypt(){

        // 使用  langx加密、hutool 来解密

        GMs.enableGlobalSM2_C1C3C2();

        String content ="hello, 对比 langx-java 的 sm2 加密, 与 hutool 的SM2结果是否一致";
        byte[] contentBytes = content.getBytes(Charsets.UTF_8);
        KeyPair keyPair = PKIs.createKeyPair(JCAEStandardName.SM2.getName());

        System.out.println("private key:");
        System.out.println(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));

        System.out.println("public key:");
        System.out.println(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));

        System.out.println("================test-hutool-01===================");

        SM2 hutoolSm2 = SmUtil.sm2(keyPair.getPrivate(),keyPair.getPublic());

        GmService gmService= GMs.getGMs().getDefault();


        System.out.println("================test-01 langx加密、hutool解密===================");
        // 使用 langx-java 加密
        //String cipherTextByLangx = Base64.encodeBase64String(Asymmetrics.encrypt(contentBytes, keyPair.getPublic().getEncoded(), JCAEStandardName.SM2.getName(),null ));
        String cipherTextByLangx = Base64.encodeBase64String(gmService.sm2Encrypt(contentBytes, keyPair.getPublic().getEncoded()));
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
        String decryptedTextByLangx2= new String( gmService.sm2Decrypt(Base64.decodeBase64(cipherTextByHutool), keyPair.getPrivate().getEncoded() ),Charsets.UTF_8);
        System.out.println(decryptedTextByLangx2);
        System.out.println(Objs.equals(decryptedTextByLangx2, content));

        // 从验证的结果来看：
        // 1) Hutool, langx-java各自加密、并解密是可以成功的。
        // 2）hutool 加密后，由langx-java解密可以成功
        // 3）langx-java加密后，由hutool解密是可以成功

    }

    @Test
    public void test3_langx_encrypt_c1c2c3(){
        test3_langx_encrytp_with_mode(SM2Mode.C1C2C3);
    }
    @Test
    public void test3_langx_encrypt_c1c2c3_defaultC1C3C2(){
        GMs.enableGlobalSM2_C1C3C2();
        test3_langx_encrytp_with_mode(SM2Mode.C1C2C3);
    }
    @Test
    public void test3_langx_encrypt_c1c3c2(){
        test3_langx_encrytp_with_mode(SM2Mode.C1C3C2);
    }
    private void test3_langx_encrytp_with_mode(SM2Mode mode){

        if(mode==SM2Mode.C1C3C2){
            GMs.sm2DefaultC1C3C2ModeEnabled();
        }

        String content ="测试模式："+mode.name();
        byte[] contentBytes = content.getBytes(Charsets.UTF_8);
        KeyPair keyPair = PKIs.createKeyPair(JCAEStandardName.SM2.getName());

        System.out.println("private key:");
        System.out.println(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));

        System.out.println("public key:");
        System.out.println(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));

        GmService gmService= GMs.getGMs().getDefault();


        System.out.println("================test-03 加密、解密 using "+mode.name()+"===================");
        // 使用 langx-java 加密
        //String cipherTextByLangx = Base64.encodeBase64String(Asymmetrics.encrypt(contentBytes, keyPair.getPublic().getEncoded(), JCAEStandardName.SM2.getName(),null ));
        String cipherTextByLangx = Base64.encodeBase64String(gmService.sm2Encrypt(contentBytes, keyPair.getPublic().getEncoded(), mode));
        System.out.println(cipherTextByLangx);



        // 使用 langx-java 解密
        String decryptedTextByLangx2= new String( gmService.sm2Decrypt(Base64.decodeBase64(cipherTextByLangx), keyPair.getPrivate().getEncoded() ,mode),Charsets.UTF_8);
        System.out.println(decryptedTextByLangx2);
        System.out.println(Objs.equals(decryptedTextByLangx2, content));


    }

    @Test
    public void test4_langx_sign_01(){
        String content ="使用 java signature api 来测试 SM2的签名";
        byte[] contentBytes=content.getBytes(Charsets.UTF_8);
        KeyPair keyPair = PKIs.createKeyPair("SM2");

        SM2SignParameterSpec parameterSpec = new SM2SignParameterSpec();

        byte[] digitSignature = Signatures.sign(contentBytes, keyPair.getPrivate().getEncoded(), "SM3WithSM2", null, null, parameterSpec);
        boolean verified = Signatures.verify(contentBytes, digitSignature, keyPair.getPublic().getEncoded(), "SM3WithSM2", null, parameterSpec);
        System.out.println("verified: " + verified);
    }

    @Test
    public void test4_langx_sign_02(){
        String content ="使用 gm service 来测试 SM2的签名";
        byte[] contentBytes=content.getBytes(Charsets.UTF_8);
        KeyPair keyPair = PKIs.createKeyPair("SM2");
        GmService gmService = GMs.getGMs().getDefault();

        byte[] digitSignature = gmService.sm2Sign(contentBytes, keyPair.getPrivate().getEncoded());
        boolean verified = gmService.sm2Verify(contentBytes,keyPair.getPublic().getEncoded(), digitSignature );
        System.out.println("verified: " + verified);
    }

    @Test
    public void test4_langx_sign_03(){
        String content ="使用 gm service 来 签名， hutool 来验签";
        byte[] contentBytes=content.getBytes(Charsets.UTF_8);
        KeyPair keyPair = PKIs.createKeyPair("SM2");
        GmService gmService = GMs.getGMs().getDefault();

        byte[] digitSignature = gmService.sm2Sign(contentBytes, keyPair.getPrivate().getEncoded());

        SM2 hutoolSm2 = SmUtil.sm2(keyPair.getPrivate(),keyPair.getPublic());
        boolean verified =hutoolSm2.verify(contentBytes, digitSignature);
        System.out.println("verified: " + verified);

        System.out.println("==============================================");

        content ="使用 hutool 来 签名， gm service 来验签";
        contentBytes=content.getBytes(Charsets.UTF_8);

        digitSignature =hutoolSm2.sign(contentBytes);

        verified =gmService.sm2Verify(contentBytes, keyPair.getPublic().getEncoded(), digitSignature );
        System.out.println("verified: " + verified);
    }
}
