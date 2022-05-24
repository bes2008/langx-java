/*
 *
 *  Copyright 2018 FJN Corp.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Author                        Date                       Issue
 *  fs1194361820@163.com          2015-01-01                 Initial Version
 *
 */

package com.jn.langx.management.connector.server.impl;

import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.management.connector.server.JMXConnectorServerWrapper;
public class IIOPJMXConnectorServerWrapper implements JMXConnectorServerWrapper {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void init() throws InitializationException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startup() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {

    }

}
