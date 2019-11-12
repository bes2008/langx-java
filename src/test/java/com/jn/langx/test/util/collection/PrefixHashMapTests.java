package com.jn.langx.test.util.collection;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.PrefixHashMap;
import com.jn.langx.util.function.Consumer;
import org.junit.Test;

import java.util.Map;

public class PrefixHashMapTests {
    @Test
    public void test() {
        PrefixHashMap<String> map = new PrefixHashMap<String>("xyz.");

        map.put("a", "a1");
        map.put("b", "b1");
        map.put("c", "c1");
        map.put("d", "d1");
        map.put("e", "e1");
        map.put("f", "f1");
        map.put("g", "g1");
        showMap(map);

        map = new PrefixHashMap<String>("xyz.", false, map);
        showMap(map);
    }

    private void showMap(PrefixHashMap<String> map) {
        Collects.forEach(map.entrySet(), new Consumer<Map.Entry<String, String>>() {
            @Override
            public void accept(Map.Entry<String, String> entry) {
                System.out.println(StringTemplates.formatWithPlaceholder("{}:{}", entry.getKey(), entry.getValue()));
            }
        });
    }
}
