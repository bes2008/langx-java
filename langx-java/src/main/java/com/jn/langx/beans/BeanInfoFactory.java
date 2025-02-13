package com.jn.langx.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

/**
 * @since 4.3.7
 */
public interface BeanInfoFactory {

    /**
     * Return the bean info for the given class, if supported.
     * <p>
     * This method is used to obtain BeanInfo for a specified class. If the class is supported, it returns the corresponding BeanInfo;
     * otherwise, it returns null. This allows the caller to obtain detailed information about the bean's properties, methods, and events.
     *
     * @param beanClass the bean class
     *        This is the class for which to retrieve BeanInfo. It is the basis for introspection to obtain bean information.
     * @return the BeanInfo, or {@code null} if the given class is not supported
     *         Returns the BeanInfo object if the class is supported; otherwise, returns null.
     * @throws IntrospectionException in case of exceptions
     *         Throws an IntrospectionException if there is an exception during the introspection process.
     */
    BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException;

}
