package com.jn.langx.java8.util.reflect.parameter;

import com.jn.langx.util.reflect.parameter.ConstructorParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public class Java8ConstructorParameter implements ConstructorParameter {
    private Parameter parameter;

    /**
     * package private
     */
    Java8ConstructorParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public boolean isNamePresent() {
        return parameter.isNamePresent();
    }

    @Override
    public Constructor getDeclaringExecutable() {
        return (Constructor) parameter.getDeclaringExecutable();
    }

    @Override
    public int getModifiers() {
        return parameter.getModifiers();
    }

    @Override
    public String getName() {
        return parameter.getName();
    }


    @Override
    public Type getParameterizedType() {
        return parameter.getParameterizedType();
    }

    @Override
    public Class<?> getType() {
        return parameter.getType();
    }

    @Override
    public boolean isImplicit() {
        return parameter.isImplicit();
    }

    @Override
    public boolean isSynthetic() {
        return parameter.isSynthetic();
    }

    @Override
    public boolean isVarArgs() {
        return parameter.isVarArgs();
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return parameter.isAnnotationPresent(annotationClass);
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return parameter.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return parameter.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return parameter.getDeclaredAnnotations();
    }

    @Override
    public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
        return parameter.getDeclaredAnnotationsByType(annotationClass);
    }

    @Override
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
        return parameter.getAnnotationsByType(annotationClass);
    }

    @Override
    public String toString() {
        return parameter.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Java8ConstructorParameter that = (Java8ConstructorParameter) o;

        return parameter.equals(that.parameter);
    }

    @Override
    public int hashCode() {
        return parameter.hashCode();
    }

}
