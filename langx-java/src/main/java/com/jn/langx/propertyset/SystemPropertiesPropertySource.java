package com.jn.langx.propertyset;

import com.jn.langx.propertyset.MapPropertySet;
import com.jn.langx.util.collection.Collects;


public class SystemPropertiesPropertySource extends MapPropertySet {
    public SystemPropertiesPropertySource() {
        this("systemProperties");
    }
    public SystemPropertiesPropertySource(String name) {
        super(name, Collects.propertiesToStringMap(System.getProperties()));
    }

}
