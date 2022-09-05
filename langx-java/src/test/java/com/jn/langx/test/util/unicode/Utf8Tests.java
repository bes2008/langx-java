package com.jn.langx.test.util.unicode;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

public class Utf8Tests {
    @Test
    public void test() {
        String v = "-1=-1";
        byte[] bytes = v.getBytes(Charsets.UTF_8);
        Collects.forEach(bytes, new Consumer<Byte>() {
            @Override
            public void accept(Byte aByte) {
                System.out.print(aByte+" ");
            }
        });
    }

}
