package com.jn.langx.util.reflect.type;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.Modifiers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.signature.TypeSignatures;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author jinuo.fang
 */
public class Types {

    private static final Type[] EMPTY_TYPE_ARRAY = new Type[]{};

    /**
     * judge a type is a primitive type or not
     */
    public static boolean isPrimitive(Type type) {
        return Primitives.isPrimitive(type);
    }

    /**
     * judge a type is a array or not
     */
    public static boolean isArray(Type type) {
        return isClass(type) && ((Class) type).isArray();
    }

    /**
     * judge a type is a class or not
     */
    public static boolean isClass(Type type) {
        return type instanceof Class;
    }

    /**
     * judge a type is a ParameterizedType
     */
    public static boolean isParameterizedType(Type type) {
        return (type instanceof ParameterizedType);
    }

    /**
     * get the wrap class for a primitive type
     */
    public static Class getPrimitiveWrapClass(Type type) {
        return Primitives.wrap(type);
    }
    private Types(){}
    /**
     * show a type as a string, for examples:
     */
    public static String typeToString(Type type) {
        if (type instanceof Class) {
            @SuppressWarnings("rawtypes") Class ctype = (Class) type;
            if (ctype.isArray()) {
                try {
                    Class cl = ctype;
                    int dimensions = 0;
                    while (cl.isArray()) {
                        dimensions++;
                        cl = cl.getComponentType();
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(cl.getName());
                    for (int i = 0; i < dimensions; i++) {
                        sb.append("[]");
                    }
                    return sb.toString();
                } catch (Throwable e) { /*FALLTHRU*/ }
            } else {
                if (Primitives.isPrimitiveOrPrimitiveWrapperType(ctype) || Modifiers.isInterface(ctype) || Modifiers.isAbstract(ctype)) {
                    return Reflects.getFQNClassName(ctype);
                }
                TypeVariable<Class<?>>[] typeParameters = ctype.getTypeParameters();
                if (Objs.isEmpty(typeParameters)) {
                    return Reflects.getFQNClassName(ctype);
                }
                return Reflects.getFQNClassName(ctype);
            }
        }
        return type.toString();
    }

    /**
     * get the type's signature in jvm runtime
     *
     * @param typeString type FQN
     * @return signature
     */
    public static String getTypeSignature(@NonNull String typeString) {
        return TypeSignatures.toTypeSignature(typeString);
    }


    /**
     * Returns an array type whose elements are all instances of
     * {@code componentType}.
     *
     * @return a {@link java.io.Serializable serializable} generic array type.
     */
    public static GenericArrayType arrayOf(Type componentType) {
        return new ParameterizedTypeImpl.GenericArrayTypeImpl(componentType);
    }

    /**
     * Returns true if {@code a} and {@code b} are equal.
     */
    public static boolean equals(Type a, Type b) {
        if (a == b) {
            // also handles (a == null && b == null)
            return true;

        } else if (a instanceof Class) {
            // Class already specifies equals().
            return a.equals(b);

        } else if (a instanceof ParameterizedType) {
            if (!(b instanceof ParameterizedType)) {
                return false;
            }

            ParameterizedType pa = (ParameterizedType) a;
            ParameterizedType pb = (ParameterizedType) b;
            return Objs.equals(pa.getOwnerType(), pb.getOwnerType())
                    && pa.getRawType().equals(pb.getRawType())
                    && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());

        } else if (a instanceof GenericArrayType) {
            if (!(b instanceof GenericArrayType)) {
                return false;
            }

            GenericArrayType ga = (GenericArrayType) a;
            GenericArrayType gb = (GenericArrayType) b;
            return equals(ga.getGenericComponentType(), gb.getGenericComponentType());

        } else if (a instanceof WildcardType) {
            if (!(b instanceof WildcardType)) {
                return false;
            }

            WildcardType wa = (WildcardType) a;
            WildcardType wb = (WildcardType) b;
            return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds())
                    && Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());

        } else if (a instanceof TypeVariable) {
            if (!(b instanceof TypeVariable)) {
                return false;
            }
            TypeVariable<?> va = (TypeVariable<?>) a;
            TypeVariable<?> vb = (TypeVariable<?>) b;
            return va.getGenericDeclaration() == vb.getGenericDeclaration()
                    && va.getName().equals(vb.getName());

        } else {
            // This isn't a type we support. Could be a generic array type, wildcard type, etc.
            return false;
        }
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            // type is a normal class.
            return (Class<?>) type;

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class.
            // Neal isn't either but suspects some pathological case related
            // to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            Preconditions.checkTrue(rawType instanceof Class);
            return (Class<?>) rawType;

        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();

        } else if (type instanceof TypeVariable) {
            // we could use the variable's bounds, but that won't work if there are multiple.
            // having a raw type that's more general than necessary is okay
            return Object.class;

        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);

        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + className);
        }
    }

    /**
     * convert any type to Class
     *
     */
    public static Class<?> toClass(Type o) {
        if (o instanceof GenericArrayType) {
            return Array.newInstance(toClass(((GenericArrayType) o).getGenericComponentType()),
                    0)
                    .getClass();
        }
        if (isPrimitive(o)) {
            return getPrimitiveWrapClass(o);
        }
        if (isClass(o)) {
            return (Class<?>) o;
        }
        if (isParameterizedType(o)) {
            ParameterizedType type = (ParameterizedType) o;
            return getParameterizedTypeWithOwnerType(type.getOwnerType(), type.getRawType(), type.getActualTypeArguments()).getClass();
        }
        throw new IllegalArgumentException();
    }

    /**
     * <pre>
     *     List<E>
     * </pre>
     */
    public static ParameterizedType getSetParameterizedType(Type elementType) {
        return getParameterizedType(Set.class, elementType);
    }

    /**
     * <pre>
     *     List<E>
     * </pre>
     */
    public static ParameterizedType getListParameterizedType(Type elementType) {
        return getParameterizedType(List.class, elementType);
    }

    /**
     * <pre>
     *     Map<K,V>
     * </pre>
     */
    public static ParameterizedType getMapParameterizedType(Type keyType, Type valueType) {
        return getParameterizedType(Map.class, keyType, valueType);
    }

    public static ParameterizedType getParameterizedType(Type rawType) {
        return new ParameterizedTypeImpl(null, rawType);
    }

    /**
     * @see #getListParameterizedType(Type)
     * @see #getMapParameterizedType(Type, Type)
     */
    public static ParameterizedType getParameterizedType(Type rawType, Type... typeArguments) {
        return getParameterizedTypeWithOwnerType(null, rawType, typeArguments);
    }

    public static ParameterizedType getParameterizedTypeWithOwnerType(Type ownerType, Type rawType, Type... typeArguments) {
        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    }

    public static Type checkPrimitiveArray(GenericArrayType genericArrayType) {
        Type clz = genericArrayType;
        Type genericComponentType = genericArrayType.getGenericComponentType();

        StringBuilder prefix = new StringBuilder("[");
        while (genericComponentType instanceof GenericArrayType) {
            genericComponentType = ((GenericArrayType) genericComponentType).getGenericComponentType();
            prefix.append("[");
        }

        if (genericComponentType instanceof Class<?>) {
            Class<?> ck = (Class<?>) genericComponentType;
            if (ck.isPrimitive()) {
                try {
                    if (ck == boolean.class) {
                        clz = Class.forName(prefix + "Z");
                    } else if (ck == char.class) {
                        clz = Class.forName(prefix + "C");
                    } else if (ck == byte.class) {
                        clz = Class.forName(prefix + "B");
                    } else if (ck == short.class) {
                        clz = Class.forName(prefix + "S");
                    } else if (ck == int.class) {
                        clz = Class.forName(prefix + "I");
                    } else if (ck == long.class) {
                        clz = Class.forName(prefix + "J");
                    } else if (ck == float.class) {
                        clz = Class.forName(prefix + "F");
                    } else if (ck == double.class) {
                        clz = Class.forName(prefix + "D");
                    }
                } catch (ClassNotFoundException e) {
                    // ignore it
                }
            }
        }

        return clz;
    }

    /**
     * @param toResolve      将要被解析的类型
     */
    public static Type resolve(Type context, Class<?> contextRawType, Type toResolve) {
        // This implementation is made a little more complicated in an attempt to avoid object-creation.
        while (true) {
            if (toResolve instanceof TypeVariable) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) toResolve;
                toResolve = resolveTypeVariable(context, contextRawType, typeVariable);
                if (toResolve == typeVariable) {
                    return toResolve;
                }

            } else if (toResolve instanceof Class && ((Class<?>) toResolve).isArray()) {
                Class<?> original = (Class<?>) toResolve;
                Type componentType = original.getComponentType();
                Type newComponentType = resolve(context, contextRawType, componentType);
                return componentType == newComponentType
                        ? original
                        : arrayOf(newComponentType);

            } else if (toResolve instanceof GenericArrayType) {
                GenericArrayType original = (GenericArrayType) toResolve;
                Type componentType = original.getGenericComponentType();
                Type newComponentType = resolve(context, contextRawType, componentType);
                return componentType == newComponentType
                        ? original
                        : arrayOf(newComponentType);

            } else if (toResolve instanceof ParameterizedType) {
                ParameterizedType original = (ParameterizedType) toResolve;
                Type ownerType = original.getOwnerType();
                Type newOwnerType = resolve(context, contextRawType, ownerType);
                boolean changed = newOwnerType != ownerType;

                Type[] args = original.getActualTypeArguments();
                for (int t = 0, length = args.length; t < length; t++) {
                    Type resolvedTypeArgument = resolve(context, contextRawType, args[t]);
                    if (resolvedTypeArgument != args[t]) {
                        if (!changed) {
                            args = args.clone();
                            changed = true;
                        }
                        args[t] = resolvedTypeArgument;
                    }
                }

                return changed
                        ? new ParameterizedTypeImpl(newOwnerType, original.getRawType(), args)
                        : original;

            } else if (toResolve instanceof WildcardType) {
                WildcardType original = (WildcardType) toResolve;
                Type[] originalLowerBound = original.getLowerBounds();
                Type[] originalUpperBound = original.getUpperBounds();

                if (originalLowerBound.length == 1) {
                    Type lowerBound = resolve(context, contextRawType, originalLowerBound[0]);
                    if (lowerBound != originalLowerBound[0]) {
                        return supertypeOf(lowerBound);
                    }
                } else if (originalUpperBound.length == 1) {
                    Type upperBound = resolve(context, contextRawType, originalUpperBound[0]);
                    if (upperBound != originalUpperBound[0]) {
                        return subtypeOf(upperBound);
                    }
                }
                return original;

            } else {
                return toResolve;
            }
        }
    }

    /**
     * Returns the declaring class of {@code typeVariable}, or {@code null} if it was not declared by
     * a class.
     */
    private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        return genericDeclaration instanceof Class ? (Class<?>) genericDeclaration : null;
    }

    private static int indexOf(Object[] array, Object toFind) {
        for (int i = 0; i < array.length; i++) {
            if (toFind.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    private static Type resolveTypeVariable(Type context, Class<?> contextRawType, TypeVariable<?> unknown) {
        Class<?> declaredByRaw = declaringClassOf(unknown);

        // We can't reduce this further.
        if (declaredByRaw == null) {
            return unknown;
        }

        Type declaredBy = getGenericSupertype(context, contextRawType, declaredByRaw);
        if (declaredBy instanceof ParameterizedType) {
            int index = indexOf(declaredByRaw.getTypeParameters(), unknown);
            return ((ParameterizedType) declaredBy).getActualTypeArguments()[index];
        }

        return unknown;
    }

    /**
     * Returns the generic supertype for {@code supertype}. For example, given a class {@code
     * IntegerSet}, the result for when supertype is {@code Set.class} is {@code Set<Integer>} and the
     * result when the supertype is {@code Collection.class} is {@code Collection<Integer>}.
     */
    public static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
        if (toResolve == rawType) {
            return context;
        }

        // we skip searching through interfaces if unknown is an interface
        if (toResolve.isInterface()) {
            Class<?>[] interfaces = rawType.getInterfaces();
            for (int i = 0, length = interfaces.length; i < length; i++) {
                if (interfaces[i] == toResolve) {
                    return rawType.getGenericInterfaces()[i];
                } else if (Reflects.isSubClassOrEquals(toResolve, interfaces[i])) {
                    return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
                }
            }
        }

        // check our supertypes
        if (!rawType.isInterface()) {
            while (rawType != Object.class) {
                Class<?> rawSupertype = rawType.getSuperclass();
                if (rawSupertype == toResolve) {
                    return rawType.getGenericSuperclass();
                } else if (Reflects.isSubClassOrEquals(toResolve, rawSupertype)) {
                    return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
                }
                rawType = rawSupertype;
            }
        }

        // we can't resolve this further
        return toResolve;
    }

    /**
     * Returns a type that represents an unknown type that extends {@code bound}. For example, if
     * {@code bound} is {@code CharSequence.class}, this returns {@code ? extends CharSequence}. If
     * {@code bound} is {@code Object.class}, this returns {@code ?}, which is shorthand for {@code
     * ? extends Object}.
     */
    public static WildcardType subtypeOf(Type bound) {
        return new ParameterizedTypeImpl.WildcardTypeImpl(new Type[]{bound}, EMPTY_TYPE_ARRAY);
    }

    /**
     * Returns a type that represents an unknown supertype of {@code bound}. For example, if {@code
     * bound} is {@code String.class}, this returns {@code ? super String}.
     */
    public static WildcardType supertypeOf(Type bound) {
        return new ParameterizedTypeImpl.WildcardTypeImpl(new Type[]{Object.class}, new Type[]{bound});
    }

    /**
     * 判断是否为 字面量类型
     */
    public static boolean isLiteralType(Type type) {
        if (Primitives.isPrimitive(type) || Primitives.isWrapperType(type)) {
            return true;
        }
        if (isClass(type)) {
            return type == String.class;
        }
        return false;
    }


    /**
     * Returns the generic form of {@code supertype}. For example, if this is {@code
     * ArrayList<String>}, this returns {@code Iterable<String>} given the input {@code
     * Iterable.class}.
     *
     * @param supertype a superclass of, or interface implemented by, this.
     */
    static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
        if (context instanceof WildcardType) {
            // wildcards are useless for resolving supertypes. As the upper bound has the same raw type, use it instead
            context = ((WildcardType) context).getUpperBounds()[0];
        }
        Preconditions.checkArgument(supertype.isAssignableFrom(contextRawType));
        return resolve(context, contextRawType, getGenericSupertype(context, contextRawType, supertype));
    }

    /**
     * Returns the component type of this array type.
     *
     * @throws ClassCastException if this type is not an array.
     */
    public static Type getArrayComponentType(Type array) {
        return array instanceof GenericArrayType
                ? ((GenericArrayType) array).getGenericComponentType()
                : ((Class<?>) array).getComponentType();
    }

    /**
     * Returns the element type of this collection type.
     *
     * @throws IllegalArgumentException if this type is not a collection.
     */
    public static Type getCollectionElementType(Type context, Class<?> contextRawType) {
        Type collectionType = getSupertype(context, contextRawType, Collection.class);

        if (collectionType instanceof WildcardType) {
            collectionType = ((WildcardType) collectionType).getUpperBounds()[0];
        }
        if (collectionType instanceof ParameterizedType) {
            return ((ParameterizedType) collectionType).getActualTypeArguments()[0];
        }
        return Object.class;
    }

    /**
     * Returns a two element array containing this map's key and value types in
     * positions 0 and 1 respectively.
     */
    public static Type[] getMapKeyAndValueTypes(Type context, Class<?> contextRawType) {
        /*
         * Work around a problem with the declaration of java.util.Properties. That
         * class should extend Hashtable<String, String>, but it's declared to
         * extend Hashtable<Object, Object>.
         */
        if (context == Properties.class) {
            return new Type[]{String.class, String.class};
        }

        Type mapType = getSupertype(context, contextRawType, Map.class);
        if (mapType instanceof ParameterizedType) {
            ParameterizedType mapParameterizedType = (ParameterizedType) mapType;
            return mapParameterizedType.getActualTypeArguments();
        }
        return new Type[]{Object.class, Object.class};
    }

}
