package com.jn.langx.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class Annotations {
    public static boolean isAnnotationPresent(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass){
        return annotatedElement.isAnnotationPresent(annotationClass);
    }
}
