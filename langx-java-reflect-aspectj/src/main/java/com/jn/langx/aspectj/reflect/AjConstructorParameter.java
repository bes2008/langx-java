package com.jn.langx.aspectj.reflect;

import com.jn.langx.util.Objects;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.reflect.parameter.ConstructorParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class AjConstructorParameter implements ConstructorParameter {
    private ConstructorParameter delegate;
    private String name;

    AjConstructorParameter(String name, ConstructorParameter delegate) {
        Preconditions.checkNotNull(delegate);
        this.delegate = delegate;
        this.name = name;
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
        return Strings.isEmpty(name) ? delegate.getName() : name;
    }

    @Override
    public Type getParameterizedType() {
        return delegate.getParameterizedType();
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

    @Override
    public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
        return delegate.getDeclaredAnnotationsByType(annotationClass);
    }

    @Override
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
        return delegate.getAnnotationsByType(annotationClass);
    }

    @Override
    public String toString() {
        if (Strings.isEmpty(name)) {
            return delegate.toString();
        } else {
            final StringBuilder sb = new StringBuilder();
            final Type type = this.getType();
            final String typename = type.toString();

            sb.append(Modifier.toString(getModifiers()));

            if (0 != getModifiers())
                sb.append(' ');

            if (isVarArgs())
                sb.append(typename.replaceFirst("\\[\\]$", "..."));
            else
                sb.append(typename);

            sb.append(' ');
            sb.append(getName());

            return sb.toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AjConstructorParameter that = (AjConstructorParameter) o;

        return Objects.equals(delegate, that.delegate);
    }

    @Override
    public int hashCode() {
        return delegate != null ? delegate.hashCode() : 0;
    }
}
