package com.jn.langx.util.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

public interface BeanInfoFactory {

    /**
     * Return the bean info for the given class, if supported.
     * @param beanClass the bean class
     * @return the BeanInfo, or {@code null} if the given class is not supported
     * @throws IntrospectionException in case of exceptions
     */
    BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException;

}
