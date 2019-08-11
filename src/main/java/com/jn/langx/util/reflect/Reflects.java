package com.jn.langx.util.reflect;

import com.jn.langx.exception.ExceptionMessage;

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

    public static boolean isAnonymous(Class clazz) {
        return !Enum.class.isAssignableFrom(clazz) && clazz.isAnonymousClass();
    }


    public static boolean isLocal(Class clazz) {
        return !Enum.class.isAssignableFrom(clazz) && clazz.isLocalClass();
    }

    public static Field getPublicField(Class clazz, String fieldName) {
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

    public static Field getAnyField(Class clazz, String fieldName) {
        Field field = getDeclaredField(clazz, fieldName);
        if (field == null) {
            Class parent = clazz.getSuperclass();
            if (parent != null) {
                return getAnyField(parent, fieldName);
            }
            return null;
        }
        return field;
    }

    public static <V> V getPublicFieldValueForcedIfPresent(Object object, String fieldName){
        try {
            return getPublicFieldValue(object, fieldName, false);
        }catch (Throwable ex){
            return null;
        }
    }

    public static <V> V getPublicFieldValue(Object object, String fieldName, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
        Field field = getPublicField(object.getClass(), fieldName);
        if (field == null) {
            if(throwException) {
                throw new NoSuchFieldException(new ExceptionMessage("Can't find public field {0} in the class {1}", fieldName, object.getClass().getCanonicalName()).getMessage());
            }
            return null;
        }else {
            return (V) field.get(object);
        }
    }

    public static <V> V getDeclaredFieldValueForcedIfPresent(Object object, String fieldName){
        try {
            return getDeclaredFieldValue(object, fieldName, true, false);
        }catch (Throwable ex){
            return null;
        }
    }

    public static <V> V getDeclaredFieldValue(Object object, String fieldName, boolean force, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
        Field field = getDeclaredField(object.getClass(), fieldName);
        if (field == null) {
            if(throwException) {
                throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1}", fieldName, object.getClass().getCanonicalName()).getMessage());
            }
            return null;
        }else {
            return getFieldValue(object, field, force, throwException);
        }
    }

    public static <V> V getAnyFieldValueForcedIfPresent(Object object, String fieldName){
        try{
            return getAnyFieldValue(object, fieldName, true, false);
        }catch (Throwable ex){
            return null;
        }
    }

    public static <V> V getAnyFieldValue(Object object, String fieldName, boolean force, boolean throwException)  throws NoSuchFieldException, IllegalAccessException {
        Field field = getAnyField(object.getClass(), fieldName);
        if(field==null){
            if(throwException){
                throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1} and its all super class", fieldName, object.getClass().getCanonicalName()).getMessage());
            }
            return null;
        }else{
            return getFieldValue(object, field, force, throwException);
        }
    }

    private static <V> V getFieldValue(Object object, Field field, boolean force, boolean throwException) throws IllegalAccessException{
        if (!force && !field.isAccessible()) {
            if(throwException) {
                throw new IllegalAccessException();
            }
            return null;
        }
        // accessible
        if (field.isAccessible()) {
            return (V) field.get(object);
        }

        // unaccessible && force
        field.setAccessible(true);
        V v = (V) field.get(object);
        field.setAccessible(false);
        return v;

    }
}
