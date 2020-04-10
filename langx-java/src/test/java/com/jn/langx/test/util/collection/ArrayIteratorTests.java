package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.iter.ArrayIterator;
import org.junit.Test;

import java.util.Iterator;

public class ArrayIteratorTests {
    @Test
    public void test(){
        Object[] array = new Object[]{"aaa", 'b', 3, true};
        Iterator iterator = new ArrayIterator(array);
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

        iterator = new ArrayIterator(array,true);
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
