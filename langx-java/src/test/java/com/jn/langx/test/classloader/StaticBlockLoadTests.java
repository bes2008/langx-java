package com.jn.langx.test.classloader;

import org.junit.Test;

public class StaticBlockLoadTests {
    @Test
    public void test(){
        System.out.println(A.INSTANCE.toString());
    }
}
