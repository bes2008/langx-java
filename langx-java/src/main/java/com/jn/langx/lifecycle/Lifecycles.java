/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.jn.langx.lifecycle;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.util.Collection;


/**
 * Utility class to help call {@link Initializable#init() Initializable.init()} and
 * {@link Destroyable#destroy() Destroyable.destroy()} methods cleanly on any object.
 *
 * @since 3.4.2
 */
public abstract class Lifecycles {
    private Lifecycles(){

    }

    public static void init(Object o) {
        if (o instanceof Initializable) {
            init((Initializable) o);
        }
    }

    public static void init(Initializable initializable) {
        initializable.init();
    }

    /**
     * Calls {@link #init(Object) init} for each object in the collection.  If the collection is {@code null} or empty,
     * this method returns quietly.
     *
     * @param c the collection containing objects to {@link #init init}.
     * @throws InitializationException if unable to initialize one or more instances.
     * @since 0.9
     */
    public static void init(Collection c) {
        if (Emptys.isEmpty(c)) {
            return;
        }
        for (Object o : c) {
            init(o);
        }
    }

    public static void destroy(Object o) {
        if (o instanceof Destroyable) {
            destroy((Destroyable) o);
        } else if (o instanceof Collection) {
            destroy((Collection) o);
        }
    }

    public static void destroy(Destroyable d) {
        if (d != null) {
            try {
                d.destroy();
            } catch (Throwable t) {
                Logger logger = Loggers.getLogger(Lifecycles.class);
                if (logger.isDebugEnabled()) {
                    String msg = "Unable to cleanly destroy instance [" + d + "] of type [" + d.getClass().getName() + "].";
                    logger.debug(msg, t);
                }
            }
        }
    }

    /**
     * Calls {@link #destroy(Object) destroy} for each object in the collection.
     * If the collection is {@code null} or empty, this method returns quietly.
     *
     * @param c the collection of objects to destroy.
     * @since 0.9
     */
    public static void destroy(Collection c) {
        if (Emptys.isEmpty(c)) {
            return;
        }

        for (Object o : c) {
            destroy(o);
        }
    }
}
