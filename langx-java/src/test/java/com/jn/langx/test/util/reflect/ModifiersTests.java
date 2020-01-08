package com.jn.langx.test.util.reflect;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.reflect.Modifiers;
import org.junit.Assert;
import org.junit.Test;

public class ModifiersTests {
    @Test
    public void test() {
        Assert.assertTrue(Modifiers.isPublic(Emptys.class));
    }
}
