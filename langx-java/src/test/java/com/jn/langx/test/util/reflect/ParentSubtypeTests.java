package com.jn.langx.test.util.reflect;

import com.jn.langx.util.reflect.ArrayAccessor;
import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

public class ParentSubtypeTests {
    @Test
    public void test(){
        System.out.println(Reflects.isSubClassOrEquals("java.lang.Object", ArrayAccessor.class));
        System.out.println(Reflects.isSubClassOrEquals("com.jn.langx.Accessor", ArrayAccessor.class));
        System.out.println(Reflects.isSubClassOrEquals("com.jn.langx.util.valuegetter.ValueGetter2", ArrayAccessor.class));
        System.out.println(Reflects.isSubClassOrEquals("com.jn.langx.util.valuegetter.ValueGetter", ArrayAccessor.class));
    }
}
