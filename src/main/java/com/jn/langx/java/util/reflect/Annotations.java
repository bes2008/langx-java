package com.jn.langx.java.util.reflect;

import java.lang.annotation.Annotation;

public class Annotations {
    public static Annotation getDeclaredAnnotation(Class clazz, Class<? extends Annotation> annotationClazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation anno : annotations) {
            if (annotationClazz.isInstance(anno)) {
                return anno;
            }
        }
        return null;
    }
}
