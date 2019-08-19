package com.jn.langx.test.util;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Collects;
import com.jn.langx.util.function.Consumer2;
import org.junit.Test;

import java.util.Map;

public class SystemsTests {
    @Test
    public void testSystemProperties() {
        System.out.println("====================System Properties===========");
        Map<String, String> map = Collects.propertiesToStringMap(System.getProperties());
        Collects.forEach(map, new Consumer2<String, String>() {
            @Override
            public void accept(String key, String value) {
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
