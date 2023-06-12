package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import org.junit.Test;

import java.util.List;

public class PartitionBySizeTests {
    @Test
    public void test() {
        List<Integer> values = Collects.asList(Arrs.range(137));
        List<List<Integer>> segments = Collects.partitionBySize(values, 100);
        System.out.println(segments.size());

    }
}
