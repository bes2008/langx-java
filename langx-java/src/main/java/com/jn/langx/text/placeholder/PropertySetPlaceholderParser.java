package com.jn.langx.text.placeholder;

import com.jn.langx.propertyset.PropertySet;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;

public class PropertySetPlaceholderParser implements PlaceholderParser {
    private PropertySet propertySource;

    public PropertySetPlaceholderParser() {

    }

    public PropertySetPlaceholderParser(PropertySet propertySource) {
        setPropertySet(propertySource);
    }

    public PropertySet getPropertySet() {
        return propertySource;
    }

    public void setPropertySet(PropertySet propertySource) {
        Preconditions.checkNotNull(propertySource);
        this.propertySource = propertySource;
    }

    @Override
    public String parse(String variable) {
        return Objs.toStringOrNull(propertySource.getProperty(variable));
    }
}
