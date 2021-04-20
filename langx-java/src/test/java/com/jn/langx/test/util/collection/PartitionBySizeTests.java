package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class PartitionBySizeTests {
    public static void main(String[] args) {
        List<Integer> values = Collects.asList(Arrs.range(137));
        List<List<Integer>> segments = Collects.partitionBySize(values, 100);
        System.out.println(segments.size());

    }
}
