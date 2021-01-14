package com.jn.langx.util.reflect.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface AnnotatedElementAnnotationParser<A extends Annotation, O> extends AnnotationParser<A, AnnotatedElement, O> {
    @Override
    Class<A> getAnnotation();

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
