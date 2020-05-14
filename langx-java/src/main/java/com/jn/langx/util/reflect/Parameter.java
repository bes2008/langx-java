package com.jn.langx.util.reflect;

import java.lang.reflect.AnnotatedElement;

public interface Parameter<T> extends AnnotatedElement {
    boolean isNamePresent();

    T getDeclaringExecutable();

    int getModifiers();

    String getName();

    Class<?> getType();

    boolean isImplicit();

    boolean isSynthetic();

    boolean isVarArgs();
}
