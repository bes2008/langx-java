package com.jn.langx.test.util.id;

import com.jn.langx.util.id.NanoidGenerator;
import com.jn.langx.util.random.Randoms;
import org.junit.Test;

public class NanoIdGenTests {
    @Test
    public void test(){
        System.out.println(new NanoidGenerator().get());
        System.out.println(new NanoidGenerator().get());
    }

    @Test
    public void test2(){
        System.out.println(Randoms.randomString());
        System.out.println(Randoms.randomString());
        System.out.println(Randoms.randomString());
    }
}
