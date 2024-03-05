package com.jn.langx.security.gm.tests.vshutool;

import com.jn.langx.security.crypto.cipher.CipherAlgorithmPadding;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.gm.GMs;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.multivalue.CommonMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.util.EnumSet;

public class SM4Tests {

    @Test
    public void test_gm_service(){
        GmService gmService= GMs.getGMs().getDefault();
        SecretKey sm4Key= PKIs.createSecretKey("SM4");
        String content="检测SM4支持了哪些 mode, padding";
        byte[] contentByte=content.getBytes(Charsets.UTF_8);

        byte[] iv = gmService.createSM4IV();

        MultiValueMap<Symmetrics.MODE, CipherAlgorithmPadding> supported= new CommonMultiValueMap<>();

        for (Symmetrics.MODE mode : EnumSet.allOf(Symmetrics.MODE.class)){
            for(CipherAlgorithmPadding padding : EnumSet.allOf(CipherAlgorithmPadding.class)){
                try {
                    byte[] encryptedData = gmService.sm4Encrypt(contentByte, mode, padding, sm4Key.getEncoded(), iv);
                    byte[] decryptedData = gmService.sm4Decrypt(encryptedData, mode, padding, sm4Key.getEncoded(), iv);
                    if(Objs.equals(content, new String(decryptedData, Charsets.UTF_8))){
                        System.out.println(StringTemplates.formatWithPlaceholder("test transformation: SM4/{}/{} success",mode.name(), padding.name()));
                    }
                    else{
                        System.out.println(StringTemplates.formatWithPlaceholder("test transformation: SM4/{}/{} fail",mode.name(), padding.name()));
                    }
                }catch (Exception e){
                    System.out.println(StringTemplates.formatWithPlaceholder("test transformation: SM4/{}/{}, error: {}",mode.name(), padding.name(),e.getMessage()));
                }
            }

            System.out.println("\n\n");
        }


    }

}
