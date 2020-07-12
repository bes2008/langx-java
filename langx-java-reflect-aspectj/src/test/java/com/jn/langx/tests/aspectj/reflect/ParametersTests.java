package com.jn.langx.tests.aspectj.reflect;


import com.jn.langx.aspectj.reflect.AjReflectConstants;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.parameter.MethodParameter;
import com.jn.langx.util.reflect.type.Types;
import org.junit.BeforeClass;
import org.junit.Test;

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
        /*
        Method method = Reflects.getAnyMethod(ParametersTests.class, "init0", Boolean.TYPE, Integer.class, String.class);
        List<MethodParameter> parameters = Reflects.getMethodParameters(AjReflectConstants.DEFAULT_PARAMETER_SUPPLIER_NAME, method);
        System.out.println(parameters);
        Constructor constructor = Reflects.getConstructor(Java8MethodParameter.class, Parameter.class);
        List<ConstructorParameter> constructorParameters = Reflects.getConstructorParameters(AjReflectConstants.DEFAULT_PARAMETER_SUPPLIER_NAME, constructor);
        System.out.println(constructorParameters);
        */

        Method showMethod = Reflects.getAnyMethod(ParametersTests.class, "show", List.class);
        List<MethodParameter> showMethodParameters = Reflects.getMethodParameters(AjReflectConstants.DEFAULT_PARAMETER_SUPPLIER_NAME, showMethod);
        Type type = Types.resolve(null, null, showMethodParameters.get(0).getParameterizedType());
        System.out.println(showMethodParameters);
    }

    public void show(List<String> ids) {

    }
}
