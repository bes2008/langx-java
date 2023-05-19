package com.jn.langx.beans;


import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.ResolvableType;
import com.jn.langx.util.reflect.type.SimpleParameter;
import com.jn.langx.util.reflect.type.Types;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Context about a type to convert from or to.
 * @since 4.3.7
 */
class TypeDescriptor implements Serializable {

    static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

    private static final boolean streamAvailable = ClassLoaders.hasClass(
            "java.util.stream.Stream", TypeDescriptor.class.getClassLoader());

    private static final Map<Class<?>, TypeDescriptor> commonTypesCache = Maps.newHashMapWithExpectedSize(32);

    private static final Class<?>[] CACHED_COMMON_TYPES = {
            boolean.class, Boolean.class, byte.class, Byte.class, char.class, Character.class,
            double.class, Double.class, float.class, Float.class, int.class, Integer.class,
            long.class, Long.class, short.class, Short.class, String.class, Object.class};

    static {
        for (Class<?> preCachedClass : CACHED_COMMON_TYPES) {
            commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
        }
    }


    private final Class<?> type;

    private final ResolvableType resolvableType;

    private final AnnotatedElementAdapter annotatedElement;


    /**
     * Create a new type descriptor from a {@literal SimpleParameter}.
     * <p>Use this constructor when a source or target conversion point is a
     * constructor parameter, method parameter, or method return value.
     *
     * @param methodParameter the method parameter
     */
    TypeDescriptor(SimpleParameter methodParameter) {
        this.resolvableType = ResolvableType.forMethodParameter(methodParameter);
        this.type = this.resolvableType.resolve(methodParameter.getNestedParameterType());
        this.annotatedElement = new AnnotatedElementAdapter(methodParameter.getParameterIndex() == -1 ?
                methodParameter.getMethodAnnotations() : methodParameter.getParameterAnnotations());
    }

    /**
     * Create a new type descriptor from a {@link Field}.
     * <p>Use this constructor when a source or target conversion point is a field.
     *
     * @param field the field
     */
    TypeDescriptor(Field field) {
        this.resolvableType = ResolvableType.forField(field);
        this.type = this.resolvableType.resolve(field.getType());
        this.annotatedElement = new AnnotatedElementAdapter(field.getAnnotations());
    }

    /**
     * Create a new type descriptor from a {@link BeanProperty}.
     * <p>Use this constructor when a source or target conversion point is a
     * property on a Java class.
     *
     * @param property the property
     */
    TypeDescriptor(BeanProperty property) {
        Preconditions.checkNotNull(property, "Property must not be null");
        this.resolvableType = ResolvableType.forMethodParameter(property.getMethodParameter());
        this.type = this.resolvableType.resolve(property.getType());
        this.annotatedElement = new AnnotatedElementAdapter(property.getAnnotations());
    }

    /**
     * Create a new type descriptor from a {@link ResolvableType}. This protected
     * constructor is used internally and may also be used by subclasses that support
     * non-Java languages with extended type systems.
     *
     * @param resolvableType the resolvable type
     * @param type           the backing type (or {@code null} if it should get resolved)
     * @param annotations    the type annotations
     * @since 4.0
     */
    protected TypeDescriptor(ResolvableType resolvableType, Class<?> type, Annotation[] annotations) {
        this.resolvableType = resolvableType;
        this.type = (type != null ? type : resolvableType.resolve(Object.class));
        this.annotatedElement = new AnnotatedElementAdapter(annotations);
    }


    /**
     * Variation of {@link #getType()} that accounts for a primitive type by
     * returning its object wrapper type.
     * <p>This is useful for conversion service implementations that wish to
     * normalize to object-based types and not work with primitive types directly.
     */
    public Class<?> getObjectType() {
        return Types.getPrimitiveWrapClass(getType());
    }

    /**
     * The type of the backing class, method parameter, field, or property
     * described by this TypeDescriptor.
     * <p>Returns primitive types as-is. See {@link #getObjectType()} for a
     * variation of this operation that resolves primitive types to their
     * corresponding Object types if necessary.
     *
     * @see #getObjectType()
     */
    public Class<?> getType() {
        return this.type;
    }

