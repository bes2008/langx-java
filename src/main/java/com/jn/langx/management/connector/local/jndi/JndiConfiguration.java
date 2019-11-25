package com.jn.langx.management.connector.local.jndi;

import com.jn.langx.management.ConnectorConfiguration;

import java.util.List;

public class JndiConfiguration extends ConnectorConfiguration {
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
