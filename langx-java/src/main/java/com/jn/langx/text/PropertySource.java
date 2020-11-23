package com.jn.langx.text;

import com.jn.langx.Named;

public interface PropertySource extends Named {
    boolean containsProperty(String name);
    String getProperty(String name);
}
