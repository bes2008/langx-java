package com.jn.langx.util.reflect;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.exception.ExceptionMessage;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.type.Types;
import com.jn.langx.util.struct.Holder;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;


/**
 * @author jinuo.fang
 */
@SuppressWarnings({"unused", "unchecked"})
public class Reflects {

    public static String getTypeName(@NonNull Class type) {
        return Types.typeToString(type);
    }

    public static boolean isInnerClass(@NonNull Class<?> clazz) {
        return clazz.isMemberClass() && !isStatic(clazz);
    }

    public static boolean isStatic(@NonNull Class<?> clazz) {
        return (clazz.getModifiers() & Modifier.STATIC) != 0;
    }

    public static boolean isAnonymousOrLocal(@NonNull Class<?> clazz) {
        return isAnonymous(clazz) || isLocal(clazz);
    }

    public static boolean isAnonymous(@NonNull Class clazz) {
        return !Enum.class.isAssignableFrom(clazz) && clazz.isAnonymousClass();
    }


    public static boolean isLocal(@NonNull Class clazz) {
        return !Enum.class.isAssignableFrom(clazz) && clazz.isLocalClass();
    }


    public static String getSimpleClassName(@NonNull Class clazz) {
        return clazz.getSimpleName();
    }

    public static String getFQNClassName(@NonNull Class clazz) {
        return clazz.getName();
    }

    public static String getPackageName(@NonNull Class clazz) {
        return clazz.getPackage().getName();
    }

    public static String getJvmSignature(@NonNull Class clazz) {
        return Types.getTypeSignature(getFQNClassName(clazz));
    }

    public static URL getCodeLocation(@NonNull Class clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }

    /**
     * <p>Gets a {@code List} of all interfaces implemented by the given
     * class and its superclasses.</p>
     * <p>
     * <p>The order is determined by looking through each interface in turn as
     * declared in the source file and following its hierarchy up. Then each
     * superclass is considered in the same way. Later duplicates are ignored,
     * so the order is maintained.</p>
     *
     * @param cls the class to look up, may be {@code null}
     * @return the {@code List} of interfaces in order,
     */
    public static List<Class<?>> getAllInterfaces(@Nullable final Class<?> cls) {
        if (cls == null) {
            return Collects.emptyArrayList();
        }

        final LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<Class<?>>();
        getAllInterfaces(cls, interfacesFound);

        return new ArrayList<Class<?>>(interfacesFound);
    }

    /**
     * Get the interfaces for the specified class.
     *
     * @param cls             the class to look up, may be {@code null}
     * @param interfacesFound the {@code Set} of interfaces for the class
     */
    private static void getAllInterfaces(@NonNull Class<?> cls, final HashSet<Class<?>> interfacesFound) {
        while (cls != null) {
            final Class<?>[] interfaces = cls.getInterfaces();

            for (final Class<?> i : interfaces) {
                if (interfacesFound.add(i)) {
                    getAllInterfaces(i, interfacesFound);
                }
            }

            cls = cls.getSuperclass();
        }
    }

    /**
     * Get an {@link Iterable} that can iterate over a class hierarchy in ascending (subclass to superclass) order,
     * excluding interfaces.
     *
     * @param type the type to get the class hierarchy from
     * @return Iterable an Iterable over the class hierarchy of the given class
     * @since 3.2
     */
    public static Iterable<Class<?>> hierarchy(@NonNull final Class<?> type) {
        Preconditions.checkNotNull(type);
        return hierarchy(type, true);
    }

