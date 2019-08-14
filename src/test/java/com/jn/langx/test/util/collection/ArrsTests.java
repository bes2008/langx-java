package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Arrs;
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
        System.out.println(a.length);
    }
}
