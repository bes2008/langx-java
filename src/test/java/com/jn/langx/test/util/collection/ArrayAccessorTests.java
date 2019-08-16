package com.jn.langx.test.util.collection;

import com.jn.langx.util.reflect.ArrayAccessor;
import org.junit.Assert;
import org.junit.Test;

public class ArrayAccessorTests {
    @Test
    public void test() {
        Object[] array = new Object[]{"aaa", 'b', 3, true};
        ArrayAccessor<Object[]> accessor = new ArrayAccessor<Object[]>(array);
        Assert.assertEquals(accessor.getString(0), "aaa");
        Assert.assertTrue(accessor.getInteger(2) == 3);
        Assert.assertTrue(accessor.getBoolean(3));
        Assert.assertTrue(accessor.getCharacter(1) == 'b');
    }
}
