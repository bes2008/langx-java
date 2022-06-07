package com.jn.langx.test.util.reflect;

import com.jn.langx.event.DomainEvent;
import com.jn.langx.util.reflect.parameter.ParameterSupplier;
import com.jn.langx.util.reflect.type.Types;
import org.junit.Test;

import java.util.Date;

public class TypeToStringTests {
    @Test
    public void test() {
        testPrimitive();
        System.out.println(Types.typeToString(TypeToStringTests.class));
        System.out.println(Types.typeToString(ParameterSupplier.class));
        System.out.println(Types.typeToString(DomainEvent.class));


        DomainEvent domainEvent = new DomainEvent("dt",new Date());
        System.out.println(Types.typeToString(domainEvent.getClass()));
    }

    private void testPrimitive() {
        System.out.println(Types.typeToString(int.class));
        System.out.println(Types.typeToString(boolean.class));
        System.out.println(Types.typeToString(short.class));
        System.out.println(Types.typeToString(byte.class));
        System.out.println(Types.typeToString(char.class));
        System.out.println(Types.typeToString(double.class));
        System.out.println(Types.typeToString(float.class));
    }
}
