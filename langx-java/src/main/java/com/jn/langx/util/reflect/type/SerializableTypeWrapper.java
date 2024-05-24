package com.jn.langx.util.reflect.type;


import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.parameter.MethodParameter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.*;


/**
 * Internal utility class that can be used to obtain wrapped {@link Serializable}
 * variants of {@link java.lang.reflect.Type}s.
 *
 * <p>{@link #forField(Field) Fields} or {@link #forMethodParameter(SimpleParameter)
 * MethodParameters} can be used as the root source for a serializable type.
 * Alternatively the {@link #forGenericSuperclass(Class) superclass},
 * {@link #forGenericInterfaces(Class) interfaces} or {@link #forTypeParameters(Class)
 * type parameters} or a regular {@link Class} can also be used as source.
 *
 * <p>The returned type will either be a {@link Class} or a serializable proxy of
 * {@link GenericArrayType}, {@link ParameterizedType}, {@link TypeVariable} or
 * {@link WildcardType}. With the exception of {@link Class} (which is final) calls
 * to methods that return further {@link Type}s (for example
 * {@link GenericArrayType#getGenericComponentType()}) will be automatically wrapped.
 *
 * @since 4.3.7
 */
public abstract class SerializableTypeWrapper {

    private static final Class<?>[] SUPPORTED_SERIALIZABLE_TYPES = {
            GenericArrayType.class, ParameterizedType.class, TypeVariable.class, WildcardType.class};

    protected static final ConcurrentReferenceHashMap<Type, Type> cache = new ConcurrentReferenceHashMap<Type, Type>(256);


    /**
     * Return a {@link Serializable} variant of {@link Field#getGenericType()}.
     */
    public static Type forField(Field field) {
        return forTypeProvider(new FieldTypeProvider(field));
    }

    /**
     * Return a {@link Serializable} variant of
     * {@link SimpleParameter#getGenericParameterType()}.
     */
    public static Type forMethodParameter(SimpleParameter methodParameter) {
        return forTypeProvider(new MethodParameterTypeProvider(methodParameter));
    }

    /**
     * Return a {@link Serializable} variant of {@link Class#getGenericSuperclass()}.
     */
    @SuppressWarnings("serial")
    public static Type forGenericSuperclass(final Class<?> type) {
        return forTypeProvider(new SimpleTypeProvider() {
            @Override
            public Type getType() {
                return type.getGenericSuperclass();
            }
        });
    }

    /**
     * Return a {@link Serializable} variant of {@link Class#getGenericInterfaces()}.
     */
    @SuppressWarnings("serial")
    public static Type[] forGenericInterfaces(final Class<?> type) {
        Type[] result = new Type[type.getGenericInterfaces().length];
        for (int i = 0; i < result.length; i++) {
            final int index = i;
            result[i] = forTypeProvider(new SimpleTypeProvider() {
                @Override
                public Type getType() {
                    return type.getGenericInterfaces()[index];
                }
            });
        }
        return result;
    }

    /**
     * Return a {@link Serializable} variant of {@link Class#getTypeParameters()}.
     */
    public static Type[] forTypeParameters(final Class<?> type) {
        Type[] result = new Type[type.getTypeParameters().length];
        for (int i = 0; i < result.length; i++) {
            final int index = i;
            result[i] = forTypeProvider(new SimpleTypeProvider() {
                @Override
                public Type getType() {
                    return type.getTypeParameters()[index];
                }
            });
        }
        return result;
    }

    /**
     * Unwrap the given type, effectively returning the original non-serializable type.
     *
     * @param type the type to unwrap
     * @return the original non-serializable type
     */
    @SuppressWarnings("unchecked")
    public static <T extends Type> T unwrap(T type) {
        Type unwrapped = type;
        while (unwrapped instanceof SerializableTypeProxy) {
            unwrapped = ((SerializableTypeProxy) type).getTypeProvider().getType();
        }
        return (T) unwrapped;
    }

    /**
     * Return a {@link Serializable} {@link Type} backed by a {@link TypeProvider} .
     */
    public static Type forTypeProvider(TypeProvider provider) {
        Type providedType = provider.getType();
        if (providedType == null || providedType instanceof Serializable) {
            // No serializable type wrapping necessary (e.g. for java.lang.Class)
            return providedType;
        }

        // Obtain a serializable type proxy for the given provider...
        Type cached = cache.get(providedType);
        if (cached != null) {
            return cached;
        }
        for (Class<?> type : SUPPORTED_SERIALIZABLE_TYPES) {
            if (type.isInstance(providedType)) {
                ClassLoader classLoader = provider.getClass().getClassLoader();
                Class<?>[] interfaces = new Class<?>[]{type, SerializableTypeProxy.class, Serializable.class};
                InvocationHandler handler = new TypeProxyInvocationHandler(provider);
                cached = (Type) Proxy.newProxyInstance(classLoader, interfaces, handler);
                cache.put(providedType, cached);
                return cached;
            }
        }
        throw new IllegalArgumentException("Unsupported Type class: " + providedType.getClass().getName());
    }


    /**
     * Additional interface implemented by the type proxy.
     */
    interface SerializableTypeProxy {

        /**
         * Return the underlying type provider.
         */
        TypeProvider getTypeProvider();
    }


    /**
     * A {@link Serializable} interface providing access to a {@link Type}.
     */
    public interface TypeProvider extends Serializable {

        /**
         * Return the (possibly non {@link Serializable}) {@link Type}.
         */
        Type getType();

        /**
         * Return the source of the type or {@code null}.
         */
        Object getSource();
    }


