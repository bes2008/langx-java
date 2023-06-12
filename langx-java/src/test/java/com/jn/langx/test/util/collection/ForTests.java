package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Collects;
import org.junit.Test;

import java.util.List;

public class ForTests {
    @Test
    public void test() {
        List<String> l = Collects.emptyArrayList();
        for (String s : l) {
            System.out.println(s);
        }
    }

}
