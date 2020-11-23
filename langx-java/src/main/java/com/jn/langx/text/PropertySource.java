package com.jn.langx.text;

public interface PropertySource {
    boolean containsProperty(String name);
    String getProperty(String name);
}