    /**
     * Base implementation of {@link TypeProvider} with a {@code null} source.
     */
    @SuppressWarnings("serial")
    private abstract static class SimpleTypeProvider implements TypeProvider {

        @Override
        public Object getSource() {
            return null;
        }
    }


    /**
     * {@link Serializable} {@link InvocationHandler} used by the proxied {@link Type}.
     * Provides serialization support and enhances any methods that return {@code Type}
     * or {@code Type[]}.
     */
    @SuppressWarnings("serial")
    private static final class TypeProxyInvocationHandler implements InvocationHandler, Serializable {

        private final TypeProvider provider;

        public TypeProxyInvocationHandler(TypeProvider provider) {
            this.provider = provider;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("equals")) {
                Object other = args[0];
                // Unwrap proxies for speed
                if (other instanceof Type) {
                    other = unwrap((Type) other);
                }
                return this.provider.getType().equals(other);
            } else if (method.getName().equals("hashCode")) {
                return this.provider.getType().hashCode();
            } else if (method.getName().equals("getTypeProvider")) {
                return this.provider;
            }

            if (Type.class == method.getReturnType() && args == null) {
                return forTypeProvider(new MethodInvokeTypeProvider(this.provider, method, -1));
            } else if (Type[].class == method.getReturnType() && args == null) {
                Object obj = this.provider.getType();
                Type[] types = Reflects.<Type[]>invokeMethod(method, obj, args);
                Type[] result = new Type[types.length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = forTypeProvider(new MethodInvokeTypeProvider(this.provider, method, i));
                }
                return result;
            }

            return Reflects.invokeMethod(method, this.provider.getType(), args);
        }
    }


    /**
     * {@link TypeProvider} for {@link Type}s obtained from a {@link Field}.
     */
    @SuppressWarnings("serial")
    public static final class FieldTypeProvider implements TypeProvider {

        private final String fieldName;

        private final Class<?> declaringClass;

        private transient Field field;

        public FieldTypeProvider(Field field) {
            this.fieldName = field.getName();
            this.declaringClass = field.getDeclaringClass();
            this.field = field;
        }

        @Override
        public Type getType() {
            return this.field.getGenericType();
        }

        @Override
        public Object getSource() {
            return this.field;
        }

        private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
            inputStream.defaultReadObject();
            this.field = Reflects.getDeclaredField(this.declaringClass, this.fieldName);
            if (this.field == null) {
                throw new IllegalStateException(StringTemplates.formatWithPlaceholder("Could not find field {} in class {}", this.fieldName, Reflects.getFQNClassName(this.declaringClass)));
            }
        }
    }


    /**
     * {@link TypeProvider} for {@link Type}s obtained from a {@link MethodParameter}.
     */
    public static final class MethodParameterTypeProvider implements TypeProvider {
        private final String methodName;

        private final Class<?>[] parameterTypes;

        private final Class<?> declaringClass;

        private final int parameterIndex;

        private transient SimpleParameter methodParameter;

        public MethodParameterTypeProvider(SimpleParameter methodParameter) {
            if (methodParameter.getMethod() != null) {
                this.methodName = methodParameter.getMethod().getName();
                this.parameterTypes = methodParameter.getMethod().getParameterTypes();
            } else {
                this.methodName = null;
                this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
            }
            this.declaringClass = methodParameter.getDeclaringClass();
            this.parameterIndex = methodParameter.getParameterIndex();
            this.methodParameter = methodParameter;
        }


        @Override
        public Type getType() {
            return this.methodParameter.getGenericParameterType();
        }

        @Override
        public Object getSource() {
            return this.methodParameter;
        }

        private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
            inputStream.defaultReadObject();
            try {
                if (this.methodName != null) {
                    this.methodParameter = new SimpleParameter(Reflects.getDeclaredMethod(this.declaringClass, this.methodName, this.parameterTypes)
                            , this.parameterIndex);
                } else {
                    this.methodParameter = new SimpleParameter(
                            this.declaringClass.getDeclaredConstructor(this.parameterTypes), this.parameterIndex);
                }
            } catch (Throwable ex) {
                throw new IllegalStateException("Could not find original class structure", ex);
            }
        }
    }


    /**
     * {@link TypeProvider} for {@link Type}s obtained by invoking a no-arg method.
     */
    @SuppressWarnings("serial")
    public static final class MethodInvokeTypeProvider implements TypeProvider {

        private final TypeProvider provider;

        private final String methodName;

        private final Class<?> declaringClass;

        private final int index;

        private transient Method method;

        private transient Object result;

        public MethodInvokeTypeProvider(TypeProvider provider, Method method, int index) {
            this.provider = provider;
            this.methodName = method.getName();
            this.declaringClass = method.getDeclaringClass();
            this.index = index;
            this.method = method;
        }

        @Override
        public Type getType() {
            Object result = this.result;
            if (result == null) {
                // Lazy invocation of the target method on the provided type
                result = Reflects.invoke(this.method, this.provider.getType(), Emptys.EMPTY_OBJECTS, true, true);
                // Cache the result for further calls to getType()
                this.result = result;
            }
            return (result instanceof Type[] ? ((Type[]) result)[this.index] : (Type) result);
        }

        @Override
        public Object getSource() {
            return null;
        }

        private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
            inputStream.defaultReadObject();
            this.method = Reflects.findMethod(this.declaringClass, this.methodName);
            if (this.method!=null && this.method.getReturnType() != Type.class && this.method.getReturnType() != Type[].class) {
                throw new IllegalStateException(
                        "Invalid return type on deserialized method - needs to be Type or Type[]: " + this.method);
            }
        }
    }

}
