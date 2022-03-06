package com.jn.langx.util.reflect.type;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.ParameterNameDiscoverer;

/**
 * Helper class that encapsulates the specification of a method parameter, i.e. a {@link Method}
 * or {@link Constructor} plus a parameter index and a nested type index for a declared generic
 * type. Useful as a specification object to pass along.
 *
 * subclass available which synthesizes annotations with attribute aliases. That subclass is used
 * for web and message endpoint processing, in particular.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Andy Clement
 * @author Sam Brannen
 * @since 2.0
 */
public class SimpleParameter {

    private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

    private static final Class<?> javaUtilOptionalClass;

    static {
        Class<?> clazz;
        try {
            clazz = ClassLoaders.loadClass("java.util.Optional", SimpleParameter.class.getClassLoader());
        }
        catch (ClassNotFoundException ex) {
            // Java 8 not available - Optional references simply not supported then.
            clazz = null;
        }
        javaUtilOptionalClass = clazz;
    }


    private final Method method;

    private final Constructor<?> constructor;

    private final int parameterIndex;

    private int nestingLevel;

    /** Map from Integer level to Integer type index */
    Map<Integer, Integer> typeIndexesPerLevel;

    /** The containing class. Could also be supplied by overriding {@link #getContainingClass()} */
    private volatile Class<?> containingClass;

    private volatile Class<?> parameterType;

    private volatile Type genericParameterType;

    private volatile Annotation[] parameterAnnotations;

    private volatile ParameterNameDiscoverer parameterNameDiscoverer;

    private volatile String parameterName;

    private volatile SimpleParameter nestedMethodParameter;


    /**
     * Create a new {@code MethodParameter} for the given method, with nesting level 1.
     * @param method the Method to specify a parameter for
     * @param parameterIndex the index of the parameter: -1 for the method
     * return type; 0 for the first method parameter; 1 for the second method
     * parameter, etc.
     */
    public SimpleParameter(Method method, int parameterIndex) {
        this(method, parameterIndex, 1);
    }

    /**
     * Create a new {@code MethodParameter} for the given method.
     * @param method the Method to specify a parameter for
     * @param parameterIndex the index of the parameter: -1 for the method
     * return type; 0 for the first method parameter; 1 for the second method
     * parameter, etc.
     * @param nestingLevel the nesting level of the target type
     * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
     * nested List, whereas 2 would indicate the element of the nested List)
     */
    public SimpleParameter(Method method, int parameterIndex, int nestingLevel) {
        Preconditions.checkNotNull(method, "Method must not be null");
        this.method = method;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
        this.constructor = null;
    }

    /**
     * Create a new MethodParameter for the given constructor, with nesting level 1.
     * @param constructor the Constructor to specify a parameter for
     * @param parameterIndex the index of the parameter
     */
    public SimpleParameter(Constructor<?> constructor, int parameterIndex) {
        this(constructor, parameterIndex, 1);
    }

