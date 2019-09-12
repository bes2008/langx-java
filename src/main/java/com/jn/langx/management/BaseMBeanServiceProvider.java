/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.langx.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

public class BaseMBeanServiceProvider {
    private static final Logger logger;
    protected static final Map<Class<?>, MBeanService> provider;

    public static <S extends MBeanService> S getService(final Class<S> serviceClazz, final ClassLoader classLoader) {
        if (serviceClazz == null) {
            return null;
        }
        final MBeanService service = (MBeanService) BaseMBeanServiceProvider.provider.get(serviceClazz);
        if (service != null && serviceClazz.isAssignableFrom(service.getClass())) {
            return (S) service;
        }
        final ClassLoader originalCL = Thread.currentThread().getContextClassLoader();
        try {
            if (classLoader != null) {
                Thread.currentThread().setContextClassLoader(classLoader);
            }
            final ServiceLoader<S> loader = ServiceLoader.load(serviceClazz);
            final Iterator<S> iter = loader.iterator();
            while (iter.hasNext()) {
                try {
                    final S svc = (S) iter.next();
                    if (svc.isServiceMatch()) {
                        BaseMBeanServiceProvider.provider.put((Class<?>) serviceClazz, svc);
                        return svc;
                    }
                    continue;
                } catch (Throwable ex) {
                    BaseMBeanServiceProvider.logger.warn("Exception occured when get the service [" + serviceClazz + "]", ex);
                }
            }
            BaseMBeanServiceProvider.logger.warn("Can't find the service [" + serviceClazz + "]");
        } finally {
            if (classLoader != null) {
                Thread.currentThread().setContextClassLoader(originalCL);
            }
        }
        return null;
    }

    static {
        logger = LoggerFactory.getLogger((Class) BaseMBeanServiceProvider.class);
        provider = new HashMap<Class<?>, MBeanService>();
    }
}
