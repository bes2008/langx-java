package com.jn.langx.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

public class MethodAttributes implements MemberAttributes<Method> {
    private Method method;

    public MethodAttributes(Method method) {
        this.method = method;
    }

    @Override
    public int getModifier() {
        return method.getModifiers();
    }

    @Override
    public Method get() {
        return method;
    }

    /**
     * @return the declaring class that contains this field
     */
    public Class<?> getDeclaringClass() {
        return method.getDeclaringClass();
    }

    /**
     * @return the name of the field
     */
    public String getName() {
        return method.getName();
    }


    /**
     * Return the {@code T} annotation object from this field if it exist; otherwise returns
     * {@code null}.
     *
     * @param annotation the class of the annotation that will be retrieved
     * @return the annotation instance if it is bound to the field; otherwise {@code null}
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    /**
     * Return the annotations that are present on this field.
     *
     * @return an array of all the annotations set on the field
     * @since 1.4
     */
    public Collection<Annotation> getAnnotations() {
        return Arrays.asList(method.getAnnotations());
    }

    /**
     * Returns {@code true} if the field is defined with the {@code modifier}.
     * <p>
     * <p>This method is meant to be called as:
     * <pre class="code">
     * boolean hasPublicModifier = fieldAttribute.hasModifier(java.lang.reflect.Modifier.PUBLIC);
     * </pre>
     *
     * @see java.lang.reflect.Modifier
     */
    public boolean hasModifier(int modifier) {
        return Modifiers.hasModifier(method, modifier);
    }

    /**
     * This is exposed internally only for the removing synthetic fields from the JSON output.
     *
     * @return true if the field is synthetic; otherwise false
     */
    boolean isSynthetic() {
        return method.isSynthetic();
    }
}
