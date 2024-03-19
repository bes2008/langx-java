package com.jn.langx.propertyset;

import com.jn.langx.text.PropertySource;

public interface PropertySet<SRC> extends PropertySource {
    SRC getSource();
}
