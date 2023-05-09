/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors as indicated
 * by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jn.langx.util.concurrent;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * A type-safe enum for the BasicThreadPool blocking mode.
 */
public class BlockingMode implements Serializable {
    private static final long serialVersionUID = -1L;

    public static final int RUN_TYPE = 0;
    public static final int WAIT_TYPE = 1;
    public static final int DISCARD_TYPE = 2;
    public static final int DISCARD_OLDEST_TYPE = 3;
    public static final int ABORT_TYPE = 4;

    /**
     * Set the policy for blocked execution to be that the current thread
     * executes the command if there are no available threads in the pool.
     */
    public static final BlockingMode RUN = new BlockingMode("run", RUN_TYPE);
    /**
     * Set the policy for blocked execution to be to wait until a thread
     * is available, unless the pool has been shut down, in which case
     * the action is discarded.
     */
    public static final BlockingMode WAIT = new BlockingMode("wait", WAIT_TYPE);
    /**
     * Set the policy for blocked execution to be to return without
     * executing the request.
     */
    public static final BlockingMode DISCARD = new BlockingMode("discard", DISCARD_TYPE);
    /**
     * Set the policy for blocked execution to be to discard the oldest
     * unhandled request
     */
    public static final BlockingMode DISCARD_OLDEST =
            new BlockingMode("discardOldest", DISCARD_OLDEST_TYPE);
    /**
     * Set the policy for blocked execution to be to throw an AbortWhenBlocked
     * (a subclass of RuntimeException).
     */
    public static final BlockingMode ABORT = new BlockingMode("abort", ABORT_TYPE);

    /**
     * The string form of the enum
     */
    private final transient String name;
    /**
     * The enum manifest constant
     */
    private final int type;

    /**
     * A utility method to convert a string name to a BlockingMode
     *
     * @return The associated BlockingMode constant if name is valid, null otherwise
     */
    public static final BlockingMode toBlockingMode(String name) {
        BlockingMode mode = null;
        if (name == null) {
            mode = null;
        } else if (name.equalsIgnoreCase("run")) {
            mode = RUN;
        } else if (name.equalsIgnoreCase("wait")) {
            mode = WAIT;
        } else if (name.equalsIgnoreCase("discard")) {
            mode = DISCARD;
        } else if (name.equalsIgnoreCase("discardOldest")) {
            mode = DISCARD_OLDEST;
        } else if (name.equalsIgnoreCase("abort")) {
            mode = ABORT;
        }
        return mode;
    }

    private BlockingMode(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String toString() {
        return name;
    }

    /**
     * Overriden to return the indentity instance of BlockingMode based on the
     * stream type int value. This ensures that BlockingMode enums can be
     * compared using ==.
     *
     * @return The BlockingMode instance for the XXX_TYPE int.
     */
    Object readResolve() throws ObjectStreamException {
        // Replace the marshalled instance type with the local instance
        BlockingMode mode = ABORT;
        switch (type) {
            case RUN_TYPE:
            case WAIT_TYPE:
            case DISCARD_TYPE:
            case DISCARD_OLDEST_TYPE:
            case ABORT_TYPE:
                mode = RUN;
                break;
        }
        return mode;
    }
}
