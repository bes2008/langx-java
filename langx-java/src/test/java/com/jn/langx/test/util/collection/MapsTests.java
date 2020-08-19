package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Supplier;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapsTests {
    @Test
    public void test(){
        Map<String, String> map = new HashMap<String, String>();

        Maps.putIfAbsent(map, "1", "1");
        Maps.putIfAbsent(map, "1","2");
        Maps.putIfAbsent(map, "2", new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input;
            }
        });

        Maps.putIfAbsent(map, "3", new Supplier<String, String>() {
            @Override
            public String get(String input) {
                return input;
            }
        });

        System.out.println(map.toString());
        System.out.println(map.size());

    }
}
