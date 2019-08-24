package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Tuple;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TupleTests {
    @Test
    public void test() {
        Tuple t1 = new Tuple(Collects.<Object>asList(Arrs.range(10)));
        Tuple t2 = new Tuple(Collects.<Object>asList(Arrs.range(0, 10, 2)));
        Tuple t3 = new Tuple(Collects.collect(t1, new ArrayList<Object>()));
        Assert.assertFalse(t1.equals(t2));
        Assert.assertTrue(t1.equals(t3));
    }
}