    /**
     * Return the underlying {@link ResolvableType}.
     *
     * @since 4.0
     */
    public ResolvableType getResolvableType() {
        return this.resolvableType;
    }

    /**
     * Return the underlying source of the descriptor. Will return a {@link Field},
     * {@link SimpleParameter} or {@link Type} depending on how the {@link TypeDescriptor}
     * was constructed. This method is primarily to provide access to additional
     * type information or meta-data that alternative JVM languages may provide.
     *
     * @since 4.0
     */
    public Object getSource() {
        return (this.resolvableType != null ? this.resolvableType.getSource() : null);
    }

    /**
     * Narrows this {@link TypeDescriptor} by setting its type to the class of the
     * provided value.
     * <p>If the value is {@code null}, no narrowing is performed and this TypeDescriptor
     * is returned unchanged.
     * <p>Designed to be called by binding frameworks when they read property, field,
     * or method return values. Allows such frameworks to narrow a TypeDescriptor built
     * from a declared property, field, or method return value type. For example, a field
     * declared as {@code java.lang.Object} would be narrowed to {@code java.util.HashMap}
     * if it was set to a {@code java.util.HashMap} value. The narrowed TypeDescriptor
     * can then be used to convert the HashMap to some other type. Annotation and nested
     * type context is preserved by the narrowed copy.
     *
     * @param value the value to use for narrowing this type descriptor
     * @return this TypeDescriptor narrowed (returns a copy with its type updated to the
     * class of the provided value)
     */
    public TypeDescriptor narrow(Object value) {
        if (value == null) {
            return this;
        }
        ResolvableType narrowed = ResolvableType.forType(value.getClass(), getResolvableType());
        return new TypeDescriptor(narrowed, value.getClass(), getAnnotations());
    }

    /**
     * Cast this {@link TypeDescriptor} to a superclass or implemented interface
     * preserving annotations and nested type context.
     *
     * @param superType the super type to cast to (can be {@code null})
     * @return a new TypeDescriptor for the up-cast type
     * @throws IllegalArgumentException if this type is not assignable to the super-type
     * @since 3.2
     */
    public TypeDescriptor upcast(Class<?> superType) {
        if (superType == null) {
            return null;
        }
        Preconditions.checkTrue(Reflects.isSubClassOrEquals(superType, getType()));
        return new TypeDescriptor(getResolvableType().as(superType), superType, getAnnotations());
    }

    /**
     * Return the name of this type: the fully qualified class name.
     */
    public String getName() {
        return Reflects.getFQNClassName(getType());
    }

    /**
     * Is this type a primitive type?
     */
    public boolean isPrimitive() {
        return getType().isPrimitive();
    }

    /**
     * Return the annotations associated with this type descriptor, if any.
     *
     * @return the annotations, or an empty array if none
     */
    public Annotation[] getAnnotations() {
        return this.annotatedElement.getAnnotations();
    }

