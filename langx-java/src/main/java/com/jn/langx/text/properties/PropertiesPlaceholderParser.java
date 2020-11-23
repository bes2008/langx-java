package com.jn.langx.text.properties;

import com.jn.langx.text.placeholder.PropertySourcePlaceholderParser;

import java.util.Properties;

public class PropertiesPlaceholderParser extends PropertySourcePlaceholderParser {

    public PropertiesPlaceholderParser(Properties properties) {
        super(new PropertiesPropertySource(properties));
    }
}
