package com.jn.langx.test.security.key;

import com.jn.langx.security.ext.cryptojs.CryptoJS;
import com.jn.langx.util.Objs;
import org.junit.Test;

public class CryptoJsMockTests {
    @Test
    public void mockAES(){
        String message="test@123";
        String passphrase="NsFoCus$#";
        String encryptedText=CryptoJS.AES.encrypt(message,passphrase,null);
        System.out.println(encryptedText);


        String decryptedText = CryptoJS.AES.decrypt(encryptedText, passphrase,null);
        System.out.println(decryptedText);
        System.out.println(Objs.equals(message,decryptedText));

        encryptedText="U2FsdGVkX18vE/xKFEjExOkwa5LGi23wj5ZXXR5zNJs=";
        decryptedText = CryptoJS.AES.decrypt(encryptedText, passphrase,null);
        System.out.println(decryptedText);
        System.out.println(Objs.equals(message,decryptedText));
    }
}
