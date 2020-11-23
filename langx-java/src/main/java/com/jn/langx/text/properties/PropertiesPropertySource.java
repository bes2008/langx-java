package com.jn.langx.text.properties;

import com.jn.langx.text.PropertySource;

import java.util.Properties;

public class PropertiesPropertySource implements PropertySource {
    private String name = "unknown";
    private Properties properties;

    public PropertiesPropertySource(String name, Properties properties) {
        this(properties);
        setName(name);
    }

    public PropertiesPropertySource(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean containsProperty(String name) {
        return this.properties.containsKey(name);
    }

    @Override
    public String getProperty(String name) {
        return this.properties.getProperty(name);
    }
}
