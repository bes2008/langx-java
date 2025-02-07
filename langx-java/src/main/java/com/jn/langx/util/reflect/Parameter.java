package com.jn.langx.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
/**
 * Parameter接口用于描述一个执行体（如方法或构造器）的参数信息
 * 它提供了获取参数名、参数类型、修饰符等信息的方法
 *
 * @param <T> 表示声明该参数的执行体类型，通常是Method或Constructor
 */
public interface Parameter<T> extends AnnotatedElement {
    /**
     * 判断参数名是否可用
     *
     * @return 如果参数名可用，则返回true；否则返回false
     */
    boolean isNamePresent();

    /**
     * 获取声明该参数的执行体
     *
     * @return 声明该参数的执行体，如方法或构造器
     */
    T getDeclaringExecutable();

    /**
     * 获取参数的修饰符
     *
     * @return 参数的修饰符，如public、private等
     */
    int getModifiers();

    /**
     * 获取参数名
     *
     * @return 参数名
     */
    String getName();

    /**
     * 获取参数的类型，考虑泛型情况
     *
     * @return 参数的类型，包括泛型信息
     */
    Type getParameterizedType();

    /**
     * 获取参数的简单类型，不考虑泛型情况
     *
     * @return 参数的简单类型
     */
    Class<?> getType();

    /**
     * 判断参数是否是隐含的
     *
     * @return 如果参数是隐含的，则返回true；否则返回false
     */
    boolean isImplicit();

    /**
     * 判断参数是否是合成的
     *
     * @return 如果参数是合成的，则返回true；否则返回false
     */
    boolean isSynthetic();

    /**
     * 判断参数是否是可变长参数
     *
     * @return 如果参数是可变长参数，则返回true；否则返回false
     */
    boolean isVarArgs();

    /**
     * 获取参数上声明的指定类型的注解数组
     *
     * @param annotationClass 注解的类型
     * @param <A> 注解类型参数
     * @return 指定类型的注解数组
     */
    <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> annotationClass);

    /**
     * 获取参数上直接或间接（通过继承）获得的指定类型的注解数组
     *
     * @param annotationClass 注解的类型
     * @param <A> 注解类型参数
     * @return 指定类型的注解数组
     */
    <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationClass);

    /**
     * 获取参数在执行体中的索引
     *
     * @return 参数在执行体中的索引
     */
    int getIndex();
}
