package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

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

    @Test
    public void copyTests() {
        int[] array = PrimitiveArrays.createIntArray(10, new Supplier<Integer, Integer>() {
            @Override
            public Integer get(Integer input) {
                return input;
            }
        });
        int[] arr2 = PrimitiveArrays.copy(array);

        String[] strings = Arrs.createArray(String.class, 10, new Supplier<Integer, String>() {
            @Override
            public String get(Integer index) {
                return "str-" + index;
            }
        });

        String[] strings2 = Arrs.copy(strings);
        String[] emptyArr2 = Arrs.copy(new String[0]);
        String[] emptyArr = Arrs.copy(null);
        Object[] objects = new Object[]{
                "ssss",
                0x22,
                32,
                'c',
                true,
                new Date(),
                3.3F,
                23.2D
        };

        Object[] objects2= Arrs.copy(objects);

        Object[] objects3= Arrs.copy(array);

        System.out.println(array);
        System.out.println(arr2);
        System.out.println(strings);
        System.out.println(strings2);
    }


}
