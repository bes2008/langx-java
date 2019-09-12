package com.jn.langx.management;

import javax.management.*;
import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

public abstract class JMXConnection implements Closeable {
    protected MBeanServerConnection conn;

    public MBeanServerConnection getRealConn() {
        return this.conn;
    }

    public JMXConnection(final MBeanServerConnection conn) {
        this.conn = conn;
    }

    public ObjectInstance createMBean(final String className, final ObjectName name) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.createMBean(className, name);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public ObjectInstance createMBean(final String className, final ObjectName name, final ObjectName loaderName) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.createMBean(className, name, loaderName);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public ObjectInstance createMBean(final String className, final ObjectName name, final Object[] params, final String[] signature) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.createMBean(className, name, params, signature);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public ObjectInstance createMBean(final String className, final ObjectName name, final ObjectName loaderName, final Object[] params, final String[] signature) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.createMBean(className, name, loaderName, params, signature);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public void unregisterMBean(final ObjectName name) throws com.jn.langx.management.MBeanException {
        try {
            this.conn.unregisterMBean(name);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public ObjectInstance getObjectInstance(final ObjectName name) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.getObjectInstance(name);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public Set<ObjectInstance> queryMBeans(final ObjectName name, final QueryExp query) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.queryMBeans(name, query);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public Set<ObjectName> queryNames(final ObjectName name, final QueryExp query) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.queryNames(name, query);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public boolean isRegistered(final ObjectName name) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.isRegistered(name);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public Integer getMBeanCount() throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.getMBeanCount();
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public Object getAttribute(final ObjectName name, final String attribute) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.getAttribute(name, attribute);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public AttributeList getAttributes(final ObjectName name, final String[] attributes) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.getAttributes(name, attributes);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public void setAttribute(final ObjectName name, final Attribute attribute) throws com.jn.langx.management.MBeanException {
        try {
            this.conn.setAttribute(name, attribute);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public AttributeList setAttributes(final ObjectName name, final AttributeList attributes) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.setAttributes(name, attributes);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public Object invoke(final ObjectName name, final String operationName, final Object[] params, final String[] signature) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.invoke(name, operationName, params, signature);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public String getDefaultDomain() throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.getDefaultDomain();
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public String[] getDomains() throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.getDomains();
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public void addNotificationListener(final ObjectName name, final NotificationListener listener, final NotificationFilter filter, final Object handback) throws com.jn.langx.management.MBeanException {
        try {
            this.conn.addNotificationListener(name, listener, filter, handback);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public void addNotificationListener(final ObjectName name, final ObjectName listener, final NotificationFilter filter, final Object handback) throws com.jn.langx.management.MBeanException {
        try {
            this.conn.addNotificationListener(name, listener, filter, handback);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public void removeNotificationListener(final ObjectName name, final ObjectName listener) throws com.jn.langx.management.MBeanException {
        try {
            this.conn.removeNotificationListener(name, listener);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public void removeNotificationListener(final ObjectName name, final ObjectName listener, final NotificationFilter filter, final Object handback) throws com.jn.langx.management.MBeanException {
        try {
            this.conn.removeNotificationListener(name, listener, filter, handback);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public void removeNotificationListener(final ObjectName name, final NotificationListener listener) throws com.jn.langx.management.MBeanException {
        try {
            this.conn.removeNotificationListener(name, listener);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public void removeNotificationListener(final ObjectName name, final NotificationListener listener, final NotificationFilter filter, final Object handback) throws com.jn.langx.management.MBeanException {
        try {
            this.conn.removeNotificationListener(name, listener, filter, handback);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public MBeanInfo getMBeanInfo(final ObjectName name) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.getMBeanInfo(name);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public boolean isInstanceOf(final ObjectName name, final String className) throws com.jn.langx.management.MBeanException {
        try {
            return this.conn.isInstanceOf(name, className);
        } catch (Throwable ex) {
            throw new com.jn.langx.management.MBeanException(ex);
        }
    }

    public boolean hasMBean(final ObjectName oname) {
        boolean hasObject = false;
        try {
            final MBeanInfo mbeanInfo = this.conn.getMBeanInfo(oname);
            if (mbeanInfo != null) {
                hasObject = true;
            }
        } catch (Throwable t) {
        }
        return hasObject;
    }

    public boolean hasAttribute(final ObjectName oname, final String attributeName) throws com.jn.langx.management.MBeanException {
        boolean hasAttribute = false;
        try {
            final MBeanInfo mbeanInfo = this.getMBeanInfo(oname);
            final MBeanAttributeInfo[] arr$;
            final MBeanAttributeInfo[] attrInfoes = arr$ = mbeanInfo.getAttributes();
            for (final MBeanAttributeInfo attr : arr$) {
                if (attr.getName().equals(attributeName)) {
                    hasAttribute = true;
                    break;
                }
            }
        } catch (Throwable ex) {
            throw new MBeanException(ex);
        }
        return hasAttribute;
    }

    @Override
    public abstract void close() throws IOException;
}
