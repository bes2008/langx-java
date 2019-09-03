package com.jn.langx.util.reflect;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.exception.ExceptionMessage;
import com.jn.langx.util.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Mapper;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.type.Types;
import com.jn.langx.util.struct.Holder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.System.arraycopy;


/**
 * @author jinuo.fang
 */
@SuppressWarnings({"unused", "unchecked"})
public class Reflects {

    private static final Pattern lamdbaPattern = Pattern.compile(".*\\$\\$Lambda\\$[0-9]+/.*");
    private static final Logger logger = LoggerFactory.getLogger(Reflects.class);

    private static final Method OBJECT_EQUALS = getDeclaredMethod(Object.class, "equals", Object.class);
    private static final Method OBJECT_HASHCODE = getDeclaredMethod(Object.class, "hashCode");


    public static String getTypeName(@NonNull Class type) {
        return Types.typeToString(type);
    }

    public static boolean isInnerClass(@NonNull Class<?> clazz) {
        return clazz.isMemberClass() && !isStatic(clazz);
    }

    public static boolean isLambda(@NonNull Class<?> clazz) {
        return clazz.isSynthetic() && lamdbaPattern.matcher(getSimpleClassName(clazz)).matches();
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

    public static String getSimpleClassName(@NonNull Object obj) {
        return getSimpleClassName(obj.getClass());
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
        Preconditions.checkNotNull(clazz);
        if (Types.isArray(clazz)) {
            return getCodeLocation(clazz.getComponentType());
        }
        ProtectionDomain pd = clazz.getProtectionDomain();
        if (pd == null) {
            return null;
        }
        CodeSource codeSource = pd.getCodeSource();
        if (codeSource == null) {
            return null;
        }
        return codeSource.getLocation();
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

    public static <E extends Annotation> E getDeclaredAnnotation(@NonNull AnnotatedElement annotatedElement, @NonNull Class<E> annotationClass) {
        return getAnnotation(annotatedElement, annotationClass);
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

    public static Field getStaticField(@NonNull Class clazz, @NonNull String fieldName) {
        Field field = getDeclaredField(clazz, fieldName);
        if (field == null) {
            return null;
        }
        if (Modifiers.isStatic(field)) {
            return field;
        }
        return null;
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

    public static <V> V getPublicFieldValue(@NonNull Object object, @NonNull String fieldName, boolean throwException) {
        try {
            Field field = getPublicField(object.getClass(), fieldName);
            if (field == null) {
                if (throwException) {
                    throw new NoSuchFieldException(new ExceptionMessage("Can't find public field {0} in the class {1}", fieldName, object.getClass().getCanonicalName()).getMessage());
                }
                return null;
            } else {
                return (V) field.get(object);
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
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
        try {
            Field field = getDeclaredField(object.getClass(), fieldName);
            if (field == null) {
                if (throwException) {
                    throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1}", fieldName, object.getClass().getCanonicalName()).getMessage());
                }
                return null;
            } else {
                return getFieldValue(field, object, force, throwException);
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static <V> V getAnyFieldValueForcedIfPresent(@NonNull Object object, @NonNull String fieldName) {
        try {
            return getAnyFieldValue(object, fieldName, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V getAnyFieldValue(@NonNull Object object, @NonNull String fieldName, boolean force, boolean throwException) {
        try {
            Field field = getAnyField(object.getClass(), fieldName);
            if (field == null) {
                if (throwException) {
                    throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1} and its all super class", fieldName, object.getClass().getCanonicalName()).getMessage());
                }
                return null;
            } else {
                return getFieldValue(field, object, force, throwException);
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static <V> V getFieldValue(@NonNull Field field, @NonNull Object object, boolean force, boolean throwException) {
        try {
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
                // field.setAccessible(false);
                // ignore it
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static void setFieldValue(@NonNull Field field, @NonNull Object target, Object value, boolean force, boolean throwException) {
        try {
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
                    // field.setAccessible(false);
                    // ignore it
                }
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static void setPublicFieldValue(@NonNull Object object, @NonNull String fieldName, Object value, boolean force, boolean throwException) {
        try {
            Field field = getPublicField(object.getClass(), fieldName);
            if (field == null) {
                if (throwException) {
                    throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1} and its all super class", fieldName, object.getClass().getCanonicalName()).getMessage());
                }
            } else {
                setFieldValue(field, object, value, force, throwException);
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static void setDeclaredFieldValue(@NonNull Object object, @NonNull String fieldName, Object value, boolean force, boolean throwException) {
        try {
            Field field = getDeclaredField(object.getClass(), fieldName);
            if (field == null) {
                if (throwException) {
                    throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1} and its all super class", fieldName, object.getClass().getCanonicalName()).getMessage());
                }
            } else {
                setFieldValue(field, object, value, force, throwException);
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static void setAnyFieldValue(@NonNull Object object, @NonNull String fieldName, Object value, boolean force, boolean throwException) {
        try {


            Field field = getAnyField(object.getClass(), fieldName);
            if (field == null) {
                if (throwException) {
                    throw new NoSuchFieldException(new ExceptionMessage("Can't find a declared field {0} in the class {1} and its all super class", fieldName, object.getClass().getCanonicalName()).getMessage());
                }
            } else {
                setFieldValue(field, object, value, force, throwException);
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
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

    public static <V> V invokePublicMethod(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters, boolean force, boolean throwException) {
        try {
            Method method = getPublicMethod(object.getClass(), methodName, parameterTypes);
            if (method == null) {
                if (throwException) {
                    throw new NoSuchMethodException(new ExceptionMessage("Can't find the method: {0}", getMethodString(getFQNClassName(object.getClass()), methodName, null, parameterTypes)).getMessage());
                }
                return null;
            }
            return (V) invokeMethodOrNull(method, object, parameters, throwException);
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static <V> V invokeDeclaredMethodForcedIfPresent(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters) {
        try {
            return (V) invokeDeclaredMethod(object, methodName, parameterTypes, parameters, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V invokeDeclaredMethod(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters, boolean force, boolean throwException) {
        try {
            Method method = getDeclaredMethod(object.getClass(), methodName, parameterTypes);
            if (method == null) {
                if (throwException) {
                    throw new NoSuchMethodException(new ExceptionMessage("Can't find the method: {0}", getMethodString(getFQNClassName(object.getClass()), methodName, null, parameterTypes)).getMessage());
                }
                return null;
            }
            return (V) invokeMethodOrNull(method, object, parameters, throwException);
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static <V> V invokeAnyMethodForcedIfPresent(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters) {
        try {
            return (V) invokeAnyMethod(object, methodName, parameterTypes, parameters, true, false);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <V> V invokeAnyMethod(@NonNull Object object, @NonNull String methodName, @Nullable Class[] parameterTypes, @Nullable Object[] parameters, boolean force, boolean throwException) {
        try {
            Method method = getAnyMethod(object.getClass(), methodName, parameterTypes);
            if (method == null) {
                if (throwException) {
                    throw new NoSuchMethodException(new ExceptionMessage("Can't find the method: {0}", getMethodString(getFQNClassName(object.getClass()), methodName, null, parameterTypes)).getMessage());
                }
                return null;
            }
            return (V) invokeMethodOrNull(method, object, parameters, throwException);
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static <V> V invoke(@NonNull Method method, @Nullable Object object, @Nullable Object[] parameters, boolean force, boolean throwException) {
        try {
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
                // method.setAccessible(false);
                // ignore it
            }
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
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

    public static <V> V invokeAnyStaticMethod(String clazz, String methodName, Class[] parameterTypes, Object[] parameters, boolean force, boolean throwException) throws ClassNotFoundException {
        return invokeAnyStaticMethod(Class.forName(clazz), methodName, parameterTypes, parameters, force, throwException);
    }

    public static <V> V invokeAnyStaticMethod(Class clazz, String methodName, Class[] parameterTypes, Object[] parameters, boolean force, boolean throwException) {
        try {
            Method method = getAnyMethod(clazz, methodName, parameterTypes);
            if (Modifiers.isStatic(method)) {
                return invoke(method, null, parameters, force, throwException);
            }
            throw new NoSuchMethodException();
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
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
                // avoid clone
                Class[] params = parameterTypes;
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

    public static Set<Class<?>> getAllInterfaces(Class clazz) {
        final Set<Class<?>> set = Collects.emptyHashSet(true);
        Class[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            Collects.addAll(set, interfaces);
            Collects.forEach(interfaces, new Consumer<Class>() {
                @Override
                public void accept(Class iface) {
                    set.addAll(getAllInterfaces(iface));
                }
            });
        }
        return set;
    }

    public static Set<Class<?>> getAllSuperClass(Class clazz) {
        final Set<Class<?>> set = Collects.emptyHashSet(true);
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            set.add(superClass);
            set.addAll(getAllInterfaces(superClass));
        }
        return set;
    }


    public static <T> boolean isInstance(T object, String classFQN) {
        Preconditions.checkNotNull(object);
        Preconditions.checkNotNull(classFQN);
        Class clazz = object.getClass();
        Set<String> set = Collects.emptyHashSet(true);
        Pipeline.of(getAllInterfaces(clazz)).concat(getAllSuperClass(clazz)).map(new Mapper<Class<?>, String>() {
            @Override
            public String apply(Class<?> ifce) {
                return getFQNClassName(ifce);
            }
        }).addTo(set);

        return set.contains(getFQNClassName(clazz));
    }

    /**
     * This new method 'slightly' outperforms the old method, it was
     * essentially a perfect example of me wasting my time and a
     * premature optimization.  But what the hell...
     *
     * @param s -
     * @return String
     */
    public static String getSetter(String s) {
        char[] chars = new char[s.length() + 3];

        chars[0] = 's';
        chars[1] = 'e';
        chars[2] = 't';

        chars[3] = Chars.toUpperCase(s.charAt(0));

        for (int i = s.length() - 1; i != 0; i--) {
            chars[i + 3] = s.charAt(i);
        }

        return new String(chars);
    }


    public static String getGetter(String s) {
        char[] c = s.toCharArray();
        char[] chars = new char[c.length + 3];

        chars[0] = 'g';
        chars[1] = 'e';
        chars[2] = 't';

        chars[3] = Chars.toUpperCase(c[0]);

        arraycopy(c, 1, chars, 4, c.length - 1);

        return new String(chars);
    }


    public static String getIsGetter(String s) {
        char[] c = s.toCharArray();
        char[] chars = new char[c.length + 2];

        chars[0] = 'i';
        chars[1] = 's';

        chars[2] = Chars.toUpperCase(c[0]);

        arraycopy(c, 1, chars, 3, c.length - 1);

        return new String(chars);
    }

    public static Method getSetter(Class clazz, String field) {
        String setter = getSetter(field);

        for (Method method : clazz.getMethods()) {
            if (setter.equals(method.getName()) && Modifiers.isPublic(method) && method.getParameterTypes().length == 1) {
                return method;
            }
        }
        return null;
    }

    public static Method getSetter(Class clazz, String field, Class parameterType) {
        String setter = getSetter(field);
        Method method = getDeclaredMethod(clazz, setter, parameterType);
        if (method != null && Modifiers.isPublic(method)) {
            return method;
        }
        return null;
    }

    public static boolean hasGetter(Field field) {
        Method method = getGetter(field.getDeclaringClass(), field.getName());
        return method != null && field.getType().isAssignableFrom(method.getReturnType());
    }

    public static boolean hasSetter(Field field) {
        Method method = getSetter(field.getDeclaringClass(), field.getName());
        return method != null && field.getType().isAssignableFrom(method.getParameterTypes()[0]);
    }

    public static Method getGetter(Class clazz, String field) {
        String simple = "get" + field;
        String simpleIsGet = "is" + field;
        String isGet = getIsGetter(field);
        String getter = getGetter(field);

        Method candidate = null;

        if (Collection.class.isAssignableFrom(clazz) && "isEmpty".equals(isGet)) {
            try {
                return Collection.class.getMethod("isEmpty");
            } catch (NoSuchMethodException ignore) {
            }
        }

        for (Method meth : clazz.getMethods()) {
            if (Modifiers.isPublic(meth) && !Modifiers.isStatic(meth) && meth.getParameterTypes().length == 0) {
                String methodName = meth.getName();
                if ((getter.equals(methodName) || field.equals(methodName) || ((isGet.equals(methodName) || simpleIsGet.equals(methodName)) && meth.getReturnType() == boolean.class)
                        || simple.equals(methodName))) {
                    if (candidate == null || candidate.getReturnType().isAssignableFrom(meth.getReturnType())) {
                        candidate = meth;
                    }
                }
            }
        }
        return candidate;
    }

    public static boolean makeAccessible(Field field) {
        if ((!Modifiers.isPublic(field) || !Modifiers.isPublic(field.getDeclaringClass()) || Modifiers.isFinal(field)) && !field.isAccessible()) {
            try {
                field.setAccessible(true);
                return true;
            } catch (SecurityException ex) {
                return false;
            }
        }
        return false;
    }

    /**
     * Determine whether the given method is an "equals" method.
     *
     * @see java.lang.Object#equals(Object)
     */
    public static boolean isEqualsMethod(@Nullable Method method) {
        if (method == null || !method.getName().equals("equals")) {
            return false;
        }
        Class<?>[] paramTypes = method.getParameterTypes();
        return (paramTypes.length == 1 && paramTypes[0] == Object.class);
    }

    /**
     * Determine whether the given method is a "hashCode" method.
     *
     * @see java.lang.Object#hashCode()
     */
    public static boolean isHashCodeMethod(@Nullable Method method) {
        return (method != null && method.getName().equals("hashCode") && !method.isVarArgs() && method.getParameterTypes().length == 0);
    }

    /**
     * Determine whether the given method is a "toString" method.
     *
     * @see java.lang.Object#toString()
     */
    public static boolean isToStringMethod(@Nullable Method method) {
        return (method != null && method.getName().equals("toString") && !method.isVarArgs() && method.getParameterTypes().length == 0);
    }

    /**
     * Determine whether the given method is originally declared by {@link java.lang.Object}.
     */
    public static boolean isObjectMethod(@Nullable Method method) {
        return (method != null && (method.getDeclaringClass() == Object.class || isEqualsMethod(method) || isHashCodeMethod(method) || isToStringMethod(method)));
    }


    /**
     * Determine if the given class defines an {@link Object#equals} override.
     *
     * @param clazz The class to check
     * @return True if clazz defines an equals override.
     */
    public static boolean isOverrideEquals(Class clazz) {
        Method equals = getDeclaredMethod(clazz, "equals", Object.class);
        return !OBJECT_EQUALS.equals(equals);
    }

    /**
     * Determine if the given class defines a {@link Object#hashCode} override.
     *
     * @param clazz The class to check
     * @return True if clazz defines an hashCode override.
     */
    public static boolean isOverrideHashCode(Class clazz) {
        Method hashCode = getDeclaredMethod(clazz, "hashCode");
        return !OBJECT_HASHCODE.equals(hashCode);
    }

    /**
     * Determine if the given class implements the given interface.
     *
     * @param clazz The class to check
     * @param intf  The interface to check it against.
     * @return True if the class does implement the interface, false otherwise.
     */
    public static boolean isImplementsInterface(Class clazz, Class intf) {
        Preconditions.checkTrue(intf.isInterface(), "Interface to check was not an interface");
        return intf.isAssignableFrom(clazz);
    }


}
