package com.jn.langx.util.os;

import com.jn.langx.util.logging.Loggers;

import java.lang.reflect.Method;

/**
 * Provide for a Uptime class that is compatible with Android, GAE, and the new Java 8 compact profiles
 */
public class Uptime {
    public static final int NOIMPL = -1;

    public interface Impl {
        long getUptime();
    }

    public static class DefaultImpl implements Uptime.Impl {
        public Object mxBean;
        public Method uptimeMethod;

        public DefaultImpl() {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            try {
                final Class<?> mgmtFactory = Class.forName("java.lang.management.ManagementFactory", true, cl);
                final Class<?> runtimeClass = Class.forName("java.lang.management.RuntimeMXBean", true, cl);
                final Class<?>[] noParams = new Class<?>[0];
                final Method mxBeanMethod = mgmtFactory.getMethod("getRuntimeMXBean", noParams);
                if (mxBeanMethod == null) {
                    throw new UnsupportedOperationException("method getRuntimeMXBean() not found");
                }
                mxBean = mxBeanMethod.invoke(mgmtFactory);
                if (mxBean == null) {
                    throw new UnsupportedOperationException("getRuntimeMXBean() method returned null");
                }
                uptimeMethod = runtimeClass.getMethod("getUptime", noParams);
                if (uptimeMethod == null) {
                    throw new UnsupportedOperationException("method getUptime() not found");
                }
            } catch (Exception e) {
                throw new UnsupportedOperationException("Implementation not available in this environment", e);
            }
        }

        @Override
        public long getUptime() {
            try {
                return (Long) uptimeMethod.invoke(mxBean);
            } catch (Exception e) {
                return NOIMPL;
            }
        }
    }

    private static final Uptime INSTANCE = new Uptime();

    public static Uptime getInstance() {
        return INSTANCE;
    }

    private Uptime.Impl impl;

    private Uptime() {
        try {
            impl = new Uptime.DefaultImpl();
        } catch (UnsupportedOperationException e) {
            Loggers.getLogger(Uptime.class).error("Defaulting Uptime to NOIMPL due to {} {}", e.getClass().getName(), e.getMessage());
            impl = null;
        }
    }

    public Uptime.Impl getImpl() {
        return impl;
    }

    public void setImpl(Uptime.Impl impl) {
        this.impl = impl;
    }

    public static long getUptime() {
        Uptime u = getInstance();
        if (u.impl == null) {
            return NOIMPL;
        }
        return u.impl.getUptime();
    }
}
