package com.jn.langx.aspectj.reflect;

import com.jn.langx.annotation.Name;
import com.jn.langx.util.reflect.parameter.ConstructorParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public class AjConstructorParameter implements ConstructorParameter {
    private ConstructorParameter delegate;

    AjConstructorParameter(ConstructorParameter delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isNamePresent() {
        return delegate.isNamePresent();
    }

    @Override
    public Constructor getDeclaringExecutable() {
        return delegate.getDeclaringExecutable();
    }

    @Override
    public int getModifiers() {
        return delegate.getModifiers();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Class<?> getType() {
        return delegate.getType();
    }

    @Override
    public boolean isImplicit() {
        return delegate.isImplicit();
    }

    @Override
    public boolean isSynthetic() {
        return delegate.isSynthetic();
    }

    @Override
    public boolean isVarArgs() {
        return delegate.isVarArgs();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return delegate.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return delegate.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return delegate.getDeclaredAnnotations();
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return delegate.isAnnotationPresent(annotationClass);
    }
}