    /**
     * Create a new MethodParameter for the given constructor.
     * @param constructor the Constructor to specify a parameter for
     * @param parameterIndex the index of the parameter
     * @param nestingLevel the nesting level of the target type
     * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
     * nested List, whereas 2 would indicate the element of the nested List)
     */
    public SimpleParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
        Preconditions.checkNotNull(constructor, "Constructor must not be null");
        this.constructor = constructor;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
        this.method = null;
    }

    /**
     * Copy constructor, resulting in an independent MethodParameter object
     * based on the same metadata and cache state that the original object was in.
     * @param original the original MethodParameter object to copy from
     */
    public SimpleParameter(SimpleParameter original) {
        Preconditions.checkNotNull(original, "Original must not be null");
        this.method = original.method;
        this.constructor = original.constructor;
        this.parameterIndex = original.parameterIndex;
        this.nestingLevel = original.nestingLevel;
        this.typeIndexesPerLevel = original.typeIndexesPerLevel;
        this.containingClass = original.containingClass;
        this.parameterType = original.parameterType;
        this.genericParameterType = original.genericParameterType;
        this.parameterAnnotations = original.parameterAnnotations;
        this.parameterNameDiscoverer = original.parameterNameDiscoverer;
        this.parameterName = original.parameterName;
    }


    /**
     * Return the wrapped Method, if any.
     * <p>Note: Either Method or Constructor is available.
     * @return the Method, or {@code null} if none
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * Return the wrapped Constructor, if any.
     * <p>Note: Either Method or Constructor is available.
     * @return the Constructor, or {@code null} if none
     */
    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    /**
     * Return the class that declares the underlying Method or Constructor.
     */
    public Class<?> getDeclaringClass() {
        return getMember().getDeclaringClass();
    }

    /**
     * Return the wrapped member.
     * @return the Method or Constructor as Member
     */
    public Member getMember() {
        // NOTE: no ternary expression to retain JDK <8 compatibility even when using
        // the JDK 8 compiler (potentially selecting java.lang.reflect.Executable
        // as common type, with that new base class not available on older JDKs)
        if (this.method != null) {
            return this.method;
        }
        else {
            return this.constructor;
        }
    }

    /**
     * Return the wrapped annotated element.
     * <p>Note: This method exposes the annotations declared on the method/constructor
     * itself (i.e. at the method/constructor level, not at the parameter level).
     * @return the Method or Constructor as AnnotatedElement
     */
    public AnnotatedElement getAnnotatedElement() {
        // NOTE: no ternary expression to retain JDK <8 compatibility even when using
        // the JDK 8 compiler (potentially selecting java.lang.reflect.Executable
        // as common type, with that new base class not available on older JDKs)
        if (this.method != null) {
            return this.method;
        }
        else {
            return this.constructor;
        }
    }

    /**
     * Return the index of the method/constructor parameter.
     * @return the parameter index (-1 in case of the return type)
     */
    public int getParameterIndex() {
        return this.parameterIndex;
    }

    /**
     * Increase this parameter's nesting level.
     * @see #getNestingLevel()
     */
    public void increaseNestingLevel() {
        this.nestingLevel++;
    }

    /**
     * Decrease this parameter's nesting level.
     * @see #getNestingLevel()
     */
    public void decreaseNestingLevel() {
        getTypeIndexesPerLevel().remove(this.nestingLevel);
        this.nestingLevel--;
    }

    /**
     * Return the nesting level of the target type
     * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
     * nested List, whereas 2 would indicate the element of the nested List).
     */
    public int getNestingLevel() {
        return this.nestingLevel;
    }

    /**
     * Set the type index for the current nesting level.
     * @param typeIndex the corresponding type index
     * (or {@code null} for the default type index)
     * @see #getNestingLevel()
     */
    public void setTypeIndexForCurrentLevel(int typeIndex) {
        getTypeIndexesPerLevel().put(this.nestingLevel, typeIndex);
    }

    /**
     * Return the type index for the current nesting level.
     * @return the corresponding type index, or {@code null}
     * if none specified (indicating the default type index)
     * @see #getNestingLevel()
     */
    public Integer getTypeIndexForCurrentLevel() {
        return getTypeIndexForLevel(this.nestingLevel);
    }

    /**
     * Return the type index for the specified nesting level.
     * @param nestingLevel the nesting level to check
     * @return the corresponding type index, or {@code null}
     * if none specified (indicating the default type index)
     */
    public Integer getTypeIndexForLevel(int nestingLevel) {
        return getTypeIndexesPerLevel().get(nestingLevel);
    }

    /**
     * Obtain the (lazily constructed) type-indexes-per-level Map.
     */
    private Map<Integer, Integer> getTypeIndexesPerLevel() {
        if (this.typeIndexesPerLevel == null) {
            this.typeIndexesPerLevel = new HashMap<Integer, Integer>(4);
        }
        return this.typeIndexesPerLevel;
    }

    /**
     * Return a variant of this {@code MethodParameter} which points to the
     * same parameter but one nesting level deeper. This is effectively the
     * same as {@link #increaseNestingLevel()}, just with an independent
     * {@code MethodParameter} object (e.g. in case of the original being cached).
     * @since 4.3
     */
    public SimpleParameter nested() {
        if (this.nestedMethodParameter != null) {
            return this.nestedMethodParameter;
        }
        SimpleParameter nestedParam = clone();
        nestedParam.nestingLevel = this.nestingLevel + 1;
        this.nestedMethodParameter = nestedParam;
        return nestedParam;
    }

    /**
     * Return whether this method parameter is declared as optional
     * in the form of Java 8's java.util.Optional.
     * @since 4.3
     */
    public boolean isOptional() {
        return (getParameterType() == javaUtilOptionalClass);
    }

    /**
     * Return a variant of this {@code MethodParameter} which points to
     * the same parameter but one nesting level deeper in case of a
     * @since 4.3
     * @see #isOptional()
     * @see #nested()
     */
    public SimpleParameter nestedIfOptional() {
        return (isOptional() ? nested() : this);
    }

    /**
     * Set a containing class to resolve the parameter type against.
     */
    public void setContainingClass(Class<?> containingClass) {
        this.containingClass = containingClass;
    }

    /**
     * Return the containing class for this method parameter.
     * @return a specific containing class (potentially a subclass of the
     * declaring class), or otherwise simply the declaring class itself
     * @see #getDeclaringClass()
     */
    public Class<?> getContainingClass() {
        return (this.containingClass != null ? this.containingClass : getDeclaringClass());
    }

    /**
     * Set a resolved (generic) parameter type.
     */
    void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    /**
     * Return the type of the method/constructor parameter.
     * @return the parameter type (never {@code null})
     */
    public Class<?> getParameterType() {
        Class<?> paramType = this.parameterType;
        if (paramType == null) {
            if (this.parameterIndex < 0) {
                Method method = getMethod();
                paramType = (method != null ? method.getReturnType() : void.class);
            }
            else {
                paramType = (this.method != null ?
                        this.method.getParameterTypes()[this.parameterIndex] :
                        this.constructor.getParameterTypes()[this.parameterIndex]);
            }
            this.parameterType = paramType;
        }
        return paramType;
    }

    /**
     * Return the generic type of the method/constructor parameter.
     * @return the parameter type (never {@code null})
     * @since 3.0
     */
    public Type getGenericParameterType() {
        Type paramType = this.genericParameterType;
        if (paramType == null) {
            if (this.parameterIndex < 0) {
                Method method = getMethod();
                paramType = (method != null ? method.getGenericReturnType() : void.class);
            }
            else {
                Type[] genericParameterTypes = (this.method != null ?
                        this.method.getGenericParameterTypes() : this.constructor.getGenericParameterTypes());
                int index = this.parameterIndex;
                if (this.constructor != null && this.constructor.getDeclaringClass().isMemberClass() &&
                        !Modifier.isStatic(this.constructor.getDeclaringClass().getModifiers()) &&
                        genericParameterTypes.length == this.constructor.getParameterTypes().length - 1) {
                    // Bug in javac: type array excludes enclosing instance parameter
                    // for inner classes with at least one generic constructor parameter,
                    // so access it with the actual parameter index lowered by 1
                    index = this.parameterIndex - 1;
                }
                paramType = (index >= 0 && index < genericParameterTypes.length ?
                        genericParameterTypes[index] : getParameterType());
            }
            this.genericParameterType = paramType;
        }
        return paramType;
    }

    /**
     * Return the nested type of the method/constructor parameter.
     * @return the parameter type (never {@code null})
     * @since 3.1
     * @see #getNestingLevel()
     */
    public Class<?> getNestedParameterType() {
        if (this.nestingLevel > 1) {
            Type type = getGenericParameterType();
            for (int i = 2; i <= this.nestingLevel; i++) {
                if (type instanceof ParameterizedType) {
                    Type[] args = ((ParameterizedType) type).getActualTypeArguments();
                    Integer index = getTypeIndexForLevel(i);
                    type = args[index != null ? index : args.length - 1];
                }
                // TODO: Object.class if unresolvable
            }
            if (type instanceof Class) {
                return (Class<?>) type;
            }
            else if (type instanceof ParameterizedType) {
                Type arg = ((ParameterizedType) type).getRawType();
                if (arg instanceof Class) {
                    return (Class<?>) arg;
                }
            }
            return Object.class;
        }
        else {
            return getParameterType();
        }
    }

    /**
     * Return the nested generic type of the method/constructor parameter.
     * @return the parameter type (never {@code null})
     * @since 4.2
     * @see #getNestingLevel()
     */
    public Type getNestedGenericParameterType() {
        if (this.nestingLevel > 1) {
            Type type = getGenericParameterType();
            for (int i = 2; i <= this.nestingLevel; i++) {
                if (type instanceof ParameterizedType) {
                    Type[] args = ((ParameterizedType) type).getActualTypeArguments();
                    Integer index = getTypeIndexForLevel(i);
                    type = args[index != null ? index : args.length - 1];
                }
            }
            return type;
        }
        else {
            return getGenericParameterType();
        }
    }

    /**
     * Return the annotations associated with the target method/constructor itself.
     */
    public Annotation[] getMethodAnnotations() {
        return adaptAnnotationArray(getAnnotatedElement().getAnnotations());
    }

    /**
     * Return the method/constructor annotation of the given type, if available.
     * @param annotationType the annotation type to look for
     * @return the annotation object, or {@code null} if not found
     */
    public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
        return adaptAnnotation(getAnnotatedElement().getAnnotation(annotationType));
    }

    /**
     * Return whether the method/constructor is annotated with the given type.
     * @param annotationType the annotation type to look for
     * @since 4.3
     * @see #getMethodAnnotation(Class)
     */
    public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
        return getAnnotatedElement().isAnnotationPresent(annotationType);
    }

    /**
     * Return the annotations associated with the specific method/constructor parameter.
     */
    public Annotation[] getParameterAnnotations() {
        Annotation[] paramAnns = this.parameterAnnotations;
        if (paramAnns == null) {
            Annotation[][] annotationArray = (this.method != null ?
                    this.method.getParameterAnnotations() : this.constructor.getParameterAnnotations());
            int index = this.parameterIndex;
            if (this.constructor != null && this.constructor.getDeclaringClass().isMemberClass() &&
                    !Modifier.isStatic(this.constructor.getDeclaringClass().getModifiers()) &&
                    annotationArray.length == this.constructor.getParameterTypes().length - 1) {
                // Bug in javac in JDK <9: annotation array excludes enclosing instance parameter
                // for inner classes, so access it with the actual parameter index lowered by 1
                index = this.parameterIndex - 1;
            }
            paramAnns = (index >= 0 && index < annotationArray.length ?
                    adaptAnnotationArray(annotationArray[index]) : EMPTY_ANNOTATION_ARRAY);
            this.parameterAnnotations = paramAnns;
        }
        return paramAnns;
    }

    /**
     * Return {@code true} if the parameter has at least one annotation,
     * {@code false} if it has none.
     * @see #getParameterAnnotations()
     */
    public boolean hasParameterAnnotations() {
        return (getParameterAnnotations().length != 0);
    }

    /**
     * Return the parameter annotation of the given type, if available.
     * @param annotationType the annotation type to look for
     * @return the annotation object, or {@code null} if not found
     */
    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
        Annotation[] anns = getParameterAnnotations();
        for (Annotation ann : anns) {
            if (annotationType.isInstance(ann)) {
                return (A) ann;
            }
        }
        return null;
    }

    /**
     * Return whether the parameter is declared with the given annotation type.
     * @param annotationType the annotation type to look for
     * @see #getParameterAnnotation(Class)
     */
    public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
        return (getParameterAnnotation(annotationType) != null);
    }

    /**
     * Initialize parameter name discovery for this method parameter.
     * <p>This method does not actually try to retrieve the parameter name at
     * this point; it just allows discovery to happen when the application calls
     * {@link #getParameterName()} (if ever).
     */
    public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    /**
     * Return the name of the method/constructor parameter.
     * @return the parameter name (may be {@code null} if no
     * parameter name metadata is contained in the class file or no
     * {@link #initParameterNameDiscovery ParameterNameDiscoverer}
     * has been set to begin with)
     */
    public String getParameterName() {
        ParameterNameDiscoverer discoverer = this.parameterNameDiscoverer;
        if (discoverer != null) {
            String[] parameterNames = (this.method != null ?
                    discoverer.getParameterNames(this.method) : discoverer.getParameterNames(this.constructor));
            if (parameterNames != null) {
                this.parameterName = parameterNames[this.parameterIndex];
            }
            this.parameterNameDiscoverer = null;
        }
        return this.parameterName;
    }


    /**
     * A template method to post-process a given annotation instance before
     * returning it to the caller.
     * <p>The default implementation simply returns the given annotation as-is.
     * @param annotation the annotation about to be returned
     * @return the post-processed annotation (or simply the original one)
     * @since 4.2
     */
    protected <A extends Annotation> A adaptAnnotation(A annotation) {
        return annotation;
    }

    /**
     * A template method to post-process a given annotation array before
     * returning it to the caller.
     * <p>The default implementation simply returns the given annotation array as-is.
     * @param annotations the annotation array about to be returned
     * @return the post-processed annotation array (or simply the original one)
     * @since 4.2
     */
    protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
        return annotations;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SimpleParameter)) {
            return false;
        }
        SimpleParameter otherParam = (SimpleParameter) other;
        return (getContainingClass() == otherParam.getContainingClass() &&
                Objs.deepEquals(this.typeIndexesPerLevel, otherParam.typeIndexesPerLevel) &&
                this.nestingLevel == otherParam.nestingLevel &&
                this.parameterIndex == otherParam.parameterIndex &&
                getMember().equals(otherParam.getMember()));
    }

    @Override
    public int hashCode() {
        return (getMember().hashCode() * 31 + this.parameterIndex);
    }

    @Override
    public String toString() {
        return (this.method != null ? "method '" + this.method.getName() + "'" : "constructor") +
                " parameter " + this.parameterIndex;
    }

    @Override
    public SimpleParameter clone() {
        return new SimpleParameter(this);
    }


    /**
     * Create a new MethodParameter for the given method or constructor.
     * <p>This is a convenience constructor for scenarios where a
     * Method or Constructor reference is treated in a generic fashion.
     * @param methodOrConstructor the Method or Constructor to specify a parameter for
     * @param parameterIndex the index of the parameter
     * @return the corresponding MethodParameter instance
     */
    public static SimpleParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex) {
        if (methodOrConstructor instanceof Method) {
            return new SimpleParameter((Method) methodOrConstructor, parameterIndex);
        }
        else if (methodOrConstructor instanceof Constructor) {
            return new SimpleParameter((Constructor<?>) methodOrConstructor, parameterIndex);
        }
        else {
            throw new IllegalArgumentException(
                    "Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
        }
    }

}