package com.jn.langx.java.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Reflects {
    public static Annotation getDeclaredAnnotation(Class clazz, Class<? extends Annotation> annotationClazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation anno : annotations) {
            if (annotationClazz.isInstance(anno)) {
                return anno;
            }
        }
        return null;
    }

    public static boolean isInnerClass(Class<?> clazz) {
        return clazz.isMemberClass() && !isStatic(clazz);
    }

    public static boolean isStatic(Class<?> clazz) {
        return (clazz.getModifiers() & Modifier.STATIC) != 0;
    }

    public static boolean isAnonymousOrLocal(Class<?> clazz) {
        return isAnonymous(clazz) || isLocal(clazz);
    }

    public static boolean isAnonymous(Class clazz){
        return !Enum.class.isAssignableFrom(clazz) && clazz.isAnonymousClass() ;
    }


    public static boolean isLocal(Class clazz){
        return !Enum.class.isAssignableFrom(clazz) && clazz.isLocalClass() ;
    }

    public static Field getField(Class clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            // ignore it
        }
        return field;
    }

    public static Field getDeclaredField(Class clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // ignore it
        }
        return field;
    }

    public static <T> T getDeclaredFieldValue(Object object, String fieldName) {
        Field field = Reflects.getDeclaredField(object.getClass(), fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                return (T) field.get(object);
            } catch (IllegalAccessException e) {
                // ignore it
            }
        }
        return null;
    }
}
