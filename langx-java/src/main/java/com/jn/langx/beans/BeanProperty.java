package com.jn.langx.beans;


import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.GenericTypeResolver;
import com.jn.langx.util.reflect.type.SimpleParameter;

/**
 * A description of a JavaBeans Property that allows us to avoid a dependency on
 * {@code java.beans.PropertyDescriptor}. The {@code java.beans} package
 * is not available in a number of environments (e.g. Android, Java ME), so this is
 * desirable for portability of Spring's core conversion facility.
 *
 * <p>Used to build a {@link TypeDescriptor} from a property location. The built
 * {@code TypeDescriptor} can then be used to convert from/to the property type.
 *
 * @see TypeDescriptor#TypeDescriptor(BeanProperty)
 * @see java.beans.PropertyDescriptor
 *
 * @since 4.3.7
 */
final class BeanProperty {

    private static Map<BeanProperty, Annotation[]> annotationCache = new ConcurrentReferenceHashMap<BeanProperty, Annotation[]>();

    private final Class<?> objectType;

    private final Method readMethod;

    private final Method writeMethod;

    private final String name;

    private final SimpleParameter methodParameter;

    private Annotation[] annotations;


    public BeanProperty(Class<?> objectType, Method readMethod, Method writeMethod) {
        this(objectType, readMethod, writeMethod, null);
    }

    public BeanProperty(Class<?> objectType, Method readMethod, Method writeMethod, String name) {
        this.objectType = objectType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.methodParameter = resolveMethodParameter();
        this.name = (name != null ? name : resolveName());
    }


    /**
     * The object declaring this property, either directly or in a superclass the object extends.
     */
    public Class<?> getObjectType() {
        return this.objectType;
    }

    /**
     * The name of the property: e.g. 'foo'
     */
    public String getName() {
        return this.name;
    }

    /**
     * The property type: e.g. {@code java.lang.String}
     */
    public Class<?> getType() {
        return this.methodParameter.getParameterType();
    }

    /**
     * The property getter method: e.g. {@code getFoo()}
     */
    public Method getReadMethod() {
        return this.readMethod;
    }

    /**
     * The property setter method: e.g. {@code setFoo(String)}
     */
    public Method getWriteMethod() {
        return this.writeMethod;
    }


    // package private

    SimpleParameter getMethodParameter() {
        return this.methodParameter;
    }

    Annotation[] getAnnotations() {
        if (this.annotations == null) {
            this.annotations = resolveAnnotations();
        }
        return this.annotations;
    }


    // internal helpers

    private String resolveName() {
        if (this.readMethod != null) {
            int index = this.readMethod.getName().indexOf("get");
            if (index != -1) {
                index += 3;
            } else {
                index = this.readMethod.getName().indexOf("is");
                if (index == -1) {
                    throw new IllegalArgumentException("Not a getter method");
                }
                index += 2;
            }
            return Strings.uncapitalize(this.readMethod.getName().substring(index));
        } else {
            int index = this.writeMethod.getName().indexOf("set");
            if (index == -1) {
                throw new IllegalArgumentException("Not a setter method");
            }
            index += 3;
            return Strings.uncapitalize(this.writeMethod.getName().substring(index));
        }
    }

    private SimpleParameter resolveMethodParameter() {
        SimpleParameter read = resolveReadMethodParameter();
        SimpleParameter write = resolveWriteMethodParameter();
        if (write == null) {
            if (read == null) {
                throw new IllegalStateException("Property is neither readable nor writeable");
            }
            return read;
        }
        if (read != null) {
            Class<?> readType = read.getParameterType();
            Class<?> writeType = write.getParameterType();
            if (!writeType.equals(readType) && writeType.isAssignableFrom(readType)) {
                return read;
            }
        }
        return write;
    }

    private SimpleParameter resolveReadMethodParameter() {
        if (getReadMethod() == null) {
            return null;
        }
        return resolveParameterType(new SimpleParameter(getReadMethod(), -1));
    }

    private SimpleParameter resolveWriteMethodParameter() {
        if (getWriteMethod() == null) {
            return null;
        }
        return resolveParameterType(new SimpleParameter(getWriteMethod(), 0));
    }

    private SimpleParameter resolveParameterType(SimpleParameter parameter) {
        // needed to resolve generic property types that parameterized by sub-classes e.g. T getFoo();
        GenericTypeResolver.resolveParameterType(parameter, getObjectType());
        return parameter;
    }

    private Annotation[] resolveAnnotations() {
        Annotation[] annotations = annotationCache.get(this);
        if (annotations == null) {
            Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap<Class<? extends Annotation>, Annotation>();
            addAnnotationsToMap(annotationMap, getReadMethod());
            addAnnotationsToMap(annotationMap, getWriteMethod());
            addAnnotationsToMap(annotationMap, getField());
            annotations = annotationMap.values().toArray(new Annotation[0]);
            annotationCache.put(this, annotations);
        }
        return annotations;
    }

    private void addAnnotationsToMap(
            Map<Class<? extends Annotation>, Annotation> annotationMap, AnnotatedElement object) {

        if (object != null) {
            for (Annotation annotation : object.getAnnotations()) {
                annotationMap.put(annotation.annotationType(), annotation);
            }
        }
    }

    private Field getField() {
        String name = getName();
        if (Strings.isEmpty(name)) {
            return null;
        }
        Class<?> declaringClass = declaringClass();
        Field field = Reflects.findField(declaringClass, name);
        if (field == null) {
            // Same lenient fallback checking as in CachedIntrospectionResults...
            field = Reflects.findField(declaringClass, Strings.uncapitalize(name));
            if (field == null) {
                field = Reflects.findField(declaringClass, Strings.capitalize(name));
            }
        }
        return field;
    }

    private Class<?> declaringClass() {
        if (getReadMethod() != null) {
            return getReadMethod().getDeclaringClass();
        } else {
            return getWriteMethod().getDeclaringClass();
        }
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BeanProperty)) {
            return false;
        }
        BeanProperty otherProperty = (BeanProperty) other;
        return (Objs.deepEquals(this.objectType, otherProperty.objectType) &&
                Objs.deepEquals(this.name, otherProperty.name) &&
                Objs.deepEquals(this.readMethod, otherProperty.readMethod) &&
                Objs.deepEquals(this.writeMethod, otherProperty.writeMethod));
    }

    @Override
    public int hashCode() {
        return Objs.hash(this.objectType,this.name);
    }

}
