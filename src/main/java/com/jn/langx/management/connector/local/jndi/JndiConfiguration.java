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

package com.jn.langx.management.connector.local.jndi;

import com.jn.langx.management.Configuration;

import java.util.List;

public class JndiConfiguration extends Configuration {
    private String serverJndi;
    private String jndiFactoryClass;

    public void setServerJndi(final String serverJndi) {
        this.serverJndi = serverJndi;
    }

    public String getServerJndi() {
        return this.serverJndi;
    }

    public String getJndiFactoryClass() {
        return this.jndiFactoryClass;
    }

    public void setFactoryUrlPackages(final List<String> factoryUrlPackages) {
        if (factoryUrlPackages != null && !factoryUrlPackages.isEmpty()) {
            final StringBuilder builder = new StringBuilder(200);
            for (final String pkg : factoryUrlPackages) {
                builder.append(":").append(pkg);
            }
            if (builder.length() > 1) {
                this.setProperty("java.naming.factory.url.pkgs", builder.substring(1));
            }
        }
    }

    public void setJndiFactoryClass(final String jndiFactoryClass) {
        this.jndiFactoryClass = jndiFactoryClass;
        if (jndiFactoryClass != null && !jndiFactoryClass.isEmpty()) {
            this.setProperty("java.naming.factory.initial", jndiFactoryClass);
        }
    }
}
