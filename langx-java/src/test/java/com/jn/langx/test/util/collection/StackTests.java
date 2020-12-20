package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.stack.SimpleStack;
import org.junit.Assert;
import org.junit.Test;

public class StackTests {
    @Test
    public void test0() {
        SimpleStack<Integer> stack = new SimpleStack<Integer>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        Assert.assertEquals(5, stack.size());

        int v = stack.peek();
        Assert.assertEquals(5, stack.size());
        Assert.assertEquals(5, v);

        v = stack.pop();
        Assert.assertEquals(4, stack.size());
        Assert.assertEquals(5, v);
    }
}
