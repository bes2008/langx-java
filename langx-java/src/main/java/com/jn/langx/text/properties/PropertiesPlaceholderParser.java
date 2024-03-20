package com.jn.langx.text.properties;

import com.jn.langx.text.placeholder.PropertySetPlaceholderParser;

import java.util.Properties;

public class PropertiesPlaceholderParser extends PropertySetPlaceholderParser {

    public PropertiesPlaceholderParser(Properties properties) {
        super(new PropertiesPropertySource(properties));
    }
}
