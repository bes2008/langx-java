package com.jn.langx.test.util.regexp;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.MapAccessor;
import com.jn.langx.util.function.Consumer;
import org.junit.Test;

public class NamedPatternTests {
    @Test
    public void test() {
        String[] strings = new String[]{
                "1.dev0",
                "1.0.dev456",
                "1.0a1",
                "1.0a2.dev456",
                "1.0a12.dev456",
                "1.0a12",
                "1.0b1.dev456",
                "1.0b2",
                "1.0b2.post345.dev456",
                "1.0b2.post345",
                "1.0rc1.dev456",
                "1.0rc1",
                "1.0",
                "1.0+abc.5",
                "1.0+abc.7",
                "1.0+5",
                "1.0.post456.dev34",
                "1.0.post456",
                "1.0.15",
                "1.1.dev1"
        };

        Collects.forEach(strings, new Consumer<String>() {
            @Override
            public void accept(String s) {
                MapAccessor mapAccessor = PythonVersionSpecifiers.extractVersionSegments(s);
                System.out.println(mapAccessor.getTarget());
            }
        });
    }

}
