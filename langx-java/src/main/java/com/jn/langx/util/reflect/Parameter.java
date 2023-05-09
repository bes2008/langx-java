package com.jn.langx.util.reflect;

import java.lang.annotation.Annotation;
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

    <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> annotationClass);

    <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationClass);

    int getIndex();
}
