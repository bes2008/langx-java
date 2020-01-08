package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.Pipeline;
import org.junit.Test;

public class PipelineTests {
    @Test
    public void test() {
        System.out.println(Pipeline.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).skip(2).limit(3).asList().toString());
    }
}