    /**
     * Get an {@link Iterable} that can iterate over a class hierarchy in ascending (subclass to superclass) order.
     *
     * @param type              the type to get the class hierarchy from
     * @param excludeInterfaces switch indicating whether to include or exclude interfaces
     * @return Iterable an Iterable over the class hierarchy of the given class
     * @since 3.2
     */
    public static Iterable<Class<?>> hierarchy(@NonNull final Class<?> type, final boolean excludeInterfaces) {
        Preconditions.checkNotNull(type);
        final Iterable<Class<?>> classes = new Iterable<Class<?>>() {

            @Override
            public Iterator<Class<?>> iterator() {
                final Holder<Class<?>> next = new Holder<Class<?>>(type);
                return new Iterator<Class<?>>() {

                    @Override
                    public boolean hasNext() {
                        return next.get() != null;
                    }

                    @Override
                    public Class<?> next() {
                        final Class<?> result = next.get();
                        next.set(result.getSuperclass());
                        return result;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                };
            }

        };
        if (excludeInterfaces) {
            return classes;
        }
        return new Iterable<Class<?>>() {

            @Override
            public Iterator<Class<?>> iterator() {
                final Set<Class<?>> seenInterfaces = new HashSet<Class<?>>();
                final Iterator<Class<?>> wrapped = classes.iterator();

                return new Iterator<Class<?>>() {
                    Iterator<Class<?>> interfaces = Collections.<Class<?>>emptySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return interfaces.hasNext() || wrapped.hasNext();
                    }

                    @Override
                    public Class<?> next() {
                        if (interfaces.hasNext()) {
                            final Class<?> nextInterface = interfaces.next();
                            seenInterfaces.add(nextInterface);
                            return nextInterface;
                        }
                        final Class<?> nextSuperclass = wrapped.next();
                        final Set<Class<?>> currentInterfaces = new LinkedHashSet<Class<?>>();
                        walkInterfaces(currentInterfaces, nextSuperclass);
                        interfaces = currentInterfaces.iterator();
                        return nextSuperclass;
                    }

                    private void walkInterfaces(final Set<Class<?>> addTo, final Class<?> c) {
                        for (final Class<?> iface : c.getInterfaces()) {
                            if (!seenInterfaces.contains(iface)) {
                                addTo.add(iface);
                            }
                            walkInterfaces(addTo, iface);
                        }
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                };
            }
        };
    }


    /**
     * Returns true if an annotation for the specified type
     * is present on this element, else false.  This method
     * is designed primarily for convenient access to marker annotations.
     */
    public static boolean isAnnotationPresent(@NonNull AnnotatedElement annotatedElement, @NonNull Class<? extends Annotation> annotationClass) {
        return annotatedElement.isAnnotationPresent(annotationClass);
    }


    /**
     * Returns this element's annotation for the specified type if
     * such an annotation is present, else null.
     */
    public static <E extends Annotation> E getAnnotation(@NonNull AnnotatedElement annotatedElement, @NonNull Class<E> annotationClass) {
        return annotatedElement.getAnnotation(annotationClass);
    }

    /**
     * Returns all annotations present on this element.  (Returns an array
     * of length zero if this element has no annotations.)  The caller of
     * this method is free to modify the returned array; it will have no
     * effect on the arrays returned to other callers.
     */
    public static Annotation[] getAnnotations(@NonNull AnnotatedElement annotatedElement) {
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
    public static List<Annotation> getDeclaredAnnotations(@NonNull AnnotatedElement annotatedElement) {
        return Collects.asList(annotatedElement.getDeclaredAnnotations());
    }

    public static Field getPublicField(@NonNull Class clazz, @NonNull String fieldName) {
        Field field = null;
        try {
            field = clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            // ignore it
        }
        return field;
    }

    public static Field getDeclaredField(@NonNull Class clazz, @NonNull String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // ignore it
        }
        return field;
    }

    public static Field getAnyField(@NonNull Class clazz, @NonNull String fieldName) {
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

    public static Collection<Field> getAllDeclaredFields(@NonNull Class clazz) {
        return getAllDeclaredFields(clazz, false);
    }

    public static Collection<Field> getAllDeclaredFields(@NonNull Class clazz, boolean containsStatic) {
        Field[] fields = clazz.getDeclaredFields();
        return !containsStatic ? filterFields(fields, Modifier.STATIC) : filterFields(fields);
    }

    public static Collection<Field> getAllPublicInstanceFields(@NonNull Class clazz) {
        return getAllPublicFields(clazz, false);
    }

    public static Collection<Field> getAllPublicFields(@NonNull Class clazz, boolean containsStatic) {
        Field[] fields = clazz.getFields();
        return !containsStatic ? filterFields(fields, Modifier.STATIC) : filterFields(fields);
    }

    public static Collection<Field> filterFields(@NonNull Field[] fields, final int... excludedModifiers) {
        final List<Integer> excludedModifierList = Collects.asList(PrimitiveArrays.wrap(excludedModifiers, false));
        return Collects.filter(fields, new Predicate<Field>() {
            @Override
            public boolean test(final Field field) {
                return Collects.noneMatch(excludedModifierList, new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer modifier) {
                        return Modifiers.hasModifier(field, modifier);
                    }
                });
            }
        });
    }

