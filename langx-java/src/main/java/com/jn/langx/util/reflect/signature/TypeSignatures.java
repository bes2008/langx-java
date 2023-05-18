package com.jn.langx.util.reflect.signature;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.Method;

public class TypeSignatures {
    private TypeSignatures(){

    }
    public static String toTypeSignature(String typeString) {
        Preconditions.checkNotNull(typeString);
        String signature;
        if ("boolean".equals(typeString)) {
            signature = "Z";
        } else if ("byte".equals(typeString)) {
            signature = "B";
        } else if ("char".equals(typeString)) {
            signature = "C";
        } else if ("short".equals(typeString)) {
            signature = "S";
        } else if ("int".equals(typeString)) {
            signature = "I";
        } else if ("long".equals(typeString)) {
            signature = "J";
        } else if ("float".equals(typeString)) {
            signature = "F";
        } else if ("double".equals(typeString)) {
            signature = "D";
        } else if (typeString.endsWith("[]")) {
            String componentType = typeString.substring(0, typeString.length() - 2);
            signature = "[" + toTypeSignature(componentType);
        } else {
            signature = "L" + typeString.replace('.', '/') + ";";
        }
        return signature;
    }

    public static String toTypeSignature(Class clazz) {
        return toTypeSignature(Reflects.getFQNClassName(clazz));
    }

    public static String toTypeSignature(Class[] classes){
        final StringBuilder builder = new StringBuilder();
        Collects.forEach(classes, new Consumer<Class>() {
            @Override
            public void accept(Class aClass) {
                builder.append(toTypeSignature(aClass));
            }
        });
        return builder.toString();
    }

    public static String toMethodSignature(Method method){
        Class declaringClass = method.getDeclaringClass();
        Class[] parameterTypes = method.getParameterTypes();
        Class returnType = method.getReturnType();
        String declaringClassSignature = Reflects.getFQNClassName(declaringClass).replace('.','/');
        String methodName = method.getName();
        String parameterTypesSignature = toTypeSignature(parameterTypes);
        String returnTypeSignature = toTypeSignature(returnType);
        return declaringClassSignature +
                "." +
                methodName +
                ":(" +
                parameterTypesSignature +
                ")" +
                returnTypeSignature;
    }



}
