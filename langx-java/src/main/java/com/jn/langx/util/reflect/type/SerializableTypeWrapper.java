package com.jn.langx.util.reflect.type;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.parameter.MethodParameter;

/**
 * Internal utility class that can be used to obtain wrapped {@link Serializable}
 * variants of {@link java.lang.reflect.Type java.lang.reflect.Types}.
 *
 * <p>{@link #forField(Field) Fields} or {@link #forMethodParameter(MethodParameter)
 * MethodParameters} can be used as the root source for a serializable type.
 * Alternatively, a regular {@link Class} can also be used as source.
 *
 * <p>The returned type will either be a {@link Class} or a serializable proxy of
 * {@link GenericArrayType}, {@link ParameterizedType}, {@link TypeVariable} or
 * {@link WildcardType}. With the exception of {@link Class} (which is final) calls
 * to methods that return further {@link Type Types} (for example
 * {@link GenericArrayType#getGenericComponentType()}) will be automatically wrapped.
 *
 * @author Phillip Webb
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 4.0
 */
final class SerializableTypeWrapper {

    private static final Class<?>[] SUPPORTED_SERIALIZABLE_TYPES = {
            GenericArrayType.class,
            ParameterizedType.class,
            TypeVariable.class,
            WildcardType.class
    };

    static final ConcurrentReferenceHashMap<Type, Type> cache = new ConcurrentReferenceHashMap<Type, Type>(256);


    private SerializableTypeWrapper() {
    }


    /**
     * Return a {@link Serializable} variant of {@link Field#getGenericType()}.
     */
    @Nullable
    public static Type forField(Field field) {
        return forTypeProvider(new FieldTypeProvider(field));
    }

    /**
     * Return a {@link Serializable} variant of
     * {MethodParameter#getGenericParameterType()}.
     */
    @Nullable
    public static Type forMethodParameter(MethodParameter methodParameter) {
        return forTypeProvider(new MethodParameterTypeProvider(methodParameter));
    }

    /**
     * Unwrap the given type, effectively returning the original non-serializable type.
     * @param type the type to unwrap
     * @return the original non-serializable type
     */
    @SuppressWarnings("unchecked")
    public static <T extends Type> T unwrap(T type) {
        Type unwrapped = null;
        if (type instanceof SerializableTypeProxy) {
            unwrapped = ((SerializableTypeProxy) type).getTypeProvider().getType();
        }
        return (unwrapped != null ? (T) unwrapped : type);
    }

    /**
     * Return a {@link Serializable} {@link Type} backed by a {@link TypeProvider} .
     * <p>If type artifacts are generally not serializable in the current runtime
     * environment, this delegate will simply return the original {@code Type} as-is.
     */
    @Nullable
    static Type forTypeProvider(TypeProvider provider) {
        Type providedType = provider.getType();
        if (providedType == null || providedType instanceof Serializable) {
            // No serializable type wrapping necessary (e.g. for java.lang.Class)
            return providedType;
        }
        if (Platform.inImageCode() || !Serializable.class.isAssignableFrom(Class.class)) {
            // Let's skip any wrapping attempts if types are generally not serializable in
            // the current runtime environment (even java.lang.Class itself, e.g. on Graal)
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
                Class<?>[] interfaces = new Class<?>[] {type, SerializableTypeProxy.class, Serializable.class};
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
    @SuppressWarnings("serial")
    interface TypeProvider extends Serializable {

        /**
         * Return the (possibly non {@link Serializable}) {@link Type}.
         */
        @Nullable
        Type getType();

        /**
         * Return the source of the type, or {@code null} if not known.
         * <p>The default implementations returns {@code null}.
         */
        @Nullable
        Object getSource();
    }


    /**
     * {@link Serializable} {@link InvocationHandler} used by the proxied {@link Type}.
     * Provides serialization support and enhances any methods that return {@code Type}
     * or {@code Type[]}.
     */
    @SuppressWarnings("serial")
    private static class TypeProxyInvocationHandler implements InvocationHandler, Serializable {

        private final TypeProvider provider;

        public TypeProxyInvocationHandler(TypeProvider provider) {
            this.provider = provider;
        }

        @Override
        @Nullable
        public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
            if (method.getName().equals("equals") && args != null) {
                Object other = args[0];
                // Unwrap proxies for speed
                if (other instanceof Type) {
                    other = unwrap((Type) other);
                }
                return Objs.equals(this.provider.getType(), other);
            }
            else if (method.getName().equals("hashCode")) {
                return Objs.hashCode(this.provider.getType());
            }
            else if (method.getName().equals("getTypeProvider")) {
                return this.provider;
            }

            if (Type.class == method.getReturnType() && args == null) {
                return forTypeProvider(new MethodInvokeTypeProvider(this.provider, method, -1));
            }
            else if (Type[].class == method.getReturnType() && args == null) {
                Type[] result = new Type[((Type[]) method.invoke(this.provider.getType())).length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = forTypeProvider(new MethodInvokeTypeProvider(this.provider, method, i));
                }
                return result;
            }

            try {
                return method.invoke(this.provider.getType(), args);
            }
            catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }


    /**
     * {@link TypeProvider} for {@link Type Types} obtained from a {@link Field}.
     */
    @SuppressWarnings("serial")
    static class FieldTypeProvider implements TypeProvider {

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
            try {
                this.field = this.declaringClass.getDeclaredField(this.fieldName);
            }
            catch (Throwable ex) {
                throw new IllegalStateException("Could not find original class structure", ex);
            }
        }
    }


    /**
     * {@link TypeProvider} for {@link Type Types} obtained from a {@link MethodParameter}.
     */
    @SuppressWarnings("serial")
    static class MethodParameterTypeProvider implements TypeProvider {

        private MethodParameter methodParameter;

        public MethodParameterTypeProvider(MethodParameter methodParameter) {
            this.methodParameter = methodParameter;
        }

        @Override
        public Type getType() {
            return this.methodParameter.getParameterizedType();
        }

        @Override
        public Object getSource() {
            return this.methodParameter;
        }

        private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
            inputStream.defaultReadObject();
        }
    }


    /**
     * {@link TypeProvider} for {@link Type Types} obtained by invoking a no-arg method.
     */
    @SuppressWarnings("serial")
    static class MethodInvokeTypeProvider implements TypeProvider {

        private final TypeProvider provider;

        private final String methodName;

        private final Class<?> declaringClass;

        private final int index;

        private transient Method method;

        @Nullable
        private transient volatile Object result;

        public MethodInvokeTypeProvider(TypeProvider provider, Method method, int index) {
            this.provider = provider;
            this.methodName = method.getName();
            this.declaringClass = method.getDeclaringClass();
            this.index = index;
            this.method = method;
        }

        @Override
        @Nullable
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
        @Nullable
        public Object getSource() {
            return null;
        }

        private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
            inputStream.defaultReadObject();
            Method method = Reflects.findMethod(this.declaringClass, this.methodName);
            if (method == null) {
                throw new IllegalStateException("Cannot find method on deserialization: " + this.methodName);
            }
            if (method.getReturnType() != Type.class && method.getReturnType() != Type[].class) {
                throw new IllegalStateException(
                        "Invalid return type on deserialized method - needs to be Type or Type[]: " + method);
            }
            this.method = method;
        }
    }

}
