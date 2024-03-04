package com.jn.langx.security.gm.tests.vshutool;

import cn.hutool.crypto.SmUtil;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.util.Objs;
import org.junit.Test;

public class SM3Tests {

    @Test
    public void test(){
        String content ="hello, 对比 langx-java 的 sm3, 与 hutool 的SM3结果是否一致";

        String digest1 = MessageDigests.getDigestHexString("SM3",content);

        String digest2 = SmUtil.sm3(content);

        System.out.println(digest1);
        System.out.println(digest2);

        System.out.println(Objs.equals(digest1, digest2));
    }



}
