package com.jn.langx.text.placeholder;

import com.jn.langx.text.PropertySource;
import com.jn.langx.util.Preconditions;

public class PropertySourcePlaceholderParser implements PlaceholderParser {
    private PropertySource propertySource;

    public PropertySourcePlaceholderParser() {

    }

    public PropertySourcePlaceholderParser(PropertySource propertySource) {
        setPropertySource(propertySource);
    }

    public PropertySource getPropertySource() {
        return propertySource;
    }

    public void setPropertySource(PropertySource propertySource) {
        Preconditions.checkNotNull(propertySource);
        this.propertySource = propertySource;
    }

    @Override
    public String parse(String variable) {
        return propertySource.getProperty(variable);
    }
}
