package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.function.Supplier;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NonAbsentMapTests {
    @Test
    public void testNonAbsentHashMap() {
        Map<String, List<String>> map = new NonAbsentHashMap<String, List<String>>(new Supplier<String, List<String>>() {
            @Override
            public List<String> get(String input) {
                return new ArrayList<String>();
            }
        });

        map.get("a").add("a0");
        map.get("b").add("b2");
        Assert.assertSame(1, map.get("a").size());
    }

    @Test
    public void testWrapedNonAbsentMap() {
        Map<String, List<String>> map = new WrappedNonAbsentMap<String, List<String>>(new HashMap<String, List<String>>(), new Supplier<String, List<String>>() {
            @Override
            public List<String> get(String input) {
                return new ArrayList<String>();
            }
        });

        map.get("a").add("a0");
        map.get("b").add("b2");
        Assert.assertSame(1, map.get("a").size());
    }
}
