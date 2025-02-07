package com.jn.langx.util.reflect.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * 定义了一个解析注解元素的接口，扩展了AnnotationParser接口
 * 该接口用于处理Java中的AnnotatedElement类型对象上的注解
 * AnnotatedElement是Java中所有带注解元素的父接口，比如类、方法、字段等
 *
 * @param <A> 注解类型，表示该解析器处理的注解类型
 * @param <O> 解析结果类型，表示解析注解后返回的结果类型
 */
public interface AnnotatedElementAnnotationParser<A extends Annotation, O> extends AnnotationParser<A, AnnotatedElement, O> {

    /**
     * 获取解析器处理的注解类型
     *
     * @return 解析器处理的注解类型的Class对象
     */
    @Override
    Class<A> getAnnotation();

    /**
     * 解析给定的AnnotatedElement对象上的注解
     *
     * @param annotatedElement 待解析的注解元素
     * @return 解析后的结果对象
     */
    @Override
    O parse(AnnotatedElement annotatedElement);

    /**
     * 是否解析声明者，对于 构造器、方法、字段来讲，declaring 就是 class
     * 对于 方法的参数来讲，declaring 就是 method
     *
     * @return true or false
     */
    boolean isParseDeclaring();
}
