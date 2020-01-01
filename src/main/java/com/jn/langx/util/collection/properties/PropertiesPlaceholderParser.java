package com.jn.langx.util.collection.properties;

import com.jn.langx.text.PlaceholderParser;

import java.util.Properties;

public class PropertiesPlaceholderParser implements PlaceholderParser {
    private Properties properties;

    public PropertiesPlaceholderParser(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String parse(String placeholderName) {
        return this.properties != null ? properties.getProperty(placeholderName) : null;
    }
}
