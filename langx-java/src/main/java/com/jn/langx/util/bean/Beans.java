package com.jn.langx.util.bean;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.beans.BeansException;
import com.jn.langx.beans.CachedIntrospectionResults;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Beans {
    private Beans() {
    }

    public static <BEAN, CI extends Iterable<BEAN>, O> List<O> getFieldList(CI beans, Function<BEAN, O> fieldExtractor) {
        return Pipeline.of(beans).map(fieldExtractor).asList();
    }

    public static <BEAN, CI extends Iterable<BEAN>, O> List<O> getFieldList(CI beans, Predicate<BEAN> predicate, Function<BEAN, O> fieldExtractor) {
        return Pipeline.of(beans).filter(predicate).map(fieldExtractor).asList();
    }

    public static <BEAN, CI extends Iterable<BEAN>, O> Set<O> getFieldSet(CI collection, Function<BEAN, O> function) {
        return Pipeline.of(collection).map(function).collect(Collects.<O>toHashSet(true));
    }

    public static <KEY, BEAN> Map<KEY, BEAN> asHashMap(Iterable<BEAN> collection, Function<BEAN, KEY> keySupplier) {
        return Collects.collect(collection, Collects.toHashMap(keySupplier, Functions.<BEAN>noopFunction(), false));
    }

    public static <KEY, BEAN> Map<KEY, BEAN> asLinkedHashMap(Iterable<BEAN> collection, Function<BEAN, KEY> keySupplier) {
        return Collects.collect(collection, Collects.toHashMap(keySupplier, Functions.<BEAN>noopFunction(), true));
    }

    public static <KEY, BEAN> Map<KEY, BEAN> asTreeMap(Iterable<BEAN> collection, Function<BEAN, KEY> keySupplier) {
        return Collects.collect(collection, Collects.toHashMap(keySupplier, Functions.<BEAN>noopFunction(), true));
    }


    /**
     * Copy the property values of the given source bean into the target bean.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     * consider using a full BeanWrapper.
     * @param source the source bean
     * @param target the target bean
  //   * @throws BeansException if the copying failed
 //    * @see BeanWrapper
     */
   // public static void copyProperties(Object source, Object target) throws BeansException {
   //     copyProperties(source, target, null, (String[]) null);
   // }

    /**
     * Copy the property values of the given source bean into the given target bean.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * @param source the source bean
     * @param target the target bean
     * @param editable the class (or interface) to restrict property setting to
     * @param ignoreProperties array of property names to ignore
     * @throws BeansException if the copying failed
     */
    private static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties)
            throws BeansException {

        Preconditions.checkNotNull(source, "Source must not be null");
        Preconditions.checkNotNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null && Reflects.isSubClassOrEquals(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                        catch (Throwable ex) {
                            throw new BeansException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * Retrieve the JavaBeans {@code PropertyDescriptor}s of a given class.
     * @param clazz the Class to retrieve the PropertyDescriptors for
     * @return an array of {@code PropertyDescriptors} for the given class
     * @throws BeansException if PropertyDescriptor look fails
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws BeansException {
        CachedIntrospectionResults cr = CachedIntrospectionResults.forClass(clazz);
        return cr.getPropertyDescriptors();
    }

    /**
     * Retrieve the JavaBeans {@code PropertyDescriptors} for the given property.
     * @param clazz the Class to retrieve the PropertyDescriptor for
     * @param propertyName the name of the property
     * @return the corresponding PropertyDescriptor, or {@code null} if none
     * @throws BeansException if PropertyDescriptor lookup fails
     */
    @Nullable
    public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) throws BeansException {
        return CachedIntrospectionResults.forClass(clazz).getPropertyDescriptor(propertyName);
    }
}
