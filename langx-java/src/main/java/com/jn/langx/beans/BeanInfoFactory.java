package com.jn.langx.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

/**
 * @since 4.3.7
 */
public interface BeanInfoFactory {

    /**
     * Return the bean info for the given class, if supported.
     * @param beanClass the bean class
     * @return the BeanInfo, or {@code null} if the given class is not supported
     * @throws IntrospectionException in case of exceptions
     */
    BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException;

}
