package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.stack.SimpleStack;
import com.jn.langx.util.collection.stack.Stack;
import com.jn.langx.util.collection.stack.ThreadSafeStack;
import com.jn.langx.util.function.Consumer;
import org.junit.Assert;
import org.junit.Test;

public class StackTests {
    @Test
    public void testSimpleStack() {
        System.out.println("========Simple Stack start========");
        test(new SimpleStack<Integer>());
        System.out.println("========Simple Stack end========");
    }

    @Test
    public void testThreadSafeStack() {
        System.out.println("========ThreadSafe Stack start========");
        test(new ThreadSafeStack<Integer>());
        System.out.println("========ThreadSafe Stack end========");
    }


    private void test(Stack<Integer> stack) {
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

        Collects.forEach(stack, new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                System.out.print("\t" + value);
            }
        });

        System.out.println();
    }
}
