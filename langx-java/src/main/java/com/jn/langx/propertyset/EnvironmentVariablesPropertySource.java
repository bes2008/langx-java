package com.jn.langx.propertyset;

/**
 * @since 5.3.8
 */
public class EnvironmentVariablesPropertySource extends MapPropertySet {
    public EnvironmentVariablesPropertySource() {
        this(PropertySet.ENV_VARS);
    }

    public EnvironmentVariablesPropertySource(String name) {
        super(name, System.getenv());
    }
}
