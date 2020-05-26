package com.jn.langx.util.reflect.parameter;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.Modifiers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Java6ConstructorParameter implements ConstructorParameter {
    private final String name;
    private final int modifiers;
    private final int index;
    private final Constructor constructor;

    /**
     * Package-private constructor for {@code Parameter}.
     * <p>
     * If method parameter data is present in the classfile, then the
     * JVM creates {@code Parameter} objects directly.  If it is
     * absent, however, then {@code Executable} uses this constructor
     * to synthesize them.
     *
     * @param name        The name of the parameter.
     * @param modifiers   The modifier flags for the parameter.
     * @param constructor The executable which defines this parameter.
     * @param index       The index of the parameter.
     */
    Java6ConstructorParameter(String name, int modifiers, Constructor constructor, int index) {
        this.name = name;
        this.modifiers = modifiers;
        this.constructor = constructor;
        this.index = index;
    }

    /**
     * Compares based on the executable and the index.
     *
     * @param obj The object to compare.
     * @return Whether or not this is equal to the argument.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Java6MethodParameter) {
            Java6ConstructorParameter other = (Java6ConstructorParameter) obj;
            return (other.constructor.equals(constructor) && other.index == index);
        }
        return false;
    }

    /**
     * Returns a hash code based on the executable's hash code and the
     * index.
     *
     * @return A hash code based on the executable's hash code.
     */
    public int hashCode() {
        return constructor.hashCode() ^ index;
    }

    /**
     * Returns true if the parameter has a name according to the class
     * file; returns false otherwise. Whether a parameter has a name
     * is determined by the {@literal MethodParameters} attribute of
     * the method which declares the parameter.
     *
     * @return true if and only if the parameter has a name according
     * to the class file.
     */
    public boolean isNamePresent() {
        return name != null;
    }

    /**
     * Returns a string describing this parameter.  The format is the
     * modifiers for the parameter, if any, in canonical order as
     * recommended by <cite>The Java&trade; Language
     * Specification</cite>, followed by the fully- qualified type of
     * the parameter (excluding the last [] if the parameter is
     * variable arity), followed by "..." if the parameter is variable
     * arity, followed by a space, followed by the name of the
     * parameter.
     *
     * @return A string representation of the parameter and associated
     * information.
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final Type type = this.getType();
        final String typename = type.toString();

        sb.append(Modifier.toString(getModifiers()));

        if (0 != modifiers)
            sb.append(' ');

        if (isVarArgs())
            sb.append(typename.replaceFirst("\\[\\]$", "..."));
        else
            sb.append(typename);

        sb.append(' ');
        sb.append(getName());

        return sb.toString();
    }

    /**
     * Return the {@code Executable} which declares this parameter.
     *
     * @return The {@code Executable} declaring this parameter.
     */
    @Override
    public Constructor getDeclaringExecutable() {
        return constructor;
    }

    /**
     * Get the modifier flags for this the parameter represented by
     * this {@code Parameter} object.
     *
     * @return The modifier flags for this parameter.
     */
    public int getModifiers() {
        return modifiers;
    }

    /**
     * Returns the name of the parameter.  If the parameter's name is
     * {@linkplain #isNamePresent() present}, then this method returns
     * the name provided by the class file. Otherwise, this method
     * synthesizes a name of the form argN, where N is the index of
     * the parameter in the descriptor of the method which declares
     * the parameter.
     *
     * @return The name of the parameter, either provided by the class
     * file or synthesized if the class file does not provide
     * a name.
     */
    public String getName() {
        // Note: empty strings as paramete names are now outlawed.
        // The .equals("") is for compatibility with current JVM
        // behavior.  It may be removed at some point.
        if (name == null || name.equals(""))
            return "arg" + index;
        else
            return name;
    }

    // Package-private accessor to the real name field.
    String getRealName() {
        return name;
    }


    /**
     * Returns a {@code Class} object that identifies the
     * declared type for the parameter represented by this
     * {@code Parameter} object.
     *
     * @return a {@code Class} object identifying the declared
     * type of the parameter represented by this object
     */
    public Class<?> getType() {
        Class<?> tmp = parameterClassCache;
        if (null == tmp) {
            tmp = constructor.getParameterTypes()[index];
            parameterClassCache = tmp;
        }
        return tmp;
    }


    private transient volatile Class<?> parameterClassCache = null;

    /**
     * Returns {@code true} if this parameter is implicitly declared
     * in source code; returns {@code false} otherwise.
     *
     * @return true if and only if this parameter is implicitly
     * declared as defined by <cite>The Java&trade; Language
     * Specification</cite>.
     */
    public boolean isImplicit() {
        return Modifiers.isMandated(getModifiers());
    }

    /**
     * Returns {@code true} if this parameter is neither implicitly
     * nor explicitly declared in source code; returns {@code false}
     * otherwise.
     *
     * @return true if and only if this parameter is a synthetic
     * construct as defined by
     * <cite>The Java&trade; Language Specification</cite>.
     * @jls 13.1 The Form of a Binary
     */
    public boolean isSynthetic() {
        return Modifiers.isSynthetic(getModifiers());
    }

    /**
     * Returns {@code true} if this parameter represents a variable
     * argument list; returns {@code false} otherwise.
     *
     * @return {@code true} if an only if this parameter represents a
     * variable argument list.
     */
    public boolean isVarArgs() {
        return constructor.isVarArgs() && index == constructor.getParameterTypes().length - 1;
    }


    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException {@inheritDoc}
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        Preconditions.checkNotNull(annotationClass);
        return annotationClass.cast(declaredAnnotations().get(annotationClass));
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        Annotation annotation = declaredAnnotations().get(annotationClass);
        return annotation != null;
    }

    /**
     * {@inheritDoc}
     */
    public Annotation[] getDeclaredAnnotations() {
        return constructor.getParameterAnnotations()[index];
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
        // Only annotations on classes are inherited, for all other
        // objects getDeclaredAnnotation is the same as
        // getAnnotation.
        return getAnnotation(annotationClass);
    }

    @Override
    public Type getParameterizedType() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }

    private transient Map<Class<? extends Annotation>, Annotation> declaredAnnotations;

    private synchronized Map<Class<? extends Annotation>, Annotation> declaredAnnotations() {
        if (null == declaredAnnotations) {
            declaredAnnotations = new HashMap<Class<? extends Annotation>, Annotation>();
            Annotation[] ann = getDeclaredAnnotations();
            for (int i = 0; i < ann.length; i++) {
                declaredAnnotations.put(ann[i].annotationType(), ann[i]);
            }
        }
        return declaredAnnotations;
    }

}
