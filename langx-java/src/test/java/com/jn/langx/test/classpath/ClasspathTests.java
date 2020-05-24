package com.jn.langx.test.classpath;

import com.jn.langx.classpath.cp.ClassClasspath;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.test.util.StringsTests;
import com.jn.langx.test.util.SystemsTests;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

public class ClasspathTests {

    @Test
    public void testClassClasspath() {
        ClassClasspath classpath = new ClassClasspath(SystemsTests.class);

        Resource resource = classpath.findResource(Reflects.getFQNClassName(StringsTests.class), true);
        resource = classpath.findResource("StringsTests", true);
        System.out.println(resource);
    }

}
