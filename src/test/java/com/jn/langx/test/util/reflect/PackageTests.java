package com.jn.langx.test.util.reflect;

import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

public class PackageTests {
    @Test
    public void packageNameTests() {
        System.out.println(Reflects.getPackageName(PackageTests.class));
    }
}
