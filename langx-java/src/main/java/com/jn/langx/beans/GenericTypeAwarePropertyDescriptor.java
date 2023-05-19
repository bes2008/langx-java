package com.jn.langx.beans;

import com.jn.langx.beans.propertyeditor.PropertyDescriptors;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.GenericTypeResolver;
import com.jn.langx.util.reflect.type.SimpleParameter;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 4.3.7
 */
final class GenericTypeAwarePropertyDescriptor extends PropertyDescriptor {

    private final Class<?> beanClass;

    private final Method readMethod;

    private final Method writeMethod;

    private volatile Set<Method> ambiguousWriteMethods;

    private SimpleParameter writeMethodParameter;

    private Class<?> propertyType;

    private final Class<?> propertyEditorClass;


    public GenericTypeAwarePropertyDescriptor(Class<?> beanClass, String propertyName,
                                              Method readMethod, Method writeMethod, Class<?> propertyEditorClass)
            throws IntrospectionException {

        super(propertyName, null, null);

        if (beanClass == null) {
            throw new IntrospectionException("Bean class must not be null");
        }
        this.beanClass = beanClass;

        Method readMethodToUse = BridgeMethodResolver.findBridgedMethod(readMethod);
        Method writeMethodToUse = BridgeMethodResolver.findBridgedMethod(writeMethod);
        if (writeMethodToUse == null && readMethodToUse != null) {
            // Fallback: Original JavaBeans introspection might not have found matching setter
            // method due to lack of bridge method resolution, in case of the getter using a
            // covariant return type whereas the setter is defined for the concrete property type.
            Method candidate = Reflects.getMethodIfAvailable(
                    this.beanClass, "set" + Strings.capitalize(getName()), (Class<?>[]) null);
            if (candidate != null && candidate.getParameterTypes().length == 1) {
                writeMethodToUse = candidate;
            }
        }
        this.readMethod = readMethodToUse;
        this.writeMethod = writeMethodToUse;

        if (this.writeMethod != null) {
            if (this.readMethod == null) {
                // Write method not matched against read method: potentially ambiguous through
                // several overloaded variants, in which case an arbitrary winner has been chosen
                // by the JDK's JavaBeans Introspector...
                Set<Method> ambiguousCandidates = new HashSet<Method>();
                for (Method method : beanClass.getMethods()) {
                    if (method.getName().equals(writeMethodToUse.getName()) &&
                            !method.equals(writeMethodToUse) && !method.isBridge() &&
                            method.getParameterTypes().length == writeMethodToUse.getParameterTypes().length) {
                        ambiguousCandidates.add(method);
                    }
                }
                if (!ambiguousCandidates.isEmpty()) {
                    this.ambiguousWriteMethods = ambiguousCandidates;
                }
            }
            this.writeMethodParameter = new SimpleParameter(this.writeMethod, 0);
            GenericTypeResolver.resolveParameterType(this.writeMethodParameter, this.beanClass);
        }

        if (this.readMethod != null) {
            this.propertyType = GenericTypeResolver.resolveReturnType(this.readMethod, this.beanClass);
        } else if (this.writeMethodParameter != null) {
            this.propertyType = this.writeMethodParameter.getParameterType();
        }

        this.propertyEditorClass = propertyEditorClass;
    }


    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public synchronized Method getReadMethod() {
        return this.readMethod;
    }

    @Override
    public synchronized Method getWriteMethod() {
        return this.writeMethod;
    }

    Method getWriteMethodForActualAccess() {
        Set<Method> ambiguousCandidates = this.ambiguousWriteMethods;
        if (ambiguousCandidates != null) {
            this.ambiguousWriteMethods = null;
            Loggers.getLogger(GenericTypeAwarePropertyDescriptor.class).warn("Invalid JavaBean property '{}' being accessed! Ambiguous write methods found next to actually used [{}]: {}", getName(), this.writeMethod, ambiguousCandidates);
        }
        return this.writeMethod;
    }

    SimpleParameter getWriteMethodParameter() {
        return this.writeMethodParameter;
    }

    @Override
    public synchronized Class<?> getPropertyType() {
        return this.propertyType;
    }

    @Override
    public Class<?> getPropertyEditorClass() {
        return this.propertyEditorClass;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GenericTypeAwarePropertyDescriptor)) {
            return false;
        }
        GenericTypeAwarePropertyDescriptor otherPd = (GenericTypeAwarePropertyDescriptor) other;
        return (getBeanClass().equals(otherPd.getBeanClass()) && PropertyDescriptors.equals(this, otherPd));
    }

    @Override
    public int hashCode() {
        return Objs.hash(getBeanClass(), getReadMethod(), getWriteMethod());
    }



}
