package com.jn.langx.management;

import com.jn.langx.configuration.Configuration;

import java.util.Properties;

public class ConnectorConfiguration implements Configuration {
    private String id;
    private Properties props;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public ConnectorConfiguration() {
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
