package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Assert;
import org.junit.Test;

public class ArrsTests {
    @Test
    public void isArrayTests() {
        Assert.assertFalse(Arrs.isArray(null));
        Assert.assertFalse(Arrs.isArray("string"));
        Assert.assertTrue(Arrs.isArray(new int[]{1}));
        Assert.assertTrue(Arrs.isArray(new String[0]));
    }

    @Test
    public void wrapAsArrayTests() {
        Assert.assertArrayEquals(new String[]{"a"}, Arrs.wrapAsArray("a"));
        Assert.assertArrayEquals(new Object[]{"a"}, Arrs.wrapAsArray("a"));
        Object[] a = Arrs.wrapAsArray("a");
        Assert.assertTrue(Reflects.getJvmSignature(Arrs.wrapAsArray("String").getClass()).equals(Reflects.getJvmSignature(String[].class)));
        System.out.println(a.length);
    }

    @Test
    public void createArrayTests() {
        Integer[] a = Arrs.createArray(int.class, 10, new Supplier<Integer, Integer>() {
            @Override
            public Integer get(Integer input) {
                return 0;
            }
        });
        Integer[] b = Arrs.createArray(Integer.class, 10, 0);
        Assert.assertArrayEquals(a, b);
    }

    @Test
    public void rangeTests() {
        Integer[] a = Arrs.range(10);
        Integer[] b = Arrs.range(0, 10, 1);
        Assert.assertArrayEquals(a, b);
    }
}
