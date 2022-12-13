package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.PrioritySet;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;
import org.junit.Test;

import java.util.Random;

public class PrioritySetTest {

    @Test
    public void test() {
        PrioritySet<Integer> set = new PrioritySet<Integer>();
        int start = 0;
        int end = 100;
        // 添加 0-99 到set
        set.addAll(Collects.asList(Arrs.range(start, end, 1)));

        Random random = GlobalThreadLocalMap.getRandom();
        for (int i = 0; i < 1000000; i++) {
            int count = random.nextInt(4);
            while (count < 1) {
                count = random.nextInt(4);
            }
            int value = end;
            for (int j = 0; value > start && j < count; j++) {
                int value2 = random.nextInt(value + 1);
                while (value2 < start) {
                    value2 = random.nextInt(value + 1);
                }
                value = value2;
            }
            set.increment(value);
        }

        System.out.println(set.showPriority());
    }
}
