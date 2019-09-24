package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.MapAccessor;
import com.jn.langx.util.collection.PropertiesAccessor;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesAccessorTests {
    @Test
    public void test() {
        Properties props = new Properties();
        props.setProperty("a", "3");
        props.setProperty("b", "false");
        props.setProperty("c", "cccc");
        props.setProperty("ac", "3.0");

        PropertiesAccessor accessor = new PropertiesAccessor(props);
        Assert.assertFalse(accessor.getBoolean("b"));
        Assert.assertTrue(accessor.getInteger("a") == 3);
        Assert.assertTrue(accessor.get("c").equals("cccc"));
        Assert.assertTrue(accessor.getInteger("ac").equals(3));


        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("a", "3");
        hashMap.put("b", "false");
        hashMap.put("c", "cccc");
        hashMap.put("ac", "3.0");
        MapAccessor accessor1 = new MapAccessor();
        Assert.assertFalse(accessor.getBoolean("b"));
        Assert.assertTrue(accessor.getInteger("a") == 3);
        Assert.assertTrue(accessor.get("c").equals("cccc"));

        Assert.assertTrue(accessor.get("ac").equals("3.0"));
        Assert.assertTrue(accessor.getInteger("ac").equals(3));
    }
}
