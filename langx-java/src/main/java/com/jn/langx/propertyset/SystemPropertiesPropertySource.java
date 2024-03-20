package com.jn.langx.propertyset;

import com.jn.langx.util.collection.Collects;

/**
 * @since 5.3.8
 */
public class SystemPropertiesPropertySource extends MapPropertySet {
    public SystemPropertiesPropertySource() {
        this("systemProperties");
    }
    public SystemPropertiesPropertySource(String name) {
        super(name, Collects.propertiesToStringMap(System.getProperties()));
    }

}
