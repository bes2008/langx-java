package com.jn.langx.propertyset;

import com.jn.langx.text.PropertySource;

/**
 * @since 5.3.8
 */
public interface PropertySet<SRC> extends PropertySource {
    SRC getSource();
}
