package com.jn.langx.propertyset;

import com.jn.langx.propertyset.MapPropertySet;

public class EnvironmentVariablesPropertySource extends MapPropertySet {
    public EnvironmentVariablesPropertySource() {
        super("envVars");
    }

    public EnvironmentVariablesPropertySource(String name) {
        super(name, System.getenv());
    }

}
