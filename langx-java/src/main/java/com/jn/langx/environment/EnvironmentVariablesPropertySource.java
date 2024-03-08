package com.jn.langx.environment;

public class EnvironmentVariablesPropertySource extends MapPropertySet{
    public EnvironmentVariablesPropertySource() {
        super("envVars");
    }

    public EnvironmentVariablesPropertySource(String name) {
        super(name, System.getenv());
    }

}
