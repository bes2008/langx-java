package com.jn.langx.util.reflect.annotation;

import com.jn.langx.Parser;

import java.lang.annotation.Annotation;

/**
 * AnnotationParser接口是一个泛型接口，用于解析带有特定注解的输入数据
 * 它继承了Parser接口，专注于解析带有特定注解类型的数据
 *
 * @param <A> 注解类型，必须是Annotation的子类型
 * @param <I> 输入类型，表示待解析的数据类型
 * @param <O> 输出类型，表示解析后的结果类型
 */
public interface AnnotationParser<A extends Annotation, I, O> extends Parser<I, O> {

    /**
     * 获取AnnotationParser关注的注解类型
     *
     * @return 返回注解类型的Class对象，用于标识解析器关注的注解类型
     */
    Class<A> getAnnotation();

    /**
     * 解析输入数据，专注于处理带有特定注解的数据
     *
     * @param input 待解析的输入数据，其类型为接口定义的输入类型I
     * @return 解析后的输出数据，其类型为接口定义的输出类型O
     */
    @Override
    O parse(I input);

}
