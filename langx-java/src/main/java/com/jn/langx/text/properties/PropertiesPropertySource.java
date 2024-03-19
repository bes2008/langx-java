package com.jn.langx.text.properties;

import com.jn.langx.propertyset.MapPropertySet;

import java.util.Map;
import java.util.Properties;

@Deprecated
public class PropertiesPropertySource extends MapPropertySet {

    public PropertiesPropertySource(String name, Properties properties) {
        super(name, (Map)properties);
    }

    public PropertiesPropertySource(Properties properties) {
        this("unknown", properties);
    }

}
