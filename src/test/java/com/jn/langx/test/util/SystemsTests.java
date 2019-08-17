package com.jn.langx.test.util;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Collects;
import com.jn.langx.util.function.Consumer2;
import org.junit.Test;

public class SystemsTests {
    @Test
    public void testSystemProperties() {
        System.out.println("====================System Properties===========");
        Collects.forEach(System.getProperties(), new Consumer2<Object, Object>() {
            @Override
            public void accept(Object key, Object value) {
                System.out.println(StringTemplates.formatWithoutIndex("{} = {}", key, value));
            }
        });

    }

    @Test
    public void testEnvironmentVariables() {
        System.out.println("====================Environment Variables===========");
        Collects.forEach(System.getenv(), new Consumer2<String, String>() {
            @Override
            public void accept(String key, String value) {
                System.out.println(StringTemplates.formatWithoutIndex("{} = {}", key, value));
            }
        });
    }
}
