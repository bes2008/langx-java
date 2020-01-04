package com.jn.langx.test.pipeline;

import com.jn.langx.pipeline.DebugHandler;
import com.jn.langx.pipeline.Handler;
import com.jn.langx.pipeline.Pipeline;
import com.jn.langx.pipeline.Pipelines;
import com.jn.langx.test.bean.Person;
import com.jn.langx.util.collection.Collects;
import org.junit.Test;

public class PipelineTests {
    @Test
    public void test() {
        Person p = new Person();
        p.setAge(3);
        p.setId("id23232");
        p.setName("hello world");
        p.setDesc("description");

        Pipeline pipeline = Pipelines.newPipeline(Collects.<Handler>asList(new DebugHandler()));
        try {
            pipeline.bindTarget(p);
            pipeline.inbound();
            pipeline.outbound();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            pipeline.clear();
        }

    }
}
