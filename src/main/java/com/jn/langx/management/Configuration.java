package com.jn.langx.management;

import java.util.Properties;

public class Configuration {
    private Properties props;

    public Configuration() {
        this.props = new Properties();
    }

    public String getProperty(final String key) {
        return this.props.getProperty(key);
    }

    public String getProperty(final String key, final String defaultValue) {
        return this.props.getProperty(key, defaultValue);
    }

    public void setProperty(final String key, final String value) {
        this.props.setProperty(key, value);
    }

    public Properties getProperties() {
        return this.props;
    }
}
