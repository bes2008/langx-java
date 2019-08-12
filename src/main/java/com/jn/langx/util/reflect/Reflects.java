package com.jn.langx.util.reflect;

import com.jn.langx.annotation.NotNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.exception.ExceptionMessage;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Strings;
import com.jn.langx.util.reflect.type.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

@SuppressWarnings({"unused", "unchecked"})
public class Reflects {

    public static String getTypeName(Class type) {
        return Types.typeToString(type);
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


    public static String getSimpleClassName(Class clazz) {
        return clazz.getSimpleName();
    }

    public static String getFQNClassName(Class clazz) {
        return clazz.getName();
    }

    public static String getPackageName(Class clazz) {
        return clazz.getPackage().getName();
    }


    /**
     * Returns true if an annotation for the specified type
     * is present on this element, else false.  This method
     * is designed primarily for convenient access to marker annotations.
     */
    public static boolean isAnnotationPresent(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass) {
        return annotatedElement.isAnnotationPresent(annotationClass);
    }


    /**
     * Returns this element's annotation for the specified type if
     * such an annotation is present, else null.
     */
    public static <T extends Annotation> T getAnnotation(AnnotatedElement annotatedElement, Class<T> annotationClass) {
        return annotatedElement.getAnnotation(annotationClass);
    }

    /**
     * Returns all annotations present on this element.  (Returns an array
     * of length zero if this element has no annotations.)  The caller of
     * this method is free to modify the returned array; it will have no
     * effect on the arrays returned to other callers.
     */
    public static Annotation[] getAnnotations(AnnotatedElement annotatedElement) {
        return annotatedElement.getAnnotations();
    }

    /**
     * Returns all annotations that are directly present on this
     * element.  Unlike the other methods in this interface, this method
     * ignores inherited annotations.  (Returns an array of length zero if
     * no annotations are directly present on this element.)  The caller of
     * this method is free to modify the returned array; it will have no
     * effect on the arrays returned to other callers.
     */
    public static Annotation[] getDeclaredAnnotations(AnnotatedElement annotatedElement) {
        return annotatedElement.getDeclaredAnnotations();
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

    public static <V> V getPublicFieldValueForcedIfPresent(Object object, String fieldName) {
        try {
            return getPublicFieldValue(object, fieldName, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V getPublicFieldValue(Object object, String fieldName, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
        Field field = getPublicField(object.getClass(), fieldName);
        if (field == null) {
            if (throwException) {
                throw new NoSuchFieldException(new ExceptionMessage("Can't find public field {0} in the class {1}", fieldName, object.getClass().getCanonicalName()).getMessage());
            }
            return null;
        } else {
            return (V) field.get(object);
        }
    }

    public static <V> V getDeclaredFieldValueForcedIfPresent(Object object, String fieldName) {
        try {
            return getDeclaredFieldValue(object, fieldName, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V getDeclaredFieldValue(Object object, String fieldName, boolean force, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
        Field field = getDeclaredField(object.getClass(), fieldName);
        if (field == null) {
            if (throwException) {
                throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1}", fieldName, object.getClass().getCanonicalName()).getMessage());
            }
            return null;
        } else {
            return getFieldValue(field, object, force, throwException);
        }
    }

    public static <V> V getAnyFieldValueForcedIfPresent(Object object, String fieldName) {
        try {
            return getAnyFieldValue(object, fieldName, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V getAnyFieldValue(Object object, String fieldName, boolean force, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
        Field field = getAnyField(object.getClass(), fieldName);
        if (field == null) {
            if (throwException) {
                throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1} and its all super class", fieldName, object.getClass().getCanonicalName()).getMessage());
            }
            return null;
        } else {
            return getFieldValue(field, object, force, throwException);
        }
    }

    public static <V> V getFieldValue(Field field, Object object, boolean force, boolean throwException) throws IllegalAccessException {
        if (!force && !field.isAccessible()) {
            if (throwException) {
                throw new IllegalAccessException();
            }
            return null;
        }

        // accessible
        if (field.isAccessible()) {
            try {
                return (V) field.get(object);
            } catch (IllegalArgumentException ex) {
                if (throwException) {
                    throw ex;
                }
                return null;
            }
        }

        // unaccessible && force
        field.setAccessible(true);
        try {
            return (V) field.get(object);
        } catch (IllegalArgumentException ex) {
            if (throwException) {
                throw ex;
            }
            return null;
        } finally {
            field.setAccessible(false);
        }
    }

    public static Method getPublicMethod(Class clazz, String methodName, Class[] parameterTypes) {
        Method method = null;
        try {
            method = clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ex) {
            method = null;
        }
        return method;
    }

    public static Method getDeclaredMethod(Class clazz, String methodName, Class[] parameterTypes) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ex) {
            method = null;
        }
        return method;
    }

    public static Method getAnyMethod(Class clazz, String methodName, Class[] parameterTypes) {
        Method method = getDeclaredMethod(clazz, methodName, parameterTypes);
        if (method == null) {
            Class parent = clazz.getSuperclass();
            if (parent != null) {
                return getAnyMethod(parent, methodName, parameterTypes);
            }
            return null;
        }
        return method;
    }

    public static <V> V invokePublicMethodForcedIfPresent(Object object, String methodName, Class[] parameterTypes, Object[] parameters) {
        try {
            return (V) invokePublicMethod(object, methodName, parameterTypes, parameters, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V invokePublicMethod(Object object, String methodName, Class[] parameterTypes, Object[] parameters, boolean force, boolean throwException) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getPublicMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            if (throwException) {
                throw new NoSuchMethodException(new ExceptionMessage("Can't find the method: {0}", getMethodString(getFQNClassName(object.getClass()), methodName, null, parameterTypes)).getMessage());
            }
            return null;
        }
        return (V) invokeMethodOrNull(method, object, parameters, throwException);
    }

    public static <V> V invokeDeclaredMethodForcedIfPresent(Object object, String methodName, Class[] parameterTypes, Object[] parameters) {
        try {
            return (V) invokeDeclaredMethod(object, methodName, parameterTypes, parameters, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V invokeDeclaredMethod(Object object, String methodName, Class[] parameterTypes, Object[] parameters, boolean force, boolean throwException) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getDeclaredMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            if (throwException) {
                throw new NoSuchMethodException(new ExceptionMessage("Can't find the method: {0}", getMethodString(getFQNClassName(object.getClass()), methodName, null, parameterTypes)).getMessage());
            }
            return null;
        }
        return (V) invokeMethodOrNull(method, object, parameters, throwException);
    }

    public static <V> V invokeAnyMethodForcedIfPresent(Object object, String methodName, Class[] parameterTypes, Object[] parameters) {
        try {
            return (V) invokeAnyMethod(object, methodName, parameterTypes, parameters, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V invokeAnyMethod(Object object, String methodName, Class[] parameterTypes, Object[] parameters, boolean force, boolean throwException) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getAnyMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            if (throwException) {
                throw new NoSuchMethodException(new ExceptionMessage("Can't find the method: {0}", getMethodString(getFQNClassName(object.getClass()), methodName, null, parameterTypes)).getMessage());
            }
            return null;
        }
        return (V) invokeMethodOrNull(method, object, parameters, throwException);
    }

    public static <V> V invoke(Method method, Object object, Object[] parameters, boolean force, boolean throwException) throws IllegalAccessException, InvocationTargetException {
        if (!force && !method.isAccessible()) {
            if (throwException) {
                throw new IllegalAccessException(new ExceptionMessage("Method {0} is not accessible", method.toString()).getMessage());
            }
            return null;
        }

        if (method.isAccessible()) {
            return (V) invokeMethodOrNull(method, object, parameters, throwException);
        }

        // force && unaccessible
        method.setAccessible(true);
        try {
            return (V) invokeMethodOrNull(method, object, parameters, throwException);
        } finally {
            method.setAccessible(false);
        }
    }

    private static <V> V invokeMethodOrNull(Method method, Object object, Object[] parameters, boolean throwException) throws IllegalAccessException, InvocationTargetException {
        try {
            return (V) method.invoke(object, parameters);
        } catch (IllegalAccessException ex) {
            if (throwException) {
                throw ex;
            }
            return null;
        } catch (InvocationTargetException ex) {
            if (throwException) {
                throw ex;
            }
            return null;
        }
    }

    public static String getMethodString(@Nullable String clazzFQN,
                                         @NotNull String methodName,
                                         @Nullable Class returnType,
                                         @Nullable Class[] parameterTypes) {
        try {
            StringBuffer sb = new StringBuffer();
            if (returnType != null) {
                sb.append(getTypeName(returnType) + " ");
            }
            if (Strings.isNotBlank(clazzFQN)) {
                sb.append(clazzFQN + ".");
            }
            sb.append(methodName + "(");
            if (!Emptys.isEmpty(parameterTypes)) {
                Class[] params = parameterTypes; // avoid clone
                for (int j = 0; j < params.length; j++) {
                    sb.append(getTypeName(params[j]));
                    if (j < (params.length - 1)) {
                        sb.append(",");
                    }
                }
            }
            sb.append(")");
            return sb.toString();
        } catch (Exception e) {
            return "<" + e + ">";
        }
    }

    public static String getMethodString(Method method) {
        return method.toString();
    }


    public static String getMethodString(Class clazz, String methodName, Class[] parameterTypes) {
        Method method = getAnyMethod(clazz, methodName, parameterTypes);
        if (method != null) {
            return getMethodString(method);
        } else {
            return getMethodString(getTypeName(clazz), methodName, null, parameterTypes);
        }
    }


}
