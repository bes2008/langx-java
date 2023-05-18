package com.jn.langx.test.util.collection;

import org.junit.Test;

import java.util.List;

public class ForTests {

    @Test
    public void test() {
        List<String> l = null;
        for (String s : l) {
            System.out.println(s);
        }
    }

}
