package com.jn.langx.jndi.template;

import com.jn.langx.util.function.Supplier0;

import javax.naming.NamingException;

/**
 * A factory implementation intended to be used to look up objects in jndi.
 *
 * @since 2.10.0
 */
public class JndiObjectFactory<T> extends JndiLocator implements Supplier0<T> {

    private String resourceName;
    private Class<? extends T> requiredType;

    public T get() {
        try {
            if(requiredType != null) {
                return requiredType.cast(this.lookup(resourceName, requiredType));
            } else {
                return (T) this.lookup(resourceName);
            }
        } catch (NamingException e) {
            final String typeName = requiredType != null ? requiredType.getName() : "object";
            throw new IllegalStateException("Unable to look up " + typeName + " with jndi name '" + resourceName + "'.", e);
        }
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Class<? extends T> getRequiredType() {
        return requiredType;
    }

    public void setRequiredType(Class<? extends T> requiredType) {
        this.requiredType = requiredType;
    }
}
