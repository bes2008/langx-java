package com.jn.langx.test.util.reflect;

import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.parameter.ConstructorParameter;
import com.jn.langx.util.reflect.parameter.MethodParameter;
import com.jn.langx.util.reflect.type.Types;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class ParametersTests {

    @BeforeClass
    public static void init() {

    }

    private static void init0(boolean booleanValue, Integer intValue, String stringValue) {

    }

    @Test
    public void getMethodParametersTest() throws NoSuchMethodException {
        Method method = Reflects.getAnyMethod(ParametersTests.class, "init0", Boolean.TYPE, Integer.class, String.class);
        List<MethodParameter> parameters = Reflects.getMethodParameters(method);
        System.out.println(parameters);

        Constructor constructor = Reflects.getConstructor(StringBuilder.class, String.class);
        List<ConstructorParameter> constructorParameters = Reflects.getConstructorParameters(constructor);
        System.out.println(constructorParameters);


        /*
        Method showMethod = Reflects.getAnyMethod(ParametersTests.class, "show", List.class);
        List<MethodParameter> showMethodParameters = Reflects.getMethodParameters("langx_aspectj", showMethod);
        Type type = Types.resolve(null, null, showMethodParameters.get(0).getParameterizedType());
        System.out.println(showMethodParameters);
        */

    }

    public void show(List<String> ids) {

    }
}