    public static <V> V getPublicFieldValueForcedIfPresent(@NonNull Object object, @NonNull String fieldName) {
        try {
            return getPublicFieldValue(object, fieldName, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V getPublicFieldValue(@NonNull Object object, @NonNull String fieldName, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
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

    public static <V> V getDeclaredFieldValueForcedIfPresent(@NonNull Object object, @NonNull String fieldName) {
        try {
            return getDeclaredFieldValue(object, fieldName, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V getDeclaredFieldValue(@NonNull Object object, String fieldName, boolean force, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
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

    public static <V> V getAnyFieldValueForcedIfPresent(@NonNull Object object, @NonNull String fieldName) {
        try {
            return getAnyFieldValue(object, fieldName, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V getAnyFieldValue(@NonNull Object object, @NonNull String fieldName, boolean force, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
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

    public static <V> V getFieldValue(@NonNull Field field, @NonNull Object object, boolean force, boolean throwException) throws IllegalAccessException {
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

    public static void setFieldValue(@NonNull Field field, @NonNull Object target, Object value, boolean force, boolean throwException) throws NullPointerException, IllegalAccessException {
        if (Emptys.isEmpty(field)) {
            if (throwException) {
                Preconditions.checkNotNull(field);
            }
        } else {
            if (!force && !field.isAccessible()) {
                if (throwException) {
                    throw new IllegalAccessException();
                }
                return;
            }

            if (field.isAccessible()) {
                if (throwException) {
                    field.set(target, value);
                } else {
                    try {
                        field.set(target, value);
                    } catch (Throwable ex) {
                        // ignore it
                    }
                }
                return;
            }

            field.setAccessible(true);
            try {
                field.set(target, value);
            } catch (Throwable ex) {
                if (throwException) {
                    throw new RuntimeException(ex);
                }
            } finally {
                field.setAccessible(false);
            }
        }
    }

    public static void setPublicFieldValue(@NonNull Object object, @NonNull String fieldName, Object value, boolean force, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
        Field field = getPublicField(object.getClass(), fieldName);
        if (field == null) {
            if (throwException) {
                throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1} and its all super class", fieldName, object.getClass().getCanonicalName()).getMessage());
            }
        } else {
            setFieldValue(field, object, value, force, throwException);
        }
    }

    public static void setDeclaredFieldValue(@NonNull Object object, @NonNull String fieldName, Object value, boolean force, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
        Field field = getDeclaredField(object.getClass(), fieldName);
        if (field == null) {
            if (throwException) {
                throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1} and its all super class", fieldName, object.getClass().getCanonicalName()).getMessage());
            }
        } else {
            setFieldValue(field, object, value, force, throwException);
        }
    }

    public static void setAnyFieldValue(@NonNull Object object, @NonNull String fieldName, Object value, boolean force, boolean throwException) throws NoSuchFieldException, IllegalAccessException {
        Field field = getAnyField(object.getClass(), fieldName);
        if (field == null) {
            if (throwException) {
                throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1} and its all super class", fieldName, object.getClass().getCanonicalName()).getMessage());
            }
        } else {
            setFieldValue(field, object, value, force, throwException);
        }
    }

    public static Constructor getConstructor(@NonNull Class clazz, Class... parameterTypes) {
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    public static <E> E newInstance(@NonNull Class<E> clazz) {
        Preconditions.checkNotNull(clazz);
        try {
            return (E) clazz.newInstance();
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <E> E newInstance(@NonNull Class<E> clazz, @Nullable Class[] parameterTypes, @NonNull Object[] parameters) {
        Preconditions.checkNotNull(clazz);
        Constructor constructor = getConstructor(clazz, parameterTypes);

        try {
            return (E) constructor.newInstance(parameters);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static Method getPublicMethod(@NonNull Class clazz, @NonNull String methodName, Class... parameterTypes) {
        Method method = null;
        try {
            method = clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ex) {
            method = null;
        }
        return method;
    }

    public static Method getDeclaredMethod(@NonNull Class clazz, @NonNull String methodName, Class... parameterTypes) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ex) {
            method = null;
        }
        return method;
    }

    public static Method getAnyMethod(@NonNull Class clazz, @NonNull String methodName, Class... parameterTypes) {
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

    public static <V> V invokePublicMethodForcedIfPresent(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters) {
        try {
            return (V) invokePublicMethod(object, methodName, parameterTypes, parameters, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V invokePublicMethod(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters, boolean force, boolean throwException) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getPublicMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            if (throwException) {
                throw new NoSuchMethodException(new ExceptionMessage("Can't find the method: {0}", getMethodString(getFQNClassName(object.getClass()), methodName, null, parameterTypes)).getMessage());
            }
            return null;
        }
        return (V) invokeMethodOrNull(method, object, parameters, throwException);
    }

    public static <V> V invokeDeclaredMethodForcedIfPresent(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters) {
        try {
            return (V) invokeDeclaredMethod(object, methodName, parameterTypes, parameters, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V invokeDeclaredMethod(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters, boolean force, boolean throwException) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getDeclaredMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            if (throwException) {
                throw new NoSuchMethodException(new ExceptionMessage("Can't find the method: {0}", getMethodString(getFQNClassName(object.getClass()), methodName, null, parameterTypes)).getMessage());
            }
            return null;
        }
        return (V) invokeMethodOrNull(method, object, parameters, throwException);
    }

    public static <V> V invokeAnyMethodForcedIfPresent(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters) {
        try {
            return (V) invokeAnyMethod(object, methodName, parameterTypes, parameters, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V invokeAnyMethod(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters, boolean force, boolean throwException) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getAnyMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            if (throwException) {
                throw new NoSuchMethodException(new ExceptionMessage("Can't find the method: {0}", getMethodString(getFQNClassName(object.getClass()), methodName, null, parameterTypes)).getMessage());
            }
            return null;
        }
        return (V) invokeMethodOrNull(method, object, parameters, throwException);
    }

    public static <V> V invoke(@NonNull Method method, @NonNull Object object, @Nullable Object[] parameters, boolean force, boolean throwException) throws IllegalAccessException, InvocationTargetException {
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

    private static <V> V invokeMethodOrNull(@NonNull Method method, @NonNull Object object, @Nullable Object[] parameters, boolean throwException) throws IllegalAccessException, InvocationTargetException {
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
                                         @NonNull String methodName,
                                         @Nullable Class returnType,
                                         @Nullable Class[] parameterTypes) {
        try {
            StringBuilder sb = new StringBuilder();
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


    public static String getMethodString(@NonNull Class clazz, @NonNull String methodName, @Nullable Class[] parameterTypes) {
        Method method = getAnyMethod(clazz, methodName, parameterTypes);
        if (method != null) {
            return getMethodString(method);
        } else {
            return getMethodString(getTypeName(clazz), methodName, null, parameterTypes);
        }
    }

}
