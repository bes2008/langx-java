package com.jn.langx.util.reflect.annotation;

import com.jn.langx.Parser;

import java.lang.annotation.Annotation;

public interface AnnotationParser<A extends Annotation, I, O> extends Parser<I, O> {
    Class<A> getAnnotation();

    @Override
    O parse(I input);

}
