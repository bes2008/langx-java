package com.jn.langx.test.util.reflect;

import com.jn.langx.util.reflect.Reflects;
import org.junit.Test;

import java.lang.reflect.Method;

public class PrimitiveTests {
    @Test
    public void test(){
        Class a1 = Integer.class;
        Class a2 = int.class;
        Class a3 = Integer.TYPE;
        System.out.println(a1==a2);
        System.out.println(a3==a2);
        Method method = Reflects.getDeclaredMethod(PrimitiveTests.class, "t1");
        Class a4 = method.getReturnType();
        System.out.println(a1 == a4);
        System.out.println(a2 == a4);

        method = Reflects.getDeclaredMethod(PrimitiveTests.class, "t2");
        Class a5 = method.getReturnType();
        System.out.println(a1 == a5);
        System.out.println(a2 == a5);
    }

    private int t1(){
        return 1;
    }

    private Integer t2(){
        return 1;
    }
}
