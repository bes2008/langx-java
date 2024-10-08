package com.jn.langx.util.reflect.type;

import com.jn.langx.util.Preconditions;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * also see  the ParameterizedTypeImpl class in gson 2.9.8
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Type ownerType;
    private final Type rawType;
    private final Type[] typeArguments;

    public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
        // require an owner type if the raw type needs it
        if (rawType instanceof Class<?>) {
            Class<?> rawTypeAsClass = (Class<?>) rawType;
            boolean isStaticOrTopLevelClass = Modifier.isStatic(rawTypeAsClass.getModifiers())
                    || rawTypeAsClass.getEnclosingClass() == null;
            checkArgument(ownerType != null || isStaticOrTopLevelClass);
        }

        this.ownerType = ownerType == null ? null : canonicalize(ownerType);
        this.rawType = canonicalize(rawType);
        this.typeArguments = typeArguments.clone();
        for (int t = 0, length = this.typeArguments.length; t < length; t++) {
            Preconditions.checkNotNull(this.typeArguments[t]);
            checkNotPrimitive(this.typeArguments[t]);
            this.typeArguments[t] = canonicalize(this.typeArguments[t]);
        }
    }

    static void checkNotPrimitive(Type type) {
        checkArgument(!(type instanceof Class<?>) || !((Class<?>) type).isPrimitive());
    }


    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Class<?> c = (Class<?>) type;
            return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;

        } else if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            return new ParameterizedTypeImpl(p.getOwnerType(),
                    p.getRawType(), p.getActualTypeArguments());

        } else if (type instanceof GenericArrayType) {
            GenericArrayType g = (GenericArrayType) type;
            return new GenericArrayTypeImpl(g.getGenericComponentType());

        } else if (type instanceof WildcardType) {
            WildcardType w = (WildcardType) type;
            return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());

        } else {
            // type is either serializable as-is or unsupported
            return type;
        }
    }

    static final Type[] EMPTY_TYPE_ARRAY = new Type[]{};

    public static final class WildcardTypeImpl implements WildcardType, Serializable {
        private final transient Type upperBound;
        private final transient Type lowerBound;

        public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            checkArgument(lowerBounds.length <= 1);
            checkArgument(upperBounds.length == 1);

            if (lowerBounds.length == 1) {
                Preconditions.checkNotNull(lowerBounds[0]);
                checkNotPrimitive(lowerBounds[0]);
                checkArgument(upperBounds[0] == Object.class);
                this.lowerBound = canonicalize(lowerBounds[0]);
                this.upperBound = Object.class;

            } else {
                Preconditions.checkNotNull(upperBounds[0]);
                checkNotPrimitive(upperBounds[0]);
                this.lowerBound = null;
                this.upperBound = canonicalize(upperBounds[0]);
            }
        }

        @Override
        public Type[] getUpperBounds() {
            return new Type[]{upperBound};
        }

        @Override
        public Type[] getLowerBounds() {
            return lowerBound != null ? new Type[]{lowerBound} : EMPTY_TYPE_ARRAY;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof WildcardType
                    && Types.equals(this, (WildcardType) other);
        }

        @Override
        public int hashCode() {
            return (lowerBound != null ? 31 + lowerBound.hashCode() : 1)
                    ^ (31 + upperBound.hashCode());
        }

        @Override
        public String toString() {
            if (lowerBound != null) {
                return "? super " + typeToString(lowerBound);
            } else if (upperBound == Object.class) {
                return "?";
            } else {
                return "? extends " + typeToString(upperBound);
            }
        }

        private static final long serialVersionUID = 0;
    }

    static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }


    static String typeToString(Type type) {
        return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
    }

    static int hashCodeOrZero(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    public static final class GenericArrayTypeImpl implements GenericArrayType {
        private final Type componentType;

        public GenericArrayTypeImpl(Type componentType) {
            this.componentType = canonicalize(componentType);
        }

        @Override
        public Type getGenericComponentType() {
            return componentType;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof GenericArrayType
                    && Types.equals(this, (GenericArrayType) o);
        }

        @Override
        public int hashCode() {
            return componentType.hashCode();
        }

        @Override
        public String toString() {
            return typeToString(componentType) + "[]";
        }

    }

    @Override
    public Type[] getActualTypeArguments() {
        return typeArguments.clone();
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ParameterizedType
                && Types.equals(this, (ParameterizedType) other);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(typeArguments)
                ^ rawType.hashCode()
                ^ hashCodeOrZero(ownerType);
    }

    @Override
    public String toString() {
        int length = typeArguments.length;
        if (length == 0) {
            return typeToString(rawType);
        }

        StringBuilder stringBuilder = new StringBuilder(30 * (length + 1));
        stringBuilder.append(typeToString(rawType)).append("<").append(typeToString(typeArguments[0]));
        for (int i = 1; i < length; i++) {
            stringBuilder.append(", ").append(typeToString(typeArguments[i]));
        }
        return stringBuilder.append(">").toString();
    }

}
