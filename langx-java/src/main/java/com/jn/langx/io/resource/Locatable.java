package com.jn.langx.io.resource;

/**
 * 可定位的， location 可以相对的，也可以是绝对的，且是有前缀的
 */
public interface Locatable {
    String getPrefix();
    String getPath();
    Location getLocation();

    String getAbsolutePath();
}