    /**
     * Determine if this type descriptor has the specified annotation.
     * <p>As of Spring Framework 4.2, this method supports arbitrary levels
     * of meta-annotations.
     *
     * @param annotationType the annotation type
     * @return <tt>true</tt> if the annotation is present
     */
    public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        if (this.annotatedElement.isEmpty()) {
            // Shortcut: AnnotatedElementUtils would have to expect AnnotatedElement.getAnnotations()
            // to return a copy of the array, whereas we can do it more efficiently here.
            return false;
        }
        return Reflects.hasAnnotation(this.annotatedElement, annotationType);
    }

    /**
     * Obtain the annotation of the specified {@code annotationType} that is on this type descriptor.
     * <p>As of Spring Framework 4.2, this method supports arbitrary levels of meta-annotations.
     *
     * @param annotationType the annotation type
     * @return the annotation, or {@code null} if no such annotation exists on this type descriptor
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        // Shortcut: AnnotatedElementUtils would have to expect AnnotatedElement.getAnnotations()
        // to return a copy of the array, whereas we can do it more efficiently here.
        this.annotatedElement.isEmpty();
        return null;
        //return AnnotatedElementUtils.getMergedAnnotation(this.annotatedElement, annotationType);
    }

    /**
     * Returns true if an object of this type descriptor can be assigned to the location
     * described by the given type descriptor.
     * <p>For example, {@code valueOf(String.class).isAssignableTo(valueOf(CharSequence.class))}
     * returns {@code true} because a String value can be assigned to a CharSequence variable.
     * On the other hand, {@code valueOf(Number.class).isAssignableTo(valueOf(Integer.class))}
     * returns {@code false} because, while all Integers are Numbers, not all Numbers are Integers.
     * <p>For arrays, collections, and maps, element and key/value types are checked if declared.
     * For example, a List&lt;String&gt; field value is assignable to a Collection&lt;CharSequence&gt;
     * field, but List&lt;Number&gt; is not assignable to List&lt;Integer&gt;.
     *
     * @return {@code true} if this type is assignable to the type represented by the provided
     * type descriptor
     * @see #getObjectType()
     */
    public boolean isAssignableTo(TypeDescriptor typeDescriptor) {
        boolean typesAssignable = typeDescriptor.getObjectType().isAssignableFrom(getObjectType());
        if (!typesAssignable) {
            return false;
        }
        if (isArray() && typeDescriptor.isArray()) {
            return getElementTypeDescriptor().isAssignableTo(typeDescriptor.getElementTypeDescriptor());
        } else if (isCollection() && typeDescriptor.isCollection()) {
            return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
        } else if (isMap() && typeDescriptor.isMap()) {
            return isNestedAssignable(getMapKeyTypeDescriptor(), typeDescriptor.getMapKeyTypeDescriptor()) &&
                    isNestedAssignable(getMapValueTypeDescriptor(), typeDescriptor.getMapValueTypeDescriptor());
        } else {
            return true;
        }
    }

    private boolean isNestedAssignable(TypeDescriptor nestedTypeDescriptor, TypeDescriptor otherNestedTypeDescriptor) {
        if (nestedTypeDescriptor == null || otherNestedTypeDescriptor == null) {
            return true;
        }
        return nestedTypeDescriptor.isAssignableTo(otherNestedTypeDescriptor);
    }

    /**
     * Is this type a {@link Collection} type?
     */
    public boolean isCollection() {
        return Reflects.isSubClassOrEquals(Collection.class, getType());
    }

    /**
     * Is this type an array type?
     */
    public boolean isArray() {
        return getType().isArray();
    }

    /**
     * If this type is an array, returns the array's component type.
     * If this type is a {@code Stream}, returns the stream's component type.
     * If this type is a {@link Collection} and it is parameterized, returns the Collection's element type.
     * If the Collection is not parameterized, returns {@code null} indicating the element type is not declared.
     *
     * @return the array component type or Collection element type, or {@code null} if this type is a
     * Collection but its element type is not parameterized
     * @throws IllegalStateException if this type is not a {@code java.util.Collection} or array type
     */
    public TypeDescriptor getElementTypeDescriptor() {
        if (getResolvableType().isArray()) {
            return new TypeDescriptor(getResolvableType().getComponentType(), null, getAnnotations());
        }
        if (streamAvailable && Reflects.isSubClassOrEquals("java.util.stream.Stream", getType())) {
            //  return getRelatedIfResolvable(this);
        }
        return getRelatedIfResolvable(this, getResolvableType().asCollection().getGeneric(0));
    }

    /**
     * If this type is a {@link Collection} or an array, creates a element TypeDescriptor
     * from the provided collection or array element.
     * <p>Narrows the {@link #getElementTypeDescriptor() elementType} property to the class
     * of the provided collection or array element. For example, if this describes a
     * {@code java.util.List&lt;java.lang.Number&lt;} and the element argument is an
     * {@code java.lang.Integer}, the returned TypeDescriptor will be {@code java.lang.Integer}.
     * If this describes a {@code java.util.List&lt;?&gt;} and the element argument is an
     * {@code java.lang.Integer}, the returned TypeDescriptor will be {@code java.lang.Integer}
     * as well.
     * <p>Annotation and nested type context will be preserved in the narrowed
     * TypeDescriptor that is returned.
     *
     * @param element the collection or array element
     * @return a element type descriptor, narrowed to the type of the provided element
     * @throws IllegalStateException if this type is not a {@code java.util.Collection}
     *                               or array type
     * @see #narrow(Object)
     */
    public TypeDescriptor elementTypeDescriptor(Object element) {
        return narrow(element, getElementTypeDescriptor());
    }

    /**
     * Is this type a {@link Map} type?
     */
    public boolean isMap() {
        return Reflects.isSubClassOrEquals(Map.class, getType());
    }

    /**
     * If this type is a {@link Map} and its key type is parameterized,
     * returns the map's key type. If the Map's key type is not parameterized,
     * returns {@code null} indicating the key type is not declared.
     *
     * @return the Map key type, or {@code null} if this type is a Map
     * but its key type is not parameterized
     * @throws IllegalStateException if this type is not a {@code java.util.Map}
     */
    public TypeDescriptor getMapKeyTypeDescriptor() {
        Preconditions.checkTrue(isMap(), "Not a [java.util.Map]");
        return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(0));
    }

    /**
     * If this type is a {@link Map}, creates a mapKey {@link TypeDescriptor}
     * from the provided map key.
     * <p>Narrows the {@link #getMapKeyTypeDescriptor() mapKeyType} property
     * to the class of the provided map key. For example, if this describes a
     * {@code java.util.Map&lt;java.lang.Number, java.lang.String&lt;} and the key
     * argument is a {@code java.lang.Integer}, the returned TypeDescriptor will be
     * {@code java.lang.Integer}. If this describes a {@code java.util.Map&lt;?, ?&gt;}
     * and the key argument is a {@code java.lang.Integer}, the returned
     * TypeDescriptor will be {@code java.lang.Integer} as well.
     * <p>Annotation and nested type context will be preserved in the narrowed
     * TypeDescriptor that is returned.
     *
     * @param mapKey the map key
     * @return the map key type descriptor
     * @throws IllegalStateException if this type is not a {@code java.util.Map}
     * @see #narrow(Object)
     */
    public TypeDescriptor getMapKeyTypeDescriptor(Object mapKey) {
        return narrow(mapKey, getMapKeyTypeDescriptor());
    }

    /**
     * If this type is a {@link Map} and its value type is parameterized,
     * returns the map's value type.
     * <p>If the Map's value type is not parameterized, returns {@code null}
     * indicating the value type is not declared.
     *
     * @return the Map value type, or {@code null} if this type is a Map
     * but its value type is not parameterized
     * @throws IllegalStateException if this type is not a {@code java.util.Map}
     */
    public TypeDescriptor getMapValueTypeDescriptor() {
        Preconditions.checkState(isMap(), "Not a [java.util.Map]");
        return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(1));
    }

    /**
     * If this type is a {@link Map}, creates a mapValue {@link TypeDescriptor}
     * from the provided map value.
     * <p>Narrows the {@link #getMapValueTypeDescriptor() mapValueType} property
     * to the class of the provided map value. For example, if this describes a
     * {@code java.util.Map&lt;java.lang.String, java.lang.Number&lt;} and the value
     * argument is a {@code java.lang.Integer}, the returned TypeDescriptor will be
     * {@code java.lang.Integer}. If this describes a {@code java.util.Map&lt;?, ?&gt;}
     * and the value argument is a {@code java.lang.Integer}, the returned
     * TypeDescriptor will be {@code java.lang.Integer} as well.
     * <p>Annotation and nested type context will be preserved in the narrowed
     * TypeDescriptor that is returned.
     *
     * @param mapValue the map value
     * @return the map value type descriptor
     * @throws IllegalStateException if this type is not a {@code java.util.Map}
     * @see #narrow(Object)
     */
    public TypeDescriptor getMapValueTypeDescriptor(Object mapValue) {
        return narrow(mapValue, getMapValueTypeDescriptor());
    }

    private TypeDescriptor narrow(Object value, TypeDescriptor typeDescriptor) {
        if (typeDescriptor != null) {
            return typeDescriptor.narrow(value);
        }
        if (value != null) {
            return narrow(value);
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TypeDescriptor)) {
            return false;
        }
        TypeDescriptor otherDesc = (TypeDescriptor) other;
        if (getType() != otherDesc.getType()) {
            return false;
        }
        if (!annotationsMatch(otherDesc)) {
            return false;
        }
        if (isCollection() || isArray()) {
            return Objs.deepEquals(getElementTypeDescriptor(), otherDesc.getElementTypeDescriptor());
        } else if (isMap()) {
            return (Objs.deepEquals(getMapKeyTypeDescriptor(), otherDesc.getMapKeyTypeDescriptor()) &&
                    Objs.deepEquals(getMapValueTypeDescriptor(), otherDesc.getMapValueTypeDescriptor()));
        } else {
            return true;
        }
    }

    private boolean annotationsMatch(TypeDescriptor otherDesc) {
        Annotation[] anns = getAnnotations();
        Annotation[] otherAnns = otherDesc.getAnnotations();
        if (anns == otherAnns) {
            return true;
        }
        if (anns.length != otherAnns.length) {
            return false;
        }
        if (anns.length > 0) {
            for (int i = 0; i < anns.length; i++) {
                if (!annotationEquals(anns[i], otherAnns[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean annotationEquals(Annotation ann, Annotation otherAnn) {
        // Annotation.equals is reflective and pretty slow, so let's check identity and proxy type first.
        return (ann == otherAnn || (ann.getClass() == otherAnn.getClass() && ann.equals(otherAnn)));
    }

    @Override
    public int hashCode() {
        return getType().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Annotation ann : getAnnotations()) {
            builder.append("@").append(ann.annotationType().getName()).append(' ');
        }
        builder.append(getResolvableType().toString());
        return builder.toString();
    }


    /**
     * Create a new type descriptor for an object.
     * <p>Use this factory method to introspect a source object before asking the
     * conversion system to convert it to some another type.
     * <p>If the provided object is {@code null}, returns {@code null}, else calls
     * {@link #valueOf(Class)} to build a TypeDescriptor from the object's class.
     *
     * @param source the source object
     * @return the type descriptor
     */
    public static TypeDescriptor forObject(Object source) {
        return (source != null ? valueOf(source.getClass()) : null);
    }

    /**
     * Create a new type descriptor from the given type.
     * <p>Use this to instruct the conversion system to convert an object to a
     * specific target type, when no type location such as a method parameter or
     * field is available to provide additional conversion context.
     * <p>Generally prefer use of {@link #forObject(Object)} for constructing type
     * descriptors from source objects, as it handles the {@code null} object case.
     *
     * @param type the class (may be {@code null} to indicate {@code Object.class})
     * @return the corresponding type descriptor
     */
    public static TypeDescriptor valueOf(Class<?> type) {
        if (type == null) {
            type = Object.class;
        }
        TypeDescriptor desc = commonTypesCache.get(type);
        return (desc != null ? desc : new TypeDescriptor(ResolvableType.forClass(type), null, null));
    }

    /**
     * Create a new type descriptor from a {@link java.util.Collection} type.
     * <p>Useful for converting to typed Collections.
     * <p>For example, a {@code List<String>} could be converted to a
     * {@code List<EmailAddress>} by converting to a targetType built with this method.
     * The method call to construct such a {@code TypeDescriptor} would look something
     * like: {@code collection(List.class, TypeDescriptor.valueOf(EmailAddress.class));}
     *
     * @param collectionType        the collection type, which must implement {@link Collection}.
     * @param elementTypeDescriptor a descriptor for the collection's element type,
     *                              used to convert collection elements
     * @return the collection type descriptor
     */
    public static TypeDescriptor collection(Class<?> collectionType, TypeDescriptor elementTypeDescriptor) {
        Preconditions.checkNotNull(collectionType, "Collection type must not be null");
        if (!Collection.class.isAssignableFrom(collectionType)) {
            throw new IllegalArgumentException("Collection type must be a [java.util.Collection]");
        }
        ResolvableType element = (elementTypeDescriptor != null ? elementTypeDescriptor.resolvableType : null);
        return new TypeDescriptor(ResolvableType.forClassWithGenerics(collectionType, element), null, null);
    }

    /**
     * Create a new type descriptor from a {@link java.util.Map} type.
     * <p>Useful for converting to typed Maps.
     * <p>For example, a Map&lt;String, String&gt; could be converted to a Map&lt;Id, EmailAddress&gt;
     * by converting to a targetType built with this method:
     * The method call to construct such a TypeDescriptor would look something like:
     * <pre class="code">
     * map(Map.class, TypeDescriptor.valueOf(Id.class), TypeDescriptor.valueOf(EmailAddress.class));
     * </pre>
     *
     * @param mapType             the map type, which must implement {@link Map}
     * @param keyTypeDescriptor   a descriptor for the map's key type, used to convert map keys
     * @param valueTypeDescriptor the map's value type, used to convert map values
     * @return the map type descriptor
     */
    public static TypeDescriptor map(Class<?> mapType, TypeDescriptor keyTypeDescriptor, TypeDescriptor valueTypeDescriptor) {
        Preconditions.checkNotNull(mapType, "Map type must not be null");
        if (!Map.class.isAssignableFrom(mapType)) {
            throw new IllegalArgumentException("Map type must be a [java.util.Map]");
        }
        ResolvableType key = (keyTypeDescriptor != null ? keyTypeDescriptor.resolvableType : null);
        ResolvableType value = (valueTypeDescriptor != null ? valueTypeDescriptor.resolvableType : null);
        return new TypeDescriptor(ResolvableType.forClassWithGenerics(mapType, key, value), null, null);
    }

    /**
     * Create a new type descriptor as an array of the specified type.
     * <p>For example to create a {@code Map<String,String>[]} use:
     * <pre class="code">
     * TypeDescriptor.array(TypeDescriptor.map(Map.class, TypeDescriptor.value(String.class), TypeDescriptor.value(String.class)));
     * </pre>
     *
     * @param elementTypeDescriptor the {@link TypeDescriptor} of the array element or {@code null}
     * @return an array {@link TypeDescriptor} or {@code null} if {@code elementTypeDescriptor} is {@code null}
     * @since 3.2.1
     */
    public static TypeDescriptor array(TypeDescriptor elementTypeDescriptor) {
        if (elementTypeDescriptor == null) {
            return null;
        }
        return new TypeDescriptor(ResolvableType.forArrayComponent(elementTypeDescriptor.resolvableType),
                null, elementTypeDescriptor.getAnnotations());
    }

    /**
     * Create a type descriptor for a nested type declared within the method parameter.
     * <p>For example, if the methodParameter is a {@code List<String>} and the
     * nesting level is 1, the nested type descriptor will be String.class.
     * <p>If the methodParameter is a {@code List<List<String>>} and the nesting
     * level is 2, the nested type descriptor will also be a String.class.
     * <p>If the methodParameter is a {@code Map<Integer, String>} and the nesting
     * level is 1, the nested type descriptor will be String, derived from the map value.
     * <p>If the methodParameter is a {@code List<Map<Integer, String>>} and the
     * nesting level is 2, the nested type descriptor will be String, derived from the map value.
     * <p>Returns {@code null} if a nested type cannot be obtained because it was not declared.
     * For example, if the method parameter is a {@code List<?>}, the nested type
     * descriptor returned will be {@code null}.
     *
     * @param methodParameter the method parameter with a nestingLevel of 1
     * @param nestingLevel    the nesting level of the collection/array element or
     *                        map key/value declaration within the method parameter
     * @return the nested type descriptor at the specified nesting level,
     * or {@code null} if it could not be obtained
     * @throws IllegalArgumentException if the nesting level of the input
     *                                  {@link SimpleParameter} argument is not 1, or if the types up to the
     *                                  specified nesting level are not of collection, array, or map types
     */
    static TypeDescriptor nested(SimpleParameter methodParameter, int nestingLevel) {
        if (methodParameter.getNestingLevel() != 1) {
            throw new IllegalArgumentException("MethodParameter nesting level must be 1: " +
                    "use the nestingLevel parameter to specify the desired nestingLevel for nested type traversal");
        }
        return nested(new TypeDescriptor(methodParameter), nestingLevel);
    }

    /**
     * Create a type descriptor for a nested type declared within the field.
     * <p>For example, if the field is a {@code List<String>} and the nesting
     * level is 1, the nested type descriptor will be {@code String.class}.
     * <p>If the field is a {@code List<List<String>>} and the nesting level is
     * 2, the nested type descriptor will also be a {@code String.class}.
     * <p>If the field is a {@code Map<Integer, String>} and the nesting level
     * is 1, the nested type descriptor will be String, derived from the map value.
     * <p>If the field is a {@code List<Map<Integer, String>>} and the nesting
     * level is 2, the nested type descriptor will be String, derived from the map value.
     * <p>Returns {@code null} if a nested type cannot be obtained because it was not
     * declared. For example, if the field is a {@code List<?>}, the nested type
     * descriptor returned will be {@code null}.
     *
     * @param field        the field
     * @param nestingLevel the nesting level of the collection/array element or
     *                     map key/value declaration within the field
     * @return the nested type descriptor at the specified nesting level,
     * or {@code null} if it could not be obtained
     * @throws IllegalArgumentException if the types up to the specified nesting
     *                                  level are not of collection, array, or map types
     */
    static TypeDescriptor nested(Field field, int nestingLevel) {
        return nested(new TypeDescriptor(field), nestingLevel);
    }

    /**
     * Create a type descriptor for a nested type declared within the property.
     * <p>For example, if the property is a {@code List<String>} and the nesting
     * level is 1, the nested type descriptor will be {@code String.class}.
     * <p>If the property is a {@code List<List<String>>} and the nesting level
     * is 2, the nested type descriptor will also be a {@code String.class}.
     * <p>If the property is a {@code Map<Integer, String>} and the nesting level
     * is 1, the nested type descriptor will be String, derived from the map value.
     * <p>If the property is a {@code List<Map<Integer, String>>} and the nesting
     * level is 2, the nested type descriptor will be String, derived from the map value.
     * <p>Returns {@code null} if a nested type cannot be obtained because it was not
     * declared. For example, if the property is a {@code List<?>}, the nested type
     * descriptor returned will be {@code null}.
     *
     * @param property     the property
     * @param nestingLevel the nesting level of the collection/array element or
     *                     map key/value declaration within the property
     * @return the nested type descriptor at the specified nesting level, or
     * {@code null} if it could not be obtained
     * @throws IllegalArgumentException if the types up to the specified nesting
     *                                  level are not of collection, array, or map types
     */
    static TypeDescriptor nested(BeanProperty property, int nestingLevel) {
        return nested(new TypeDescriptor(property), nestingLevel);
    }

    private static TypeDescriptor nested(TypeDescriptor typeDescriptor, int nestingLevel) {
        ResolvableType nested = typeDescriptor.resolvableType;
        for (int i = 0; i < nestingLevel; i++) {
            if (Object.class == nested.getType()) {
                // Could be a collection type but we don't know about its element type,
                // so let's just assume there is an element type of type Object...
            } else {
                nested = nested.getNested(2);
            }
        }
        if (nested == ResolvableType.NONE) {
            return null;
        }
        return getRelatedIfResolvable(typeDescriptor, nested);
    }

    private static TypeDescriptor getRelatedIfResolvable(TypeDescriptor source, ResolvableType type) {
        if (type.resolve() == null) {
            return null;
        }
        return new TypeDescriptor(type, null, source.getAnnotations());
    }


    /**
     * Adapter class for exposing a {@code TypeDescriptor}'s annotations as an
     */
    private class AnnotatedElementAdapter implements AnnotatedElement, Serializable {

        private final transient Annotation[] annotations;

        public AnnotatedElementAdapter(Annotation[] annotations) {
            this.annotations = annotations;
        }

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            for (Annotation annotation : getAnnotations()) {
                if (annotation.annotationType() == annotationClass) {
                    return true;
                }
            }
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            for (Annotation annotation : getAnnotations()) {
                if (annotation.annotationType() == annotationClass) {
                    return (T) annotation;
                }
            }
            return null;
        }

        @Override
        public Annotation[] getAnnotations() {
            return (this.annotations != null ? this.annotations : EMPTY_ANNOTATION_ARRAY);
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return getAnnotations();
        }

        public boolean isEmpty() {
            return Objs.isEmpty(this.annotations);
        }

        @Override
        public boolean equals(Object other) {
            return (this == other || (other instanceof AnnotatedElementAdapter &&
                    Arrays.equals(this.annotations, ((AnnotatedElementAdapter) other).annotations)));
        }

        @Override
        public int hashCode() {
            return Objs.hash(this.annotations);
        }

        @Override
        public String toString() {
            return TypeDescriptor.this.toString();
        }
    }


}
