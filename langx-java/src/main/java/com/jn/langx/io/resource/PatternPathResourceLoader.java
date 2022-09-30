package com.jn.langx.io.resource;

import java.util.List;

public interface PatternPathResourceLoader extends ResourceLoader {
    List<Resource> getResources(String locationPattern);
}
