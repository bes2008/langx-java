package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Bag;
import org.junit.Test;

public class BagTest {
    @Test
    public void test(){

        Bag<String> bag = new Bag<String>();

        bag.add("01");
        bag.add("02");
        bag.add("03");

        System.out.println("size of bag = " + bag.size());
        for (String s : bag) {
            System.out.println(s);
        }

        System.out.println(bag.contains(null));
        System.out.println(bag.contains("01"));
        System.out.println(bag.contains("03"));
        System.out.println(bag.contains("04"));
    }
}
