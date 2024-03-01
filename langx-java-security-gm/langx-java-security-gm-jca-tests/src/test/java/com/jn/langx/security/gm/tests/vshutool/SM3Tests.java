package com.jn.langx.security.gm.tests.vshutool;

import com.jn.langx.security.crypto.digest.MessageDigests;
import org.junit.Test;

public class SM3Tests {

    @Test
    public void test(){
        String content ="hello, 对比 langx-java 的 sm3, 与 hutool 的SM3结果是否一致";

        String digest1 = MessageDigests.getDigestHexString("SM3",content);
        System.out.println(digest1);
    }



}
