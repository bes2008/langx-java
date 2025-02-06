/*
 * Copyright (c) 2008-2021, Hazelcast, Inc. All Rights Reserved.
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

import java.util.concurrent.TimeUnit;

/**
 * Interface for runnable with timeout value
 * This interface extends Runnable and adds the concept of timeout, allowing the Runnable task to specify its timeout value and time unit.
 */
public interface TimeoutRunnable extends Runnable {

    /**
     * Gets the timeout value of the task
     *
     * @return The timeout value in the specified time unit
     */
    long getTimeout();

    /**
     * Gets the time unit of the timeout value
     *
     * @return The time unit of the timeout value
     */
    TimeUnit getTimeUnit();
}
