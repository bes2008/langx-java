package com.jn.langx.util.reflect;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

public interface Parameter<T> extends AnnotatedElement {
    boolean isNamePresent();

    T getDeclaringExecutable();

    int getModifiers();

    String getName();

    Type getParameterizedType();

    Class<?> getType();

    boolean isImplicit();

    boolean isSynthetic();

    boolean isVarArgs();
}
