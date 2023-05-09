package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.iter.ArrayIterator;
import com.jn.langx.util.collection.iter.ReverseListIterator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("ALL")
public class ArrayIteratorTests {
    @Test
    public void test(){
        Object[] array = new Object[]{"aaa", 'b', 3, true};
        Iterator iterator = new ArrayIterator(array);
        System.out.println("============test-001===========");
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

        iterator = new ArrayIterator(array,true);
        System.out.println("============test-002===========");
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }


        List list = Arrays.asList(array);
        iterator = new ReverseListIterator(list);

        System.out.println("============test-003===========");
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
