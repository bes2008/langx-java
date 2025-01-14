package com.jn.langx.io.resource;

import com.jn.langx.Ordered;

import java.util.List;

public interface PatternResourceLoader extends ResourceLoader , Ordered {
    List<Resource> getResources(String locationPattern);

    boolean isSupportedPatttern(String locaionPattern);
}
