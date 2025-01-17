package com.jn.langx.propertyset;

/**
 * @since 5.3.8
 */
public class EnvironmentVariablesPropertySource extends MapPropertySet {
    public EnvironmentVariablesPropertySource() {
        super("envVars");
    }

    public EnvironmentVariablesPropertySource(String name) {
        super(name, System.getenv());
    }


    @Override
    public boolean containsProperty(String key) {
        return System.getenv().containsKey(key);
    }
}
